package activities;


import photoview.PhotoViewAttacher;
import utilities.Variables;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.wcu.wcutour.R;
import edu.wcu.wcutour.R.drawable;
import edu.wcu.wcutour.R.id;
import edu.wcu.wcutour.R.layout;
import utilities.ZoomActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Caleb Gragg, Jeremiah Griffin
 *
 * The information screen for an item that is selected from the list of waypoints or 
 * from the waypoints in the selected tour.
 */

public class SelectedItem extends BaseActivity implements View.OnClickListener{

    public GoogleMap googleMap; // google map object
    ListView list;
    HorizontalScrollView hsv;
    //http://android-er.blogspot.com/2012/07/implement-gallery-like.html
    LinearLayout myGallery;

    PhotoViewAttacher mAttacher;

    //int[] images = {R.drawable.alumni_tower,R.drawable.alumni_tower2, R.drawable.alumni_tower3 };
    ArrayList<Integer> imageIds = new ArrayList<>();

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;
    /**
     * Textview for the name of the selected waypoint.
     */
    TextView tv1;
    /**
     * Textview for description.
     */
    TextView tv2;
    /**
     * Textview for more information.
     */
    TextView tv3;
    /**
     * Textview for images.
     */
    TextView tv4;
    /**
     * Name to identify which item to show.
     */
    String name;


    //------------------------------------------------------------------------------------------------------------
    /**
     * onCreate method for the selected item.
     */
    //------------------------------------------------------------------------------------------------------------

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_selected_item, null, false);
        mDrawerLayout.addView(contentView, 0);

        map(); //sets up the map
        setImages(); // sets up the images to be used
        Bundle extras = getIntent().getExtras();


        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


        //sets up the information regarding the waypoint.
        if(extras != null)
        {
            // Find all the textviews.
            tv1 = (TextView) this.findViewById(R.id.textView1);
            tv2 = (TextView) this.findViewById(R.id.textView2);
            tv3 = (TextView) this.findViewById(R.id.textView3);
            tv4 = (TextView) this.findViewById(R.id.textView4);

            // Check to see where the intent is coming from.
            if(extras.containsKey("NAME")){
                name = extras.getString("NAME");
                tv1.setText(""+ name);
            }else {


                //waypoint name
                tv1.setText("" + Variables.selectedItem);
            }
            tv2.setText("Description:" + "\n");
            //when clicked takes the user to the more information screen
            tv3.setText("Click here for more information.\n");
            tv3.setOnClickListener(this);

            tv4.setText("Photos:");
        }


        //sets up the gallery
        myGallery = (LinearLayout)findViewById(R.id.mygallery);

        //loops over all the images and adds them to the gallery
        for(int i = 0; i<imageIds.size();i++) {
            ImageView iv = new ImageView (this);
            /* Finding the image Id. */
            Drawable bitmap = getResources().getDrawable(imageIds.get(i));
            /* Setting drawable image. */
            iv.setImageDrawable(bitmap);
            /* temporary iterator variable. */
            final int t = i;
            /* adding each image to the ListView. */
            myGallery.addView(iv);
            /* Setting click listener for each ImageView. */
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(SelectedItem.this, ZoomActivity.class);
                    /* Passing id to next activity . */
                    in.putExtra("image", imageIds.get(t));
                    /* Starting new Activity. */
                    startActivity(in);
                }
            });
        }

    }
    /**
     * Starts the more information screen when the "click here for more information"
     * is clicked
     * @param v view that was clicked
     */

    @Override
    public void onClick(View v) {

        Intent i = new Intent(SelectedItem.this,WaypointDescription.class);
        startActivity(i);
    }

    //------------------------------------------------------------------------------------------------------------
    /**
     * Method that calls the other methods relavent to setting up the map.
     */
    //------------------------------------------------------------------------------------------------------------

    public void map() {
        start();
        setUpOneWaypoint();
    }
    //------------------------------------------------------------------------------------------------------------
    /**
     * Trys to call the initialize map method.
     */
    //------------------------------------------------------------------------------------------------------------
    public void start() {
        try {
            initializeMap();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------
    /**
     * Initializes the map if the map is null.
     */
    //------------------------------------------------------------------------------------------------------------

    public void initializeMap() {
        if(googleMap == null) {
            googleMap = ((MapFragment)
                    getFragmentManager().findFragmentById(R.id.map)).getMap();
            if(googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------
    /**
     * Zooms over the waypoint.
     */
    //------------------------------------------------------------------------------------------------------------

    private void setUpOneWaypoint() {
        double latitude = Variables.selectedWaypoint.getLatitude();
        double longitude = Variables.selectedWaypoint.getLongitude();

        LatLng location = new LatLng(latitude,longitude);
        CameraUpdate center = CameraUpdateFactory.newLatLng(location);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

        // Set the location of the camera.
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        setMarkers();
    }

    //------------------------------------------------------------------------------------------------------------
    /**
     * Sets the marker for the single waypoint.
     */
    //------------------------------------------------------------------------------------------------------------

    public void setMarkers() {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(Variables.selectedWaypoint.getLatitude(), Variables.selectedWaypoint.getLongitude())).title(Variables.selectedWaypoint.getDescription()).snippet("WCU")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
    }

    //-------------------------------------------------------------------------
    /**
     * Sets up the image ids for the pictures in the waypoint screen.
     */
    //-------------------------------------------------------------------------
    public void setImages() {
        // local counter.
        int counter = 0;
        // local boolean
        boolean check = true;
        // name of waypoint
        String name = "";
        // get the name of the selected waypoint.
        name = Variables.selectedItem.toLowerCase();
        // take out whitespace.
        name = name.replaceAll("\\s+", "");
        name = name.replaceAll("\\\\","");
        //gets all images with the name followed by _ and a number
        while(check) {
            imageIds.add(counter,getResources().getIdentifier(name + "_" + counter,
                    "drawable", getPackageName()));
            if(imageIds.get(counter) == 0) {
                check = false;
                imageIds.remove(counter);
            }
            counter++;
        }
    }
}

