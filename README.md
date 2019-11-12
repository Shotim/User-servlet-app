# Restful Application on Servlet API 4.0.1
## Information
This project was done to realize how java servlets work. The entity `User` is included in it and simple basic CRUD methods are realized for it.

## Requirements
* Java 1.8 or later
* IDE (Intellij Idea will be more suitable)
* Maven v3.6.2
* Tomcat 9.0.27
* Docker

## Used Technologies
* Servlet API 4.0.1

## Deployment

#### * Clone git repository `https://github.com/Shotim/User-servlet-app`


#### * Build project:
```
mvn clean install
```

>If the project will be built result in the command line will be the following:
```
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
#### * Run project
* Run docker container with MYSQL database
```
docker-compose up
```
* Configure Tomcat for build and run app
* Send GET request `/users` to see the list of already stored users