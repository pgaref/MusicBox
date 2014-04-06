/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import java.util.LinkedList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

/**
 *
 * @author PG
 */
public final class VolumeControl
{

    public VolumeControl(){}

    private static LinkedList<Line> speakers = new LinkedList<Line>();

    public final static void findSpeakers()
    {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        for (Mixer.Info mixerInfo : mixers)
        {
           // System.out.println("MIXER: " + mixerInfo.getName());
            //if(!mixerInfo.getName().equals("Java Sound Audio Engine")) continue;

            Mixer mixer         = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lines   = mixer.getTargetLineInfo();

            for (Line.Info info : lines)
            {

                try 
                {
                    Line line = mixer.getLine(info);
                    line.open();
                    if(line.isControlSupported(FloatControl.Type.VOLUME)){
                       // System.out.println("Mixinfo: "+ line.getLineInfo());
                        speakers.add(line);
                    }

                }
                catch (LineUnavailableException e)      { e.printStackTrace();                                                                                  } 
                catch (IllegalArgumentException iaEx)   {                                                                                                       }
            }
        }
    }

    static
    {
        findSpeakers();
    }

    public static void setVolume(float level)
    {
        findSpeakers();
        System.out.println("setting volume to "+level);
        for(Line line : speakers)
        {
            try
            {
                line.open();
                FloatControl control = (FloatControl)line.getControl(FloatControl.Type.VOLUME);
                control.setValue((float)(level/100));
            }
            catch (LineUnavailableException e) { continue; }
            catch(java.lang.IllegalArgumentException e) { continue; }



        }
    }

    private static float limit(FloatControl control,float level)
    { return Math.min(control.getMaximum(), Math.max(control.getMinimum(), level)); }

}
