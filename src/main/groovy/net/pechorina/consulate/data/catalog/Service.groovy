package net.pechorina.consulate.data.catalog

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@EqualsAndHashCode(includes=['id', 'name', 'port'])
@ToString(includeNames=true)
class Service {
	
	@JsonProperty("Service")
	String name
	
	@JsonProperty("ID")
	String id
	
	@JsonProperty("Port")
	Integer port
	
	@JsonProperty("ServiceTags")
	List<String> serviceTags

}
