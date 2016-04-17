package task;

import constant.Constant;

public class Subtask {
	private int[] require = new int[Constant.RESOURCE_NUM];	//タスクリソース配列
	private int[] leftRequire = new int[Constant.RESOURCE_NUM];	//残りリソースの配列
	private int requireSum = 0;	//リソースの合計
	private int deadline;	//タスクのデッドライン
	
	private AgentInfo agentInfo = new AgentInfo();
	
	/**
	 * コンストラクタ
	 * @param require
	 * @param deadline
	 */
	public Subtask(int[] resource, int deadline){
		for(int i = 0; i < resource.length; i++){
			this.require[i] = resource[i];
			this.leftRequire[i] = resource[i];
			requireSum += this.require[i];
		}
		this.deadline = deadline;
	}
	
	public int getRequire(int i) {
		return require[i];
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
	
	public AgentInfo getAgentInfo() {
		return agentInfo;
	}
	
	/**
	 * リソースを表す
	 */
	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append("require = ");
		for(int i = 0; i < require.length; i++){
			str.append(require[i] + " ");	
		}
		return str.toString();
	}
	
}
