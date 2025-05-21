

import psycopg2

def process_book_return(p_loan_id):
    conn = psycopg2.connect(
        host="your_host",
        database="your_database",
        user="your_username",
        password="your_password"
    )
    cur = conn.cursor()
    cur.execute("""
        SELECT book_id, patron_id, due_date 
        FROM loans 
        WHERE loan_id = %s AND status = 'ACTIVE'
    """, (p_loan_id,))
    row = cur.fetchone()
    if row is None:
        raise Exception('Active loan not found')
    v_book_id, v_patron_id, v_due_date = row
    
    cur.execute("SELECT CURRENT_DATE")
    current_date = cur.fetchone()[0]
    v_days_overdue = (current_date - v_due_date).days
    if v_days_overdue > 0:
        v_fine_amount = v_days_overdue * 0.50
        cur.execute("""
            INSERT INTO fines (
                patron_id, loan_id, amount, issue_date, due_date, status
            ) VALUES (
                %s,
                %s,
                %s,
                CURRENT_DATE,
                CURRENT_DATE + INTERVAL '30 days',
                'PENDING'
            )
        """, (v_patron_id, p_loan_id, v_fine_amount))
    
    cur.execute("""
        UPDATE loans 
        SET status = 'RETURNED',
            return_date = CURRENT_DATE
        WHERE loan_id = %s
    """, (p_loan_id,))
    
    cur.execute("CALL update_book_availability(%s, 1)", (v_book_id,))
    conn.commit()
    cur.close()
    conn.close()