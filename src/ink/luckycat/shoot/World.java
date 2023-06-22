package ink.luckycat.shoot;

// 窗口的包
/*
    JFrame是Java Swing库中的一个类，用于创建图形用户界面(GUI)应用程序的顶层窗口。它是一个容器，可以包含其他 GUI 元素，并允许通过最小化、最大化和关闭按钮进行控制。
 */

import javax.swing.JFrame;
/*
    JPanel 是 Java Swing 库中的一个类，用于创建容器对象，可以用来包含和组织其他 GUI 元素，例如按钮、标签、文本框、列表框等。JPanel 通常是放置在 JFrame 窗口中的一个子组件，它可以帮助我们组织和布局界面。
 */
import javax.swing.JPanel;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

/**
 * 游戏窗口
 */
public class World extends JPanel {
    public static final int WIDTH = 400;
    public static final int Height = 700;

    // 天空对象
    private Sky sky = new Sky();

    // 英雄机对象
    private Hero hero = new Hero();

    // 敌人数组（向上造型，所有敌军飞机都可以存入这个数组）
    private FlyingObject[] enemies = {};

    // 子弹数组
    private Bullet[] bullets = {};

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        World world = new World();
        frame.add(world);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, Height);
        frame.setVisible(true);

        // 启动
        world.action();
    }

    /**
     * 重写父类paint方法
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        // 天空1
        g.drawImage(sky.getImage(), sky.x, sky.y, null);
        // 天空1上面的天空
        g.drawImage(sky.getImage(), sky.x, sky.getY1(), null);
        g.drawImage(hero.getImage(), hero.x, hero.y, null);
        for (int i = 0; i < enemies.length; i++) {
            g.drawImage(enemies[i].getImage(), enemies[i].x, enemies[i].y, null);
        }
        for (int i = 0; i < bullets.length; i++) {
            g.drawImage(bullets[i].getImage(), bullets[i].x, bullets[i].y, null);
        }
    }

    /**
     * 生成敌机对象
     *
     * @return
     */
    public FlyingObject nextOne() {
        Random random = new Random();
        int type = random.nextInt(20);
        if (type < 5) {
            return new Bee(); // 小蜜蜂
        } else if (type < 13) {
            return new Airplane(); // 小敌机
        } else {
            return new BigAirplane(); // 大敌机
        }
    }

    /**
     * 启动游戏执行
     */
    public void action() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                    enterAction(); // 敌人入场
                    shootAction(); // 子弹入场
                    repaint(); // 重新绘画窗口
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 敌人入场
     */
    private int enterIndex = 0; // 敌人入场计数

    public void enterAction() {
        enterIndex++;
        if (enterIndex % 40 == 0) { // 400毫秒生成一个敌人
            FlyingObject obj = nextOne(); // 获取敌人对象
            enemies = Arrays.copyOf(enemies, enemies.length + 1);
            enemies[enemies.length - 1] = obj;
        }
    }

    /**
     * 子弹入场
     */
    private int shootIndex = 0;

    public void shootAction() {
        shootIndex++;
        if (shootIndex % 30 == 0) { // 300ms射一次子弹
            Bullet[] bss = hero.shoot();
            bullets = Arrays.copyOf(bullets, bullets.length + bss.length);
            System.arraycopy(bss, 0, bullets, bullets.length - bss.length, bss.length);
        }
    }
}
