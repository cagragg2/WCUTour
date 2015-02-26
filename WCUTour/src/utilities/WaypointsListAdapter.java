package utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.wcu.wcutour.R;

/**
 * Created by Caleb Gragg, Jeremiah Griffin on 2/25/2015.
 */
public class WaypointsListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<WaypointItem> waypointItem;

    public WaypointsListAdapter(Context context, ArrayList<WaypointItem> waypointItem){
        this.context = context;
        this.waypointItem = waypointItem;
    }


    @Override
    public int getCount() {
        return waypointItem.size();
    }

    @Override
    public Object getItem(int position) {
        return waypointItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.waypoint_list_item, null);
        }

        TextView waypoint = (TextView) convertView.findViewById(R.id.waylisttv1);
        TextView dist = (TextView) convertView.findViewById(R.id.waylisttv2);
        //TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter2);

       // imgIcon.setImageResource(waypointItem.get(position).getIcon());
       waypoint.setText(waypointItem.get(position).getTitle());
       dist.setText(waypointItem.get(position).getDistance() + "");

        // displaying count
        // check whether it set visible or not
        if(waypointItem.get(position).getCounterVisibility()){
            txtCount.setText(waypointItem.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }
}
