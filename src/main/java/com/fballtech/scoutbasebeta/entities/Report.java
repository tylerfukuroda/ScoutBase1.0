package com.fballtech.scoutbasebeta.entities;

import java.time.LocalDateTime;

import com.fballtech.scoutbasebeta.Position;


public class Report {
	
	private Long id;
	private Double grade;
	private LocalDateTime dateCreated;
	private String summary;
	private Long userId;
	private Long playerId;
	private String playerFirstName;
	private String playerLastName;
	private Integer draftClass;
	private Position playerPosition;
	private Integer playerHeight;
	private Integer playerWeight;
	private String playerSchool;
	
	
	public Report(Long id, Double grade, LocalDateTime dateCreated, String summary, Long userId, Long playerId, String playerFirstName, String playerLastName, Integer draftClass, Position playerPosition, Integer playerHeight, Integer playerWeight, String playerSchool) {
		this.id = id;
		this.grade = grade;
		this.dateCreated = dateCreated;
		this.summary = summary;
		this.userId = userId;
		this.playerId = playerId;
		this.playerFirstName = playerFirstName;
		this.playerLastName = playerLastName;
		this.draftClass = draftClass;
		this.playerPosition = playerPosition;
		this.playerHeight = playerHeight;
		this.playerWeight = playerWeight;
		this.playerSchool = playerSchool;
	}


	public Report(Long id, Long userId, Long playerId) {
		this.id = id;
		this.userId = userId;
		this.playerId = playerId;
		grade = 0.0;
		dateCreated = null;
		summary = "";
	}
	
	public Report() {
		id = (long) 0;
		userId = (long) 0;
		playerId = (long) 0;
		grade = 0.0;
		dateCreated = null;
		summary = "";
	}


	
	
	public Integer getPlayerHeight() {
		return playerHeight;
	}


	public Integer getPlayerWeight() {
		return playerWeight;
	}


	public String getPlayerSchool() {
		return playerSchool;
	}


	public void setPlayerHeight(Integer playerHeight) {
		this.playerHeight = playerHeight;
	}


	public void setPlayerWeight(Integer playerWeight) {
		this.playerWeight = playerWeight;
	}


	public void setPlayerSchool(String playerSchool) {
		this.playerSchool = playerSchool;
	}


	public String getPlayerFirstName() {
		return playerFirstName;
	}


	public String getPlayerLastName() {
		return playerLastName;
	}


	public Integer getDraftClass() {
		return draftClass;
	}


	public Position getPlayerPosition() {
		return playerPosition;
	}


	public void setPlayerFirstName(String playerFirstName) {
		this.playerFirstName = playerFirstName;
	}


	public void setPlayerLastName(String playerLastName) {
		this.playerLastName = playerLastName;
	}


	public void setDraftClass(Integer draftClass) {
		this.draftClass = draftClass;
	}


	public void setPlayerPosition(Position playerPosition) {
		this.playerPosition = playerPosition;
	}


	public Long getId() {
		return id;
	}


	public Double getGrade() {
		return grade;
	}


	public LocalDateTime getDateCreated() {
		return dateCreated;
	}


	public String getSummary() {
		return summary;
	}


	public Long getUserId() {
		return userId;
	}


	public Long getPlayerId() {
		return playerId;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void setGrade(Double grade) {
		this.grade = grade;
	}


	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}
	
}
