package ink.luckycat.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 小蜜蜂
 */
public class Bee extends FlyingObject implements EnemyAward {
    private int xSpeed;
    private int ySpeed;
    private int awardType; // 奖励类型
    private Random random = new Random();
    private int rand = random.nextInt(2) == 0 ? 1 : -1;

    public Bee() {
        super(60, 51);
        xSpeed = 1;
        ySpeed = 2;

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

    // 移动方式
    public void step() {
        x += rand * xSpeed;
        y += ySpeed;
        if (x <= 0 || x + width >= World.WIDTH) {
            xSpeed *= -1;
        }
    }

    // 奖励（重写接口）
    public int getAwardType(){
        return awardType;
    }
}
