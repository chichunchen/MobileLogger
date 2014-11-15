package com.example.mobilelogger;

import java.util.List;

import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class CellInfo {
	private TelephonyManager telephonyManager;
	private ReadWriteFile readWriteFile;
	
	public GsmCellLocation cellLocation;
	public List<NeighboringCellInfo> NeighboringList;
	
	private int count;
	private String dateTime;
	public String networkOperator = "", mcc = "-1", mnc = "-1";
	
	public CellInfo(TelephonyManager telephonyManager, ReadWriteFile readWriteFile){
		this.telephonyManager = telephonyManager;
		this.readWriteFile = readWriteFile;
		
		String content = "Index\tDateTime\tLAC\tCellID\tRSSI\n";
		this.readWriteFile.writeFile(content);
		
		try{
			networkOperator = telephonyManager.getNetworkOperator();
			mcc = networkOperator.substring(0, 3);
			mnc = networkOperator.substring(3);
		}
		catch(Exception e){
			networkOperator = "";
			mcc = "-1";
			mnc = "-1";
		}
	}
	
	public void getCellInfo(int count, String dateTime){
		cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
		NeighboringList = telephonyManager.getNeighboringCellInfo();
		
		this.count = count;
		this.dateTime = dateTime;
		
		parseCellLocation();
		parseNeighboringCellInfo();
	}
	
	private void parseCellLocation(){
		String cid = String.valueOf(cellLocation.getCid());
		String lac = String.valueOf(cellLocation.getLac());
		
		String content = count+"\t"+dateTime+"\t"+lac+"\t"+cid+"\t"+0+"\n";
		readWriteFile.writeFile(content);
	}
	
	private void parseNeighboringCellInfo(){
		for (int i = 0; i < NeighboringList.size(); i++) {

			String dBm;
			int rssi = NeighboringList.get(i).getRssi();
			if (rssi == NeighboringCellInfo.UNKNOWN_RSSI) {
				dBm = "Unknown RSSI";
			} else {
				dBm = String.valueOf(-113 + 2 * rssi);
			}
			
			String content = count+"\t"+dateTime+"\t"+NeighboringList.get(i).getLac()+"\t"+NeighboringList.get(i).getCid()+"\t"+dBm+"\n";
			readWriteFile.writeFile(content);
		}
	}
}
