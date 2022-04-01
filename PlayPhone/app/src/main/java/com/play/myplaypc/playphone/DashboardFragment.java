package com.play.myplaypc.playphone;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.*;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.EditText;
import org.eclipse.paho.client.mqttv3.*;
/**
 * Created by Belal on 1/23/2018.
 */

public class DashboardFragment extends Fragment {
    EditText t1;
    Button b1;
    static TextView log=null;
    TextView label;
    View view=null;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        if(view==null){ view=inflater.inflate(R.layout.fragment_dashboard, null);init();}

        return view;
    }
    public void init()
    {
        t1= (EditText) view.findViewById(R.id.t1);
        t1.setTextColor(Color.parseColor("gray"));
        b1= (Button) view.findViewById(R.id.b1);
        log= (TextView) view.findViewById(R.id.log);
        log.setMovementMethod(new ScrollingMovementMethod());
        log.setSelected(true);
        log.setTextColor(Color.parseColor("black"));
        log.setText(MainActivity.mqtt.sb);
        t1.setOnFocusChangeListener(new View.OnFocusChangeListener(){ public void onFocusChange(View view, boolean f){softText(f);}});
        b1.setOnClickListener(new View.OnClickListener(){ public void onClick(View view){sendMSG();}});
    }
    public void sendMSG()
    {

         String e=t1.getText().toString();
         t1.setText("");
         MainActivity.mqtt.sendMessage(e);
    }
    public void softText(boolean f)
    {
        String text = t1.getText().toString();
        if(f)
        {
            if (text.equals("Send Message"))
            {

                t1.setTextColor(Color.parseColor("black"));
                t1.setText("");
            }
        }
         else
        {
            if(text.equals("")){
            t1.setTextColor(Color.parseColor("gray"));
                String n="Send Message";
            t1.setText(n);}
        }
    }
    @Override
    public void onResume()
    {
        String text = t1.getText().toString();
        if(text.equals("")){
            t1.setTextColor(Color.parseColor("gray"));
            String n="Send Message";
            t1.setText(n);}
        super.onResume();
    }
    public void setTextt()
    {

    }
}
