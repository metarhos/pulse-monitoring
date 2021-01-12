package telran.monitoring;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.monitoring.entities.Doctor;
import telran.monitoring.entities.Patient;
import telran.monitoring.entities.Visit;
import telran.monitoring.repo.DoctorRepository;
import telran.monitoring.repo.PatientRepository;
import telran.monitoring.repo.VisitRepository;
import org.junit.runners.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

@TestPropertySource(locations = "classpath:application.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DoctorDataTest {
	
private static final String PHONE_NUMBER = "1234567";
@Autowired
DoctorRepository doctors;
@Autowired
PatientRepository patients;
@Autowired
VisitRepository visits;

@Autowired
MockMvc mock;

@Test

public void addVisit() {
	Doctor doctor = doctors.save(new Doctor(123, "Moshe", "doctor", PHONE_NUMBER, "moshe@gmail.com"));
	Patient patient = patients.save(new Patient(1, "Sara"));
	visits.save(new Visit(LocalDateTime.now(), patient, doctor, "will live"));
}



@Test
public void getDoctorDataFromController() throws Exception {
	String responseBody = mock.perform(get("/doctors/phone/1"))
			.andReturn().getResponse().getContentAsString();
	String [] data = new ObjectMapper().readValue(responseBody, String[].class);
	assertEquals(PHONE_NUMBER, data[0]);
	
}

}
