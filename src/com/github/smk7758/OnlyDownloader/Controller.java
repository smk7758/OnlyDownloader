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
	Button btn_select_folder, btn_start_download;
	@FXML
	TextField field_url;
	@FXML
	TextField field_download_path;
	@FXML
	ProgressBar progressbar_download;

	public void initialize() {
		textarea_log.setText("[Log] Start time: " + time.toString());
	}

	public void addLog(String string, int mode) {
		if (mode == 0) {
			textarea_log.appendText("\n[Log] " + string);
		} else if (mode == 1) {
			textarea_log.appendText("\n[Error] " + string);
		} else {
			textarea_log.appendText("\n" + string);
		}
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
			addLog("Main.primaryStage is null.", 1);
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
			addLog("Please write all of fields.", 1);
			return;
		}

		// todo
		// Get filename
		String[] url_split_filename = field_url.getText().trim().split("/");
		String filename = url_split_filename[url_split_filename.length - 1];
		System.out.println("Filename: " + filename);
		addLog("Filename: " + filename, 0);

		// Get extension from URL
		String[] url_split_extension = field_url.getText().split("\\.");
		String filename_extension = url_split_extension[url_split_extension.length - 1];
		System.out.println("FilenameExtension: " + filename_extension);
		addLog("FilenameExtension: " + filename_extension, 0);

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
				addLog("Conection Error Code: " + httpStatusCode, 1);
				return;
			} else {
				addLog("HTTP Conection OK!", 0);
			}
		} catch (ProtocolException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Get content type
		String content_type = conn.getContentType();
		System.out.println("ContentType: " + content_type);
		addLog("ContentType: " + content_type, 0);

		// Get content length
		long content_length = conn.getContentLengthLong();
		System.out.println("ContentLength: " + content_length);
		addLog("ContentLength: " + content_length, 0);

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
					field_download_path.getText().trim().length()) + "/" + filename;
			System.out.println("output_path: " + output_path);
			addLog("OutputPath: " + output_path, 0);
		}

		// Output Stream
		DataOutputStream dataOutStream = null;
		try {
			dataOutStream = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(output_path)));
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
		addLog("Finish download.", 0);

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
