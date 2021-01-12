package telran.monitoring.controllers;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.monitoring.dto.ConfigurationData;

@RestController
@EnableBinding(Source.class)
public class ConfiguraterController {
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	Source source;
	static Logger LOG = LoggerFactory.getLogger(ConfiguraterController.class);
	@PostMapping("/value/min")
ResponseEntity<ConfigurationData> setMinNormalValue(@RequestBody int minNormalValue) {
	ConfigurationData configuration = new ConfigurationData(minNormalValue, Integer.MAX_VALUE, Integer.MAX_VALUE);
	return configProcessing(configuration);
	
}
	@PostMapping("/value/max")
ResponseEntity<ConfigurationData> setMaxNormalValue(@RequestBody int maxNormalValue) {
	ConfigurationData configuration = new ConfigurationData(Integer.MAX_VALUE, maxNormalValue, Integer.MAX_VALUE);
	return configProcessing(configuration);
	
}
private ResponseEntity<ConfigurationData> configProcessing(ConfigurationData configuration) {
	try {
		sendConfiguration(configuration);
	} catch (JsonProcessingException e) {
		LOG.error("Wrong configuration {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	return ResponseEntity.ok(configuration);
}
@PostMapping("/count/reduce")
ResponseEntity<ConfigurationData> setReduceCount(@RequestBody int reduceCount) {
	ConfigurationData configuration = new ConfigurationData(Integer.MAX_VALUE, Integer.MAX_VALUE, reduceCount);
	return configProcessing(configuration);
	
}
private void sendConfiguration(ConfigurationData configuration) throws JsonProcessingException {
	String configurationJson = mapper.writeValueAsString(configuration);
	source.output().send(MessageBuilder.withPayload(configurationJson).build());
	LOG.debug("configuration: {} sent to middleware", configurationJson);
	
}
}
