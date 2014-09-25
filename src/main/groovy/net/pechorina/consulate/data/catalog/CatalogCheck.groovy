package net.pechorina.consulate.data.catalog

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@EqualsAndHashCode
@ToString(includeNames=true)
class CatalogCheck {
	@JsonProperty("Node")
	String node
	
	@JsonProperty("CheckID")
	String id

	@JsonProperty("Name")
	String name

	@JsonProperty("Notes")
	String notes

	@JsonProperty("Status")
	String status

	@JsonProperty("ServiceID")
	String serviceID
}
