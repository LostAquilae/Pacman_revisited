/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;

/**
 *
 * @author Rutabaga
 */
public class GhostManager implements Runnable
{
    private Game game;
    private ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
    private GhostMode oldGhostMode;
    private GhostMode currentGhostMode;
    private int ghostScore;
    private int[] ghostModeTimings = { 7 , 27 , 34 , 54 , 59 , 79 , 84 };
    private int currentGhostModeTimingIndex;
    private long frightenedTime = 5000; // ms
    private long frightenedTimeCounter;
    private long lastLoadTime;
    private float ghostSpeedMultiplier = 0.9f;
    
    public GhostManager(Game game, ArrayList<Ghost> ghosts)
    {
        this.game = game;
        this.ghosts = ghosts;
        
        resetGhostMode();
        
        game.getPacman().setGhostManager(this);
        for(Ghost ghost : ghosts)
            ghost.setGhostManager(this);
    }
    
    public void setGhostsFrightened()
    {
        ghostSpeedMultiplier = 0.6f;
        frightenedTimeCounter = System.currentTimeMillis() - lastLoadTime;
        ghostScore = 200;
        if(currentGhostMode != GhostMode.Frightened)
            oldGhostMode = currentGhostMode;
        currentGhostMode = GhostMode.Frightened;
        
        for(Ghost ghost : ghosts)
            ghost.turnOver();
    }
    
    public void checkGhostMode()
    {
        if(currentGhostMode == GhostMode.Frightened)
        {
            if(frightenedTimeCounter + frightenedTime - (1000 * game.getLevelNumber()) < System.currentTimeMillis() - lastLoadTime)
            {
                ghostSpeedMultiplier = 0.9f;
                ghostScore = 200;
                currentGhostMode = oldGhostMode;
            }
        }
        else
        {
            if(currentGhostModeTimingIndex < ghostModeTimings.length)
            {
                if(System.currentTimeMillis() - lastLoadTime >= ghostModeTimings[currentGhostModeTimingIndex] * 1000)
                {
                    if(currentGhostMode == GhostMode.Scatter)
                        currentGhostMode = GhostMode.Chase;
                    else
                        currentGhostMode = GhostMode.Scatter;
                    
                    currentGhostModeTimingIndex++;
                    for(Ghost ghost : ghosts)
                        ghost.turnOver();
                }
            }
        }
    }
    
    public void resetGhostMode()
    {
        currentGhostModeTimingIndex = 0;
        lastLoadTime = System.currentTimeMillis();
        frightenedTimeCounter = 0;
        oldGhostMode = GhostMode.Scatter;
        currentGhostMode = GhostMode.Scatter;
    }
    
    public void moveGhosts()
    {
        for (Ghost ghost : ghosts)
        {
            ghost.move();
        }
    }
    
    public void moveToStartPositions()
    {
        for (Ghost ghost : ghosts)
        {
            ghost.move(ghost.getStartPosition());
            ghost.setSitOn(null);
        }
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            moveGhosts();
            
            game.notify(null);
            
            try
            {
                long waitTime = (int)((Math.max(170 - 15 * game.getLevelNumber(), 50)) / ghostSpeedMultiplier);
                Thread.sleep(waitTime);
            }
            catch (InterruptedException ex)
            {
                System.out.println("Exception : " + ex.getMessage());
                Thread.currentThread().interrupt();
                game.stop();
                break;
            }
        }
    }
    
    public void stop()
    { Thread.currentThread().interrupt(); }
    
    // Getters - setters
    public Ghost getBlinky()
    { return ghosts.get(0); }
    
    public int getGhostScore()
    {
        ghostScore *= 2;
        return ghostScore / 2;
    }
    
    public GhostMode getGhostMode()
    { return currentGhostMode; }
}
