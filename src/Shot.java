import javax.swing.ImageIcon;

public class Shot extends Sprite {
    private final String shotImagePath = "images/shot.png";
    private final int SHOT_WIDTH = 6;
    private final int SHOT_HEIGHT = 1;

    public Shot() {
    }

    public Shot(int x, int y) {
        ImageIcon shotIcon = new ImageIcon(shotImagePath);
        setImage(shotIcon.getImage());
        setX(x + SHOT_WIDTH);
        setY(y + SHOT_HEIGHT);
    }
}
