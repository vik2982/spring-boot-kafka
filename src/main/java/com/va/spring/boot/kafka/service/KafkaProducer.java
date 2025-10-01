package com.va.spring.boot.kafka.service;

import com.va.spring.boot.kafka.model.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    
    @Value("${tpd.topic1-name}")
	private String topicName;

    @Value("${tpd.topic2-name}")
    private String customTopicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

    public void sendMessage(String msg) {
        kafkaTemplate.send(topicName, msg);
    }

    public void sendCustomMessage(String msg, String name) {
        greetingKafkaTemplate.send(customTopicName, new Greeting(msg, name));
    }

}

