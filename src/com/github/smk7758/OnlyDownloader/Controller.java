package com.github.smk7758.OnlyDownloader;

import java.io.File;
import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class Controller {
	public final static LocalDateTime time_start = LocalDateTime.now();
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
		textarea_log.setText("[Log] Start time: " + time_start.toString());
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
		fc.setTitle("フォルダ選択");
		if (Main.primary_stage != null) {
			File import_file = fc.showDialog(Main.primary_stage);
			if (import_file != null) field_download_path.setText(import_file.getPath().toString());
		} else {
			System.out.println("Main.primary_stage is null.");
			addLog("Main.primary_stage is null.", 1);
		}
	}

	@FXML
	protected void onBtnStartDownload(ActionEvent e) {
		DownloadThread dth = new DownloadThread(this);
		dth.setDaemon(true);
		dth.start();
	}
}
