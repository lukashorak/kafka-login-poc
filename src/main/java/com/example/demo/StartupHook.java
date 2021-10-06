package com.example.demo;

import com.example.demo.service.DebugKafkaProduceJdzDelegate;
import com.example.demo.service.KafkaActuatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class StartupHook {

    private final static Logger LOGGER = Logger.getLogger("StartupHook");

    @Autowired
    KafkaActuatorService kafkaActuatorService;

    @Autowired
    DebugKafkaProduceJdzDelegate debugKafkaProduceJdzDelegate;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        LOGGER.info("Startup Hook - Start");
        debugKafkaProduceJdzDelegate.sendMessage("test", "message1");
        LOGGER.info("Startup Hook - Done");
    }
}
