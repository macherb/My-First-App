package com.example.myfirstapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements FragmentManager.OnBackStackChangedListener {

	private TextView		mHelloWorld,
							mBackStack;
	private SensorManager	mSensorManager;
	private Sensor			mLight;
	private Button			mAdd,
							mPop;

	// The component name of MusicIntentReceiver, for use with media button and remote control
    // APIs
    ComponentName mMediaButtonReceiverComponent;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("", "beginning of onCreate");

		mHelloWorld =		(TextView		) findViewById		(R.id.light			);
		mSensorManager =	(SensorManager	) getSystemService	(SENSOR_SERVICE		);
		mLight =			 mSensorManager.getDefaultSensor	(Sensor.TYPE_LIGHT	);
        mAdd =				(Button			) findViewById		(R.id.add			);
        mPop =				(Button			) findViewById		(R.id.pop			);
        mBackStack =		(TextView		) findViewById		(R.id.backStack		);

        mAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getFragmentManager()
                	.beginTransaction()
                	.addToBackStack(null)
                	.commit();
            	Log.d("", "Add button");
            }
        });
        mPop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getFragmentManager().popBackStack();
            	Log.d("", "Pop button");
            }
        });
        getFragmentManager().addOnBackStackChangedListener(this);

        if (mLight == null)
			mHelloWorld.setText("Sensor.TYPE_LIGHT NOT Available");
		else
			mSensorManager.registerListener(LightSensorListener, mLight, SensorManager.SENSOR_DELAY_UI);
		
		mMediaButtonReceiverComponent = new ComponentName(this, MusicIntentReceiver.class);
		
		Log.d("", "end of onCreate");
	}///onCreateOptionsMenu is next

    @Override
    public void onBackStackChanged() {
    	Log.d("", "onBackStackChanged");
    	mBackStack.setText("Stack is +" + getFragmentManager().getBackStackEntryCount());
    	if (getFragmentManager().getBackStackEntryCount() == 0)
    		mPop.setEnabled(false);
    	else
    		mPop.setEnabled(true);
    }
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
