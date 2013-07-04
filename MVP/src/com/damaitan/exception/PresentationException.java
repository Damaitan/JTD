/**
 * 
 */
package com.damaitan.exception;

/**
 * @author admin
 *
 */
public class PresentationException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5895809347557589623L;
	public PresentationException(){
		super("PresentationException");
	}
	public PresentationException(String string) {
		// TODO Auto-generated constructor stub
		super("PresentationException" + string);
	}
	
	public PresentationException(String message,Throwable cause) {
		// TODO Auto-generated constructor stub
		super("PresentationException:" + message,cause);
	}
	
	public PresentationException(Throwable cause) {
		// TODO Auto-generated constructor stub
		super("PresentationException",cause);
	}

}
