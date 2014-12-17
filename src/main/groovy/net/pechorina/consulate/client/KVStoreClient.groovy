package net.pechorina.consulate.client

import groovy.util.logging.Slf4j
import net.pechorina.consulate.Consul
import net.pechorina.consulate.data.KeyValue
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@Slf4j
class KVStoreClient {
	String url
	Consul consul
	
	String getValue(String key, String datacenter) {
		String uri = "$url/$key"
		if (datacenter) uri += "?dc=$datacenter"
		String v = null;
		ParameterizedTypeReference<List<KeyValue>> typeRef = new ParameterizedTypeReference<List<KeyValue>>() {};
		ResponseEntity<List<KeyValue>> resp = consul.restTemplate.exchange(uri, HttpMethod.GET, null, typeRef);
		if (resp.statusCode == HttpStatus.OK) {
			List<KeyValue> list = resp.body
			
			if (list && list.size() ) {
				v = list.get(0).getValueAsString()
			}
		}
		return v
	}
	
	/**
	 * Returns decoded value by key from the local datacenter
	 * @param key 
	 * @return
	 */
	String getValue(String key) {
		return getValue(key, null)
	}
	
	List<String> getValuesByPrefix(String key) {
		List<String> lst = null;
		ParameterizedTypeReference<List<KeyValue>> typeRef = new ParameterizedTypeReference<List<KeyValue>>() {};
		ResponseEntity<List<KeyValue>> response = consul.restTemplate.exchange("$url/$key?recurse", HttpMethod.GET, null, typeRef);
		if (response.statusCode == HttpStatus.OK) {
			if (response.body && response.body.size() ) {
				List<KeyValue> entries = response.body
				lst = entries.collect { entry -> entry.getValueAsString() }
			}
		}
		return lst
	}
	
	Map<String,String> getEntriesAsMap(String key) {
		ParameterizedTypeReference<List<KeyValue>> typeRef = new ParameterizedTypeReference<List<KeyValue>>() {};
		ResponseEntity<List<KeyValue>> response = consul.restTemplate.exchange("$url/$key?recurse", HttpMethod.GET, null, typeRef);
		if (response.statusCode == HttpStatus.OK) {
			if (response.body && response.body.size() ) {
				List<KeyValue> entries = response.body
				return entries.collectEntries{ entry -> [(entry.key): entry.getValueAsString() ]}
			}
		}
		return null
	}
	
	List<String> listKeysByPrefix(String key) {
		ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<List<String>>() {};
		ResponseEntity<List<String>> response = consul.restTemplate.exchange("$url/$key", HttpMethod.GET, null, typeRef);
		if (response.statusCode == HttpStatus.OK) {
			return response.getBody()
		}
		return null
	}
	
	void put(String key, String value) {
		consul.restTemplate.put("$url/$key", value)
	}
	
	void delete(String key) {
		consul.restTemplate.delete("$url/$key")
	}
	
	void deleteRecursive(String key) {
		consul.restTemplate.delete("$url/$key?recurse")
	}
	
}
