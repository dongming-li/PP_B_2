package com.example.myfirstapp;

import java.util.ArrayList;

/**
 * Created by Isaac Holtkamp on 9/29/2017.
 */

public class Hex_data {

    private int ID;
    private army hex_army;
    private double slow;
    private int bonus_defense;
    private User user;//player in control of hex
    private double GPT;//gold per turn
    private int MPT;//manpower per turn
    private boolean buildings[] = new boolean[6];
    private int build_time[] = new int[6];

    public Hex_data(int ID, army hex_army, double slow, double GPT, int MPT, boolean buildings[], User user){
        this.ID = ID;
        this.hex_army = hex_army;
        // display army stats in hexes
        this.slow = slow;
        this.GPT = GPT;
        this.MPT = MPT;
        this.user = user;
        this.buildings = buildings;
    }

    public army select_move(int num_swords, int num_horses, int num_archers){
        army moving = new army(null, null, null, ID, hex_army.get_time_on_hex());
        for(int i = 0; i < num_swords; i++){
            moving.add_sword(hex_army.get_sword_at(0),i);
            moving.remove_swords(0);
        }
        for(int i = 0; i < num_horses; i++){
            moving.add_horses(hex_army.get_horse_at(0),i);
            moving.remove_horse(0);
        }
        for(int i = 0; i < num_archers; i++){
            moving.add_archers(hex_army.get_archer_at(0),i);
            moving.remove_archer(0);
        }
        return moving;
    }

    public int[] get_build_time(){return build_time;}

    public void set_build_time(int[] build_time){this.build_time = build_time;}

    public boolean[] get_buildings(){return buildings;}

    public void set_buildings(boolean[] buildings){this.buildings = buildings;}

    public int move_time(army moving_army){
        return (int) (moving_army.army_speed() * slow);
    }

    public int get_ID(){return ID;}

    public User get_User(){return user;}

    public void set_User(User user){
        this.user = user;
    }

    public double get_GPT(){return GPT;}

    public void set_GPT(double GPT){this.GPT = GPT;}

    public int get_MPT(){return MPT;}

    public void set_MPT(int GPT){this.MPT = MPT;}


    public double find_bonus_defense(){
        if(hex_army.get_time_on_hex() < 100){
            return ((hex_army.get_time_on_hex()/100) * bonus_defense);
        }
        return bonus_defense;
    }

    public army getHex_army() {return hex_army;}

    public ArrayList<units> get_swords(){return hex_army.get_swords();}

    public ArrayList<units> get_archers(){return hex_army.get_archers();}

    public ArrayList<units> get_horses(){
        return hex_army.get_horses();
    }

    public void add_army(army a){
        for(units u : a.get_swords()){
            hex_army.add_sword(u, 0);
        }
        for(units u : a.get_horses()){
            hex_army.add_horses(u, 0);
        }
        for(units u : a.get_archers()){
            hex_army.add_archers(u, 0);
        }
        hex_army.reorder_forces();
    }
}
