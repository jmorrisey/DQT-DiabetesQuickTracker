package com.dqt.jmorrisey.dqt.searchitems;

import android.app.AlertDialog;
import android.app.DialogFragment;


import android.content.DialogInterface;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.additems.ItemInformation;
import com.dqt.jmorrisey.dqt.additems.UserInformation;
import com.dqt.jmorrisey.dqt.records.RecordInfo;
import com.dqt.jmorrisey.dqt.registration.RegisterDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by b3021318 on 26/03/2017.
 */

public class SearchFood extends DialogFragment {
    View myView;
    private ListView listViewFood;
    DatabaseReference databaseFood;
    DatabaseReference databaseRecord;
    DatabaseReference databaseUserData;
    EditText etSearch;
    private ArrayAdapter<ItemInformation> listAdapter;
    private ArrayAdapter<RegisterDetails> registerAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String userDetails;
    Double icr;




    List<ItemInformation> itemInformationList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_search_food, container, false);
        getDialog().setTitle("Add Reading");

        databaseFood = FirebaseDatabase.getInstance().getReference("fooditems");

        //Gets the record reference
        databaseRecord = FirebaseDatabase.getInstance().getReference("users");

        databaseUserData = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference firebaseCurrentUserRef = databaseUserData.child(firebaseUser.getUid());


        firebaseCurrentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<UserInformation>> t = new GenericTypeIndicator<ArrayList<UserInformation>>(){};
                UserInformation userInfo = new UserInformation();
                userInfo = dataSnapshot.getValue(UserInformation.class);
                //Gets the current users icr
                icr = userInfo.getICR();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listViewFood = (ListView) myView.findViewById(R.id.ListViewFood);
        listViewFood.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewFood.setSelector(R.color.pressed_color);
        itemInformationList = new ArrayList<>();

        return myView;
    }



    @Override
    public void onStart(){
        super.onStart();

        databaseFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemInformationList.clear();

                for(DataSnapshot foodSnapShot: dataSnapshot.getChildren()){
                    ItemInformation foodInfo = foodSnapShot.getValue(ItemInformation.class);

                    itemInformationList.add(foodInfo);
                }

                FoodList adapter = new FoodList(getActivity(), itemInformationList);
                listViewFood.setAdapter(adapter);
                listViewFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ItemInformation itemInformation = (ItemInformation) parent.getItemAtPosition(position);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String currentTime = sdf.format(new Date());



                        //Calculates how much insulin required for that particular item.
                        final String user = firebaseUser.getUid().toString();
                        final String name = itemInformation.getName();
                        final Double carbohydrates = itemInformation.getCarbs();
                        final Double amount = carbohydrates/((icr)*10);
                        final Double serving = itemInformation.getServing();
                        final String time = currentTime;

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Add " + name + " to daily intake?")
                                .setMessage("Insulin shots required: " + amount)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //add all the code for new table
                                        RecordInfo recordInfo = new RecordInfo(user, name, carbohydrates, amount, serving, time);
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("records");
                                        myRef.push().setValue(recordInfo);

                                        dialog.dismiss();


                                    }
                                })

                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }









}
