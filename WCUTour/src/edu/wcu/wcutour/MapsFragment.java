package edu.wcu.wcutour;

import utilities.Variables;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import activities.MainActivity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public  class MapsFragment extends Fragment {

private static View view;
/**
 * Note that this may be null if the Google Play services APK is not
 * available.
 */

private static GoogleMap mMap;
private static Double latitude, longitude;




public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    if (container == null) {
        return null;
    }
    view =  inflater.inflate(R.layout.fragment_map, container, false);
            latitude = 35.309200;
            longitude = -83.186941;
            
            setUpMapIfNeeded(); // For setting up the MapFragment

    return view;
}



/***** Sets up the map if it is possible to do so *****/
public static void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (mMap == null) {
        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) MainActivity.fragmentManager
                .findFragmentById(R.id.map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // Check if we were successful in obtaining the map.
        if (mMap != null)
            setUpMap();
        
        
    }
}

/**
 * This is where we can add markers or lines, add listeners or move the
 * camera.
 * <p>
 * This should only be called once and when we are sure that {@link #mMap}
 * is not null.
 */
private static void setUpMap() {
	
    // For showing a move to my loction button
    mMap.setMyLocationEnabled(true);
    // For dropping a marker at a point on the Map
   // mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(Variables.listOfWaypoints.get(0).getDescription()).snippet("WCU"));
  //  mMap.addMarker(new MarkerOptions()
    								//.position(new LatLng(latitude, longitude))
    								//.title("WCU")
    								//.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
    // For zooming automatically to the Dropped PIN Location
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
            longitude), 15.0f));
    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    mMap.setTrafficEnabled(true);
    
    for(int i = 0; i < Variables.listOfWaypoints.size(); i++){
        mMap.addMarker(new MarkerOptions().position(new LatLng(Variables.listOfWaypoints.get(i).getLatitude(), 
        							Variables.listOfWaypoints.get(i).getLongitude())).title(Variables.listOfWaypoints.get(i).getDescription()).snippet("WCU")
        							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
    }
}

@Override
public void onViewCreated(View view, Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    if (mMap != null)
        setUpMap();

    if (mMap == null) {
        // Try to obtain the map from the SupportMapFragment.
        mMap = ((SupportMapFragment) MainActivity.fragmentManager
                .findFragmentById(R.id.map)).getMap();
        
        // Check if we were successful in obtaining the map.
        if (mMap != null)
            setUpMap();
    }
    
}
@Override
public void onPause(){
	super.onPause();
	/*
	Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
	FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
	ft.remove(fragment);
	ft.commit();
	*/
}

/**** The mapfragment's id must be removed from the FragmentManager
 **** or else if the same it is passed on the next time then 
 **** app will crash ****/

@Override
public void onDestroyView() 
{
   super.onDestroyView();
   
  // Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
   //FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
   //ft.remove(fragment);
   //ft.commit();
   /*
   if (mMap != null) {
		MainActivity.fragmentManager.beginTransaction()
       .remove(MainActivity.fragmentManager.findFragmentById(R.id.map)).commit();
		mMap = null;
	}
	*/
}

public void setMarkers(){
	
    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    mMap.setTrafficEnabled(true);
    
    for(int i = 0; i < Variables.listOfWaypoints.size(); i++){
        mMap.addMarker(new MarkerOptions().position(new LatLng(Variables.listOfWaypoints.get(i).getLatitude(), 
        							Variables.listOfWaypoints.get(i).getLongitude())).title(Variables.listOfWaypoints.get(i).getDescription()).snippet("WCU")
        							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
    }
	
}





}