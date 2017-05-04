package com.dqt.jmorrisey.dqt.records;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dqt.jmorrisey.dqt.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by james on 20/04/2017.
 */

public class RecordList extends ArrayAdapter<RecordInfo> {
    private Activity context;
    private List<RecordInfo> recList;

    public RecordList(Activity context, List<RecordInfo> recList){
        super (context, R.layout.recordlistlayout, recList);
        this.context=context;
        this.recList=recList;
        DecimalFormat form = new DecimalFormat("0.00");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewRecords = inflater.inflate(R.layout.recordlistlayout, null, true);

        TextView textViewName = (TextView) listViewRecords.findViewById(R.id.textViewName);

        TextView textViewCarbs = (TextView) listViewRecords.findViewById(R.id.textViewCarbs);

        TextView textViewInsulin = (TextView) listViewRecords.findViewById(R.id.textViewInsulin);

        TextView textViewServing = (TextView) listViewRecords.findViewById(R.id.textViewServing);

        TextView textViewTime = (TextView) listViewRecords.findViewById(R.id.textViewTime);

        RecordInfo recordInfo = recList.get(position);

        textViewName.setText(recordInfo.getName());

        textViewCarbs.setText(String.valueOf(String.format("%.0f",recordInfo.getCarbs())));

        textViewInsulin.setText(String.valueOf(String.format("%.0f",recordInfo.getAmount())));

        textViewServing.setText(String.valueOf(String.format("%.0f",recordInfo.getServing())));

        textViewTime.setText(String.valueOf(recordInfo.getTime()));


        return listViewRecords;
    }
}
