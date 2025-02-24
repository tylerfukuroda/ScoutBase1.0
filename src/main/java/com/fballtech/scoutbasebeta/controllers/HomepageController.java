package com.fballtech.scoutbasebeta.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fballtech.scoutbasebeta.Position;
import com.fballtech.scoutbasebeta.dao.PlayerDAO;
import com.fballtech.scoutbasebeta.entities.Player;
import com.fballtech.scoutbasebeta.service.SessionManager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;


public class HomepageController {
	
	@FXML
	private TableView<Player> playerTable;
	
	@FXML
	private TableColumn<Player, Long> idColumn; 
	
	@FXML
	private TableColumn<Player, String> firstNameColumn;
	
	@FXML 
	private TableColumn<Player, String> lastNameColumn;
	
	@FXML
	private TableColumn<Player, String> positionColumn;
	
	@FXML
	private TableColumn<Player, Integer> heightColumn;
	
	@FXML
	private TableColumn<Player, Integer> weightColumn;
	
	@FXML 
	private TableColumn<Player, Integer> classColumn;
	
	@FXML
	private TableColumn<Player, String> schoolColumn;
	
	@FXML
	private Button createReportButton;
	
	@FXML
	private Button viewReportButton;
	
	@FXML
	private Button createPlayerButton;
	
	@FXML
	private Button draftBoardsButton;
	
	@FXML
	private Button favoritesButton;
	
	@FXML
	private Button viewProfileButton;
	
	@FXML
	private Text usernameText;
	
	@FXML
	private Text playerIdDisplay;
	
	@FXML
	private Text firstNameDisplay;
	
	@FXML
	private Text lastNameDisplay;
	
	@FXML
	private Text positionDisplay;
	
	@FXML
	private Text classDisplay;
	
	@FXML
	private Text heightDisplay;
	
	@FXML
	private Text weightDisplay;
	
	@FXML
	private Text schoolDisplay;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private ComboBox<Position> filterBox;
	
	@FXML
	private ComboBox<Integer> filterByDraftClass;
	
	private ObservableList<Player> players;
	
	@FXML
	public void initialize() {
		
		System.out.println("Initialize called."); //Signifies that this function was called.

        try {
        	idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
			firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
			lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
	        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
	        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
	        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
	        classColumn.setCellValueFactory(new PropertyValueFactory<>("draftClass"));
	        schoolColumn.setCellValueFactory(new PropertyValueFactory<>("school"));

	        loadPlayers();
	        playerSearch();
	        draftClassFilter();
	        //positionFilter();
	        setFilterBox();
	        usernameText.setText("Welcome, " + SessionManager.getInstance().getUser().getFirstName() + "!");
	        
	        filterByDraftClass.setVisible(false);
	        
	        playerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        	if (newSelection != null) {
	        		displaySelectedPlayer(newSelection); //Created listener on all players in tableview.
	        	}
	        });
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void loadPlayers() { //Loads players from SQLite database to playerTable.
		players = PlayerDAO.getAllPlayersFromLocalStorage();
		try {
			playerTable.setItems(players);
			System.out.println("Players loaded into players table.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Function to display player info to bottom half of the screen in homepage.
	public void displaySelectedPlayer(Player selectedPlayer) { 
		playerIdDisplay.setText(selectedPlayer.getId().toString());
		firstNameDisplay.setText(selectedPlayer.getFirstName());
		lastNameDisplay.setText(selectedPlayer.getLastName());
		positionDisplay.setText(selectedPlayer.getPosition().toString());
		heightDisplay.setText(selectedPlayer.getHeight().toString());
		weightDisplay.setText(selectedPlayer.getWeight().toString());
		classDisplay.setText(selectedPlayer.getDraftClass().toString());
		schoolDisplay.setText(selectedPlayer.getSchool());
	}
	
	//Opens Create Report page when Create Report button is clicked.
	public void openCreateReportPage() {
		try {
			final String createReportPageFile = "/resources/CreateReport.fxml";
			Player selectedPlayer = getSelectedPlayer();
				if(selectedPlayer != null) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(createReportPageFile));
					Parent root = loader.load();
					CreateReportController controller = loader.getController();
					controller.setPlayerDetails(selectedPlayer);
					Stage stage = new Stage();
					stage.setTitle("Create Report");
					stage.setScene(new Scene(root));
					stage.setResizable(false);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.showAndWait();
				}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
} 
	
	public void openReportMenuPage() {
		try {
			final String reportMenuPageFile = "/resources/ReportMenu.fxml";
			FXMLLoader loader= new FXMLLoader(getClass().getResource(reportMenuPageFile));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Report Menu");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openCreatePlayerPage() {
		try {
			final String createPlayerPageFile = "/resources/CreatePlayer.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(createPlayerPageFile));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Create Player");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playerSearch() {
		
		FilteredList<Player> filteredPlayers = new FilteredList<Player>(players, p -> true);
		searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
			filteredPlayers.setPredicate(player -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseSearch = newValue.toLowerCase();
				
				if (player.getFirstName().toLowerCase().contains(lowerCaseSearch)) {
					return true;
				} else return player.getLastName().toLowerCase().contains(lowerCaseSearch);
			});
		});
		playerTable.setItems(filteredPlayers);
	}
	
	public void draftClassFilter() {
		ObservableList<Integer> draftClasses = FXCollections.observableArrayList(2025, 2026, 2027);
		filterByDraftClass.setItems(draftClasses);
	}
	
	public void setFilterBox() {
		ObservableList<Position> positions = FXCollections.observableArrayList(Position.values());
		filterBox.setItems(positions);
	}
	
	public void positionFilter() {
		
		FilteredList<Player> filteredPlayers = new FilteredList<Player>(players, p -> true);
		filterBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			filteredPlayers.setPredicate(player -> {
				if (newValue == null) {
					return true;
				}
				return player.getPosition().equals(newValue);
			});
			});
		playerTable.setItems(filteredPlayers);
		};
	
	
	public Player getSelectedPlayer() { //Returns the player that is currently selected in playerTable.
		return playerTable.getSelectionModel().getSelectedItem();
	}
	
	public void comingSoonAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("ATTENTION: COMING SOON");
		alert.setContentText("Feature Coming Soon! (Will be available in Version 2.0.)");
		alert.showAndWait();
	}
	
	public void logout() throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("ATTENTION");
		alert.setContentText("Are you sure you would like to log out?");
		if(alert.showAndWait().get() == ButtonType.OK) {
			SessionManager.getInstance().endSession();
			System.out.println("Session ended");
			loadLoginPage();
		}else {
			alert.close();
		}
	}
	
	public void loadViewUserProfilePage() throws IOException {
		final String userProfileFile = "/resources/ViewUser.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(userProfileFile));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setTitle("User Profile: " + SessionManager.getInstance().getUser().getUsername());
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}
		
	
	public void loadLoginPage() throws IOException {
		closeAllWindowsWithConfirmation();
		final String loginPageFile = "/resources/Login.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource((loginPageFile)));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setTitle("Homepage");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
		
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
