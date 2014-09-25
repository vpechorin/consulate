package net.pechorina.consulate.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException

import com.fasterxml.jackson.core.type.TypeReference

import groovy.util.logging.Slf4j
import net.pechorina.consulate.Consul;
import net.pechorina.consulate.data.agent.AgentService
import net.pechorina.consulate.data.catalog.CatalogRegistration
import net.pechorina.consulate.data.catalog.CatalogService
import net.pechorina.consulate.data.catalog.NodeAddress
import net.pechorina.consulate.data.catalog.Service

/**
 * @author victor
 *
 */
@Slf4j
class CatalogClient {
	String url
	 
	Consul consul
	
	/**
	 * @param CatalogRegistration reg
	 * @return
	 */
	Boolean register(CatalogRegistration reg) {
		try {
			consul.restTemplate.put(url + "/register", reg)
			return true
		}
		catch (RestClientException ex) {
			log.error("REST error: " + ex)
		}
		return false
	}
	
	/**
	 * @param datacenter
	 * @param node
	 * @param address
	 * @param serviceId
	 * @param serviceName
	 * @param servicePort
	 * @return
	 */
	Boolean register(String datacenter, String node, String address, String serviceId, String serviceName, int servicePort) {
		Service s = new Service(
			id: serviceId,
			name: serviceName,
			port: servicePort
		)
		CatalogRegistration r = new CatalogRegistration(
			datacenter: datacenter,
			node: node,
			address: address,
			service: s
		)
		return register(r)
	}
	
	/**
	 * @param node
	 * @param address
	 * @param serviceId
	 * @param serviceName
	 * @param servicePort
	 * @return
	 */
	Boolean register(String node, String address, String serviceId, String serviceName, int servicePort) {
		Service s = new Service(
			id: serviceId,
			name: serviceName,
			port: servicePort
		)
		CatalogRegistration r = new CatalogRegistration(
			node: node,
			address: address,
			service: s
		)
		return register(r)
	}
	
	
	/**
	 * @param node
	 * @param address
	 * @return
	 */
	Boolean register(String node, String address) {
		CatalogRegistration r = new CatalogRegistration(
			node: node,
			address: address
		)
		return register(r)
	}
	
	/**
	 * @param serviceId
	 * @param node
	 * @param datacenter
	 * @param checkId
	 * @return
	 */
	Boolean deregister(String serviceId, String node, String datacenter, String checkId) {
		def req = ['Node': node]
		if (serviceId) req['ServiceID'] = serviceId
		if (datacenter) req['Datacenter'] = datacenter
		if (checkId) req['CheckID'] = checkId
		try {
			consul.restTemplate.put(url + "/service/deregister", req)
			return true
		}
		catch (RestClientException ex) {
			log.error("REST error: " + ex)
		}
		return false
	}
	
	/**
	 * @param serviceId
	 * @param node
	 * @param datacenter
	 * @return
	 */
	Boolean deregister(String serviceId, String node, String datacenter) {
		return deregister(serviceId, node, datacenter, null)
	}
	
	/**
	 * @param serviceId
	 * @param node
	 * @return
	 */
	Boolean deregister(String serviceId, String node) {
		return deregister(serviceId, node, null, null)
	}
	
	/**
	 * @param node
	 * @return
	 */
	Boolean deregister(String node) {
		return deregister(null, node, null, null)
	}
	
	List<String> listDatacenters() {
		ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<List<String>>() {};
		ResponseEntity<List<String>> response = consul.restTemplate.exchange("$url/datacenters", HttpMethod.GET, null, typeRef);
		if (response.statusCode == HttpStatus.OK) {
			return response.body
		}
		return null
	}
	
	List<NodeAddress> listNodes() {
		ParameterizedTypeReference<List<NodeAddress>> typeRef = new ParameterizedTypeReference<List<NodeAddress>>() {};
		ResponseEntity<List<NodeAddress>> response = consul.restTemplate.exchange("$url/nodes", HttpMethod.GET, null, typeRef);
		if (response.statusCode == HttpStatus.OK) {
			return response.body
		}
	}
	
	/**
	 * @param datacenter
	 * @return
	 */
	Map<String, List<String>> listServices(String datacenter) {
		String uri = url + "/services"
		def q = [:]
		if (datacenter) url += "?dc=$datacenter"
		ParameterizedTypeReference<Map<String, List<String>>> typeRef = new ParameterizedTypeReference<Map<String, List<String>>>() {};
		ResponseEntity<Map<String, List<String>>> response = consul.restTemplate.exchange(uri, HttpMethod.GET, null, typeRef);
		if (response.statusCode == HttpStatus.OK) {
			return response.body
		}
		return null
	}
	
	List<CatalogService> listNodesByService(String serviceId, String datacenter, String tag) {
		String uri = url + "/service/$serviceId"
		def q = [:]
		if (datacenter) q['dc'] = datacenter
		if (tag) q['tag'] = tag
		if (q.size()) {
			uri = uri + '?' + q.collect { it }.join('&')
		}
		
		ParameterizedTypeReference<List<CatalogService>> typeRef = new ParameterizedTypeReference<List<CatalogService>>() {};
		ResponseEntity<List<CatalogService>> response = consul.restTemplate.exchange(uri, HttpMethod.GET, null, typeRef);
		if (response.statusCode == HttpStatus.OK) {
			return response.body
		}
		return null
	}
	
	/**
	 * @param serviceId
	 * @return
	 */
	CatalogService findService(String serviceId) {
		List<CatalogService> l = listNodesByService(serviceId, null, null)
		if (l && l.size() > 0) return l[0]
		return null
	}
	
	/**
	 * @param serviceId
	 * @param datacenter
	 * @return
	 */
	CatalogService findService(String serviceId, String datacenter) {
		List<CatalogService> l = listNodesByService(serviceId, datacenter)
		if (l && l.size() > 0) return l[0]
		return null
	}
}
