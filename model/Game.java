/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;

public class Game extends Observable implements Runnable
{
    private Pacman pacman;
    private Level level;
    private GhostManager ghostManager;
    private int score = 0;
    private int lives = 3;
    private int levelNumber = 1;
    
    // Constructor
    public Game()
    {
        try
        {
            level = new Level(this, 1);
            ArrayList<Ghost> ghosts = level.load();
            ghostManager = new GhostManager(this, ghosts);
        }
        catch(Exception e)
        {
            System.out.println("Exception encoutered : " + e.getMessage());
        }
    }
    
    
    /* --- GETTERS / SETTERS --- */
    // <editor-fold>
    public Pacman getPacman()
    { return pacman; }
    
    public void setPacman(Pacman pacman)
    { this.pacman = pacman; }
    
    public Level getLevel()
    { return level; }
    
    public int getLevelNumber()
    { return levelNumber; }
    
    // Get the score
    public int getScore()
    { return this.score; }
    
    // Add points to the Score
    public void addScore(int s)
    { this.score += s; }
    
    // Get the number of lives
    public int getLives()
    { return lives; }
    
    public Block getInGrid(Point position)
    { return level.getInGrid(position); }    
    
    public GhostManager getGhostManager()
    { return ghostManager; }
    // </editor-fold>
    
    
    /* --- Game Manager --- */
    // <editor-fold>
    public void start()
    {
        new Thread(this).start();
        new Thread(ghostManager).start();
    }
    
    public void loose()
    {
        lives -= 1;
        if(lives == 0)
            gameOver();
        
        notify(null);
        
        ghostManager.moveToStartPositions();
        
        pacman.move(pacman.getStartPosition());
        pacman.setSitOn(null);
        ghostManager.resetGhostMode();
        notify("death");
    }
    
    //Fonctions for the gameOver
    public void gameOver()
    {
        levelNumber = 1;
        score = 0;
        lives = 3;
        level.updateHighScore(score);
        reloadLevel();
    }
    
    public void reloadLevel()
    {
        try
        {
            ghostManager = new GhostManager(this, level.load());
            start();
            Thread.currentThread().interrupt();
        }
        catch(Exception e)
        {
            System.out.println("Exception encoutered : " + e.getMessage());
        }
    }
    
    public void notify(Object arg)
    {
        setChanged();
        notifyObservers(arg);
    }
    
    public void winLevel()
    {
        levelNumber++;
        reloadLevel();
    }

    @Override
    public void run()
    {
        notify("beginning");
        while (true)
        {
            pacman.move();
            
            ghostManager.checkGhostMode();
            
            notify(null);

            try
            {
                long waitTime = Math.max(170 - 15 * levelNumber, 50);
                Thread.sleep(waitTime);
            }
            catch (InterruptedException ex)
            {
                System.out.println("Exception : " + ex.getMessage());
                Thread.currentThread().interrupt();
                ghostManager.stop();
                break;
            }
        }
    }
    
    public void stop()
    { Thread.currentThread().interrupt(); }
    // </editor-fold>
}
