package com.va.spring.boot.kafka.service;

import com.va.spring.boot.kafka.model.Greeting;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    // groupId is optional on next line - uses groupId from consumerFactoryGroup1 bean
    @KafkaListener(topics = "topic1", groupId = "group_id1", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        System.out.printf("Received Message %s in group group_id1%n ", message);
    }

    // groupId is optional - uses groupId from consumerFactoryGroup2 bean
    @KafkaListener(topics = "topic1", containerFactory = "filterKafkaListenerContainerFactory")
    public void listenWithFilter(@Payload String message,
                                 @Header(KafkaHeaders.GROUP_ID) String groupId,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION) String partition) {
        System.out.printf("Received Message %s in group %s in partition %s%n", message, groupId, partition);
    }

    @RetryableTopic(
            attempts = "4", // Original attempt + 3 retries
            backoff = @Backoff(delay = 1000, multiplier = 2.0), // Exponential backoff
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = DltStrategy.FAIL_ON_ERROR, // If DLT processing fails, throw an error
            autoCreateTopics = "true" // Automatically create retry and DLT topics - default is true...
    )
    // Following groupid overrides the group id defined for consumerFactoryGroup1 bean
    @KafkaListener(topics = "topic1", groupId = "group_id3", containerFactory = "kafkaListenerContainerFactory", filter = "retryRecordFilterStrategy")
    public void consumeRetry(@Payload String message,
                             @Header(KafkaHeaders.GROUP_ID) String groupId,
                             @Header(KafkaHeaders.RECEIVED_PARTITION) String partition) {
        System.out.printf("Received Message %s in group %s in partition %s%n", message, groupId, partition);
        throw new RuntimeException("Simulating processing error");
    }

    @DltHandler
    public void processDlt(String message) {
        System.err.println("All retries exhausted.  Following message picked up from DLT: " + message);

    }

    @KafkaListener(topics = "topic2", containerFactory = "greetingKafkaListenerContainerFactory")
    public void greetingListener(Greeting greeting) {
        System.out.printf("Received Greeting Message %s from %s%n ", greeting.getMsg(), greeting.getName());
    }
}

