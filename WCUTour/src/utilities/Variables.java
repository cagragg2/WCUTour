package utilities;

import java.util.ArrayList;
import java.util.List;

import models.Tours;
import models.Waypoint;

import com.google.android.gms.maps.model.LatLng;

import android.app.PendingIntent;
import android.location.Location;
import android.location.LocationManager;

/**
 * 
 * @author Caleb Gragg, Jeremiah Griffin
 * 
 * Class to hold all of the variables that are used throughout the application.
 *
 */
public class Variables {
	/**
	 * the list of waypoints in the application
	 */
	public static List<Waypoint> listOfWaypoints;
	/**
	 * The list of tours that are avaliable to the application
	 */
	public static List<Tours> listOfTours;
	
	/**
	 * The selected waypoints information.
	 */
	public static String selectedItem;
	
	/**
	 * the selected waypoint to display information about.
	 */
	public static Waypoint selectedWaypoint; 
	
	/**
	 * the tour that the user selected.
	 */
	public static Tours selectedTour;
	
	/**
	 * the movements of the user
	 */
	public static ArrayList<LatLng> myTracks;
	
	/**
	 * to count where we are at in the tour.
	 */
	public static int tourCounter;
	
	/**
	 * Location manager to be used in the navigation activity
	 * used for location of the user.
	 */
	public static LocationManager locationManager;
	
	/**
	 * Whether the application should add a new proximity.
	 */
	public static boolean addNewProximity;
	
	/**
	 * the pending intent to be used for the proximity.
	 */
	public static PendingIntent pi;
	
	/**
	 * the distance from the user to the next waypoint
	 */
	public static int initialDistance;
	
	/**
	 * Whether the application should change the distance
	 */
	public static boolean changeDistance;
	
	/**
	 * the progress that user is from the next waypoint.
	 */
	public static double progressStatus;
	
	/**
	 * Whether or not the information page should be displayed.
	 */
	public static boolean updateInformation;

    /**
     * My location.
     */
    public static Location myLocation;
}

