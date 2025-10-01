package com.va.spring.boot.kafka.controller;

import com.va.spring.boot.kafka.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

	@Autowired
    private KafkaProducer producer;

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        this.producer.sendMessage(message);
    }

    @PostMapping(value = "/publishCustom")
    public void sendCustomMessageToKafkaTopic(@RequestParam("message") String message,@RequestParam("name") String name) {
        this.producer.sendCustomMessage(message,name);
    }

}