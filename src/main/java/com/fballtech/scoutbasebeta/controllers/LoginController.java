package com.fballtech.scoutbasebeta.controllers;

import java.io.IOException;

import java.io.UnsupportedEncodingException;

import com.fballtech.scoutbasebeta.dao.ReportDAO;
import com.fballtech.scoutbasebeta.dao.UserDAO;
import com.fballtech.scoutbasebeta.entities.User;
import com.fballtech.scoutbasebeta.localstorage.LocalStorage;
import com.fballtech.scoutbasebeta.service.SessionManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private TextField usernameInput;
	
	@FXML
	private PasswordField passwordInput;
	
	@FXML
	private Button loginButton;
	
	@FXML
	private Button createAccountButton;
	
	public void createAccount() {
		try {
			final String createAccountPage = "resources/CreateAccount.fxml";
			FXMLLoader loader= new FXMLLoader(getClass().getResource(createAccountPage));
			Parent root = loader.load();
			Stage stage = new Stage();
			Stage currentStage = (Stage) createAccountButton.getScene().getWindow();
			stage.setTitle("Create Account");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
			currentStage.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void verifyLogin() {
		User user = new User();
		if(!UserDAO.verifyUserFromServer(usernameInput.getText(), passwordInput.getText())) {
			System.out.println("Could not verify user from server or could not connect to server.");
			user = UserDAO.getLocalUser(usernameInput.getText());
		} else if (UserDAO.verifyUserFromServer(usernameInput.getText(), passwordInput.getText())){
			user = UserDAO.getUserFromServer(usernameInput.getText());
			UserDAO.saveUserToLocalStorage(user);
			System.out.println("Server verfied user: ");
		}
		try {
			if(user.getUsername().equals(usernameInput.getText()) && UserDAO.verifyPassword(passwordInput.getText(), user.getPassword())) {
				try {
					usernameInput.clear();
					passwordInput.clear();
					SessionManager sessionManager = SessionManager.getInstance();
					sessionManager.startSession(user);
					sessionManager.setUser(user);
					ReportDAO.addReportsToLocalStorage(ReportDAO.getServerReportByUserId(user.getId()));
					openHomePage();
					LocalStorage.printSQL(user.getId());
				} catch(IOException e) {
					e.printStackTrace();
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("ERROR");
				alert.setContentText("Invalid username or password. Please try again.");
				alert.showAndWait();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openHomePage() throws IOException {
		final String createPlayerPageFile = "/resources/Homepage.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(createPlayerPageFile));
		Parent root = loader.load();
		Stage stage = new Stage();
		Stage oldStage = (Stage) loginButton.getScene().getWindow();
		stage.setTitle("Homepage");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
		oldStage.close();
	}
}


