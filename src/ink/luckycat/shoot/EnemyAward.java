package ink.luckycat.shoot;

/**
 * 奖励接口
 */
public interface EnemyAward {
    public int FIRE = 0; // 火力
    public int LIFE = 1; // 命

    public int getAwardType();
}
