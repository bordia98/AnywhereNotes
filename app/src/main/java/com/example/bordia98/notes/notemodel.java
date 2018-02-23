package com.example.bordia98.notes;

/**
 * Created by bordia98 on 23/2/18.
 */

public class notemodel {
    public String notetitle;
    public String notetime;

    public notemodel(){

    }

    public notemodel(String notetitle,String notetime){
        this.notetitle=notetitle;
        this.notetime=notetime;
    }

    public String getNotetitle(){
        return notetitle;
    }
    public String getNotetime(){
        return notetime;
    }
    public void setnotetitle(String title){
        this.notetitle=title;
    }

    public void setnotetime(String time){
        this.notetime=time;
    }
}
