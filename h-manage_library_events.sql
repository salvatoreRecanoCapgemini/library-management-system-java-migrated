

import psycopg2
import json

def manage_library_events(p_action, p_event_id, p_event_data):
    conn = psycopg2.connect(
        host="your_host",
        database="your_database",
        user="your_username",
        password="your_password"
    )
    cur = conn.cursor()

    if p_action == 'CANCEL_EVENT':
        cur.execute("""
            CREATE TEMP TABLE affected_registrants AS
            SELECT 
                er.registration_id,
                p.email,
                p.first_name,
                le.title as event_title,
                le.event_date
            FROM event_registrations er
            JOIN patrons p ON er.patron_id = p.patron_id
            JOIN library_events le ON er.event_id = le.event_id
            WHERE le.event_id = %s
            AND er.attendance_status = 'REGISTERED';
        """, (p_event_id,))

        cur.execute("""
            UPDATE library_events
            SET status = 'CANCELLED'
            WHERE event_id = %s;
        """, (p_event_id,))

        cur.execute("""
            UPDATE event_registrations
            SET attendance_status = 'NO_SHOW'
            WHERE event_id = %s;
        """, (p_event_id,))

        cur.execute("SELECT * FROM affected_registrants;")
        registrants = cur.fetchall()
        for registrant in registrants:
            cur.execute("""
                INSERT INTO audit_log (
                    table_name,
                    record_id,
                    action_type,
                    action_timestamp,
                    new_values
                ) VALUES (
                    'event_notifications',
                    %s,
                    'INSERT',
                    CURRENT_TIMESTAMP,
                    %s
                );
            """, (registrant[0], json.dumps({
                'email': registrant[1],
                'message': f"Event \"{registrant[3]}\" scheduled for {registrant[4]} has been cancelled."
            })))

        cur.execute("DROP TABLE affected_registrants;")

    elif p_action == 'RESCHEDULE_EVENT':
        if 'new_date' not in p_event_data:
            raise Exception('New date must be provided for rescheduling')

        cur.execute("""
            CREATE TEMP TABLE schedule_conflicts AS
            SELECT 
                er.patron_id,
                p.email,
                p.first_name
            FROM event_registrations er
            JOIN patrons p ON er.patron_id = p.patron_id
            JOIN library_events le ON er.event_id = le.event_id
            WHERE er.event_id != %s
            AND le.event_date = %s;
        """, (p_event_id, p_event_data['new_date']))

        cur.execute("""
            UPDATE library_events
            SET event_date = %s
            WHERE event_id = %s
            RETURNING *;
        """, (p_event_data['new_date'], p_event_id))
        event_record = cur.fetchone()

        cur.execute("SELECT * FROM schedule_conflicts;")
        conflicts = cur.fetchall()
        for conflict in conflicts:
            cur.execute("""
                INSERT INTO audit_log (
                    table_name,
                    record_id,
                    action_type,
                    action_timestamp,
                    new_values
                ) VALUES (
                    'schedule_conflicts',
                    %s,
                    'INSERT',
                    CURRENT_TIMESTAMP,
                    %s
                );
            """, (conflict[0], json.dumps({
                'email': conflict[1],
                'message': f"Event \"{event_record[1]}\" has been rescheduled to {event_record[2]}. You have a scheduling conflict."
            })))

        cur.execute("DROP TABLE schedule_conflicts;")

    else:
        raise Exception('Invalid action specified')

    conn.commit()
    cur.close()
    conn.close()