package com.dqt.jmorrisey.dqt.leaderboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.additems.UserInformation;
import com.dqt.jmorrisey.dqt.registration.RegisterDetails;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by b3021318 on 09/02/2017.
 */

public class LeaderboardFragment extends Fragment{
    View myView;
    private ListView listViewUsers;
    DatabaseReference databaseUsers;

    List<UserInformation> userInformationList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Sets the layout for the leaderboard and initialises listview.
        myView= inflater.inflate(R.layout.leaderboard_layout,container,false);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        listViewUsers = (ListView) myView.findViewById(R.id.listViewUsers);

        userInformationList = new ArrayList<>();


        return myView;
    }

    @Override
    public void onStart(){
        super.onStart();
        // addValueEventListener to look for data from the users table in database
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clears the list when data is changed so data is real time
                userInformationList.clear();

                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    UserInformation userInfo = userSnapshot.getValue(UserInformation.class);

                    userInformationList.add(userInfo);
                }

                LeaderboardList adapter = new LeaderboardList(getActivity(), userInformationList);
                listViewUsers.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
