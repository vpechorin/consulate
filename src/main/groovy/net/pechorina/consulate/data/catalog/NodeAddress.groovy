package net.pechorina.consulate.data.catalog

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@EqualsAndHashCode(includes=['node', 'address'])
@ToString(includeNames=true)
class NodeAddress {
	@JsonProperty("Node")
	String node
	
	@JsonProperty("Address")
	String address
}
