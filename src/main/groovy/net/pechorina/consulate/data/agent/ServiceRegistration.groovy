package net.pechorina.consulate.data.agent

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@EqualsAndHashCode
@ToString(includeNames=true)
class ServiceRegistration {
	@JsonProperty("Name")
	String name
	
	@JsonProperty("ID")
	String id
	
	@JsonProperty("Port")
	Integer port
	
	@JsonProperty("Tags")
	List<String> tags
	
	@JsonProperty("Check")
	Check check
}
