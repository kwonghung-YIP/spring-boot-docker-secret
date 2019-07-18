# Introduction

This demo shows how to load the docker secrets into spring boot environment properties, it prevents the password property such as **spring.datasource.password** being exposed as plain text in your application.yml file.

# "spring.datasource.password" in application.yml 

The **spring.datasource.password** property in our demo refers to other property **${docker-secret-mysql-user-pw}**, which is preload by our EnvironmentPostProcessor implementation. The prefix **docker-secret-** helps to identify that the property is loaded from docker secret, and **mysql-user-pw** is the filename bind within the docker container under the **/run/secrets** folder.

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

The spring-boot service bind the secret **mysql-user-pw** which store the password of user **dba**.

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
    image: [kwonghung/spring-boot-docker-secret:latest](https://cloud.docker.com/u/kwonghung/repository/docker/kwonghung/spring-boot-docker-secret)
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

The [DockerSecretProcessor](/src/main/java/hung/org/DockerSecretProcessor.java) implements the [EnvironmentPostProcessor](https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/htmlsingle/#howto-customize-the-environment-or-application-context) interface, it loads all the files under the **/run/secrets** folder as environment properties.

For this demo, the docker secret **my-user-pw** is bind as file **/run/secrets/mysql-user-pw"** in the docker image, which is loaded as the spring boot property **docker-secret-mysql-user-pw**.

# META-INF/spring.factories

And you have to declare your EnvironmentPostProcessor class in META-INF/spring.factories file

```properties
org.springframework.boot.env.EnvironmentPostProcessor=hung.org.DockerSecretProcessor
```

# Run the demo as docker stack

1. Get the docker swarm ready, or initiate a new swarm.

```bash
docker swarm init
```

2. Define the docker secret in the manager node.

```bash
printf "password"|docker secret create mysql-user-pw -
```

4. Download the docker-compose.yml.

```bash
wget -qO- --no-cache https://raw.githubusercontent.com/kwonghung-YIP/spring-boot-docker-secret/master/docker-compose.yml
```

5. Start the docker stack.

```bash
docker stack deploy --compose-file docker-compose.yml demo
```

6. Try the link.

```bash
curl http://localhost:8080/
```
