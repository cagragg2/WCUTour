package activities;

import models.Waypoint;
import utilities.Variables;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.wcu.wcutour.R;
import edu.wcu.wcutour.R.id;
import edu.wcu.wcutour.R.layout;
import edu.wcu.wcutour.R.menu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 *
 * @author Caleb Gragg, Jeremiah Griffin
 *
 * Creates the campus map when the campus map button is selected from the home screen.
 * Also places all the markers for each of the waypoints.
 *
 */

public class GoogleMapsActivity extends BaseActivity implements GoogleMap.OnMarkerClickListener {

    /**
     * Google Map object.
     */

    private GoogleMap googleMap;

    /**
     * THis method creates the activity and registers all of the components.
     * @param savedInstanceState - saved data.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_map);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.fragment_map, null, false);
        mDrawerLayout.addView(contentView, 0);

        try {
            //load map
            initilizeMap();
            setUpMap();
        } catch(Exception e){
            e.printStackTrace();
        } //end catch
    }//end onCreate


    /**
     * Checks if the map is null and if it is sets up a new map.
     *
     */
    private void initilizeMap() {
        if(googleMap == null) {
            googleMap = ((MapFragment)
                    getFragmentManager().findFragmentById(R.id.map)).getMap();
            //check if map is created successully or not
            if(googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method initializes the map when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    /**
     * This method creates the actionbar menu.
     * @param menu - the menu to create
     * @return - true if menu was created successfully.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps, menu);
        return true;
    }

    /**
     * This method handles when the actionbar options are selected.
     * @param item - option on the menu.
     * @return - true if a valid option was selected.
     */
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
     * Private method to set up the map to be presented.
     */
    private void setUpMap() {

        // latitude and longitude
        double latitude = 35.309200;
        double longitude = -83.186941;

        //Specify WGS 84 coordinates for WCU
        LatLng location = new LatLng(35.309164, -83.185412);
        //Pass them to a new CameraUpdateObject
        CameraUpdate center= CameraUpdateFactory.newLatLng( location );
        //Specify a zoom level
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        //Position and zoom camera;
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setMyLocationEnabled(true);

        setMarkers();

    }
    /**
     * Sets up the markers for all of the waypoints.
     *
     */

    public void setMarkers(){

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setOnMarkerClickListener(this);
        Marker marker;
//Loops over and places all of the markers.
        for(int i = 0; i < Variables.listOfWaypoints.size(); i++){
            marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Variables.listOfWaypoints.get(i).getLatitude(),
                    Variables.listOfWaypoints.get(i).getLongitude())).title(Variables.listOfWaypoints.get(i).getDescription()).snippet("WCU")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        }
    }

    /**
     * THis method handles when a marker is clicked. It creates an intent that shows the
     * information screen of the selected waypoint.
     * @param marker - the selected marker on the map.
     * @return - true if selected.
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        // String for the title of the selected marker.
        String title = marker.getTitle();
        // Value to send in intent so SelectedItem knows which waypoint to display.
        String value = "";
        // switch statement to handle the title of each waypoint.
        switch(title){
            case "Soccer Field/Running Track":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(13);
                Variables.selectedItem = Variables.listOfWaypoints.get(13).getDescription();
                Variables.selectedItem = Variables.listOfWaypoints.get(13).getDescription();
                value = Variables.listOfWaypoints.get(13).getDescription();
                Intent in = new Intent(this,SelectedItem.class);
                in.putExtra("NAME",value);
                startActivity(in);
                break;

            case "Ramsey Center":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(0);
                value = Variables.listOfWaypoints.get(0).getDescription();
                Variables.selectedItem = Variables.listOfWaypoints.get(0).getDescription();
                Variables.selectedItem = Variables.listOfWaypoints.get(0).getDescription();
                Intent in1 = new Intent(this,SelectedItem.class);
                in1.putExtra("NAME",value);
                startActivity(in1);
                break;
            case "Alumni Tower":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(1);
                Variables.selectedItem = Variables.listOfWaypoints.get(1).getDescription();
                Intent in2 = new Intent(this,SelectedItem.class);
                value = Variables.listOfWaypoints.get(1).getDescription();
                in2.putExtra("NAME",value);
                startActivity(in2);
                break;
            case "Coulter":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(2);
                Variables.selectedItem = Variables.listOfWaypoints.get(2).getDescription();
                value = Variables.listOfWaypoints.get(2).getDescription();
                Intent in3 = new Intent(this,SelectedItem.class);
                in3.putExtra("NAME",value);
                startActivity(in3);
                break;
            case "Reid Gym":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(3);
                Variables.selectedItem = Variables.listOfWaypoints.get(3).getDescription();
                value = Variables.listOfWaypoints.get(3).getDescription();
                Intent in4 = new Intent(this,SelectedItem.class);
                in4.putExtra("NAME",value);
                startActivity(in4);
                break;
            case "Student One-Stop":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(4);
                Variables.selectedItem = Variables.listOfWaypoints.get(4).getDescription();
                value= Variables.listOfWaypoints.get(4).getDescription();
                Intent in5 = new Intent(this,SelectedItem.class);
                in5.putExtra("NAME",value);
                startActivity(in5);
                break;
            case "Hunter Library":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(5);
                Variables.selectedItem = Variables.listOfWaypoints.get(5).getDescription();
                value = Variables.listOfWaypoints.get(5).getDescription();
                Intent in6 = new Intent(this,SelectedItem.class);
                in6.putExtra("NAME",value);
                startActivity(in6);
                break;
            case "Student Union Center":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(6);
                Variables.selectedItem = Variables.listOfWaypoints.get(6).getDescription();
                value= Variables.listOfWaypoints.get(6).getDescription();
                Intent in7 = new Intent(this,SelectedItem.class);
                in7.putExtra("NAME",value);
                startActivity(in7);
                break;
            case "Football Field":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(7);
                Variables.selectedItem = Variables.listOfWaypoints.get(7).getDescription();
                value = Variables.listOfWaypoints.get(7).getDescription();
                Intent in8 = new Intent(this,SelectedItem.class);
                in8.putExtra("NAME",value);
                startActivity(in8);
                break;
            case "Campus Recreation Center":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(8);
                Variables.selectedItem = Variables.listOfWaypoints.get(8).getDescription();
                value= Variables.listOfWaypoints.get(8).getDescription();
                Intent in9 = new Intent(this,SelectedItem.class);
                in9.putExtra("NAME",value);
                startActivity(in9);
                break;
            case "Belk":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(9);
                Variables.selectedItem = Variables.listOfWaypoints.get(9).getDescription();
                value = Variables.listOfWaypoints.get(9).getDescription();
                Intent in11 = new Intent(this,SelectedItem.class);
                in11.putExtra("NAME",value);
                startActivity(in11);
                break;
            case "Campus Bookstore":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(10);
                Variables.selectedItem = Variables.listOfWaypoints.get(10).getDescription();
                value = Variables.listOfWaypoints.get(10).getDescription();
                Intent in12 = new Intent(this,SelectedItem.class);
                in12.putExtra("NAME",value);
                startActivity(in12);
                break;
            case "Administration Building":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(11);
                Variables.selectedItem = Variables.listOfWaypoints.get(11).getDescription();
                value = Variables.listOfWaypoints.get(11).getDescription();
                Intent in13 = new Intent(this,SelectedItem.class);
                in13.putExtra("NAME",value);
                startActivity(in13);
                break;
            case "Campus Police":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(12);
                Variables.selectedItem = Variables.listOfWaypoints.get(12).getDescription();
                value = Variables.listOfWaypoints.get(12).getDescription();
                Intent in14 = new Intent(this,SelectedItem.class);
                in14.putExtra("NAME",value);
                startActivity(in14);
                break;
            case "Bardo Arts Center":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(14);
                Variables.selectedItem = Variables.listOfWaypoints.get(14).getDescription();
                value = Variables.listOfWaypoints.get(14).getDescription();
                Intent in15 = new Intent(this,SelectedItem.class);
                in15.putExtra("NAME",value);
                startActivity(in15);
                break;
            case "Courtyard Cafeteria":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(15);
                Variables.selectedItem = Variables.listOfWaypoints.get(15).getDescription();
                value = Variables.listOfWaypoints.get(15).getDescription();
                Intent in16 = new Intent(this,SelectedItem.class);
                in16.putExtra("NAME", value);
                startActivity(in16);
                break;
            case "Fountain":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(16);
                Variables.selectedItem = Variables.listOfWaypoints.get(16).getDescription();
                value = Variables.listOfWaypoints.get(16).getDescription();
                Intent in17 = new Intent(this,SelectedItem.class);
                in17.putExtra("NAME",value);
                startActivity(in17);
                break;
            case "Health Services":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(17);
                Variables.selectedItem = Variables.listOfWaypoints.get(17).getDescription();
                value = Variables.listOfWaypoints.get(17).getDescription();
                Intent in18 = new Intent(this,SelectedItem.class);
                in18.putExtra("NAME",value);
                startActivity(in18);
                break;
            case "Baseball Field":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(18);
                Variables.selectedItem = Variables.listOfWaypoints.get(18).getDescription();
                value = Variables.listOfWaypoints.get(18).getDescription();
                Intent in19 = new Intent(this,SelectedItem.class);
                in19.putExtra("NAME",value);
                startActivity(in19);
                break;
            case "Catamount":
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(19);
                Variables.selectedItem = Variables.listOfWaypoints.get(19).getDescription();
                value = Variables.listOfWaypoints.get(19).getDescription();
                Intent in20 = new Intent(this,SelectedItem.class);
                in20.putExtra("NAME",value);
                startActivity(in20);
                break;
        }


        return false;
    }
}

