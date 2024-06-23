/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * @author Rutabaga
 */
public class Consumable extends Block
{
    protected int points = 10;
    
    public Consumable(Level level)
    {
        super(level);
        isBlocking = false;
    }
    
    public void consume()
    {
        game.addScore(points);
    }
    
    public int getPoints()
    { return points; }
}
