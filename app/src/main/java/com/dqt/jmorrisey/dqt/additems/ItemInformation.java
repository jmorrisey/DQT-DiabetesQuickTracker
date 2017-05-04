package com.dqt.jmorrisey.dqt.additems;

/**
 * Created by b3021318 on 27/03/2017.
 */

public class ItemInformation {
    public String id;
    public String barcode;
    public String name;
    public Double carbohydrates;
    public Double serving;

    public ItemInformation(){

    }

    public ItemInformation(String id, String barcode, String name, Double carbohydrates, Double serving){
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.carbohydrates = carbohydrates;
        this.serving = serving;
    }
    public String getBarcode(){
        return barcode;
    }
    public String getName(){
        return name;
    }
    public Double getCarbs(){
        return carbohydrates;
    }
    public Double getServing(){
        return serving;
    }
}
