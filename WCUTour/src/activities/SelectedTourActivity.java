package activities;

import java.util.ArrayList;
import java.util.List;

import utilities.Variables;
import edu.wcu.wcutour.R;
import edu.wcu.wcutour.R.id;
import edu.wcu.wcutour.R.layout;
import edu.wcu.wcutour.R.menu;
import models.Tours;
import models.Waypoint;
import navigation.NavigationActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author Caleb Gragg, Jeremiah Griffin
 *
 * When a user selects a tour from the list of tours.
 */

public class SelectedTourActivity extends BaseActivity {

    //list view to use for the list of waypoints
	private ListView lv;

	/*
	 * Sets up the Tour header and the list of waypoints in the selected tour.
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    LayoutInflater inflater = (LayoutInflater) this
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View contentView = inflater.inflate(R.layout.activity_selected_tour, null, false);
	    mDrawerLayout.addView(contentView, 0); 
	    
        lv = (ListView) findViewById(R.id.list2); //sets up the waypoints in the tour.
        
        //Set up the header for the xml page
        TextView tourHeader = (TextView) this.findViewById(R.id.selected_tour_header);
        
        //Log.e("tour", Variables.selectedTour.getTourName()+ "");

        tourHeader.setText("" + Variables.selectedTour.getTourName()); //sets the title of the tour
        List<Waypoint> waypoints = Variables.selectedTour.getTour(); //gets all the waypoints from the tour. 
        
        
        List<String> locations = new ArrayList<String>();
        for(int i =0; i < waypoints.size();i++) {
        locations.add(waypoints.get(i).getDescription()); //adds the waypoints to be put in the list
        } 
        //adds the list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, 
                R.layout.list_item,
                locations );

        lv.setAdapter(arrayAdapter); 
        //handles clicks redirects to the waypoints information screen.
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
        			int position, long id) {
        		
        		Intent i = new Intent(SelectedTourActivity.this, SelectedItem.class);
        		
        		Variables.selectedWaypoint = Variables.listOfWaypoints.get(parent.getPositionForView(view));
        		Variables.selectedItem = Variables.selectedWaypoint.getDescription();
        		
        		i.putExtra(Variables.selectedItem + "", ((TextView) view).getText());
        		
        		startActivity(i);
        	}
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selected_tour, menu);
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
	/**
	 * The navigate button at the top of the screen starts the Navigation Activity.
	 */
	public void navigate(View view) {
		Intent activityNavigation = new Intent(this, TourNavigationActivity.class);
		this.startActivity(activityNavigation);
	}
}
