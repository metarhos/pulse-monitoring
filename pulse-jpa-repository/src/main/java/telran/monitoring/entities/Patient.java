package telran.monitoring.entities;

import javax.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {
@Id
@Column(name="patient_id")
int patientId;
String name;
public Patient() {
}
public Patient(int patientId, String name) {
	super();
	this.patientId = patientId;
	this.name = name;
}

}
