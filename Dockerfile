FROM openjdk:16
ADD src/target/demo-0.0.1-SNAPSHOT.jar .
EXPOSE 8000
CMD java -jar demo-0.0.1-SNAPSHOT.jar