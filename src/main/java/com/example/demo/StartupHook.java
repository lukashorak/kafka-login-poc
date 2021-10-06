package com.example.demo;

import com.example.demo.service.DebugKafkaProduceJdzDelegate;
import com.example.demo.service.KafkaActuatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class StartupHook {

    @Autowired
    KafkaActuatorService kafkaActuatorService;

    @Autowired
    DebugKafkaProduceJdzDelegate debugKafkaProduceJdzDelegate;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        System.out.println("hello world, I have just started up");
        debugKafkaProduceJdzDelegate.sendMessage("test", "message1");
    }
}
