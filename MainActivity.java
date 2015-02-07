package com.example.myfirstapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView		mHelloWorld;
	private SensorManager	mSensorManager;
	private Sensor			mLight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("", "beginning of onCreate");

		mHelloWorld =		(TextView		) findViewById		(R.id.helloworld	);
		mSensorManager =	(SensorManager	) getSystemService	(SENSOR_SERVICE		);
		mLight =			 mSensorManager.getDefaultSensor	(Sensor.TYPE_LIGHT	);
		
		if (mLight == null)
			mHelloWorld.setText("Sensor.TYPE_LIGHT NOT Available");
		else
			mSensorManager.registerListener(LightSensorListener, mLight, SensorManager.SENSOR_DELAY_UI);
		
		Log.d("", "end of onCreate");
	}///onCreateOptionsMenu is next

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {///after onCreate
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		Log.d("", "onCreateOptionsMenu");
		return true;
	}

	private final SensorEventListener LightSensorListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() != Sensor.TYPE_LIGHT)
                return;
			mHelloWorld.setText("LIGHT: " + event.values[0]);
    	}

    	@Override
    	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	}
	};
}
