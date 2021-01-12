package telran.monitoring.dto;

public class PulseData {
public int value;
public int patientId;
public long timestamp;
public PulseData() {
}
public PulseData(int value, int patientId, long timestamp) {
	super();
	this.value = value;
	this.patientId = patientId;
	this.timestamp = timestamp;
}

}
