package com.github.smk7758.OnlyDownloader;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class Controller{
	Main main;
	@FXML Label label_log;
	@FXML Button btn_select_folder;
	@FXML Button btn_start_download;
	@FXML TextField field_url;
	@FXML TextField field_download_path;
	@FXML ProgressBar progressbar_download;

	public Controller() {
	}

	public Controller(Main main) {
		this.main = main;
	}

	@FXML
	protected void onBtnSelectFolder(ActionEvent e) {
		final DirectoryChooser fc = new DirectoryChooser();
		fc.setTitle("ファイル選択");
		File importFile = fc.showDialog(main.primaryStage);
		if (importFile != null) field_download_path.setText(importFile.getPath().toString());
	}

	@FXML
	protected void onBtnStartDownload(ActionEvent e) {

	}
}
