

import psycopg2

def process_fine_payment(p_fine_id, p_amount_paid):
    conn = psycopg2.connect(
        host="your_host",
        database="your_database",
        user="your_username",
        password="your_password"
    )
    cur = conn.cursor()
    cur.execute("""
        SELECT amount
        FROM fines 
        WHERE fine_id = %s AND status = 'PENDING'
    """, (p_fine_id,))
    v_total_amount = cur.fetchone()
    if v_total_amount is None:
        raise Exception('Valid unpaid fine not found')
    v_total_amount = v_total_amount[0]
    if p_amount_paid < v_total_amount:
        v_remaining_amount = v_total_amount - p_amount_paid
        raise Exception('Partial payments not supported')
    cur.execute("""
        UPDATE fines 
        SET status = 'PAID',
            payment_date = CURRENT_DATE
        WHERE fine_id = %s
    """, (p_fine_id,))
    conn.commit()
    cur.close()
    conn.close()