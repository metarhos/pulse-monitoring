package telran.monitoring.entities;

import javax.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {
@Id
@Column(name = "doctor_id")
int doctorId;
String name;
String specialization;
@Column(name="phone_number")
String phoneNumber;
String email;
public Doctor() {
}
public Doctor(int doctorId, String name, String specialization, String phoneNumber, String email) {
	super();
	this.doctorId = doctorId;
	this.name = name;
	this.specialization = specialization;
	this.phoneNumber = phoneNumber;
	this.email = email;
}

}
