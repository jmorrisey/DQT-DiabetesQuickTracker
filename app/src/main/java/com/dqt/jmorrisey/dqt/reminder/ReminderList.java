package com.dqt.jmorrisey.dqt.reminder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dqt.jmorrisey.dqt.R;
import java.util.List;

/**
 * Created by b3021318 on 27/04/2017.
 */

public class ReminderList extends ArrayAdapter<ReminderInformation> {
    private Activity context;
    private List<ReminderInformation> remList;

    public ReminderList(Activity context, List<ReminderInformation> remList){
        super (context, R.layout.reminderlistlayout, remList);
        this.context=context;
        this.remList=remList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewReminders = inflater.inflate(R.layout.reminderlistlayout, null, true);

        // Sets the location of TextViews for displaying the list data back to the user.
        TextView textViewName = (TextView) listViewReminders.findViewById(R.id.textViewName);

        TextView textViewDescription = (TextView) listViewReminders.findViewById(R.id.textViewDescription);

        ReminderInformation reminderInformation = remList.get(position);

        textViewName.setText(reminderInformation.getName());

        textViewDescription.setText(reminderInformation.getDescription());

        return listViewReminders;
    }
}
