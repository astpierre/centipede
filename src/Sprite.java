import java.awt.Image;

public class Sprite {
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    private Image image;
    private boolean visible;
    protected boolean dying;

    /**
        Basic Sprite constructor
    */
    public Sprite() {
        visible = true;
    }

    /**
        Sets the visible parameter to false when dead.
    */
    public void die() {
        visible = false;
    }

    /**
        Sets visible parameter to boolean value provided.
    */
    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
        Gets this Sprite's current x position.
    */
    public boolean isVisible() {
        return this.visible;
    }

    /**
        Sets this Sprite's current image.
    */
    public void setImage(Image image){
        this.image = image;
    }

    /**
        Gets this Sprite's current image.
    */
    public Image getImage() {
        return this.image;
    }

    /**
        Gets this Sprite's current x position.
    */
    public int getX() {
        return this.x;
    }

    /**
        Gets this Sprite's current y position.
    */
    public int getY() {
        return this.y;
    }

    /**
        Sets this Sprite's current x position.
    */
    public void setX(int x) {
        this.x = x;
    }

    /**
        Sets this Sprite's current y position.
    */
    public void setY(int y) {
        this.y = y;
    }

    /**
        Gets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public int getVelocityX() {
        return dx;
    }

    /**
        Gets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public int getVelocityY() {
        return dy;
    }

    /**
        Sets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityX(int dx) {
        this.dx = dx;
    }

    /**
        Sets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityY(int dy) {
        this.dy = dy;
    }

    /**
        Sets the Sprite to dying state.
    */
    public void setDying(boolean dying) {
        this.dying = dying;
    }

    /**
        Checks if the Sprite is in dying state.
    */
    public boolean isDying() {
        return this.dying;
    }
}
