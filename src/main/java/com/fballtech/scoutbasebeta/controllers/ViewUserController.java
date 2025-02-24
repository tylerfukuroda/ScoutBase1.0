package com.fballtech.scoutbasebeta.controllers;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import com.fballtech.scoutbasebeta.dao.UserDAO;
import com.fballtech.scoutbasebeta.entities.User;
import com.fballtech.scoutbasebeta.service.SessionManager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ViewUserController {
	
	@FXML
	private TextField firstNameText;
	
	@FXML
	private TextField lastNameText;
	
	@FXML
	private TextField emailText;
	
	@FXML
	private TextField usernameText;
	
	@FXML
	private PasswordField newPinText;
	
	@FXML
	private PasswordField confirmPinText;
	
	@FXML
	private Button editProfileButton;
	
	@FXML
	private Button resetPinButton;
	
	@FXML
	private Button deleteAccountButton;
	
	@FXML
	private Text isAdminText;
	
	@FXML
	private Text isActiveText;
	
	@FXML
	private AnchorPane pinAnchor;
	
	private User user;
	
	
	@FXML
	public void initialize() {
		User user = SessionManager.getInstance().getUser();
		pinAnchor.setVisible(false);
		firstNameText.setText(user.getFirstName());
		lastNameText.setText(user.getLastName());
		emailText.setText(user.getEmail());
		usernameText.setText(user.getUsername());
		newPinText.textProperty().addListener((obs, oldValue, newValue) -> {
			if(!newValue.matches("\\d*")) {
				newPinText.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		confirmPinText.textProperty().addListener((obs, oldValue, newValue) -> {
			if(!newValue.matches("\\d*")) {
				confirmPinText.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		if(user.isActive())
			isActiveText.setText("Membership Status: Active");
		else
			isActiveText.setText("Membership Status: Not Active");
		if(user.isAdmin())
			isAdminText.setText("Membership Type: Admin");
		else
			isAdminText.setText("Membership Type: Scout");
	}
	
	public void updateUserDetails() {
		user.setFirstName(firstNameText.getText());
		user.setLastName(lastNameText.getText());
		user.setEmail(emailText.getText());
		UserDAO.saveUserToLocalStorage(user);
		UserDAO.updateUserServer(user);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Attention");
		alert.setContentText("You have successfully updated your profile.");
		alert.showAndWait();
		reset();
	}
	
	public void updatePin() {
		user = SessionManager.getInstance().getUser();
		System.out.println(user.getFirstName());
		if(!verifyPinLength()) {
			System.out.println("Length error.");
		}
		else if(newPinText.getText().equals(confirmPinText.getText())) {
			String updatedPassword = UserDAO.hashPassword(newPinText.getText());
			user.setPassword(updatedPassword);
			UserDAO.saveUserToLocalStorage(user);
			UserDAO.updateUserServer(user);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Attention");
			alert.setContentText("You have successfully reset your pin.");
			alert.showAndWait();
			reset();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("ERROR: MISMATCH PIN");
			alert.setContentText("Both pin numbers need to match. Try again.");
			alert.showAndWait();
		}
		
		
	}
	
	public boolean verifyPinLength() {
		if(newPinText.getText().length() > 4 || newPinText.getText().length() < 4) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("ERROR: INCORRECT PIN LENGTH");
				alert.setContentText("Pin must be 4 digits long.");
				alert.showAndWait();
				return false;
				}
		else {
			return true;
		}
	}
	
	public void deleteAccount(){
		user = SessionManager.getInstance().getUser();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("ATTENTION");
		alert.setContentText("Are you sure you would like to delete your account? All reports and players you created will be deleted. This account will be removed from our databases.");
		if(alert.showAndWait().get() == ButtonType.OK) {
			UserDAO.deleteUserFromServer(user);
			UserDAO.deleteUserFromLocalStorage(user);
			closeAllWindowsWithConfirmation();
			
		} else {
			alert.close();
		}
	}
	
	
	public void makeUserEditable() {
		firstNameText.setEditable(true);
		lastNameText.setEditable(true);
		emailText.setEditable(true);
		editProfileButton.setText("SAVE");
		resetPinButton.setText("CANCEL");
		editProfileButton.setOnAction(e -> updateUserDetails());
		resetPinButton.setOnAction(e -> reset());
	}
	
	public void resetPinVisible() {
		pinAnchor.setVisible(true);
		editProfileButton.setText("SAVE");
		resetPinButton.setText("CANCEL");
		editProfileButton.setOnAction(e -> updatePin());
		resetPinButton.setOnAction(e-> reset());
	}
	
	public void reset() {
		firstNameText.setEditable(false);
		lastNameText.setEditable(false);
		emailText.setEditable(false);
		newPinText.setEditable(false);
		confirmPinText.setEditable(false);
		pinAnchor.setVisible(false);
		editProfileButton.setText("EDIT PROFILE");
		resetPinButton.setText("RESET PIN");
		editProfileButton.setOnAction(e -> makeUserEditable());
		resetPinButton.setOnAction(e -> updatePin());
	}
	
	public void closeAllWindowsWithConfirmation() {
	    
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Close Application");
	    alert.setHeaderText("Close All Windows");
	    alert.setContentText("Are you sure you want to close all windows?");
	    
	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        Platform.exit();
	    }
	}
	
	
	
	
	
}
