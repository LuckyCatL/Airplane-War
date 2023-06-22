package ink.luckycat.shoot;

import java.awt.image.BufferedImage;

/**
 * 小敌机
 */
public class Airplane extends FlyingObject {
    private int speed; // 移动速度

    public Airplane() {
        super(48, 50);
        speed = 2;
    }

    private int index = 2;
    private int i = 0;

    public BufferedImage getImage() {
        if (isLive()) {
            return Images.airs[i++ % 2];
        } else if (isDead()) {
            BufferedImage img = Images.airs[index++];
            if (index == Images.airs.length) {
                state = REMOVE;
            }
            return img;
        }
        return null;
    }
}
