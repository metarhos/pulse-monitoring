package telran.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DIscoveryServerAppl {

	public static void main(String[] args) {
		SpringApplication.run(DIscoveryServerAppl.class, args);

	}

}
