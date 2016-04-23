package net.pechorina.consulate.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@JsonIgnoreProperties(ignoreUnknown = true)
@CompileStatic
@EqualsAndHashCode(includes=['key', 'value', 'flags'])
@ToString(includeNames=true)
class KeyValue {
	@JsonProperty("CreateIndex")
	Long createIndex
	
	@JsonProperty("ModifyIndex")
	Long modifyIndex
	
	@JsonProperty("LockIndex")
	Long lockIndex
	
	@JsonProperty("Key")
	String key
	
	@JsonProperty("Flags")
	Integer flags
	
	@JsonProperty("Value")
	String value
	
	@JsonProperty("Session")
	String session
	
	String getValueAsString() {
		if (value) {
			byte[] decodedByteArray = value.decodeBase64()
			return new String(decodedByteArray)
		}
		return null
	}
}
