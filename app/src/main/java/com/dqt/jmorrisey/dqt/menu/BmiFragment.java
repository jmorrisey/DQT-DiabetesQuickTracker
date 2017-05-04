package com.dqt.jmorrisey.dqt.menu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dqt.jmorrisey.dqt.R;

/**
 * Created by b3021318 on 09/02/2017.
 */

public class BmiFragment extends Fragment{
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bmi_layout, container, false);

        // Initialise variables required for calculations
        final Button calculateBtn = (Button)rootView.findViewById(R.id.button_calc);
        final EditText field_weight = (EditText)rootView.findViewById(R.id.field_weight);
        final EditText field_height = (EditText)rootView.findViewById(R.id.field_height);
        final TextView view_result = (TextView)rootView.findViewById(R.id.view_result);
        final TextView message = (TextView)rootView.findViewById(R.id.view_msg);

        calculateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // On click calculates bmi based on edit text values.
                double weight;
                double height;
                double bmi;
                String msg="";

                weight = Double.parseDouble(field_weight.getText().toString());
                height = Double.parseDouble(field_height.getText().toString());

                bmi = weight/height;
                bmi = bmi/height;

                view_result.setText(String.valueOf(bmi));

                if(bmi < 18.5){
                    msg= "Underweight";
                }else if(bmi >= 18.5 && bmi < 25){
                    msg= "Healthy Weight";
                }else if(bmi>=25){
                    msg="Overweight";
                }else if (bmi>=30){
                    msg="Obese";
                }
                // Sets the message depending on the number returned from bmi.
                message.setText(msg);

            }
        });



        return rootView;
    }






}

