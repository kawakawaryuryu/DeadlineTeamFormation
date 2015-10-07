package fixed.strategy.memberselection;

import java.util.ArrayList;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.library.AgentTaskLibrary;
import fixed.main.RandomKey;
import fixed.main.RandomManager;
import fixed.main.TeamFormationMain;
import fixed.message.FixedOfferMessage;
import fixed.task.FixedSubtask;
import fixed.task.FixedTask;

public class RandomTentativeMemberSelection implements
		TentativeMemberSelectionStrategy {

	@Override
	public boolean searchTentativeMembers(FixedAgent leader, FixedTask task) {
		ArrayList<FixedAgent> selectedAgents = new ArrayList<FixedAgent>();
		
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
	public void pullExecutedSubtaskByLeader(FixedAgent leader, FixedTask task) {
		int leftDeadline = task.getDeadlineInTask() - FixedConstant.DEADLINE_MIN_2;
		boolean isAssigned = false;
		
		for(FixedSubtask subtask : task.getSubTaskList()){
			if(AgentTaskLibrary.isExecuteSubTask(leader, subtask, leftDeadline) && !isAssigned){
				int executeTime = AgentTaskLibrary.calculateExecuteTime(leader, subtask);
				leftDeadline -= executeTime;
				leader.getParameter().setExecutedSubtasks(subtask, executeTime);
				isAssigned = true;
//				System.out.println("リーダの処理するサブタスク " + subtask);
			}
			else{
				task.subtasksByMembers.add(subtask);
			}
		}
	}

	@Override
	public boolean selectTentativeMemberEverySubtask(FixedAgent leader,
			FixedTask task, ArrayList<FixedAgent> selectedAgents,
			FixedAgent[] sortedAgents) {
		if(sortedAgents != null){
			System.err.println("sotedAgentsはnullではありません");
			System.exit(-1);
		}
		
		for(FixedSubtask subtask : task.subtasksByMembers){
			int selected = 0;
			FixedAgent selectedMember;	//メンバ候補
			ArrayList<FixedAgent> canExecuteSubTaskAgents = new ArrayList<FixedAgent>(TeamFormationMain.getParameter().getAgent());
			
//			System.out.println("probability = " + probability);
			while(selected < FixedConstant.SELECT_MEMBER_NUM){
//				System.out.println(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");
				
				// デッドラインまでに処理できるエージェントがいなければ
				if(canExecuteSubTaskAgents.isEmpty()){
					System.err.println("RandomTentativeMemberSelectionでこのようなパターンはありえません");
					System.exit(-1);
				}

				// εの確率でランダムにエージェントを選ぶ
				selectedMember = canExecuteSubTaskAgents.remove(RandomManager.getRandom(RandomKey.SELECT_RANDOM_2).nextInt(canExecuteSubTaskAgents.size()));
//				System.out.println(selectedMember + " をランダムで選びました");

				// すでに選ばれているエージェントの場合は、最初から
				if(selectedAgents.contains(selectedMember)){
//					System.out.println(selectedMember + " はすでに選ばれているのでやり直し");
					continue;
				}
				else{
//					System.out.println(selectedMember + " を" + subtask + " のサブタスク処理のメンバ候補として選びました");
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
	public void sendOfferMessageToTentativeMembers(FixedTask task,
			FixedAgent leader) {
		for(FixedSubtask subtask : task.subtasksByMembers){
			for(FixedAgent agent : subtask.getAgentInfo().getSelectedAgents()){
				TeamFormationMain.getPost().postOfferMessage(agent, new FixedOfferMessage(leader, agent, subtask));
				leader.getParameter().addSendAgents(agent);
			}
		}
	}
	
	public String toString() {
		return "ランダムに仮メンバを選ぶ";
	}

}
