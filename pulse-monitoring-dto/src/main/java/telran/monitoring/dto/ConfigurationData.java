package telran.monitoring.dto;

public class ConfigurationData {
public int minNormalValue;
public int maxNormalValue;
public int reduceCount;
public ConfigurationData() {
}
public ConfigurationData(int minNormalValue, int maxNormalValue, int reduceCount) {
	super();
	this.minNormalValue = minNormalValue;
	this.maxNormalValue = maxNormalValue;
	this.reduceCount = reduceCount;
}


}
