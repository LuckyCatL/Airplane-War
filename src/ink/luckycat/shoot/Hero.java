package ink.luckycat.shoot;

import java.awt.image.BufferedImage;

/**
 * 英雄机
 */
public class Hero extends FlyingObject {
    private int life;   // 命
    private int fire;   // 火力值

    public Hero() {
        super(97, 139, 140, 400);

        life = 3;
        fire = 0;
    }

    private int index = 0;

    public BufferedImage getImage() {
        return Images.heros[index++ % 2];
    }

    /**
     * 英雄机生成子弹
     */
    public Bullet[] shoot() {
        int xStep = this.width / 4;
        int yStep = 20;
        if (fire > 0) { // 双倍火力
            Bullet[] bs = new Bullet[2];
            bs[0] = new Bullet(x + xStep, y - yStep);
            bs[1] = new Bullet(x + 3 * xStep, y - yStep);
            fire -= 2; // 双倍火力子弹减少
            return bs;
        }
        // 单倍
        Bullet[] bs = new Bullet[1];
        bs[0] = new Bullet(x + 2 * xStep, y - yStep);
        return bs;
    }
}
