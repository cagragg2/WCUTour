package activities;

import nav_fragments.NavFragment;
import nav_fragments.TourInfoFragment;
import nav_fragments.WaypointsInTourFragment;
import edu.wcu.wcutour.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


public class TourNavigationActivity extends BaseActivity implements ActionBar.TabListener {

	Fragment NavFragment = new NavFragment();
	Fragment TourInfoFragment = new TourInfoFragment();
	Fragment WaypointsInTourFragment = new WaypointsInTourFragment();
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_tour_navigation);
	    LayoutInflater inflater = (LayoutInflater) this
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View contentView = inflater.inflate(R.layout.activity_tour_navigation, null, false);
	    mDrawerLayout.addView(contentView, 0); 
        
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	getActionBar().setDisplayUseLogoEnabled(false);
    	getActionBar().setDisplayShowTitleEnabled(true);
    	getActionBar().setDisplayShowHomeEnabled(true);
    	getActionBar().setDisplayHomeAsUpEnabled(true);
    	
    	ActionBar.Tab newTab0 = getActionBar().newTab();
    	newTab0.setText("Navigation");
    	ActionBar.Tab newTab1 = getActionBar().newTab();
    	newTab1.setText("Tour Information");
    	ActionBar.Tab newTab2 = getActionBar().newTab();
    	newTab2.setText("Waypoints in tour");
    	
    	//newTab0.setTabListener(this);
    	//newTab1.setTabListener(this);
    	//newTab2.setTabListener(this);
    	
    	newTab0.setTabListener(new TabListener(NavFragment));
    	newTab1.setTabListener(new TabListener(TourInfoFragment));
    	newTab2.setTabListener(new TabListener(WaypointsInTourFragment));
    	
    	getActionBar().addTab(newTab0);
    	getActionBar().addTab(newTab1);
    	getActionBar().addTab(newTab2);
    	}
	
	
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	
		Fragment newFragment;
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch(tab.getPosition()) {
			case 0:
				toastText("Opening NAvigation");
				newFragment = new NavFragment();
				//transaction.replace(R.id.container, newFragment);
				transaction.replace(R.id.fragment_container1,newFragment);
				//transaction.addToBackStack(null);
				transaction.commit();
				break;
				
			case 1:
				toastText("tab " + String.valueOf(tab.getPosition()) + " clicked");
				newFragment = new TourInfoFragment();
				transaction.replace(R.id.fragment_container1, newFragment);
				//transaction.addToBackStack(null);
				transaction.commit();
				break;
				
			case 2:
				toastText("tab " + String.valueOf(tab.getPosition()) + " clicked");
				newFragment = new WaypointsInTourFragment();
				transaction.replace(R.id.fragment_container1, newFragment);
				//transaction.addToBackStack(null);
				transaction.commit();
				break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	private void toastText(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}
	
	