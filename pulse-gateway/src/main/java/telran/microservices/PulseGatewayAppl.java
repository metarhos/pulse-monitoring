package telran.microservices;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.microservices.discovery.LoadBalancer;

@SpringBootApplication
@ComponentScan({"telran"})
@RestController
public class PulseGatewayAppl {
	static Logger LOG = LoggerFactory.getLogger(PulseGatewayAppl.class);
	@Autowired
LoadBalancer loadBalancer;
	@Value("${app.attemps:10}")
	private int attemps;
	
	@Value("${app.services.allowed:pulse-configurater}")
	Set<String> services;
	public static void main(String[] args) {
		SpringApplication.run(PulseGatewayAppl.class, args);

	}
	@PostMapping("/**")
	public ResponseEntity<?> proxyPathPost(ProxyExchange<byte[]> proxy) throws Exception {
		  String path = proxy.path();
		  String uri = "";
		  String serviceName = getServiceName(path);
		  if(serviceName==null) {
			  return ResponseEntity.status(403).body("service is denied");
		  }
		  uri = getUri(path, serviceName);
		  LOG.debug("received uri is {} ", uri);
		  if (uri == null || uri.isEmpty()) {
			  LOG.error("Path: {} can't be performed as URI is not found ", path);
			  return ResponseEntity.status(400).body("URI is wrong");
		  }
		  return proxy.uri(uri).post(); //routing to a real host
		}
	
	@GetMapping("/**")
	public ResponseEntity<?> proxyPathGet(ProxyExchange<byte[]> proxy,
			HttpServletRequest request) throws Exception {
		  String path = proxy.path();
		  String uri = "";
		  String serviceName = getServiceName(path);
		  if(serviceName==null) {
			  return ResponseEntity.status(403).body("service is denied");
		  }
		  uri = getUri(path, serviceName);
		  LOG.debug("received uri is {} ", uri);
		  if (uri == null || uri.isEmpty()) {
			  LOG.error("Path: {} can't be performed as URI is not found ", path);
			  return ResponseEntity.status(400).body("URI is wrong");
		  }
		  String queryParams = request.getQueryString();
		  if(queryParams != null && !queryParams.isEmpty()) {
			  uri += queryParams;
		  }
		  return proxy.uri(uri).get(); //routing to a real host
		}
	private String getServiceName(String path) {
		String serviceName = path.split("/")[1];
		  LOG.debug("service {} from the path {}", serviceName, path);
		  if(!services.contains(serviceName)) {
			  LOG.error("Service {} is not allowed", serviceName);
			  return null;
		  }
		return serviceName;
	}
	private String getUri(String path, String serviceName) throws InterruptedException {
		// path - structure 
		  String uri="";
		  for(int i=0; i < attemps; i++) {
			 uri = loadBalancer.getServiceUrl(serviceName);
			 if(uri != null && !uri.isEmpty())
			 {
				 break;
			 }
			 Thread.sleep(1500);
			  
		  }
		  
		  if(uri.isEmpty()) {
			  LOG.warn("service {} is not registred in DiscoveryServer", serviceName);
			  uri = "http://" + serviceName ;
		  }
		  uri += path.substring(serviceName.length() + 1);
		return uri;
	}
	
	

}
