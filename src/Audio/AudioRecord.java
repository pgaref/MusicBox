/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

/**
 *
 * @author PG
 */
public class AudioRecord {
     protected String title;
     protected String artist;
     protected String album;
     private int rank;

     public AudioRecord() {
         title = "";
         artist = "";
         album = "";
     }
     
     public AudioRecord(String title, String artist, String album, int rank) {
         this.title = title;
         this.artist = artist;
         this.album = album;
         this.rank = rank;
     }

     public String getTitle() {
         return title;
     }

     public void setTitle(String title) {
         this.title = title;
     }

     public String getArtist() {
         return artist;
     }

     public void setArtist(String artist) {
         this.artist = artist;
     }

     public String getAlbum() {
         return album;
     }

     public void setAlbum(String album) {
         this.album = album;
     }

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }
}
