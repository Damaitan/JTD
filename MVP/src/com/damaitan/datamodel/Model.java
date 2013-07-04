/**
 * 
 */
package com.damaitan.datamodel;

/**
 * @author admin
 *
 */
public class Model {
	private String name;
	private int id;
	
	public Model(){
		id = -1;
	}
	
	public Model(int id, String name){
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
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	

}
