package net.pechorina.consulate.data.agent

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
class AgentService {
	@JsonProperty("Service")
	String name
	
	@JsonProperty("ID")
	String id
	
	@JsonProperty("Port")
	Integer port
	
	@JsonProperty("Tags")
	List<String> tags
}
