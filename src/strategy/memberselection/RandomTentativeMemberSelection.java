package strategy.memberselection;

import java.util.ArrayList;

import random.RandomKey;
import random.RandomManager;
import task.Subtask;
import task.Task;
import library.AgentTaskLibrary;
import log.Log;
import main.teamformation.TeamFormationInstances;
import message.OfferMessage;
import constant.Constant;
import exception.AbnormalException;
import agent.Agent;

public class RandomTentativeMemberSelection implements
		TentativeMemberSelectionStrategy {

	@Override
	public boolean searchTentativeMembers(Agent leader, Task task) {
		ArrayList<Agent> selectedAgents = new ArrayList<Agent>();
		
		// タスク中の各サブタスクをリソースの降順にソート
		task.sortSubTaskListByRequire();

		// リーダが処理するサブタスクをリストから取り出す
		pullExecutedSubtaskByLeader(leader, task);

		// リーダを格納
		selectedAgents.add(leader);

		// サブタスクごとにメンバ候補を選択する
		return selectTentativeMemberEverySubtask(leader, task, selectedAgents, null);
	}

	@Override
	public void pullExecutedSubtaskByLeader(Agent leader, Task task) {
		int leftDeadline = task.getDeadlineInTask() - Constant.DEADLINE_MIN_2;
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
	public boolean selectTentativeMemberEverySubtask(Agent leader,
			Task task, ArrayList<Agent> selectedAgents,
			Agent[] sortedAgents) {
		if(sortedAgents != null){
			throw new AbnormalException("sotedAgentsはnullではありません");
		}
		
		for(Subtask subtask : task.subtasksByMembers){
			int selected = 0;
			Agent selectedMember;	//メンバ候補
			ArrayList<Agent> canExecuteSubTaskAgents = new ArrayList<Agent>(TeamFormationInstances.getInstance().getParameter().getAgents());
			
			while(selected < Constant.SELECT_MEMBER_NUM){
				Log.log.debugln(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");
				
				// デッドラインまでに処理できるエージェントがいなければ
				if(canExecuteSubTaskAgents.isEmpty()){
					throw new AbnormalException("RandomTentativeMemberSelectionでこのようなパターンはありえません");
				}

				// εの確率でランダムにエージェントを選ぶ
				selectedMember = canExecuteSubTaskAgents.remove(RandomManager.getRandom(RandomKey.SELECT_RANDOM_2).nextInt(canExecuteSubTaskAgents.size()));
				Log.log.debugln(selectedMember + " をランダムで選びました");

				// すでに選ばれているエージェントの場合は、最初から
				if(selectedAgents.contains(selectedMember)){
					Log.log.debugln(selectedMember + " はすでに選ばれているのでやり直し");
					continue;
				}
				else{
					Log.log.debugln(selectedMember + " を" + subtask + " のサブタスク処理のメンバ候補として選びました");
					selectedAgents.add(selectedMember);
					subtask.getAgentInfo().addSelectedAgent(selectedMember);
					selected++;
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
				TeamFormationInstances.getInstance().getPost().postOfferMessage(agent, new OfferMessage(leader, agent, subtask));
				leader.getParameter().addSendAgents(agent);
			}
		}
	}
	
	public String toString() {
		return "ランダムに仮メンバを選ぶ";
	}

}
