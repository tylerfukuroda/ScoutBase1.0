package com.fballtech.scoutbasebeta.dao;

import java.io.IOException;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fballtech.scoutbasebeta.Position;
import com.fballtech.scoutbasebeta.entities.Report;
import com.fballtech.scoutbasebeta.localstorage.LocalStorage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportDAO {
	
	private static final String apiURL= "http://localhost:8080/api/reports";
	
	public static ObservableList<Report> getAllReports(){
		try {
			URL url = new URL(apiURL + "/all_reports");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			
			System.out.println("Response code: " + connection.getResponseCode());
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				ObjectMapper objectMapper = new ObjectMapper();
				
				List<Report> reportList = objectMapper.readValue(
					connection.getInputStream(),
					new TypeReference<List<Report>>() {}
					);
				connection.disconnect();
				
				ObservableList<Report> reports = FXCollections.observableArrayList(reportList);
				reports.forEach(report->
				System.out.println(report.getPlayerId() + " imported.")
				);
				
				return reports;
			}else {
				System.err.println("Failed to fetch reports. HTTP Code: " + connection.getResponseCode());
			}
			connection.disconnect();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return FXCollections.observableArrayList();
	}
	
	public static void addReportsToLocalStorage(ObservableList<Report> reports) {
		String sqlStmt = "Insert or Replace Into reports (id, userId, playerId, grade, dateCreated, summary, playerFirstName, playerLastName, draftClass, playerPosition, playerHeight, playerWeight, playerSchool)" +
						"Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try(Connection connect = LocalStorage.connect();
				PreparedStatement preparedStatement = connect.prepareStatement(sqlStmt)){
			for(Report report : reports) {
				preparedStatement.setLong(1, report.getId());
				preparedStatement.setLong(2, report.getUserId());
				preparedStatement.setLong(3, report.getPlayerId());
				if(report.getGrade() != null) {
					preparedStatement.setDouble(4, report.getGrade());
				} else {
					preparedStatement.setNull(4, java.sql.Types.DOUBLE);
				}
				
				if(report.getDateCreated() != null) {
					preparedStatement.setObject(5, report.getDateCreated());
				} else {
					preparedStatement.setNull(5, java.sql.Types.TIMESTAMP);
				}
				if(report.getSummary() != null) {
					preparedStatement.setString(6, report.getSummary());
				} else {
					preparedStatement.setNull(6, java.sql.Types.VARCHAR);
				}
				if(report.getPlayerFirstName() != null) {
					preparedStatement.setString(7, report.getPlayerFirstName());
				} else {
					preparedStatement.setNull(7, java.sql.Types.VARCHAR);
				}
				if(report.getPlayerLastName() != null) {
					preparedStatement.setString(8, report.getPlayerLastName());
				} else {
					preparedStatement.setNull(8, java.sql.Types.VARCHAR);
				}
				if(report.getDraftClass() != null) {
					preparedStatement.setInt(9, report.getDraftClass());
				} else {
					preparedStatement.setNull(9, java.sql.Types.INTEGER);
				}
				if(report.getPlayerPosition() != null) {
					preparedStatement.setString(10, report.getPlayerPosition().toString());
				} else {
					preparedStatement.setNull(10, java.sql.Types.VARCHAR);
				}
				if(report.getPlayerHeight() != null) {
					preparedStatement.setInt(11, report.getPlayerHeight());
				} else {
					preparedStatement.setNull(11, java.sql.Types.INTEGER);
				}
				if(report.getPlayerWeight() != null) {
					preparedStatement.setInt(12, report.getPlayerWeight());
				} else {
					preparedStatement.setNull(12, java.sql.Types.INTEGER);
				}
				if(report.getPlayerSchool() != null) {
					preparedStatement.setString(13, report.getPlayerSchool());
				} else {
					preparedStatement.setNull(13, java.sql.Types.VARCHAR);
				}
				
				preparedStatement.addBatch();
			};
			preparedStatement.executeBatch();
			System.out.println("Reports added to local storage");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ObservableList<Report> getAllReportsFromLocalStorage(){
		ObservableList<Report> reports = FXCollections.observableArrayList();
		String stmt = "Select * From reports";
		System.out.println("Get all reports called.");
		try(Connection connect = LocalStorage.connect();
			PreparedStatement preparedStatement = connect.prepareStatement(stmt);
			ResultSet rs = preparedStatement.executeQuery()) {
			
			while(rs.next()) {
				Report report = new Report(
					rs.getLong("id"),
					rs.getDouble("grade"),
					rs.getObject("dateCreated", LocalDateTime.class),
					rs.getString("summary"),
					rs.getLong("userId"),
					rs.getLong("playerId"),
					rs.getString("playerFirstName"),
					rs.getString("playerLastName"),
					rs.getInt("draftClass"),
					Position.valueOf(rs.getString("playerPosition")),
					rs.getInt("playerHeight"),
					rs.getInt("playerWeight"),
					rs.getString("playerSchool")
					);
				reports.add(report);
				
			} 
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return reports;
	}
	
	public static ObservableList<Report> getServerReportByUserId(Long userId) {
		try {
			URL url = new URL(apiURL + "/reports/user_id/" + userId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			
			System.out.println("Response code: " + connection.getResponseCode());
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				ObjectMapper objectMapper = new ObjectMapper();
				
				List<Report> reportList = objectMapper.readValue(
					connection.getInputStream(),
					new TypeReference<List<Report>>() {}
					);
				connection.disconnect();
				
				ObservableList<Report> reports = FXCollections.observableArrayList(reportList);
				reports.forEach(report->
				System.out.println(report.getPlayerId() + " imported.")
				);
				
				return reports;
			}else {
				System.err.println("Failed to fetch reports. HTTP Code: " + connection.getResponseCode());
			}
			connection.disconnect();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return FXCollections.observableArrayList();
	}
	
	
	
	public static void saveReportToLocalStorage(Report report) {
		String sqlStmt = "Insert or Replace Into reports (id, userId, playerId, grade, dateCreated, summary, playerFirstName, playerLastName, draftClass, playerPosition, playerHeight, playerWeight, playerSchool)" +
				"Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try(Connection connect = LocalStorage.connect();
			PreparedStatement preparedStatement = connect.prepareStatement(sqlStmt)){
			preparedStatement.setLong(1, report.getId());
			preparedStatement.setLong(2, report.getUserId());
			preparedStatement.setLong(3, report.getPlayerId());
			if(report.getGrade() != null) {
				preparedStatement.setDouble(4, report.getGrade());
			} else {
				preparedStatement.setNull(4, java.sql.Types.DOUBLE);
			}
			
			if(report.getDateCreated() != null) {
				preparedStatement.setObject(5, report.getDateCreated());
			} else {
				preparedStatement.setNull(5, java.sql.Types.DATE);
			}
			if(report.getSummary() != null) {
				preparedStatement.setString(6, report.getSummary());
			} else {
				preparedStatement.setNull(6, java.sql.Types.VARCHAR);
			}
			if(report.getPlayerFirstName() != null) {
				preparedStatement.setString(7, report.getPlayerFirstName());
			} else {
				preparedStatement.setNull(7, java.sql.Types.VARCHAR);
			}
			if(report.getPlayerLastName() != null) {
				preparedStatement.setString(8, report.getPlayerLastName());
			} else {
				preparedStatement.setNull(8, java.sql.Types.VARCHAR);
			}
			if(report.getDraftClass() != null) {
				preparedStatement.setInt(9, report.getDraftClass());
			} else {
				preparedStatement.setNull(9, java.sql.Types.INTEGER);
			}
			if(report.getPlayerPosition() != null) {
				preparedStatement.setString(10, report.getPlayerPosition().toString());
				System.out.println(report.getPlayerPosition());
			} else {
				preparedStatement.setNull(10, java.sql.Types.VARCHAR);
			}
			if(report.getPlayerHeight() != null) {
				preparedStatement.setInt(11, report.getPlayerHeight());
			} else {
				preparedStatement.setNull(11, java.sql.Types.INTEGER);
			}
			if(report.getPlayerWeight() != null) {
				preparedStatement.setInt(12, report.getPlayerWeight());
			} else {
				preparedStatement.setNull(12, java.sql.Types.INTEGER);
			}
			if(report.getPlayerSchool() != null) {
				preparedStatement.setString(13, report.getPlayerSchool());
			} else {
				preparedStatement.setNull(13, java.sql.Types.VARCHAR);
			}
			
			preparedStatement.addBatch();
			preparedStatement.executeBatch();
		
		
			} catch(SQLException e) {
				e.printStackTrace();
			}

	}
	
	public static int saveReportToServer(Report report) {
		final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		try {
			URL url = new URL(apiURL + "/reports/create");
			String jsonRequest = objectMapper.writeValueAsString(report);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			
			try(OutputStream os = connection.getOutputStream()) {
				byte [] input = jsonRequest.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			int responseCode = connection.getResponseCode();
			if (connection.getResponseCode() == 200) {
				System.out.println("Connection successful. Report successfully saved to server.");
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
	
	public static Report getReportById(Long reportId) {
		System.out.println(reportId);
		String sqlStatement = "Select * From reports Where id = ?";
		Report result = new Report();
		try(Connection conn = LocalStorage.connect();
			PreparedStatement ps = conn.prepareStatement(sqlStatement);
			ResultSet rs = ps.executeQuery()) {
			
			while(rs.next()) {
				Report report = new Report(
						rs.getLong("id"),
						rs.getDouble("grade"),
						rs.getObject("dateCreated", LocalDateTime.class),
						rs.getString("summary"),
						rs.getLong("userId"),
						rs.getLong("playerId"),
						rs.getString("playerFirstName"),
						rs.getString("playerLastName"),
						rs.getInt("draftClass"),
						Position.valueOf(rs.getString("playerPosition")),
						rs.getInt("playerHeight"),
						rs.getInt("playerWeight"),
						rs.getString("playerSchool")
						);
				result = report;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	public static ObservableList<Report> getLocalReportsByUserId(Long userId){
			
		String sqlStatement = "Select * From reports Where userId = ?";
		ObservableList<Report> reports = FXCollections.observableArrayList();
		try(Connection conn = LocalStorage.connect();
			PreparedStatement ps = conn.prepareStatement(sqlStatement)){
				ps.setLong(1, userId);
			
			try(ResultSet rs = ps.executeQuery()) {
			
			while(rs.next()) {
				Report report = new Report(
						rs.getLong("id"),
						rs.getDouble("grade"),
						rs.getObject("dateCreated", LocalDateTime.class),
						rs.getString("summary"),
						rs.getLong("userId"),
						rs.getLong("playerId"),
						rs.getString("playerFirstName"),
						rs.getString("playerLastName"),
						rs.getInt("draftClass"),
						Position.valueOf(rs.getString("playerPosition")),
						rs.getInt("playerHeight"),
						rs.getInt("playerWeight"),
						rs.getString("playerSchool")
						);
				reports.add(report);
			}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return reports;
			
	}
	
	
	public static void updateReportInLocalStorage(Long reportId, Double updatedGrade, String updatedSummary) {
		Report report = getReportById(reportId);
		report.setGrade(updatedGrade);
		report.setSummary(updatedSummary);
		saveReportToLocalStorage(report);
	}
	
	public static int updateReportInServer(Report report) {
		final String updateURL = apiURL + "/reports/update/" + report.getId();
		final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		try {
			URL url = new URL(updateURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String jsonRequest = objectMapper.writeValueAsString(report);
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
                System.out.println("Report updated successfully.");
                return responseCode;
            } else {
                System.out.println("Failed to update report. Response Code: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return 0;
    }
	
	public static int deleteFromServer(Report report) {
	    try {
	        URL url = new URL(apiURL + "/reports/delete/" + report.getId());
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("DELETE");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setDoOutput(true);
	        
	        int responseCode = connection.getResponseCode();


	        if (responseCode == 200) {
	            System.out.println("Connection successful. Report successfully deleted from server.");
	            return responseCode;
	        } else {
	            System.out.println("ERROR " + connection.getResponseCode());
	        }

	        if (connection != null) {
	            connection.disconnect();
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0;
	}

		
	
	
	public static void deleteReport(Report report) {
		String stmt = "Delete From reports Where id = ?";
		try(Connection connect = LocalStorage.connect();
			PreparedStatement ps = connect.prepareStatement(stmt)){
			ps.setLong(1, report.getId());
			ps.executeUpdate();
			System.out.println("Report deleted from database.");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
