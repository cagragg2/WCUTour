package activities;

import utilities.Variables;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.wcu.wcutour.R;
import edu.wcu.wcutour.R.id;
import edu.wcu.wcutour.R.layout;
import edu.wcu.wcutour.R.menu;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * @author Caleb Gragg, Jeremiah Griffin
 * 
 * Creates the campus map when the campus map button is selected from the home screen.
 * Also places all the markers for each of the waypoints.
 *
 */

public class GoogleMapsActivity extends BaseActivity {
	
	//Google Map Object
	
	private GoogleMap googleMap;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.fragment_map);
	    LayoutInflater inflater = (LayoutInflater) this
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View contentView = inflater.inflate(R.layout.fragment_map, null, false);
	    mDrawerLayout.addView(contentView, 0); 
		
		try {
			//load map
			initilizeMap();
			setUpMap();
		} catch(Exception e){
			e.printStackTrace();
		} //end catch
	}//end onCreate
	
	
	/*
	 * Checks if the map is null and if it is sets up a new map.
	 * 
	 */
	private void initilizeMap() {
		if(googleMap == null) {
			googleMap = ((MapFragment) 
					getFragmentManager().findFragmentById(R.id.map)).getMap();
			//check if map is created successully or not
			if(googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! Unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 
	
	
	/*
	 * Private method to set up the map to be presented.
	 */
	private void setUpMap() {
		
		// latitude and longitude
		double latitude = 35.309200;
		double longitude = -83.186941;
		
		//Specify WGS 84 coordinates for WCU
		LatLng location = new LatLng(35.309164, -83.185412);
		//Pass them to a new CameraUpdateObject
		CameraUpdate center= CameraUpdateFactory.newLatLng( location ); 
		//Specify a zoom level
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		//Position and zoom camera;
		googleMap.moveCamera(center); 
		googleMap.animateCamera(zoom);
		googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    googleMap.setMyLocationEnabled(true);
		
		setMarkers();

	}
	/**
	 * Sets up the markers for all of the waypoints.
	 * 
	 */
	
	public void setMarkers(){
		
	    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    googleMap.setTrafficEnabled(true);
	    
//Loops over and places all of the markers.
	    for(int i = 0; i < Variables.listOfWaypoints.size(); i++){
	        googleMap.addMarker(new MarkerOptions().position(new LatLng(Variables.listOfWaypoints.get(i).getLatitude(), 
	        							Variables.listOfWaypoints.get(i).getLongitude())).title(Variables.listOfWaypoints.get(i).getDescription()).snippet("WCU")
	        							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
	    }
	}
	

}

