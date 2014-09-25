package net.pechorina.consulate.data.catalog

import java.util.List;

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@EqualsAndHashCode
@ToString(includeNames=true)
class CatalogRegistration {
	@JsonProperty("Datacenter")
	String datacenter
	
	@JsonProperty("Node")
	String node
	
	@JsonProperty("Address")
	String address

	@JsonProperty("Service")
	Service service
	
	@JsonProperty("Check")
	CatalogCheck check
}
