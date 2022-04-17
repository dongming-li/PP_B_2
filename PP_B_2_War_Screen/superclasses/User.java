package com.warscreen.war_screen;

import java.util.ArrayList;

/**
 * Created by Isaac Holtkamp on 10/9/2017.
 */

public class User {
    String name;
    int id;
    double tot_gold;
    int tot_manpower;
    ArrayList<army> tot_armies;

    public User(String name, int id, double tot_gold, int tot_manpower, ArrayList<army> tot_armies){
        this.name = name;
        this.id = id;
        this.tot_gold = tot_gold;
        this.tot_manpower = tot_manpower;
        this.tot_armies = tot_armies;
    }

    public int get_ID(){
        return id;
    }

    public double get_gold(){
        return tot_gold;
    }

    public void set_gold(double gold){
        this.tot_gold = gold;
    }

    public int get_Tot_manpower(){
        return tot_manpower;
    }

    public void set_tot_manpower(int tot_manpower){
        this.tot_manpower = tot_manpower;
    }

    public ArrayList<army> get_tot_armies(){return tot_armies;}

    public void set_tot_armies(ArrayList<army> tot_armies){this.tot_armies = tot_armies;}

    public void add_army(army a){tot_armies.add(a);}

}
