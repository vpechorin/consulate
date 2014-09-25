package net.pechorina.consulate.data.catalog

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@EqualsAndHashCode(includes=['node', 'address', 'serviceId', 'serviceName', 'servicePort'])
@ToString(includeNames=true)
class CatalogService {
	@JsonProperty("Node")
	String node
	
	@JsonProperty("Address")
	String address
	
	@JsonProperty("ServiceName")
	String serviceName
	
	@JsonProperty("ServiceID")
	String serviceId
	
	@JsonProperty("ServicePort")
	int servicePort
	
	@JsonProperty("ServiceTags")
	List<String> serviceTags
}
