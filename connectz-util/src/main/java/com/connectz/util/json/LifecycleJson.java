package com.connectz.util.json;

public class LifecycleJson  extends JsonObjectImpl {
	private String name;
	private String clazz;
	private int index;
	private String status; // ACTIVE, STOP
	private boolean active;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LifecycleJson(String name,String clazz,int index) {
		super(null);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.clazz = clazz;
		this.index = index;
	}
	
	public LifecycleJson(String json) {
		super(json);
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isActive() {
		return active;
	}

	public LifecycleJson setActive(boolean active) {
		this.active = active;
		return this;
	}
}
