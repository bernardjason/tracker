FROM python:3

WORKDIR /home/app

RUN apt-get update
RUN apt-get install -y libsqlite3-dev
RUN apt-get install -y sqlite3


ADD tracker /home/app/tracker

RUN pip3 install six
RUN pip3 install redis
RUN pip3 install rejson

RUN find /home/app
RUN mkdir /home/app/db

VOLUME ["/home/app/db"]

ENTRYPOINT [ "python3", "tracker/TrackerReader.py" ]
