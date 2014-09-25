package net.pechorina.consulate

import net.pechorina.consulate.data.catalog.CatalogService
import spock.lang.Ignore
import spock.lang.Specification;
import groovy.util.logging.Slf4j;

@Slf4j
class CatalogSpecs extends Specification {
	
	Consul consul
	
	def setup() {
		consul = Consul.newClient()	
	}
	
	def "List datacenters"() {
		when: 
			def r = consul.catalog.listDatacenters()
			log.debug(r)
		then: 
			r.size() > 0
	}
	
	def "Register node"() {
		when: 
			def r1 = consul.catalog.register("localhost", "127.0.0.1")
		then: 
			r1 == true
	}
	
	def "Register service and locate it"() {
		when:
			def r1 = consul.catalog.register("localhost", "127.0.0.1", 'srv2', 'srv2', 8080)
			CatalogService s = consul.catalog.findService('srv2')
		then:
			r1 == true
			s != null
			s.serviceId == "srv2"
			s.servicePort == 8080
	}
	
	def cleanup() {
		consul.catalog.deregister("localhost")
	}
}
