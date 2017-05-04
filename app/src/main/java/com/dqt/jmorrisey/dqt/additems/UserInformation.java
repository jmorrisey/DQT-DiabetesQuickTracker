package com.dqt.jmorrisey.dqt.additems;

import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.ui.auth.ui.User;

/**
 * Created by b3021318 on 25/03/2017.
 */

public class UserInformation {
    public String id;
    public String username;
    public String age;
    public String type;
    public String insulin;
    public Double icr;
    public Double targetbg;
    public Integer points;

    public UserInformation(){

    }

    public UserInformation(String id, String username, String age, String type, String insulin, Double icr, Double targetbg, Integer points){
        this.id = id;
        this.username = username;
        this.age = age;
        this.type = type;
        this.insulin = insulin;
        this.icr = icr;
        this.targetbg = targetbg;
        this.points = points;
    }

    public String getUsername(){
        return username;
    }
    public Integer getPoints(){
        return points;
    }
    public Double getICR(){
        return icr;
    }


}
