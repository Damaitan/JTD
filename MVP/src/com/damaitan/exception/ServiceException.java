/**
 * 
 */
package com.damaitan.exception;

/**
 * @author admin
 *
 */
public class ServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4197355139248658643L;
	public ServiceException(){
		super("ServiceException");
	}
	public ServiceException(String string) {
		// TODO Auto-generated constructor stub
		super("ServiceException" + string);
	}
	
	public ServiceException(String message,Throwable cause) {
		// TODO Auto-generated constructor stub
		super("ServiceException:" + message,cause);
	}
	
	public ServiceException(Throwable cause) {
		// TODO Auto-generated constructor stub
		super("ServiceException",cause);
	}

}
