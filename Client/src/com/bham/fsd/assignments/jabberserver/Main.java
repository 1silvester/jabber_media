package com.bham.fsd.assignments.jabberserver;
	
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		Scene scene = new Scene(root, 1000,700);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setTitle("Jabber Messenger");
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
