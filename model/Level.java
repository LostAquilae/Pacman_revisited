package model;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level
{
    private Game game;
    private static String levelPath;
    private int dots;
    private int high_score = 0;
    
    public int SIZE_X = 10;
    public int SIZE_Y = 10;
    
    private ArrayList<ArrayList<Block>> blockGrid;
    
    public Level(Game game, int levelNumber)
    {
        this.game = game;
        levelPath = "./src/levels/level_" + levelNumber + ".json";
    }
    
    public ArrayList<Ghost> load() throws Exception
    {
        try
        {
            Pacman pacman = null;
            ArrayList<Ghost> ghosts = new ArrayList<>();
            
            if(blockGrid != null)
                blockGrid.clear();
            else
                blockGrid = new ArrayList<ArrayList<Block>>();
            
            boolean onePacman = false;
            boolean hasBlinky = false;
            dots = 0;
            
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(new FileReader(levelPath));
            JSONObject size_array = (JSONObject) object.get("size");
            SIZE_X = Math.toIntExact((long)size_array.get("x"));
            SIZE_Y = Math.toIntExact((long)size_array.get("y"));
            JSONArray block_array = (JSONArray) object.get("blocks");
            JSONObject score_object = (JSONObject) object.get("score");
            high_score = Math.toIntExact((long)score_object.get("high_score"));
            for (int i = 0; i < block_array.size(); i++)
            {
                ArrayList<Block> blockRow = new ArrayList<>();
                JSONArray row = (JSONArray)block_array.get(i);
                for(int j = 0; j < row.size(); j++)
                {
                    Ghost ghost;
                    switch((String)row.get(j))
                    {
                        case "empty": blockRow.add(null); break;
                        case "wall": blockRow.add(new Wall(this)); break;
                        case "dot":
                            blockRow.add(new Dot(this));
                            dots += 1;
                            break;
                        case "energizer":
                            blockRow.add(new Energizer(this));
                            dots += 1;
                            break;
                        case "fruit": blockRow.add(new Fruit(this, 100)); break;
                        
                        case "blinky":
                            hasBlinky = true;
                            ghost = new Ghost(this, GhostName.Blinky, i, j);
                            ghosts.add(0, ghost);
                            blockRow.add(ghost);
                            break;
                            
                        case "pinky":
                            ghost = new Ghost(this, GhostName.Pinky, i, j);
                            ghosts.add(ghost);
                            blockRow.add(ghost);
                            break;
                            
                        case "inky":
                            ghost = new Ghost(this, GhostName.Inky, i, j);
                            ghosts.add(ghost);
                            blockRow.add(ghost);
                            break;
                            
                        case "clyde":
                            ghost = new Ghost(this, GhostName.Clyde, i, j);
                            ghosts.add(ghost);
                            blockRow.add(ghost);
                            break;
                            
                        case "pacman":
                            if(onePacman) throw new Exception("Level not readable");
                            onePacman = true;
                            pacman = new Pacman(this, i, j);
                            game.setPacman(pacman);
                            blockRow.add(pacman);
                            break;
                            
                        default: throw new Exception("Level not readable");
                    }
                }
                blockGrid.add(blockRow);
            }
            if(!onePacman || !hasBlinky) throw new Exception("Level not readable");
            
            return ghosts;
        }
        catch(FileNotFoundException e)
        { System.out.println("File not found while loading level : " + e.getMessage()); }
        catch(IOException | ParseException e)
        { System.out.println("Exception while loading level : " + e.getMessage()); }
        return null;
    }
    
    //Update the high score
    public void updateHighScore(int score)
    {
        if(score <= high_score)
            return;
        
        JSONParser parser = new JSONParser();
        JSONObject object;
        try
        {
            object = (JSONObject) parser.parse(new FileReader(levelPath));
            JSONObject score_object = (JSONObject) object.get("score");
            score_object.put("high_score", score);
            PrintWriter file = new PrintWriter(levelPath);
            file.print(object.toJSONString());
            file.close();
        }
        catch (Exception ex)
        { System.out.println("Highscore couldn't be upload : " + ex.getMessage()); }
    }
    
    public boolean canMoveOn(Point position)
    { return blockGrid.get(position.x).get(position.y) == null || !blockGrid.get(position.x).get(position.y).isBlocking; }
    
    public ArrayList<ArrayList<Block>> getBlockGrid()
    { return blockGrid; }
    
    public Block getInGrid(Point position)
    { return blockGrid.get(position.x).get(position.y); }
    
    public void setInGrid(Point position, Block block)
    { blockGrid.get(position.x).set(position.y, block); }
    
    public void removeDot()
    {
        this.dots--;
        if(dots <= 0)
            game.winLevel();
    }
    
    public int getHighscore()
    { return high_score; }
    
    public Game getGame()
    { return game; }
}
