package telran.monitoring;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.slf4j.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerAppl {
	static Logger LOG = LoggerFactory.getLogger(ConfigServerAppl.class);

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerAppl.class, args);

	}
	@PostConstruct
	void init() {
		try {
			LOG.info("ip address: {}", InetAddress.getByName("config-server"));
		} catch (UnknownHostException e) {
			LOG.error(e.getMessage());
		}
	}
	

}
