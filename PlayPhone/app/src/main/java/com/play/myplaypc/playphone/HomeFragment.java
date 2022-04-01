package com.play.myplaypc.playphone;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Belal on 1/23/2018.
 */

public class HomeFragment extends Fragment {


    Button b1,b2;
    View view=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        if(view==null){  view=inflater.inflate(R.layout.fragment_home, null);;init();}

        return view;
    }
    public void init()
    {
        b1=(Button)view.findViewById(R.id.b1);
        b2=(Button)view.findViewById(R.id.b2);
        b1.setOnClickListener(new View.OnClickListener(){ public void onClick(View view){MainActivity.mqtt.sendMessage("ON");}});
        b2.setOnClickListener(new View.OnClickListener(){ public void onClick(View view){confirm();}});


    }
    public void confirm()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        MainActivity.mqtt.sendMessage("OFF");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
