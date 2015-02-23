package activities;

import java.util.ArrayList;
import java.util.List;

import edu.wcu.wcutour.R;
import utilities.Variables;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LocationsActivity extends BaseActivity {

    private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    LayoutInflater inflater = (LayoutInflater) this
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View contentView = inflater.inflate(R.layout.activity_locations, null, false);
	    mDrawerLayout.addView(contentView, 0);  
	    
        lv = (ListView) findViewById(R.id.list);

        List<String> locations = new ArrayList<String>();
        for(int i = 0; i < Variables.listOfWaypoints.size();i++) {
        	locations.add(Variables.listOfWaypoints.get(i).getDescription());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, 
                R.layout.list_item,
                locations );

        lv.setAdapter(arrayAdapter); 
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
        			int position, long id) {
        		
        		Intent i = new Intent(LocationsActivity.this, SelectedItem.class);
        		//
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
		getMenuInflater().inflate(R.menu.locations, menu);
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
	
/*	@Override
	public void onListItemClick(ListView l, View view, int position,
			long id) {
	
		for (int i= 0; i < 50;i++) {
			System.out.println("here");
		}
		
		Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
				Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, SelectedItem.class);
		i.putExtra("text", ((TextView) view).getText());
	
		this.startActivity(i);
	} */
}
