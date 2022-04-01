package com.play.myplaypc.playphone;
import org.eclipse.paho.android.service.*;
import org.eclipse.paho.client.mqttv3.*;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.*;
import android.support.v4.app.Fragment;
import android.util.*;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MY PlayPC on 26 Jan,2020 026.
 */

public class MqttHelper {
    public MqttAndroidClient client;

    final String serverUri = "tcp://soldier.cloudmqtt.com:17816";

    final String clientId = "ExampleAndroidClient";
    final String topic = "test";
    TextView status;
    final String username = "bwjmonhj";
    final String password = "fKKY2bpfZiZ2";
    MqttConnectOptions mqttConnectOptions;
    private final int qos = 1;
    Context context;
    boolean connection=false;
    int j=0;
    String recon[]={"Disconnected:trying to Connect","Disconnected:trying to Connect.","Disconnected:trying to Connect..","Disconnected:trying to Connect..."};
    public MqttHelper(Context context) {
        this.context=context;
        client = new MqttAndroidClient(context, serverUri, clientId);
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                processMsg( mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        TimerTask t1=new TimerTask() {
            @Override
            public void run() {
                connect();
            }
        };
        new Timer().scheduleAtFixedRate(t1,0l,1000l);
        connect();
    }

    public void setCallback(MqttCallbackExtended callback) {
        client.setCallback(callback);
    }

    private void connect() {
        if (false) {
            //disconnect();
            Log.e("mqttc","Internet not Avaliable");
            connection=false;
        }
        else {
            if(!connection) {
                Log.e("mqttc",recon[j]); if(j<3){j++;}else{j=0;}
                try {
                    if (!client.isConnected()) {
                        client.connect(mqttConnectOptions, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                                disconnectedBufferOptions.setBufferEnabled(true);
                                disconnectedBufferOptions.setBufferSize(100);
                                disconnectedBufferOptions.setPersistBuffer(false);
                                disconnectedBufferOptions.setDeleteOldestMessages(false);
                                client.setBufferOpts(disconnectedBufferOptions);
                                setText("Connected to: " + serverUri);
                                connection = true;
                                subscribeToTopic();
                                Log.e("mqttc","Connected");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                setText("Failed to connect to: " + serverUri + exception.toString());
                            }
                        });
                    }
                } catch (Exception ex) {
                    setText("Cannot connect due to ERRor:" + ex.getMessage());
                }
            }
        }
        Log.e("mqttc",String.valueOf(connection));
    }
    private void subscribeToTopic() {
        try {
            client.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    setText("Subscribed!");sendMessage("PlayPhone Online");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    setText("Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            setText("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public void sendMessage(String payload) {

        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        setText(payload);
        try {
            client.publish(this.topic, message);
        } catch (Exception e) {
            setText("message Cannot be sent due to ERRor:" + e.getMessage());
        }// Blocking publish

    }

    public void disconnect() {
        try {
            this.client.disconnect();
        } catch (Exception e) {
        }


    }
    StringBuilder sb=new StringBuilder("Logs");
    boolean log=true;
    public void processMsg(String msg) {
        if (msg.equals("ON"))
        {
            Intent i=context.getPackageManager().getLaunchIntentForPackage("ro.andreimircius.remotefingerauth");
            context.startActivity(i);
        }
        setText(msg);
    }
    public void setText(String newText)
    {
        sb.append("\n");
        sb.append(newText);
        if(DashboardFragment.log!=null)
        {
            DashboardFragment.log.setText(sb);
        }
    }
}