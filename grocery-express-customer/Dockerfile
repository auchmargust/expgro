FROM openjdk:11

WORKDIR /
COPY ./target/customer-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java","-Dfile.encoding=UTF-8", "-jar", "/customer-0.0.1-SNAPSHOT.jar"]
EXPOSE 8088
