package task;

import java.util.ArrayList;
import java.util.List;

import main.Constant;
import agent.Agent;

public class SubTask {
	private int[] require = new int[Constant.RESOURCE_NUM];	//タスクリソース配列
	private int[] leftRequire = new int[Constant.RESOURCE_NUM];	//残りリソースの配列
	private int requireSum = 0;	//リソースの合計
	private int deadline;	//タスクのデッドライン
	public List<Agent> candidates = new ArrayList<Agent>();	//メンバ候補
	private Agent executingAgent;	//このサブタスクを処理するエージェント
	
	private AgentInfo agentInfo = new AgentInfo();
	
	/**
	 * コンストラクタ
	 * @param require
	 * @param deadline
	 */
	public SubTask(int[] resource, int deadline){
		for(int i = 0; i < resource.length; i++){
			this.require[i] = resource[i];
			this.leftRequire[i] = resource[i];
			requireSum += this.require[i];
		}
		this.deadline = deadline;
	}
	
	/**
	 * リソースを返す
	 * @return
	 */
	public int[] getRequire(){
		return require;
	}
	
	/**
	 * サブタスクのリソースの合計を返す
	 * @return
	 */
	public int getRequireSum(){
		return requireSum;
	}
	
	/**
	 * サブタスクのリソースを減らす
	 * @param require
	 */
	public void subtractRequire(int[] require){
		for(int i = 0; i < require.length; i++){
			leftRequire[i] -= require[i];
		}
	}
	
	/**
	 * サブタスクの残りリソースを返す
	 * @return
	 */
	public int[] getLeftRequire(){
		return leftRequire;
	}
	
	/**
	 * デッドラインを返す
	 * @return
	 */
	public int getDeadline(){
		return deadline;
	}
	
	/**
	 * デッドラインを1減らす
	 */
	public void subtractDeadlinePerTurn(){
		deadline -= 1;
	}
	
	/**
	 * メンバ候補を加える
	 * @param candidate
	 */
	public void addMemberCandidate(Agent candidate){
		candidates.add(candidate);
	}
	
	/**
	 * メンバ候補を削除する
	 * @param candidate
	 */
	public void removeMemberCandidate(Agent candidate){
		candidates.remove(candidate);
	}
	
	/**
	 * メンバ候補リストを空にする
	 */
	public void clewarMemberCandidate(){
		candidates.clear();
	}
	
	/**
	 * サブタスクのメンバ候補を返す
	 * @return
	 */
	public List<Agent> getMemberCandidate(){
		return candidates;
	}
	
	/**
	 * このサブタスクを処理するエージェントを代入
	 * @param agent
	 */
	public void addExecutingAgent(Agent agent){
		executingAgent = agent;
	}
	
	/**
	 * このサブタスクを処理するエージェントを返す
	 * @return
	 */
	public Agent getExecutingAgent(){
		return executingAgent;
	}
	
	public AgentInfo getAgentInfo() {
		return agentInfo;
	}
	
	/**
	 * リソースを表す
	 */
	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append("require = ");
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			str.append(require[i] + " ");	
		}
		return str.toString();
	}
	
}
