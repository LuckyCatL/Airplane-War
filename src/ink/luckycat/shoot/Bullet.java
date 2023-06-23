package ink.luckycat.shoot;

import java.awt.image.BufferedImage;

/**
 * 子弹
 */
public class Bullet extends FlyingObject {
    private int speed;

    public Bullet(int x, int y) {
        super(8, 20, x, y);

        speed = 3;
    }

    public BufferedImage getImage() {
        if (isLive()) {
            return Images.bullet;
        } else if (isDead()) {
            state = REMOVE;
        }
        return null;
    }

    public void step() {
        y -= speed;
    }

    // 重新越界判断

    @Override
    public boolean isOutBounds() {
        return y + height <= 0;
    }
}
