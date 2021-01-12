package telran.monitoring.jpa;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import telran.monitoring.entities.Doctor;
import telran.monitoring.entities.Patient;
import telran.monitoring.entities.Visit;
import telran.monitoring.repo.*;

@SpringBootApplication
@EnableJpaRepositories({"telran.monitoring.repo"})
@EntityScan({"telran.monitoring.entities"})
public class RandomDbCreator {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(RandomDbCreator.class, args);
		DoctorRepository doctorRepository = ctx.getBean(DoctorRepository.class);
		PatientRepository patientRepository = ctx.getBean(PatientRepository.class);
		VisitRepository visitRepository = ctx.getBean(VisitRepository.class);
		createDoctors(doctorRepository, 5);
		createPatients(patientRepository, 20);
		createVisits(visitRepository, 50, 5, 20, doctorRepository, patientRepository);
		ctx.close();

	}

	private static void createVisits(VisitRepository visitRepository, int nVisits, int nDoctors, int nPatients, DoctorRepository doctorRepository, PatientRepository patientRepository) {
		for(int i = 0; i < nVisits; i++) {
			visitRepository.save(new Visit(LocalDateTime.now(), getPatient(patientRepository),
					getDoctor(doctorRepository), "everything ok"));
		}
		
	}

	private static Doctor getDoctor(DoctorRepository doctorRepository) {
	
		return doctorRepository.findById(getRandomNumber(1, 5)).get();
	}

	private static Patient getPatient(PatientRepository patientRepository) {
		Patient patient = patientRepository.findById(getRandomNumber(1, 20)).get();
		return patient;
	}

	private static Integer getRandomNumber(int min, int max) {
		
		return (int) (min + Math.random() * (max - min + 1));
	}

	private static void createPatients(PatientRepository patientRepository, int nPatients) {
		
		for (int i = 1; i <= nPatients; i++) {
			patientRepository.save(new Patient(i, "patient" + i));
		}
	}

	private static void createDoctors(DoctorRepository doctorRepository, int nDoctors) {
		for (int i = 1; i <= nDoctors; i++) {
			doctorRepository.save(new Doctor(i, "doctor" + i, "family", ("" + i).repeat(7), i + "@gmail.com"));
		}
		
	}

}
