package com.dqt.jmorrisey.dqt.additems;

import android.app.DialogFragment;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.dqt.jmorrisey.dqt.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by b3021318 on 26/03/2017.
 */

public class AddItemFragment extends DialogFragment {
    private EditText etBarcode;
    private EditText etName;
    private EditText etCarbohydrates;
    private EditText etServingSize;
    private Button btnSave;



    DatabaseReference databaseFoodItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View is set to fragment layout and title of dialog is set.
        View rootView = inflater.inflate(R.layout.addfood_layout, container, false);
        getDialog().setTitle("Add Food");

        // Database reference to firebase fooditems
        databaseFoodItems = FirebaseDatabase.getInstance().getReference("fooditems");

        // setting location of edit texts and buttons
        etBarcode = (EditText) rootView.findViewById(R.id.etBarcode);

        etName = (EditText) rootView.findViewById(R.id.etName);

        etCarbohydrates = (EditText) rootView.findViewById(R.id.etCarbohydrates);

        etServingSize = (EditText) rootView.findViewById(R.id.etServingSize);

        btnSave = (Button) rootView.findViewById(R.id.buttonSave);

        // OnClick add a new food or drink item to the database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                addFood();
            }
        });
        return rootView;
    }

    private void addFood() {

        // Sets the values from edittext values
        String barcode = etBarcode.getText().toString().trim();

        String name = etName.getText().toString().trim();

        Double carbs = null;
        String temp3 = etCarbohydrates.getText().toString().trim();
        if(!temp3.isEmpty())
            try{
                carbs = Double.parseDouble(temp3);
            }catch (Exception e1){
                e1.printStackTrace();
            }

        Double servingsize = null;
        String temp5 = etServingSize.getText().toString().trim();
        if(!temp5.isEmpty())
            try{
                servingsize = Double.parseDouble(temp5);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        if(!TextUtils.isEmpty(barcode)){
            String id = barcode;
            ItemInformation foodInfo = new ItemInformation(id, barcode, name, carbs, servingsize);
            // Child the database reference with the current users id then set value to foodInfo
            databaseFoodItems.child(id).setValue(foodInfo);

            Toast.makeText(getActivity(), "Item Added Succesfully ", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Please enter all details", Toast.LENGTH_LONG).show();
        }

    }


}
