# Event Driven Design Patterns implemented with Spring Boot

This project demonstrates the following event driven architecture patterns:
* **Communication** - Publish and subscribe
* **Consumer scalability** - Consumer groups and partitioning
* **Consumption** - Event filtering
* **Error handling** - Retry and dead letter topic

## Prerequisites

* JDK 24
* Maven Latest version
* Latest Docker installation

## How to Run

To start kafka: 
```
docker-compose up -d
```
To start microservice:
```
mvn spring-boot:run
```

## How to Test

### Test simple String message:
```
curl --location --request POST 'http://localhost:9001/kafka/publish?message=Hello jon'
```
In the application console you will see consumers in group_id1/2 have consumed the message<br/>

### Test the filter consumer:
```
curl --location --request POST 'http://localhost:9001/kafka/publish?message=Hello world'
```
In the application console you will see the message is not consumed by consumer group_id2 <br/>

### Test retry logic:
```
curl --location --request POST 'http://localhost:9001/kafka/publish?message=Hello retry'
```
You can see in the console the logic is retried as configured for group_id3 and on exhaustion the dlt handler is invoked

### Test custom object
```
curl --location --request POST 'http://localhost:9001/kafka/publishCustom?message=Hello&name=david'
```
The greeting message is produced/consumed to/from topic2