import json
import time

import rejson

import Database

redis_host = "localhost"
redis_port = 6379
redis_password = ""

def connect():
    global red;
    red = rejson.Client(host='localhost', port=6379, decode_responses=True)


connect()

database = r"pythonsqlite.db"

sql_create_tickets_table = """ CREATE TABLE IF NOT EXISTS tickets (
                                        ticketId integer PRIMARY KEY,
                                        ticketUserId integer ,
                                        ticketName text,
                                        ticketCreated integer,
                                        ticketDetails text
                                    ); """

dbconn = Database.create_connection(database)

if dbconn is not None:
    Database.create_table(dbconn, sql_create_tickets_table)

else:
    print("Error! cannot create the database connection.")
    exit(0)


def pop():
    global dbconn
    j = red.rpop("updatequeue")
    r=False
    if ( j is not None ) :
        t=json.loads(j)
        print(t)
        t1 = ( t.get("ticketId") , t.get("ticketUserId"),t.get("ticketName"), int(t.get("ticketCreated"))/1000,t.get("ticketDetails"))
        Database.create_task(dbconn,t1)
        dbconn.commit()
        r=True
    return r

"""
select ticketId,ticketUserId,ticketName, datetime(ticketCreated,'unixepoch') ,ticketDetails from tickets;
"""
while True:
    r=pop()
    if r is False :
        time.sleep(5)
