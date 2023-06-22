package ink.luckycat.shoot;

import java.util.Random;
import java.awt.image.BufferedImage;
/**
 * 飞行物
 */
public abstract class FlyingObject {

    public static final int LIVE = 0;   // 活着的
    public static final int DEAD = 1;   // 死了的
    public static final int REMOVE = 2; // 删除的

    // 当前状态(默认活着)
    protected int state = LIVE;

    protected int width; // 宽
    protected int height; // 高
    protected int x;  // 坐标x
    protected int y;  // 坐标y

    // 小敌机 大敌机 小蜜蜂初始化
    public FlyingObject(int width, int height) {
        this.width = width;
        this.height = height;

        // 随机初始位置
        Random random = new Random();
        x = random.nextInt(World.WIDTH - width);
        y = height;
    }

    // 天空 英雄机 子弹初始化
    public FlyingObject(int width, int height, int x, int y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    // 获取子类对应的图片
    public abstract BufferedImage getImage();

    // 判断是否活着
    public boolean isLive(){
        return state == LIVE;
    }

    // 判断是否死了
    public boolean isDead(){
        return state == DEAD;
    }

    // 判断是否要删除
    public boolean isRemove(){
        return state == REMOVE;
    }
}
