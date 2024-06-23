package model;

import java.awt.Point;
import java.util.ArrayList;
import static model.Direction.*;
import static model.GhostMode.*;

public class Ghost extends Character
{
    protected static Direction[] allDirections = { haut, bas, gauche, droite };
    private ArrayList<Direction> possibleDirections;
    private Point target;
    private Point scatterTarget;
    private Direction oldDirection;
    private boolean turnOver;
    private GhostName name;
    
    public Ghost(Level level, GhostName name, int x, int y)
    {
        super(level, x, y);
        direction = droite;
        oldDirection = droite;
        turnOver = false;
        
        this.name = name;
        switch(name)
        {
            case Blinky: scatterTarget = new Point(level.SIZE_X-3, 0); break;
            case Pinky: scatterTarget = new Point(3, 0); break;
            case Inky: scatterTarget = new Point(level.SIZE_X-1, level.SIZE_Y - 1); break;
            case Clyde: scatterTarget = new Point(0, level.SIZE_Y-1); break;
        }
    }
    
    public GhostName getName()
    { return name; }
    
    public void turnOver()
    { turnOver = true; }
    
    private void selectTarget()
    {
        if(ghostManager.getGhostMode() == Scatter)
            target = scatterTarget;
        else
        {
            switch(name)
            {
                case Blinky:
                    target = game.getPacman().getPosition();
                    break;
                
                case Pinky:
                    target = game.getPacman().getPosition();
                    for(int i = 0; i < 4; i++)
                        target = game.getPacman().getNextPosition(game.getPacman().getDirection(), true);
                    break;
                    
                case Inky:
                    Point blinkyPos = ghostManager.getBlinky().getPosition();
                    Point difference = new Point(game.getPacman().getPosition().x - blinkyPos.x, game.getPacman().getPosition().y - blinkyPos.y);
                    target = new Point(blinkyPos.x + difference.x * 2, blinkyPos.y + difference.y * 2);
                    
                    if(target.x < 0) target.x = 0;
                    else if(target.x >= level.SIZE_X) target.x = level.SIZE_X - 1;
                    
                    if(target.y < 0) target.y = 0;
                    else if(target.y >= level.SIZE_Y) target.y = level.SIZE_Y - 1;
                    
                    break;
                
                case Clyde:
                    target = game.getPacman().getPosition();
                    if(target.distance(position) < 8)
                        target = scatterTarget;
                    break;
            }
        }
    }
    
    private void AI()
    {
        possibleDirections = new ArrayList<>();
        for(Direction dir : allDirections)
        {
            if(level.canMoveOn(getNextPosition(dir)))
                possibleDirections.add(dir);
        }
        
        if(possibleDirections.size() > 1)
                possibleDirections.remove(oldDirection.inverse());
        
        if(possibleDirections.size() >= 2) // Means that the ghost is at an intersection
        {
            selectTarget();
            
            GhostMode currentMode = ghostManager.getGhostMode();
            if(currentMode == Frightened)
                FrightenedAI();
            else
                ChaseAI();
        }
        else if(possibleDirections.size() > 0)
        {
            direction = possibleDirections.get(0);
        }
    }
    
    private void ChaseAI()
    {
        double minDistance = getNextPosition(possibleDirections.get(0)).distance(target);
        direction = possibleDirections.get(0);
        for(Direction dir : possibleDirections)
        {
            double distance = getNextPosition(dir).distance(target);
            if(distance < minDistance)
            {
                minDistance = distance;
                direction = dir;
            }
        }
    }
    
    private void FrightenedAI()
    {
        do
        {
            int random = (int)(Math.random() * possibleDirections.size());
            direction = possibleDirections.get(random);
        } while(direction == oldDirection.inverse());
    }

    @Override
    public boolean move()
    {
        if(turnOver)
        {
            direction = direction.inverse();
            turnOver = false;
        }
        else
            AI();
        
        // Actually moves
        if(super.move())
            oldDirection = direction;
        else
            return false;

        // Kill pacman
        if(sitOn instanceof Character)
        {
            if(sitOn instanceof Pacman)
            {
                if(ghostManager.getGhostMode() == Frightened)
                {
                    this.kill();
                }
                else
                    game.loose();
            }
            sitOn = null;
        }
        
        return true;
    }
    
    public void kill()
    {
        move(start_position);
        sitOn = null;
        game.notify("eatghost");
        game.addScore(ghostManager.getGhostScore());
    }
}
