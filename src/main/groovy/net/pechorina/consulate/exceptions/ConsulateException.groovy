package net.pechorina.consulate.exceptions

import groovy.transform.CompileStatic

@CompileStatic
class ConsulateException extends Exception {
	ConsulateException(String msg) {
		super(msg)
	}
}
