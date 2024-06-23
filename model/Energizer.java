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
public class Energizer extends Consumable
{
    public Energizer(Level level)
    {
        super(level);
        points = 50;
    }

    @Override
    public void consume()
    {
        game.getGhostManager().setGhostsFrightened();
        level.removeDot();
    }
}
