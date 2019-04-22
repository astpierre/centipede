import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class Centipede extends Sprite implements Constraints {
    private final String headRightImagePath = "images/centipedeHeadR.png";
    private final String headLeftImagePath = "images/centipedeHeadL.png";
    private final String sickRightImagePath = "images/centipedeHeadSickR.png";
    private final String sickLeftImagePath = "images/centipedeHeadSickL.png";
    public final int bottomBarrier = GROUND - PLAYER_HEIGHT - 5;
    public final int rightBarrier = BOARD_WIDTH - CENTIPEDE_HEIGHT;
    public int hitCount;
    public int dx = -CENTIPEDE_SPEED;
    public String direction = "L";

    public Centipede(int x, int y) {
        updateImage("L");
        hitCount = 0;
        setVisible(true);
        direction = "L";
    }

    public void update(String dir) {
        if(direction == "L") {
            this.x -= CENTIPEDE_SPEED;
        } else if(direction == "R") {
            this.x += CENTIPEDE_SPEED;
        }
    }

    public void downLevel() {
        this.setY(this.y + CENTIPEDE_HEIGHT);
    }

    public void goRight() {
        this.x += CENTIPEDE_SPEED;
        this.direction = "R";
        switch(hitCount) {
            case 1:
                updateImage("SR");
                break;
            default:
                updateImage("R");
        }
    }

    public void goLeft() {
        this.x -= CENTIPEDE_SPEED;
        this.direction = "L";
        switch(hitCount) {
            case 1:
                updateImage("SL");
                break;
            default:
                updateImage("L");
        }
    }

    private void updateImage(String mode) {
        ImageIcon icon = null;
        switch(mode) {
            case "L" :
                icon = new ImageIcon(headLeftImagePath);
                setImage(icon.getImage());
                break;
            case "R" :
                icon = new ImageIcon(headRightImagePath);
                setImage(icon.getImage());
                break;
            case "SL" :
                icon = new ImageIcon(sickLeftImagePath);
                setImage(icon.getImage());
                break;
            case "SR" :
                icon = new ImageIcon(sickRightImagePath);
                setImage(icon.getImage());
                break;
        }
    }

    public void gotShot() {
        hitCount = hitCount + 1;
        if (hitCount == 1) {
            switch(direction) {
                case "R":
                    updateImage("SR");
                    break;
                case "L":
                    updateImage("SL");
                    break;
            }
        } else if (hitCount == 2) {
            this.setDying(true);
        }
    }

    public void setInitialImage() {
        updateImage("L");
        hitCount = 0;
        setVisible(true);
    }
}
