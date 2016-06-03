package agent.leader;

import java.util.ArrayList;
import java.util.HashMap;

import task.Subtask;
import agent.ConcreteAgent;

public class LeaderField {

	public ArrayList<ConcreteAgent> answerAgents = new ArrayList<ConcreteAgent>();	//返答したエージェントのリスト
	public ArrayList<ConcreteAgent> trueAgents = new ArrayList<ConcreteAgent>();	//チーム参加にOKしてくれたエージェントのリスト
	public ArrayList<ConcreteAgent> falseAgents = new ArrayList<ConcreteAgent>();	//チーム参加にNGをしたエージェントのリスト
	public ArrayList<ConcreteAgent> leaderAndTrueAgents = new ArrayList<ConcreteAgent>();	//チーム参加にOKしてくれたエージェント＋リーダのリスト
	public ArrayList<ConcreteAgent> members = new ArrayList<ConcreteAgent>();	//メンバを保持するエージェントリスト
	public ArrayList<ConcreteAgent> notAssignToMemberList = new ArrayList<ConcreteAgent>();	//まだサブタスクを割り当てられていないメンバ候補のリスト
	public ArrayList<Subtask> notAssignedSubTask = new ArrayList<Subtask>();	//処理するメンバが決まっていないサブタスクリスト
	public HashMap<ConcreteAgent, ArrayList<Subtask>> memberSubtaskMap = new HashMap<ConcreteAgent, ArrayList<Subtask>>();
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
	
	public void setAgentToMemberSubtaskMap(ConcreteAgent member) {
		if(!memberSubtaskMap.containsKey(member)){
			memberSubtaskMap.put(member, new ArrayList<Subtask>());
		}
		else{
			System.err.println("すでに " + member + " が格納されています");
			System.exit(-1);
		}
	}
	
	public void addSubtaskToMemberSubtaskMap(ConcreteAgent member, Subtask subtask) {
		if(!memberSubtaskMap.get(member).contains(subtask)){
			memberSubtaskMap.get(member).add(subtask);
		}
		else{
			System.err.println("すでに " + member + " のキーに対応するリストに " + subtask + " が格納されています");
		}
	}
}
