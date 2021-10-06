package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value("${poc.kafka.kafkaAddress}")
	private String kafkaAddress;

	@Value("${poc.kafka.security:PLAINTEXT}")
	private String security;

	@Value("${poc.kafka.username}")
	private String username;

	@Value("${poc.kafka.password}")
	private String password;

	@Value("${poc.kafka.groupId:group1}")
	private String groupId;

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

	private final static Logger LOGGER = Logger.getLogger("KafkaConsumerConfig");

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				kafkaAddress);
		props.put(
				ConsumerConfig.GROUP_ID_CONFIG,
				groupId);
		props.put(
				ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);
		props.put(
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class);

		if ("PLAIN".equals(security)){
			props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
			props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
			props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
					"%s required username=\"%s\" " + "password=\"%s\";", PlainLoginModule.class.getName(), username, password
			));
			LOGGER.info("Setting security: "+ security);
		} else if ("SSL".equals(security)) {
			props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
			props.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);
			LOGGER.info("Setting security: " + security);
		} else if ("SSL_CUSTOM_CERT".equals(security)) {
			props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
			props.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);

			props.put("ssl.truststore.location", trustStoreLocation);
			props.put("ssl.truststore.password", trustStorePassword);
			props.put("ssl.truststore.type", trustStoreType);

			props.put("ssl.key.password", keyStorePassword);
			props.put("ssl.keystore.password", keyStorePassword);
			props.put("ssl.keystore.location", keyStoreLocation);
			props.put("ssl.keystore.type", keyStoreType);
			LOGGER.info("Setting security: " + security);
		}

		LOGGER.info("Starting Kafka with props "+ this.convertWithStream(props));
		return new DefaultKafkaConsumerFactory<>(props);
	}

	public String convertWithStream(Map<?, ?> map) {
		String mapAsString = map.keySet().stream()
				.map(key -> key + "=" + map.get(key))
				.collect(Collectors.joining(", ", "{", "}"));
		return mapAsString;
	}


	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String>
	kafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, String> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());

//		factory.setRecordFilterStrategy(consumerRecord -> {
//			try {
//				TypeReference<HashMap<String, Object>> typeRef
//						= new TypeReference<HashMap<String, Object>>() {};
//				Map<String, Object> data = objectMapper.readValue(consumerRecord.value(), typeRef);
//				boolean result = (Objects.equals("POZ_ZAK_OD", data.get("taskType")));
//				LOGGER.warning("Message " + consumerRecord.key() + " filter result:" + result);
//				return result;
//			} catch (JsonProcessingException e) {
//				LOGGER.warning("Not able to process Kafka Message during Filter phase");
//			}
//			return false;
//		});

		return factory;
	}
}
