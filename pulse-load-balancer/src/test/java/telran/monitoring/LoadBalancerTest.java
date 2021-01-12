package telran.monitoring;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


import telran.microservices.discovery.LoadBalancer;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application.properties")
public class LoadBalancerTest {
	private static final String SI1 = "si1";
	private static final String SI2 = "si2";
	private static final String SI3 = "si3";
	@Autowired
	LoadBalancer loadBalancer;
	@MockBean
	DiscoveryClient dc;

	ServiceInstance si1 = mock(ServiceInstance.class);
	ServiceInstance si2 = mock(ServiceInstance.class);
	ServiceInstance si3 = mock(ServiceInstance.class);
	List<ServiceInstance> instances= new ArrayList<>(Arrays.asList(si1, si2,si3));


@Before
public void setUp() {
	when(dc.getInstances(Mockito.anyString())).thenReturn(instances);
	when(si1.getUri()).thenReturn(URI.create(SI1));
	when(si2.getUri()).thenReturn(URI.create(SI2));
	when(si3.getUri()).thenReturn(URI.create(SI3));
}
@Test
public void test1() {
	for(int i = 0; i < 100; i++) {
		instances.forEach(si -> 
		assertEquals(si.getUri().toString(),loadBalancer.getServiceUrl("abc")));
	}
	
}
}

