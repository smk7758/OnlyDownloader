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
import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class Controller {
	public final static LocalDateTime time = LocalDateTime.now();
	@FXML
	TextArea textarea_log;
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

	@Override
	public void initialize () {
		textarea_log.setText(time.toString());
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
			textarea_log.appendText("\n[Error] Main.primaryStage is null.");
		}
	}

	@FXML
	protected void onBtnStartDownload(ActionEvent e) {
		// initialize
		URL url = null;
		HttpURLConnection conn = null;

		// null,Empty check
		if (field_url == null || field_url.getText().trim().length() < 1 || field_download_path == null
				|| field_download_path.getText().trim().length() < 1) {
			System.out.println("Please write all of fields.");
			textarea_log.appendText("\n[Error] Please write all of fields.");
			return;
		}

		// todo
		// Get filename
		String[] url_split_filename = field_url.getText().trim().split("//");
		String filename = url_split_filename[url_split_filename.length - 1];
		System.out.println("Filename: " + filename);
		textarea_log.appendText("\n[Log] Filename: " + filename);

		// Get extension
		String[] url_split_extension = field_url.getText().split("\\.");
		String filename_extension = url_split_extension[url_split_extension.length - 1];
		System.out.println("FilenameExtension: " + filename_extension);
		textarea_log.appendText("\n[Log] FilenameExtension: " + filename_extension);

		// URL init.
		try {
			url = new URL(field_url.getText());
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}

		// HTTP Conection
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setAllowUserInteraction(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.connect();
			int httpStatusCode = conn.getResponseCode();
			if (httpStatusCode != HttpURLConnection.HTTP_OK) {
				System.out.println("Conection Error Code: " + httpStatusCode);
				textarea_log.appendText("\n[Log] Conection Error Code: " + httpStatusCode);
				return;
			}
		} catch (ProtocolException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Get content type
		String content_type = conn.getContentType();
		System.out.println("ContentType: " + content_type);
		textarea_log.appendText("\n[Log] ContentType: " + content_type);

		// Get content length
		long content_length = conn.getContentLengthLong();
		System.out.println("ContentLength: " + content_length);
		textarea_log.appendText("\n[Log] ContentLength: " + content_length);

		// Input Stream
		DataInputStream dataInStream = null;
		try {
			dataInStream = new DataInputStream(conn.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// todo
		// Output Path
		String output_path = null;
		if (field_download_path.getText().trim().endsWith("\\")) {
			output_path = field_download_path.getText().trim().substring(0,
					field_download_path.getText().trim().length());
		}

		// Output Stream
		DataOutputStream dataOutStream = null;
		try {
			dataOutStream = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(output_path + "/" + filename)));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

		// Read Data
		byte[] b = new byte[4096];
		int readByte = 0;
		try {
			while (-1 != (readByte = dataInStream.read(b))) {
				if (dataOutStream != null) dataOutStream.write(b, 0, readByte);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("Finish download.");
		textarea_log.appendText("Finish download.");

		// Close Stream
		try {
			dataOutStream.close();
			dataInStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		conn.disconnect();
	}
}
