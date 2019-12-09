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

#### Setting up of system variables

Set the following system variables: 
`DB_URL=jdbc:mysql://SERVICE_NAME:PORT/DB_NAME`,
`DB_USER=YOUR_USERNAME`, 
`DB_PASSWORD=YOUR_PASSWORD`,
`DB_ROOT_PASSWORD=YOUR_ROOT_PASSWORD`;

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
4) Stop Tomcat server. On Windows run file `shutdown.bat`

#### Deploying with Docker

2. Start docker containers by docker-compose:
`
docker-compose up -d
`
3. Go to the following `URL`:
`
http://localhost:8080/WAR_FILE_NAME/users
`
You will see the list of users.

> To stop application type in cmd: `docker-compose stop`.