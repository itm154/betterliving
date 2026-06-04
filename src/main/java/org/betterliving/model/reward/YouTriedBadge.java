package org.betterliving.model.reward;

public class YouTriedBadge implements Rewardable {
	private final String name = "You Tried";
	private final String desc = "Completed a quiz with under 10% score";
	private final int maxScore;
	private final int totalScore;

	public YouTriedBadge() {
		this(100, 0);
	}

	public YouTriedBadge(int maxScore, int totalScore) {
		this.maxScore = maxScore;
		this.totalScore = totalScore;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public int getBonuses() {
		return 10;
	}

	@Override
	public boolean qualifies(int score) {
		if (maxScore <= 0) {
			return false;
		}
		return (double) score / maxScore < 0.1;
	}
}
