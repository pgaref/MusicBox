/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

/**
 *
 * @author PG
 */
 import java.util.Collection;
import java.util.Collections;
 import java.util.Vector;
 import javax.swing.table.AbstractTableModel;

 public class InteractiveTableModel extends AbstractTableModel {
     public static final int TITLE_INDEX = 0;
     public static final int ARTIST_INDEX = 1;
     public static final int ALBUM_INDEX = 2;
     public static final int HIDDEN_INDEX = 3;

     protected String[] columnNames;
     private Vector<AudioRecord> dataVector;

     public InteractiveTableModel(String[] columnNames) {
         this.columnNames = columnNames;
         dataVector = new Vector<AudioRecord>();
     }

     public String getColumnName(int column) {
         return columnNames[column];
     }

     public boolean isCellEditable(int row, int column) {
         if (column == HIDDEN_INDEX) return true;
         else return false;
     }

     public Class getColumnClass(int column) {
         switch (column) {
             case TITLE_INDEX:
             case ARTIST_INDEX:
             case ALBUM_INDEX:
                return String.class;
             default:
                return Object.class;
         }
     }

     public Object getValueAt(int row, int column) {
         AudioRecord record = (AudioRecord)getDataVector().get(row);
         switch (column) {
             case TITLE_INDEX:
                return record.getTitle();
             case ARTIST_INDEX:
                return record.getArtist();
             case ALBUM_INDEX:
                return record.getAlbum();
             case HIDDEN_INDEX:
                 return record.getRank();
             default:
                return null;
         }
     }

     public void setValueAt(Object value, int row, int column) {
         AudioRecord record = (AudioRecord)getDataVector().get(row);
         switch (column) {
             case TITLE_INDEX:
                record.setTitle((String)value);
                break;
             case ARTIST_INDEX:
                record.setArtist((String)value);
                break;
             case ALBUM_INDEX:
                record.setAlbum((String)value);
                break;
             case HIDDEN_INDEX:
                 record.setRank(Integer.parseInt((String)value));
                 break;
             default:
                System.out.println("invalid index");
         }
         fireTableCellUpdated(row, column);
         this.SortVector(true);
     }

     public int getRowCount() {
         return getDataVector().size();
     }

     public int getColumnCount() {
         return columnNames.length;
     }

     public boolean hasEmptyRow() {
         if (getDataVector().size() == 0) return false;
         AudioRecord audioRecord = (AudioRecord)getDataVector().get(getDataVector().size() - 1);
         if (audioRecord.getTitle().trim().equals("") &&
            audioRecord.getArtist().trim().equals("") &&
            audioRecord.getAlbum().trim().equals(""))
         {
            return true;
         }
         else return false;
     }

     public void addEmptyRow() {
         getDataVector().add(new AudioRecord());
         fireTableRowsInserted(
            getDataVector().size() - 1,
            getDataVector().size() - 1);
     }
     
     public void addSong(String title, String artist, String album, String path) {
         getDataVector().add(new AudioRecord(title, artist , album,getDataVector().size() +1 , path));
         Collections.sort(getDataVector());
         fireTableRowsInserted(
            getDataVector().size() - 1,
            getDataVector().size() - 1);
     }
     
     
     public void PrintDataVector(){
       
         for(int i = 0 ; i < getDataVector().size(); i++ ){
             System.out.println("["+i+"] "+((AudioRecord)getDataVector().get(i)).toString());
         }
         
     }
     
     public void SortVector(boolean ascending){
         if(ascending)
            Collections.sort(dataVector);
         else
            Collections.reverse(dataVector);
         fireTableDataChanged();
     }
     
     public void ShuffleVector(){
         Collections.shuffle(getDataVector());
         fireTableDataChanged();
     }
     
     public boolean isEmpty(){
         return (this.getDataVector().size() == 0);
         
         
     }

    /**
     * @return the dataVector
     */
    public Vector<AudioRecord> getDataVector() {
        return dataVector;
    }
 }
