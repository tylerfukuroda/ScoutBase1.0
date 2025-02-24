package com.fballtech.scoutbasebeta.controllers;

import java.io.IOException;

import java.util.UUID;

import com.fballtech.scoutbasebeta.dao.UserDAO;
import com.fballtech.scoutbasebeta.entities.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateAccountController {
	
	@FXML
	private TextField firstNameText;
	
	@FXML
	private TextField lastNameText;
	
	@FXML
	private TextField emailText; 
	
	@FXML
	private TextField usernameText;
	
	@FXML
	private PasswordField pinText;
	
	@FXML
	private PasswordField pinConfirm;
	
	@FXML
	private Button createButton;
	
	
	@FXML
	public void initialize() {
		pinText.textProperty().addListener((obs, oldValue, newValue) -> {
			if(!newValue.matches("\\d*")) {
				pinText.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
	}
	
	public void createAccount() {
		if(firstNameText.getText().isEmpty()||
			lastNameText.getText().isEmpty()||
			emailText.getText().isEmpty()||
			usernameText.getText().isEmpty()||
			pinText.getText().isEmpty()||
			pinConfirm.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("ERROR");
			alert.setContentText("ALL fields must be filled out. Please try again.");
			alert.showAndWait();
		} else if(!verifyPinLength(pinText.getText())) {
			System.out.println("Pin length error");
		} else if(Integer.parseInt(pinText.getText()) != Integer.parseInt(pinConfirm.getText())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("MISMATCH ERROR");
			alert.setContentText("Pin confirmation doesn't match entered pin.");
			alert.showAndWait();
		} else if(UserDAO.verifyUsernameFromServer(usernameText.getText()) == true) { //Not properly checking username input
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("USERNAME ALREADY EXISTS");
			alert.setContentText("The username you chose already exists. Please try again.");
			alert.showAndWait();
		} else {
			UUID uniqueId = UUID.randomUUID();
			Long userId = uniqueId.getLeastSignificantBits() & Long.MAX_VALUE;
			System.out.println("Pin: " + pinText.getText());
			//String hashedPassword = UserDAO.hashPassword(pinText.getText());
			User user = new User(
					userId,
					firstNameText.getText(),
					lastNameText.getText(),
					usernameText.getText(),
					UserDAO.hashPassword(pinText.getText()),
					emailText.getText(),
					false,
					true
					);
			UserDAO.saveUserToServer(user);
			UserDAO.saveUserToLocalStorage(user);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("SUCCESS");
			alert.setContentText("Your account was successfully created! Please login.");
			alert.showAndWait();
			try {
				final String loginPageFile = "/resources/Login.fxml";
				FXMLLoader loader= new FXMLLoader(getClass().getResource(loginPageFile));
				Parent root = loader.load();
				Stage stage = new Stage();
				stage.setTitle("Login");
				stage.setScene(new Scene(root));
				stage.setResizable(false);
				stage.show();
				Stage oldStage = (Stage) createButton.getScene().getWindow();
				oldStage.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public boolean verifyPinLength(String pin) {
		boolean result = false;
		if(pin.length() > 4 || pin.length() < 4) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("ERROR");
			alert.setContentText("Pin must be 4 digits long.");
			alert.showAndWait();
		} else {
			result = true;
		}
		return result;
	}
}
