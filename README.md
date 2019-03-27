# Introduction

This demo shows how spring boot loading the password property from docker secret, instead of hard coding it in a yaml or properties file. The idea is to load the password stored in docker secert as the properties, then other properties such as "spring.datasource.password" can refer to it.

# Sample application.yml 

```yaml
spring:
  profiles: docker-stack
  
  datasource:
    url: jdbc:mysql://mysql:3306/testdb
    username: dba
    password: ${docker-secret-mysql-user-pw}
    
docker-secret:
  bind-path: /run/secrets
```

# Run the demo

```bash
docker swarm init

printf "password"|docker secret create mysql-user-pw -

wget -qO- --no-cache https://raw.githubusercontent.com/kwonghung-YIP/spring-boot-docker-secret/master/docker-compose.yml | docker stack deploy --compose-file - demo
```
