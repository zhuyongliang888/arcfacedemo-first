package com.hoperun.arcfacedemo.arcsoft.cls;

import java.io.Serializable;

public class TestUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	private String name;
	private int age;
	

}
