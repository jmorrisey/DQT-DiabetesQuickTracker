package com.dqt.jmorrisey.dqt.records;

import java.util.Date;

/**
 * Created by james on 19/04/2017.
 */

public class RecordInfo {
    public String user;
    public String name;
    public Double carbohydrates;
    public Double amount;
    public Double serving;
    public String time;

    public RecordInfo(){

    }

    public RecordInfo(String user, String name, Double carbohydrates, Double amount, Double serving, String time){
        this.user = user;
        this.name = name;
        this.carbohydrates = carbohydrates;
        this.amount=amount;
        this.serving = serving;
        this.time=time;
    }
    public String getName(){
        return name;
    }
    public Double getCarbs(){
        return carbohydrates;
    }
    public Double getAmount(){
        return amount;
    }


    public Double getServing(){
        return serving;
    }
    public String getTime(){
        return time;
    }
    public String getUser(){
        return user;
    }
}


