package net.pechorina.consulate.data.agent

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@EqualsAndHashCode
@ToString(includeNames=true)
class Check {
	@JsonProperty("ID")
	String id

	@JsonProperty("Name")
	String name

	@JsonProperty("Notes")
	String notes

	@JsonProperty("Script")
	String script

	@JsonProperty("Interval")
	String interval

	@JsonProperty("TTL")
	String ttl
}
