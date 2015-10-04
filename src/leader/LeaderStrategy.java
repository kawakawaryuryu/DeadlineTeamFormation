package leader;

import agent.Agent;

/**
 * リーダ提案送信状態の戦略
 */
public interface LeaderStrategy {
	
	/**
	 * 仮チームのメンバにサブタスクを振り割る
	 * @param leader
	 */
	public abstract void decideMember(Agent leader);
	
	/**
	 * リーダ以外が処理する残りのサブタスクリソース量を求める
	 * @param leader
	 * @return
	 */
	public abstract int calculateLeftRequireSum(Agent leader);
}
