package com.example.demo;

import com.example.demo.service.DebugKafkaProduceJdzDelegate;
import com.example.demo.service.KafkaActuatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	KafkaActuatorService kafkaActuatorService;

	@Autowired
	DebugKafkaProduceJdzDelegate debugKafkaProduceJdzDelegate;

	@Test
	void contextLoads() {
	}

	@Test
	public void kafkaTest(){
		debugKafkaProduceJdzDelegate.sendMessage("test", "message1");
	}

}
