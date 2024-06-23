/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import model.Direction;
import model.Game;
import model.Pacman;
import view.View;

/**
 *
 * @author Rutabaga
 */
public class Controller
{
    Game game;
    View view;
    
    public Controller(Game game, View view)
    {
        this.game = game;
        this.view = view;
        
        addKeyboardListener();
    }
    
    private void addKeyboardListener()
    { 
        view.addKeyListener(new KeyAdapter() // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                Pacman pacman = game.getPacman();
                
                switch(e.getKeyCode()) // on écoute les flèches de direction du clavier
                {
                    case KeyEvent.VK_LEFT : pacman.setNextDirection(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : pacman.setNextDirection(Direction.droite); break;
                    case KeyEvent.VK_DOWN : pacman.setNextDirection(Direction.bas); break;
                    case KeyEvent.VK_UP : pacman.setNextDirection(Direction.haut); break;
                }
            }
        });
    }
}
