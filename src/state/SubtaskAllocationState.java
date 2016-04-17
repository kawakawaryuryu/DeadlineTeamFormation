package state;

import java.util.ArrayList;

import role.Role;
import strategy.StrategyManager;
import strategy.subtaskallocation.SubtaskAllocationStrategy;
import task.Failure;
import task.Subtask;
import team.Team;
import main.TeamFormationMain;
import message.AnswerMessage;
import message.TeamFormationMessage;
import agent.Agent;

public class SubtaskAllocationState implements State {
	
	private static State state = new SubtaskAllocationState();
	private SubtaskAllocationStrategy strategy = StrategyManager.getAllocationStrategy();
	

	@Override
	public void agentAction(Agent leader) {
		// エージェントを参加OKかNGかで分類
		classifyMessageIntoTrueOrFalse(leader);
//		debugAnswerAgents(leader);
		
		// 仮メンバにサブタスクを割り当て、メンバを決定する
		strategy.decideMembers(leader);
		
		// メンバ分の報酬と処理するサブタスクリソースの合計値を算出
		double leftReward = calculateLeftReward(leader);
		int leftRequireSum = calculateLeftRequireSum(leader);
		
		// OKメッセージが返ってきたエージェントにチーム編成成否メッセージを送信
		sendTeamFormationMessage(leader, leftReward, leftRequireSum);
		
		// Q値のフィードバック
		feedbackGreedyAndTrust(leader);
		
		// チーム編成に成功した場合
		if(leader.getParameter().getLeaderField().isTeaming){
//			System.out.println("チーム編成に成功しました");
//			debugExecutedSubtask(leader);
			
			// チームでの処理時間 & 拘束時間を算出
			calculateExecutingTimeAndBindingTimeInTeam(leader.getParameter().getParticipatingTeam());
			
			// データを計測
			TeamFormationMain.getMeasure().countInSuccessCase(leader.getParameter().getMarkedTask().getTaskRequireSum(), 
					leader.getParameter().getParticipatingTeam());
			leader.getMeasure().countInLeaderSuccessCase(leader.getParameter().getElement(Role.LEADER), leader, 
					leader.getParameter().getParticipatingTeam());
			
			// チーム履歴にチームを追加
			leader.getParameter().getPastTeam().addTeam(leader.getParameter().getParticipatingTeam());
			
			// タスクをキューから取り出す
			TeamFormationMain.getParameter().removeTask(leader.getParameter().getMarkedTask());
			
			// 状態遷移
			leader.getParameter().changeState(TaskExecuteState.getState());
		}
		// チーム編成に失敗した場合
		else{
//			System.out.println("チーム編成に失敗しました");
			
			// チーム編成失敗回数をカウント
			TeamFormationMain.getMeasure().countFailureTeamFormationNum();
			
			// サブタスクが保持している情報をクリア
			leader.getParameter().getMarkedTask().clear();
			
			// タスクからマークを外す
			leader.getParameter().getMarkedTask().markingTask(false, Failure.TEAM_FORMATION_FAILURE);
			
			// 状態遷移
			leader.getParameter().changeState(TaskSelectionState.getState());
		}
		
		// チーム編成回数をカウント
		TeamFormationMain.getMeasure().countTryingTeamFormationNum();
	}
	
	private void debugExecutedSubtask(Agent leader) {
		System.out.println("処理するサブタスク");
		for(Subtask subtask : leader.getParameter().getExecutedSubtasks()){
			System.out.println(subtask);
		}
	}
	
	private void classifyMessageIntoTrueOrFalse(Agent leader) {
		// 返答メッセージによってエージェントを分類
		for(AnswerMessage answer : leader.getParameter().getAnswerMessages()){
			leader.getParameter().getLeaderField().answerAgents.add(answer.getFrom());
			if(answer.getIsOk()){
				leader.getParameter().getLeaderField().trueAgents.add(answer.getFrom());
			}
			else{
				leader.getParameter().getLeaderField().falseAgents.add(answer.getFrom());
			}
		}
		
		// 返答メッセージが返ってきていないエージェントをリストに追加
		for(Agent agent : leader.getParameter().getSendAgents()){
			if(!leader.getParameter().getLeaderField().answerAgents.contains(agent)){
				leader.getParameter().getLeaderField().falseAgents.add(agent);
			}
		}
	}
	
	private void debugAnswerAgents(Agent leader) {
		System.out.println("OKメッセージを送ったエージェント");
		for(Agent agent : leader.getParameter().getLeaderField().trueAgents){
			System.out.println(agent);
		}
		System.out.println("NGメッセージを送ったエージェント");
		for(Agent agent : leader.getParameter().getLeaderField().falseAgents){
			System.out.println(agent);
		}
	}
	
	private double calculateLeftReward(Agent leader) {
		return (double)leader.getParameter().getMarkedTask().getTaskRequireSum() * (1.0 - leader.getGreedy());
	}
	
	private int calculateLeftRequireSum(Agent leader) {
		int leftRequireSum = leader.getParameter().getMarkedTask().getTaskRequireSum();
		for(Subtask subtask : leader.getParameter().getExecutedSubtasks()){
			leftRequireSum -= subtask.getRequireSum();
		}
		return leftRequireSum;
	}
	
	private void sendTeamFormationMessage(Agent leader, double leftReward, int leftRequireSum) {
		for(Agent agent : leader.getParameter().getLeaderField().trueAgents){
			if(!leader.getParameter().getLeaderField().memberSubtaskMap.containsKey(agent) 
					|| !leader.getParameter().getLeaderField().isTeaming){
				TeamFormationMain.getPost().postTeamFormationMessage(agent, new TeamFormationMessage(leader, agent, false));
//				System.out.println(agent + " にチーム編成失敗メッセージを送信しました");
			}
			else if(leader.getParameter().getLeaderField().memberSubtaskMap.containsKey(agent) 
					&& leader.getParameter().getLeaderField().isTeaming){
				ArrayList<Subtask> subtasks = leader.getParameter().getLeaderField().memberSubtaskMap.get(agent);
				Team team = leader.getParameter().getParticipatingTeam();
				TeamFormationMain.getPost().postTeamFormationMessage(agent, 
						new TeamFormationMessage(leader, agent, true, subtasks, leftReward, leftRequireSum, team));
//				System.out.println(agent + " にチーム編成成功メッセージを送信しました");
			}
			else{
				System.err.println("sendTeamFormationMessage: このようなパターンはありません");
				System.exit(-1);
			}
		}
	}
	
	private void feedbackGreedyAndTrust(Agent leader) {
		// 欲張り度
		leader.feedbackGreedy(leader.getParameter().getLeaderField().isTeaming);
		
		// 信頼度
		for(Agent agent : leader.getParameter().getLeaderField().trueAgents){
			leader.feedbackTrust(agent, true);
		}
		for(Agent agent : leader.getParameter().getLeaderField().falseAgents){
			leader.feedbackTrust(agent, false);
		}
	}
	
	private void calculateExecutingTimeAndBindingTimeInTeam(Team team) {
		team.calculateExecutingTime();
		team.calculateBindingTime();
	}

	public static State getState() {
		return state;
	}
	
	public String getStrategy() {
		return strategy.toString();
	}
	
	public String toString() {
		return "サブタスク割り当て状態（リーダ）";
	}

}
