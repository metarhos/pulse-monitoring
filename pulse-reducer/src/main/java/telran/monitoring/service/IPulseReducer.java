package telran.monitoring.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.SubscribableChannel;

public interface IPulseReducer extends Processor {
	String CONFIGURATION_CHANNEL = "config";
	@Input(CONFIGURATION_CHANNEL)
	SubscribableChannel getConfig();
}
