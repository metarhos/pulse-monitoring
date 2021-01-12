package telran.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan({"telran"})
public class AbnormalValuesProcessorAppl {

	public static void main(String[] args) {
		SpringApplication.run(AbnormalValuesProcessorAppl.class, args);

	}
	

}
