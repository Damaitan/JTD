package com.damaitan.mobileUI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.os.Environment;
import android.util.Log;

public class JsonHelper{
	
	public enum Type{
		jtd,
		statistics
	}
	public static String RootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LightGTD/";
	public static final String JTDFile = RootPath  + "JTD.json";
	public static final String StatisticsFile = RootPath + "statistics.json";
	
	public JsonHelper() {
	}
	
	public static boolean isFileExist(Type type){
		if (type == Type.statistics){
			return isFileExist(StatisticsFile);
		}
		return isFileExist(JTDFile);
	}
	
	public static  void initFolder(){
		File f = new File(RootPath);
		if (!f.exists()) {
			f.mkdir();
			Log.i("JsonHelper", RootPath);
		}
		
	}
	
	private static boolean isFileExist(String path) {
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
	

	public static boolean saveContentToFile(String json ) throws Exception {
		return saveJsonStringToFile(JTDFile, json);
	}
	
	public static boolean saveStatisticsToFile(String json ) throws Exception {
		return saveJsonStringToFile(StatisticsFile, json);
	}
	
	//@SuppressLint("WorldReadableFiles")
	public static boolean saveJsonStringToFile(String filename, String json ) throws Exception {
		try {
			//@SuppressWarnings("deprecation")
			File f = new File(filename);
			f.setWritable(true);
			//FileOutputStream fout = handler.openFileOutput(filename, Context.MODE_WORLD_WRITEABLE);
			FileOutputStream fout = new FileOutputStream(f);
			byte[] bytes = json.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
	
		}
		return true;
	}
	public static String getJsonString() throws Exception{
		return getJsonString(JTDFile);
	}
	
	public static String getJsonString(String path) throws Exception{
		File f = new File(path);
		//FileInputStream stream  = handler.openFileInput(path);
		FileInputStream stream  = new FileInputStream(f);
		int length = stream.available();
		byte[] buffer = new byte[length];
		stream.read(buffer);
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		arrayOutputStream.write(buffer, 0,length);
		String json = new String(arrayOutputStream.toByteArray());
		stream.close();
		return json;
	}
	
	
	

}