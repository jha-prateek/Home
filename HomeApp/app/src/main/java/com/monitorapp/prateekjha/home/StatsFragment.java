package com.monitorapp.prateekjha.home;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;


public class StatsFragment extends Fragment {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference data = root.child("Switch_Duration");
    private DatabaseReference future = root.child("Tommorow's Prediction");

    private TextView tomo;
    private TextView disp;
    private double unitPowerRate = (0.015)/3600000; // LED_Power x Rate (5)
    private LinkedList<Double> power = new LinkedList<>();


    private final Handler hand = new Handler();
    private Runnable timer;
    private GraphView pgraph;
    private LineGraphSeries<DataPoint> series;


    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Home - Statistics");
        return inflater.inflate(R.layout.fragment_stats, container, false);

    }

    @Override
    public void onStart(){
        super.onStart();

        power.add(0.0);

        View view = getView();

        disp = (TextView)view.findViewById(R.id.textView3);
        tomo = (TextView)view.findViewById(R.id.textView4);

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    long day = Long.parseLong(ds.child("Day").getValue().toString());
                    long duration = 0;
                    for(DataSnapshot Ids: dataSnapshot.getChildren()){

                        if(Long.parseLong(Ids.child("Day").getValue().toString())==day){
                            duration += Long.parseLong(Ids.child("Duration").getValue().toString());
                        }
                    }

                    if(power.getLast() != duration*unitPowerRate*100){
                        power.addLast(duration*unitPowerRate*100);
                    }

                }

                disp.setText("Last Consumption: " + Double.toString(power.getLast()).substring(0,7) + "kW");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        future.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tomo.setText("Tomorrow's Consumption \n" + dataSnapshot.getValue().toString().substring(0,7) + "kW");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        pgraph = (GraphView) view.findViewById(R.id.temp_graph);
        series = new LineGraphSeries<>(generateData());
        pgraph.addSeries(series);
        pgraph.getViewport().setYAxisBoundsManual(true);
        pgraph.getViewport().setMinY(0);
        pgraph.getViewport().setMaxY(700);
        pgraph.getViewport().setXAxisBoundsManual(true);
        pgraph.getViewport().setMinX(1);
        pgraph.getViewport().setMaxX(10);
        pgraph.getViewport().setScrollable(true);
    }

    @Override
    public void onResume(){
        super.onResume();

        timer = new Runnable(){
            @Override
            public void run(){
                series.resetData(generateData());
                hand.postDelayed(this,100);
            }
        };
        hand.postDelayed(timer,100);

    }

    private DataPoint[] generateData() {

        LinkedList<Integer> pk = new LinkedList<>();
        for(int s=0; s<10; s++){
            pk.add(0);
        }
        DataPoint[] values = new DataPoint[10];
        if(power.size()>5){
            double x = 0;
            int a = 0;
            for (int i = power.size()-10; i < power.size(); i++) {
                double y = power.get(i);
                DataPoint v = new DataPoint(x++, y);
                values[a++] = v;
            }
        }else {
        for (int i=0; i<10; i++) {
            double x = i;
            double y = pk.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
    }
        return values;
    }


    @Override
    public void onPause() {

        series.resetData(new DataPoint[]{
                new DataPoint(0, 0),
        });
        hand.removeCallbacks(timer);

        super.onPause();
    }

}
