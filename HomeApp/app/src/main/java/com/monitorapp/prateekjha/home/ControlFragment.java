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
    private DatabaseReference mSwitch = root.child("Switch");

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
                mSwitch.setValue(1);
                vibrator.vibrate(200);
            }
        });

        offSwitch = (Button)root.findViewById(R.id.button2);
        offSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitch.setValue(0);
                vibrator.vibrate(200);
            }
        });
        return root;
    }

    @Override
    public void onStart(){
        super.onStart();
        View contents = getView();
        switchStateView = (TextView)contents.findViewById(R.id.textView);
        mSwitch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switchStateView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                switchStateView.setText(databaseError.toString());
            }
        });
    }
}