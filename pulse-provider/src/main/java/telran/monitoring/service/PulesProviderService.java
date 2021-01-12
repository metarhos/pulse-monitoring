package telran.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.monitoring.dto.PulseData;

@EnableBinding(Source.class)
public class PulesProviderService {
	static Logger LOG = LoggerFactory.getLogger(PulesProviderService.class);
ObjectMapper mapper = new ObjectMapper();
@Value("${app.max.provided.value:250}")
int maxProviderValue;
@Value("${app.min.provided.value:30}")
int minProviderValue;
@Value("${app.count.patients:20}")
int nPatients;
@Value("${spring.cloud.stream.bindings.output.destination}")
String topic;
@InboundChannelAdapter(Source.OUTPUT)
String getPulseData() throws JsonProcessingException 
{
	PulseData pulseData = getRandomPulse();
	return mapper.writeValueAsString(pulseData);
}
private PulseData getRandomPulse() {
	
	int value = getRandomNumber(minProviderValue, maxProviderValue);
	int patientId = getRandomNumber(1, nPatients);
	LOG.trace("pulse for patient: {} value: {} on topic {}", patientId, value,topic);
	return new PulseData(value, patientId, System.currentTimeMillis());
}
private int getRandomNumber(int min, int max) {
	return (int) (min + Math.random() * (max - min + 1));
}
}
