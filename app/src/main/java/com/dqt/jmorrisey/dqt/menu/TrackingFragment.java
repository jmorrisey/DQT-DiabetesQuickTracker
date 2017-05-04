package com.dqt.jmorrisey.dqt.menu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.addbg.ReadingInformation;
import com.dqt.jmorrisey.dqt.records.BloodSugarList;
import com.dqt.jmorrisey.dqt.records.RecordList;
import com.dqt.jmorrisey.dqt.records.RecordInfo;
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

public class TrackingFragment extends Fragment{
    View myView;
    private ListView listViewRecords;
    private ListView listViewBG;
    DatabaseReference databaseListRecords;
    DatabaseReference databaseListBG;
    DatabaseReference databaseRecord;
    DatabaseReference databaseUserData;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    List<RecordInfo> recordInfoList;
    List<ReadingInformation> readingInformationList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.fragment_tracking,container,false);

        // Gets the reference for the records database
        databaseListRecords = FirebaseDatabase.getInstance().getReference("records");

        // Gets the reference for the bgreadings table in firebase
        databaseListBG = FirebaseDatabase.getInstance().getReference("bgreadings");

        // Gets users table reference from firebase
        databaseRecord = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve current authorised user
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        // Acts as an inner join between the users and records
        DatabaseReference firebaseCurrenUserRef = databaseRecord.child(firebaseUser.getUid()).push();


        // Listener to retrieve record info data from firebase
        firebaseCurrenUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<RecordInfo>> t = new GenericTypeIndicator<ArrayList<RecordInfo>>() {};
                RecordInfo recordInfo = new RecordInfo();

                recordInfo = dataSnapshot.getValue(RecordInfo.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });

        // Initialising the listview sets a choide to single.
        listViewRecords = (ListView) myView.findViewById(R.id.ListViewRecords);
        listViewRecords.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        recordInfoList = new ArrayList<>();

        listViewBG = (ListView) myView.findViewById(R.id.ListViewBG);
        listViewBG.setChoiceMode(ListView.CHOICE_MODE_NONE);

        readingInformationList= new ArrayList<>();
        return myView;
    }

    @Override
    public void onStart(){
        super.onStart();
        // Listener for blood sugar database to retrieve data for listview
        databaseListBG.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                readingInformationList.clear();

                for(DataSnapshot bgSnapShot: dataSnapshot.getChildren()){


                    ReadingInformation readingInformation = bgSnapShot.getValue(ReadingInformation.class);

                    readingInformationList.add(readingInformation);
                }
                BloodSugarList adapterBG = new BloodSugarList(getActivity(), readingInformationList);
                listViewBG.setAdapter(adapterBG);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseListRecords.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recordInfoList.clear();
            // Gets all children for dataSnapshot in records database.
            for(DataSnapshot recordSnapShot: dataSnapshot.getChildren()){



                RecordInfo recordInfo = recordSnapShot.getValue(RecordInfo.class);

                recordInfoList.add(recordInfo);
            }

                RecordList adapter = new RecordList(getActivity(), recordInfoList);
                listViewRecords.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
