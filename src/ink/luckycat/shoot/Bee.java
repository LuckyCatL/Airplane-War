package ink.luckycat.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 小蜜蜂
 */
public class Bee extends FlyingObject {
    private int xSpeed;
    private int ySpeed;
    private int awardType; // 奖励类型

    public Bee() {
        super(60, 51);
        xSpeed = 1;
        ySpeed = 2;

        Random random = new Random();
        awardType = random.nextInt(2);
    }

    private int index = 2;
    private int i = 0;

    public BufferedImage getImage() {
        if (isLive()) {
            return Images.bees[i++ % 2];
        } else if (isDead()) {
            BufferedImage img = Images.bees[index++];
            if (index == Images.airs.length) {
                state = REMOVE;
            }
            return img;
        }
        return null;
    }
}
