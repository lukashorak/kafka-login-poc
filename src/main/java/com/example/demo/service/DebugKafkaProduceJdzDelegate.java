package com.example.demo.service;

import com.example.demo.config.KafkaProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.TypedValue;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;


@Service
public class DebugKafkaProduceJdzDelegate {

    private final static Logger LOGGER = Logger.getLogger("DebugKafkaProduceJdzDelegate");

    private String kafkaTopic = "idm-task-change";

    @Autowired
	KafkaProducerConfig kafkaProducerConfig;

    public void execute(String topic, String message) throws Exception {
		LOGGER.info("Sending message to Kafka");
		LOGGER.info("Sending message to Kafka (topic)  :"+topic);
		LOGGER.info("Sending message to Kafka (message):"+message );
		this.sendMessage(topic, message);

    }

	public void sendMessage(String selectedTopic, String msg) {
		if (this.kafkaTemplate() != null) {
			String uuid = UUID.randomUUID().toString();
			this.kafkaTemplate().send(selectedTopic, uuid, msg);
			//this.kafkaTemplate().send(kafkaTopic, "test1");
			LOGGER.info(String.format("Kafka message %s to topic: %s sent", uuid, kafkaTopic));
		}else{
			LOGGER.info("kafkaTemplate is missing");
		}
	}

	private KafkaTemplate<String, String> kafkaTemplate() {
		return kafkaProducerConfig.bpmKafkaTemplate();
	}
}
