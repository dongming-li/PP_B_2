package com.warscreen.war_screen;

import java.util.ArrayList;

/**
 * Created by Isaac Holtkamp on 10/27/2017.
 */

public class TurnUpdate {

    public TurnUpdate(ArrayList<Hex_data> hexs, User user){
        for(Hex_data h : user.get_owned_hexs()){
            //Add gold and manpower to user stats
            user.set_gold(user.get_gold() + h.get_GPT());
            user.set_tot_manpower(user.get_Tot_manpower() + h.get_MPT());

            //for each building, if any, update build time and stats if needed
            for(buildings build : h.get_buildings()) {
                if(build.get_const_time() > 0) {
                    build.set_const_time(build.get_const_time() - 1);
                }else if(build.get_const_time() == 1){
                    build.set_const_time(0);
                    if(build.getClass() == barrack.class){
                        h.set_MPT((int)(h.get_MPT() * 1.25));
                    }else if(build.getClass() == market.class){
                        h.set_GPT(h.get_GPT() * 1.25);
                    }else if(build.getClass() == fort.class){
                        h.set_bonus_defense(h.get_bonus_defense() + 2);
                    }
                }
            }
            //Update swords being built
            for(swords s : h.get_swords_building()){
                if(s.get_build_time() == 1){
                    h.add_swords(s);
                    h.remove_built_unit(s);
                }
                else{
                    s.set_build_time(s.get_build_time() - 1);
                }
            }
            //Update archers being built
            for(archers a : h.get_archers_building()){
                if(a.get_build_time() == 1){
                    h.add_archers(a);
                    h.remove_built_unit(a);
                }
                else{
                    a.set_build_time(a.get_build_time() - 1);
                }
            }
            //Update horses being built
            for(horses hor : h.get_horses_building()){
                if(hor.get_build_time() == 1){
                    h.add_horses(hor);
                    h.remove_built_unit(hor);
                }
                else{
                    hor.set_build_time(hor.get_build_time() - 1);
                }
            }
        }

        //All armies on hexs and moving armies list
        for(army a : user.get_tot_armies()){
            //Movement
            if(a.get_is_moving()){
                if(a.get_move_time() > 0){
                    a.set_move_time(a.get_move_time() - 1);
                }else if(a.get_move_time() == 0){
                    a.set_hex_at(a.get_move_to_ID());
                    a.set_time_on_hex(0);
                    a.set_is_moving(false);
                    if(hexs.get(a.get_hex_at()).get_User().get_ID() == user.get_ID()){
                        hexs.get(a.get_hex_at()).add_army(a);
                        user.add_army(hexs.get(a.get_hex_at()).get_hex_army());
                        user.get_tot_armies().remove(a);
                        a.clear();
                    }else if(hexs.get(a.get_hex_at()).get_User() == null){
                        hexs.get(a.get_hex_at()).set_User(user);
                        hexs.get(a.get_hex_at()).add_army(a);
                        user.add_hex(hexs.get(a.get_hex_at()));
                    }else{
                        new Combat(hexs.get(a.get_hex_at()).get_hex_army(), a, hexs.get(a.get_hex_at()));
                    }
                }
            //Bonus defense update for combat
            }else{
                a.set_hex_at(a.get_time_on_hex() + 1);
            }
        }
    }





}
