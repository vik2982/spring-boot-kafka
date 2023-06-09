# Spring boot with kafka example

### Prerequisites

* JDK 8
* Maven 3.0+
* Latest Docker installation

### How to Run

To start kafka: `docker-compose up -d`  
Send a post request to the resource: `curl --location --request POST 'http://localhost:9001/kafka/publish?message=ALOHA'`  
In the application console you will see the log `Consumed Message -> ALOHA` which signifies the message posted to the resource has been put on the kafka topic and then consumed by our application

