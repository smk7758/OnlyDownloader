package com.github.smk7758.OnlyDownloader;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

public class DownloadThread extends Thread {
	Controller ctr;
	LocalDateTime time;

	public DownloadThread(Controller ctr) {
//		ctr = Main.ctr;
		this.ctr = ctr;
	}

	public void run() {
		// initialize
		URL url = null;
		HttpURLConnection conn = null;
		ctr.progressbar_download.setProgress(0.0);

		// fields null,Empty check
		if (ctr.field_url == null || ctr.field_url.getText().trim().length() < 1 || ctr.field_download_path == null
				|| ctr.field_download_path.getText().trim().length() < 1) {
			System.out.println("Please write all of fields.");
			ctr.addLog("Please write all of fields.", 1);
			return;
		}

		// todo
		// Get filename
		String[] url_split_filename = ctr.field_url.getText().trim().split("/");
		String filename = url_split_filename[url_split_filename.length - 1];
		System.out.println("Filename: " + filename);
		ctr.addLog("Filename: " + filename, 0);

		// Get extension from URL
		String[] url_split_extension = ctr.field_url.getText().split("\\.");
		String filename_extension = url_split_extension[url_split_extension.length - 1];
		System.out.println("FilenameExtension: " + filename_extension);
		ctr.addLog("FilenameExtension: " + filename_extension, 0);

		// URL init.
		try {
			url = new URL(ctr.field_url.getText());
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
				ctr.addLog("Conection Error Code: " + httpStatusCode, 1);
				return;
			} else {
				ctr.addLog("HTTP Conection OK!", 0);
			}
		} catch (ProtocolException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Get content type
		String content_type = conn.getContentType();
		System.out.println("ContentType: " + content_type);
		ctr.addLog("ContentType: " + content_type, 0);

		// Get content length
		long content_length = conn.getContentLengthLong();
		System.out.println("ContentLength: " + content_length);
		ctr.addLog("ContentLength: " + content_length, 0);

		// Input Stream
		DataInputStream dataInStream = null;
		try {
			dataInStream = new DataInputStream(conn.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Output Path
		String output_path = null;
		if (ctr.field_download_path.getText().trim().endsWith("\\")) {
			output_path = ctr.field_download_path.getText().trim().substring(0,
					ctr.field_download_path.getText().trim().length()) + "/" + filename;
			System.out.println("output_path: " + output_path);
		} else {
			output_path = ctr.field_download_path.getText().trim() + "/" + filename;
		}
		ctr.addLog("OutputPath: " + output_path, 0);

		// Output Stream
		DataOutputStream dataOutStream = null;
		try {
			dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output_path)));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

		// Read Data
		int read_size = 4096;
		byte[] b = new byte[read_size];
		int read_byte = 0;
		int count = 0;
		try {
			time = LocalDateTime.now();
			while (-1 != (read_byte = dataInStream.read(b))) {
				count = count + 1;
				if (dataOutStream == null) return;
				dataOutStream.write(b, 0, read_byte);
				setProgressBar(content_length, read_size, read_byte, count);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		ctr.progressbar_download.setProgress(1.0);
		System.out.println("Finish download.");
		ctr.addLog("Finish download.", 0);

		// Close Stream
		try {
			dataOutStream.close();
			dataInStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		conn.disconnect();
	}

	private void setProgressBar(long content_length, int read_size, int read_byte, int count) {
		long interval_ms = 1;
		if (time.isAfter(time.plus(Duration.ofSeconds(interval_ms)))) {
			int percentage = (int) ((read_size * count / (double) content_length) * 100);
			if (100 < percentage) percentage = 100;
			ctr.addLog("Download percentenge: " + String.valueOf(percentage) + "%", 0);
			ctr.progressbar_download.setProgress((double) percentage / 100);
			System.out.println((double) percentage / 100);
			time = LocalDateTime.now();
		}
	}
}
