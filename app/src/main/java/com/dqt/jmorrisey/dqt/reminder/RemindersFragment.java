package com.dqt.jmorrisey.dqt.reminder;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dqt.jmorrisey.dqt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by b3021318 on 09/02/2017.
 */

public class RemindersFragment extends Fragment{


    // Initialise variables
    private ListView listViewReminders;
    private EditText etName;
    private EditText etDesc;
    private Button btnSave;

    DatabaseReference databaseReminderItems;
    DatabaseReference databaseUserData;
    DatabaseReference firebaseCurrentUserRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String uid;

    // Creating a custom list on ReminderInformation
    List<ReminderInformation> reminderInformationList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Sets rootView layout to that of reminder_layout
        View rootView = inflater.inflate(R.layout.reminder_layout, container, false);


        databaseReminderItems = FirebaseDatabase.getInstance().getReference("reminders");
        databaseUserData = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseCurrentUserRef = databaseUserData.child(firebaseUser.getUid());

        // Sets values to data retrieved from widgets
        uid = (String) firebaseAuth.getCurrentUser().getUid();

        etName = (EditText) rootView.findViewById(R.id.etName);

        etDesc = (EditText) rootView.findViewById(R.id.etDesc);

        btnSave = (Button) rootView.findViewById(R.id.buttonSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                addReminder();
            }
        });

        // Listening for information from the users table in firebase
        firebaseCurrentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<ReminderInformation>> t = new GenericTypeIndicator<ArrayList<ReminderInformation>>() {};
                ReminderInformation reminderInformation = new ReminderInformation();

                reminderInformation = dataSnapshot.getValue(ReminderInformation.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });


        listViewReminders = (ListView) rootView.findViewById(R.id.listViewReminders);
        listViewReminders.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        reminderInformationList = new ArrayList<>();
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        databaseReminderItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reminderInformationList.clear();

                for(DataSnapshot reminderSnapShot: dataSnapshot.getChildren()){
                    ReminderInformation reminderInformation = reminderSnapShot.getValue(ReminderInformation.class);
                    reminderInformationList.add(reminderInformation);
                }
                ReminderList adapterRL = new ReminderList(getActivity(), reminderInformationList);
                listViewReminders.setAdapter(adapterRL);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void GenerateKey() {
        String mKey = databaseReminderItems.push().getKey();
        if(mKey.equalsIgnoreCase(mKey)){
            mKey = databaseReminderItems.push().getKey();
        }
    }

    private void addReminder() {
        // This method is used to collect the data from users input and then upload this data to the firebase database.
        String name = etName.getText().toString().trim();

        String description = etDesc.getText().toString().trim();

        String id = uid;
        if(!TextUtils.isEmpty(uid)){
            ReminderInformation reminderInformation = new ReminderInformation(id, name, description);

            databaseReminderItems.push().setValue(reminderInformation);

            Toast.makeText(getActivity(), "Reminder added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Please enter all details", Toast.LENGTH_LONG).show();
        }

    }

}
