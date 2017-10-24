package com.hubciti.common.exception;

/**
 * This DAO exception this exception will be thrown in DAO layers.
 * 
 * @author dileepa_cc
 */
public class HubCitiWebSqlException extends Exception {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * errorCode declared as int.
	 */
	private int errorCode;

	/**
	 * This method returns the error code of exception.
	 * 
	 * @return the errorCode
	 */
	public final int getErrorCode() {
		return errorCode;
	}

	/**
	 * this method can be used to set error code for the excpetion.
	 * 
	 * @param errorCode
	 *            the errorCode to set
	 */
	public final void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * constructor with two arguments.
	 * 
	 * @param message
	 *            the message to be displayed to the user
	 * @param cause
	 *            the cause of the exception
	 */
	public HubCitiWebSqlException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * constructor.
	 * 
	 * @param errorMessage
	 *            .
	 * @param errorCode
	 *            .
	 * @param errorCause
	 *            .
	 */
	public HubCitiWebSqlException(String errorMessage, int errorCode, Throwable errorCause) {
		super(errorMessage, errorCause);
		this.errorCode = errorCode;
	}

	/**
	 * constructor with one argument.
	 * 
	 * @param cause
	 *            the cause of the exception
	 */

	public HubCitiWebSqlException(Throwable cause) {
		super(cause);
	}

	/**
	 * Default constructor.
	 */
	public HubCitiWebSqlException() {
	}

	/**
	 * Single argument constructor.
	 * 
	 * @param s
	 *            message to be displayed
	 */

	public HubCitiWebSqlException(String s) {
		super(s);
	}

	/**
	 * constructor.
	 * 
	 * @param errorMessage
	 *            .
	 * @param errorCode
	 *            .
	 */
	public HubCitiWebSqlException(String errorMessage, int errorCode) {
		super(errorMessage);
		this.errorCode = errorCode;
	}

}
