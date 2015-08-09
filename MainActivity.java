package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements FragmentManager.OnBackStackChangedListener {

    static final double _eQuatorialEarthRadius = 6378.1370D;
    static final double _d2r = (Math.PI / 180D);

    private TextView		mHelloWorld,
							mBackStack,
							mHome,

							mNow;
	private SensorManager	mSensorManager;
	private Sensor			mLight;
	private Button			mAdd,
							mPop,
							mNew;
	private	EditText		mLatitude,
							mLongitude;

	LocationManager			locationManager;

	LocationListener		locationListener;

	double					HomeLatitude,
							HomeLongitude,
							HomeAltitude;

	ComponentName mMediaButtonReceiverComponent;

    @Override
	protected void onCreate(Bundle savedInstanceState) {//first, no onSaveInstanceState
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("", "beginning of onCreate");

		mHelloWorld =		(TextView		) findViewById		(R.id.light			);
		mSensorManager =	(SensorManager	) getSystemService	(SENSOR_SERVICE		);
		mLight =			 mSensorManager.getDefaultSensor	(Sensor.TYPE_LIGHT	);
        mAdd =				(Button			) findViewById		(R.id.add			);
        mPop =				(Button			) findViewById		(R.id.pop			);
        mBackStack =		(TextView		) findViewById		(R.id.backStack		);
        mNew =				(Button			) findViewById		(R.id.newLocation	);
        mHome =				(TextView		) findViewById		(R.id.locationHome	);
        mNow =				(TextView		) findViewById		(R.id.locationNow	);
        mLatitude =			(EditText		) findViewById		(R.id.Latitude		);
		mLongitude =		(EditText		) findViewById		(R.id.Longitude		);

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
        mNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            	{
            		HomeLatitude = 0;
            		HomeLongitude = 0;
            		HomeAltitude = 0;
            		mHome.setText("no gps");
            	} else {
            		Location HomeLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            		try {
                		HomeLatitude = HomeLocation.getLatitude();
                		HomeLongitude = HomeLocation.getLongitude();
                		HomeAltitude = HomeLocation.getAltitude();
//            			mHome.setText(HomeLatitude + " " + HomeLongitude + " " + HomeAltitude);
                		if (
                			(!mLatitude.	getText().toString().equals(""))
                    		&&
                    		(!mLongitude.	getText().toString().equals(""))
                    	   )
                		{
                			double dlong =	(HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * _d2r;

                			double dlat =	(HomeLatitude - Double.parseDouble(mLatitude.getText().toString())) * _d2r;

                			double a =		Math.pow(Math.sin(dlat / 2D), 2D) +
                	        				Math.cos(Double.parseDouble(mLatitude.getText().toString()) * _d2r) * 
                	        				Math.cos(HomeLatitude * _d2r) * 
                	        				Math.pow(Math.sin(dlong / 2D), 2D);

                	        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));

                	        String distance =	"You are " + _eQuatorialEarthRadius * c + " km ";

                	        String direction = null;

                			if		(Double.parseDouble(mLatitude.	getText().toString()) < HomeLatitude	)		//destination is south of location
                			{
                				if		(Double.parseDouble(mLongitude.	getText().toString()) < HomeLongitude	)	//destination is west  of location
                				{
                					if      (
                							 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 4D)
                							 <
                							 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 1D)
                							)
                					{
                						direction = "east";
                					}
                					else if (
                							 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 4D)
                							  <
                							 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 3D)
                							)
                					{
                						direction = "east north east";
                					}
                					else if (
                							 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 3D)
                							  <
                							 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 4D)
                							)
                					{
                						direction = "north east";
               						}
                					else if (
                							 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 1D)
                							  <
                							 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 4D)
                							)
                					{
                						direction = "north north east";
                					}
                					else
                					{
                						direction = "north";
                					}
                				}
                				else if (Double.parseDouble(mLongitude.	getText().toString()) > HomeLongitude	)	//destination is east  of location
                    			{
                					if      (
               							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 4D)
               							 	  <
               							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 1D)
                							)
                					{
                						direction = "west";
                					}
                					else if (
               							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 4D)
               							 	  <
               							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 3D)
                							)
                					{
                						direction = "west north west";
                					}
                					else if (
                							 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 3D)
                							  <
                							 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 4D)
                							)
                					{
                						direction = "north west";
              						}
                					else if (
               							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 1D)
               							 	 <
               							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 4D)
                							)
                					{
                						direction = "north north west";
                					}
                					else
                					{
                						direction = "north";
                					}
                    			}
                			}
                			else if (Double.parseDouble(mLatitude.	getText().toString()) > HomeLatitude	)		//destination is north of location
                			{
                				if		(Double.parseDouble(mLongitude.	getText().toString()) < HomeLongitude	)	//destination is west  of location
                				{
                					if      (
               							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 4D)
               							 	  <
               							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 1D)
                							)
                					{
                						direction = "east";
                					}
                					else if (
               							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 4D)
               							 	  <
               							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 3D)
                							)
                					{
                						direction = "east south east";
                					}
                					else if (
               							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 3D)
               							 	  <
               							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 4D)
                							)
                					{
                						direction = "south east";
              						}
                					else if (
               							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 1D)
               							 	  <
               							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 4D)
                							)
                					{
                						direction = "south south east";
                					}
                					else
                					{
                						direction = "south";
                					}
                				}
                				else if (Double.parseDouble(mLongitude.	getText().toString()) > HomeLongitude	)	//destination is east  of location
                				{
                					if      (
              							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 4D)
              							 	  <
              							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 1D)
                							)
                					{
                						direction = "west";
                					}
                					else if (
              							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 4D)
              							 	  <
              							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 3D)
                							)
                					{
                						direction = "west south west";
                					}
                					else if (
              							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 3D)
              							 	  <
              							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 4D)
                							)
                					{
                						direction = "south west";
             						}
                					else if (
              							 	 Math.abs((HomeLatitude -  Double.parseDouble(mLatitude. getText().toString())) * 1D)
              							 	  <
              							 	 Math.abs((HomeLongitude - Double.parseDouble(mLongitude.getText().toString())) * 4D)
                							)
                					{
                						direction = "south south west";
                					}
                					else
                					{
                						direction = "south";
                					}
                				}
                			}

                			mHome.setText("" + distance + direction + " of the location");
                		}
            		} catch (Exception ex) {
                		HomeLatitude = 0;
                		HomeLongitude = 0;
                		HomeAltitude = 0;
            			mHome.setText("" + ex.toString());
            		}
            	}
            	Log.d("", "New button");
            }
        });
        getFragmentManager().addOnBackStackChangedListener(this);

        if (mLight == null)
			mHelloWorld.setText("Sensor.TYPE_LIGHT NOT Available");
		else
			mSensorManager.registerListener(LightSensorListener, mLight, SensorManager.SENSOR_DELAY_UI);
		
		mMediaButtonReceiverComponent = new ComponentName(this, MusicIntentReceiver.class);

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		    	Log.d("", "onLocationChanged");
		    	try {
		    		mNow.setText(location.getLatitude() + " " + location.getLongitude() + " " + location.getAltitude());
		    	} catch (Exception ex) {
		    		mNow.setText("" + ex.toString());
		    	}
			}

		    public void onStatusChanged(String provider, int status, Bundle extras) {
		    	Log.d("", "onStatusChanged");
		    }

		    public void onProviderEnabled(String provider) {
		    	Log.d("", "onProviderEnabled");
		    }

		    public void onProviderDisabled(String provider) {
		    	Log.d("", "onProviderDisabled");
		    }
		  };

		// Register the listener with the Location Manager to receive location updates
///		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
//		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		if (savedInstanceState != null) {
			mHome.setText(savedInstanceState.getInt("HomeLatitude") + " " + savedInstanceState.getInt("HomeLongitude") + " " + savedInstanceState.getInt("HomeAltitude"));
		} else {
			mHome.setText("No Home set!");
		}

    	Log.d("", "end of onCreate");
	}//onStart is next

    @Override
    public void onBackStackChanged() {
    	Log.d("", "onBackStackChanged");
    	mBackStack.setText("Stack is +" + getFragmentManager().getBackStackEntryCount());
    	if (getFragmentManager().getBackStackEntryCount() == 0)
    		mPop.setEnabled(false);
    	else
    		mPop.setEnabled(true);

		List<Address> addresses = null;

    	Geocoder geocoder = new Geocoder(this, Locale.getDefault());
    	try {    		
    		if (
    			(!mLatitude.	getText().toString().equals(""))
    			&&
    			(!mLongitude.	getText().toString().equals(""))
    		   )
    			addresses = geocoder.getFromLocation(Double.parseDouble(mLatitude.getText().toString()), Double.parseDouble(mLongitude.getText().toString()), 1);
    		else
    			addresses = geocoder.getFromLocation(HomeLatitude, HomeLongitude, 1);
    		if (addresses.size() != 0) {//(addresses != null)
    			Address address = addresses.get(0);
    			//getThoroughfare() street name
    			//getLocality() city
    			//getAdminArea() state abbreviation
    			//getFeatureName() Golden Gate Bridge
    			ArrayList<String> addressFragments = new ArrayList<String>();
    			for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
    	            addressFragments.add(address.getAddressLine(i));
    	        mHome.setText(TextUtils.join(System.getProperty("line.separator"), addressFragments));
    		} else
    			mHome.setText("Unknown");
    	} catch (Exception ex) {
    		mHome.setText("" + ex.toString());
    	}
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {//after onResume
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		Log.d("", "onCreateOptionsMenu");
		return true;
	}//last thing before

    @Override
    public void onDestroy() {//after onStop
    	Log.d("", "onDestroy");
///		locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }//last thing before

    @Override
    protected void onSaveInstanceState(Bundle outState) {//after onPause
//		super.onSaveInstanceState(outState);//move to bottom?
        outState.putDouble("HomeLatitude", Double.valueOf(HomeLatitude));
        outState.putDouble("HomeLongitude", Double.valueOf(HomeLongitude));
        outState.putDouble("HomeAltitude", Double.valueOf(HomeAltitude));
        Log.d("", "onSaveInstanceState");
    }//onStop is next

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
    	Log.d("", "onRestoreInstanceState");
    	if (savedInstanceState != null) {
			mHome.setText(savedInstanceState.getInt("HomeLatitude") + " " + savedInstanceState.getInt("HomeLongitude") + " " + savedInstanceState.getInt("HomeAltitude"));
		} else {
			mHome.setText("No Home set?");
		}
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {//after onStart
		super.onPostCreate(savedInstanceState);//necessary! top or bottom?
    	Log.d("", "onPostCreate");
    }//onResume is next

    @Override
    protected void onRestart() {//first, after onSaveInstanceState
		super.onRestart();//necessary! top or bottom?
    	Log.d("", "onRestart");
    }//onStart is next

    @Override
    protected void onStart() {//after onCreate, onRestart
		super.onStart();//necessary! top or bottom?
    	Log.d("", "onStart");
    }//onPostCreate, or onResume is next

    @Override
    protected void onResume() {//after onPostCreate, onStart
		super.onResume();//necessary! top or bottom?
    	Log.d("", "onResume");
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }//onCreateOptionsMenu is next, or last thing before

    @Override
    protected void onStop() {//after onPause, onSaveInstanceState
    	super.onStop();
    	Log.d("", "onStop");
    }//onDestroy, last thing before

    @Override
    protected void onPause() {//pressed back, home
    	super.onPause();
    	Log.d("", "onPause");
    	locationManager.removeUpdates(locationListener);
    }//onStop, onSaveInstanceState is next

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
