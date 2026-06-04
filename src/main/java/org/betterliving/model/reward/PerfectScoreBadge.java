package org.betterliving.model.reward;

public class PerfectScoreBadge implements Rewardable {
	private final String name = "Perfect Score Badge";
	private final String desc = "Achieved a perfect score for a quiz";
	private final int maxScore;

	public PerfectScoreBadge(int maxScore) {
		this.maxScore = maxScore;
	}

	@Override
	public boolean qualifies(int score) {
		return score == maxScore;
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
		return 100;
	}
}
