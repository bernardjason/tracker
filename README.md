# tracker

docker build -t "bernardjason/play-tracker" play-tracker
docker build -t "bernardjason/python-tracker" python-tracker
docker build -t "bernardjason/spring-edge-tracker" spring-edge-tracker
docker build -t "bernardjason/spring-eureka" spring-eureka

docker swarm init

docker stack deploy --compose-file docker-stack.yml tracker

docker stack rm tracker


docker ps
docker exec -ti XXXX /bin/bash

docker stack services tracker
docker service update --force q6o9n7n4i9bz


createuser jasonb bernie


http://127.0.0.1:9000
