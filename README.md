# Welcome to Grocery Express

## Note: please run all scripts/commands from the project root directory

## Prerequisites:

1. Docker installed
   Use `docker --version` to check whether Docker has been installed.
2. Maven installed
   Use `mvn -version` to check whether Maven has been installed.

### To Install Docker go to:

```
https://docs.docker.com/get-docker/
```

### To Install Maven go to:

```
https://maven.apache.org/download.cgi
```

## Compile project

```shell script
mvn clean package
```

- This step compiles the project using Maven build system by running the command "mvn clean package". The "clean" command deletes any existing build artifacts and the "package" command compiles the project and generates a JAR package, that can be used to run the application.

## Build docker images

```shell script
sh docker_build.sh --platform linux/arm64/v8/amd64
```

- This step builds Docker images for the application using the script "docker_build.sh". The script contains instructions for building the images, including specifying the base image to use, copying the application files into the image, and setting environment variables.

## Run database

```shell script
docker-compose up -d mysql
```

- This step runs a MySQL database instance as a Docker container. The command starts the MySQL container and runs it as a background process.
- Please note that if you have not previously installed the Docker image for MySQL, Docker will automatically download the MySQL image from Docker Hub before running the "docker-compose up -d mysql" command. The download time of the image depends on your network speed and the size of the image to be downloaded.

## Run service

```shell script
docker-compose up -d
```

- This step runs the application as a Docker container using the command "docker-compose up -d". The command starts all the containers defined in the Docker Compose file, which means that the containers run in the background.

## Run client

```shell script
java -jar ./grocery-express-client/target/client-0.0.1-SNAPSHOT.jar http://localhost:80
```

- The default URL for requests is http://localhost:8080, but you can specify a custom request URL by providing it as a parameter when running the client JAR.
- This is a command to run the client JAR file located at "./grocery-express-client/target/client-0.0.1-SNAPSHOT.jar" with a custom request URL specified as a parameter. The custom request URL in this case is "http://localhost:80".

## Remove service

```shell script
docker-compose down
```

- This step stops and removes the containers created by the previous step using the command "docker-compose down". The command stops the containers and removes any networks defined in the Docker Compose file.

## Improvements

### Summary

This system adopts the "Client-Server" architecture pattern of front-end and back-end separation. The front-end is a CLI application program written in Java language, and the back-end uses the Spring Boot framework. Additionally, Maven is used for automating the build and managing the dependencies of the Java project, simplifying the development, build, and deployment process. As for the database, we use the Hibernate framework to manage the object mapping of MySQL database, which simplifies the development of data access layer and efficiently handles data operations.

### Scalability

- **Database optimization**. First, we carefully designed and improved the table structure of the database. Then, we used the Hibernate ORM framework to manage the mapping between objects and the database, further improving the efficiency of data processing. Finally, we added indexes to reduce the database load and avoid system bottlenecks, resulting in faster read and write operations. For example, we added two indexes, `customer_id` and `store_id`, to the `coupon` table, an index for `store_id` to the `fueling_station` table, and two indexes, `pilot_id` and `store_id`, to the `drone` table.

- **Load balancing architecture**. We implemented load balancing through Docker Compose and Nginx. The approach involved deploying customer and store microservices in Docker containers, adjusting the scale of Docker container instances, and using Nginx to perform traffic forwarding and load balancing. Details can be found in the `docker-compose.yml` and `nginx.conf` code.

- **Microservices architecture**. We split the backend services into two parts, customer and store, and implemented microservices through deployment in the `docker-compose.yml`. This is evident from our code structure.


