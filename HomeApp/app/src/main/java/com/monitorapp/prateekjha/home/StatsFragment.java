package com.monitorapp.prateekjha.home;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.Random;


public class StatsFragment extends Fragment {

    private final Handler hand = new Handler();
    private Runnable timer1;
    private Runnable timer2;
    private GraphView tgraph;
    private GraphView hgraph;
    private LineGraphSeries<DataPoint> series1;
    private LineGraphSeries<DataPoint> series2;
    private LinkedList<Double> Y = new LinkedList<>();

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

        for(int i=0; i<10; i++){
            Y.add(getRandom());
        }

        View contents = getView();

        tgraph = (GraphView) contents.findViewById(R.id.temp_graph);
        series1 = new LineGraphSeries<>();
        tgraph.addSeries(series1);
        tgraph.getViewport().setYAxisBoundsManual(true);
        tgraph.getViewport().setMinY(0);
        tgraph.getViewport().setMaxY(100);

        hgraph = (GraphView) contents.findViewById(R.id.humid_graph);
        series2 = new LineGraphSeries<>(generateData());
        hgraph.addSeries(series2);
        hgraph.getViewport().setYAxisBoundsManual(true);
        hgraph.getViewport().setMinY(0);
        hgraph.getViewport().setMaxY(100);
        hgraph.getViewport().setXAxisBoundsManual(true);
        hgraph.getViewport().setMinX(0);
        hgraph.getViewport().setMaxX(10);
        hgraph.getViewport().setScrollable(true);


        GraphView rgraph = (GraphView) contents.findViewById(R.id.rain_graph);
        LineGraphSeries<DataPoint> rseries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 90),
                new DataPoint(1, 75),
                new DataPoint(2, 35),
                new DataPoint(3, 100),
                new DataPoint(4, 85)
        });
        rgraph.addSeries(rseries);
    }

    private int load = 0;
    @Override
    public void onResume(){
        super.onResume();
        timer1 = new Runnable(){
            @Override
            public void run(){
                load += 2;
                series1.appendData(new DataPoint(load, getRandom()), true, 40);
                hand.postDelayed(this,1000);
            }
        };
        hand.postDelayed(timer1,1000);

        timer2 = new Runnable(){
            @Override
            public void run(){
                series2.resetData(generateData());
                hand.postDelayed(this,5000);
            }
        };
        hand.postDelayed(timer2,5000);
    }

    private double getRandom() {
        Random rand = new Random();
        return rand.nextDouble()*100;
    }

    private DataPoint[] generateData() {
        Random rand = new Random();
        DataPoint[] values = new DataPoint[10];
        for (int i=0; i<10; i++) {
            double x = i;
            double y = Y.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        Y.remove();
        Y.add(rand.nextDouble()*100);
        return values;
    }

    @Override
    public void onPause() {
        series2.resetData(new DataPoint[]{
                new DataPoint(0, 0),
        });
        hand.removeCallbacks(timer1);
        hand.removeCallbacks(timer2);
        super.onPause();
    }

}
