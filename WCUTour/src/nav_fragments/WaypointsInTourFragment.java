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

public class WaypointsInTourFragment extends Fragment {

    private ListView lv;
    private WaypointsListAdapter adapter;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.waypoints_in_tour_fragment, container, false);

        lv = (ListView) rootView.findViewById(R.id.way_list);

       // List<String> locations = new ArrayList<String>();
       // for(int i = 0; i < Variables.listOfWaypoints.size();i++) {
       //     locations.add(Variables.listOfWaypoints.get(i).getDescription());
      //  }
       // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
         //       getActivity().getApplicationContext(),
        //        R.layout.waypoint_list_item,
        //        locations );

        //lv.setAdapter(arrayAdapter);

        ArrayList<WaypointItem> loc = new ArrayList<WaypointItem>();
        for(int i = 0; i < Variables.selectedTour.getTour().size();i++) {
            loc.add(i,new WaypointItem(Variables.selectedTour.getTour().get(i).getDescription()
            , 3));
        }

        adapter = new WaypointsListAdapter(getActivity().getApplicationContext(), loc);
        lv.setAdapter(adapter);
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
