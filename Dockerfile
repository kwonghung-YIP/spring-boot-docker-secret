FROM gradle:5.2-jdk8
LABEL maintainer="kwonghung.yip@gmail.com"

COPY --chown=gradle:gradle . .

RUN chmod u+x gradlew 

RUN ./gradlew build -x test

#CMD ["./gradlew", "bootRun"]
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","build/libs/spring-boot-and-docker-secret-0.0.1-SNAPSHOT.jar"]