package com.example.mobilelogger;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiInfo {
	private WifiManager wifiManager;
	private ReadWriteFile readWriteFile;
	
	public List<ScanResult> scanReslutList;
	
	private int count;				// Calculate the index of wifi info data 
	private String dateTime;
	
	public WifiInfo(WifiManager wifiManager, ReadWriteFile readWriteFile){
		this.wifiManager = wifiManager;
		this.readWriteFile = readWriteFile;
	
		// Lid = locationId, but the length is too long
		String content = "LID\tIndex\tDateTime\tBSSID\tSSID\tRSSI\n";
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
				
				// result level returns the received signal strength of 802.11 network
				String content = readWriteFile.locationId+"\t"
									+count+"\t"
									+dateTime+"\t"
									+result.BSSID+"\t"
									+result.SSID+"\t"
									// level returns the received signal strength of 802.11 network
									+result.level+"\n";
					
				this.readWriteFile.writeFile(content);
			}
		}
	}
}
