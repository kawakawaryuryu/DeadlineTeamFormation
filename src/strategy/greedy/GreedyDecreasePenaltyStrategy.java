package strategy.greedy;

import agent.Agent;

public class GreedyDecreasePenaltyStrategy implements GreedyPenaltyStrategy {

	@Override
	public void decreaseGreedy(Agent agent) {
		// 欲張り度を下げる
		agent.feedbackGreedy(false);
	}

	public String toString() {
		return "見積もり失敗時の欲張り度ペナルティあり戦略";
	}
}
