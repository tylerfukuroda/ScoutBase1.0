package com.fballtech.scoutbasebeta;

import com.fballtech.scoutbasebeta.*;
import com.fballtech.scoutbasebeta.dao.PlayerDAO;
import com.fballtech.scoutbasebeta.localstorage.LocalStorage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/resources/Login.fxml"));
			
			Scene scene = new Scene(root);
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) {
		LocalStorage.createUsersTable();
		LocalStorage.createReportsTable();
		LocalStorage.createPlayersTable();
		PlayerDAO.addPlayersToLocalStorage(PlayerDAO.getAllPlayers());
		launch(args);
		
	}
}

