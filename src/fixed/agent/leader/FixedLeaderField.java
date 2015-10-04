package fixed.agent.leader;

import java.util.ArrayList;
import java.util.HashMap;

import fixed.agent.FixedAgent;
import fixed.task.FixedSubtask;

public class FixedLeaderField {

	public ArrayList<FixedAgent> answerAgents = new ArrayList<FixedAgent>();	//返答したエージェントのリスト
	public ArrayList<FixedAgent> trueAgents = new ArrayList<FixedAgent>();	//チーム参加にOKしてくれたエージェントのリスト
	public ArrayList<FixedAgent> falseAgents = new ArrayList<FixedAgent>();	//チーム参加にNGをしたエージェントのリスト
	public ArrayList<FixedAgent> leaderAndTrueAgents = new ArrayList<FixedAgent>();	//チーム参加にOKしてくれたエージェント＋リーダのリスト
	public ArrayList<FixedAgent> members = new ArrayList<FixedAgent>();	//メンバを保持するエージェントリスト
	public ArrayList<FixedAgent> notAssignToMemberList = new ArrayList<FixedAgent>();	//まだサブタスクを割り当てられていないメンバ候補のリスト
	public ArrayList<FixedSubtask> notAssignedSubTask = new ArrayList<FixedSubtask>();	//処理するメンバが決まっていないサブタスクリスト
	public HashMap<FixedAgent, ArrayList<FixedSubtask>> memberSubtaskMap = new HashMap<FixedAgent, ArrayList<FixedSubtask>>();
	public boolean isTeaming;	//チーム編成成功か失敗か
	public boolean isTeamingAgainAllocation;	//再割り当てを行ってチーム編成が成功したか失敗したか
	
	/**
	 * 変数、リストの初期化
	 */
	public void initialize(){
		answerAgents.clear();
		trueAgents.clear();
		falseAgents.clear();
		leaderAndTrueAgents.clear();
		members.clear();
		notAssignToMemberList.clear();
		notAssignedSubTask.clear();
		isTeaming = true;
		isTeamingAgainAllocation = true;
	}
	
	public void setAgentToMemberSubtaskMap(FixedAgent member) {
		memberSubtaskMap.put(member, new ArrayList<FixedSubtask>());
	}
	
	public void addSubtaskToMemberSubtaskMap(FixedAgent member, FixedSubtask subtask) {
		memberSubtaskMap.get(member).add(subtask);
	}
}
