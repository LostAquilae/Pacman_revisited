/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author slx
 */
public class SoundManager extends Thread
{
    private Sound[] sounds = new Sound[5];
    private String sound_string[] = {"beginning","chomp","eatghost","eatfruit","death"};
    
    public SoundManager()
    {
        for(int i = 0; i < 5; i++)
            sounds[i] = new Sound("src/Sounds/" + sound_string[i] + ".wav");
    }
    
    public void updateSounds(String sound)
    {
        switch (sound)
        {
            case "beginning":
                this.sounds[0].playSound();
                break;
            case "chomp":
                this.sounds[1].playSound();
                break;
            case "eatghost":
                this.sounds[2].playSound();
                break;
            case "eatfruit":
                this.sounds[3].playSound();
                break;
            case "death":
                this.sounds[4].playSound();
                break;
        }
    }
    
    class Sound
    {
        private URL url;//l'url de ton fichier son
        private AudioClip s1;//le son créé depuis ton url

        public Sound(String path) {
            try {
                url = new File(path).toURI().toURL();
                s1 = Applet.newAudioClip(url);
            } catch (MalformedURLException ex) {
                Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public void playSound() {
            s1.stop();
            s1.play();
        }
        public void loopSound() {
            s1.loop();
        }
        public void stopSound() {
            s1.stop();
        }
    }
}
