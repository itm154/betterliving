package org.betterliving.Rewards;

public interface Rewardable {
    boolean qualifies(int score);
    String getBadgeName();
    int getBonusPoints();
}