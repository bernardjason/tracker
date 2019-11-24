# tracker

Based on code from
https://spring.io/blog/2015/07/14/microservices-with-spring


Browser application that calls a Play framework scala application that is stateless.

Session state is maintained in a cookie. See https://www.playframework.com/documentation/2.7.x/SettingsSession

So multiple docker instances of play-tracker and be stateless

Rest services exposed on docker instances spring-edge-tracker. Again session state maintained in Redis database. So multiple instances can run

There's a simple docker instance of Redis in the swarm

Finally there's a Python backend reading from a Redis queue of closed tickets writing to a sqlite database. This is of course single instance.

To build
```
docker build -t "bernardjason/play-tracker" play-tracker
docker build -t "bernardjason/python-tracker" python-tracker
docker build -t "bernardjason/spring-edge-tracker" spring-edge-tracker
docker build -t "bernardjason/spring-eureka" spring-eureka
```

to deploy
```
docker swarm init
docker stack deploy --compose-file docker-stack.yml tracker
```

clean up
```
docker stack rm tracker
```

whats going on, logon to container
```
docker ps
```

CONTAINER ID        IMAGE                                        COMMAND                  CREATED             STATUS              PORTS                NAMES
6663ca49e98d        bernardjason/spring-edge-controller:latest   "start -u http://spr…"   4 minutes ago       Up 4 minutes        3333/tcp, 3344/tcp   **tracker_spring-edge-controller.1.wwuggjgvew130sjt62hq3z1mh**

```
docker exec -ti **tracker_spring-edge-controller.1.wwuggjgvew130sjt62hq3z1mh** /bin/bash
```

update a container
```
docker stack services tracker
docker service update --force q6o9n7n4i9bz
```


to create a login to the tracker for the first time
```
cd spring-edge-tracker
./createuser jasonb bernie
```

http://127.0.0.1:9000


