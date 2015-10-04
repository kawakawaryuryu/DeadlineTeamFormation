package leader;

import java.util.ArrayList;
import java.util.List;

import message.AnswerMessage;
import task.SubTask;
import agent.Agent;

public class LeaderField {
	
	public List<AnswerMessage> messages = new ArrayList<AnswerMessage>();	//返答メッセージを保持
	public List<Agent> trueAgentList = new ArrayList<Agent>();	//チーム参加にOKしてくれたエージェントのリスト
	public List<Agent> falseAgentList = new ArrayList<Agent>();	//チーム参加にNGをしたエージェントのリスト
	public List<Agent> trueAgentAndLeaderList = new ArrayList<Agent>();	//チーム参加にOKしてくれたエージェント＋リーダのリスト
	public List<Agent> memberList = new ArrayList<Agent>();	//メンバを保持するエージェントリスト
	public List<Agent> notAssignToMemberList = new ArrayList<Agent>();	//まだサブタスクを割り当てられていないメンバ候補のリスト
	public List<SubTask> notAssignedSubTask = new ArrayList<SubTask>();	//処理するメンバが決まっていないサブタスクリスト
	public boolean isTeaming;	//チーム編成成功か失敗か
	public boolean isTeamingAgainAllocation;	//再割り当てを行ってチーム編成が成功したか失敗したか
	
	/**
	 * 変数、リストの初期化
	 */
	public void initialize(){
		trueAgentList.clear();
		falseAgentList.clear();
		trueAgentAndLeaderList.clear();
		memberList.clear();
		notAssignToMemberList.clear();
		notAssignedSubTask.clear();
		isTeaming = true;
		isTeamingAgainAllocation = true;
	}
}
