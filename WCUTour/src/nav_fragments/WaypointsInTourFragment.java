package nav_fragments;

import activities.SelectedItem;
import edu.wcu.wcutour.R;
import utilities.NavDrawerListAdapter;
import utilities.Variables;
import utilities.WaypointItem;
import utilities.WaypointsListAdapter;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Caleb Gragg, Jeremiah Griffin
 * Fragment for the waypoints in tour tab.  Consisits of a custom list view for each waypoint
 * in the currently selected tour, and a custom adapter for the list view.
 */
public class WaypointsInTourFragment extends Fragment {

    private ListView lv; //list view to be populated.
    private WaypointsListAdapter adapter; //adapter for the list view

    /**
     * onCreate method to set up the list view and initialize the custom adapter
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.waypoints_in_tour_fragment, container, false);

        lv = (ListView) rootView.findViewById(R.id.way_list);//finds the list view

        //sets up the locations for the adapter
        ArrayList<WaypointItem> loc = new ArrayList<WaypointItem>();
        for(int i = 0; i < Variables.selectedTour.getTour().size();i++) {
            loc.add(i,new WaypointItem(Variables.selectedTour.getTour().get(i).getDescription()
            , 3));
        }

        //sets up the custom adapter
        adapter = new WaypointsListAdapter(getActivity().getApplicationContext(), loc);
        lv.setAdapter(adapter);
        //sets a click listener for each waypoint
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(getActivity(), SelectedItem.class);
                //
                Variables.selectedWaypoint = Variables.listOfWaypoints.get(parent.getPositionForView(view));
                Variables.selectedItem = Variables.selectedWaypoint.getDescription();

               // i.putExtra(Variables.selectedItem + "", ((TextView) view).getText());
                i.putExtra("g",true);
                startActivity(i);
            }
        });

        return rootView;
	}
}
