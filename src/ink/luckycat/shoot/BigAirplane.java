package ink.luckycat.shoot;

import java.awt.image.BufferedImage;

/**
 * 大敌机
 */
public class BigAirplane extends FlyingObject implements EnemyScore {
    private int speed; // 移动速度

    public BigAirplane() {
        super(60, 89);
        speed = 2;
    }

    private int index = 2;
    private int i = 0;

    public BufferedImage getImage() {
        if (isLive()) {
            return Images.bairs[i++ % 2];
        } else if (isDead()) {
            BufferedImage img = Images.bairs[index++];
            if (index == Images.airs.length) {
                state = REMOVE;
            }
            return img;
        }
        return null;
    }

    public void step() {
        y += speed;
    }

    public int getScore(){
        return 3; // 大敌机得分
    }
}
