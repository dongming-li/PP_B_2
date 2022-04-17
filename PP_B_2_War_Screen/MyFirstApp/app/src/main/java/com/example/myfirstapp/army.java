package com.example.myfirstapp;

import java.util.ArrayList;

/**
 * Created by Isaac Holtkamp on 9/28/2017.
 */

public class army {

    private ArrayList<units> sword_list = new ArrayList<units>();
    private ArrayList<units> archer_list = new ArrayList<units>();
    private ArrayList<units> horse_list = new ArrayList<units>();
    private int hex_at;
    private int move_time;
    private int move_to_ID;
    private boolean is_moving;
    private int time_on_hex;

    public army(ArrayList<units> swords, ArrayList<units> archers, ArrayList<units> horses, int hex_at, int time_on_hex){
        sword_list = swords;
        archer_list = archers;
        horse_list = horses;
        this.hex_at = hex_at;
        this.time_on_hex = time_on_hex;
    }

    public void set_time_on_hex(int time_on_hex){this.time_on_hex = time_on_hex;}

    public int get_time_on_hex(){return time_on_hex;}

    public void set_hex_at(int hex_at){this.hex_at = hex_at;}

    public int get_hex_at(){return hex_at;}

    public void set_is_moving(boolean b){is_moving = b;}

    public boolean get_is_moving(){return is_moving;}

    public void set_move_time(int move_time){this.move_time = move_time;}

    public int get_move_time(){return move_time;}

    public void set_move_to_ID(int move_to_ID){this.move_to_ID = move_to_ID;}

    public int get_move_to_ID(){return move_to_ID;}

    public ArrayList<units> get_swords(){return sword_list;}

    public units get_sword_at(int loc){return sword_list.get(loc);}

    public ArrayList<units> get_archers(){return archer_list;}

    public units get_archer_at(int loc){return archer_list.get(loc);}

    public ArrayList<units> get_horses(){return horse_list;}

    public units get_horse_at(int loc){return horse_list.get(loc);}

    public int army_speed(){
        int max_speed = 1000;
        for(units sword: sword_list){
            if(sword.get_speed() < max_speed){
                max_speed = sword.get_speed();
            }
        }
        for(units archer: archer_list){
            if(archer.get_speed() < max_speed){
                max_speed = archer.get_speed();
            }
        }
        for(units horse: horse_list){
            if(horse.get_speed() < max_speed){
                max_speed = horse.get_speed();
            }
        }
        return max_speed;
    }

    public int add_sword(units u1, int loc) {
        for(units swords : sword_list) {
            if(swords.get_name().equals(u1.get_name())){
                u1.set_name(u1.get_name() +"1");
                add_sword(u1, loc);
            }
        }
        sword_list.add(loc, u1);
        return 0;
    }

    public int add_archers(units u1, int loc){
        for(units archers : archer_list){
            if(archers.get_name().equals(u1.get_name())){
                u1.set_name(u1.get_name() + "1");
                add_archers(u1, loc);
            }
        }
        archer_list.add(loc, u1);
        return 0;
    }

    public int add_horses(units u1, int loc){
        for(units horses : horse_list){
            if(horses.get_name().equals(u1.get_name())){
                u1.set_name(u1.get_name() + "1");
                add_horses(u1, loc);
            }
        }
        horse_list.add(loc, u1);
        return 0;
    }

    public void remove_swords(int loc){
        sword_list.remove(loc);
    }

    public void remove_archer(int loc){
        archer_list.remove(loc);
    }

    public void remove_horse(int loc){
        horse_list.remove(loc);
    }

    public void combine_two_unit_forces(units u1, units u2){
        //Must check for same type of units before calling this method
        int tot_manpower = u1.get_manpower() + u2.get_manpower();
        if(tot_manpower > 1000){
            u1.set_manpower(1000);
            u2.set_manpower(tot_manpower - 1000);
        }
        else{
            u1.set_manpower(tot_manpower);
        }
    }

    public void combine_forces(army combine){

        while(combine.get_swords().size() != 0){
            sword_list.add(0, combine.get_sword_at(0));
            combine.remove_swords(0);
        }

        while(combine.get_archers().size() != 0){
            archer_list.add(0, combine.get_archer_at(0));
            combine.remove_swords(0);
        }

        while(combine.get_horses().size() != 0){
            horse_list.add(0, combine.get_sword_at(0));
            combine.remove_swords(0);
        }
        reorder_forces();
    }

    public void reorder_forces(){
        units temp;
        int i, j;
        while(true){
            for(i = 1; i > sword_list.size(); i++) {
                for (j = 0; j < i; j++) {
                    if(sword_list.get(i).get_manpower() > sword_list.get(j).get_manpower()){
                        temp = sword_list.get(i);
                        sword_list.remove(i);
                        sword_list.add(j, temp);
                        j = i;
                    }
                }
            }
            for(i = 1; i > archer_list.size(); i++) {
                for (j = 0; j < i; j++) {
                    if(archer_list.get(i).get_manpower() > archer_list.get(j).get_manpower()){
                        temp = archer_list.get(i);
                        archer_list.remove(i);
                        archer_list.add(j, temp);
                        j = i;
                    }
                }
            }
            for(i = 1; i > horse_list.size(); i++) {
                for (j = 0; j < i; j++) {
                    if(horse_list.get(i).get_manpower() > horse_list.get(j).get_manpower()){
                        temp = horse_list.get(i);
                        horse_list.remove(i);
                        horse_list.add(j, temp);
                        j = i;
                    }
                }
            }
        }
    }
}
