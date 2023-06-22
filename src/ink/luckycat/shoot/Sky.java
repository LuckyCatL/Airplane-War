package ink.luckycat.shoot;

import java.awt.image.BufferedImage;
/**
 * 天空
 */
public class Sky extends FlyingObject {
    private int speed;
    private int y1; // 第二个天空图片的y坐标

    public Sky() {
        super(World.WIDTH, World.Height, 0, 0);
        speed = 1;
        y1 = -height;
    }

    public BufferedImage getImage(){
       return Images.sky;
    }

    public int getY1(){
        return y1;
    }

}

