package telran.monitoring;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.microservices.discovery.LoadBalancer;
import telran.monitoring.dto.PulseData;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)	
@SuppressWarnings("unchecked")
@TestPropertySource(locations = "classpath:application.properties")
public class AbnormalValuesProcessorTest {
	private static final int PATIENT_ID = 123;
	private static final String DOCTOR_PHONE = "12345";
	@Autowired
	MessageCollector messageCollector;

	@Autowired
	AbnormalValuesService avService;
	@Autowired
	Sink sink;
	@MockBean
	RestTemplate restTemplate;

	@MockBean
	LoadBalancer loadBalancer;
	
	@Before
	public void setUp() {
		when(loadBalancer.getServiceUrl(anyString())).thenReturn("unit_test");
		when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class), any(Object[].class)))
		.thenReturn(ResponseEntity.ok(List.of(DOCTOR_PHONE)));
		
	}
	@Test
	public void abnormalValuesServiceTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		PulseData pulseData = new PulseData(300, PATIENT_ID, System.currentTimeMillis());
	
		
		sink.input().send(new GenericMessage<String>(mapper.writeValueAsString(pulseData)));
		assertEquals(PATIENT_ID, avService.getPatientId());
		assertEquals(DOCTOR_PHONE,avService.getPhoneNumbers().get(0));
	}

}
