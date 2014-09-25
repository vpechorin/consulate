package net.pechorina.consulate

import java.util.List

import net.pechorina.consulate.client.AgentClient;
import net.pechorina.consulate.client.CatalogClient;
import net.pechorina.consulate.client.KVStoreClient
import net.pechorina.consulate.exceptions.ConsulateException;

import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.client.RestTemplate

import com.fasterxml.jackson.databind.ObjectMapper

class Consul {
	String host
	int port
	ObjectMapper objectMapper
	AgentClient agent
	CatalogClient catalog
	KVStoreClient kv
	List<HttpMessageConverter<?>> messageConverters
	RestTemplate restTemplate
	String url
	
	static Consul newClient(String host, int port) {
		try {
			
			ObjectMapper mapper = new ObjectMapper()
			RestTemplate restTemplate = new RestTemplate()
			
			Consul c = new Consul(
				host: host, 
				port: port, 
				objectMapper: mapper,
				restTemplate: restTemplate,
				url: "http://$host:$port/v1"
			)
			
			AgentClient ac = new AgentClient(consul: c, url: c.url + "/agent")
			c.agent = ac
			
			CatalogClient cc = new CatalogClient(consul: c, url: c.url + "/catalog")
			c.catalog = cc
			
			KVStoreClient kv = new KVStoreClient(consul: c, url: c.url + "/kv")
			c.kv = kv
			
			return c
		} catch (URISyntaxException ex) {
			throw new ConsulateException("Bad CONSUL url: $host:$port" + ex.toString())
		}
		return null
	}
	
	static Consul newClient() {
		return newClient("localhost", 8500)
	}
}
