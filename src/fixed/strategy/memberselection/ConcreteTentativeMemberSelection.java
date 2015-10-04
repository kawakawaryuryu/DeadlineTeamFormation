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

public class ConcreteTentativeMemberSelection implements
		TentativeMemberSelectionStrategy {

	@Override
	public boolean searchTentativeMembers(FixedAgent leader, FixedTask task) {
		ArrayList<FixedAgent> selectedAgents = new ArrayList<FixedAgent>();
		
		// エージェントを信頼度にソート
		FixedAgent[] sortedAgents = AgentTaskLibrary.getSortedAgentsFromArray(leader.getTrust(), 
				TeamFormationMain.getParameter().getAgent());
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
	
	private void debugSortedAgents(FixedAgent[] agents, FixedAgent leader) {
		System.out.println("信頼度順で並んだエージェント");
		for(FixedAgent agent : agents){
			System.out.println(agent + " / 信頼度 = " + leader.getTrust(agent));
		}
	}

	@Override
	public void pullExecutedSubtaskByLeader(FixedAgent leader, FixedTask task) {
		int leftDeadline = task.getDeadlineInTask() - FixedConstant.DEADLINE_MIN_2;
		boolean isAssigned = false;
		
		for(FixedSubtask subtask : task.getSubTaskList()){
			if(AgentTaskLibrary.isExecuteSubTask(leader, subtask, leftDeadline) && !isAssigned){
				leader.getParameter().setExecutedSubtasks(subtask, AgentTaskLibrary.calculateExecuteTime(leader, subtask));
				isAssigned = true;
				System.out.println("リーダの処理するサブタスク " + subtask);
			}
			else{
				task.subtasksByMembers.add(subtask);
			}
		}
	}

	@Override
	public boolean selectTentativeMemberEverySubtask(FixedAgent leader, FixedTask task, ArrayList<FixedAgent> selectedAgents, FixedAgent[] sortedAgents) {
		for(FixedSubtask subtask : task.subtasksByMembers){
			int selected = 0;
			FixedAgent selectedMember;	//メンバ候補
			ArrayList<FixedAgent> canExecuteSubTaskAgents = AgentTaskLibrary.getAgentsCanExecuteSubtask(subtask, sortedAgents);	//subtaskを処理できるエージェントのリスト
			
			double probability = RandomManager.getRandom(RandomKey.EPSILON_GREEDY_RANDOM_2).nextDouble();	//ε-greedyの確率
			System.out.println("probability = " + probability);
			while(selected < FixedConstant.SELECT_MEMBER_NUM){
				System.out.println(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");
				
				// デッドラインまでに処理できるエージェントがいなければ
				if(canExecuteSubTaskAgents.isEmpty()){
					System.out.println(subtask + " を処理できるメンバがいないので、メンバ候補探しに失敗しました");
					return false;
				}
				
				// εの確率でランダムにエージェントを選ぶ
				if(probability <= FixedConstant.EPSILON2){
					selectedMember = canExecuteSubTaskAgents.remove(RandomManager.getRandom(RandomKey.SELECT_RANDOM_2).nextInt(canExecuteSubTaskAgents.size()));
					System.out.println(selectedMember + " をランダムで選びました");
				}
				// 1-εの確率で提案受託期待度の高いエージェントを選ぶ
				else{
					selectedMember = canExecuteSubTaskAgents.remove(0);
					System.out.println(selectedMember + " を信頼度順で選びました");
				}
				
				// すでに選ばれているエージェントの場合は、最初から
				if(selectedAgents.contains(selectedMember)){
					System.out.println(selectedMember + " はすでに選ばれているのでやり直し");
					continue;
				}
				else{
					System.out.println(selectedMember + " を" + subtask + " のサブタスク処理のメンバ候補として選びました");
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
}
