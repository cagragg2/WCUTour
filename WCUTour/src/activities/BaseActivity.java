package activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import models.NavDrawerItem;
import models.Tours;
import models.Waypoint;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import utilities.NavDrawerListAdapter;
import utilities.ReadXMLFile;
import utilities.Variables;
import edu.wcu.wcutour.R;
import edu.wcu.wcutour.R.array;
import edu.wcu.wcutour.R.drawable;
import edu.wcu.wcutour.R.id;
import edu.wcu.wcutour.R.layout;
import edu.wcu.wcutour.R.menu;
import edu.wcu.wcutour.R.string;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * @author Caleb Gragg, Jeremiah Griffin
 * 
 * This is the base activity which every activity extends for the navigation drawer.
 * Creates the nav drawer and does our xml parsing.
 *
 */
public class BaseActivity extends FragmentActivity {

	
	 protected DrawerLayout mDrawerLayout; // nav drawer layout
	    private ListView mDrawerList;  //nav drawer list
	    private ActionBarDrawerToggle mDrawerToggle;
	    // nav drawer title
	    private CharSequence mDrawerTitle;
	    // used to store app title
	    private CharSequence mTitle;
	 
	    // slide menu items
	    private String[] navMenuTitles;
	    private TypedArray navMenuIcons;
	 
	    private ArrayList<NavDrawerItem> navDrawerItems; //items in the drawer
	    private NavDrawerListAdapter adapter;
	    
	    public static FragmentManager fragmentManager;



		//------------------------------------------------------------------------------------------------------------
		/*
		 * onCreate method for the base activity.
		 */
		//------------------------------------------------------------------------------------------------------------
	    
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			fragmentManager = getSupportFragmentManager();
			
			mTitle = mDrawerTitle = getTitle();
			 
	        // load slide menu items
	        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
	 
	        // nav drawer icons from resources
	        navMenuIcons = getResources()
	                .obtainTypedArray(R.array.nav_drawer_icons);
	 
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
	 
	        navDrawerItems = new ArrayList<NavDrawerItem>();
	 
	        // adding nav drawer items to array
	        // Home
	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
	        // Find People
	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
	        // Photos
	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
	        // Communities, Will add a counter here
	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
	        // Pages
	        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
	      //  navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
	        
	 
	        // Recycle the typed array
	        navMenuIcons.recycle();
	        
	        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
	 
	        // setting the nav drawer list adapter
	        adapter = new NavDrawerListAdapter(getApplicationContext(),
	                navDrawerItems);
	        mDrawerList.setAdapter(adapter);
	 
	        // enabling action bar app icon and behaving it as toggle button
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setHomeButtonEnabled(true);
	 
	        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
	                R.drawable.ic_drawer, //nav menu toggle icon
	                R.string.app_name, // nav drawer open - description for accessibility
	                R.string.app_name // nav drawer close - description for accessibility
	        ){
	            public void onDrawerClosed(View view) {
	                getActionBar().setTitle(mTitle);
	                // calling onPrepareOptionsMenu() to show action bar icons
	                invalidateOptionsMenu();
	            }
	 
	            public void onDrawerOpened(View drawerView) {
	                getActionBar().setTitle(mDrawerTitle);
	                // calling onPrepareOptionsMenu() to hide action bar icons
	                invalidateOptionsMenu();
	            }
	        };
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	        parser();
		}//end onCreate
		
		
		@Override
		protected void onResume() {
			
			super.onResume();
			
		//	initilizeMap();
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.maps, menu);
			return true;
		}

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // toggle nav drawer on selecting action bar app icon/title
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	        // Handle action bar actions click
	        switch (item.getItemId()) {
	        case R.id.action_settings:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
		
		
	    /***
	     * Called when invalidateOptionsMenu() is triggered
	     */
	    @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        // if nav drawer is opened, hide the action items
	        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
	        return super.onPrepareOptionsMenu(menu);
	    }
	 
	    @Override
	    public void setTitle(CharSequence title) {
	        mTitle = title;
	        getActionBar().setTitle(mTitle);
	    }
	    
	    
	    /**
	     * When using the ActionBarDrawerToggle, you must call it during
	     * onPostCreate() and onConfigurationChanged()...
	     */
	 
	    @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }
	 
	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        // Pass any configuration change to the drawer toggls
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }
	     
	    /**
	     * Slide menu item click listener
	     * */
	    private class SlideMenuClickListener implements
	            ListView.OnItemClickListener {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position,
	                long id) {
	            // display view for selected nav drawer item
	            displayView(position);
	        }
	    }
	    
		//------------------------------------------------------------------------------------------------------------
		/*
		 * Handles the case for which nav drawer item was clicked.
		 */
		//------------------------------------------------------------------------------------------------------------
	    private void displayView(int position) {
	        // update the main content by replacing fragments
	        Fragment fragment = null;
	        switch (position) {
	        case 0:
	        	Intent activityHome = new Intent(this, MainActivity.class);
	        	this.startActivity(activityHome);
	            break;
	        case 1:
	        	Intent activityMaps = new Intent(this, GoogleMapsActivity.class);
	        	this.startActivity(activityMaps);
	        	break;
	        case 2:
	            Intent tourActivity = new Intent(this, TourActivity.class);
	            this.startActivity(tourActivity);
	        	break;
	        case 3:
	            Intent settingsActivity = new Intent(this, SettingsActivity.class);
	            this.startActivity(settingsActivity);
	        	break;
	        case 4:
	            Intent achievementsActivity = new Intent(this,AchievementsActivity.class);
	            this.startActivity(achievementsActivity);
	            break; 
	        default:
	            break;
	        }
	    }
	    
	    public void parser() {
	   	 
	        Variables.listOfWaypoints = new ArrayList<Waypoint>();
	        Variables.listOfTours = new ArrayList<Tours>();
	        
	        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	        AssetManager assetManager = getBaseContext().getAssets();
	        try {
	        	InputStream is = assetManager.open("waypoints.xml");
	        	
	            SAXParserFactory spf = SAXParserFactory.newInstance();
	            SAXParser sp = spf.newSAXParser();
	            XMLReader xr = sp.getXMLReader();
	 
	   			ReadXMLFile handler = new ReadXMLFile();
	   			xr.setContentHandler(handler);
	   			InputSource inStream = new InputSource(is);
	   			xr.parse(inStream);
	 
	            List<Waypoint> wayList = handler.getWayList();
	           // Variables.listOfWaypoints.add(wayList.get(0));
	            for(Waypoint waypoint : wayList) {
	            	Variables.listOfWaypoints.add(waypoint);
	            }
	        }// catch (ParserConfigurationException | SAXException | IOException e) {
                catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
	        ArrayList<Waypoint> waypoints2 = new ArrayList<Waypoint>();
	        ArrayList<Waypoint> testWaypoint = new ArrayList<Waypoint>();
	        ArrayList<Waypoint> sampleTour = new ArrayList<Waypoint>();
	        
	        for(int i = 0; i < 8; i++) {
	        	waypoints.add(Variables.listOfWaypoints.get(i));
	        }
	        
	        for(int i = 0; i < Variables.listOfWaypoints.size();i++) {
	        	if(Variables.listOfWaypoints.get(i).getDescription().equals("Alumni Tower")) {
	        		waypoints2.add(Variables.listOfWaypoints.get(i));
	        		System.out.println("added alumni tower");
	        	}
	        	if(Variables.listOfWaypoints.get(i).getDescription().equals("Courtyard Cafeteria")) {
	        		waypoints2.add(Variables.listOfWaypoints.get(i));
	        		System.out.println("added caf");
	        	}
	        }
	        Waypoint truck = new Waypoint(35.332879, -83.200240,"Truck",998,"The truck");
	        Waypoint end_of_poarch = new Waypoint(35.332995, -83.199904,"End of Poarch",999,"End of Poarch");
	        testWaypoint.add(truck);
	        testWaypoint.add(end_of_poarch);
	        
	       /* Waypoint coulter = new Waypoint(35.311357,-83.182171,"Coulter Stop 1", 997,"Coulter Stop 1");
	        Waypoint campus_rec_center = new Waypoint(35.310717,-83.183028,"Campus Rec Center Stop 2", 996, "Campus Rec Center Stop 2");
	        Waypoint alumni_tower = new Waypoint(35.310450,-83.182631,"Alumni Tower Stop 3", 995, "Alumni Tower Stop 3");
	        Waypoint fountain = new Waypoint(35.310000,-83.182575,"Fountain Stop 4", 994,"Fountain Stop 4");
	        Waypoint caf = new Waypoint(35.309418,-83.183299,"Caf Stop 5", 993, "Caf Stop 5");
	        Waypoint library = new Waypoint(35.313028,-83.179745,"Hunter Library Last stop", 992,"Hunter Library last stop");
	        
	        
	        
	        
	        
	        sampleTour.add(coulter);
	        sampleTour.add(campus_rec_center);
	        sampleTour.add(alumni_tower);
	        sampleTour.add(fountain);
	        sampleTour.add(caf);
	        sampleTour.add(library); */
	        Waypoint library = new Waypoint(35.312966, -83.179877,"Hunter Library", 992,"Hunter Library last");
	        Waypoint alumni_tower = new Waypoint(35.310413, -83.182663,"Alumni Tower", 995, "Alumni Tower");
	        Waypoint caf = new Waypoint(35.309474, -83.183245,"Cafeteria ", 993, "Caf Stop");
	        Waypoint belk = new Waypoint (35.307838, -83.183053,"Belk Building", 1024, "Belk Building");
	        Waypoint stadium = new Waypoint(35.305428, -83.181740,"Stadium", 1025, "Stadium");
	        
	        sampleTour.add(library);
	        sampleTour.add(alumni_tower);
	        sampleTour.add(caf);
	        sampleTour.add(belk);
	        sampleTour.add(stadium);
	        
	        
			//Variables.listOfTours.add(new Tours(waypoints,"Academic Buildings Tour"));
			//Variables.listOfTours.add(new Tours(waypoints2,"Residential Living Tour"));
			Variables.listOfTours.add(new Tours(testWaypoint,"Sport's Tour"));
			Variables.listOfTours.add(new Tours(sampleTour, "Cross Campus Tour"));
	    }
}
