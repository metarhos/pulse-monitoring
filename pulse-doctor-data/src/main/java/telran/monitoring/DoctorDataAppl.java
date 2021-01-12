package telran.monitoring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import telran.monitoring.service.DoctorDataService;

@SpringBootApplication
@RestController
public class DoctorDataAppl {
	@Autowired
	DoctorDataService doctorDataService;
	@GetMapping("/doctors/phone/{patientId}")
List<String> getDoctorPhoneData(@PathVariable("patientId") int patientId) {
		return doctorDataService.getPhoneNumbers(patientId);
	}
	public static void main(String[] args) {
		SpringApplication.run(DoctorDataAppl.class, args);

	}

}
