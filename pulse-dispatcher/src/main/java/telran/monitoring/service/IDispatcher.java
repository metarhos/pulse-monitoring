package telran.monitoring.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface IDispatcher extends Sink {
//	spring.cloud.stream.bindings.config.destination=config
//			spring.cloud.stream.bindings.reducer.destination=reducer
//			spring.cloud.stream.bindings.abnormal.destination=abnormal
	String CONFIGURATION_CHANNEL = "config";
	@Input(CONFIGURATION_CHANNEL)
	SubscribableChannel getConfig();
	String REDUCER_CHANNEL = "reducer";
	String ABNORMAL_FLOW_CHANNEL = "abnormal";
	
	@Output(REDUCER_CHANNEL)
	MessageChannel sendReducer();
	@Output(ABNORMAL_FLOW_CHANNEL)
	MessageChannel sendAbnormalFlow();
}
