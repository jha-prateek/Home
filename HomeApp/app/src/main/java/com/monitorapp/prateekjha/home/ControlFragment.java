package com.monitorapp.prateekjha.home;


import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static android.content.Context.VIBRATOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ControlFragment extends Fragment {

    MqttAndroidClient client;
    String CLIENT_ID = "things";
    final String ADAFRUIT_HOST = "tcp://m21.cloudmqtt.com:16923";
    final String USERNAME = "automation";
    final String PASSWORD = "automation";
    final String TOPIC = "/data";
    private EditText getMessage;
    private TextView displayMessage;
    private TextView connection;
    private Button publish;
    private Vibrator vibrator;


    public ControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Home - Control");

        View root = inflater.inflate(R.layout.fragment_control, container, false);

        publish = (Button)root.findViewById(R.id.button);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMessage = (EditText)getView().findViewById(R.id.editText);
                String message = getMessage.getText().toString();
                if(client.isConnected()){
                    try {
                        client.publish(TOPIC, message.getBytes(), 0, false);
                        Log.d("TAG", "Published");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onStart(){
        super.onStart();

        View contents = getView();
        displayMessage = (TextView)contents.findViewById(R.id.textView);
        connection = (TextView)contents.findViewById(R.id.textView2);
        vibrator = (Vibrator)getActivity().getSystemService(VIBRATOR_SERVICE);

        CLIENT_ID = CLIENT_ID + System.currentTimeMillis();
        client = new MqttAndroidClient(this.getActivity().getApplicationContext(), ADAFRUIT_HOST, CLIENT_ID);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    connection.setText("Connected");
                    // Subscribe to the Topic on Cloud
                    subscribeTopic();

                    Log.d("TAG", "Success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    connection.setText("Disonnected");
                    Log.d("TAG", "Failed");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                connection.setText("Disconnected");


            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                displayMessage.setText("Message Received:\n" + new String(message.getPayload()));
                connection.setText("Connected");
                vibrator.vibrate(300);
                Log.d("TAG", "Received");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void subscribeTopic(){
        try {
            client.subscribe(TOPIC,0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

}
