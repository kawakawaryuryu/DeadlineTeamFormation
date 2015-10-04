package initialRoleSelect;

import java.util.List;

import agent.Agent;
import task.Task;
import message.OfferMessage;

/**
 * 初期状態の戦略
 */
public interface InitialRoleSelectStrategy {
	
	/**
	 * メッセージを抽出する
	 * @param messages
	 * @param agent
	 * @return
	 */
	public abstract List<OfferMessage> getCanExecuteMessages(List<OfferMessage> messages, Agent agent);
	
	/**
	 * 提案メッセージを選択する
	 * @param messages
	 * @param agent
	 * @return
	 */
	public abstract OfferMessage selectMessage(List<OfferMessage> messages, Agent agent);
	
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
	public abstract double calculateExpectedMemberReward(Agent agent, OfferMessage message);
	
	/**
	 * メンバ候補を探す
	 * （メンバ候補がいなかったらfalseを返す）
	 * @param agent
	 * @param task
	 * @return
	 */
	public abstract boolean searchMemberCandidates(Agent agent, Task task);
	
	/**
	 * リーダが処理するサブタスクをリストから引き抜き、リーダ以外が処理するサブタスクリストはmemberSubTaskListに格納
	 * @param agent
	 * @param task
	 */
	public abstract void pullLeaderExecuteSubTask(Agent agent, Task task);
}
