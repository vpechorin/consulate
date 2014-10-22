package net.pechorina.consulate.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import groovy.util.logging.Slf4j
import net.pechorina.consulate.Consul
import net.pechorina.consulate.data.agent.AgentService
import net.pechorina.consulate.data.agent.ServiceRegistration

@Slf4j
class AgentClient {
	static String urlPrefix = "/v1/agent"
	String url
	Consul consul
	
	Boolean register(String id, String name, int port) {
		ServiceRegistration reg = new ServiceRegistration(name: name, id: id, port: port)
		
		try {
			consul.restTemplate.put(url + "/service/register", reg)
			return true
		}
		catch (RestClientException ex) {
			log.error("Registration failed ---> REST error: " + ex)
		}
		return false
	}
	
	Boolean deregister(String id) {
		try {
			consul.restTemplate.put(url + "/service/deregister/$id", null)
			return true
		}
		catch (RestClientException ex) {
			log.error("Dergistration failed ---> REST error: " + ex)
		}
		return false
	}
	
	Map<String, AgentService> listLocalServices() {
		ParameterizedTypeReference<Map<String,AgentService>> typeRef = new ParameterizedTypeReference<Map<String,AgentService>>() {};
		ResponseEntity<Map<String,AgentService>> response = consul.restTemplate.exchange(url + "/services", HttpMethod.GET, null, typeRef);
		if (response.statusCode == HttpStatus.OK) {
			return response.body
		}
		return null
	}
}
