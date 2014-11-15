package com.example.mobilelogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ReadWriteFile {
	File file;
	
	public ReadWriteFile(String filePath, String fileName){
		try {
			File path=new File("/sdcard/"+filePath);
			if (!path.exists()) {
			    path.mkdirs();
			}
			
			file = new File("/sdcard/"+filePath+"/"+fileName+".txt");
			if(!file.exists()){
				file.createNewFile();
			}
		} catch (Exception e) {}
	}
	
	public String readFile(){
		String content = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String temp = "";
			while ((temp = bufferedReader.readLine()) != null) {
				content += temp + "\n";
			}
			bufferedReader.close();
		} catch (Exception e) {
			content = "Error!";
		}
		
		return content;
	}
	
	public void writeFile(String content){
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			outputStreamWriter.append(content);
			outputStreamWriter.close();
			fileOutputStream.close();
		} catch (Exception e) {}
	}
}
