package net.pechorina.consulate.data.catalog

import java.util.Map;

import net.pechorina.consulate.data.catalog.Service;
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@EqualsAndHashCode(includes=['node'])
@ToString(includeNames=true)
class CatalogNode {
	@JsonProperty("Node")
	NodeAddress node
	
	@JsonProperty("Services")
	Map<String, Service> services
}
