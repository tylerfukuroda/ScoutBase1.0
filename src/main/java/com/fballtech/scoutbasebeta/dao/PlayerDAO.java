package com.fballtech.scoutbasebeta.dao;

import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fballtech.scoutbasebeta.Position;
import com.fballtech.scoutbasebeta.entities.Player;
import com.fballtech.scoutbasebeta.localstorage.LocalStorage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class PlayerDAO {
    
    private static final String apiURL = "http://localhost:8080/api/players";

    
    public static ObservableList<Player> getAllPlayers() {
        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            
            System.out.println("Response Code: " + connection.getResponseCode());
            
            // If response is OK (HTTP 200)
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                
                // Deserialize JSON response into List<Player>
                List<Player> playerList = objectMapper.readValue(
                    connection.getInputStream(),
                    new TypeReference<List<Player>>() {}
                );

                connection.disconnect();
                
                ObservableList<Player> players = FXCollections.observableArrayList(playerList);
                // Debugging: Print all players
                players.forEach(player -> 
                    System.out.println(player.getFirstName() + " " + player.getLastName() + " added.")
                );

                return players;
            } else {
                System.err.println("Failed to fetch players. HTTP Code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return FXCollections.observableArrayList();

 
    }
    
    public static void addPlayersToLocalStorage(ObservableList<Player> players) {
    	String sqlStmt = "Insert Or Replace Into players (id, firstName, lastName, position, height, weight, age, draftClass, school)" +
    					"Values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	
    	try(Connection connect = LocalStorage.connect();
    			PreparedStatement preparedStatement = connect.prepareStatement(sqlStmt)){
    		for(Player player : players) {
    			preparedStatement.setLong(1, player.getId());
    			preparedStatement.setString(2, player.getFirstName());
    			preparedStatement.setString(3, player.getLastName());
    			preparedStatement.setString(4, player.getPosition().toString());

                if (player.getHeight() != null) {
                    preparedStatement.setInt(5, player.getHeight());
                } else {
                    preparedStatement.setNull(5, java.sql.Types.INTEGER);
                }
                
                if (player.getWeight() != null) {
                    preparedStatement.setInt(6, player.getWeight());
                } else {
                    preparedStatement.setNull(6, java.sql.Types.INTEGER);
                }
                
                if (player.getAge() != null) {
                	preparedStatement.setInt(7, player.getAge());
                } else {
                	preparedStatement.setNull(7, java.sql.Types.INTEGER);
                }

                if (player.getDraftClass() != null) {
                    preparedStatement.setInt(8, player.getDraftClass());
                } else {
                    preparedStatement.setNull(8, java.sql.Types.INTEGER);
                }
                if (player.getSchool() != null) {
                	preparedStatement.setString(9, player.getSchool());
                } else {
                	preparedStatement.setNull(9, java.sql.Types.LONGVARCHAR);
                }
    			
    			preparedStatement.addBatch();
    		}
    		
    		preparedStatement.executeBatch();
    		System.out.println("Players added to local storage.");
    		
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    public static ObservableList<Player> getAllPlayersFromLocalStorage(){
    	ObservableList<Player> players = FXCollections.observableArrayList();
    	String sqlStmt = "Select * From players";
    	
    	try(Connection connect = LocalStorage.connect();
    		PreparedStatement preparedStatement = connect.prepareStatement(sqlStmt);
    		ResultSet rs = preparedStatement.executeQuery()){
    		
    		while(rs.next()) {
    			Player player = new Player(
    					rs.getLong("id"),
    					rs.getString("firstName"),
    					rs.getString("lastName"),
    					Position.valueOf(rs.getString("position")),
    					rs.getInt("height"),
    					rs.getInt("weight"),
    					rs.getInt("draftClass"),
    					rs.getString("school")
    					);
    			players.add(player);
    			System.out.println(player.getFirstName() + " " + player.getLastName());
    		} 
    	}catch(SQLException e) {
    			e.printStackTrace();
    		}
    		return players;
    	}
    public static void savePlayerToLocalStorage(Player player) {
    	
        	String sqlStmt = "Insert Or Replace Into players (id, firstName, lastName, position, height, weight, age, draftClass, school)" +
        					"Values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        	
        	try(Connection connect = LocalStorage.connect();
        			PreparedStatement preparedStatement = connect.prepareStatement(sqlStmt)){
        		{
        			preparedStatement.setLong(1, player.getId());
        			preparedStatement.setString(2, player.getFirstName());
        			preparedStatement.setString(3, player.getLastName());
        			preparedStatement.setString(4, player.getPosition().toString());

                    if (player.getHeight() != null) {
                        preparedStatement.setInt(5, player.getHeight());
                    } else {
                        preparedStatement.setNull(5, java.sql.Types.INTEGER);
                    }
                    
                    if (player.getWeight() != null) {
                        preparedStatement.setInt(6, player.getWeight());
                    } else {
                        preparedStatement.setNull(6, java.sql.Types.INTEGER);
                    }
                    
                    if (player.getAge() != null) {
                    	preparedStatement.setInt(7, player.getAge());
                    } else {
                    	preparedStatement.setNull(7, java.sql.Types.INTEGER);
                    }

                    if (player.getDraftClass() != null) {
                        preparedStatement.setInt(8, player.getDraftClass());
                    } else {
                        preparedStatement.setNull(8, java.sql.Types.INTEGER);
                    }
                    if (player.getSchool() != null) {
                    	preparedStatement.setString(9, player.getSchool());
                    } else {
                    	preparedStatement.setNull(9, java.sql.Types.LONGVARCHAR);
                    }
        			
        			preparedStatement.addBatch();
        		}
        		
        		preparedStatement.executeBatch();
        		System.out.println("Player added to local storage.");
        		
        	} catch(SQLException e) {
        		e.printStackTrace();
        	}
    }
        
    
  }
    
