# Restful Application on Servlet API 2.5
## Information
This project was done to realize how java servlets work. The entity `User` is included in it and simple basic CRUD methods are realized for it.
There were realized 2 connection pools for connecting to database: HikariCP and TomcatCP.
HikariCP was configured for Hibernate.
TomcatCP was configured for establishing connection for PetRepository.

## Requirements
* Java 1.8 or later
* Maven v3.6.2
* Tomcat 9.0.27
* Docker

## Used Technologies
* Servlet API
* JPA, Hibernate
* Validation API

## Deployment

#### * Clone git repository

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
#### * Deployment with Docker
* Set the following system variables for docker:
1) Open tomcat_template.env and db_template.env and fill variables with your values
2) Rename accordingly this files as `tomcat.env` and `db.env` or in `docker-compose.yml` rename .env-files as
tomcat_template.db and db_template.env
* Check `src/main/resources/META-INF/services/com.leverx.core.loader.DBCredentialsLoader` file. It should contain the following:
`com.leverx.core.loader.environmentVariables.DBEnvironmentVariableLoader`. If not rewrite it.
* Start docker containers by docker-compose:
`
docker-compose up -d
`
*  Go to the following `URL`:
`
http://localhost:8080/WAR_FILE_NAME/users
`
You will see the list of users.

Stop docker containers with command
 ```
 docker-compose stop
```
#### * Local Deployment:
* Copy war file you have just created to `CATALINA_HOME/webapps`, e.g.
`C:/Tomcat9/webapps`
* Check `src/main/resources/META-INF/services/com.leverx.core.loader.DBCredentialsLoader` file. It should contain the following:
`com.leverx.core.loader.properties.DBPropertiesLoader`. If not rewrite it.
* Start Tomcat server. On Windows run as Administrator file `startup.bat`
* Write the following Url: `https://localhost/YOUR_WAR_FILE_NAME/users`
Response will contain the list of already stored users
* Stop Tomcat server. On Windows run file `shutdown.bat`