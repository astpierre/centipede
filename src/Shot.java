import javax.swing.ImageIcon;

public class Shot extends Sprite {
    private final String shotImagePath = "images/shot.png";
    private final int H_SPACE = 6;
    private final int V_SPACE = 1;

    public Shot() {
    }

    public Shot(int x, int y) {
        ImageIcon shotIcon = new ImageIcon(shotImagePath);
        setImage(shotIcon.getImage());

        setX(x + H_SPACE);
        setY(y + V_SPACE);
    }
}
