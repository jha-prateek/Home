package com.monitorapp.prateekjha.home;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.anastr.speedviewlib.SpeedView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashFragment extends Fragment {
    private SpeedView speedView;
    private final Handler hand = new Handler();
    private Runnable timer;

    public DashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Home - Dashboard");
        return inflater.inflate(R.layout.fragment_dash, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        speedView = (SpeedView)view.findViewById(R.id.speedView);
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Runnable(){
            @Override
            public void run(){
                speedView.speedTo((float) getRandom());
                hand.postDelayed(this,3000);
            }
        };
        hand.postDelayed(timer,3000);
    }

    private double getRandom() {
        Random rand = new Random();
        return rand.nextDouble()*50;
    }

    @Override
    public void onPause() {
        hand.removeCallbacks(timer);
        super.onPause();
    }
}
