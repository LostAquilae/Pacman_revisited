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
public class Fruit extends Consumable
{
    public Fruit(Level level, int points)
    {
        super(level);
        this.points = points;
    }

    @Override
    public void consume()
    {
        // Display fruit
    }
}
