package fixed.expriment;

import fixed.constant.FixedConstant;
import fixed.strategy.StrategyManager;

public class Experiment {

	/**
	 * 学習戦略、見積もり戦略によってパラメータを設定する
	 * @param isLearning 学習あり、学習なし、ランダム のうちのどれか
	 * @param isEstimating 見積もりあり。見積もりなし のうちのどれか
	 */
	public static void set(String isLearning, String isEstimating) {
		setStrategy(isLearning);
		setEstimation(isEstimating);
		setLearnRate(isLearning);
	}
	
	private static void setStrategy(String isLearning) {
		if(isLearning.equals("learning") || isLearning.equals("noLearning")) {
			StrategyManager.setLearningAndNoLearningStrategy();
		}
		else if(isLearning.equals("random")) {
			StrategyManager.setRandomStrategy();
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
			FixedConstant.setLearnRateWhenLearning();
		}
		else if(isLearning.equals("noLearning") || isLearning.equals("random")) {
			FixedConstant.setLearnRateWhenNoLearning();
		}
		else {
			System.err.println("そのような学習戦略は存在しません");
			System.exit(-1);
		}
	}
}
