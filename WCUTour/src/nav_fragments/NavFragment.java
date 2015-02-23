package nav_fragments;

import java.util.ArrayList;
import java.util.Calendar;

import models.Waypoint;
import navigation.NavigationActivity;
import navigation.ProximityReceiver;
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
import android.app.Fragment;
import android.app.PendingIntent;
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

public class NavFragment extends Fragment implements LocationListener, SensorEventListener {
	
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
	
	@Override
	public void onResume() {
		super.onResume();
		
		Log.e("tour", "turning on sensor");
		sensor = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		//mAccelerometer = sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerometer = sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensor.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	    try {
	        getActivity().unregisterReceiver(mybroadcast);
            sensor.unregisterListener(this);
	    } catch (IllegalArgumentException e) {
	       // Log.d("reciever", e.toString());
	    }
		Variables.locationManager.removeUpdates(this);
		Variables.locationManager.removeProximityAlert(Variables.pi);
		Log.e("Tour", "Turning off proximity alerts.");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup containter,
			Bundle savedInstanceState) {
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
	
	public void start() {
		 Variables.locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

		    try {
		    	initilizeMap();
		    	setUpMap();
		    	setUpTour();
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
		    
		    //locationView = (TextView) V.findViewById(R.id.tv1);
		   // distanceView = (TextView) V.findViewById(R.id.tv2);
		  
		    locationView.setText(Variables.selectedTour.getTour().get(0).getDescription() + "");
		    distanceView.setText("-999");
		
		    /*
		     * Progress bar
		     */
		
		    //mProgress = (ProgressBar) V.findViewById(R.id.progressBar1);
		    mProgress.setMax(100);
		    Variables.changeDistance = true;
		    Variables.updateInformation = false;

        Log.e("tour", "turning on sensor");
        sensor = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //mAccelerometer = sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerometer = sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensor.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	/*
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
	
	
	/*
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
				Toast.makeText(getActivity().getApplicationContext(), "Waiting to aquire Location.", Toast.LENGTH_SHORT).show();
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
                .target(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()))
                .tilt(65.5f).zoom(18f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));


	    setMarkers();
	}
	
	/*
	 * Sets up the markers for the waypoints in the selected tour.
	 */
	public void setMarkers(){
		
	    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    googleMap.setTrafficEnabled(true);
	    

	    
	    for(int i = 0; i < Variables.selectedTour.getTour().size(); i++){
	        googleMap.addMarker(new MarkerOptions().position(new LatLng(Variables.selectedTour.getTour().get(i).getLatitude(), 
	        							Variables.selectedTour.getTour().get(i).getLongitude())).title(Variables.selectedTour.getTour().get(i).getDescription()).snippet("WCU")
	        							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

	    }
		
	    myLocMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(0,
	    		0)));
	}
	
	
	/*
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
	/*
	 * Will use later for updating the UI on how far to the next location.
	 */
	
	public float checkDistance(Location loc1, Location loc2) {
		return loc1.distanceTo(loc2);
	}

	/*
	 * The onDestroy() method unregisters the receiver and stops tracking location.
	 */
	@Override
	public void onLocationChanged(Location location) {
    //	Toast.makeText(getApplicationContext(),"Location Changed", 
     //           Toast.LENGTH_SHORT).show();
		trackLocation(location);

		/*
		 * To fix the screen at the bottom below the map
		 */
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
			 Log.e("Tour", "vibrating");
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

			//myLocMarker.remove();
		   // myLocMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(googleMap.getMyLocation().getLatitude(),
		    //		googleMap.getMyLocation().getLongitude())));
		  //  myLocMarker.isFlat();
		   // myLocMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.arrow));
		   // Log.e("tour", "Moving");


		    
	}

    public void updateCamera(float bearing) {
        CameraPosition currentPlace = new CameraPosition.Builder()
                .target(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()))
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
    LatLng soccer  = new LatLng(35.310455, -83.185925);
    points.add(soccer);
    LatLng a2 = new LatLng(35.310334, -83.185852);
    points.add(a2);
    LatLng a3 = new LatLng(35.310131, -83.186173);
    points.add(a3);
    LatLng a4 = new LatLng(35.310046, -83.186284);
    points.add(a4);
    LatLng a5 = new LatLng(35.309928, -83.186378);
    points.add(a5);
    LatLng a6 = new LatLng(35.309662, -83.186505);
    points.add(a6);
    LatLng a7 = new LatLng(35.309314, -83.186672);
    points.add(a7);
    LatLng a8 = new LatLng(35.309218, -83.186694);
    points.add(a8);
    LatLng a9 = new LatLng(35.309137, -83.186688);
    points.add(a9);
    LatLng a10 = new LatLng(35.309074, -83.186721);
    points.add(a10);
    LatLng a11 = new LatLng(35.309020, -83.186810);
    points.add(a11);
    LatLng a12 = new LatLng(35.308983, -83.186866);
    points.add(a12);
    LatLng a13 = new LatLng(35.308699, -83.186926);
    points.add(a13);
    LatLng a14 = new LatLng(35.308591, -83.186926);
    points.add(a14);
    LatLng a15 = new LatLng(35.308450, -83.186888);
    points.add(a15);
    LatLng a16 = new LatLng(35.308337, -83.186806);
    points.add(a16);
    LatLng a17 = new LatLng(35.308238, -83.186667);
    points.add(a17);
    LatLng a18 = new LatLng(35.308153, -83.186522);
    points.add(a18);
    LatLng a19 = new LatLng(35.308076, -83.186384);
    points.add(a19);
    LatLng a20 = new LatLng(35.310403, -83.185947);
    points.add(a20);
    LatLng a21 = new LatLng(35.307954, -83.186174);
    points.add(a21);
    LatLng a22 = new LatLng(35.307854, -83.186019);
    points.add(a22);
    LatLng a23 = new LatLng(35.307786, -83.185876);
    points.add(a23);
    LatLng a24 = new LatLng(35.307750, -83.185870);
    points.add(a24);
    LatLng a25 = new LatLng(35.307258, -83.185200);
    points.add(a25);
    LatLng a26 = new LatLng(35.307050, -83.184934);
    points.add(a26);
    LatLng a27 = new LatLng(35.306783, -83.184674);
    points.add(a27);
    LatLng a28 = new LatLng(35.306642, -83.184529);
    points.add(a28);
    LatLng a29 = new LatLng(35.306461, -83.184341);
    points.add(a29);
    LatLng a30 = new LatLng(35.306385, -83.184202);
    points.add(a30);
    LatLng a31 = new LatLng(35.306313, -83.184036);
    points.add(a31);
    LatLng a32 = new LatLng(35.306280, -83.183814);
    points.add(a32);
    LatLng a33 = new LatLng(35.306280, -83.183670);
    points.add(a33);
    LatLng a34 = new LatLng(35.306302, -83.183515);
    points.add(a34);
    LatLng a35 = new LatLng(35.306429, -83.182984);
    points.add(a35);
    LatLng a36 = new LatLng(35.306514, -83.182819);
    points.add(a36);
    LatLng a37 = new LatLng(35.306582, -83.182709);
    points.add(a37);
    LatLng a38 = new LatLng(35.306468, -83.182544);
    points.add(a38);
    LatLng a39 = new LatLng(35.306441, -83.182500);
    points.add(a39);
    LatLng a40 = new LatLng(35.306333, -83.182428);
    points.add(a40);
    LatLng a41 = new LatLng(35.306113, -83.182357);
    points.add(a41);
    LatLng a42 = new LatLng(35.305933, -83.182313);
    points.add(a42);
    LatLng a43 = new LatLng(35.305865, -83.182329);
    points.add(a43);
    LatLng a44 = new LatLng(35.305829, -83.182357);
    points.add(a44);
    LatLng a45 = new LatLng(35.305784, -83.182235);
    points.add(a45);
    LatLng a46 = new LatLng(35.305749, -83.182063);
    points.add(a46);
    LatLng a47 = new LatLng(35.305684, -83.181862);
    points.add(a47);
    LatLng a48 = new LatLng(35.305491, -83.181772);
    points.add(a48);
    LatLng football = new LatLng(35.305456, -83.181759);
    points.add(football);
    LatLng a50 = new LatLng(35.305467, -83.181656);
    points.add(a50);
    LatLng a51 = new LatLng(35.304596, -83.181383);
    points.add(a51);
    LatLng a52 = new LatLng(35.304455, -83.181351);
    points.add(a52);
    LatLng a53 = new LatLng(35.304263, -83.181340);
    points.add(a53);
    LatLng a54 = new LatLng(35.304022, -83.181356);
    points.add(a54);
    LatLng a55 = new LatLng(35.303793, -83.181401);
    points.add(a55);
    LatLng a56 = new LatLng(35.303707, -83.181430);
    points.add(a56);
    LatLng a57 = new LatLng(35.303480, -83.181526);
    points.add(a57);
    LatLng a58 = new LatLng(35.303281, -83.181646);
    points.add(a58);
    LatLng a59 = new LatLng(35.303087, -83.181818);
    points.add(a59);
    LatLng ramsey = new LatLng(35.302555, -83.182347);
    points.add(ramsey);
    LatLng a61 = new LatLng(35.302469, -83.182162);
    points.add(a61);
    LatLng a62 = new LatLng(35.302426, -83.182100);
    points.add(a62);
    LatLng a63 = new LatLng(35.302198, -83.181941);
    points.add(a63);
    LatLng a64 = new LatLng(35.302219, -83.181748);
    points.add(a64);
    LatLng a65 = new LatLng(35.302104, -83.181588);
    points.add(a65);
    LatLng baseball = new LatLng(35.301609, -83.181002);
    points.add(baseball);

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
            .snippet("This is the last location, thanks for taking the tour!")
            .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    line = googleMap.addPolyline(pOptions);

}
public void setUpTour() {
		
		//LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
		
		
		PolylineOptions pOptions = new PolylineOptions().width(15).color(Color.rgb(128,0,128)) //85,30,97
				.geodesic(true);
		//Arraylist to hold all coordinates on tour. 
		ArrayList<LatLng> hunterToCrc = new ArrayList<LatLng>();
		//Building Tour
		LatLng hunterLibrary = new LatLng(35.312966, -83.179877);
		hunterToCrc.add(hunterLibrary);
		LatLng aaa = new LatLng(35.312967, -83.179898);
		hunterToCrc.add(aaa);
		LatLng bbb = new LatLng(35.312955, -83.179924);
		hunterToCrc.add(bbb);
		LatLng ccc = new LatLng(35.312861, -83.179821);
		hunterToCrc.add(ccc);
		LatLng ddd = new LatLng(35.312829, -83.179854);
		hunterToCrc.add(ddd);
		
		LatLng a = new LatLng(35.312804, -83.179894);
		hunterToCrc.add(a);
		LatLng b = new LatLng(35.312722, -83.179983);
		hunterToCrc.add(b);
		LatLng c = new LatLng(35.312693, -83.180012);
		hunterToCrc.add(c);
		LatLng d = new LatLng(35.312686, -83.180031);
		hunterToCrc.add(d);
		LatLng e = new LatLng(35.312668, -83.180060);
		hunterToCrc.add(e);
		LatLng f = new LatLng(35.312653, -83.180082);
		hunterToCrc.add(f);
		LatLng g = new LatLng(35.312493, -83.180285);
		hunterToCrc.add(g);
		LatLng h = new LatLng(35.312467, -83.180417);
		hunterToCrc.add(h);
		LatLng i = new LatLng(35.312451, -83.180442);
		hunterToCrc.add(i);
		LatLng j = new LatLng(35.312399, -83.180517);
		hunterToCrc.add(j);
		LatLng k = new LatLng(35.312308, -83.180618);
		hunterToCrc.add(k);
		LatLng l = new LatLng(35.312278, -83.180678);
		hunterToCrc.add(l);
		LatLng m = new LatLng(35.312244, -83.180706);
		hunterToCrc.add(m);
		LatLng n = new LatLng(35.312042, -83.180972);
		hunterToCrc.add(n);
		LatLng o = new LatLng(35.312019, -83.181032);
		hunterToCrc.add(o);
		LatLng p = new LatLng(35.312004, -83.181142);
		hunterToCrc.add(p);
		LatLng q = new LatLng(35.311991, -83.181198);
		hunterToCrc.add(q);
		LatLng r = new LatLng(35.311846, -83.181161);
		hunterToCrc.add(r);
		LatLng s = new LatLng(35.311680, -83.181116);
		hunterToCrc.add(s);
		LatLng t = new LatLng(35.311655, -83.181218);
		hunterToCrc.add(t);
		LatLng u = new LatLng(35.311644, -83.181266);
		hunterToCrc.add(u);
		LatLng v = new LatLng(35.311624, -83.181347);
		hunterToCrc.add(v);
		LatLng A = new LatLng(35.311589, -83.181381);
		hunterToCrc.add(A);
		LatLng B = new LatLng(35.311496, -83.181435);
		hunterToCrc.add(B);
		LatLng C = new LatLng(35.311139, -83.181627);
		hunterToCrc.add(C);
		LatLng D = new LatLng(35.311128, -83.181631);
		hunterToCrc.add(D);
		LatLng E = new LatLng(35.311049, -83.181629);
		hunterToCrc.add(E);
		LatLng F = new LatLng(35.311012, -83.181644);
		hunterToCrc.add(F);
		LatLng G = new LatLng(35.310985, -83.181684);
		hunterToCrc.add(G);
		LatLng H = new LatLng(35.310948, -83.181708);
		hunterToCrc.add(H);
		LatLng I = new LatLng(35.310891, -83.181713);
		hunterToCrc.add(I);
		LatLng J = new LatLng(35.310823, -83.181719);
		hunterToCrc.add(J);
		LatLng K = new LatLng(35.310770, -83.181722);
		hunterToCrc.add(K);
		LatLng L = new LatLng(35.310747, -83.181844);
		hunterToCrc.add(L);
		LatLng M = new LatLng(35.310676, -83.182253);
		hunterToCrc.add(M);
		LatLng N = new LatLng(35.310615, -83.182319);
		hunterToCrc.add(N);
		LatLng O = new LatLng(35.310561, -83.182363);
		hunterToCrc.add(O);
		LatLng P = new LatLng(35.310554, -83.182394);
		hunterToCrc.add(P);
		LatLng Q = new LatLng(35.310521, -83.182678);
		hunterToCrc.add(Q);
		LatLng R = new LatLng(35.310413, -83.182663);
		hunterToCrc.add(R);
		LatLng S = new LatLng(35.310353, -83.182650);
		hunterToCrc.add(S);
		LatLng T = new LatLng(35.310104, -83.182607);
		hunterToCrc.add(T);
		LatLng U = new LatLng(35.309928, -83.182782);
		hunterToCrc.add(U);
		LatLng V = new LatLng(35.309857, -83.183046);
		hunterToCrc.add(V);
		LatLng W = new LatLng(35.309486, -83.183247);
		LatLng Courtyard = new LatLng(35.309474, -83.183245);
		hunterToCrc.add(W);
		LatLng X = new LatLng(35.309099, -83.183206);
		hunterToCrc.add(X);
		LatLng Y = new LatLng(35.308619, -83.183150);
		hunterToCrc.add(Y);
		LatLng Z = new LatLng(35.308440, -83.183121);
		hunterToCrc.add(Z);
		LatLng aa = new LatLng(35.308000, -83.183077);
		hunterToCrc.add(aa);
		LatLng bb = new LatLng(35.307838, -83.183053);
		hunterToCrc.add(bb);
		LatLng cc = new LatLng(35.307702, -83.183013);
		hunterToCrc.add(cc);
		LatLng dd = new LatLng(35.307542, -83.183023);
		hunterToCrc.add(dd);
		LatLng ee = new LatLng(35.307356, -83.183029);
		hunterToCrc.add(ee);
		LatLng ff = new LatLng(35.307016, -83.182955);
		hunterToCrc.add(ff);
		LatLng gg = new LatLng(35.306921, -83.182966);
		hunterToCrc.add(gg);
		LatLng hh = new LatLng(35.306633, -83.183033);
		hunterToCrc.add(hh);
		LatLng ii = new LatLng(35.306468, -83.182944);
		hunterToCrc.add(ii);
		LatLng jj = new LatLng(35.306514, -83.182846);
		hunterToCrc.add(jj);
		LatLng kk = new LatLng(35.306580, -83.182713);
		hunterToCrc.add(kk);
		LatLng ll = new LatLng(35.306477, -83.182571);
		hunterToCrc.add(ll);
		LatLng mm = new LatLng(35.306461, -83.182530);
		hunterToCrc.add(mm);
		LatLng nn = new LatLng(35.306349, -83.182445);
		hunterToCrc.add(nn);
		LatLng oo = new LatLng(35.305991, -83.182320);
		hunterToCrc.add(oo);
		LatLng pp = new LatLng(35.305916, -83.182308);
		hunterToCrc.add(pp);
		LatLng qq = new LatLng(35.305882, -83.182312);
		hunterToCrc.add(qq);
		LatLng rr = new LatLng(35.305826, -83.182362);
		hunterToCrc.add(rr);
		LatLng ss = new LatLng(35.305789, -83.182423);
		hunterToCrc.add(ss);
		LatLng tt = new LatLng(35.305697, -83.182389);
		hunterToCrc.add(tt);
		LatLng uu = new LatLng(35.305741, -83.182305);
		hunterToCrc.add(uu);
		LatLng vv = new LatLng(35.305789, -83.182244);
		hunterToCrc.add(vv);
		LatLng ww = new LatLng(35.305726, -83.182019);
		hunterToCrc.add(ww);
		LatLng xx = new LatLng(35.305624, -83.181850);
		hunterToCrc.add(xx);
		LatLng yy = new LatLng(35.305545, -83.181801);
		hunterToCrc.add(yy);
		LatLng zz = new LatLng(35.305462, -83.181753);
		hunterToCrc.add(zz);
		LatLng a1 = new LatLng(35.305428, -83.181740);
		hunterToCrc.add(a1);
		
		
		
		MarkerOptions options = new MarkerOptions();
		for(int z = 0; z < hunterToCrc.size(); z++) {
			LatLng point = hunterToCrc.get(z);
			pOptions.add(point);
			
		}
		//LatLng coord = new LatLng(35.312804, -83.179894);
		//CameraUpdate center= CameraUpdateFactory.newLatLng(coord);
		//CameraUpdate zoom=CameraUpdateFactory.zoomTo(17);
		//googleMap.moveCamera(center);
		//googleMap.animateCamera(zoom);
		
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

@Override
public void onSensorChanged(SensorEvent event) {
	float degree = Math.round(event.values[0]);

		Log.e("tour",degree + "");
if(myLocMarker != null) {
	//myLocMarker.setRotation(degree *20);
            updateCamera(event.values[0]);}
	Log.e("tour", "rotating");
	
}

@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
	Log.e("tour", "turnng");
	
}
}
