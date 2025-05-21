

import psycopg2
import json

def manage_program_lifecycle(program_id, action, params=None):
    conn = psycopg2.connect(
        host="your_host",
        database="your_database",
        user="your_username",
        password="your_password"
    )
    cur = conn.cursor()
    cur.callproc('manage_program_lifecycle', [program_id, action, json.dumps(params) if params else None])
    conn.commit()
    cur.close()
    conn.close()

# Example usage
manage_program_lifecycle(1, 'START_PROGRAM')