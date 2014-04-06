/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Player;

import Audio.AudioRecord;
import GUI.MusicPlayerGUI;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;

/**
 *
 * @author PG
 */
public class PausablePlayer {

    private final static int NOTSTARTED = 0;
    private final static int PLAYING = 1;
    private final static int PAUSED = 2;
    private final static int FINISHED = 3;

    // the player actually doing all the work
    private final Player player;
    private InputStream stream;
    private Vector<AudioRecord> playlist;
    private boolean playinglist = false;

    // locking object used to communicate with player thread
    private final Object playerLock = new Object();

    // status variable what player thread is doing/supposed to do
    private int playerStatus = NOTSTARTED;

    public PausablePlayer(final InputStream inputStream) throws JavaLayerException {
        this.stream =inputStream;
        this.player = new Player(inputStream);
    }

    public PausablePlayer(final InputStream inputStream, final AudioDevice audioDevice) throws JavaLayerException {
        this.stream =inputStream;
        this.player = new Player(inputStream, audioDevice);
    }
    
    public PausablePlayer playPlayList(Vector<AudioRecord> musicList) throws FileNotFoundException, JavaLayerException{
        this.playlist = musicList;
        this.playinglist = true;
        FileInputStream input = new FileInputStream(this.playlist.get(0).getPath()); 
        PausablePlayer  p = new PausablePlayer(input);
        return p;
        
    }
    /**
     * Starts playback (resumes if paused)
     */
    public void play() throws JavaLayerException {
        synchronized (playerLock) {
            switch (getPlayerStatus()) {
                case NOTSTARTED:
                    final Runnable r = new Runnable() {
                        public void run() {
                            playInternal();
                        }
                    };
                    final Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setPriority(Thread.MAX_PRIORITY);
                    playerStatus = PLAYING;
                    t.start();
                    break;
                case PAUSED:
                    resume();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Pauses playback. Returns true if new state is PAUSED.
     */
    public boolean pause() {
        synchronized (playerLock) {
            if (getPlayerStatus() == PLAYING) {
                playerStatus = PAUSED;
            }
            return getPlayerStatus() == PAUSED;
        }
    }

    /**
     * Resumes playback. Returns true if the new state is PLAYING.
     */
    public boolean resume() {
        synchronized (playerLock) {
            if (getPlayerStatus() == PAUSED) {
                playerStatus = PLAYING;
                playerLock.notifyAll();
            }
            return getPlayerStatus() == PLAYING;
        }
    }
    
    public boolean seek(long bytes){
        try {
            this.stream.skip(bytes);
            
        } catch (IOException ex) {
            Logger.getLogger(PausablePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    /**
     * Stops playback. If not playing, does nothing
     */
    public void stop() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
            playerLock.notifyAll();
        }
    }

    private void playInternal() {
        while (getPlayerStatus() != FINISHED) {
            try {
                if (!player.play(1)) {
                    break;
                }
                MusicPlayerGUI.jProgressBar1.setValue(player.getPosition()/5000);
                MusicPlayerGUI.playtime.setText((player.getPosition()/60000)+":"+(player.getPosition()/1000)%60+" ~ Playing");
            } catch (final JavaLayerException e) {
                break;
            }
            // check if paused or terminated
            synchronized (playerLock) {
                while (getPlayerStatus() == PAUSED) {
                    try {
                        playerLock.wait();
                    } catch (final InterruptedException e) {
                        // terminate player
                        break;
                    }
                }
            }
        }
        close();
    }
    /**
     * Closes the player, regardless of current state.
     */
    public void close() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
        }
        try {
            player.close();
        } catch (final Exception e) {
            // ignore, we are terminating anyway
        }
    }

    // demo how to use
    public static void main(String[] argv) {
        try {
            FileInputStream input = new FileInputStream("ImperialMarch.mp3"); 
            PausablePlayer player = new PausablePlayer(input);

            // start playing
            player.play();

            // after 5 secs, pause
            Thread.sleep(5000);
            player.pause();     

            // after 5 secs, resume
            Thread.sleep(5000);
            player.resume();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the playerStatus
     */
    public int getPlayerStatus() {
        return playerStatus;
    }

}