package net.pechorina.consulate

import net.pechorina.consulate.data.KeyValue
import net.pechorina.consulate.data.catalog.CatalogService
import spock.lang.Ignore
import spock.lang.Specification;
import groovy.util.logging.Slf4j;

@Slf4j
class KeyValueSpecs extends Specification {
	
	Consul consul
	
	def setup() {
		consul = Consul.newClient()	
	}
	
	def "Step0"() {
		when:
			KeyValue kv = new KeyValue(value: "djEyMw==", key: "kkk")
		then:
			kv.getValueAsString() == "v123"
	}
	
	def "Step1"() {
		when: 
			consul.kv.put("test/k1", "v123")
			def s = consul.kv.getValue("test/k1") 
		then: 
			s == "v123"
	}
	
	def "Step3"() {
		when:
			consul.kv.put("test/k1", "v123")
			consul.kv.put("test/k2", "v124")
			consul.kv.put("test/k3", "v125")
			def l = consul.kv.getValuesByPrefix("test/")
		then:
			l == ["v123", "v124", "v125"]
	}
	
	def "Step4"() {
		when:
			consul.kv.put("test/k11", "v123")
			consul.kv.put("test/k21", "v124")
			consul.kv.put("test/k31", "v125")
			def l = consul.kv.getEntriesAsMap("test/")
		then:
			l == ["test/k11": "v123", "test/k21":"v124", "test/k31":"v125"]
	}
	
	def cleanup() {
		consul.kv.deleteRecursive("test/")
	}
}
