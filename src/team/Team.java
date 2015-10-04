package team;

import java.util.List;
import java.util.ArrayList;

import main.Constant;
import agent.Agent;

/**
 * チームの情報を管理
 */
public class Team {
	private int executeTime = 0;									//チームでのタスク処理時間
	private int executingTime = 0;
	private double executingTimePerAgentAverage = 0;				//1人あたりの平均処理時間
	private double executeTimeDifference = 0;						//拘束時間の差分
	private double executeTimeDifferencePerAgent = 0;				//1人あたりの拘束時間の差分
	private int[] teamResource = new int[Constant.RESOURCE_NUM];	//チーム合計の各リソース値
	private int teamResourceSum = 0;								//チーム合計のリソース合計値
	private int leaderResource = 0;									//リーダリソース
	private int memberResource = 0;									//メンバリソース
	private List<Agent> temporaryTeamMate = new ArrayList<Agent>();	//仮チームに属するエージェント
	private List<Agent> teamMate = new ArrayList<Agent>();			//チームに属するエージェント
	private Agent leader;											//チームに属するリーダ
	private List<Agent> members = new ArrayList<Agent>();			//チームに属するメンバ
	
	/**
	 * コンストラクタ
	 * @param leader
	 */
	public Team(Agent leader){
		this.leader = leader;
		leaderResource = leader.getAbilitySum();

		//仮チームにエージェントを加える
		addTemporaryTeamMate(leader);
		for(Agent agent : leader.getSendAgents()){
			addTemporaryTeamMate(agent);
		}
		
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			teamResource[i] = 0;
		}
	}
	
	/**
	 * チーム内のエージェントの数を返す
	 * @return
	 */
	public int getSize(){
		return teamMate.size();
	}
	
	/**
	 * 実行状態時のチームでのタスク実行（拘束）時間を返す
	 * @return
	 */
	public int getExecuteTime(){
		return executeTime;
	}
	
	/**
	 * 1人あたりの平均処理時間を返す
	 */
	public void calculateExecutingTimePerAgentAverage(){
		int executingTimeSum = 0;
		for(Agent agent : teamMate){
			executingTimeSum += agent.getExecuteTime();
		}
		executingTimePerAgentAverage = (double)executingTimeSum / (double)teamMate.size();
		executingTime = executingTimeSum;
	}
	
	public int getExecutingTime() {
		return executingTime;
	}
	
	/**
	 * 1人あたりの平均処理時間を返す
	 * @return
	 */
	public double getExecutingTimePerAgentAverage(){
		return executingTimePerAgentAverage;
	}
	
	/**
	 * チームの不要な拘束時間を求める
	 */
	public void calculateExecuteTimeDifference(){
		int executeTimeDifferenceSum = 0;
		for(Agent agent : teamMate){
			executeTimeDifferenceSum += (executeTime - agent.getExecuteTime());
		}
		executeTimeDifference = (double)executeTimeDifferenceSum;
		executeTimeDifferencePerAgent = (double)executeTimeDifferenceSum / (double)teamMate.size();
	}
	
	/**
	 * チームの不要な拘束時間を返す
	 * @return
	 */
	public double getExecuteTimeDifference(){
		return executeTimeDifference;
	}
	
	/**
	 * 1人あたりのチームの不要な拘束時間を返す
	 * @return
	 */
	public double getExecuteTimeDifferencePerAgent(){
		return executeTimeDifferencePerAgent;
	}
	
	/**
	 * 仮チームのチームメイトを加える
	 * @param agent
	 */
	public void addTemporaryTeamMate(Agent agent){
		temporaryTeamMate.add(agent);
	}
	
	/**
	 * チームメイトを加える
	 * @param agent
	 */
	public void addTeamMate(Agent agent){
		teamMate.add(agent);
		teamResourceSum += agent.getAbilitySum();
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			teamResource[i] += agent.getAbility()[i];
		}
		
		if(executeTime < agent.getExecuteTime()){
			executeTime = agent.getExecuteTime();
		}
	}
	
	/**
	 * メンバを加える
	 * @param agent
	 */
	public void addMembers(Agent agent){
		members.add(agent);
		addTeamMate(agent);
		memberResource += agent.getAbilitySum();
	}
	
	/**
	 * 仮チームメイトを返す
	 * @return
	 */
	public List<Agent> getTemporaryTeamMate(){
		return temporaryTeamMate;
	}
	
	/**
	 * チームメイトを返す
	 * @return
	 */
	public List<Agent> getTeamMate(){
		return teamMate;
	}
	
	/**
	 * リーダを返す
	 * @return
	 */
	public Agent getLeader(){
		return leader;
	}
	
	/**
	 * メンバを返す
	 * @return
	 */
	public List<Agent> getMembers(){
		return members;
	}
	
	/**
	 * チーム合計の各リソースを返す
	 * @return
	 */
	public int[] getTeamResource(){
		return teamResource;
	}
	
	/**
	 * チーム合計のリソース合計値を返す
	 * @return
	 */
	public int getTeamResourceSum(){
		return teamResourceSum;
	}
	
	/**
	 * リーダリソースを返す
	 * @return
	 */
	public int getLeaderResource(){
		return leaderResource;
	}
	
	/**
	 * メンバリソースを返す
	 * @return
	 */
	public int getMemberResource(){
		return memberResource;
	}
	
	/**
	 * チーム人数とタスク実行（拘束）時間とチーム内リソースを返す
	 */
	public String toString(){
		return "チーム人数 = " + teamMate.size() + " / タスク実行（拘束）時間 = " + executeTime + " / チーム内リソース = " + teamResourceSum;
	}
}
