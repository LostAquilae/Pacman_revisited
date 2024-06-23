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
public class Dot extends Consumable
{
    public Dot(Level level)
    {
        super(level);
        points = 10;
    }
    
    @Override
    public void consume()
    {
        super.consume();
        level.removeDot();
    }
}
