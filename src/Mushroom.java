import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class Mushroom extends Sprite implements Constraints {
    private final String mushroomImagePath = "images/mushroom.png";
    private final String mushroomHit1ImagePath = "images/mushroomHit1.png";
    private final String mushroomHit2ImagePath = "images/mushroomHit2.png";
    public int hitCount;
    public int initX;
    public int initY;

    public Mushroom(int x, int y) {
        initX = x;
        initY = y;
        hitCount = 0;
        updateImage();
        setX(x);
        setY(y);
        setVisible(true);
    }

    public int restore() {
        int hcPrev = hitCount;
        setX(initX);
        setY(initY);
        hitCount = 0;
        updateImage();
        setDying(false);
        setVisible(true);
        if(hcPrev > 0) return 1;
        else return 0;
    }

    public void update(String dir) {  }

    private void updateImage() {
        ImageIcon icon = null;
        switch(hitCount) {
            case 0:
                icon = new ImageIcon(mushroomImagePath);
                setImage(icon.getImage());
                break;
            case 1:
                icon = new ImageIcon(mushroomHit1ImagePath);
                setImage(icon.getImage());
                break;
            case 2:
                icon = new ImageIcon(mushroomHit2ImagePath);
                setImage(icon.getImage());
                break;
            default:
                icon = new ImageIcon(mushroomImagePath);
                setImage(icon.getImage());
                break;
        }
    }

    public void gotShot() {
        hitCount = hitCount + 1;
        if (hitCount == 3) {
            this.setDying(true);
        } else {
            updateImage();
        }
    }
}
