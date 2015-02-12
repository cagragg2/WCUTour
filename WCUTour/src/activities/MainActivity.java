package activities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import models.NavDrawerItem;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import utilities.NavDrawerListAdapter;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.wcu.wcutour.LocationsActivity;
import edu.wcu.wcutour.R;
import edu.wcu.wcutour.R.id;
import edu.wcu.wcutour.R.layout;
import edu.wcu.wcutour.R.menu;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Caleb Gragg, Jeremiah Griffin
 * 
 *Sets up the main page of the app, the home screen with buttons and nav drawer.
 */

public class MainActivity extends BaseActivity {
	//int campus_unpressed = R.drawable.campusmapunpressed;
	//int campus_pressed = R.drawable.campusmappressed;
	//int locations_unpressed = R.drawable.locationsunpressed;
	//int locations_pressed = R.drawable.locationspressed;
	//int tours_unpressed = R.drawable.toursunpressed;
	//int tours_unpressed = R.drawable.tourbuttonjeremiahunpressed;
	//int tours_pressed = R.drawable.tourspressed;
	
	int campus_unpressed = R.drawable.campusmapjgcampusmapjeremiahunpressed;
	int campus_pressed = R.drawable.campusmapjgcampusmapjeremiahpressed;
	int locations_unpressed = R.drawable.campusmapjglocationsjeremiahunpressed;
	int locations_pressed = R.drawable.campusmapjglocationsjeremiahpressed;
	int tours_unpressed = R.drawable.campusmapjgtoursjeremiahunpressed;
	int tours_pressed = R.drawable.campusmapjgtoursjeremiahpressed;
	
	Thread thread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    LayoutInflater inflater = (LayoutInflater) this
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View contentView = inflater.inflate(R.layout.home, null, false);
	    mDrawerLayout.addView(contentView, 0); 
	    
	    //to set up the custom buttons.
	    setUpButtons();
	}
	
	public void setUpButtons() {
	    /*
	     * Images to represent the buttons.
	     */
	    
		final ImageView campusMap = ( ImageView ) this.findViewById(R.id.campusmap);
		final ImageView locations = (ImageView ) this.findViewById(R.id.locationsview);
		final ImageView tours = ( ImageView ) this.findViewById(R.id.toursview);
		
	    campusMap.setBackgroundResource(campus_unpressed);
	    locations.setBackgroundResource(locations_unpressed);
	    tours.setBackgroundResource(tours_unpressed);
	    
	    
	    campusMap.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					campusMap.setBackgroundResource(campus_pressed);
					break;
				}
				case MotionEvent.ACTION_UP: {
					campusMap.setBackgroundResource(campus_unpressed);
					openMaps(v);
					break;
				}
				}
				return true;
			}
	    	
	    });
	    
	    locations.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					locations.setBackgroundResource(locations_pressed);
					break;
				}
				case MotionEvent.ACTION_UP: {
					locations.setBackgroundResource(locations_unpressed);
					openLocations(v);
					break;
				}
				}
				return true;
			}
	    	
	    });
	    
	    tours.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					tours.setBackgroundResource(tours_pressed);
					break;
				}
				case MotionEvent.ACTION_UP: {
					tours.setBackgroundResource(tours_unpressed);
					openTours(v);
					break;
				}
				}
				return true;
			}
	    	
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.achievements, menu);
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
	 * What happens when the buttons are clicked.
	 * 
	 */
	public void openMaps(View view) {
    	Intent activityMaps = new Intent(this, GoogleMapsActivity.class);
    	this.startActivity(activityMaps);
	}
	
	public void openTours(View view) {
        Intent tourActivity = new Intent(this, TourActivity.class);
        this.startActivity(tourActivity);
	}
	
	public void openLocations(View view) {
		Intent locationActivity = new Intent(this, LocationsActivity.class);
		this.startActivity(locationActivity);
	}
}
