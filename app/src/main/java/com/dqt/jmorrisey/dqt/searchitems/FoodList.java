package com.dqt.jmorrisey.dqt.searchitems;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.additems.ItemInformation;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by b3021318 on 10/04/2017.
 */

public class FoodList extends ArrayAdapter<ItemInformation> {
    private Activity context;
    private List<ItemInformation> fiList;

    public FoodList(Activity context, List<ItemInformation> fiList){
        super (context, R.layout.foodlistlayout, fiList);
        this.context=context;
        this.fiList=fiList;
        DecimalFormat form = new DecimalFormat("0.00");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewFood = inflater.inflate(R.layout.foodlistlayout,null,true);

        TextView textViewName = (TextView) listViewFood.findViewById(R.id.textViewName);

        TextView textViewCarbs = (TextView) listViewFood.findViewById(R.id.textViewCarbs);

        TextView textViewServing = (TextView) listViewFood.findViewById(R.id.textViewServing);

        ItemInformation foodInfo = fiList.get(position);

        textViewName.setText(foodInfo.getName());

        textViewCarbs.setText(String.valueOf(String.format("%.0f",foodInfo.getCarbs())));

        textViewServing.setText(String.valueOf(String.format("%.0f",foodInfo.getServing())));

        return listViewFood;


    }
}
