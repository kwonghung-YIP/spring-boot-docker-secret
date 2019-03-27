# Introduction

This demo shows how spring boot loading the password property from docker secret, instead of hard coding it in a yaml or properties file. The idea is to load the password stored in docker secert as the properties, then other properties such as "spring.datasource.password" can refer to it.

# Sample application.yml 

```yaml
spring:
  datasource:
    password: ${docker-secret-mydb-password}
```
# Define docker service and secret

```
docker secret
docker service create \
  --secret
```

# EnvironmentPostProcessor implementation

```spring
```

# Run the demo

```bash
printf "password"|docker secret create mysql-user-pw -
```

```bash
wget -qO- --no-cache https://raw.githubusercontent.com/kwonghung-YIP/spring-boot-docker-secret/master/docker-compose.yml | docker stack deploy --compose-file - demo
```
