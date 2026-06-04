package org.betterliving.model.reward;

public interface Rewardable {
	String getName();

	String getDesc();

	int getBonuses();

	boolean qualifies(int score);
}