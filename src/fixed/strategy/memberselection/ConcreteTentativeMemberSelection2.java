package fixed.strategy.memberselection;

import java.util.ArrayList;

import main.Constant;
import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.library.AgentTaskLibrary;
import fixed.main.RandomKey;
import fixed.main.RandomManager;
import fixed.main.TeamFormationMain;
import fixed.message.FixedOfferMessage;
import fixed.task.FixedSubtask;
import fixed.task.FixedTask;

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
	public boolean searchTentativeMembers(FixedAgent leader, FixedTask task) {
		ArrayList<FixedAgent> selectedAgents = new ArrayList<FixedAgent>();

		// エージェントを信頼度にソート
		FixedAgent[] sortedAgents = AgentTaskLibrary.getSortedAgentsFromArray(leader.getTrust(), 
				TeamFormationMain.getParameter().getAgent());
//		debugSortedAgents(sortedAgents, leader);

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
	public boolean selectTentativeMemberEverySubtask(FixedAgent leader, FixedTask task, ArrayList<FixedAgent> selectedAgents, FixedAgent[] sortedAgents) {
		for(int selected = 0; selected < Constant.SELECT_MEMBER_NUM; selected++){

			int i = 0;
			while(i < task.subtasksByMembers.size()){
				FixedSubtask subtask = task.subtasksByMembers.get(i);
//				System.out.println(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");

				double probability = RandomManager.getRandom(RandomKey.EPSILON_GREEDY_RANDOM_2).nextDouble();	//ε-greedyの確率
//				System.out.println("probability = " + probability);

				FixedAgent selectedMember;	//メンバ候補
				ArrayList<FixedAgent> canExecuteSubTaskAgents = AgentTaskLibrary.getAgentsCanExecuteSubtask(subtask, sortedAgents);	//subtaskを処理できるエージェントのリスト

				while(true){
					// デッドラインまでに処理できるエージェントがいなければ
					if(canExecuteSubTaskAgents.isEmpty()){
//						System.out.println(subtask + " を処理できるメンバがいないので、メンバ候補探しに失敗しました");
						return false;
					}

					// εの確率でランダムにエージェントを選ぶ
					if(probability <= FixedConstant.EPSILON2){
						selectedMember = canExecuteSubTaskAgents.remove(RandomManager.getRandom(RandomKey.SELECT_RANDOM_2).nextInt(canExecuteSubTaskAgents.size()));
//						System.out.println(selectedMember + " をランダムで選びました");
					}
					// 1-εの確率で提案受託期待度の高いエージェントを選ぶ
					else{
						selectedMember = canExecuteSubTaskAgents.remove(0);
//						System.out.println(selectedMember + " を信頼度順で選びました");
					}

					// すでに選ばれているエージェントの場合は、最初から
					if(selectedAgents.contains(selectedMember)){
//						System.out.println(selectedMember + " はすでに選ばれているのでやり直し");
						continue;
					}
					else{
//						System.out.println(selectedMember + " を" + subtask + " のサブタスク処理のメンバ候補として選びました");
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
		return "仮メンバを信頼度の高い順に選ぶ";
	}
}
