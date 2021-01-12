package telran.monitoring;

import java.io.IOException;
import java.util.List;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.microservices.discovery.LoadBalancer;
import telran.monitoring.dto.PulseData;

@EnableBinding(Sink.class)
public class AbnormalValuesService {
	static Logger LOG = LoggerFactory.getLogger(AbnormalValuesService.class);
	@Value("${app.service.name}")
	String serviceName;
	List<String>  phoneNumbers; //mainly for unit test
public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
@Autowired
LoadBalancer loadBalancer;
ObjectMapper mapper = new ObjectMapper();
@Autowired
RestTemplate restTemplate;
@Bean
RestTemplate getRestTemplate() {
	return new RestTemplate();
}
private int patientId;
public int getPatientId() {
	return patientId;
}
@StreamListener(Sink.INPUT)
void takePatientData(String pulseDataJson)  {
	LOG.trace("received: {}", pulseDataJson);
	try {
		PulseData pulseData = mapper.readValue(pulseDataJson, PulseData.class);
		patientId = pulseData.patientId;
		List<String> phones = getPhones(pulseData.patientId);
		LOG.debug("list of phones {}", phones);
		sendSms(phones);
	} catch (IOException e) {
		LOG.error("exception from JSON parsing {} ", e.getMessage());
	}
}
private void sendSms(List<String> phones) {
	//FIXME for real SMS sending
	phoneNumbers = phones;//for testing purposes
	phones.stream().forEach(phone -> LOG.debug("sent SMS to phone: {}", phone));
	
}
private List<String> getPhones(int patientId) {
	String url = loadBalancer.getServiceUrl(serviceName);
	url = url + "/doctors/phone/" + patientId;
	LOG.debug("received URL is {}", url);
	ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
			new ParameterizedTypeReference<List<String>>() {
	});
	
	return response.getBody();
}
}
