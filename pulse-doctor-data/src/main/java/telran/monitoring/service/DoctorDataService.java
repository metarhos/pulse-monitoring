package telran.monitoring.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import telran.monitoring.repo.VisitRepository;

@Service
@EnableJpaRepositories({"telran.monitoring.repo"})
@EntityScan({"telran.monitoring.entities"})
public class DoctorDataService {
@Autowired
VisitRepository visitRepository;
public List<String> getPhoneNumbers(int patientId) {
	return visitRepository.findDoctorsPatient(patientId);
}
}
