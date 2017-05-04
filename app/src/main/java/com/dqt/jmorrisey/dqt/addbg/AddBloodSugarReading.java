package com.dqt.jmorrisey.dqt.addbg;

import android.app.DialogFragment;


import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.dqt.jmorrisey.dqt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by b3021318 on 26/03/2017.
 */

public class AddBloodSugarReading extends DialogFragment {
    private EditText etBGReading;
    private Button btnSave;


    FirebaseAuth firebaseAuth;
    DatabaseReference databaseBGItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View is set to fragment layout and title of dialog is set.
        View rootView = inflater.inflate(R.layout.fragment_add_blood_sugar_reading, container, false);
        getDialog().setTitle("Add Reading");

        // Database reference to firebase bgreadings
        databaseBGItems = FirebaseDatabase.getInstance().getReference("bgreadings");

        // setting location of edit texts and buttons
        etBGReading = (EditText) rootView.findViewById(R.id.etBG);
        btnSave = (Button) rootView.findViewById(R.id.buttonSave);

        // Gets the current instance of authorisation
        firebaseAuth = FirebaseAuth.getInstance();

        // OnClick add a new reading to the database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                addReading();
            }
        });
        return rootView;
    }


    private void addReading() {
        // Setting a date format for upload to the database format is 11:30
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // Sets the current time when user adds a reading and converts to string
        String currentTime = sdf.format(new Date());
        // Gets the current active user on the application
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // Pushes value stored as id for the JSON tree
        String id = databaseBGItems.push().getKey();
        // Getting current user ID and sets all values for upload to firebase.
        String userid = user.getUid();
        Double reading = null;
        String temp = etBGReading.getText().toString().trim();
        if(!temp.isEmpty())
            try{
                reading = Double.parseDouble(temp);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        String date = currentTime;

        if(!TextUtils.isEmpty(id)){
            ReadingInformation bgInfo = new ReadingInformation(id, userid, reading, date);

            databaseBGItems.child(id).setValue(bgInfo);

            Toast.makeText(getActivity(), "Blood Sugar reading successful", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Please enter all details", Toast.LENGTH_LONG).show();
        }

    }


}
