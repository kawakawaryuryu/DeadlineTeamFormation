package fixed.strategy.roleselection;

import java.util.ArrayList;

import fixed.agent.FixedAgent;
import fixed.message.FixedOfferMessage;
import fixed.task.FixedTask;

/**
 * 初期状態の戦略
 */
public interface FixedRoleSelectionStrategy {
	
	/**
	 * 提案メッセージを選択する
	 * @param messages
	 * @param agent
	 * @return
	 */
	public abstract FixedOfferMessage selectMessage(ArrayList<FixedOfferMessage> messages, FixedAgent agent);
	
	/**
	 * リーダの期待報酬を計算する
	 * @param agent
	 * @param task
	 * @return
	 */
	public abstract double calculateExpectedLeaderReward(FixedAgent agent, FixedTask task);
	
	/**
	 * メンバの期待報酬を計算する
	 * @param agent
	 * @param message
	 * @return
	 */
	public abstract double calculateExpectedMemberReward(FixedAgent agent, ArrayList<FixedOfferMessage> messages);
}
