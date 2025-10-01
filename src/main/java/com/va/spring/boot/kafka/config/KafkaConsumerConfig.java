package com.va.spring.boot.kafka.config;

import com.va.spring.boot.kafka.model.Greeting;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean("consumerConfigProps")
    public Map<String, Object> getConfigProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactoryGroup1() {
        Map<String, Object> props = getConfigProps();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id1");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactoryGroup2() {
        Map<String, Object> props = getConfigProps();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id2");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGroup1());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> filterKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGroup2());
        factory.setRecordFilterStrategy(
                record -> record.value().toLowerCase().contains("world"));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Greeting> greetingConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                getConfigProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(Greeting.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Greeting> greetingKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, Greeting> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(greetingConsumerFactory());
        return factory;
    }
}
