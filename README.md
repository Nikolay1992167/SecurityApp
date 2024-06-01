# User Service

The service that is responsible for creating, storing data and authorizing users

### Technologies that I used on the project:

* Java 17
* Gradle 8.1.1
* Lombok plugin 8.4
* Postgresql 42.6
* Spring-boot 3.2.3
* Spring-boot-starter-data-validation
* Spring-boot-starter-data-jpa
* Spring-boot-starter-web
* Spring-boot-starter-security
* Spring-boot-configuration-processor
* Springdoc-openapi-starter-webmvc-ui 2.1.0
* Spring-boot-starter-data-redis
* Mapstruct 1.5.3.Final
* JpaModelGen
* Jsonwebtoken-jjwt-api 0.11.5
* Jsonwebtoken-jjwt-impl 0.11.5
* Jsonwebtoken-jjwt-jackson 0.11.5
* Liquibase
* Spring-boot-starter-test
* Spring-security-test
* Spring-cloud-starter-contract-stub-runner
* Testcontainers-Postgresql 1.19.3

### Instructions to run application (dev profile):

1. You must have [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
   [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/),
   [Postgresql](https://www.postgresql.org/download/) and [Redis](https://redis.io/) installed
   (P.S.: You can deploy postgresql and redis in a [DockerPostgres](https://hub.docker.com/_/postgres) and
   [DockerRedis](https://hub.docker.com/_/redis) container).
2. You need [Docker](https://www.docker.com/products/docker-desktop/) for integration tests with testcontainers.
3. In [application.yaml](user-service/src/main/resources/application.yml) enter dev in line №3.
4. You need to check all the data in the files [application.yaml](user-service/src/main/resources/application.yml), 
[application-dev.yaml](user-service/src/main/resources/application-dev.yml) and enter your values.
5. Application is ready to work.

### Unit tests

1. Tests have been written with 100% coverage of services and controllers. 
2. Integration tests for controllers and services with testcontainers are also written.
3. You can run the tests for this project, by at the root of the project executing:

```
./gradlew test
```

### Documentation

To view the API Swagger documentation, start the application and see: 

* [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)