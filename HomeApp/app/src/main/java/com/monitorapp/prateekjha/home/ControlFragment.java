package com.monitorapp.prateekjha.home;


import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.VIBRATOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ControlFragment extends Fragment {

    private Vibrator vibrator;
    private Button onSwitch;
    private Button offSwitch;
    private TextView switchStateView;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference Switch = root.child("Switch_Status");
    private DatabaseReference switch_Duration = root.child("Switch_Duration");
    private DatabaseReference sensors = root.child("Sensors");
    private String switchState;
    private String switchTime;
    private durationData durationObject;
    private float temp;
    private float humid;


    public ControlFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Home - Control");
        View root = inflater.inflate(R.layout.fragment_control, container, false);

        vibrator = (Vibrator)getActivity().getSystemService(VIBRATOR_SERVICE);

        onSwitch = (Button)root.findViewById(R.id.button);
        onSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchState.equals("0")){
                    Switch.child("Switch").setValue(1);
                    Switch.child("Time").setValue(System.currentTimeMillis());
                    vibrator.vibrate(200);
                }else {
                    return;
                }
            }
        });

        offSwitch = (Button)root.findViewById(R.id.button2);
        offSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchState.equals("1")){

                    durationObject = new durationData(System.currentTimeMillis()/86400000,
                            System.currentTimeMillis() - Long.parseLong(switchTime),
                            System.currentTimeMillis(),
                            temp,
                            humid);
                    switch_Duration.push().setValue(durationObject);

                    Switch.child("Switch").setValue(0);
                    Switch.child("Time").setValue(System.currentTimeMillis());
                    vibrator.vibrate(200);
                }else {
                    return;
                }
            }
        });
        return root;
    }

    @Override
    public void onStart(){
        super.onStart();
        View contents = getView();

        switchStateView = (TextView)contents.findViewById(R.id.textView);

        // Getting Switch State
        Switch.child("Switch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switchStateView.setText(dataSnapshot.getValue().toString());
                switchState = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                switchStateView.setText(databaseError.toString());
            }
        });

        // Getting Switch Time Value
        Switch.child("Time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switchTime = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Getting Humidity
        sensors.child("Humid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                humid = Float.parseFloat(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Getting Temperature
        sensors.child("Temp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp = Float.parseFloat(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

class durationData{
    public long Day;
    public long Time;
    public long Duration;
    public float Temperature;
    public float Humidity;

    public durationData(long day, long duration, long time, float temperature, float humidity) {
        Day = day;
        Time = time;
        Duration = duration;
        Temperature = temperature;
        Humidity = humidity;
    }

    public durationData() {
    }
}