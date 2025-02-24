package com.fballtech.scoutbasebeta.controllers;

import java.util.UUID;

import com.fballtech.scoutbasebeta.Position;
import com.fballtech.scoutbasebeta.dao.PlayerDAO;
import com.fballtech.scoutbasebeta.entities.Player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreatePlayerController {
	
	@FXML
	private TextField firstNameTextBox;
	
	@FXML
	private TextField lastNameTextBox;
	
	@FXML
	private ComboBox<Position> positionChoice;
	
	@FXML
	private TextField schoolTextBox;
	
	@FXML
	private TextField heightTextBox;
	
	@FXML
	private TextField weightTextBox;
	
	@FXML
	private ComboBox<Integer> draftClassChoice;
	
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private Button createPlayerButton;
	
	@FXML
	public void initialize() {
		setPositionComboBox();
		setDraftClassComboBox();
	}
	
	public void createPlayer() {
		if(firstNameTextBox.getText().isEmpty() ||
			lastNameTextBox.getText().isEmpty() ||
			schoolTextBox.getText().isEmpty() ||
			heightTextBox.getText().isEmpty() ||
			weightTextBox.getText().isEmpty() ||
			positionChoice.getValue() == null ||
			draftClassChoice.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("ERROR: Empty input!");
			alert.setContentText("All fields must be filled out.");
			alert.showAndWait();
		}
		if(!formatCheck(heightTextBox.getText()) || !intCheck(heightTextBox.getText())) {
			Alert intAlert = new Alert(AlertType.ERROR);
			intAlert.setHeaderText("ERROR: WRONG INPUT TYPE");
			intAlert.setContentText("You must enter a number in 0000 format. Please try again.");
			intAlert.showAndWait();
		}else if(!intCheck(weightTextBox.getText())) {
			Alert intAlert = new Alert(AlertType.ERROR);
			intAlert.setHeaderText("ERROR: WRONG INPUT TYPE");
			intAlert.setContentText("You must enter a number. Please try again.");
			intAlert.showAndWait();
		} else {
			UUID uniqueId = UUID.randomUUID();
			Long playerId = uniqueId.getLeastSignificantBits() & Long.MAX_VALUE;
			Player player = new Player(
					playerId,
					firstNameTextBox.getText(),
					lastNameTextBox.getText(),
					positionChoice.getValue(),
					Integer.parseInt(heightTextBox.getText()),
					Integer.parseInt(weightTextBox.getText()),
					draftClassChoice.getValue(),
					schoolTextBox.getText()
					);
			Alert createAlert = new Alert(AlertType.CONFIRMATION);
			createAlert.setHeaderText("ATTENTION!");
			createAlert.setContentText("Are you sure you would like to create this player? Please make sure that all fields have been entered correctly.");
			if(createAlert.showAndWait().get() == ButtonType.OK) {
				PlayerDAO.savePlayerToLocalStorage(player);
			} else {
				createAlert.close();
			}
		}
		
	}
	
	public boolean intCheck(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean formatCheck(String input) {
		int height = Integer.parseInt(input);
		if(!intCheck(input)) {
			System.out.println("Did not pass intCheck()");
			return false;
		} else if(height < 1000 || height > 9999) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setPositionComboBox() {
		ObservableList<Position> positions = FXCollections.observableArrayList(Position.values());
		positionChoice.setItems(positions);
	}
	
	public void setDraftClassComboBox() {
		ObservableList<Integer> draftClasses = FXCollections.observableArrayList(2025, 2026, 2027);
		draftClassChoice.setItems(draftClasses);
	}
	
	public void cancelButton() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
		
	
}
