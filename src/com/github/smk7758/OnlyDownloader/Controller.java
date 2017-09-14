package com.github.smk7758.OnlyDownloader;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class Controller {
	Main main;
	@FXML
	Label label_log;
	@FXML
	Button btn_select_folder;
	@FXML
	Button btn_start_download;
	@FXML
	TextField field_url;
	@FXML
	TextField field_download_path;
	@FXML
	ProgressBar progressbar_download;

	public Controller() {
		this.main = new Main();
	}

	public Controller(Main main) {
		this.main = main;
	}

	@FXML
	protected void onBtnSelectFolder(ActionEvent e) throws InterruptedException {
		final DirectoryChooser fc = new DirectoryChooser();
		fc.setTitle("ファイル選択");
		if (Main.primaryStage != null) {
			File importFile = fc.showDialog(Main.primaryStage);
			if (importFile != null) field_download_path.setText(importFile.getPath().toString());
		} else {
			System.out.println("Main.primaryStage is null.");
		}
	}

	@FXML
	protected void onBtnStartDownload(ActionEvent e) {
		// Source from: http://www.k-sugi.sakura.ne.jp/java/android/1710/
		URL url = null;
		HttpURLConnection conn = null;
		String filename_extension = null;
		try {
			if (field_url.getText().length() < 1 || field_download_path.getText().length() < 1); //returnなり止める。
			String[] url_split = field_url.getText().split("\\.");
			filename_extension = url_split[url_split.length - 1];
			System.out.println(filename_extension);
			url = new URL(field_url.getText());
			conn = (HttpURLConnection) url.openConnection();
			conn.setAllowUserInteraction(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.connect();
			int httpStatusCode = conn.getResponseCode();
			if (httpStatusCode != HttpURLConnection.HTTP_OK) {
				System.out.println("Conection Error Code: " + httpStatusCode);
				return;
			}
			String content_type = conn.getContentType();
			System.out.println("ContentType: " + content_type);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (ProtocolException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Input Stream
		DataInputStream dataInStream = null;
		try {
			dataInStream = new DataInputStream(conn.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Output Stream
		DataOutputStream dataOutStream = null;
		try {
			String output_path;
			if (System.getProperty("user.name")
					.equals("smk7758")) output_path = "F:\\users\\smk7758\\Desktop\\log_client." + filename_extension;
			else output_path = System.getProperty("user.home") + "\\Desktop\\log_client." + filename_extension;
			dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output_path)));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

		// Read Data
		byte[] b = new byte[4096];
		int readByte = 0;
		try {
			while (-1 != (readByte = dataInStream.read(b))) {
				dataOutStream.write(b, 0, readByte);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Close Stream
		try {
			dataInStream.close();
			dataOutStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		conn.disconnect();

	}
}
