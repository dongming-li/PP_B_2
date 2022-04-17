package com.warscreen.warscreen;

/**
 * Created by Isaac Holtkamp on 9/28/2017.
 */

public class buildings {

    private String name;
    private int cost;
    private int const_time;
    private int strength;
    private int destruction_time;

    public buildings(String name, int cost, int const_time, int strength, int destruction_time) {
        this.name = name;
        this.cost = cost;
        this.const_time = const_time;
        this.strength = strength;
        this.destruction_time = destruction_time;
    }

    public buildings(String name, int cost, int const_time){
        this.name = name;
        this.cost = cost;
        this.const_time = const_time;
        this.strength = 0;
        this.destruction_time = 0;
    }

    public String get_name(){
        return name;
    }

    public int get_cost(){
        return cost;
    }

    public int get_const_time(){return const_time;}

    public void set_const_time(int time){const_time = time;}

    public int get_strength(){
        return strength;
    }

    public int get_destruction_time(){
        return destruction_time;
    }
}
