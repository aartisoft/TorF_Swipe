package com.amine.torf.pojo;

public class Scoredata {
	
	int id;
	String name, score;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}

	public Scoredata(String name, String score) {
		super();
	
		this.name = name;
		this.score = score;
	}
	public Scoredata() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
