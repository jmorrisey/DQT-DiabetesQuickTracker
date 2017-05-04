package com.dqt.jmorrisey.dqt.reminder;

/**
 * Created by b3021318 on 27/04/2017.
 */

public class ReminderInformation {
    public String id;
    public String name;
    public String description;

    public ReminderInformation(){

    }


    public ReminderInformation(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;


    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }



}


