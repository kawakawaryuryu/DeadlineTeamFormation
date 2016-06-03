package strategy.roleselection;

import java.util.ArrayList;

import task.Task;
import message.OfferMessage;
import agent.ConcreteAgent;

/**
 * 初期状態の戦略
 */
public interface RoleSelectionStrategy {
	
	/**
	 * 提案メッセージを選択する
	 * @param messages
	 * @param agent
	 * @return
	 */
	public abstract OfferMessage selectMessage(ArrayList<OfferMessage> messages, ConcreteAgent agent);
	
	/**
	 * リーダの期待報酬を計算する
	 * @param agent
	 * @param task
	 * @return
	 */
	public abstract double calculateExpectedLeaderReward(ConcreteAgent agent, Task task);
	
	/**
	 * メンバの期待報酬を計算する
	 * @param agent
	 * @param message
	 * @return
	 */
	public abstract double calculateExpectedMemberReward(ConcreteAgent agent, ArrayList<OfferMessage> messages);
}
