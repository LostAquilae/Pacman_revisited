package model;

import java.awt.Point;

public abstract class Character extends Block
{
    protected GhostManager ghostManager;
    protected Direction direction;
    protected Block sitOn;
    protected Point position;
    protected Point start_position;
    
    public Character(Level level, int x, int y)
    {
        super(level);
        this.position = new Point(x, y);
        this.start_position = new Point(x, y);
        isBlocking = false;
    }
    
    public void setGhostManager(GhostManager ghostManager)
    { this.ghostManager = ghostManager; }
    
    public Point getPosition()
    { return position; }
    
    public Direction getDirection()
    { return direction; }
    
    public boolean move()
    { return move(getNextPosition()); }
    
    public boolean move(Point nextPosition)
    {     
        if(!level.canMoveOn(nextPosition))
            return false;
        
        level.setInGrid(position, sitOn);
        
        sitOn = level.getInGrid(nextPosition);
        
        level.setInGrid(nextPosition, this);
        
        position = nextPosition;
        
        return true;
    }
    
    public Point getNextPosition()
    { return getNextPosition(this.direction, false); }
    
    public Point getNextPosition(Direction direction)
    { return getNextPosition(direction, false); }
    
    public Point getNextPosition(Direction direction, boolean block)
    {
        Point newPosition;
        switch(direction)
        {
            case haut:
                newPosition = new Point(position.x, position.y - 1);
                if(newPosition.y < 0)
                {
                    if(!block) newPosition.y = level.SIZE_Y - 1;
                    else newPosition.y = 0;                        
                }
            break;
            
            case bas:
                newPosition = new Point(position.x, position.y + 1);
                if(newPosition.y >= level.SIZE_Y)
                {
                    if(!block) newPosition.y = 0;
                    else newPosition.y = level.SIZE_Y - 1;
                }
            break;
            
            case gauche: 
                newPosition = new Point(position.x - 1, position.y);
                if(newPosition.x < 0)
                {
                    if(!block) newPosition.x = level.SIZE_X - 1;
                    else newPosition.x = 0;
                }
            break;
            
            
            case droite:
                newPosition = new Point(position.x + 1, position.y);
                if(newPosition.x >= level.SIZE_X)
                {
                    if(!block) newPosition.x = 0;
                    else newPosition.y = level.SIZE_X - 1;
                }
            break;
            
            default: return null;
        }
        
        return newPosition;
    }
    
    //Set the position
    public void setPosition(Point p)
    { this.position = p; }
    
    public void setSitOn(Block sitOn)
    { this.sitOn = sitOn; }
    
    //Get the start Position
    public Point getStartPosition()
    { return this.start_position; }
    
    public Block sitOn()
    { return sitOn; }
}
