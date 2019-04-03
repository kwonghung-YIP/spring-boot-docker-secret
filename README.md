# Introduction

This demo shows how spring boot loading the password property from docker secret, instead of hard coding it in a yaml or properties file. The idea is to load the password stored in docker secert as the properties, then other properties such as "spring.datasource.password" can refer to it.

# "spring.datasource.password" is no more a plain text password in application.yml 

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

The [DockerSecretProcessor](/src/main/java/hung/org/DockerSecretProcessor.java) implements the [EnvironmentPostProcessor](https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/htmlsingle/#howto-customize-the-environment-or-application-context) interface

# META-INF/spring.factories

And you have to declare your EnvironmentPostProcessor class in META-INF/spring.factories file

```properties
org.springframework.boot.env.EnvironmentPostProcessor=hung.org.DockerSecretProcessor
```

# Run the demo as docker stack

```bash
docker swarm init
```
```bash
printf "password"|docker secret create mysql-user-pw -
```
```bash
wget -qO- --no-cache https://raw.githubusercontent.com/kwonghung-YIP/spring-boot-docker-secret/master/docker-compose.yml
```
```bash
docker stack deploy --compose-file docker-compose.yml demo
```
