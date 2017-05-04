package com.dqt.jmorrisey.dqt.mapsinsulin;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.menu.MainMenu;

/**
 * Created by b3021318 on 09/02/2017.
 */

public class InsulinLocator extends Fragment{
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.insulin_layout,container,false);
        return myView;

    }

}
