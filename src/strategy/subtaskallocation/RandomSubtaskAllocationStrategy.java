package strategy.subtaskallocation;

import task.FixedSubtask;
import library.AgentTaskLibrary;
import main.RandomKey;
import main.RandomManager;
import agent.FixedAgent;

public class RandomSubtaskAllocationStrategy implements
		SubtaskAllocationStrategy {

	@Override
	public void decideMembers(FixedAgent leader) {
		boolean isTeaming;
		
		// サブタスクごとにメンバを絞る
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
			
			// 割り当てエージェントがいない場合、チーム編成失敗
			if(selectedAgentsNum == 0){
				return false;
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
		return true;
	}
	
	private void setToLeaderField(FixedAgent leader, FixedAgent member, FixedSubtask subtask) {
		leader.getParameter().getLeaderField().members.add(member);
		leader.getParameter().getLeaderField().setAgentToMemberSubtaskMap(member);
		leader.getParameter().getLeaderField().addSubtaskToMemberSubtaskMap(member, subtask);
	}

	@Override
	public FixedAgent selectMember(FixedAgent leader, FixedSubtask subtask) {
		FixedAgent maxTrustMember;
		
		int randomIndex = RandomManager.getRandom(RandomKey.SELECT_RANDOM_4).nextInt(subtask.getAgentInfo().getSelectedAgents().size());
		maxTrustMember = subtask.getAgentInfo().getSelectedAgent(randomIndex);
		
		return maxTrustMember;
	}

	@Override
	public boolean allocateNotAllocatedSubtask(FixedAgent leader) {
		return false;
	}

	public String toString() {
		return "再割り当ては行わない";
	}
}
