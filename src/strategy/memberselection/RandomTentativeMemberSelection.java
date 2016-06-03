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
import agent.ConcreteAgent;

public class RandomTentativeMemberSelection implements
		TentativeMemberSelectionStrategy {

	@Override
	public boolean searchTentativeMembers(ConcreteAgent leader, Task task) {
		ArrayList<ConcreteAgent> selectedAgents = new ArrayList<ConcreteAgent>();
		
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
	public void pullExecutedSubtaskByLeader(ConcreteAgent leader, Task task) {
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
	public boolean selectTentativeMemberEverySubtask(ConcreteAgent leader,
			Task task, ArrayList<ConcreteAgent> selectedAgents,
			ConcreteAgent[] sortedAgents) {
		if(sortedAgents != null){
			System.err.println("sotedAgentsはnullではありません");
			System.exit(-1);
		}
		
		for(Subtask subtask : task.subtasksByMembers){
			int selected = 0;
			ConcreteAgent selectedMember;	//メンバ候補
			ArrayList<ConcreteAgent> canExecuteSubTaskAgents = new ArrayList<ConcreteAgent>(TeamFormationInstances.getInstance().getParameter().getAgents());
			
			while(selected < Constant.SELECT_MEMBER_NUM){
				Log.log.debugln(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");
				
				// デッドラインまでに処理できるエージェントがいなければ
				if(canExecuteSubTaskAgents.isEmpty()){
					System.err.println("RandomTentativeMemberSelectionでこのようなパターンはありえません");
					System.exit(-1);
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
			ConcreteAgent leader) {
		for(Subtask subtask : task.subtasksByMembers){
			for(ConcreteAgent agent : subtask.getAgentInfo().getSelectedAgents()){
				TeamFormationInstances.getInstance().getPost().postOfferMessage(agent, new OfferMessage(leader, agent, subtask));
				leader.getParameter().addSendAgents(agent);
			}
		}
	}
	
	public String toString() {
		return "ランダムに仮メンバを選ぶ";
	}

}
