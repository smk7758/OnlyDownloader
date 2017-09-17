package com.github.smk7758.OnlyDownloader;

import java.io.IOException;
import java.time.LocalDateTime;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public Controller ctr = new Controller();
	public static Stage primaryStage = null;
	public final static LocalDateTime time = LocalDateTime.now();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Scene scene = new Scene(FXMLLoader.load(getClass().getResource("OnlyDownloader_0.0.2.fxml")));
			// Set ico
//			primaryStage.getIcons().add(new Image((getClass().getResource("OnlyDownloader_x16.ico").toString())));
//			primaryStage.getIcons().add(new Image((getClass().getResource("OnlyDownloader_x32.ico").toString())));
			// Set Title
			primaryStage.setTitle("OnlyDownloader_0.0.2");
			// Set Window
			primaryStage.setResizable(false);
			// Set Scene
			primaryStage.setScene(scene);
			primaryStage.show();
			if (ctr.textarea_log != null) ctr.textarea_log.setText(Main.time.toString());
			else System.out.println("NULL");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Main.primaryStage = primaryStage;
		}
	}
}
