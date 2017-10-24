package com.hubciti.common.exception;

/**
 * Generic exception class. this will be thrown whenevr excpetions occurs in the
 * Service and controller layers of the application. This exception has three
 * constructors.
 * 
 * @author dileepa_cc
 */
public class HubCitiServiceException extends Exception {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor with two arguments.
	 * 
	 * @param message
	 *            the message to be displayed to the user
	 * @param cause
	 *            the cause of the exception
	 */
	public HubCitiServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * constructor with one argument.
	 * 
	 * @param cause
	 *            the cause of the exception
	 */
	public HubCitiServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * no-arg constructor.
	 */
	public HubCitiServiceException() {
	}

	/**
	 * Single argument constructor.
	 * 
	 * @param s
	 *            message to be displayed
	 */
	public HubCitiServiceException(String s) {
		super(s);
	}

}
