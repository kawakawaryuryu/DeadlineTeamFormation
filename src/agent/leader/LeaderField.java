package agent.leader;

import java.util.ArrayList;
import java.util.HashMap;

import task.Subtask;
import agent.Agent;

public class LeaderField {

	public ArrayList<Agent> answerAgents = new ArrayList<Agent>();	//返答したエージェントのリスト
	public ArrayList<Agent> trueAgents = new ArrayList<Agent>();	//チーム参加にOKしてくれたエージェントのリスト
	public ArrayList<Agent> falseAgents = new ArrayList<Agent>();	//チーム参加にNGをしたエージェントのリスト
	public ArrayList<Agent> leaderAndTrueAgents = new ArrayList<Agent>();	//チーム参加にOKしてくれたエージェント＋リーダのリスト
	public ArrayList<Agent> members = new ArrayList<Agent>();	//メンバを保持するエージェントリスト
	public ArrayList<Agent> notAssignToMemberList = new ArrayList<Agent>();	//まだサブタスクを割り当てられていないメンバ候補のリスト
	public ArrayList<Subtask> notAssignedSubTask = new ArrayList<Subtask>();	//処理するメンバが決まっていないサブタスクリスト
	public HashMap<Agent, ArrayList<Subtask>> memberSubtaskMap = new HashMap<Agent, ArrayList<Subtask>>();
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
		memberSubtaskMap.clear();
	}
	
	public void setAgentToMemberSubtaskMap(Agent member) {
		if(!memberSubtaskMap.containsKey(member)){
			memberSubtaskMap.put(member, new ArrayList<Subtask>());
		}
		else{
			System.err.println("すでに " + member + " が格納されています");
			System.exit(-1);
		}
	}
	
	public void addSubtaskToMemberSubtaskMap(Agent member, Subtask subtask) {
		if(!memberSubtaskMap.get(member).contains(subtask)){
			memberSubtaskMap.get(member).add(subtask);
		}
		else{
			System.err.println("すでに " + member + " のキーに対応するリストに " + subtask + " が格納されています");
		}
	}
}
