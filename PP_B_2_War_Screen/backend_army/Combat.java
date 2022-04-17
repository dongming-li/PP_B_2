package com.warscreen.war_screen;

import java.util.Random;

/**
 * Created by Isaac Holtkamp on 10/2/2017.
 */

public class Combat {

    public Combat(army a1, army a2, Hex_data hex) {
        // combination of HOI IV and EUIV
        // each unit targets a unit near him i.e. same location in arraylist or +- 1
        // select larger array and center smaller army on larger center
        // Swords and horses in front with archers/ranged in back
        // swords engage all available swords
        // horses attack all horses - if no horse to challenge hit archers in back or flank infantry
        Random rand = new Random(System.currentTimeMillis());
        int luck1 = (rand.nextInt() % 10) + 1;
        int luck2 = (rand.nextInt() % 10) + 1;

        int curr_a1_unit = 0;
        int curr_a2_unit = 0;
        
        sword_combat_2(a1, a2, luck1, luck2);// swords target other swords, then horses, then archers
        sword_combat_2(a2, a1, luck2, luck1);
        horse_combat_2(a1, a2, luck1, luck2); // horses target other horses, then archers, then swords
        horse_combat_2(a2, a1, luck2, luck1);
        archer_combat_2(a1, a2, luck1, luck2);// archers target swords then archers, then horses
        archer_combat_2(a2, a1, luck2, luck1);// method written so only one army actually attacks (saves on room and coding)

        for(swords s : a1.get_swords()){
            if(s.get_manpower() < s.get_losses()) {
                s.set_manpower(0);
            }else {
                s.set_manpower(s.get_manpower() - s.get_losses());
            }
        }

        for(swords s : a2.get_swords()){
            if(s.get_manpower() < s.get_losses()) {
                s.set_manpower(0);
            }else {
                s.set_manpower(s.get_manpower() - s.get_losses());
            }
        }

        for(horses h : a1.get_horses()){
            if(h.get_manpower() < h.get_losses()) {
                h.set_manpower(0);
            }else {
                h.set_manpower(h.get_manpower() - h.get_losses());
            }
        }

        for(horses h : a2.get_horses()){
            if(h.get_manpower() < h.get_losses()) {
                h.set_manpower(0);
            }else {
                h.set_manpower(h.get_manpower() - h.get_losses());
            }
        }

        for(archers a : a1.get_archers()){
            if(a.get_manpower() < a.get_losses()) {
                a.set_manpower(0);
            }else {
                a.set_manpower(a.get_manpower() - a.get_losses());
            }
        }

        for(archers a : a2.get_archers()){
            if(a.get_manpower() < a.get_losses()) {
                a.set_manpower(0);
            }else {
                a.set_manpower(a.get_manpower() - a.get_losses());
            }
        }
    }

    /*
     * First attempt at combat
     * Causes errors and is bad coding
     * DO NOT USE
     * Use horse_combat_2, sword_combat_2, archer_combat_2
     * Will remove after Demo 3 Grading
     */
    private void horse_combat(army attackers, army defenders, int luck1, int luck2) {
        int curr_attacking_unit = 0;
        int curr_defending_unit = 0;

        while (true) {
            if (attackers.get_horses().get(curr_attacking_unit) == null || attackers.get_horses().get(curr_attacking_unit).get_manpower() < 1) {
                break;
            } else if (defenders.get_horses().get(curr_defending_unit) == null || defenders.get_horses().get(curr_defending_unit).get_manpower() < 1) {
                curr_defending_unit = 0;
                while (true) {
                    if (attackers.get_horses().get(curr_attacking_unit) == null || attackers.get_horses().get(curr_attacking_unit).get_manpower() < 1) {
                        break;
                    }else if (defenders.get_swords().get(curr_defending_unit) == null || defenders.get_swords().get(curr_defending_unit).get_manpower() < 1) {
                        curr_defending_unit = 0;
                        while (true) {
                            if (attackers.get_horses().get(curr_attacking_unit) == null || attackers.get_horses().get(curr_attacking_unit).get_manpower() < 1) {
                                break;
                            } else if (defenders.get_archers().get(curr_defending_unit) == null || defenders.get_archers().get(curr_defending_unit).get_manpower() < 1) {
                                //bonus
                                curr_attacking_unit++;
                            } else {
                                unit_fight(attackers.get_horses().get(curr_attacking_unit), defenders.get_archers().get(curr_defending_unit), luck1, luck2);
                                curr_attacking_unit++;
                                curr_defending_unit++;
                            }
                        }
                    } else {
                        unit_fight(attackers.get_horses().get(curr_attacking_unit), defenders.get_swords().get(curr_defending_unit), luck1, luck2);
                        curr_attacking_unit++;
                        curr_defending_unit++;
                    }
                }
            } else {
                unit_fight(attackers.get_horses().get(curr_attacking_unit), defenders.get_horses().get(curr_defending_unit), luck1, luck2);
                curr_attacking_unit++;
                curr_defending_unit++;
            }
        }
    }

    //Do not use
    private void archer_combat(army attackers, army defenders, int luck1, int luck2) {
        int curr_attacking_unit = 0;
        int curr_defending_unit = 0;

        while (true) {
            if (attackers.get_archers().get(curr_attacking_unit) == null || attackers.get_archers().get(curr_attacking_unit).get_manpower() < 1) {
                break;
            } else if (defenders.get_swords().get(curr_defending_unit) == null || defenders.get_swords().get(curr_defending_unit).get_manpower() < 1) {
                curr_defending_unit = 0;
                while (true) {
                    if (attackers.get_archers().get(curr_attacking_unit) == null || attackers.get_archers().get(curr_attacking_unit).get_manpower() < 1) {
                        break;
                    }else if (defenders.get_archers().get(curr_defending_unit) == null || defenders.get_archers().get(curr_defending_unit).get_manpower() < 1) {
                        curr_defending_unit = 0;
                        while (true) {
                            if (attackers.get_archers().get(curr_attacking_unit) == null || attackers.get_archers().get(curr_attacking_unit).get_manpower() < 1) {
                                break;
                            } else if (defenders.get_horses().get(curr_defending_unit) == null || defenders.get_horses().get(curr_defending_unit).get_manpower() < 1) {
                                curr_attacking_unit++;
                                //bonus
                            } else {
                                unit_fight(attackers.get_archers().get(curr_attacking_unit), defenders.get_horses().get(curr_defending_unit), luck1, luck2);
                                curr_attacking_unit++;
                                curr_defending_unit++;
                            }
                        }
                    } else {
                        unit_fight(attackers.get_archers().get(curr_attacking_unit), defenders.get_archers().get(curr_defending_unit), luck1, luck2);
                        curr_attacking_unit++;
                        curr_defending_unit++;
                    }
                }
            } else {
                unit_fight(attackers.get_archers().get(curr_attacking_unit), defenders.get_swords().get(curr_defending_unit), luck1, luck2);
                curr_attacking_unit++;
                curr_defending_unit++;
            }
        }
    }

    //Do not use
    private void swords_combat(army attackers, army defenders, int luck1, int luck2) {
        //start with first unit in array
        //attack unit in front of it
        //when out of swords target other units
        int curr_attacking_unit = 0;
        int curr_defending_unit = 0;

        while (true) {
            if (attackers.get_swords().get(curr_attacking_unit) == null || attackers.get_swords().get(curr_attacking_unit).get_manpower() < 1) {
                break;
            } else if (defenders.get_swords().get(curr_defending_unit) == null || defenders.get_swords().get(curr_defending_unit).get_manpower() < 1) {
                curr_defending_unit = 0;
                while (true) {
                    if (attackers.get_swords().get(curr_attacking_unit) == null || attackers.get_swords().get(curr_attacking_unit).get_manpower() < 1) {
                        break;
                    }else if (defenders.get_horses().get(curr_defending_unit) == null || defenders.get_horses().get(curr_defending_unit).get_manpower() < 1) {
                        curr_defending_unit = 0;
                        while (true) {
                            if (attackers.get_swords().get(curr_attacking_unit) == null || attackers.get_swords().get(curr_attacking_unit).get_manpower() < 1) {
                                break;
                            } else if (defenders.get_archers().get(curr_defending_unit) == null || defenders.get_archers().get(curr_defending_unit).get_manpower() < 1) {
                                curr_attacking_unit++;
                                //bonus
                            } else {
                                unit_fight(attackers.get_swords().get(curr_attacking_unit), defenders.get_archers().get(curr_defending_unit), luck1, luck2);
                                curr_attacking_unit++;
                                curr_defending_unit++;
                            }
                        }
                    } else {
                        unit_fight(attackers.get_swords().get(curr_attacking_unit), defenders.get_horses().get(curr_defending_unit), luck1, luck2);
                        curr_attacking_unit++;
                        curr_defending_unit++;
                    }
                }
            } else {
                unit_fight(attackers.get_swords().get(curr_attacking_unit), defenders.get_swords().get(curr_defending_unit), luck1, luck2);
                curr_attacking_unit++;
                curr_defending_unit++;
            }
        }
    }

    //Attack gets stats and Defender gets stats
    //Sets losses and kills in units that will be applied after combat completed
    private void unit_fight(units attacker, units defender, int luck1, int luck2) {
        int calc_attack = calc_unit_defense(defender, luck1) - calc_unit_attack(attacker, luck2);
        defender.set_losses(calc_attack);
        if(calc_attack > defender.get_manpower()) {
            calc_attack = defender.get_manpower();
        }
        attacker.set_kills(attacker.get_kills() + calc_attack);
    }

    private int calc_unit_attack(units s, int luck) {
        return s.get_attack() * luck * (s.get_manpower() / 1000);
    }

    private int calc_unit_defense(units s, int luck) {
        return s.get_defense() * luck * (s.get_manpower() / 1000);
    }

    //Each Sword attacks another unit
    //TODO edit so that 0 manpower units do not fight
    private void sword_combat_2(army attackers, army defenders, int luck1, int luck2){
        int d_swords = 0, d_horses = 0, d_archers = 0;
        for(swords s : attackers.get_swords()){
            if(defenders.get_swords().size() > d_swords){
                unit_fight(s, defenders.get_swords().get(d_swords), luck1, luck2);
                d_swords++;
            }else if(defenders.get_horses().size() > d_horses){
                unit_fight(s, defenders.get_horses().get(d_horses), luck1, luck2);
                d_horses++;
            }else if(defenders.get_swords().size() + defenders.get_horses().size() - d_archers < attackers.get_swords().size() && defenders.get_archers().size() > d_archers){
                unit_fight(s, defenders.get_archers().get(d_archers), luck1, luck2);
                d_archers++;
            }else{
                //bonus
            }
        }
    }

    //Read sword_combat_2
    private void archer_combat_2(army attackers, army defenders, int luck1, int luck2){
        int d_swords = 0, d_horses = 0, d_archers = 0;
        for(archers ar : attackers.get_archers()){
            if(defenders.get_swords().size() > d_swords){
                unit_fight(ar, defenders.get_swords().get(d_swords), luck1, luck2);
                d_swords++;
            }else if(defenders.get_archers().size() > d_archers){
                unit_fight(ar, defenders.get_archers().get(d_archers), luck1, luck2);
                d_archers++;
            }else if(defenders.get_horses().size() > d_horses){
                unit_fight(ar, defenders.get_horses().get(d_horses), luck1, luck2);
                d_horses++;
            }else{
                //bonus
            }
        }
    }

    //read sword_combat_2
    private void horse_combat_2(army attackers, army defenders, int luck1, int luck2){
        int d_swords = 0, d_horses = 0, d_archers = 0;
        for(horses ho : attackers.get_horses()){
            if(defenders.get_horses().size() > d_horses){
                unit_fight(ho, defenders.get_horses().get(d_horses), luck1, luck2);
                d_horses++;
            }else if(defenders.get_archers().size() > d_archers){
                unit_fight(ho, defenders.get_archers().get(d_archers), luck1, luck2);
                d_archers++;
            }else if(defenders.get_swords().size() > d_swords){
                unit_fight(ho, defenders.get_swords().get(d_swords), luck1, luck2);
                d_swords++;
            }else{
                //bonus
            }
        }
    }
}