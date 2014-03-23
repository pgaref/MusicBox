package Player;

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


public class SimplePlayer {
    private String filename;
    private Player player;

    // constructor that takes the name of an MP3 file
    public SimplePlayer(String filename) {
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
        SimplePlayer mp3 = new SimplePlayer("ImperialMarch.mp3");
        mp3.play();
   
    }

}

