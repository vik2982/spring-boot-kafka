# Event Driven Design Patterns implemented with Spring Boot

This project demonstrates the following event driven architecture patterns:
* **Communication** - Publish and subscribe
* **Consumer scalability** - Consumer groups and partitioning
* **Consumption** - Event filtering
* **Error handling** - Retry and dead letter topic

## Prerequisites

* JDK 25
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

## Create Kafka topic partitions

If we run the kafka describe command on the topics which have been created by spring boot we will see only one partition has been created by default  for each topic. 
We can create topic1 with multiple partitions as follows (stop the microservice first):
```
docker exec -it container_id /bin/bash
cd /opt/kafka/bin 
./kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic topic1
./kafka-topics.sh --bootstrap-server localhost:9092 --topic topic1 --create --partitions 3 --replication-factor 1
./kafka-topics.sh --bootstrap-server localhost:9092 --list
./kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic topic1 
```

## How to Test

### Test simple String message:
```
curl --location --request POST 'http://localhost:9001/kafka/publish?message=Hello%20jon'
```
In the application console you will see consumers in group_id1/2 have consumed the message<br/>

### Test the filter consumer:
```
curl --location --request POST 'http://localhost:9001/kafka/publish?message=Hello%20world'
```
In the application console you will see the message is not consumed by consumer group_id2 <br/>

### Test retry logic:
```
curl --location --request POST 'http://localhost:9001/kafka/publish?message=Hello%20retry'
```
You can see in the console the logic is retried as configured for group_id3 and on exhaustion the dlt handler is invoked

### Test custom object
```
curl --location --request POST 'http://localhost:9001/kafka/publishCustom?message=Hello&name=david'
```
The greeting message is produced/consumed to/from topic2