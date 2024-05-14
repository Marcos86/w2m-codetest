# spacecraft


## Prequisites
What is needed to set up the dev environment:
openjdk version "21.0.3" 2024-04-16 LTS
Apache Maven 3.9.6


## Tech
These are principal framework/libraries used:

| Framework-library | Version     |
| ----------------- |-------------|
| Java              | 21          |
| Spring boot       | 3.2.5       |
| Maven             | 3.9.6       |
| Lombok            | 1.18.32     |
| Mapstruct         | 1.5.5.Final |
| JUnit             | 5           |


### Api first
This microservice has been built using the api first philosophy.
In file ../spacecraft/doc/openapi/openapi-rest.yml first of all microservice contract has been defined.
In swagger editor page: https://editor.swagger.io/ you can show this.
In file ../spacecraft/doc/postman/spacecrafts.postman_collection.json you have examples to test api with postman.

### Architecture
Hexagonal architecture
- domain: domain objects and ports.
- application: with business usecases.
- infrastructure: adapters which implements all ports definition.
  One maven module to be really decoupled from each one and mappers to convert infrastructure to domain objects or vice-versa.

### Application configuration
In you need take a look or set microservice configuration you can see the file:
../spacecraft/spacecraft-infrastructure/src/main/resources/application.yml

### Compile
Using root folder:
```sh
mvn clean install -U
```

### Launch application
In your watch lo launch the application from local use this steps
(if you prefer use dockerized one go to section Dockerize app)
Using root folder:
```sh
java -jar spacecraft-infrastructure/target/spacecraft-infrastructure-0.0.1-SNAPSHOT.jar
```
You can do a simple curl to test:
```sh
curl --location 'http://localhost:8080/spacecrafts/1'
```
This is expected response:
```sh
{"timestamp":...,"error":{"code":"1","message":"Spacecraft selected with id: 1 not found"}}
```
App is running, then you can make http request with you favourite http client
(previously you can see a postman file with api examples).

### Tests
- JUnit testing everywhere :)

- Integration tests codified in SpacecraftControllerIntegrationTest.

You can run this with command to execute everyone:
```sh
mvn test
```

### Api first
- This microservice has been built using the api first philosophy.
- In file ../spacecraft/doc/openapi/openapi-rest.yml first of all microservice contract has been defined.
- In swagger editor page: https://editor.swagger.io/ you can show this.

- Postman: in file ../spacecraft/doc/postman/spacecrafts.postman_collection.json you have examples to test api with postman.

### Manage future DDL scripts
- Idea: Library to manage database scripts: liquibase o flyway.
Here you have an interesting comparasion:
https://www.bytebase.com/blog/flyway-vs-liquibase/


### Securize api
I think the most elegant way to secure an api would be to have an endpoint to which we include a username and password 
as the body of the request and whose response would be a JWT token.
Each call to the api should have this token as a header.
Internally, the application will decrypt this token and check if the person making the request has the correct token 
as well as the appropriate permissions. If it has them, it will be able to continue with the operation and the endpoint
that is being invoked will perform the relevant operation. However, if it does not have permissions, it will return an 
Unauthorized (401).

This would be an example:
https://medium.com/@bhushannemade2001/spring-boot-3-1-jwt-authentication-authorization-secure-your-apis-like-a-pro-4873e507db4d

Translated with DeepL.com (free version)

### Dockerize app
Having docker previously installed, using root folder do following commands and :

1.Compile application:
```sh
mvn clean install -U
```

2.Build docker image app:
```sh
docker build --tag=spacecraft-app:latest .
```

3.Run your container:
```sh
docker run -p 8081:8080 spacecraft-app:latest
```
NOTE: Is mandatory to be available port 8081 in your computer, please change this if not.

4.You can do a simple curl to test:
```sh
curl --location 'http://localhost:8081/spacecrafts/1'
```
This is the expected response:
```sh
{"timestamp":...,"error":{"code":"1","message":"Spacecraft selected with id: 1 not found"}}
```

If you see this, now your spring application is dockerized and running :)
