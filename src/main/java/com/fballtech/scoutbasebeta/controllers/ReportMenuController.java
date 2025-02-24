package com.fballtech.scoutbasebeta.controllers;

import java.io.IOException;

import java.time.LocalDateTime;

import com.fballtech.scoutbasebeta.Position;
import com.fballtech.scoutbasebeta.dao.ReportDAO;
import com.fballtech.scoutbasebeta.entities.Report;
import com.fballtech.scoutbasebeta.service.SessionManager;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReportMenuController {
	
	@FXML
	private TableView<Report> reportsTable;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private Button deleteReportButton;
	
	@FXML
	private Button editReportButton;
	
	@FXML
	private Button exportReportButton;
	
	@FXML
	private Button viewReportButton;
	
	@FXML
	private TableColumn<Report, Long> reportIdColumn;
	
	@FXML
	private TableColumn<Report, String> firstNameColumn;
	
	@FXML
	private TableColumn<Report, String> lastNameColumn;
	
	@FXML
	private TableColumn<Report, Position> positionColumn;
	
	@FXML
	private TableColumn<Report, Integer> classColumn;
	
	@FXML
	private TableColumn<Report, Double> gradeColumn;
	
	@FXML
	private TableColumn<Report, LocalDateTime> dateCreatedColumn;
	
	@FXML
	private ComboBox<Position> filterByPosition;
	private ObservableList<Report> reports;
	
	
	@FXML
	public void initialize() {
		System.out.println("Report initialize called");
		try {
			reportIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
			firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerFirstName"));
			lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerLastName"));
			positionColumn.setCellValueFactory(new PropertyValueFactory<>("playerPosition"));
			classColumn.setCellValueFactory(new PropertyValueFactory<>("draftClass"));
			gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
			dateCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
			filterByPosition.setVisible(false);
			loadReports();
			
			 reportsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		        	if (newSelection != null) {
		        		Report r = newSelection; 
		        		System.out.println(r.getPlayerFirstName());//Created listener on all players in tableview.
		        		System.out.println(r.getPlayerSchool());
		        		System.out.println(r.getPlayerPosition());
		        		System.out.println(r.getGrade());
		        		System.out.println("this is the user id" + r.getUserId());
		        		
		        		
		        	}
			 });
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadReports() {
		Long userId = SessionManager.getInstance().getUser().getId();
		reports = ReportDAO.getLocalReportsByUserId(userId);
		System.out.println("I was called!");
		try {
			reportsTable.setItems(reports);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteReport() {
		Report report = new Report();
		report = getSelectedReport();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		System.out.println("Player selected: " + report.getPlayerFirstName());
		alert.setHeaderText("DELETE REPORT CONFIRMATION");
		alert.setContentText("Are you sure you would like to delete this report? This action cannot be reversed.");
		if(alert.showAndWait().get() == ButtonType.OK) {
			ReportDAO.deleteFromServer(report);
			ReportDAO.deleteReport(report);
			System.out.println("Report removed.");
			reports.remove(report);
			loadReports();
			System.out.println("Reports reloaded.");
		}else {
			alert.close();
		}
		
		
	}
	
	public void viewReport() {
		try {
			final String viewReportFile = "/resources/ViewReport.fxml";
			Report selectedReport = getSelectedReport();
			if(selectedReport != null) {
				FXMLLoader loader= new FXMLLoader(getClass().getResource(viewReportFile));
				Parent root = loader.load();
				ViewReportController vrc = loader.getController();
				vrc.setReportDetails(selectedReport);
				Stage stage = new Stage();
				stage.setTitle("View Report");
				stage.setScene(new Scene(root));
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.show();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void searchReportsByName() {
		FilteredList<Report> filteredReports = new FilteredList<Report>(reports, p -> true);
		searchBar.setOnKeyPressed(event -> {
	        if (event.getCode() == KeyCode.ENTER) {
	            event.consume(); 
	        }
		});
		searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
			filteredReports.setPredicate(report -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseSearch = newValue.toLowerCase();
				
				if (report.getPlayerFirstName().toLowerCase().contains(lowerCaseSearch)) {
					return true;
				} else return report.getPlayerLastName().toLowerCase().contains(lowerCaseSearch);
			});
		});
		reportsTable.setItems(filteredReports);
	}
	
	public Report getSelectedReport() {
		return reportsTable.getSelectionModel().getSelectedItem();
	}
	
	
}
