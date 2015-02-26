package activities;

import edu.wcu.wcutour.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author Caleb Gragg, Jeremiah Griffin
 * 
 *Sets up the main page of the app, the home screen with buttons and nav drawer.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
	//int campus_unpressed = R.drawable.campusmapunpressed;
	//int campus_pressed = R.drawable.campusmappressed;
	//int locations_unpressed = R.drawable.locationsunpressed;
	//int locations_pressed = R.drawable.locationspressed;
	//int tours_unpressed = R.drawable.toursunpressed;
	//int tours_unpressed = R.drawable.tourbuttonjeremiahunpressed;
	//int tours_pressed = R.drawable.tourspressed;
	/*
	int campus_unpressed = R.drawable.campusmapjgcampusmapjeremiahunpressed;
	int campus_pressed = R.drawable.campusmapjgcampusmapjeremiahpressed;
	int locations_unpressed = R.drawable.campusmapjglocationsjeremiahunpressed;
	int locations_pressed = R.drawable.campusmapjglocationsjeremiahpressed;
	int tours_unpressed = R.drawable.campusmapjgtoursjeremiahunpressed;
	int tours_pressed = R.drawable.campusmapjgtoursjeremiahpressed;
	*/
	Thread thread;

    /**
     * This method creates the activity.
     * @param savedInstanceState - Bundle object with state data.
     */
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

    /**
     * This method initializes and ties all the buttons on screen together.
     */
	public void setUpButtons() {
	    /*
	     *  Finding the buttons.
	     */
	    
		final Button campusMap = ( Button ) this.findViewById(R.id.campus_map_button_home);
		final Button locations = (Button ) this.findViewById(R.id.locations_button_home);
		final Button tours = ( Button ) this.findViewById(R.id.tours_button_home);
		
	    //campusMap.setBackgroundResource(campus_unpressed);
	    //locations.setBackgroundResource(locations_unpressed);
	    //tours.setBackgroundResource(tours_unpressed);
	    campusMap.setOnClickListener(this);
        locations.setOnClickListener(this);
        tours.setOnClickListener(this);
	    /*
	    //campusMap.setOnTouchListener(new OnTouchListener() {
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
        */
	}

    /**
     * This method handles when the buttons on the activity are pressed and what screens should
     * be shown after the press.
     * @param v - the button that was selected.
     */
    public void onClick(View v){

        Button b = (Button)findViewById(v.getId());
        Intent maps = new Intent(this,GoogleMapsActivity.class);
        Intent loc = new Intent(this,LocationsActivity.class);
        Intent tours = new Intent(this,TourActivity.class);

        if(b.getId() == R.id.campus_map_button_home)
            startActivity(maps);

        if(b.getId() == R.id.locations_button_home)
            startActivity(loc);

        if(b.getId() == R.id.tours_button_home)
            startActivity(tours);
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
	
	

}
