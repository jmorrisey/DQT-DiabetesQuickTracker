package com.dqt.jmorrisey.dqt.leaderboard;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.additems.UserInformation;
import com.firebase.ui.auth.ui.User;

import java.util.List;

/**
 * Created by b3021318 on 10/04/2017.
 */

public class LeaderboardList extends ArrayAdapter<UserInformation> {

    private Activity context;
    private List<UserInformation> lbList;



    public LeaderboardList(Activity context, List<UserInformation> lbList){

        super(context, R.layout.listlayout, lbList);

        this.context = context;
        this.lbList = lbList;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Creates the custom list and pulls contents of the list from UserInformation model
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listlayout,null,true);


        TextView textViewUser = (TextView) listViewItem.findViewById(R.id.textViewUser);
        TextView textViewPoints = (TextView) listViewItem.findViewById(R.id.textViewPoints);

        UserInformation userInfo = lbList.get(position);

        textViewUser.setText(userInfo.getUsername());
        textViewPoints.setText(String.valueOf(userInfo.getPoints())) ;

        return listViewItem;
    }

}