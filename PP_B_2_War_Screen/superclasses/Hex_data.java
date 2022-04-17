package com.warscreen.war_screen;

import java.util.ArrayList;

/**
 * Created by Isaac Holtkamp on 9/29/2017.
 */

public class Hex_data {

    private int ID; //Hex location in array
    private army hex_army;
    private double slow; //Multiplier for army transport time
    private int bonus_defense;
    private User user;//player in control of hex
    private double GPT;//gold per turn
    private int MPT;//manpower per turn
    private ArrayList<buildings> build;
    /*
     *Used for naming
     */
    private int swords_made;
    private int archers_made;
    private int horses_made;
    /*
     *Units building not included in combat or moving
     */
    private ArrayList<swords> swords_building;
    private ArrayList<archers> archers_building;
    private ArrayList<horses> horses_building;

    public Hex_data(int ID, army hex_army, double slow, double GPT, int MPT, ArrayList<buildings> buildings, User user) {
        this.ID = ID;
        this.hex_army = hex_army;
        this.slow = slow;
        this.GPT = GPT;
        this.MPT = MPT;
        this.user = user;
        this.build = buildings;
        swords_made = 0;
        archers_made = 0;
        horses_made = 0;

    }

    //Once unit has been constructed remove it from the list
    public void remove_built_unit(units u){
       if(u.getClass() == swords.class){
           swords_building.remove(u);
       }
       else if(u.getClass() == archers.class){
           archers_building.remove(u);
       }
       else if(u.getClass() == horses.class){
           horses_building.remove(u);
       }
    }

    //Remove units from Hex to be moved. Not included in combat
    public army select_move(int num_swords, int num_horses, int num_archers){
        army moving = new army(null, null, null, ID, hex_army.get_time_on_hex());
        for(int i = 0; i < num_swords; i++){
            if(hex_army.get_swords() == null){
                break;
            }
            moving.add_sword(hex_army.get_sword_at(0),i);
            hex_army.remove_swords(0);
        }
        for(int i = 0; i < num_horses; i++){
            if(hex_army.get_archers() == null){
                break;
            }
            moving.add_horses(hex_army.get_horse_at(0),i);
            hex_army.remove_horse(0);
        }
        for(int i = 0; i < num_archers; i++){
            if(hex_army.get_horses() == null){
                break;
            }
            moving.add_archers(hex_army.get_archer_at(0),i);
            hex_army.remove_archer(0);
        }
        return moving;
    }

    //How long itll take to move to next hex
    public int move_time(army moving_army){
        return (int) (moving_army.get_max_speed() * slow);
    }

    //Set data for moving
    public void move_to(army a, Hex_data too){
        a.set_is_moving(true);
        a.set_move_time(move_time(a));
        a.set_move_to_ID(too.ID);
    }

    public void make_fort(){
        if(user.get_gold() >= 25) {
            user.set_gold(user.get_gold() - 25);
            fort f = new fort("Fort" + ID, 25);
            build.add(f);
        }
    }

    public void make_market(){
        if(user.get_gold() >= 15) {
            user.set_gold(user.get_gold() - 15);
            market m = new market("Market" + ID, 15);
            build.add(m);
        }
    }

    public void make_barrack(){
        if(user.get_gold() >= 15) {
            user.set_gold(user.get_gold() - 15);
            barrack m = new barrack("Barrack" + ID, 15);
            build.add(m);
        }
    }

    public ArrayList<buildings> get_buildings(){return build;}

    public void make_sword() {
        if (10 < user.get_gold() && user.get_Tot_manpower() >= 1000) {
            swords s = new swords(ID + "sword" + swords_made, 10, 10, 10, 10, 1000, 10);
            user.set_gold(user.get_gold() - 10);
            user.set_tot_manpower(user.get_Tot_manpower() - 1000);
            swords_building.add(s);
            swords_made++;
        }
    }

    public void make_archer() {
        if (10 < user.get_gold() && user.get_Tot_manpower() >= 1000) {
            archers a = new archers(ID + "archer" + archers_made, 10, 10, 10, 10, 1000, 10);
            user.set_gold(user.get_gold() - 10);
            user.set_tot_manpower(user.get_Tot_manpower() - 1000);
            archers_building.add(a);
            archers_made++;
        }
    }

    public void make_horse() {
        if (10 < user.get_gold() && user.get_Tot_manpower() >= 1000) {
            horses h = new horses(ID + "horse" + horses_made, 10, 10, 10, 10, 1000, 10);
            user.set_gold(user.get_gold() - 10);
            user.set_tot_manpower(user.get_Tot_manpower() - 1000);
            horses_building.add(h);
            horses_made++;
        }
    }


    public void set_bonus_defense(int bonus_defense){this.bonus_defense = bonus_defense;}

    public int get_bonus_defense(){return bonus_defense;}

    public ArrayList<swords> get_swords_building(){return swords_building;}

    public ArrayList<archers> get_archers_building(){return archers_building;}

    public ArrayList<horses> get_horses_building(){return horses_building;}

    public int get_ID(){return ID;}

    public User get_User(){return user;}

    public void set_User(User user){
        this.user = user;
    }

    public double get_GPT(){return GPT;}

    public void set_GPT(double GPT){this.GPT = GPT;}

    public int get_MPT(){return MPT;}

    public void set_MPT(int MPT){this.MPT = MPT;}

    //Used in combat to increase defensive army stats
    public double find_bonus_defense(){
        if(hex_army.get_time_on_hex() < 100){
            return ((hex_army.get_time_on_hex()/100) * bonus_defense);
        }
        return bonus_defense;
    }

    public army get_hex_army() {return hex_army;}

    public ArrayList<swords> get_swords(){return hex_army.get_swords();}

    public void add_swords(swords u){hex_army.add_sword(u, 0);}

    public ArrayList<archers> get_archers(){return hex_army.get_archers();}

    public void add_archers(archers u){hex_army.add_archers(u, 0);}

    public ArrayList<horses> get_horses(){
        return hex_army.get_horses();
    }

    public void add_horses(horses u){hex_army.add_horses(u, 0);}

    //Merge Hex army with called army
    public void add_army(army a){
        for(swords s : a.get_swords()){
            hex_army.add_sword(s, 0);
        }
        for(archers ar : a.get_archers()){
            hex_army.add_archers(ar, 0);
        }
        for(horses h : a.get_horses()){
            hex_army.add_horses(h, 0);
        }
        hex_army.reorder_forces();
        a.clear();
    }
}
