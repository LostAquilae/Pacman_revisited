package model;

public class Pacman extends Character
{
    private Direction nextDirection;
    
    public Pacman(Level level, int x, int y)
    {
        super(level, x, y);
        direction = Direction.droite;
        nextDirection = Direction.droite;
    }
    
    public void setNextDirection(Direction nextDirection)
    { this.nextDirection = nextDirection; }
    
    @Override
    public boolean move()
    {
        if(level.canMoveOn(getNextPosition(nextDirection)))
            direction = nextDirection;
        
        if(!super.move())
            return false;
        
        if(sitOn instanceof Ghost)
        {
            Ghost ghost = ((Ghost)sitOn);
            sitOn = ghost.sitOn();
            if(ghostManager.getGhostMode() == GhostMode.Frightened) // kill ghost
                ghost.kill();
            else
                game.loose();
        }
        
        if(sitOn instanceof Consumable)
        {
            ((Consumable)sitOn).consume();
            sitOn = null;
            game.notify("chomp");
        }
        
        return true;
    }
}
