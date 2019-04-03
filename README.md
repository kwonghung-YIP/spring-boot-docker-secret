# Introduction

This demo shows how spring boot loading the password property from docker secret, instead of hard coding it in a yaml or properties file. The idea is to load the password stored in docker secert as the properties, then other properties such as "spring.datasource.password" can refer to it.

# "spring.datasource.password" in application.yml 

```yaml
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/testdb
    username: dba
    password: ${docker-secret-mysql-user-pw}

# The "docker-secret.bind-path" property trigger the EnvironmentPostProcessor to load 
# the bind docker secrets as password property
docker-secret:
  bind-path: /run/secrets
```

# docker-compose.yml
```yml
version: '3.7'

services:
  mysql:
    image: mysql:8
    environment:
      - MYSQL_ROOT_PASSWORD=yes
      - MYSQL_DATABASE=testdb
      - MYSQL_USER=dba
      - MYSQL_PASSWORD_FILE=/run/secrets/mysql-user-pw
    secrets:
      - mysql-user-pw

  spring-boot:
    image: kwonghung/spring-boot-docker-secret:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker-stack
    secrets:
      - mysql-user-pw

secrets:
  mysql-user-pw:
    external: true
```

# EnvironmentPostProcessor Implementation

The DockerSecretProcessor implements the EnvironmentPostProcessor interface

# META-INF/spring.factories

```properties
org.springframework.boot.env.EnvironmentPostProcessor=hung.org.DockerSecretProcessor
```

# Run the demo as docker stack

```bash
docker swarm init

printf "password"|docker secret create mysql-user-pw -

wget -qO- --no-cache https://raw.githubusercontent.com/kwonghung-YIP/spring-boot-docker-secret/master/docker-compose.yml

docker stack deploy --compose-file docker-compose.yml demo
```
