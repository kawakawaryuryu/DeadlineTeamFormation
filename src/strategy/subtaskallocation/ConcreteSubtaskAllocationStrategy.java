package strategy.subtaskallocation;

import java.util.Collections;
import java.util.Comparator;

import task.Subtask;
import library.AgentTaskLibrary;
import log.Log;
import main.RandomKey;
import main.RandomManager;
import constant.Constant;
import agent.Agent;

public class ConcreteSubtaskAllocationStrategy implements
		SubtaskAllocationStrategy {

	@Override
	public void decideMembers(Agent leader) {
		boolean isTeaming;
		
		// サブタスクごとにメンバを絞る
		Log.log.debugln("サブタスクごとにメンバを一人に絞り込みます");
		isTeaming = decideMemberEverySubtask(leader);
		Log.log.debugln();
		
		leader.getParameter().getLeaderField().isTeaming = isTeaming;
		
		// チーム編成成功の場合は、メンバをチームに加える
		if(leader.getParameter().getLeaderField().isTeaming){
			Log.log.debugln("チーム編成成功！");
			leader.getParameter().getParticipatingTeam().addTeamMate(leader);
			for(Agent member : leader.getParameter().getLeaderField().memberSubtaskMap.keySet()){
				for(Subtask subtask : leader.getParameter().getLeaderField().memberSubtaskMap.get(member)){
					member.getParameter().setExecutedSubtasks(subtask, AgentTaskLibrary.calculateExecuteTime(member, subtask));
				}
				leader.getParameter().getParticipatingTeam().addMember(member);
			}
		}

	}
	
	private boolean decideMemberEverySubtask(Agent leader) {
		for(Subtask subtask : leader.getParameter().getMarkedTask().subtasksByMembers){
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
				Log.log.debugln(subtask + " を処理するメンバはいません");
			}
			
			// 今のエージェントをメンバとする
			else if(selectedAgentsNum == 1){
				Agent member = subtask.getAgentInfo().getSelectedAgent(0);
				setToLeaderField(leader, member, subtask);
				Log.log.debugln(subtask + " を処理するメンバは " + member + " に決まりました");
			}
			
			// メンバを一人に絞る
			else if(selectedAgentsNum > 1){
				Agent member = selectMember(leader, subtask);
				setToLeaderField(leader, member, subtask);
				Log.log.debugln(subtask + " を処理するメンバは " + member + " に決まりました");
			}
			
			else{
				System.err.println("メンバ候補の数がありえません");
				System.exit(-1);
			}
		}
		
		// 割り当てが決まっていないサブタスクがある場合は、再割り当てを行う
		if(!leader.getParameter().getLeaderField().notAssignedSubTask.isEmpty()){
			Log.log.debugln("割り当てが決まっていないサブタスクを割り当てます");
			return allocateNotAllocatedSubtask(leader);
		}
		else{
			leader.getParameter().getLeaderField().isTeamingAgainAllocation = false;
			return true;
		}
	}
	
	private void setToLeaderField(Agent leader, Agent member, Subtask subtask) {
		leader.getParameter().getLeaderField().members.add(member);
		leader.getParameter().getLeaderField().setAgentToMemberSubtaskMap(member);
		leader.getParameter().getLeaderField().addSubtaskToMemberSubtaskMap(member, subtask);
	}
	
	@Override
	public Agent selectMember(Agent leader, Subtask subtask) {
		Collections.shuffle(subtask.getAgentInfo().getSelectedAgents(), RandomManager.getRandom(RandomKey.SORT_RANDOM_3));
		double probability = RandomManager.getRandom(RandomKey.EPSILON_GREEDY_RANDOM_3).nextDouble();	//ε-greedyの確率
		Agent maxTrustMember;
		// εの確率でランダムにメンバを決める
		if(probability <= Constant.EPSILON){
			int randomIndex = RandomManager.getRandom(RandomKey.SELECT_RANDOM_4).nextInt(subtask.getAgentInfo().getSelectedAgents().size());
			maxTrustMember = subtask.getAgentInfo().getSelectedAgent(randomIndex);
		}
		// 提案受託期待度の高いメンバーを選ぶ
		else{
			maxTrustMember = subtask.getAgentInfo().getSelectedAgent(0);
			for(int i = 1; i < subtask.getAgentInfo().getSelectedAgents().size(); i++){
				Agent agent = subtask.getAgentInfo().getSelectedAgent(i);
				if(leader.getTrustToMember(maxTrustMember) < leader.getTrustToMember(agent)){
					maxTrustMember = agent;
				}
			}
		}
		
		return maxTrustMember;
	}
	
	@Override
	public boolean allocateNotAllocatedSubtask(Agent leader) {
		// OKメッセージが返ってきたエージェントを信頼度でソート
		Agent[] sortedAgents = AgentTaskLibrary.getSortedAgentsFromArray(leader.getTrustToMember(), 
				leader.getParameter().getLeaderField().trueAgents);

		// まだ割り当てられていないサブタスクをリソースの降順にソート
		sortNotAssignedSubTaskList(leader);
		
		// サブタスクごとに割り当てるエージェントを決めていく
		return decideMemberEveryNotAllocatedSubtask(leader, sortedAgents);
	}
	
	private void sortNotAssignedSubTaskList(Agent leader){
		Collections.shuffle(leader.getParameter().getLeaderField().notAssignedSubTask, 
				RandomManager.getRandom(RandomKey.SORT_RANDOM_4));
		Collections.sort(leader.getParameter().getLeaderField().notAssignedSubTask, new Comparator<Subtask>(){
			@Override
			public int compare(Subtask st1, Subtask st2){
				return st2.getRequireSum() - st1.getRequireSum();
			}
		});
	}
	
	private boolean decideMemberEveryNotAllocatedSubtask(Agent leader, Agent[] sortedAgents) {
		for(Subtask subtask : leader.getParameter().getLeaderField().notAssignedSubTask){
			boolean isAllocated = false;
			for(Agent member : sortedAgents){
				int executeTime = getExecuteTime(leader, member);
				int deadline = subtask.getDeadline() - executeTime - Constant.DEADLINE_MIN_1;
				if(AgentTaskLibrary.isExecuteSubTask(member, subtask, deadline)){
					if(!leader.getParameter().getLeaderField().memberSubtaskMap.containsKey(member)){
						leader.getParameter().getLeaderField().members.add(member);
						leader.getParameter().getLeaderField().setAgentToMemberSubtaskMap(member);
					}
					leader.getParameter().getLeaderField().addSubtaskToMemberSubtaskMap(member, subtask);
					Log.log.debugln(subtask + " を処理するメンバは " + member + " に決まりました");
					
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
	
	private int getExecuteTime(Agent leader, Agent member) {
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
