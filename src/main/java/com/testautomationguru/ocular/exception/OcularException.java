package com.testautomationguru.ocular.exception;

public class OcularException extends RuntimeException {

	private static final long serialVersionUID = 2544120188796893890L;

	public OcularException() {
    }

    public OcularException(String message) {
        super(message);
    }

    public OcularException(Throwable cause) {
        super(cause);
    }

    public OcularException(String message, Throwable cause) {
        super(message, cause);
    }
}