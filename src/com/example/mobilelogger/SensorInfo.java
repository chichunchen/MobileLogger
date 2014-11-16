package com.example.mobilelogger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorInfo implements SensorEventListener {
	private SensorManager sensorManager;
	private ReadWriteFile readWriteFile;
	
	private String x = "0", y = "0", z = "0";
	
	public SensorInfo(SensorManager sensorManager, ReadWriteFile readWriteFile){
		this.sensorManager = sensorManager;
		this.readWriteFile = readWriteFile;
		
		String content = "LocationID\tIndex\tDateTime\tX\tY\tZ\n";
		this.readWriteFile.writeFile(content);
		
		@SuppressWarnings("deprecation")
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void getSensorInfo(int count, String dateTime){
		String content = readWriteFile.locationId+"\t"+count+"\t"+dateTime+"\t"+x+"\t"+y+"\t"+z+"\n";
		this.readWriteFile.writeFile(content);
	}
	
	public void removeUpdates(){
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values;
		
		x = String.valueOf(values[0]);
		y = String.valueOf(values[1]);
		z = String.valueOf(values[2]);
	}

}
