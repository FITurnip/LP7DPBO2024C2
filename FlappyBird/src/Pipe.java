import java.awt.*;

public class Pipe {
    // image, position and size
    private int posX, posY;
    private int width, height;
    private Image image;
    // velocity
    private int velocityX;
    // passed condition
    boolean passed;

    // constructor
    public Pipe(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;

        this.velocityX = 1;
        this.passed = false;
    }

    // getter and setter
    public int getPosX() {
        return posX;
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }
    public int getPosY() { return posY; }
    public void setPosY(int posY) {
        this.posY = posY;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public int getVelocityX() {
        return velocityX;
    }
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }
    public boolean isPassed() {
        return passed;
    }
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
