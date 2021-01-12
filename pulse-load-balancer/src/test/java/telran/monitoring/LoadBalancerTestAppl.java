package telran.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.microservices.discovery.LoadBalancer;

@SpringBootApplication
class LoadBalancerTestAppl {
	@Bean
	LoadBalancer loadBalancer() {
		return new LoadBalancer();
	}
public static void main(String[] args) {
	SpringApplication.run(LoadBalancerTestAppl.class, args);
}
}
