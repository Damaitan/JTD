/**
 * 
 */
package com.damaitan.exception;

/**
 * @author admin
 *
 */
public class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1722642835910038068L;

	public BaseException(){
		super("BaseException");
	}

	public BaseException(String string) {
		// TODO Auto-generated constructor stub
		super("BaseException:" + string);
	}
	
	public BaseException(String message,Throwable cause) {
		// TODO Auto-generated constructor stub
		super("BaseException:" + message,cause);
	}
	
	public BaseException(Throwable cause) {
		// TODO Auto-generated constructor stub
		super("BaseException",cause);
	}
	
}
