package com.example.mobilelogger;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private GpsInfo gpsInfo;
	private CellInfo cellInfo;
	private WifiInfo wifiInfo;
	private SensorInfo sensorInfo;
	
	private ReadWriteFile gpsInfoFile, cellInfoFile, wifiInfoFile, sensorInfoFile;
	
	private TextView mobilInfo;
	private Button startButton, stopButton;
	private EditText locationId;
	
	private int count = 0;
	private long sleepCycleTime = 1000;
	private boolean loop = true;
	
	final Handler UiHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initUI();
	}
	
	@Override
	public void onDestroy(){
		stopMainProgram();
		
		super.onDestroy();
	}
	
	private void initUI(){
		locationId = (EditText) findViewById(R.id.locationId);
		mobilInfo = (TextView) findViewById(R.id.mobilInfo);
		startButton = (Button) findViewById(R.id.startButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		
		startButton.setOnClickListener(new Button.OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				startMainProgram();
			}
		}); 
		stopButton.setOnClickListener(new Button.OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				stopMainProgram();
			}
		});
	}
	
	private void initManager(){
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		

		gpsInfo = new GpsInfo(locationManager, gpsInfoFile);
		// cellInfo = new CellInfo(telephonyManager, cellInfoFile);
		wifiInfo = new WifiInfo(wifiManager, wifiInfoFile);
		sensorInfo = new SensorInfo(sensorManager, sensorInfoFile);
	}
	
	private void startMainProgram(){
		String fileName = getDateTime();
		gpsInfoFile = new ReadWriteFile("mobileInfo/"+fileName, "gpsInfo", locationId.getText().toString());
		// cellInfoFile = new ReadWriteFile("mobileInfo/"+fileName, "cellInfo", locationId.getText().toString());
		wifiInfoFile = new ReadWriteFile("mobileInfo/"+fileName, "wifiInfo", locationId.getText().toString());
		sensorInfoFile = new ReadWriteFile("mobileInfo/"+fileName, "sensorInfo", locationId.getText().toString());
		
		initManager();
		
		loop = true;
		MobileInfoUpdateThread mobileInfoUpdateThread = new MobileInfoUpdateThread();
		mobileInfoUpdateThread.start();
	}
	
	private void stopMainProgram(){
		mobilInfo.setText("已停止收集行動資料");
		
		try{
			loop = false;
			gpsInfo.removeUpdates();
			sensorInfo.removeUpdates();
		}
		catch(Exception e){
		}
	}
	
	private String getDateTime(){
		Date now = new java.util.Date();
		return new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(now);
	}
	
	final Runnable MobileInfoUpdateResults = new Runnable() {
		public void run() {
			count++;
			Log.d("MobileInfoCounter", ""+count);
			
			String dateTime = getDateTime();
			
			gpsInfo.getLocationInfo(count, dateTime);
			// cellInfo.getCellInfo(count, dateTime);
			wifiInfo.getWifiInfo(count, dateTime);
			sensorInfo.getSensorInfo(count, dateTime);
			
			mobilInfo.setText("正在收集行動資料\nLat: "+gpsInfo.lat+"" +
					", Lon: "+gpsInfo.lon+", Speed: "+gpsInfo.speed);
		}
	};
	
	class MobileInfoUpdateThread extends Thread{
		
		public MobileInfoUpdateThread(){
		}
		public void run(){
			while(loop){
	    		try {
	    			UiHandler.post(MobileInfoUpdateResults);
					Thread.sleep(sleepCycleTime);
				} catch (Exception e) {
					Log.e("Exception", e.getMessage());
				}
	    	}
		}
	}
}
