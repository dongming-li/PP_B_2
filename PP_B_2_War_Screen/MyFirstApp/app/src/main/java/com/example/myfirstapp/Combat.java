package com.example.myfirstapp;

import java.util.Random;

/**
 * Created by Isaac Holtkamp on 10/2/2017.
 */

/**
 * TODO:
 * finish swords attack
 * -make special cases for horse/archer attacks
 * --put a special bonus parameter in attack method
 * --hitting horses head on gains swords bonus
 *
 * create archer target selection method
 * -attacks swords if available, then other archers, then horses
 * --put special bonus parameter in attack (lower attack on horses)
 *
 * create horse attack
 * -Attempt to flank
 * --Target all horses, then hit outside swords for extreme bonus effect
 * --Remainder hit archers if possible for extra bonus damage
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
        
        swords_combat(a1, a2, luck1, luck2);// swords target other swords, then horses, then archers
        swords_combat(a2, a1, luck2, luck1);
        horse_combat(a1, a2, luck1, luck2); // horses target other horses, then archers, then swords
        horse_combat(a2, a1, luck2, luck1);
        archer_combat(a1, a2, luck1, luck2);// archers target swords then archers, then horses
        archer_combat(a2, a1, luck2, luck1);// method written so only one army actually attacks (saves on room and coding)

        while (true) {
            while (a1.get_swords().get(curr_a1_unit) != null) {
                if(a1.get_swords().get(curr_a1_unit).get_manpower() < a1.get_swords().get(curr_a1_unit).get_losses()) {
                    a1.get_swords().get(curr_a1_unit).set_manpower(0);
                }else {
                    a1.get_swords().get(curr_a1_unit).set_manpower(a1.get_swords().get(curr_a1_unit).get_manpower() - a1.get_swords().get(curr_a1_unit).get_losses());
                }
                curr_a1_unit++;
            }
            curr_a1_unit = 0;
            while (a1.get_horses().get(curr_a1_unit) != null) {
                if(a1.get_horses().get(curr_a1_unit).get_manpower() < a1.get_horses().get(curr_a1_unit).get_losses()) {
                    a1.get_horses().get(curr_a1_unit).set_manpower(0);
                }else {
                    a1.get_horses().get(curr_a1_unit).set_manpower(a1.get_horses().get(curr_a1_unit).get_manpower() - a1.get_horses().get(curr_a1_unit).get_losses());
                }
                curr_a1_unit++;
            }
            curr_a1_unit = 0;
            while (a1.get_archers().get(curr_a1_unit) != null) {
                if (a1.get_archers().get(curr_a1_unit).get_manpower() < a1.get_archers().get(curr_a1_unit).get_losses()) {
                    a1.get_archers().get(curr_a1_unit).set_manpower(0);
                } else {
                    a1.get_archers().get(curr_a1_unit).set_manpower(a1.get_archers().get(curr_a1_unit).get_manpower() - a1.get_archers().get(curr_a1_unit).get_losses());
                }
                curr_a1_unit++;
            }

            while (a2.get_swords().get(curr_a2_unit) != null) {
                if(a2.get_swords().get(curr_a2_unit).get_manpower() < a2.get_swords().get(curr_a2_unit).get_losses()) {
                    a2.get_swords().get(curr_a2_unit).set_manpower(0);
                }else {
                    a2.get_swords().get(curr_a2_unit).set_manpower(a2.get_swords().get(curr_a2_unit).get_manpower() - a2.get_swords().get(curr_a2_unit).get_losses());
                }
                curr_a2_unit++;
            }
            curr_a2_unit = 0;

            while (a2.get_horses().get(curr_a2_unit) != null) {
                if(a2.get_horses().get(curr_a2_unit).get_manpower() < a2.get_horses().get(curr_a2_unit).get_losses()) {
                    a2.get_horses().get(curr_a2_unit).set_manpower(0);
                }else {
                    a2.get_horses().get(curr_a2_unit).set_manpower(a2.get_horses().get(curr_a2_unit).get_manpower() - a2.get_horses().get(curr_a2_unit).get_losses());
                }
                curr_a2_unit++;
            }
            curr_a2_unit = 0;
            while (a2.get_archers().get(curr_a2_unit) != null) {
                if(a2.get_archers().get(curr_a2_unit).get_manpower() < a2.get_archers().get(curr_a2_unit).get_losses()) {
                    a2.get_archers().get(curr_a2_unit).set_manpower(0);
                }else {
                    a2.get_archers().get(curr_a2_unit).set_manpower(a2.get_archers().get(curr_a2_unit).get_manpower() - a2.get_archers().get(curr_a2_unit).get_losses());
                }
                curr_a2_unit++;
            }
            curr_a2_unit = 0;
        }

    }


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
}