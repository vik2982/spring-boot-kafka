# Spring boot with kafka example

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
In the application console you will see consumers in both groups (group_id1 and group_id2) have consumed the message<br/>

### Test the filter consumer:
```
curl --location --request POST 'http://localhost:9001/kafka/publish?message=Hello world'
```
In the application console you will see the message is only consumed by consumer group_id1 <br/>

### Test custom object
```
curl --location --request POST 'http://localhost:9001/kafka/publishCustom?message=Hello&name=david'
```
The greeting message is produced/consumed to/from topic2