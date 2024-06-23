package view;

import model.Energizer;
import model.GhostName;
import model.Ghost;
import model.Game;
import model.Pacman;
import model.Block;
import model.GhostMode;
import model.Wall;
import model.Dot;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Cette classe a deux fonctions :
  (1) View : proposer une représentation graphique de l'application (cases graphiques, etc.)
  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 * @author freder
 */
public class View extends JFrame implements Observer
{
    private Game jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    
    private int SIZE_X;
    private int SIZE_Y;
    
    private final int BLOCK_SIZE_X = 14;
    private final int BLOCK_SIZE_Y = 14;

    private ImageIcon icoPacman[] = new ImageIcon[4];
    private ImageIcon icoBlinky[] = new ImageIcon[4];
    private ImageIcon icoPinky[] = new ImageIcon[4];
    private ImageIcon icoInky[] = new ImageIcon[4];
    private ImageIcon icoClyde[] = new ImageIcon[4];
    private ImageIcon icoFrightenedGhost;
    private ImageIcon icoEmpty;
    private ImageIcon icoWall;
    private ImageIcon icoDot;
    private ImageIcon icoEnergizer;

    private JLabel[][] gridLabels; // cases graphique (au moment du rafraichissement, chaque case va être associé à une icône, suivant ce qui est présent dans la partie modèle)
    
    private JComponent livesPanel = new JPanel(new FlowLayout());
    private JLabel[] liveLabels = new JLabel[3];
    private JLabel scoreLabel = new JLabel();
    private JLabel highscoreLabel = new JLabel();
    private JLabel levelLabel = new JLabel();
    
    private SoundManager soundManager;

    private Pacman pacman;
    
    public View(Game jeu)
    {
        this.jeu = jeu;
        
        SIZE_X = jeu.getLevel().SIZE_X;
        SIZE_Y = jeu.getLevel().SIZE_Y;
        
        pacman = jeu.getPacman();
        soundManager = new SoundManager();

        loadIcons();
        buildFrame();
    }

    private void loadIcons() {
        String imageFolderPath = "src/Images/";
        icoPacman[0] = loadIcon(imageFolderPath + "up_pacman.png");
        icoPacman[1] = loadIcon(imageFolderPath + "down_pacman.png");
        icoPacman[2] = loadIcon(imageFolderPath + "left_pacman.png");
        icoPacman[3] = loadIcon(imageFolderPath + "right_pacman.png");
        icoEmpty = loadIcon(imageFolderPath + "empty.png");
        icoBlinky[0] = loadIcon(imageFolderPath + "up_blinky.png");
        icoBlinky[1] = loadIcon(imageFolderPath + "down_blinky.png");
        icoBlinky[2] = loadIcon(imageFolderPath + "left_blinky.png");
        icoBlinky[3] = loadIcon(imageFolderPath + "right_blinky.png");
        icoPinky[0] = loadIcon(imageFolderPath + "up_pinky.png");
        icoPinky[1] = loadIcon(imageFolderPath + "down_pinky.png");
        icoPinky[2] = loadIcon(imageFolderPath + "left_pinky.png");
        icoPinky[3] = loadIcon(imageFolderPath + "right_pinky.png");
        icoInky[0] = loadIcon(imageFolderPath + "up_inky.png");
        icoInky[1] = loadIcon(imageFolderPath + "down_inky.png");
        icoInky[2] = loadIcon(imageFolderPath + "left_inky.png");
        icoInky[3] = loadIcon(imageFolderPath + "right_inky.png");
        icoClyde[0] = loadIcon(imageFolderPath + "up_clyde.png");
        icoClyde[1] = loadIcon(imageFolderPath + "down_clyde.png");
        icoClyde[2] = loadIcon(imageFolderPath + "left_clyde.png");
        icoClyde[3] = loadIcon(imageFolderPath + "right_clyde.png");
        icoFrightenedGhost = loadIcon(imageFolderPath + "frightened_ghost.png");
        icoWall = loadIcon(imageFolderPath + "wall.png");
        icoDot = loadIcon(imageFolderPath + "dot.png");
        icoEnergizer = loadIcon(imageFolderPath + "energizer.png");
    }

    private ImageIcon loadIcon(String urlIcone) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ImageIcon(image);
    }

    private void buildFrame()
    {
        setTitle("PacMan");
        setSize(SIZE_X * BLOCK_SIZE_X + 50, SIZE_Y * BLOCK_SIZE_Y + 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent gridPanel = new JPanel(new GridLayout(SIZE_Y, SIZE_X, 0, 0)); // gridPanel va contenir les cases graphiques et les positionner sous la forme d'une grille
        gridPanel.setPreferredSize(new Dimension(SIZE_X * BLOCK_SIZE_X, SIZE_Y * BLOCK_SIZE_Y));
        
        gridLabels = new JLabel[SIZE_X][SIZE_Y];
        
        for (int y = 0; y < SIZE_Y; y++)
        {
            for (int x = 0; x < SIZE_X; x++)
            {
                JLabel jlab = new JLabel();
                gridLabels[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                gridPanel.add(jlab);
            }
        }
        
        JComponent interfacePanel = new JPanel(new BorderLayout());
        
        JComponent scoresPanel = new JPanel(new BorderLayout());
        scoresPanel.add(scoreLabel, BorderLayout.NORTH);
        scoresPanel.add(highscoreLabel, BorderLayout.SOUTH);
        
        JComponent eastInterfacePanel = new JPanel(new BorderLayout());
        eastInterfacePanel.add(levelLabel, BorderLayout.NORTH);
        eastInterfacePanel.add(livesPanel, BorderLayout.SOUTH);
        
        interfacePanel.add(scoresPanel, BorderLayout.WEST);
        interfacePanel.add(eastInterfacePanel, BorderLayout.EAST);
        
        updateInterface();
        
        JComponent flowPanel = new JPanel(new FlowLayout());
        flowPanel.add(gridPanel);
        
        JComponent generalPanel = new JPanel(new BorderLayout());
        generalPanel.add(flowPanel, BorderLayout.CENTER);
        generalPanel.add(interfacePanel, BorderLayout.SOUTH);
        
        this.add(generalPanel);
    }
    
    private void updateInterface()
    {
        scoreLabel.setText("Score : " + jeu.getScore());
        highscoreLabel.setText("Highscore : " + Math.max(jeu.getScore(), jeu.getLevel().getHighscore()));
        levelLabel.setText("Level " + jeu.getLevelNumber());
        
        livesPanel.removeAll();
        livesPanel.setLayout(new FlowLayout());
        livesPanel.add(new JLabel("Lives : "));
        for(int i = 0; i < jeu.getLives(); i++)
        {
            livesPanel.add(new JLabel(icoPacman[3]));
        }
    }
    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void updateGrid()
    {
        for (int x = 0; x < SIZE_X; x++)
        {
            for (int y = 0; y < SIZE_Y; y++)
            {
                Block block = jeu.getInGrid(new Point(x, y));
                Icon icon = icoEmpty;
                
                if(block instanceof Wall)
                { icon = icoWall; }
                else if(block instanceof Dot)
                { icon = icoDot; }
                else if(block instanceof Energizer)
                { icon = icoEnergizer; }
                else if (block instanceof model.Character)
                {
                    int index = 2;
                    switch(((model.Character)block).getDirection())
                    {
                        case haut: index = 0; break;
                        case bas: index = 1; break;
                        case gauche: index = 2; break;
                        case droite: index = 3; break;
                    }
                    
                    if(block instanceof Pacman)
                        icon = icoPacman[index];
                    else if(block instanceof Ghost)
                    {
                        if(jeu.getGhostManager().getGhostMode() == GhostMode.Frightened)
                            icon = icoFrightenedGhost;
                        else
                        {
                            GhostName ghostName = ((Ghost)block).getName();
                            if(ghostName == GhostName.Pinky)
                                icon = icoPinky[index];
                            else if(ghostName == GhostName.Inky)
                                icon = icoInky[index];
                            else if(ghostName == GhostName.Clyde)
                                icon = icoClyde[index];
                            else
                                icon = icoBlinky[index];
                        }
                    }
                }
                
                gridLabels[x][y].setIcon(icon);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if(arg == null)
        {
            updateGrid();
            updateInterface();
        }
        else if (arg instanceof String)
        {
            soundManager.updateSounds((String)arg);
        }
    }
}
