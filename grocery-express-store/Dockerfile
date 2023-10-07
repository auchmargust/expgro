FROM openjdk:11

WORKDIR /
COPY ./target/store-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java","-Dfile.encoding=UTF-8", "-jar", "/store-0.0.1-SNAPSHOT.jar"]
EXPOSE 8089
