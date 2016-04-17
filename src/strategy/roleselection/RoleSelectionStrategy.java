package strategy.roleselection;

import java.util.ArrayList;

import task.Task;
import message.OfferMessage;
import agent.Agent;

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
	public abstract OfferMessage selectMessage(ArrayList<OfferMessage> messages, Agent agent);
	
	/**
	 * リーダの期待報酬を計算する
	 * @param agent
	 * @param task
	 * @return
	 */
	public abstract double calculateExpectedLeaderReward(Agent agent, Task task);
	
	/**
	 * メンバの期待報酬を計算する
	 * @param agent
	 * @param message
	 * @return
	 */
	public abstract double calculateExpectedMemberReward(Agent agent, ArrayList<OfferMessage> messages);
}
