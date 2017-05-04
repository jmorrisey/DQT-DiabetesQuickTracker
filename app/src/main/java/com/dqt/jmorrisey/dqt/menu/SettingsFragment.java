package com.dqt.jmorrisey.dqt.menu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.barcode.ScanBarcodeActivity;
import com.dqt.jmorrisey.dqt.registration.RegisterDetails;

/**
 * Created by b3021318 on 09/02/2017.
 */

public class SettingsFragment extends Fragment {
    View myView;
    Button logoutButton;
    Button editButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.settings_layout, container, false);


        logoutButton = (Button) myView.findViewById(R.id.buttonLogout);

        editButton = (Button) myView.findViewById(R.id.buttonEditDetails);
        // On editbutton press take the user to the register details page to edit details.
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterDetails.class);
                startActivity(intent);
            }
        });

        return myView;




    }

}
