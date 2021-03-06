package strategy.memberselection;

import java.util.ArrayList;

import random.RandomKey;
import random.RandomManager;
import task.Subtask;
import task.Task;
import library.AgentTaskLibrary;
import library.DeadlineLibrary;
import library.MessageLibrary;
import log.Log;
import main.teamformation.TeamFormationInstances;
import message.OfferMessage;
import constant.Constant;
import agent.Agent;

/**
 * 仮メンバの選び方を信頼度の高いエージェントごとにサブタスクを割り当てていく
 * 
 * サブタスクごとに仮メンバを2体選ぶ場合
 * サブタスク ◯ △ ⬜︎
 * 1巡目: ◯:①　△:②　⬜︎:③
 * 2巡目: ◯:④　△:⑤　⬜︎:⑥
 */
public class ConcreteTentativeMemberSelection2 implements
TentativeMemberSelectionStrategy {

	@Override
	public boolean searchTentativeMembers(Agent leader, Task task) {
		ArrayList<Agent> selectedAgents = new ArrayList<Agent>();

		// エージェントを信頼度にソート
		Agent[] sortedAgents = AgentTaskLibrary.getSortedAgentsFromArray(leader.getTrustToMember(), 
				TeamFormationInstances.getInstance().getParameter().getAgents());
		debugSortedAgents(sortedAgents, leader);

		// タスク中の各サブタスクをリソースの降順にソート
		task.sortSubTaskListByRequire();

		// リーダが処理するサブタスクをリストから取り出す
		pullExecutedSubtaskByLeader(leader, task);

		// リーダを格納
		selectedAgents.add(leader);

		// サブタスクごとにメンバ候補を選択する
		return selectTentativeMemberEverySubtask(leader, task, selectedAgents, sortedAgents);
	}

	private void debugSortedAgents(Agent[] agents, Agent leader) {
		Log.log.debugln("信頼度順で並んだエージェント");
		for(Agent agent : agents){
			Log.log.debugln(agent + " / 信頼度 = " + leader.getTrustToMember(agent));
		}
	}

	@Override
	public void pullExecutedSubtaskByLeader(Agent leader, Task task) {
		int leftDeadline = task.getDeadlineInTask() - DeadlineLibrary.getReducedDeadlineAtFirstTurn(Constant.MESSAGE_DELAY);
		boolean isAssigned = false;

		for(Subtask subtask : task.getSubTaskList()){
			if(AgentTaskLibrary.isExecuteSubTask(leader, subtask, leftDeadline) && !isAssigned){
				int executeTime = AgentTaskLibrary.calculateExecuteTime(leader, subtask);
				leftDeadline -= executeTime;
				leader.getParameter().setExecutedSubtasks(subtask, executeTime);
				isAssigned = true;
				Log.log.debugln("リーダの処理するサブタスク " + subtask);
			}
			else{
				task.subtasksByMembers.add(subtask);
			}
		}
	}

	@Override
	public boolean selectTentativeMemberEverySubtask(Agent leader, Task task, ArrayList<Agent> selectedAgents, Agent[] sortedAgents) {
		for(int selected = 0; selected < Constant.SELECT_MEMBER_NUM; selected++){

			int i = 0;
			while(i < task.subtasksByMembers.size()){
				Subtask subtask = task.subtasksByMembers.get(i);
				Log.log.debugln(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");

				double probability = RandomManager.getRandom(RandomKey.EPSILON_GREEDY_RANDOM_2).nextDouble();	//ε-greedyの確率
				Log.log.debugln("probability = " + probability);

				Agent selectedMember;	//メンバ候補
				ArrayList<Agent> canExecuteSubTaskAgents = AgentTaskLibrary.getAgentsCanExecuteSubtask(subtask, sortedAgents);	//subtaskを処理できるエージェントのリスト

				while(true){
					// デッドラインまでに処理できるエージェントがいなければ
					if(canExecuteSubTaskAgents.isEmpty()){
						Log.log.debugln(subtask + " を処理できるメンバがいないので、メンバ候補探しに失敗しました");
						return false;
					}

					// εの確率でランダムにエージェントを選ぶ
					if(probability <= Constant.EPSILON2){
						selectedMember = canExecuteSubTaskAgents.remove(RandomManager.getRandom(RandomKey.SELECT_RANDOM_2).nextInt(canExecuteSubTaskAgents.size()));
						Log.log.debugln(selectedMember + " をランダムで選びました");
					}
					// 1-εの確率で提案受託期待度の高いエージェントを選ぶ
					else{
						selectedMember = canExecuteSubTaskAgents.remove(0);
						Log.log.debugln(selectedMember + " を信頼度順で選びました");
					}

					// すでに選ばれているエージェントの場合は、最初から
					if(selectedAgents.contains(selectedMember)){
						Log.log.debugln(selectedMember + " はすでに選ばれているのでやり直し");
						continue;
					}
					else{
						Log.log.debugln(selectedMember + " を" + subtask + " のサブタスク処理のメンバ候補として選びました");
						selectedAgents.add(selectedMember);
						subtask.getAgentInfo().addSelectedAgent(selectedMember);
						i++;
						break;
					}
				}
			}
		}
		// メンバ候補に提案メッセージを送る
		sendOfferMessageToTentativeMembers(task, leader);

		return true;
	}

	@Override
	public void sendOfferMessageToTentativeMembers(Task task,
			Agent leader) {
		for(Subtask subtask : task.subtasksByMembers){
			for(Agent agent : subtask.getAgentInfo().getSelectedAgents()){
				int delayTime = MessageLibrary.getMessageTime(leader, agent);
				TeamFormationInstances.getInstance().getPost().postOfferMessage(agent,
						new OfferMessage(leader, agent, delayTime, subtask));
				leader.getParameter().addSendAgents(agent);
			}
		}	
	}

	public String toString() {
		return "各サブタスクごとに仮メンバを信頼度の高い順に1体ずつ選び、それを" + Constant.SELECT_MEMBER_NUM + "回行う";
	}
}
