package com.va.spring.boot.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKafkaApplication.class, args);
	}

	@Value("${tpd.topic-name}")
	private String topicName;

	@Bean
    public NewTopic adviceTopic() {
        return new NewTopic(topicName, 1, (short) 1);
    }
}

