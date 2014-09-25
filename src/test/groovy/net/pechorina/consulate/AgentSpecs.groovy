package net.pechorina.consulate

import java.util.Map;

import net.pechorina.consulate.data.agent.AgentService;
import spock.lang.Ignore
import spock.lang.Specification;
import groovy.util.logging.Slf4j;

@Slf4j
class AgentSpecs extends Specification {
	
	Consul consul
	
	def setup() {
		consul = Consul.newClient()	
	}
	
	def "Register service"() {
		when: def r = consul.agent.register("test1", "test1Addr", 12345)
		then: r == true
	}
	
	def "Deregister service"() {
		when: 
			def r1 = consul.agent.register("test2", "test2Name", 12346)
			def r2 = consul.agent.deregister("test2")
		then: 
			r1 == true
			r2 == true
	}
	
	def "List services"() {
		when:
			def r1 = consul.agent.register("test3", "test3Name", 12346)
			def r2 = consul.agent.register("test4", "test4Name", 12346)
			def r3 = consul.agent.register("test5", "test5Name", 12346)
			Map<String, AgentService> m = consul.agent.listLocalServices()
			log.debug(m.toMapString())
		then:
			r1 == true
			m."test4".port == 12346  
	}
	
	def cleanup() {
		["test1", "test3", "test4", "test5"].each {
			consul.agent.deregister(it)
		}
	}
}
