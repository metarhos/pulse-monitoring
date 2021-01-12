package telran.monitoring.service;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.monitoring.dto.ConfigurationData;
import telran.monitoring.dto.PulseData;

@EnableBinding(IPulseReducer.class)
public class PulseReducerService {
	static Logger LOG = LoggerFactory.getLogger(PulseReducerService.class);
ObjectMapper mapper = new ObjectMapper();
@Autowired
IPulseReducer pulseReducer;
@Value("${app.count.reduce: 20}")
int reduceCount;
public int getReduceCount() {
	return reduceCount;
}
HashMap<Integer, List<PulseData>> mapPulses = new HashMap<>();
@StreamListener(IPulseReducer.CONFIGURATION_CHANNEL) 
void takeConfiguration(String configurationJson) {
	try {
		ConfigurationData configuration = mapper.readValue(configurationJson, ConfigurationData.class);
		if(configuration.reduceCount != Integer.MAX_VALUE) {
			reduceCount = configuration.reduceCount;
			LOG.debug("new reuceCount value: {}", reduceCount);
		}
	} catch (IOException e) {
		LOG.error("configuration is unaccepted: {}", e.getMessage());
	}
	
}
@StreamListener(IPulseReducer.INPUT)
void takePulseData(String pulseDataJson) throws Exception {
	LOG.trace("received {}", pulseDataJson);
	PulseData pulseData = mapper.readValue(pulseDataJson, PulseData.class);
	List<PulseData> pulses = mapPulses.computeIfAbsent(pulseData.patientId, k -> new ArrayList<>());
	pulses.add(pulseData);
	if(pulses.size() >= reduceCount) {
		reduceProcessing(pulseData.patientId);
	}
}
private void reduceProcessing(int patientId) throws JsonProcessingException {
	double avgValue = mapPulses.get(patientId).stream().collect(Collectors.averagingInt(pd -> pd.value));
	PulseData reducedData = new PulseData((int)avgValue, patientId, System.currentTimeMillis());
	String reducedDataJson = mapper.writeValueAsString(reducedData);
	pulseReducer.output().send(MessageBuilder.withPayload(reducedDataJson).build());
	LOG.debug("data: {} sent to population channel", reducedDataJson);
	mapPulses.remove(patientId);
	
}
}
