package org.betterliving.model.Rewards;

import java.lang.annotation.Retention;

public class PerfectScoreBAdge implements Rewardable{
    private final String badgeName = "Perfect Score Badge";
    private final int requiredScore;

    public PerfectScoreBAdge(int requiredScore) {
        this.requiredScore = requiredScore;
    }

    @Override
    public boolean qualifies(int score) {
        return score >= requiredScore;
    }

    @Override
    public String getBadgeName() {
        return badgeName;
    }

    @Override
    public int getBonusPoints() {
        Return 100; // Bonus points for achieving a perfect score
    }
}
