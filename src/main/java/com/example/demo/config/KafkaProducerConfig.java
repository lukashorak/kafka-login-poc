package com.example.demo.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Configuration
public class KafkaProducerConfig {

	private final static Logger LOGGER = Logger.getLogger("BpmKafkaProducerConfig");

	@Value("${poc.kafka.kafkaAddress}")
	private String kafkaAddress;

	@Value("${poc.kafka.security:PLAINTEXT}")
	private String security;

	@Value("${poc.kafka.username}")
	private String username;

	@Value("${poc.kafka.password}")
	private String password;

	@Value("${poc.kafka.clientId:cpr-bpm-user}")
	private String clientId;

	@Value("${poc.kafka.trustStoreLocation:default}")
	private String trustStoreLocation;

	@Value("${poc.kafka.trustStorePassword:default}")
	private String trustStorePassword;

	@Value("${poc.kafka.trustStoreType:default}")
	private String trustStoreType;

	@Value("${poc.kafka.keyStorePassword:default}")
	private String keyStorePassword;

	@Value("${poc.kafka.keyStoreLocation:default}")
	private String keyStoreLocation;

	@Value("${poc.kafka.keyStoreType:default}")
	private String keyStoreType;

	@Bean
	public ProducerFactory<String, String> bpmProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				kafkaAddress);
		configProps.put(
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		configProps.put(
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);

		if ("PLAIN".equals(security)) {
			configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
			configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
			configProps.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
					"%s required username=\"%s\" " + "password=\"%s\";", PlainLoginModule.class.getName(), username, password
			));
			LOGGER.info("Setting security: " + security);
		} else if ("SSL".equals(security)) {
			configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
			configProps.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);
			LOGGER.info("Setting security: " + security);
		} else if ("SSL_CUSTOM_CERT".equals(security)) {
			configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
			configProps.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);

			configProps.put("ssl.truststore.location", trustStoreLocation);
			configProps.put("ssl.truststore.password", trustStorePassword);
			configProps.put("ssl.truststore.type", trustStoreType);

			configProps.put("ssl.key.password", keyStorePassword);
			configProps.put("ssl.keystore.password", keyStorePassword);
			configProps.put("ssl.keystore.location", keyStoreLocation);
			configProps.put("ssl.keystore.type", keyStoreType);
			LOGGER.info("Setting security: " + security);
		}

		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, String> bpmKafkaTemplate() {
		return new KafkaTemplate<>(bpmProducerFactory());
	}
}
