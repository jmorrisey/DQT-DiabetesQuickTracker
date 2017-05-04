package com.dqt.jmorrisey.dqt.addbg;

import java.util.Date;
import java.sql.Time;

/**
 * Created by b3021318 on 31/03/2017.
 */

public class ReadingInformation {
    public String id;
    public String userid;
    public Double reading;
    public String timestamp;

    public ReadingInformation(){

    }


    public ReadingInformation(String id, String userid, Double reading, String timestamp){
        this.id = id;
        this.userid = userid;
        this.reading = reading;
        this.timestamp=timestamp;

    }
    public Double getReading(){
        return reading;
    }
    public String getTime(){
        return timestamp;
    }



}
