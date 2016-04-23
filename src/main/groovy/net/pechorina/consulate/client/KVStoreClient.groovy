package net.pechorina.consulate.client

import com.fasterxml.jackson.core.type.TypeReference
import groovy.util.logging.Slf4j
import net.pechorina.consulate.Consul
import net.pechorina.consulate.data.KeyValue
import net.pechorina.consulate.exceptions.RESTException
import net.pechorina.consulate.utils.JsonMapHelper

@Slf4j
class KVStoreClient {
	String url
	Consul consul
	
	String getValue(String key, String datacenter) {
		String uri = "$url/$key"
		if (datacenter) uri += "?dc=$datacenter"
		String v = null;

		TypeReference<List<KeyValue>> typeRef = new TypeReference<List<KeyValue>>() {}
		try {
			String json = consul.httpClient.get(uri)
			List<KeyValue> list = JsonMapHelper.fromJson(consul, json, typeRef)

			if (list && list.size() ) {
				v = list.get(0).getValueAsString()
			}
		}
		catch (RESTException ex) {
			log.error("List nodes by service error: ", ex)
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

		TypeReference<List<KeyValue>> typeRef = new TypeReference<List<KeyValue>>() {}
		try {
			String json = consul.httpClient.get("$url/$key?recurse")
			List<KeyValue> list = JsonMapHelper.fromJson(consul, json, typeRef)

			if (list && list.size() ) {
				lst = list.collect { entry -> entry.getValueAsString() }
			}
		}
		catch (RESTException ex) {
			log.error("List nodes by service error: ", ex)
		}

		return lst
	}
	
	Map<String,String> getEntriesAsMap(String key) {
		TypeReference<List<KeyValue>> typeRef = new TypeReference<List<KeyValue>>() {}
		try {
			String json = consul.httpClient.get("$url/$key?recurse")
			List<KeyValue> list = JsonMapHelper.fromJson(consul, json, typeRef)

			if (list && list.size() ) {
				return list.collectEntries{ entry -> [(entry.key): entry.getValueAsString() ]}
			}
		}
		catch (RESTException ex) {
			log.error("List nodes by service error: ", ex)
		}

		return []
	}
	
	List<String> listKeysByPrefix(String key) {

		TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {}
		try {
			String json = consul.httpClient.get("$url/$key")
			return JsonMapHelper.fromJson(consul, json, typeRef)
		}
		catch (RESTException ex) {
			log.error("listKeysByPrefix error: ", ex)
		}

		return null
	}
	
	void put(String key, String value) {
		try {
			consul.httpClient.put("$url/$key", value)
		}
		catch (RESTException ex) {
			log.error("List nodes by service error: ", ex)
		}

	}
	
	void delete(String key) {
		try {
			consul.httpClient.delete("$url/$key")
		}
		catch (RESTException ex) {
			log.error("List nodes by service error: ", ex)
		}

	}
	
	void deleteRecursive(String key) {
		try {
			consul.httpClient.delete("$url/$key?recurse")
		}
		catch (RESTException ex) {
			log.error("List nodes by service error: ", ex)
		}
	}
	
}
