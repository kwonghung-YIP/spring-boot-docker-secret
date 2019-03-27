# spring-boot-and-docker-secret

echo "password"|docker secret create mysql-user-pw -

wget -qO- --no-cache https://raw.githubusercontent.com/kwonghung-YIP/spring-boot-and-docker-secret/master/docker-compose.yml | docker stack deploy --compose-file - demo
