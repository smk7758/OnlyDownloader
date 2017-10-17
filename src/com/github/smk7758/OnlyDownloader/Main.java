package com.github.smk7758.OnlyDownloader;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
//	public static Controller ctr;
	public static Stage primary_stage = null;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primary_stage) throws Exception {
		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("OnlyDownloader_0.0.2.fxml"));
//			Scene scene = new Scene(loader.load());
//			ctr = (Controller) loader.getController();
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("OnlyDownloader_0.0.2.fxml")));
			// Set ico
			// primaryStage.getIcons().add(new Image((getClass().getResource("OnlyDownloader_x16.ico").toString())));
			// primaryStage.getIcons().add(new Image((getClass().getResource("OnlyDownloader_x32.ico").toString())));
			// Set Title
			primary_stage.setTitle("OnlyDownloader_0.0.2");
			// Set Window
			primary_stage.setResizable(false);
			// Set Scene
			primary_stage.setScene(scene);
			primary_stage.show();
			// primaryStage.onShownProperty();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Main.primary_stage = primary_stage;
		}
	}
}
