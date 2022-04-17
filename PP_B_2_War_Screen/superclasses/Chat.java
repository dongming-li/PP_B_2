package com.warscreen.war_screen;

/**
 * Created by Isaac Holtkamp on 10/9/2017.
 */

public class Chat {

    String user_id;
    String sentence;

    public Chat(String user_id, String sentence){
        this.user_id = user_id;
        this.sentence = sentence;
    }

    public void print_message(){
        System.out.println("[" + user_id + "] " + sentence);
    }

    public String get_message(){ return sentence; }

    public void set_message(String sentence){this.sentence = sentence; }
}
