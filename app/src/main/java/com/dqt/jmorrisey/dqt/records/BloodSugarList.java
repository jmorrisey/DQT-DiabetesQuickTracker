package com.dqt.jmorrisey.dqt.records;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.addbg.ReadingInformation;

import java.util.List;

/**
 * Created by james on 20/04/2017.
 */

public class BloodSugarList extends ArrayAdapter<ReadingInformation> {
    private Activity context;
    private List<ReadingInformation> bgList;

    public BloodSugarList(Activity context, List<ReadingInformation> bgList){
        super (context, R.layout.bloodsugarlist, bgList);
        this.context=context;
        this.bgList=bgList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewBG = inflater.inflate(R.layout.bloodsugarlist, null, true);

        //Sets the textviews to store the info
        TextView textViewReading = (TextView) listViewBG.findViewById(R.id.textViewReading);

        TextView textViewTime = (TextView) listViewBG.findViewById(R.id.textViewTime);


        ReadingInformation readingInformation = bgList.get(position);
        // Sets the text and formats it
        textViewReading.setText(String.valueOf(String.format("%.0f", readingInformation.getReading())));

        textViewTime.setText(String.valueOf(readingInformation.getTime()));



        return listViewBG;
    }
}
