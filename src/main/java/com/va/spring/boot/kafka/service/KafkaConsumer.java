package com.va.spring.boot.kafka.service;

import com.va.spring.boot.kafka.model.Greeting;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "topic1", groupId = "group_id1", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        System.out.printf("Received Message %s in group group_id1%n ", message);
    }

    @KafkaListener(topics = "topic1", groupId = "group_id2", containerFactory = "filterKafkaListenerContainerFactory")
    public void listenWithFilter(@Payload String message,
                                 @Header(KafkaHeaders.GROUP_ID) String groupId,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION) String partition) {
        System.out.printf("Received Message %s in group %s in partition %s%n", message, groupId, partition);
    }

    @KafkaListener(topics = "topic2", containerFactory = "greetingKafkaListenerContainerFactory")
    public void greetingListener(Greeting greeting) {
        System.out.printf("Received Greeting Message %s from %s%n ", greeting.getMsg(), greeting.getName());
    }
}

