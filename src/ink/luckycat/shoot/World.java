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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * 游戏窗口
 */
public class World extends JPanel {
    public static final int WIDTH = 400;
    public static final int Height = 700;

    public static final int START = 0;      // 启动状态
    public static final int RUNNING = 1;    // 运行状态
    public static final int PAUSE = 2;      // 暂停状态
    public static final int GAME_OVER = 3;   // 游戏结束状态
    public int state = START;   // 当前状态


    // 天空对象
    private Sky sky = new Sky();

    // 英雄机对象
    private Hero hero = new Hero();

    // 敌人数组（向上造型，所有敌军飞机都可以存入这个数组）
    private List<FlyingObject> enemies = new ArrayList<>();

    // 子弹数组
    private List<Bullet> bullets = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        World world = new World();
        frame.add(world);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, Height);
        // 不允许最大化
        frame.setResizable(false);
        // 窗口在中心位置出现
        frame.setLocationRelativeTo(null);
//        frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
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
        for (FlyingObject e : enemies) {
            g.drawImage(e.getImage(), e.x, e.y, null);
        }
        for (Bullet b : bullets) {
            g.drawImage(b.getImage(), b.x, b.y, null);
        }
        g.drawImage(hero.getImage(), hero.x, hero.y, null);

        g.setColor(Color.RED);
        g.drawString("SCORE: " + score, 10, 25);
        g.drawString(" L I F E : " + hero.getLife(), 10, 45);
        g.drawString(" F I R E : " + hero.getFire(), 10, 65);

        switch (state) {
            case START:
                g.drawImage(Images.start, 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(Images.pause, 0, 0, null);
                break;
            case GAME_OVER:
                g.drawImage(Images.gameover, 0, 0, null);
                break;
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
        // 线程控制其他飞行物的事件
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                    if(state == RUNNING){
                        enterAction(); // 敌人入场
                        shootAction(); // 子弹入场
                        stepAction(); // 飞行物移动
                        outOfBoundsAction(); // 删除越界敌人
                        bulletBangAction(); // 子弹碰撞敌人
                        heroBangAction();   // 英雄机碰撞敌人
                        checkGameOverAction();
                    }
                    repaint(); // 重新绘画窗口
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 鼠标监听器
        MouseAdapter m = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(state == RUNNING){
                    hero.moveTo(e.getX(), e.getY());
                }
            }

            /**
             * 鼠标点击事件
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (state){
                    case START:
                        state = RUNNING;
                        center();
                        break;
                    case GAME_OVER:
                        // GAME_OVER 之后对象重新实例化（可以做到初始化）
                        score = 0;
//                        sky = new Sky();
                        hero = new Hero();
                        enemies = new ArrayList<>();
                        bullets = new ArrayList<>();
                        state = START;
                        break;
                }

            }

            /**
             * 鼠标移出事件
             * @param e
             */
            @Override
            public void mouseExited(MouseEvent e) {
                if(state == RUNNING){
                    state = PAUSE;
                }
            }

            /**
             * 鼠标移入事件
             * @param e
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                if(state == PAUSE){
                    center();
                    state = RUNNING;
                }
            }
        };
        this.addMouseListener(m);
        this.addMouseMotionListener(m);

    }

    /**
     * 敌人入场
     */
    private int enterIndex = 0; // 敌人入场计数

    public void enterAction() {
        enterIndex++;
        if (enterIndex % 40 == 0) { // 400毫秒生成一个敌人
            FlyingObject obj = nextOne(); // 获取敌人对象
            enemies.add(obj);
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
            for (int i = 0; i < bss.length; i++) {
                bullets.add(bss[i]);
            }
        }
    }

    /**
     * 飞行物移动
     */
    public void stepAction() {
        // 天空移动
        sky.step();
        // 敌机移动
        for (FlyingObject e : enemies) {
            e.step();
        }
        // 子弹移动
        for (Bullet b : bullets) {
            b.step();
        }
    }

    /**
     * 飞行物销毁
     */
    public void outOfBoundsAction() {
        Iterator<FlyingObject> eIterator = enemies.iterator();
        while (eIterator.hasNext()) {
            FlyingObject e = eIterator.next();
            if (e.isOutBounds() || e.isRemove()) {
                eIterator.remove();
            }
        }

        Iterator<Bullet> bIterator = bullets.iterator();
        while (bIterator.hasNext()) {
            Bullet b = bIterator.next();
            if (b.isOutBounds() || b.isRemove()) {
                bIterator.remove();
            }
        }

    }

    // 玩家分数
    private int score = 0;

    /**
     * 子弹与敌人碰撞
     */
    public void bulletBangAction() {
        for (Bullet b : bullets) {
            for (FlyingObject e : enemies) {
                if (b.isLive() && e.isLive() && e.isHit(b)) {
                    b.goDead();
                    e.goDead();
                    if (e instanceof EnemyScore) {    // 得分敌机
                        score += ((EnemyScore) e).getScore();
                    }
                    if (e instanceof EnemyAward) {    // 奖励敌机
                        int awardType = ((EnemyAward) e).getAwardType();
                        switch (awardType) {
                            case EnemyAward.FIRE:
                                hero.addFire();
                                break;
                            case EnemyAward.LIFE:
                                hero.addLife();
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 英雄机碰撞敌人检测
     */
    public void heroBangAction() {
        for (FlyingObject e : enemies) {
            if (e.isLive() && hero.isLive() && e.isHit(hero)) {
                e.goDead();
                hero.subtractLife();
                hero.clearFire();
            }
        }
    }

    /**
     * 检测游戏结束
     */
    public void checkGameOverAction() {
        if (hero.getLife() <= 0) {
            state = GAME_OVER;
        }
    }

    public void center(){
        try {
            Robot r = new Robot();
            Point p = getLocationOnScreen();
            p.x += hero.x + hero.width / 2;
            p.y += hero.y + hero.height / 2;
            r.mouseMove(p.x, p.y);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }
}
