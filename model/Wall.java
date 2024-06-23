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
public class Wall extends Block
{
    public Wall(Level level)
    {
        super(level);
        isBlocking = true;
    }
}
