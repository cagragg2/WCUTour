package activities;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import models.TourPoint;
import utilities.Decompress;
import utilities.NavDrawerItem;
import models.Tours;
import models.Waypoint;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import utilities.NavDrawerListAdapter;
import utilities.ReadTourXML;
import utilities.ReadXMLFile;
import utilities.Variables;
import edu.wcu.wcutour.R;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


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
	    private NavDrawerListAdapter adapter; //adapter for the custom list
	    
	    public static FragmentManager fragmentManager; // fragment manager for transactions.



		//------------------------------------------------------------------------------------------------------------
		/**
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

            checkIfDownloaded();
	        parser();
		}//end onCreate

	private void checkIfDownloaded() {
        File dirFiles = getFilesDir();
        for (String strFile : dirFiles.list())
        {
            Log.e("FILES",strFile.toString());
        }
    }


		/**
		 * onResume method. 
		 */
		@Override
		protected void onResume() {
			
			super.onResume();

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
	    /**
	     * Sets the title of the actionBar. 
	     * @param title - title of action bar.
	     */
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
		/**
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


    /**
     * Method to parse the xml files and record the values read into the Variables class for
     * later use throughtout the application.
     */

	    public void parser() {

            unzip("Tours.zip",getFilesDir().getAbsolutePath());


            File dirFiles = getFilesDir();

            //All of the waypoints to store from xml file.
	        Variables.listOfWaypoints = new ArrayList<Waypoint>();
	        //The tours made from waypoints.
            Variables.listOfTours = new ArrayList<Tours>();
            // The cross-campus tour.
            Variables.crossCampusTour = new ArrayList<TourPoint>();

	        // Reading the waypoints.xml file in and storing it an ArrayList.
	        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	        AssetManager assetManager = getBaseContext().getAssets();


            try {

                FileInputStream fis = openFileInput("waypoints.xml");
                InputStreamReader isr = new InputStreamReader(fis);

	        	
	            SAXParserFactory spf = SAXParserFactory.newInstance();
	            SAXParser sp = spf.newSAXParser();
	            XMLReader xr = sp.getXMLReader();
	 
	   			ReadXMLFile handler = new ReadXMLFile(); //reader for the xml file
	   			xr.setContentHandler(handler);
	   			InputSource inStream = new InputSource(isr);
	   			xr.parse(inStream);
	 
	            List<Waypoint> wayList = handler.getWayList();
                //Inserts all of the waypoints in the ArrayList.
	            for(Waypoint waypoint : wayList) {
	            	Variables.listOfWaypoints.add(waypoint);
	            }
	        }
                catch (Exception e) { //somehow got an error.
	            e.printStackTrace();
	        }

            // Reading the cross_campus_tour.xml in and storing it in an ArrayList.

            SAXParserFactory saxParserFact2 = SAXParserFactory.newInstance();
            AssetManager assetManager2 = getBaseContext().getAssets();

            try {

                FileInputStream fis = openFileInput("cross_campus_tour.xml");
                InputStreamReader isr = new InputStreamReader(fis);

                SAXParserFactory spf2 = SAXParserFactory.newInstance();
                SAXParser sp2 = spf2.newSAXParser();
                XMLReader reader = sp2.getXMLReader();

                ReadTourXML handler2 = new ReadTourXML();
                reader.setContentHandler(handler2);
                InputSource inStream2 = new InputSource(fis);
                reader.parse(inStream2);

                List<TourPoint> tourList = handler2.getTourList();

                for(TourPoint point : tourList) {
                    Variables.crossCampusTour.add(point);


                }
            }// catch (ParserConfigurationException | SAXException | IOException e) {
            catch (Exception e) {
                e.printStackTrace();
            }

	        ArrayList<Waypoint> testWaypoint = new ArrayList<Waypoint>();
	        ArrayList<Waypoint> sampleTour = new ArrayList<Waypoint>();

	        Waypoint library = new Waypoint(35.312966, -83.179877,"Hunter Library", 992,"Hunter Library last","","");
	        Waypoint alumni_tower = new Waypoint(35.310413, -83.182663,"Alumni Tower", 995, "Alumni Tower","","");
	        Waypoint caf = new Waypoint(35.309474, -83.183245,"Cafeteria ", 993, "Caf Stop","","");
	        Waypoint belk = new Waypoint (35.307838, -83.183053,"Belk Building", 1024, "Belk Building","","");
	        Waypoint stadium = new Waypoint(35.305428, -83.181740,"Stadium", 1025, "Stadium","","");
	        
	        sampleTour.add(library);
	        sampleTour.add(alumni_tower);
	        sampleTour.add(caf);
	        sampleTour.add(belk);
	        sampleTour.add(stadium);

			Variables.listOfTours.add(new Tours(testWaypoint,"Sport's Tour"));
			Variables.listOfTours.add(new Tours(sampleTour, "Cross Campus Tour"));


	    }


    public void unzip(String zipFile, String location) {
        try {
            FileInputStream fin;
            fin = openFileInput(zipFile);
            unzip(fin, location);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void unzip(InputStream stream, String destination) {
      //  dirChecker(destination, "");
        byte[] buffer = new byte[1024*10];
        try {
            ZipInputStream zin = new ZipInputStream(stream);
            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null) {
              //  Log.v("tag", "Unzipping " + ze.getName());
                String name = "";
                if(ze.getName().equals("Tours/cross_campus_tour.xml")) {
                    name = "cross_campus_tour.xml";
                } else if (ze.getName().equals("Tours/waypoints.xml")) {
                   // Log.e("tag","here");
                    name = "waypoints.xml";
                } else if(ze.getName().equals("Tours/sports_tour.xml")) {
                    name = "sports_tour.xml";
                }

                if (ze.isDirectory()) {
             //       dirChecker(destination, ze.getName());
                } else {
                   // File f = new File(destination + ze.getName());
                    File f = new File(destination + "/" + name);
                    Log.e("here","Made file " + destination +"/" + name);
                   // if (!f.exists()) {
                       // FileOutputStream fout = new FileOutputStream(destination + ze.getName());
                        FileOutputStream fout = new FileOutputStream(destination +"/"+ name);
                        int count;
                        while ((count = zin.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                       //     Log.e("tag","here");
                        }
                        zin.closeEntry();
                        fout.close();
                    //}
                }

            }
            zin.close();
        } catch (Exception e) {
            Log.e("TAg", "unzip", e);
        }

    }

    private void dirChecker(String destination, String dir) {
        File f = new File(destination + dir);

        if (!f.isDirectory()) {
            boolean success = f.mkdirs();
            if (!success) {
                // Log.w("tag", "Failed to create folder " + f.getName());
            }
        }
    }

}
