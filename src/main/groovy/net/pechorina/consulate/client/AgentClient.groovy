package net.pechorina.consulate.client

import com.fasterxml.jackson.core.type.TypeReference
import groovy.util.logging.Slf4j
import net.pechorina.consulate.Consul
import net.pechorina.consulate.data.agent.AgentService
import net.pechorina.consulate.data.agent.ServiceRegistration
import net.pechorina.consulate.exceptions.RESTException
import net.pechorina.consulate.utils.JsonMapHelper

@Slf4j
class AgentClient {
	static String urlPrefix = "/v1/agent"
	String url
	Consul consul
	
	Boolean register(String id, String name, int port) {
		ServiceRegistration reg = new ServiceRegistration(name: name, id: id, port: port)
		
		try {
			consul.httpClient.put(url + "/service/register", JsonMapHelper.toJson(consul, reg));
			return true
		}
		catch (RESTException ex) {
			log.error("Registration failed ---> REST error: " + ex)
		}
		return false
	}
	
	Boolean deregister(String id) {
		try {
			consul.httpClient.put(url + "/service/deregister/$id", "");
			consul.kv.put("/dataimport/$id", "DOWN")
			return true
		}
		catch (RESTException ex) {
			log.error("Deregistration failed ---> REST error: " + ex)
		}

		return false
	}
	
	Map<String, AgentService> listLocalServices() {
        TypeReference<Map<String,AgentService>> typeRef = new TypeReference<Map<String,AgentService>>() { }

        try {
            String responseBody = consul.httpClient.get(url + "/services");
            return JsonMapHelper.fromJson(consul, responseBody, typeRef);
        }
        catch (RESTException e) {
            log.error("Exception calling consul", e)
        }
        catch (IOException e) {
            log.error("Exception calling consul", e)
        }
		return null
	}
}
