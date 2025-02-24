package com.fballtech.scoutbasebeta.dao;

import java.io.IOException;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fballtech.scoutbasebeta.entities.User;
import com.fballtech.scoutbasebeta.localstorage.LocalStorage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class UserDAO {
	private static final String apiURL = "http://localhost:8080/api";
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public static void saveUserToLocalStorage(User user) {
		String sqlStatement = "Insert or Replace Into users (id, firstName, lastName, username, password, email, isAdmin, isActive)"
				+ "Values (?, ?, ?, ?, ?, ?, ?, ?)";
		try(Connection connect = LocalStorage.connect();
				PreparedStatement ps = connect.prepareStatement(sqlStatement)){
			ps.setLong(1, user.getId());
			ps.setString(2, user.getFirstName());
			ps.setString(3, user.getLastName());
			ps.setString(4, user.getUsername());
			ps.setString(5, user.getPassword());
			System.out.println(user.getPassword());
			ps.setString(6, user.getEmail());
			ps.setBoolean(7, user.isAdmin());
			ps.setBoolean(8, user.isActive());
			System.out.println("User saved." + user.getFirstName() + " " + user.getLastName());
			System.out.println(user.getUsername());
			
			ps.addBatch();
			ps.executeBatch();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int saveUserToServer(User user) {
		final ObjectMapper objectMapper = new ObjectMapper();
		final String createUserURL = apiURL + "/users/create_user";
		try {
			URL url = new URL(createUserURL);
			String jsonRequest = objectMapper.writeValueAsString(user);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			
			try(OutputStream os = connection.getOutputStream()) {
				byte [] input = jsonRequest.getBytes("utf-8");
				os.write(input, 0, input.length);
				System.out.println(jsonRequest);
			}
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				System.out.println("Connection successful. User successfully saved to server.");
				return responseCode;
			} else {
				System.out.println("ERROR " + connection.getResponseCode());
			}
			
			if(connection != null) {
				connection.disconnect();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}
	
	public static int updateUserServer(User user) {
		final ObjectMapper objectMapper = new ObjectMapper();
		final String updateURL = apiURL + "/users/updateUser/" + user.getId();
		try {
			URL url = new URL(updateURL);
			String jsonRequest = objectMapper.writeValueAsString(user);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check response
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("User updated successfully.");
                return responseCode;
            } else {
                System.out.println("Failed to update user. Response Code: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return 0;
    }
	
	
	
	
	public static boolean verifyUserFromServer(String username, String inputPassword) {
            String getUserApiURL = apiURL + "/users/search/username/" + username;
            try {
                // Construct the full URL with the username query parameter
                URL url = new URL(getUserApiURL);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                System.out.println("Response Code: " + connection.getResponseCode());

                // If response is OK (HTTP 200)
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Scanner scanner = new Scanner(url.openStream());
                    String inline = "";
                    while (scanner.hasNext()) {
                        inline += scanner.nextLine();
                    }
                    scanner.close();

                    ObjectMapper objectMapper = new ObjectMapper();
                    User user = objectMapper.readValue(inline, User.class);

                    connection.disconnect();

                    // Debugging: Print user details
                    if (user != null && user.getUsername().equals(username)) {
                        System.out.println("User: " + user.getFirstName() + " " + user.getLastName() + " found.");
                        // Verify the password using bcrypt
                        return encoder.matches(inputPassword, user.getPassword());
                    }
                } else {
                    System.err.println("Failed to fetch user. HTTP Code: " + connection.getResponseCode());
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
	}
	
	public static boolean verifyUsernameFromServer(String username) {
		
        String getUserApiURL = apiURL + "/users/search/username/" + username;
        try {
            // Construct the full URL with the username query parameter
            URL url = new URL(getUserApiURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            System.out.println("Response Code: " + connection.getResponseCode());

            // If response is OK (HTTP 200)
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(url.openStream());
                String inline = "";
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();

                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(inline, User.class);

                connection.disconnect();

                // Debugging: Print user details
                if (user != null && user.getUsername().equals(username)) {
                    System.out.println("User: " + user.getFirstName() + " " + user.getLastName() + " found.");
                    return true;
                }
            } else {
                System.err.println("Failed to fetch user. HTTP Code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
	}
	
	public static User getLocalUser(String username) {
		 String sqlStatement = "SELECT * FROM users WHERE username = ?";
		 User user = new User();
		    try (Connection connect = LocalStorage.connect();
		         PreparedStatement ps = connect.prepareStatement(sqlStatement)) {
		        
		        // Set the username parameter before executing the query
		        ps.setString(1, username);
		        try (ResultSet rs = ps.executeQuery()) {
		            while (rs.next()) {
		                User userOutput = new User(
		                		rs.getLong("id"),
		                		rs.getString("firstName"),
		                		rs.getString("lastName"),
		                        rs.getString("username"),
		                        rs.getString("password"),
		                        rs.getString("email"),
		                        rs.getBoolean("isAdmin"),
		                        rs.getBoolean("isActive")// Assuming the column name is hashedPassword
		                );
		                user = userOutput;
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    System.out.println(user.getFirstName());
		    return user;
	}
	
	public static void deleteUserFromLocalStorage(User user) {
		String stmt = "Delete From users Where id = ?";
		try(Connection connect = LocalStorage.connect();
			PreparedStatement ps = connect.prepareStatement(stmt)){
			ps.setLong(1, user.getId());
			ps.executeUpdate();
			System.out.println("User deleted from database.");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int deleteUserFromServer(User user) {
		try {
	        URL url = new URL(apiURL + "/users/delete_user/" + user.getId());
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("DELETE");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setDoOutput(true);
	        
	        int responseCode = connection.getResponseCode();
	        if (responseCode == 200) {
	            System.out.println("Connection successful. User successfully deleted from server.");
	        } else {
	            System.out.println("ERROR " + connection.getResponseCode());
	        }

	        if (connection != null) {
	            connection.disconnect();
	        }
	        return responseCode;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return 0;
	}
	
	public static User getUserFromServer(String username){
		User user = new User();
		try {
			URL url = new URL(apiURL + "/users/search/username/" + username);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			
			System.out.println("Response code: " + connection.getResponseCode());
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				ObjectMapper objectMapper = new ObjectMapper();
				
					user = (User) objectMapper.readValue(
					connection.getInputStream(),
					new TypeReference<User>() {}
					);
				connection.disconnect();
				
				return user;
			}else {
				System.err.println("Failed to fetch reports. HTTP Code: " + connection.getResponseCode());
			}
			connection.disconnect();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public static boolean verifyPassword(String inputPassword, String protectedPassword) throws UnsupportedEncodingException {
		return encoder.matches(inputPassword, protectedPassword);
	}
	
	public static String hashPassword(String password) {
		return encoder.encode(password);
	}
}
