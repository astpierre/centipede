import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class Player extends Sprite implements Constraints {
    public final int START_Y = 600;
    public final int START_X = 350;
    private final String playerImagePath = "images/player.png";
    public int lives = 3;
    public boolean gameOver = false;

    public Player() {
        ImageIcon playerIcon = new ImageIcon(playerImagePath);
        setImage(playerIcon.getImage());
        setX(START_X);
        setY(START_Y);
        setVisible(true);
    }

    public void restore() {
        setX(START_X);
        setY(START_Y);
        setVisible(true);
        lives = 3;
        gameOver = false;
    }

    public void update() {
        x += dx;
        y += dy;

        if(x <= 0) {
            x = 0;
        }

        if(x >= BOARD_WIDTH - PLAYER_WIDTH) {
            x = BOARD_WIDTH - PLAYER_WIDTH;
        }

        if(y <= 0){
            y = 0;
        }

        if(y >= BOARD_HEIGHT - PLAYER_HEIGHT) {
            y = BOARD_HEIGHT - PLAYER_HEIGHT;
        }

        dx = 0;
        dy = 0;
    }

    public void loseLife() {
        lives--;
    }

    public void mouseMoved(MouseEvent e) {
        dx = e.getX() - x;
        dy = e.getY() - y;
    }
}
