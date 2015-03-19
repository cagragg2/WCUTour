package nav_fragments;

import java.util.ArrayList;
import java.util.Calendar;

import models.Waypoint;
import navigation.NavigationActivity;
import navigation.ProximityReceiver;
import utilities.LocationHelper;
import utilities.Variables;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Marker;

import edu.wcu.wcutour.R;
import activities.SelectedItem;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Caleb Gragg, Jeremiah Griffin
 * Fragment tab of the TourNavigationActivity.  Handles the creation of google maps and
 * sets up the tour.  Guides the user throughout the tour and has a LocationListener to
 * monitor the users location.
 */
public class NavFragment extends Fragment implements LocationListener{//, SensorEventListener {
	
	private GoogleMap googleMap; //google map to use
	private Polyline line; //for drawing the line on the map 
	private ArrayList<Waypoint> tempVariable = new ArrayList<Waypoint>(); //just to hold the waypoints for the class meeting.
	private BroadcastReceiver mybroadcast; //to send msgs to the proximity reciever class to handle proximity alerts.
	private TextView locationView; //to set the location to be traveled to
	private TextView distanceView; // to set the distance between the next location.
	private ProgressBar mProgress; //the progress bar
	private int counter = 0;
	private int counter2 = 0;
	View V;
	Activity activity;
	private static View view;
	Marker myLocMarker;
	SensorManager sensor;
	Sensor mAccelerometer;
    private UpdateLocation updateLocationTask;//async task to find location
    private boolean hasLocation = false; //wether or not we have found the user location
    LocationHelper locationHelper; //to help with determining initial location
	
	@Override
	public void onResume() {
		super.onResume();

		//sensor = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		//mAccelerometer = sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      //  mAccelerometer = sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	//	sensor.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

    /**
     * onCreate method to start the async task of finding the users initial location
     * @param savedInstanceState
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        locationHelper = new LocationHelper();
        locationHelper.getLocation(getActivity().getApplicationContext(),locationResult);
        updateLocationTask = new UpdateLocation();
        updateLocationTask.execute(getActivity().getApplicationContext());
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

    /**
     * onDestroy method to stop tracking the users location.
     */
	@Override
	public void onDestroy() {
		super.onDestroy();
	    try {
	        getActivity().unregisterReceiver(mybroadcast);
        //    sensor.unregisterListener(this);
	    } catch (IllegalArgumentException e) {
	       // Log.d("reciever", e.toString());
	    }
		Variables.locationManager.removeUpdates(this);
		Variables.locationManager.removeProximityAlert(Variables.pi);
	}

    /**
     * Called when the view is created initializes the map.
     * @param inflater
     * @param containter
     * @param savedInstanceState
     * @return
     */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
        Log.e("tour","starting oncreateview");
		//View rootView = inflater.inflate(R.layout.navfragment, containter, false);
		if(view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if(parent != null) 
				parent.removeView(view);
		}
		
		try {
			view = inflater.inflate(R.layout.navfragment, containter,false);
		} catch (InflateException e) {}
		
	    locationView = (TextView) view.findViewById(R.id.tv1);
	    distanceView = (TextView) view.findViewById(R.id.tv2);
	    mProgress = (ProgressBar) view.findViewById(R.id.progressBar1);
		start();


		return view;
	}

    /**
     * Method to initialize the map and start the proximity reciever.
     */
	public void start() {
		 Variables.locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

		    try {
		    	initilizeMap();
		  //  	setUpMap();

		    //	startListening();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		    //=================================
		    
		    //the first waypoint in the tour.
		    tempVariable.add(Variables.selectedTour.getTour().get(0));


		    mybroadcast = new ProximityReceiver(); //sets up the receiver.
		    String proximitys = "ProximityService";
		    IntentFilter filter = new IntentFilter(proximitys);
		    getActivity().registerReceiver(mybroadcast,filter); //registers the receiver to use.
		    
		    //lat and long of the first waypoint in the tour.
		    double lat1 = tempVariable.get(0).getLatitude();
		    double lon1 = tempVariable.get(0).getLongitude();
		    //sets the selected waypoint for when the information screen appears when in range of the proximity.
		    Variables.selectedWaypoint = tempVariable.get(0);
		    //radius to check for proximity alerts.
		    float radius=15;
		    //the intent when in the proximity.
		    Intent i = new Intent(proximitys);
		    Variables.pi = PendingIntent.getBroadcast(getActivity().getApplicationContext(), -1, i, 0);
		    //adds the proximity alert for the first waypoint in the list.
		    Variables.locationManager.addProximityAlert(lat1, lon1, radius, -1, Variables.pi);
		    Variables.tourCounter = 1; //so we know which waypoint in the list we are at.

		  
		    locationView.setText(Variables.selectedTour.getTour().get(0).getDescription() + "");
		    distanceView.setText("-999");
		
		    /*
		     * Progress bar
		     */
		    mProgress.setMax(100);
		    Variables.changeDistance = true;
		    Variables.updateInformation = false;

     //   sensor = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //mAccelerometer = sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    //    mAccelerometer = sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    //    sensor.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	/**
	 * Checks to see if the google maps is null if it is creates a new google maps.
	 */
	private void initilizeMap() {
		if(googleMap == null) {
			googleMap = ((MapFragment) 
					getFragmentManager().findFragmentById(R.id.map2)).getMap();
			//check if map is created successully or not
			if(googleMap == null) {
				Toast.makeText(getActivity().getApplicationContext(),
						"Sorry! Unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	/**
	 * Sets up the map by getting the current location and initilizing things to start the tracking the location changes.
	 */
	private void setUpMap() {
		
		Variables.myTracks = new ArrayList<LatLng>(); // Everytime the location changes.
	    googleMap.setMyLocationEnabled(true); //track my location.
	
		//=============================================================
		//starts tracking location.
	    Variables.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
		//gets the last known location for starting dropping the polylines.
		Location loc = Variables.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		// progress bar when finding location
		//asyncronis task
		//Checks to see how recent the last location update was.
			if(loc != null && loc.getTime() > Calendar.getInstance().getTimeInMillis() - 2*60*1000) {
				//Toast.makeText(getActivity().getApplicationContext(), "Waiting to aquire Location.", Toast.LENGTH_SHORT).show();
				loc = Variables.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),"Location Found.",Toast.LENGTH_SHORT).show();
			}
			//sets up the first point to be tracked.
		Variables.myTracks.add(new LatLng(loc.getLatitude(),loc.getLongitude()));
		
		//Pass them to a new CameraUpdateObject
		CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(),loc.getLongitude())); 
		//Specify a zoom level
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(19);
		//Position and zoom camera;
		googleMap.moveCamera(center); 
		googleMap.animateCamera(zoom);
		googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		//=============================================================

        CameraPosition currentPlace = new CameraPosition.Builder()
                .target(new LatLng(Variables.myLocation.getLatitude(), Variables.myLocation.getLongitude()))
                .tilt(65.5f).zoom(18f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));

        //sets up the markers for the map
	    setMarkers();
	}
	
	/**
	 * Sets up the markers for the waypoints in the selected tour.
	 */
	public void setMarkers(){
		
	    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    googleMap.setTrafficEnabled(true);
	    

	    //loops over all waypoints in the tour and sets a marker for each waypoint
	    for(int i = 0; i < Variables.selectedTour.getTour().size(); i++){
	        googleMap.addMarker(new MarkerOptions().position(new LatLng(Variables.selectedTour.getTour().get(i).getLatitude(), 
	        							Variables.selectedTour.getTour().get(i).getLongitude())).title(Variables.selectedTour.getTour().get(i).getDescription()).snippet("WCU")
	        							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

	    }

        if(Variables.selectedTour.getTourName().equals("Sport's Tour")) {
            setUpSportsTour();
        }
        if(Variables.selectedTour.getTourName().equals("Cross Campus Tour")){
            setUpCrossCampusTour();
        }
		
	    myLocMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(0,
	    		0)));
	}
	
	
	/**
	 * Draws a lime green polyline everytime the location changes.
	 */
	public void trackLocation(Location location) {
		
		LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
		
		PolylineOptions pOptions = new PolylineOptions().width(5).color(Color.GREEN)
				.geodesic(true);
		Variables.myTracks.add(latlng);// move up
		
		for(int i = 0; i < Variables.myTracks.size(); i++) {
			LatLng point = Variables.myTracks.get(i);
			pOptions.add(point);
		}
		
		line = googleMap.addPolyline(pOptions);		
	}
	/**
	 * Will use later for updating the UI on how far to the next location.
	 */
	
	public float checkDistance(Location loc1, Location loc2) {
		return loc1.distanceTo(loc2);
	}

	/*
	 * Called when the users location is changed.
	 * @param location the current location of the device.
	 */
	@Override
	public void onLocationChanged(Location location) {
		trackLocation(location);

        //calcs the distance to the next location
		Location loc99 = new Location("");
		loc99.setLatitude(Variables.selectedTour.getTour().get(Variables.tourCounter-1).getLatitude());
		loc99.setLongitude(Variables.selectedTour.getTour().get(Variables.tourCounter-1).getLongitude());
		int distance = (int)(Math.round(loc99.distanceTo(location)));
		distanceView.setText(distance + " meters");
		
		locationView.setText("" + Variables.selectedTour.getTour().get(Variables.tourCounter-1).getDescription());
	
		//update the progress bar
		if(Variables.changeDistance) {	
			Variables.initialDistance = distance;
			Variables.changeDistance = false;
		}
		/*
		 * To bring up the information screen and vibrate when the user reaches the waypoint.
		 */
		if(Variables.updateInformation) {
		if(counter2 == 0) {
			 Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
			 // Vibrate for 500 milliseconds
			 v.vibrate(500);
			 Intent i = new Intent(getActivity(), SelectedItem.class);
			 
			Variables.selectedWaypoint = Variables.selectedTour.getTour().get(counter);
			Variables.selectedItem = Variables.selectedWaypoint.getDescription();
		
		//	i.putExtra(Variables.selectedItem + "", ((TextView) view).getText());
			i.putExtra(Variables.selectedItem,true);
		
			startActivity(i);
			counter++;
			}
			counter2 =0;
			Variables.updateInformation = false;
		}
		
		Variables.progressStatus = ((double) (Variables.initialDistance-distance)/Variables.initialDistance) * 100;
		int progress = (int) (Math.round(Variables.progressStatus));
		mProgress.setProgress(progress);
		
		 Variables.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
			//gets the last known location for starting dropping the polylines.
			Location loc = Variables.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			//Pass them to a new CameraUpdateObject
			CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(),loc.getLongitude())); 
			//Specify a zoom level
		    
	}

    /**
     * Updates the camera to a new bearing.
     * @param bearing
     */
    public void updateCamera(float bearing) {
        CameraPosition currentPlace = new CameraPosition.Builder()
                .target(new LatLng(Variables.myLocation.getLatitude(),Variables.myLocation.getLongitude()))
                .bearing(bearing).tilt(65.5f).zoom(18f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));

    }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

public void setUpSportsTour(){
    // Creating temporary arraylist that builds tour path.
    ArrayList<LatLng> points = new ArrayList<LatLng>();
    PolylineOptions pOptions = new PolylineOptions().width(15).color(Color.rgb(128,0,128)) //85,30,97
            .geodesic(true);
    
    MarkerOptions options = new MarkerOptions();
    for(int z = 0; z < points.size(); z++) {
        LatLng point = points.get(z);
        pOptions.add(point);

    }
    googleMap.addMarker(options.position(soccer)
            .title("Soccer Field and Track")
            .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    googleMap.addMarker(options.position(baseball)
            .title("Baseball Field")
            .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    googleMap.addMarker(options.position(ramsey)
            .title("Ramsey Center")
            .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

    googleMap.addMarker(options.position(football)
            .title("E.J. Whitmore Stadium")
            .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    line = googleMap.addPolyline(pOptions);

}
public void setUpCrossCampusTour() {
		
		//LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
		
		
		PolylineOptions pOptions = new PolylineOptions().width(15).color(Color.rgb(128,0,128)) //85,30,97
				.geodesic(true);
		//Arraylist to hold all coordinates on tour. 
		ArrayList<LatLng> hunterToCrc = new ArrayList<LatLng>();
		//Building Tour
	
		LatLng point;
		MarkerOptions options = new MarkerOptions();
		for(int z = 0; z < Variables.crossCampusTour.size(); z++) {
			point = Variables.crossCampusTour.get(z).getPoint();
			pOptions.add(point);
			
		}
		
		googleMap.addMarker(options.position(hunterLibrary)
								   .title("Hunter Library")
								   .icon(BitmapDescriptorFactory
								         .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		googleMap.addMarker(options.position(Courtyard)
				   			.title("Courtyard Dining Hall")
				   			.snippet("Starbucks Coffee, \n" +
				   					"Whichwich, " +
				   					"Cafeteria")
				   			.icon(BitmapDescriptorFactory
				            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		googleMap.addMarker(options.position(R)
				   .title("Alumni Tower")
				   .snippet("Dedicated by Alumni in 1989")
				   .icon(BitmapDescriptorFactory
				         .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		googleMap.addMarker(options.position(bb)
				   .title("Belk Building")
				   .snippet("The Belk Building is on your left!")
				   .icon(BitmapDescriptorFactory
				         .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		
		googleMap.addMarker(options.position(a1)
								   .title("E.J. Whitmore Stadium")
								   .snippet("This is the last location, thanks for taking the tour!")
								   .icon(BitmapDescriptorFactory
								         .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		line = googleMap.addPolyline(pOptions);
		//Variables.myTracks.add(latlng);
	}

/*@Override
public void onSensorChanged(SensorEvent event) {
	float degree = Math.round(event.values[0]);

if(myLocMarker != null) {
	//myLocMarker.setRotation(degree *20);
            updateCamera(event.values[0]);}
	
}

@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub

	
} */

    /**
     * Async task to find the users location in the background.  When finished calls the setUpMap()
     * method
     */
    public class UpdateLocation extends AsyncTask<Context, Void, Void> {

        //dialog box
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        /**
         * called before the execution.
         */
        protected void onPreExecute() {
            this.dialog.setMessage("Searching for Location");
            this.dialog.show();
            Toast.makeText(getActivity().getApplicationContext(), "Waiting to aquire Location.", Toast.LENGTH_SHORT).show();
            Log.e("tour","doing pre execute");
        }

        /**
         * Waits for Variables to find location.
         * @param params
         * @return
         */
        protected Void doInBackground(Context... params) {
            int count = 0;
            while(Variables.myLocation == null || count > 10) {
                try {
                    Thread.sleep(500);
                    count++;
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return null;
        }


        /**
         * After found calls the setUpMap() method
         * @param unused
         */
        protected  void onPostExecute(final Void unused) {
            if(this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if(Variables.myLocation != null) {
                Toast.makeText(getActivity().getApplicationContext(), "Location Found.", Toast.LENGTH_SHORT).show();
                Log.e("tour", "location found!!!!!!");
                setUpMap();
            }
        }
    }
    public LocationHelper.LocationResult locationResult = new LocationHelper.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            Variables.myLocation = location;
        }
    };

}
