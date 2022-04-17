package com.warscreen.war_screen;

/**
 * Created by Isaac Holtkamp on 9/28/2017.
 * Super class with basic stats
 */


public class buildings {

    private String name;
    private int const_time;

    public buildings(String name, int const_time){
        this.name = name;
        this.const_time = const_time;
    }

    public String get_name(){
        return name;
    }

    public int get_const_time(){return const_time;}

    public void set_const_time(int time){const_time = time;}
}
