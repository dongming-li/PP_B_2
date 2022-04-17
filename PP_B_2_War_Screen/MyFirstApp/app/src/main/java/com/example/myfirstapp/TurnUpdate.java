package com.example.myfirstapp;

import java.util.ArrayList;

/**
 * Created by Isaac Holtkamp on 10/27/2017.
 */

public class TurnUpdate {

    public TurnUpdate(ArrayList<Hex_data> hexs, ArrayList<army> armies, User user){
        int[] temp = new int[6];
        boolean[] temp2 = new boolean[6];
        for(Hex_data h : hexs){
            if(h.get_User().get_ID() == user.get_ID()){//if 1 means owned by player else 0 for unowned and 2 for opposing player
                user.set_gold(user.get_gold() + h.get_GPT());
                user.set_tot_manpower(user.get_Tot_manpower() + h.get_MPT());
                temp = h.get_build_time();
                for(int i = 0; i < 6; i++){
                    if(temp[i] == 1){
                        temp2 = h.get_buildings();
                        temp2[i] = true;
                    }
                    temp[i] --;
                }
                h.set_build_time(temp);
                h.set_buildings(temp2);
            }
        }
        for(army a : armies){
            if(a.get_is_moving() == true){
                if(a.get_move_time() > 0){
                    a.set_move_time(a.get_move_time() - 1);
                }else{
                    a.set_hex_at(a.get_move_to_ID());
                    a.set_time_on_hex(0);
                    a.set_is_moving(false);
                    if(hexs.get(a.get_hex_at()).get_User().get_ID() == user.get_ID()){
                        hexs.get(a.get_hex_at()).add_army(a);
                    }else if(hexs.get(a.get_hex_at()).get_User() == null){
                        hexs.get(a.get_hex_at()).set_User(user);
                        hexs.get(a.get_hex_at()).add_army(a);
                    }else{
                        new Combat(hexs.get(a.get_hex_at()).getHex_army(), a, hexs.get(a.get_hex_at()));
                    }
                }
            }
        }
    }





}
