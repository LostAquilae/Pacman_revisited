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
public class Block
{
    protected Game game;
    protected Level level;
    protected boolean isBlocking;
    
    public Block(Level level)
    {
        this.level = level;
        game = level.getGame();
    }
}


/*
0 = VIDE
1 = MUR
2 = PACMAN
3 = FANTOME
4 = PACGOMME
5 = SUPERPACGOMME
6 = FRUIT
*/
