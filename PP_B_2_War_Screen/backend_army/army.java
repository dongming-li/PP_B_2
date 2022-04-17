package com.warscreen.war_screen;

import java.util.ArrayList;

/**
 * Created by Isaac Holtkamp on 9/28/2017.
 */

public class army {

    private ArrayList<swords> sword_list = new ArrayList<swords>(); //All of the swords in the army
    private ArrayList<archers> archer_list = new ArrayList<archers>(); //All of the archers in the army
    private ArrayList<horses> horse_list = new ArrayList<horses>(); //All of the horses in the army
    private int hex_at; //current army location
    private int move_time; //time to move to another hex
    private int move_to_ID; //what hex army moving to if moving
    private boolean is_moving; //is the army moving
    private int time_on_hex; //how long has the army been stationary
    private int max_speed; //slowest unit, fastest speed

    public army(ArrayList<swords> swords, ArrayList<archers> archers, ArrayList<horses> horses, int hex_at, int time_on_hex){
        sword_list = swords;
        archer_list = archers;
        horse_list = horses;
        this.hex_at = hex_at;
        this.time_on_hex = time_on_hex;
        max_speed = 1000;
        army_speed();
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

    public ArrayList<swords> get_swords(){return sword_list;}

    public swords get_sword_at(int loc){return sword_list.get(loc);}

    public ArrayList<archers> get_archers(){return archer_list;}

    public archers get_archer_at(int loc){return archer_list.get(loc);}

    public ArrayList<horses> get_horses(){return horse_list;}

    public horses get_horse_at(int loc){return horse_list.get(loc);}

    public int get_max_speed(){return max_speed;}

    /*search all units in army for slowest units
    * more applicable if different units
    * called only once when army is created else updated with each new unit added
    */
    private void army_speed(){
        if(max_speed == 0){
            max_speed = 1000;
        }
        for(swords sword: sword_list){
            if(sword.get_speed() < max_speed){
                max_speed = sword.get_speed();
            }
        }
        for(archers archer: archer_list){
            if(archer.get_speed() < max_speed){
                max_speed = archer.get_speed();
            }
        }
        for(horses horse: horse_list) {
            if (horse.get_speed() < max_speed) {
                max_speed = horse.get_speed();
            }
        }
    }

    /*
    * insert a sword unit into the sword list
    * check that there is no repeat name in the list (there shouldnt be due to counting on each hex)
    * update max speed
     */
    public int add_sword(swords u1, int loc) {
        sword_list.add(loc, u1);
        return 0;
    }

    /*
      * insert a archer unit into the sword list
      * check that there is no repeat name in the list (there shouldnt be due to counting on each hex)
      * update max speed
       */
    public int add_archers(archers u1, int loc){
        archer_list.add(loc, u1);
        return 0;
    }

    /*
  * insert a horse unit into the sword list
  * check that there is no repeat name in the list (there shouldnt be due to counting on each hex)
  * update max speed
   */
    public int add_horses(horses u1, int loc){
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

    public void clear(){
        for(swords s : sword_list){
            s = null;
        }
        for(archers a : archer_list){
            a = null;
        }
        for(horses h : horse_list){
            h = null;
        }
    }
    /*
    * Instead of having manpower used to replenish both units combine and lower needed replenishment
    * Will combine different units so when it's being called must check for same unit type
    * If more than 1000 manpower combined keep both units else remove one
    */
    public void combine_two_unit_forces(units u1, units u2){
        int tot_manpower = u1.get_manpower() + u2.get_manpower();
        if(tot_manpower > 1000){
            u1.set_manpower(1000);
            u2.set_manpower(tot_manpower - 1000);
        }
        else{
            u1.set_manpower(tot_manpower);
            u2.set_manpower(0);
        }
    }

    /*
     * Reorders force so that units with the most manpower are at beginning of the array
     * Should not be called while armies in combat
     */
    public void reorder_forces(){
        swords temps;
        archers tempa;
        horses temph;
        int i, j;
        while(true){
            for(i = 1; i < sword_list.size(); i++) {
                for (j = i - 1; j >= 0; j--) {
                    if(sword_list.get(i).get_manpower() > sword_list.get(j).get_manpower()){
                        temps = sword_list.get(i);
                        sword_list.remove(i);
                        sword_list.add(j, temps);
                        j = i;
                    }
                }
            }
            for(i = 1; i < archer_list.size(); i++) {
                for (j = i - 1; j >= 0; j--) {
                    if(archer_list.get(i).get_manpower() > archer_list.get(j).get_manpower()){
                        tempa = archer_list.get(i);
                        archer_list.remove(i);
                        archer_list.add(j, tempa);
                        j = i;
                    }
                }
            }
            for(i = 1; i < horse_list.size(); i++) {
                for (j = i - 1; j >= 0; j--) {
                    if(horse_list.get(i).get_manpower() > horse_list.get(j).get_manpower()){
                        temph = horse_list.get(i);
                        horse_list.remove(i);
                        horse_list.add(j, temph);
                        j = i;
                    }
                }
            }
        }
    }
}
