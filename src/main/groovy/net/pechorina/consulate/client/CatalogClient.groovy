package net.pechorina.consulate.client

import com.fasterxml.jackson.core.type.TypeReference
import groovy.util.logging.Slf4j
import net.pechorina.consulate.Consul
import net.pechorina.consulate.data.catalog.CatalogRegistration
import net.pechorina.consulate.data.catalog.CatalogService
import net.pechorina.consulate.data.catalog.NodeAddress
import net.pechorina.consulate.data.catalog.Service
import net.pechorina.consulate.exceptions.RESTException
import net.pechorina.consulate.utils.JsonMapHelper

@Slf4j
class CatalogClient {
    String url

    Consul consul

    /**
     * @param CatalogRegistration reg
     * @return
     */
    Boolean register(CatalogRegistration reg) {
        try {
            consul.httpClient.put(url + "/register", JsonMapHelper.toJson(consul, reg))
            return true
        }
        catch (RESTException ex) {
            log.error("Registration error", ex)
        }
        return false
    }

    /**
     * @param datacenter
     * @param node
     * @param address
     * @param serviceId
     * @param serviceName
     * @param servicePort
     * @return
     */
    Boolean register(String datacenter, String node, String address, String serviceId, String serviceName, int servicePort) {
        Service s = new Service(
                id: serviceId,
                name: serviceName,
                port: servicePort
        )
        CatalogRegistration r = new CatalogRegistration(
                datacenter: datacenter,
                node: node,
                address: address,
                service: s
        )
        return register(r)
    }

    /**
     * @param node
     * @param address
     * @param serviceId
     * @param serviceName
     * @param servicePort
     * @return
     */
    Boolean register(String node, String address, String serviceId, String serviceName, int servicePort) {
        Service s = new Service(
                id: serviceId,
                name: serviceName,
                port: servicePort
        )
        CatalogRegistration r = new CatalogRegistration(
                node: node,
                address: address,
                service: s
        )
        return register(r)
    }

    /**
     * @param node
     * @param address
     * @return
     */
    Boolean register(String node, String address) {
        CatalogRegistration r = new CatalogRegistration(
                node: node,
                address: address
        )
        return register(r)
    }

    /**
     * @param serviceId
     * @param node
     * @param datacenter
     * @param checkId
     * @return
     */
    Boolean deregister(String serviceId, String node, String datacenter, String checkId) {
        def req = ['Node': node]
        if (serviceId) req['ServiceID'] = serviceId
        if (datacenter) req['Datacenter'] = datacenter
        if (checkId) req['CheckID'] = checkId
        try {
            consul.httpClient.put(url + "/service/deregister", JsonMapHelper.toJson(consul, req))
            return true
        }
        catch (RESTException ex) {
            log.error("Deregistration error: ", ex)
        }
        return false
    }

    /**
     * @param serviceId
     * @param node
     * @param datacenter
     * @return
     */
    Boolean deregister(String serviceId, String node, String datacenter) {
        return deregister(serviceId, node, datacenter, null)
    }

    /**
     * @param serviceId
     * @param node
     * @return
     */
    Boolean deregister(String serviceId, String node) {
        return deregister(serviceId, node, null, null)
    }

    /**
     * @param node
     * @return
     */
    Boolean deregister(String node) {
        return deregister(null, node, null, null)
    }

    List<String> listDatacenters() {

        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {}
        try {
            String json = consul.httpClient.get("$url/datacenters")
            return JsonMapHelper.fromJson(consul, json, typeRef)
        }
        catch (RESTException ex) {
            log.error("List datacenters error: ", ex)
        }
        return []
    }

    List<NodeAddress> listNodes() {
        TypeReference<List<NodeAddress>> typeRef = new TypeReference<List<NodeAddress>>() {}
        try {
            String json = consul.httpClient.get("$url/nodes")
            return JsonMapHelper.fromJson(consul, json, typeRef)
        }
        catch (RESTException ex) {
            log.error("List nodes error: ", ex)
        }
        return []
    }

    /**
     * @param datacenter
     * @return
     */
    Map<String, List<String>> listServices(String datacenter) {
        String uri = url + "/services"
        if (datacenter) uri += "?dc=$datacenter"

        TypeReference<Map<String, List<String>>> typeRef = new TypeReference<Map<String, List<String>>>() {}
        try {
            String json = consul.httpClient.get(uri)
            return JsonMapHelper.fromJson(consul, json, typeRef)
        }
        catch (RESTException ex) {
            log.error("List nodes error: ", ex)
        }
        return null
    }

    List<CatalogService> listNodesByService(String serviceId, String datacenter, String tag) {
        String uri = url + "/service/$serviceId"
        def q = [:]
        if (datacenter) q['dc'] = datacenter
        if (tag) q['tag'] = tag
        if (q.size()) {
            uri = uri + '?' + q.collect { it }.join('&')
        }

        TypeReference<List<CatalogService>> typeRef = new TypeReference<List<CatalogService>>() {}
        try {
            String json = consul.httpClient.get(uri)
            return JsonMapHelper.fromJson(consul, json, typeRef)
        }
        catch (RESTException ex) {
            log.error("List nodes by service error: ", ex)
        }
        return null
    }

    /**
     * @param serviceId
     * @return
     */
    CatalogService findService(String serviceId) {
        List<CatalogService> l = listNodesByService(serviceId, null, null)
        if (l && l.size() > 0) return l[0]
        return null
    }

    /**
     * @param serviceId
     * @param datacenter
     * @return
     */
    CatalogService findService(String serviceId, String datacenter) {
        List<CatalogService> l = listNodesByService(serviceId, datacenter, null)
        if (l && l.size() > 0) return l[0]
        return null
    }
}
