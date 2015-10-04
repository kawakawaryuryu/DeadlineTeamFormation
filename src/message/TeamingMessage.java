package message;

import java.util.ArrayList;
import java.util.List;

import task.SubTask;
import agent.Agent;
import team.Team;

public class TeamingMessage {
	private Agent from;	//誰からのメッセージか
	private boolean isok;	//チーム編成成功かどうか
	private List<SubTask> subtaskList = new ArrayList<SubTask>();	//成功した場合はサブタスクも一緒に添付
	private double leftReward;	//全報酬からリーダが獲得した報酬を除いた報酬
	private int leftRequireSum;	//リーダ以外が処理するサブタスクのリソースの合計
	private Team team_info;	//チーム情報
	private int subtaskRequireSum = 0;	//添付されたサブタスクのリソースの合計
	
	/**
	 * コンストラクタ
	 * @param from
	 * @param isok
	 * @param subtaskList
	 * @param leftReward
	 * @param leftRequireSum
	 * @param team_info
	 */
	public TeamingMessage(Agent from, boolean isok, List<SubTask> subtaskList, double leftReward, int leftRequireSum, Team team_info){
		this.from = from;
		this.isok = isok;
		this.subtaskList = subtaskList;
		this.leftReward = leftReward;
		this.leftRequireSum = leftRequireSum;
		this.team_info = team_info;
		
		if(subtaskList.isEmpty() == true){
			subtaskRequireSum = 1;
		}
		/* subtaskRequireSumを計算 */
		for(SubTask subtask : this.subtaskList){
			subtaskRequireSum += subtask.getRequireSum();
		}
	}
	
	/**
	 * 誰からのメッセージか返す
	 * @return
	 */
	public Agent getFrom(){
		return from;
	}
	
	/**
	 * チーム編成に成功かどうか返す
	 * @return
	 */
	public boolean getIsOk(){
		return isok;
	}
	
	/**
	 * チーム編成に成功した場合に添付されたサブタスクを返す
	 * @return
	 */
	public List<SubTask> getSubTaskList(){
		return subtaskList;
	}
	
	/**
	 * メンバが獲得できる残りの報酬を返す
	 * @return
	 */
	public double getLeftReward(){
		return leftReward;
	}
	
	/**
	 * リーダ以外が処理するサブタスクのリソースの合計を返す
	 * @return
	 */
	public int getLeftRequireSum(){
		return leftRequireSum;
	}
	
	public Team getTeamInfo(){
		return team_info;
	}
	
	/**
	 * 添付されたサブタスクのリソースの合計を返す
	 * @return
	 */
	public int getSubTaskRequireSum(){
		return subtaskRequireSum;
	}

}
