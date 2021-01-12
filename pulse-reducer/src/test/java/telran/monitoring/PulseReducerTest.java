package telran.monitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.monitoring.dto.ConfigurationData;
import telran.monitoring.dto.PulseData;
import telran.monitoring.service.IPulseReducer;
import telran.monitoring.service.PulseReducerService;
@RunWith(SpringRunner.class)
@SpringBootTest

@TestPropertySource(locations = "classpath:application.properties")
@SuppressWarnings("unchecked")
public class PulseReducerTest {
	private static final int PULSE_VALUE = 100;
	private static final int NEW_REDUCE_COUNT = 60;
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	IPulseReducer pulseReducer;
	@Autowired
	PulseReducerService reducerService;
	
	 @Autowired
	  private MessageCollector messageCollector;

	@Test
	  
	  public void reducing() throws Exception {
		PulseData pulseData100 = new PulseData(PULSE_VALUE, 1, System.currentTimeMillis());
		PulseData pulseData200 = new PulseData(500, 1, System.currentTimeMillis());
		
	    Message<String> message = null;
	    int limit = reducerService.getReduceCount();
		for (int i = 0; i < limit ; i++) {
			//should be reduced
			message = new GenericMessage<>(mapper.writeValueAsString(pulseData100)); 
			pulseReducer.input().send(message);
		}
		limit = reducerService.getReduceCount() - 1;
		for (int i = 0; i < limit; i++) {
			//should not be reduced
			message = new GenericMessage<>(mapper.writeValueAsString(pulseData200)); 
			pulseReducer.input().send(message);
		}
	   
	    Message<String> received = (Message<String>) messageCollector.forChannel(pulseReducer.output())
	    		.poll();
	    assertNotNull(received);
	    PulseData averagePulseData = mapper.readValue(received.getPayload(), PulseData.class);
	    assertEquals(PULSE_VALUE, averagePulseData.value);
	    received = (Message<String>) messageCollector.forChannel(pulseReducer.output())
	    		.poll();
	    assertNull(received);
	    
	  }
	@Test
	public void configurationTest() throws JsonProcessingException {
		//testing whether the service takes new configuration
		ConfigurationData configData = new ConfigurationData(Integer.MAX_VALUE, Integer.MAX_VALUE, NEW_REDUCE_COUNT);
		Message<String> message = new GenericMessage<String>(mapper.writeValueAsString(configData));
		pulseReducer.getConfig().send(message);
		assertEquals(NEW_REDUCE_COUNT, reducerService.getReduceCount());
		
		
	}
	
}
