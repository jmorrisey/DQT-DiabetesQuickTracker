package com.dqt.jmorrisey.dqt.registration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.additems.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterDetails extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername;
    private Spinner spinnerAge;
    private Spinner spinnerDiabetesType;
    private Spinner spinnerInsulinTherapy;
    private EditText etInsulintoCarbRatio;
    private EditText etTargetBG;
    private Button btnRegisterButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        firebaseAuth = FirebaseAuth.getInstance();


        // Sets the values in the array to 1 to 100 for age
        List age = new ArrayList<Integer>();
        for (int i = 1; i <= 100; i++) {
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        // Sets the location of all text fields in the layout file
        spinnerAge = (Spinner)findViewById(R.id.spinnerAge);
        spinnerAge.setAdapter(spinnerArrayAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        etUsername = (EditText) findViewById(R.id.etUsername);
        spinnerDiabetesType = (Spinner) findViewById(R.id.spinnerDiabetesType);
        ArrayAdapter<CharSequence> adapterdt = ArrayAdapter.createFromResource(this, R.array.diabetestype, android.R.layout.simple_spinner_item);
        spinnerDiabetesType.setAdapter(adapterdt);
        spinnerInsulinTherapy = (Spinner) findViewById(R.id.spinnerInsulinTherapy);
        ArrayAdapter<CharSequence> adapterit = ArrayAdapter.createFromResource(this, R.array.insulinrequired, android.R.layout.simple_spinner_item);
        spinnerInsulinTherapy.setAdapter(adapterit);
        etInsulintoCarbRatio = (EditText) findViewById(R.id.etInsulintoCarbRatio);
        etTargetBG = (EditText) findViewById(R.id.etTargetBG);
        btnRegisterButton = (Button) findViewById(R.id.buttonRegister);

        btnRegisterButton.setOnClickListener(this);

    }
    private void saveUserInfo(){
        // Saves the user info underneath a users unique id.
        String id = databaseReference.push().getKey();
        String username = etUsername.getText().toString().trim();
        String age = spinnerAge.getSelectedItem().toString().trim();
        String type = spinnerDiabetesType.getSelectedItem().toString().trim();
        String insulin = spinnerInsulinTherapy.getSelectedItem().toString().trim();
        Integer points = 0;

        Double icr = null;
            String temp = etInsulintoCarbRatio.getText().toString().trim();
        if(!temp.isEmpty())
            try{
                icr = Double.parseDouble(temp);
            }catch (Exception e1){
                e1.printStackTrace();
            }

        Double targetbg = null;
        String temp2 = etTargetBG.getText().toString().trim();
        if(!temp2.isEmpty())
            try{
                targetbg = Double.parseDouble(temp2);
            }catch (Exception e1){
                e1.printStackTrace();
            }

        UserInformation userInfo = new UserInformation(id,username,age,type,insulin,icr,targetbg,points);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInfo);

        Toast.makeText(this, "Information Saved " + ("\ud83d\ude01"), Toast.LENGTH_LONG).show();




    }

    @Override
    public void onClick(View view) {
        if(view == btnRegisterButton){
            saveUserInfo();

        }

    }
}
