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
import edu.wcu.wcutour.R.drawable;
import edu.wcu.wcutour.R.id;
import edu.wcu.wcutour.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 
 * @author Caleb Gragg, Jeremiah Griffin
 *
 * The information screen for an item that is selected from the list of waypoints or 
 * from the waypoints in the selected tour.
 */

public class SelectedItem extends BaseActivity implements View.OnClickListener{
	
	public GoogleMap googleMap; // google map object
	ListView list;

	//http://android-er.blogspot.com/2012/07/implement-gallery-like.html
	LinearLayout myGallery;
	
	//int[] images = {R.drawable.alumni_tower,R.drawable.alumni_tower2, R.drawable.alumni_tower3 };
	ArrayList<Integer> imageIds = new ArrayList<>();
	

	//------------------------------------------------------------------------------------------------------------
	/**
	 * onCreate method for the selected item.
	 */
	//------------------------------------------------------------------------------------------------------------
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
	    LayoutInflater inflater = (LayoutInflater) this
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View contentView = inflater.inflate(R.layout.activity_selected_item, null, false);
	    mDrawerLayout.addView(contentView, 0); 

	    map(); //sets up the map
	    setImages(); // sets up the images to be used
        Bundle extras = getIntent().getExtras();


	//sets up the information regarding the waypoint.
		if(extras != null)
		{
		
			TextView tv1 = (TextView) this.findViewById(R.id.textView1);
			TextView tv2 = (TextView) this.findViewById(R.id.textView2);
			TextView tv3 = (TextView) this.findViewById(R.id.textView3);
			TextView tv4 = (TextView) this.findViewById(R.id.textView4);


            //waypoint name
			tv1.setText("" + Variables.selectedItem);
			tv2.setText("Description:" + "\n");
            //when clicked takes the user to the more information screen
            tv3.setText("Click here for more information.\n");
            tv3.setOnClickListener(this);

			tv4.setText("Photos:");
		}
		Button button = new Button(this);
		button.setText("Nav");
		addContentView(button, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

        //sets up the galler
		myGallery = (LinearLayout)findViewById(R.id.mygallery);

        //loops over all the images and adds them to the gallery
		for(int i = 0; i<imageIds.size();i++) {
			ImageView iv = new ImageView (this);
            iv.setImageResource(imageIds.get(i));
			myGallery.addView(iv);
		}

	}

    /**
     * Starts the more information screen when the "click here for more information"
     * is clicked
     * @param v view that was clicked
     */

    @Override
    public void onClick(View v) {
        Intent i = new Intent(SelectedItem.this,WaypointDescription.class);
        startActivity(i);
    }
	
	//------------------------------------------------------------------------------------------------------------
	/**
	 * Method that calls the other methods relavent to setting up the map.
	 */
	//------------------------------------------------------------------------------------------------------------
	
	public void map() {
		start();
		setUpOneWaypoint();
	}
	//------------------------------------------------------------------------------------------------------------
	/**
	 * Trys to call the initialize map method.
	 */
	//------------------------------------------------------------------------------------------------------------
	public void start() {
		try {
			initializeMap();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------------------------------------------------------------------
	/**
	 * Initializes the map if the map is null.
	 */
	//------------------------------------------------------------------------------------------------------------
	
	public void initializeMap() {
		if(googleMap == null) {
			googleMap = ((MapFragment)
					getFragmentManager().findFragmentById(R.id.map)).getMap();
			if(googleMap == null) {
				Toast.makeText(getApplicationContext(), 
						"Sorry! Unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	//------------------------------------------------------------------------------------------------------------
	/**
	 * Zooms over the waypoint.
	 */
	//------------------------------------------------------------------------------------------------------------
	
	private void setUpOneWaypoint() {
		double latitude = Variables.selectedWaypoint.getLatitude();
		double longitude = Variables.selectedWaypoint.getLongitude();
		
		LatLng location = new LatLng(latitude,longitude);
		CameraUpdate center = CameraUpdateFactory.newLatLng(location);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
		
		
		googleMap.moveCamera(center);
		googleMap.animateCamera(zoom);
		googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		
		
		setMarkers();
	}
	
	//------------------------------------------------------------------------------------------------------------
	/**
	 * Sets the marker for the single waypoint.  
	 */
	//------------------------------------------------------------------------------------------------------------
	
	public void setMarkers() {
		googleMap.addMarker(new MarkerOptions().position(new LatLng(Variables.selectedWaypoint.getLatitude(), Variables.selectedWaypoint.getLongitude())).title(Variables.selectedWaypoint.getDescription()).snippet("WCU")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
	}

    //-------------------------------------------------------------------------
    /**
     * Sets up the image ids for the pictures in the waypoint screen.
     */
    //-------------------------------------------------------------------------
    public void setImages() {
        int counter = 0;
        boolean check = true;
        String name = "";
        name = Variables.selectedItem.toLowerCase();
        name = name.replaceAll("\\s+", "");
        name = name.replaceAll("\\\\","");
        //gets all images with the name followed by _ and a number
        while(check) {
             imageIds.add(counter,getResources().getIdentifier(name + "_" + counter,
                    "drawable", getPackageName()));
            if(imageIds.get(counter) == 0) {
                check = false;
                imageIds.remove(counter);
            }
            counter++;
        }
    }
}

