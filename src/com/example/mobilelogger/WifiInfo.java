package com.example.mobilelogger;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiInfo {
	private WifiManager wifiManager;
	private ReadWriteFile readWriteFile;
	
	public List<ScanResult> scanReslutList;
	
	private int count;
	private String dateTime;
	
	public WifiInfo(WifiManager wifiManager, ReadWriteFile readWriteFile){
		this.wifiManager = wifiManager;
		this.readWriteFile = readWriteFile;
		
		String content = "Index\tDateTime\tBSSID\tSSID\tRSSI\n";
		this.readWriteFile.writeFile(content);
	}
	
	public void getWifiInfo(int count, String dateTime){
		wifiManager.startScan();
		scanReslutList = wifiManager.getScanResults();
		
		this.count = count;
		this.dateTime = dateTime;
		
		parseWifiScanResult();
	}
	
	private void parseWifiScanResult(){
		if (scanReslutList != null) {
			final int size = scanReslutList.size();
			for(int i=0; i < size; i++){
				ScanResult result = scanReslutList.get(i);
				String content = count+"\t"+dateTime+"\t"+result.BSSID+"\t"+result.SSID+"\t"+result.level+"\n";
				this.readWriteFile.writeFile(content);
			}
		}
	}
}
