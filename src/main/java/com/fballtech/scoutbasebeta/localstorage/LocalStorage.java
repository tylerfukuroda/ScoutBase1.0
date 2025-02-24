package com.fballtech.scoutbasebeta.localstorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LocalStorage {
	
	private static final String databaseUrl = "jdbc:sqlite:scoutbase.db";

	
	public static Connection connect() {
		try {
			System.out.println("Connected to local storage.");
			return DriverManager.getConnection(databaseUrl);
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void createPlayersTable() {
		String sqlStmt = "CREATE TABLE IF NOT EXISTS players (" +
						"id LONG PRIMARY KEY, " +
						"firstName TEXT, " +
						"lastName TEXT, " +
						"position TEXT, " +
						"height INTEGER, " +
						"weight INTEGER, " +
						"age INTEGER, " +
						"draftClass INTEGER, " +
						"school TEXT)";
		
		try(Connection conn = connect();
			Statement statement = conn.createStatement()) {
			statement.execute(sqlStmt);
			System.out.println("players table created.");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void dropTable() {
		String statement = "DROP TABLE users";
		try(Connection conn = connect();
			Statement stmt = conn.createStatement()){
			stmt.execute(statement);
			System.out.println("Table dropped");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createReportsTable() {
		String sqlStmt = "CREATE TABLE IF NOT EXISTS reports ("
				+ "id INTEGER PRIMARY KEY, "
				+ "userId INTEGER, "
				+ "playerId INTEGER, "
				+ "grade FLOAT, "
				+ "dateCreated DATETIME, "
				+ "summary TEXT, "
				+ "playerFirstName TEXT, "
				+ "playerLastName TEXT, "
				+ "draftClass INTEGER, "
				+ "playerPosition TEXT, "
				+ "playerHeight INTEGER, "
				+ "playerWeight INTEGER, "
				+ "playerSchool TEXT)";
		try(Connection conn = connect();
			Statement statement = conn.createStatement()){
			statement.execute(sqlStmt);
			System.out.println("Reports table created.");
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createUsersTable() {
		String sqlStatement = "CREATE TABLE IF NOT EXISTS users("
				+ "id INTEGER PRIMARY KEY, "
				+ "firstName TEXT, "
				+ "lastName TEXT, "
				+ "username TEXT, "
				+ "password BLOB, "
				+ "email TEXT, "
				+ "isAdmin BOOLEAN, "
				+ "isActive BOOLEAN)";
		try(Connection conn = connect();
				Statement statement = conn.createStatement()){
				statement.execute(sqlStatement);
				System.out.println("Users table created.");
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static void printSQL(Long userId) {
		String query = "SELECT * FROM reports where userId = ?";
		        
		        try (Connection conn = connect();
		        		
		             Statement stmt = conn.createStatement();
		             ResultSet rs = stmt.executeQuery(query)) {
		System.out.println(userId);
		            while (rs.next()) {
		                long id = rs.getLong("userId");
		                String summary = rs.getString("summary");
		   
		                
		                // Print the values to the console
		                System.out.println("ID: " + id);
		              System.out.println(summary);
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
			}
}
