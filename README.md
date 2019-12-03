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
* Servlet API
* JPA, Hibernate
* Validation API

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
#### * Run project by Docker
* Build `war` file with Maven:
```
mvn clean install
```
* Run docker container with MYSQL database
```
docker-compose up
```
* Go to the Url:
`
http://localhost:8080/application/users
`
#### * Local Deployment:
1) Copy war file you have just created to `CATALINA_HOME/webapps`, e.g.
`C:/Tomcat9/webapps`
2) Start Tomcat server. On Windows run as Administrator file `startup.bat`
3) Write the following Url: `https://localhost/YOUR_WAR_FILE_NAME/users`
Response will contain the list of already stored users 