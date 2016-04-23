package net.pechorina.consulate.utils

import net.pechorina.consulate.Consul

class JsonMapHelper {
    static toJson(Consul consul, source) {
        return consul.objectMapper.writeValueAsString(source);
    }

    static fromJson(Consul consul, sourceJson, valueType) {
        return consul.objectMapper.readValue(sourceJson, valueType);
    }
}
