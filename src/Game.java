import java.awt.EventQueue;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Point;
import javax.swing.JFrame;

public class Game extends JFrame implements Constraints {
    public Game(int mushroomSeed) {
        /* Initialize the game board */
        add(new Board(mushroomSeed));
        setTitle("Centipede");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);

        /* Make the cursor invisible */
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        getContentPane().setCursor(blankCursor);

        /* Misc. settings */
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String [] args) {
        EventQueue.invokeLater(() -> {
            int mushroomSeed = Integer.parseInt(args[0]);
            System.out.print("Frequency of mushroom placement: "+mushroomSeed+"\n");
            Game ex = new Game(mushroomSeed);
            ex.setVisible(true);
        });
    }
}
