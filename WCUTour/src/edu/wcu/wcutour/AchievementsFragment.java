package edu.wcu.wcutour;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AchievementsFragment extends Fragment {
    public AchievementsFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        //View rootView = inflater.inflate(R.layout.fragment_achievements, container, false);
          View rootView = inflater.inflate(R.layout.fragment_achievements, container,false);
        return rootView;
    }
}
