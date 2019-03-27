# spring-boot-and-docker-secret

```bash
printf "password"|docker secret create mysql-user-pw -
```

```bash
wget -qO- --no-cache https://raw.githubusercontent.com/kwonghung-YIP/spring-boot-docker-secret/master/docker-compose.yml | docker stack deploy --compose-file - demo
```
