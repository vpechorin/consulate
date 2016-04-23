package net.pechorina.consulate

import com.fasterxml.jackson.databind.ObjectMapper
import net.pechorina.consulate.client.AgentClient
import net.pechorina.consulate.client.CatalogClient
import net.pechorina.consulate.client.KVStoreClient
import net.pechorina.consulate.exceptions.ConsulateException
import net.pechorina.consulate.utils.HttpClient

class Consul {
	String host
	int port
	ObjectMapper objectMapper
    HttpClient httpClient
	AgentClient agent
	CatalogClient catalog
	KVStoreClient kv
	String url

	static Consul newClient(String host, int port) {
		try {
			
			ObjectMapper mapper = new ObjectMapper()
            HttpClient httpClient = HttpClient.newClient();

            Consul c = new Consul(
                    host: host,
                    port: port,
                    objectMapper: mapper,
                    httpClient: httpClient,
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
