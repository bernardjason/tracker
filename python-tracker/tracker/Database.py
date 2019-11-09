import sqlite3
from sqlite3 import Error


def create_connection(db_file):
    conn = None
    try:
        conn = sqlite3.connect(db_file)
        return conn
    except Error as e:
        print(e)

    return conn


def create_table(conn, create_table_sql):
    try:
        c = conn.cursor()
        c.execute(create_table_sql)
    except Error as e:
        print(e)

def create_task(conn, ticket):


    sql = ''' INSERT INTO tickets(ticketId,ticketUserId,ticketName,ticketCreated,ticketDetails)
              VALUES(?,?,?,?,?) '''
    cur = conn.cursor()
    cur.execute(sql, ticket)
    return cur.lastrowid


def main():
    database = r"pythonsqlite.db"

    sql_create_tickets_table = """ CREATE TABLE IF NOT EXISTS tickets (
                                        ticketId integer PRIMARY KEY,
                                        ticketUserId integer ,
                                        ticketName text,
                                        ticketCreated integer,
                                        ticketDetails text
                                    ); """

    conn = create_connection(database)

    if conn is not None:
        create_table(conn, sql_create_tickets_table)

    else:
        print("error no connection")

    t1 = ( 2 , 2 , "FRED", "1572682316" , "first time")
    create_task(conn,t1)
    conn.commit()

if __name__ == '__main__':
    main()
