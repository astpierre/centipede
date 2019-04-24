import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import java.lang.Math;
import java.util.Random;

public class Spider extends Sprite implements Constraints {
    private final String spiderImagePath = "images/spider.png";
    private final String spiderHit1ImagePath = "images/spiderHit1.png";
    public final int bottomBarrier = GROUND + 100 + PLAYER_HEIGHT;// - 5;
    public final int rightBarrier = BOARD_WIDTH - SPIDER_WIDTH;
    public int hitCount;
    public int dx = -SPIDER_SPEED;
    public String direction = "L";
    private int initX;
    private int initY;


    public Spider(int x, int y) {
        initX = x;
        initY = y;
        hitCount = 0;
        setX(x);
        setY(y);
        updateImage();
        setVisible(true);
        direction = "L";
    }

    public void restore() {
        setX(initX);
        setY(initY);
        hitCount = 0;
        setDying(false);
        updateImage();
        setVisible(true);
        direction = "L";
    }

    public void update(String dir) {
        if(direction == "L") {
            this.x -= SPIDER_SPEED;
            this.y += SPIDER_SPEED-3;
        } else if(direction == "R") {
            this.x += SPIDER_SPEED;
            this.y += SPIDER_SPEED+4;
        } else if(direction == "U") {
            this.x += SPIDER_SPEED-2;
            this.y -= SPIDER_SPEED;
        }
    }

    public void changeLevel() {
        Random r = new Random();
        int chance = r.nextInt(100);
        if(chance > 75) {
            this.setX(this.x - SPIDER_HEIGHT);
            this.setY(this.y + SPIDER_HEIGHT);
        } else {
            this.setX(this.x + SPIDER_WIDTH);
            this.setY(this.y - SPIDER_WIDTH);
        }
    }

    public void goUp() {
        this.setY(this.y - SPIDER_HEIGHT);
        this.direction = "U";
    }

    public void goRight() {
        this.x += SPIDER_SPEED;
        this.direction = "R";
    }

    public void goLeft() {
        this.x -= SPIDER_SPEED;
        this.direction = "L";
    }

    private void updateImage() {
        ImageIcon icon = null;
        switch(hitCount) {
            case 0:
                icon = new ImageIcon(spiderImagePath);
                setImage(icon.getImage());
                break;
            case 1:
                icon = new ImageIcon(spiderHit1ImagePath);
                setImage(icon.getImage());
                break;
            case 2:
                setDying(true);
        }
    }

    public void gotShot() {
        hitCount = hitCount + 1;
        updateImage();
    }

    public void setInitialImage() {
        updateImage();
        hitCount = 0;
        setVisible(true);
    }
}
