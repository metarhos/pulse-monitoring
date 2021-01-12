package telran.monitoring.service;

import java.io.IOException;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.monitoring.dto.ConfigurationData;
import telran.monitoring.dto.PulseData;

@EnableBinding(IDispatcher.class)
public class DispatcherService {
	static Logger LOG = LoggerFactory.getLogger(DispatcherService.class);
	ObjectMapper mapper = new ObjectMapper();
@Autowired
IDispatcher dispatcher;
@Value("${app.normal.min:45}")
int minNormalValue;
@Value("${app.normal.max:215}")
int maxNormalValue;
@StreamListener(IDispatcher.CONFIGURATION_CHANNEL)
void takeConfiguration(String configJson)  {
	try {
		ConfigurationData configuration = mapper.readValue(configJson, ConfigurationData.class);
		if (configuration.minNormalValue != Integer.MAX_VALUE) {
			minNormalValue = configuration.minNormalValue;
			LOG.debug("new minNormalValue: {}", minNormalValue);
		}
		if (configuration.maxNormalValue != Integer.MAX_VALUE) {
			maxNormalValue = configuration.maxNormalValue;
			LOG.debug("new maxNormalValue: {}", maxNormalValue);
		}
	} catch (IOException e) {
		LOG.error("Configuration is unaccepted {}", e.getMessage());
	}
}
@StreamListener(IDispatcher.INPUT) 
void takePulseData(String pulseDataJson) throws Exception {
	PulseData pulseData = mapper.readValue(pulseDataJson, PulseData.class);
	LOG.trace("received: {}", pulseDataJson);
	if (pulseData.value < minNormalValue || pulseData.value > maxNormalValue) {
		LOG.debug("minNormalValue: {}, maxNormalValue:{}", minNormalValue, maxNormalValue);
		LOG.trace("sent to abnormal channel with id:{} and value: {}", pulseData.patientId, pulseData.value);
		dispatcher.sendAbnormalFlow().send(MessageBuilder.withPayload(pulseDataJson).build());
	}
	//sending to reducer
	dispatcher.sendReducer().send(MessageBuilder.withPayload(pulseDataJson).build());
}

}
