package strategy.greedy;

import agent.Agent;

public class GreedyUnchangePenaltyStrategy implements GreedyPenaltyStrategy {

	@Override
	public void decreaseGreedy(Agent agent) {
		// 何もしない
	}

	public String toString() {
		return "見積もり失敗時の欲張り度ペナルティなし戦略";
	}

}
