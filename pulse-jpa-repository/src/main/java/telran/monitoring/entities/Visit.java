package telran.monitoring.entities;
import java.time.LocalDateTime;

import javax.persistence.*;
@Entity
@Table(name = "visits", indexes = {@Index(columnList = "patient_id"), @Index(columnList = "doctor_id")})
public class Visit {
@Id
@GeneratedValue
@Column(name = "visit_id")
long visitId;
@Column(name = "visit_time")
LocalDateTime visitTime;
@ManyToOne
@JoinColumn(name = "patient_id")
Patient patient;
@ManyToOne
@JoinColumn(name = "doctor_id")
Doctor doctor;
String diagnose;
public Visit() {
}
public Visit(LocalDateTime visitTime, Patient patient, Doctor doctor, String diagnose) {
	super();
	this.visitTime = visitTime;
	this.patient = patient;
	this.doctor = doctor;
	this.diagnose = diagnose;
}


}
