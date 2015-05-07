package activities;

import java.util.ArrayList;
import java.util.List;

import utilities.Variables;
import edu.wcu.wcutour.R;
import edu.wcu.wcutour.R.id;
import edu.wcu.wcutour.R.layout;
import edu.wcu.wcutour.R.menu;
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

public class TourActivity extends BaseActivity {
	
	private ListView lv;
	
	/** 
	 *  @author Caleb Gragg, Jeremiah Griffin
	 * 
	 * Sets up a list of tours that are avaliable.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_tour);
	    LayoutInflater inflater = (LayoutInflater) this
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View contentView = inflater.inflate(R.layout.activity_tour, null, false);
	    mDrawerLayout.addView(contentView, 0); 
	    
	    lv = (ListView) findViewById(R.id.tour_list);

        // sets up an array list of tour names
        List<String> locations = new ArrayList<String>();
        for(int i = 0; i < Variables.listOfTours.size();i++) {
        	locations.add(Variables.listOfTours.get(i).getTourName());
        }
        //adds all the tours to the list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, 
                R.layout.list_item,
                locations );

        lv.setAdapter(arrayAdapter); 
        //handles the click on the tour.
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
        			int position, long id) {
        		//took this out to compensate for the new TourNavigationActivity
        		Intent i = new Intent(TourActivity.this, SelectedTourActivity.class);
        		
        		Variables.selectedTour = Variables.listOfTours.get(parent.getPositionForView(view));
                Log.e("SELECTED TOUR",Variables.selectedTour.getTourName());
        		i.putExtra(Variables.selectedItem + "", ((TextView) view).getText());
        		
        		startActivity(i);
        	}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tour, menu);
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
