package com.github.smk7758.OnlyDownloader;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public Controller ctr = new Controller(this);
	public Unit unit = new Unit(this);
	public static Stage primaryStage = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("OnlyDownloader_0.0.1.fxml")));
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			Main.primaryStage = primaryStage;
		}
	}
}
