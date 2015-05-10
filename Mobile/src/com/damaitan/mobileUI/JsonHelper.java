package com.damaitan.mobileUI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;

public class JsonHelper{

	public static final String JTDFile = "JTD.json";
	public static final String StatisticsFile = "statistics.json";
	
	public JsonHelper() {
	}
	
	public static boolean isJTDFileExist() {
		return isFileExist(JTDFile);
	}
	
	public static boolean isFileExist(String path) {
		if (path == null) {
			return false;
		}
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	

	public static boolean saveContentToFile(Context handler, String json ) throws Exception {
		return saveJsonStringToFile(handler, JTDFile, json);
	}
	
	public static boolean saveStatisticsToFile(Context handler, String json ) throws Exception {
		return saveJsonStringToFile(handler, StatisticsFile, json);
	}
	
	public static boolean saveJsonStringToFile(Context handler, String filename, String json ) throws Exception {
		try {
			FileOutputStream fout = handler.openFileOutput(filename, Context.MODE_WORLD_READABLE);
			byte[] bytes = json.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
	
		}
		return true;
	}
	public static String getJsonString(Context handler) throws Exception{
		return getJsonString(handler, JTDFile);
	}
	
	public static String getJsonString(Context handler, String path) throws Exception{
		FileInputStream stream  = handler.openFileInput(path);
		int length = stream.available();
		byte[] buffer = new byte[length];
		stream.read(buffer);
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		arrayOutputStream.write(buffer, 0,length);
		String json = new String(arrayOutputStream.toByteArray());
		return json;
	}
	
	
	

}