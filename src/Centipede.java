import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class Centipede extends Sprite implements Constraints {
    private final String centipedeHeadRImagePath = "images/centipedeHeadR.png";
    private final String centipedeHeadLImagePath = "images/centipedeHeadL.png";
    private final String centipedeHeadSickImagePath = "images/centipedeHeadSick.png";
    private final String centipedeBodyImagePath = "images/centipedeBody.png";
    public int hitCount;
    public int cDir = -CENTIPEDE_SPEED;

    public Centipede(int x, int y) {
        initCentipede(x, y);
    }

    private void initCentipede(int x, int y) {
        ImageIcon centipedeHeadIcon = new ImageIcon(centipedeHeadLImagePath);
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
            ImageIcon centipedeIcon = new ImageIcon(centipedeHeadSickImagePath);
            setImage(centipedeIcon.getImage());
        }
    }

    public void setInitialImage() {
        ImageIcon centipedeHeadIcon = new ImageIcon(centipedeHeadLImagePath);
        setImage(centipedeHeadIcon.getImage());
        hitCount = 0;
        setVisible(true);
    }
}
