package experiment;

import strategy.StrategyManager;
import config.Configuration;
import constant.Constant;

public class Experiment {

	/**
	 * 学習戦略、見積もり戦略によってパラメータを設定する
	 * @param isLearning 学習あり、学習なし、ランダム のうちのどれか
	 * @param isEstimating 見積もりあり。見積もりなし のうちのどれか
	 */
	public static void set() {
		setStrategy(Configuration.LEARNING, Configuration.AGENT_TYPE);
		setEstimation(Configuration.ESTIMATION);
		setLearnRate(Configuration.LEARNING);
	}
	
	private static void setStrategy(String isLearning, String agentType) {
		if(isLearning.equals("random")) {
			StrategyManager.setRandomStrategy();
		}
		else if (agentType.equals("Rational")) {
			if(isLearning.equals("learning") || isLearning.equals("noLearning")) {
				// StrategyManager.setLearningAndNoLearningStrategy();
				// StrategyManager.setLearningAndNoLearningStrategyForMemberSelection();
				StrategyManager.setLearningAndNoLearningStrategy();
			}
			else {
				System.err.println("そのような学習戦略は存在しません");
				System.exit(-1);
			}
		}
		else if (agentType.equals("Structured")) {
			if(isLearning.equals("learning") || isLearning.equals("noLearning")) {
				// StrategyManager.setLearningAndNoLearningStrategy();
				// StrategyManager.setLearningAndNoLearningStrategyForMemberSelection();
				StrategyManager.setReciprocalStrategy();
			}
			else {
				System.err.println("そのような学習戦略は存在しません");
				System.exit(-1);
			}
		}
		else {
			System.err.println("そのような学習戦略は存在しません");
			System.exit(-1);
		}
	}
	
	private static void setEstimation(String isEstimating) {
		if(isEstimating.equals("estimation")) {
			StrategyManager.setEstimationStrategy();
		}
		else if(isEstimating.equals("noEstimation")) {
			StrategyManager.setNoEstimationStrategy();
		}
		else {
			System.err.println("そのような見積もり戦略は存在しません");
			System.exit(-1);
		}
	}
	
	private static void setLearnRate(String isLearning) {
		if(isLearning.equals("learning")) {
			Constant.setLearnRateWhenLearning();
		}
		else if(isLearning.equals("noLearning") || isLearning.equals("random")) {
			Constant.setLearnRateWhenNoLearning();
		}
		else {
			System.err.println("そのような学習戦略は存在しません");
			System.exit(-1);
		}
	}
}
