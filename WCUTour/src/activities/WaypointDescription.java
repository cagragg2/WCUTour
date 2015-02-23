package activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.wcu.wcutour.R;
import models.WaypointInfo;
import utilities.Variables;

/**
 * @author Caleb Gragg, Jeremiah Griffin
 * @version 2/23/2015
 *
 * Activity to show more information about a given waypoint.
 */

public class WaypointDescription extends Activity {


    TextView tv1;
    TextView tv2;
    TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint_description);

        String desc = Variables.selectedItem;
        String inf = Variables.selectedWaypoint.getInformation();
        String hours = Variables.selectedWaypoint.getHours();
        String uses = Variables.selectedWaypoint.getUses();

        WaypointInfo info = new WaypointInfo(desc,inf,hours,uses);

        tv1 = (TextView) findViewById(R.id.infotv1);
        tv2 = (TextView) findViewById(R.id.infotv2);
        tv3 = (TextView) findViewById(R.id.infotv3);

        tv1.setText(info.getInformation());
        //tv2.setText("heyeyeyey");
        tv2.setText(info.getHours());
        tv3.setText(info.getUses());

        Log.v("Tour",Variables.selectedWaypoint.getHours());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waypoint_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
