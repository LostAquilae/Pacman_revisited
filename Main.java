import view.View;
import model.Game;
import controller.Controller;

public class Main {
    public static void main(String[] args) {
        
        Game game = new Game();
        
        View view = new View(game);
        
        Controller controller = new Controller(game, view);
        
        game.addObserver(view);
        
        view.setVisible(true);
        
        game.start();
    }
}
