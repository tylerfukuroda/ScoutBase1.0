package com.fballtech.scoutbasebeta.entities;

import com.fballtech.scoutbasebeta.Position;

public class Player {
	
	private Long id;
	private String firstName;
	private String lastName;
	private Position position;
	private Integer height;
	private Integer weight;
	private Integer age;
	private Integer draftClass;
	private String school;
	
	public Player(Long id, String firstName, String lastName, Position position, int height, int weight, int draftClass, String school) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
		this.height = height;
		this.weight = weight;
		this.draftClass = draftClass;
		this.school = school;
	}
	
	public Player(Long id, String firstName, String lastName, Position position) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
	}
	
	public Player() {
		id = (long) 0;
		firstName = "";
		lastName = "";
		position = position.QB;
		height = 0000;
		weight = 000;
		age = 0;
		draftClass = 2025;
		school = "";
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Position getPosition() {
		return position;
	}

	public Integer getHeight() {
		return height;
	}

	public Integer getWeight() {
		return weight;
	}

	public Integer getAge() {
		return age;
	}

	public Integer getDraftClass() {
		return draftClass;
	}

	public String getSchool() {
		return school;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setDraftClass(Integer draftClass) {
		this.draftClass = draftClass;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
}
