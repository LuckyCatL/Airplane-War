package ink.luckycat.shoot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 图片工具类
 */
public class Images {
    public static BufferedImage sky;    // 天空图片
    public static BufferedImage bullet; // 子弹图片
    public static BufferedImage[] heros; // 英雄机图片的数组
    public static BufferedImage[] airs; // 小敌机（包括爆炸图片）
    public static BufferedImage[] bairs; // 大敌机
    public static BufferedImage[] bees; // 小蜜蜂
    public static BufferedImage start;
    public static BufferedImage pause;
    public static BufferedImage gameover;

    // 图片初始化
    static {
        sky = readImage("background.png");
        bullet = readImage("bullet.png");
        heros = new BufferedImage[]{readImage("hero0.png"), readImage("hero1.png")};

        airs = new BufferedImage[6];
        bairs = new BufferedImage[6];
        bees = new BufferedImage[6];

        airs[0] = readImage("airplane0.png");
        airs[1] = readImage("airplane1.png");
        bairs[0] = readImage("bigairplane0.png");
        bairs[1] = readImage("bigairplane1.png");
        bees[0] = readImage("bee0.png");
        bees[1] = readImage("bee1.png");

        for (int i = 2; i < airs.length; i++) {
            airs[i] = bairs[i] = bees[i] = readImage("bom" + (i - 1) + ".png");
        }

        start = readImage("start.png");
        pause = readImage("pause.png");
        gameover = readImage("gameover.png");
    }

    // 读取图片
    public static BufferedImage readImage(String fileName) {
        try {
            BufferedImage img = ImageIO.read(FlyingObject.class.getResource("static/" + fileName));
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
