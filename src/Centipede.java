import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class Centipede extends Sprite implements Constraints {
    private final String centipedeHeadImagePath = "images/centipedeHead.png";
    private final String centipedeBodyImagePath = "images/centipedeBody.png";
    public int hitCount;
    public int cDir = -CENTIPEDE_SPEED;

    public Centipede(int x, int y) {
        initCentipede(x, y);
    }

    private void initCentipede(int x, int y) {
        ImageIcon centipedeHeadIcon = new ImageIcon(centipedeHeadImagePath);
        setImage(centipedeHeadIcon.getImage());
        hitCount = 0;
        setVisible(true);
    }

    public void act(int dir) {
        this.x += dir;
    }

    public void switchState() {
        hitCount++;

        if (hitCount == 2) {
            this.setDying(true);
        }
        else if (hitCount == 1) {
            ImageIcon centipedeBodyIcon = new ImageIcon(centipedeBodyImagePath);
            setImage(centipedeBodyIcon.getImage());
        }
    }

    public void setInitialImage() {
        ImageIcon centipedeHeadIcon = new ImageIcon(centipedeHeadImagePath);
        setImage(centipedeHeadIcon.getImage());
        hitCount = 0;
        setVisible(true);
    }
}
