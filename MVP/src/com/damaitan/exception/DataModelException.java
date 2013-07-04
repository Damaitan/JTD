package com.damaitan.exception;

public class DataModelException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4060528593764853656L;
	public DataModelException(){
		super("DataModelException");
	}
	public DataModelException(String string) {
		// TODO Auto-generated constructor stub
		super("DataModelException" + string);
	}
	
	public DataModelException(String message,Throwable cause) {
		// TODO Auto-generated constructor stub
		super("DataModelException:" + message,cause);
	}
	
	public DataModelException(Throwable cause) {
		// TODO Auto-generated constructor stub
		super("DataModelException",cause);
	}
}
