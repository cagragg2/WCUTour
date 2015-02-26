package nav_fragments;

import edu.wcu.wcutour.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Caleb Gragg, Jeremiah Griffin
 *
 * Tab for the information about the tour.
 */
public class TourInfoFragment extends Fragment{

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tour_info_fragment, container, false);


        return rootView;
	}
}
