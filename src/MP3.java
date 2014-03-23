/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pgaref@ics.forth.gr
 */
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;


public class MP3 {
    private String filename;
    private Player player;

    // constructor that takes the name of an MP3 file
    public MP3(String filename) {
        this.filename = filename;
    }

    // play the MP3 file
    public void play() {
        try {
            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            player.play();
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        
    }

    
    public static void main(String[] args) {
        
        //plays 07.mp3 file located at C drive
        MP3 mp3 = new MP3("ImperialMarch.mp3");
        mp3.play();
   
    }

}

