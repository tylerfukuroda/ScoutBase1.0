package com.fballtech.scoutbasebeta.controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GeneralController {
	
	private FXMLLoader loader;
	private Parent root;
	private Stage stage;
	
	//Function to open new FXML files... will be called when buttons are used.
	public void openNewPage(String fxmlFile, String title) {
		try {
			loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
			root = loader.load();
			stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			stage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public FXMLLoader getLoader() {
		return loader;
	}

	public Parent getRoot() {
		return root;
	}

	public Stage getStage() {
		return stage;
	}

	public void setLoader(FXMLLoader loader) {
		this.loader = loader;
	}

	public void setRoot(Parent root) {
		this.root = root;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	
}
