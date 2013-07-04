/**
 * 
 */
package com.damaitan.exception;

/**
 * @author admin
 *
 */
public class AccessException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4179338225333683844L;
	public AccessException(){
		super("AccessException");
	}
	public AccessException(String string) {
		// TODO Auto-generated constructor stub
		super("AccessException:" + string);
	}

}
