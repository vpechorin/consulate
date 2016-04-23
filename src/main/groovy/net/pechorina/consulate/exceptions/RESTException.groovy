package net.pechorina.consulate.exceptions

import groovy.transform.CompileStatic

@CompileStatic
class RESTException extends Exception {

    RESTException(String msg) {
        super(msg)
    }

    RESTException(String msg, Throwable e) {
        super(msg, e)
    }
}
