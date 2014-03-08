/**
 * 
 */
package com.damaitan.datamodel;

/**
 * @author admin
 *
 */
public class Model {
	public final static long invalidId = 0xFFFF; 
	public final static String invalidStr = "EMPTY";
	
	private String name;
	private long id;  
	
	public Model(){
		id = invalidId;
		name = invalidStr;
	}
	
	public Model(long id, String name){
		setId(id);
		setName(name);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	

}
