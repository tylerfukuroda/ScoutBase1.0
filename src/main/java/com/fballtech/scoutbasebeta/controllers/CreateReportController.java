package com.fballtech.scoutbasebeta.controllers;

import java.time.LocalDateTime;

import java.util.UUID;

import com.fballtech.scoutbasebeta.Position;
import com.fballtech.scoutbasebeta.dao.ReportDAO;
import com.fballtech.scoutbasebeta.entities.Player;
import com.fballtech.scoutbasebeta.entities.Report;
import com.fballtech.scoutbasebeta.service.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateReportController {
	
	@FXML
	private Text firstNameText;
	
	@FXML
	private Text lastNameText;
	
	@FXML 
	private Text positionText;
	
	@FXML
	private Text schoolText;
	
	@FXML
	private Text heightText;
	
	@FXML
	private Text weightText;
	
	@FXML
	private Text draftClassText;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private TextField gradeInput;
	
	@FXML
	private TextArea summaryBox;
	
	@FXML
	private Button saveButton;
	
	private Long playerId;
	
	private Long userId;
	
	
	public void setPlayerDetails(Player player) {
		firstNameText.setText(player.getFirstName());
		lastNameText.setText(player.getLastName());
		positionText.setText(player.getPosition().toString());
		schoolText.setText(player.getSchool());
		heightText.setText(player.getHeight().toString());
		weightText.setText(player.getWeight().toString());
		draftClassText.setText(player.getDraftClass().toString());
		playerId = player.getId();
		
	}
	
	@FXML
	public void cancelReportAction(ActionEvent event) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void saveReportAction(ActionEvent event) {
		String input = gradeInput.getText();
		UUID uniqueIdGenerator = UUID.randomUUID();
		Long uniqueId = uniqueIdGenerator.getMostSignificantBits() & Long.MAX_VALUE;
		if (gradeInput.getText().isEmpty() || summaryBox.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ERROR");
			alert.setContentText("Grade input and summary should not be left blank.");
			alert.showAndWait();
		}
		else if(!isDouble(input)){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ERROR");
			alert.setHeaderText("WRONG DATA TYPE INPUT");
			alert.setContentText("Grade must be a number (e.g. 0.0, 5.5, 10.0)");
			alert.showAndWait();
		} else {
			
		Report report = new Report();
		System.out.println(SessionManager.getInstance().getUser().getId());
		userId = SessionManager.getInstance().getUser().getId();
		report.setUserId(userId);
		report.setPlayerId(playerId);
		report.setId(uniqueId);
		report.setDateCreated(LocalDateTime.now());
		report.setSummary(summaryBox.getText());
		report.setGrade(Double.parseDouble(input));
		report.setPlayerFirstName(firstNameText.getText());
		report.setPlayerLastName(lastNameText.getText());
		report.setDraftClass(Integer.parseInt(draftClassText.getText()));
		report.setPlayerPosition(Position.valueOf(positionText.getText()));
		report.setPlayerHeight(Integer.parseInt(heightText.getText()));
		report.setPlayerWeight(Integer.parseInt(weightText.getText()));
		report.setPlayerSchool(schoolText.getText());
		
		Alert confirmSaveAlert = new Alert(AlertType.CONFIRMATION);
		confirmSaveAlert.setHeaderText("Confirm Save");
		confirmSaveAlert.setContentText("Are you sure you would like to save this report?");
		if(confirmSaveAlert.showAndWait().get() == ButtonType.OK) {
			ReportDAO.saveReportToLocalStorage(report);
			ReportDAO.saveReportToServer(report);
			Stage stage = (Stage) saveButton.getScene().getWindow();
			stage.close();
		} else {
			confirmSaveAlert.close();
		}
		}
	}
	
	public boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
}
