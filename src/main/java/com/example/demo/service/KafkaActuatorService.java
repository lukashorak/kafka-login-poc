package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KafkaActuatorService {

	private final static Logger LOGGER = Logger.getLogger("BpmKafkaActuatorService");

	@Value("${poc.actuator.kafka.topic}")
	private String topicName;

	@Value("${poc.actuator.kafka.groupId}")
	private String groupId;

	@KafkaListener(topics = "#{'${poc.actuator.kafka.topic}'}", groupId = "#{'${poc.actuator.kafka.groupId}'}")
	public void listenBpmKafkaActuatorMessage(String message) {
		LOGGER.info("Received Message: " + message);
		this.processMessage(message);
	}

	public void processMessage(String message) {
	}
}
