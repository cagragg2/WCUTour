package navigation;

import edu.wcu.wcutour.R;
import utilities.Variables;
import activities.SelectedTourActivity;
import activities.TourActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Caleb Gragg, Jeremiah Griffin
 * 
 * The proximity receiver class to handle when a user gets within a certain
 * range of the waypoint.
 *
 */
public class ProximityReceiver extends BroadcastReceiver {
	
	/**
	 * When we get within a distance from the next waypoint
	 */
	 @Override
	 public void onReceive(Context arg0, Intent arg1) {
	  
	  String k=LocationManager.KEY_PROXIMITY_ENTERING;
	 // Key for determining whether user is leaving or entering 
	  
	  boolean state=arg1.getBooleanExtra(k, false);
	  //Gives whether the user is entering or leaving in boolean form
	  
	  
	  /*
	   * When in range of a proximity it will toast that you made it then remove the alert and add another to the next waypoint in the list.
	   */
	  if(state){	   
		 

		  //if we need to update the proximities.
	   if(Variables.selectedTour.getTour().size() > Variables.tourCounter) {
		   Toast.makeText(arg0, "Welcome to " + Variables.selectedTour.getTour().get(Variables.tourCounter-1).getDescription(), 600).show();  
		   Variables.addNewProximity = true;
		   double lat;
		   double lon;
		   lat = Variables.selectedTour.getTour().get(Variables.tourCounter).getLatitude();
		   lon = Variables.selectedTour.getTour().get(Variables.tourCounter).getLongitude();
	   
		   Variables.locationManager.removeProximityAlert(Variables.pi);
		   Variables.locationManager.addProximityAlert(lat, lon, 15, -1, Variables.pi);
		   Variables.tourCounter += 1;
		   Variables.changeDistance = true;
		   Variables.updateInformation = true;
		   Log.e("Tour", "size was greater than counter");
		   
	   } else {
		   Variables.updateInformation = true;
		   Log.e("Tour", "size was less than counter");
	   }
	  }else{
	   //Other custom Notification 
		  
	   Toast.makeText(arg0, "Done", 600).show();

	  }
	}
}
