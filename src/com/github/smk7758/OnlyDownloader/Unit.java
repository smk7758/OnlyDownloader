package com.github.smk7758.OnlyDownloader;

public class Unit {
	Main main;

	public Unit(Main main) {
		this.main = main;
	}

	public String[] splitString(String string, int mode) {
		String[] strings = {"" , "", ""};
		if (mode == 0) {
			strings[0] = string.substring(0, 3);
			strings[1] = string.substring(3, 6);
			strings[2] = string.substring(6, 9);
		} else if (mode == 1) {
			strings[0] = string.substring(0, 2);
			strings[1] = string.substring(2, 4);
			strings[2] = string.substring(4, 6);
		}
		return strings;
	}

	public int[] parseIntFromString(String[] a) {
		int[] i_RGB = {0 , 0, 0};
		try {
			i_RGB[0] = Integer.parseInt(a[0]);
			i_RGB[1] = Integer.parseInt(a[1]);
			i_RGB[2] = Integer.parseInt(a[2]);
		} catch (NumberFormatException e) {
			System.out.println("The seccond argument is not number.");
			System.exit(0);
		}
		return i_RGB;
	}

	public String[] toHexStringFromNumber(int[] i_RGB) {
		String[] s_RGB = {"", "", ""};
		s_RGB[0] = Integer.toHexString(i_RGB[0]);
		s_RGB[1] = Integer.toHexString(i_RGB[1]);
		s_RGB[2] = Integer.toHexString(i_RGB[2]);
		return s_RGB;
	}

	public String[] toDecimalStringFromString(String[] strings) {
		String[] s_RGB = {"" , "", ""};
		try {
			s_RGB[0] = Integer.parseInt(strings[0], 16)+"";
			s_RGB[1] = Integer.parseInt(strings[1], 16)+"";
			s_RGB[2] = Integer.parseInt(strings[2], 16)+"";
		} catch (NumberFormatException e) {
			System.out.println("Program cannot parse to HexString.");
			System.exit(0);
		}
		return s_RGB;
	}

	public String[] toTwoHexString(String[] strings) {
		if (strings[0].length() < 2) strings[0] = "0"+strings[0];
		if (strings[1].length() < 2) strings[1] = "0"+strings[1];
		if (strings[2].length() < 2) strings[2] = "0"+strings[2];
		return strings;
	}
}