package fixed.strategy.subtaskallocation;

import java.util.Collections;
import java.util.Comparator;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.library.AgentTaskLibrary;
import fixed.main.RandomKey;
import fixed.main.RandomManager;
import fixed.task.FixedSubtask;

public class ConcreteSubtaskAllocationStrategy implements
		SubtaskAllocationStrategy {

	@Override
	public void decideMembers(FixedAgent leader) {
		boolean isTeaming;
		
		// サブタスクごとにメンバを絞る
//		System.out.println("サブタスクごとにメンバを一人に絞り込みます");
		isTeaming = decideMemberEverySubtask(leader);
//		System.out.println();
		
		leader.getParameter().getLeaderField().isTeaming = isTeaming;
		
		// チーム編成成功の場合は、メンバをチームに加える
		if(leader.getParameter().getLeaderField().isTeaming){
//			System.out.println("チーム編成成功！");
			leader.getParameter().getParticipatingTeam().addTeamMate(leader);
			for(FixedAgent member : leader.getParameter().getLeaderField().memberSubtaskMap.keySet()){
				for(FixedSubtask subtask : leader.getParameter().getLeaderField().memberSubtaskMap.get(member)){
					member.getParameter().setExecutedSubtasks(subtask, AgentTaskLibrary.calculateExecuteTime(member, subtask));
				}
				leader.getParameter().getParticipatingTeam().addMember(member);
			}
		}

	}
	
	private boolean decideMemberEverySubtask(FixedAgent leader) {
		for(FixedSubtask subtask : leader.getParameter().getMarkedTask().subtasksByMembers){
			// OKメッセージが返ってきていないエージェントはメンバ候補から削除する
			for(int i = 0; i < subtask.getAgentInfo().getSelectedAgents().size();){
				if(!leader.getParameter().getLeaderField().trueAgents.contains(subtask.getAgentInfo().getSelectedAgent(i))){
					subtask.getAgentInfo().removeAgent(i);
				}
				else i++;
			}
			
			int selectedAgentsNum = subtask.getAgentInfo().getSelectedAgents().size();
			
			// 割り当てエージェントがいない場合、あとで再割り当てを行うためにリストに退避
			if(selectedAgentsNum == 0){
				leader.getParameter().getLeaderField().notAssignedSubTask.add(subtask);
//				System.out.println(subtask + " を処理するメンバはいません");
			}
			
			// 今のエージェントをメンバとする
			else if(selectedAgentsNum == 1){
				FixedAgent member = subtask.getAgentInfo().getSelectedAgent(0);
				setToLeaderField(leader, member, subtask);
//				System.out.println(subtask + " を処理するメンバは " + member + " に決まりました");
			}
			
			// メンバを一人に絞る
			else if(selectedAgentsNum > 1){
				FixedAgent member = selectMember(leader, subtask);
				setToLeaderField(leader, member, subtask);
//				System.out.println(subtask + " を処理するメンバは " + member + " に決まりました");
			}
			
			else{
				System.err.println("メンバ候補の数がありえません");
				System.exit(-1);
			}
		}
		
		// 割り当てが決まっていないサブタスクがある場合は、再割り当てを行う
		if(!leader.getParameter().getLeaderField().notAssignedSubTask.isEmpty()){
//			System.out.println("割り当てが決まっていないサブタスクを割り当てます");
			return allocateNotAllocatedSubtask(leader);
		}
		else{
			leader.getParameter().getLeaderField().isTeamingAgainAllocation = false;
			return true;
		}
	}
	
	private void setToLeaderField(FixedAgent leader, FixedAgent member, FixedSubtask subtask) {
		leader.getParameter().getLeaderField().members.add(member);
		leader.getParameter().getLeaderField().setAgentToMemberSubtaskMap(member);
		leader.getParameter().getLeaderField().addSubtaskToMemberSubtaskMap(member, subtask);
	}
	
	@Override
	public FixedAgent selectMember(FixedAgent leader, FixedSubtask subtask) {
		Collections.shuffle(subtask.getAgentInfo().getSelectedAgents(), RandomManager.getRandom(RandomKey.SORT_RANDOM_3));
		double probability = RandomManager.getRandom(RandomKey.EPSILON_GREEDY_RANDOM_3).nextDouble();	//ε-greedyの確率
		FixedAgent maxTrustMember;
		// εの確率でランダムにメンバを決める
		if(probability <= FixedConstant.EPSILON){
			int randomIndex = RandomManager.getRandom(RandomKey.SELECT_RANDOM_4).nextInt(subtask.getAgentInfo().getSelectedAgents().size());
			maxTrustMember = subtask.getAgentInfo().getSelectedAgent(randomIndex);
		}
		// 提案受託期待度の高いメンバーを選ぶ
		else{
			maxTrustMember = subtask.getAgentInfo().getSelectedAgent(0);
			for(int i = 1; i < subtask.getAgentInfo().getSelectedAgents().size(); i++){
				FixedAgent agent = subtask.getAgentInfo().getSelectedAgent(i);
				if(leader.getTrust(maxTrustMember) < leader.getTrust(agent)){
					maxTrustMember = agent;
				}
			}
		}
		
		return maxTrustMember;
	}
	
	@Override
	public boolean allocateNotAllocatedSubtask(FixedAgent leader) {
		// OKメッセージが返ってきたエージェントを信頼度でソート
		FixedAgent[] sortedAgents = AgentTaskLibrary.getSortedAgentsFromArray(leader.getTrust(), 
				leader.getParameter().getLeaderField().trueAgents);

		// まだ割り当てられていないサブタスクをリソースの降順にソート
		sortNotAssignedSubTaskList(leader);
		
		// サブタスクごとに割り当てるエージェントを決めていく
		return decideMemberEveryNotAllocatedSubtask(leader, sortedAgents);
	}
	
	private void sortNotAssignedSubTaskList(FixedAgent leader){
		Collections.shuffle(leader.getParameter().getLeaderField().notAssignedSubTask, 
				RandomManager.getRandom(RandomKey.SORT_RANDOM_4));
		Collections.sort(leader.getParameter().getLeaderField().notAssignedSubTask, new Comparator<FixedSubtask>(){
			@Override
			public int compare(FixedSubtask st1, FixedSubtask st2){
				return st2.getRequireSum() - st1.getRequireSum();
			}
		});
	}
	
	private boolean decideMemberEveryNotAllocatedSubtask(FixedAgent leader, FixedAgent[] sortedAgents) {
		for(FixedSubtask subtask : leader.getParameter().getLeaderField().notAssignedSubTask){
			boolean isAllocated = false;
			for(FixedAgent member : sortedAgents){
				int executeTime = getExecuteTime(leader, member);
				int deadline = subtask.getDeadline() - executeTime - FixedConstant.DEADLINE_MIN_1;
				if(AgentTaskLibrary.isExecuteSubTask(member, subtask, deadline)){
					if(!leader.getParameter().getLeaderField().memberSubtaskMap.containsKey(member)){
						leader.getParameter().getLeaderField().members.add(member);
						leader.getParameter().getLeaderField().setAgentToMemberSubtaskMap(member);
					}
					leader.getParameter().getLeaderField().addSubtaskToMemberSubtaskMap(member, subtask);
//					System.out.println(subtask + " を処理するメンバは " + member + " に決まりました");
					
					isAllocated = true;
					break;
				}
			}
			
			if(!isAllocated){
				leader.getParameter().getLeaderField().isTeaming = false;
				leader.getParameter().getLeaderField().isTeamingAgainAllocation = false;
				return false;
			}
		}
		
		return true;
	}
	
	private int getExecuteTime(FixedAgent leader, FixedAgent member) {
		if(leader.getParameter().getLeaderField().memberSubtaskMap.containsKey(member)){
			return AgentTaskLibrary.calculateExecuteTimeSum(member, 
					leader.getParameter().getLeaderField().memberSubtaskMap.get(member));
		}
		else if(leader.equals(member)){
			return AgentTaskLibrary.calculateExecuteTimeSum(leader, leader.getParameter().getExecutedSubtasks());
		}
		else{
			return 0;
		}
	}

	public String toString() {
		return "割り当てが決まらないサブタスクは信頼度の大きい順に再割り当てを行う";
	}
}
