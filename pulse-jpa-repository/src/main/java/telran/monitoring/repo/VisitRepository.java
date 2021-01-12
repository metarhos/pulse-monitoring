package telran.monitoring.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import telran.monitoring.entities.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
	@Query("select distinct doctor.phoneNumber from Visit  where patient.patientId=:patientId")
	List<String> findDoctorsPatient(@Param("patientId") int patientId);

}
