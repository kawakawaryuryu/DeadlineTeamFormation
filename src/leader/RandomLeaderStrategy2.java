package leader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import main.Administrator;
import main.Constant;
import task.SubTask;
import agent.Agent;

/**
 * ランダムに再割り当てを行う
 */
public class RandomLeaderStrategy2 implements LeaderStrategy {

	/**
	 * 仮チームのメンバにサブタスクを振り割る
	 */
	@Override
	public void decideMember(Agent leader) {
		for(SubTask subtask : leader.getMarkingTask().subtasksByMembers){
			Agent member;	//メンバ
			
			/* 返答OKメッセージが返ってきていないエージェントをメンバ候補から削除する */
			for(int i = 0; i < subtask.candidates.size();){
				Agent candidate = subtask.candidates.get(i);
				if(leader.getLeaderFields().trueAgentList.contains(candidate) == false){
//					System.out.println(candidate + " を " + subtask + "　のサブタスク処理のメンバ候補から削除します");
					subtask.candidates.remove(i);
				}
				else i++;
			}
			
			/* サブタスクを処理するメンバ候補がいない場合はnotAssignSubTaskに追加 */
			if(subtask.candidates.size() == 0){
//				System.out.println(subtask + " を処理するメンバ候補がいないため、保留");
				leader.getLeaderFields().notAssignedSubTask.add(subtask);
			}
			
			/* メンバ候補が２人以上いるときは、メンバ候補を１人に絞る */
			else if(subtask.candidates.size() > 1){
				member = selectMember(leader, subtask);
				member.willBeExecutedSubTaskList.add(subtask);	//メンバが処理する予定のサブタスクを追加
				leader.getLeaderFields().memberList.add(member);	
				member.addExecuteTime(calculateExecuteTime(member, subtask));	//エージェントのタスク処理時間を保持
//				System.out.println("メンバ候補が２人以上いたため、" + subtask + "を処理するメンバを " + member + " のエージェントとしました");
			}
			
			/* メンバ候補がすでに１人の場合はそのエージェントをメンバーとする */
			else{
				member = subtask.candidates.get(0);
				member.willBeExecutedSubTaskList.add(subtask);	//メンバが処理する予定のサブタスクを追加
				leader.getLeaderFields().memberList.add(member);
				member.addExecuteTime(calculateExecuteTime(member, subtask));	//エージェントのタスク処理時間を保持
//				System.out.println(subtask + "を処理するメンバを " + member + " のエージェントとしました");
			}
		}
		
		/* まだ割り当てが決まっていないサブタスクをランダムにエージェントに割り当て */
		if(leader.getLeaderFields().notAssignedSubTask.isEmpty() == false){
			sortNotAssignedSubTaskList(leader);
			
			for(SubTask subtask : leader.getLeaderFields().notAssignedSubTask){
				List<Agent> sortedTemporaryTeamAgents = leader.getLeaderFields().trueAgentAndLeaderList;
				Collections.shuffle(sortedTemporaryTeamAgents, Administrator.sort_random2);
				
				boolean isAssign = false;	//サブタスクが割り当てられたかどうか
				for(int i = 0; i < sortedTemporaryTeamAgents.size(); i++){
					int taskDeadline = subtask.getDeadline() - sortedTemporaryTeamAgents.get(i).getExecuteTime() - 1 * Constant.WAIT_TURN;
					if(isExecuteSubTask(sortedTemporaryTeamAgents.get(i), subtask, taskDeadline) == true){
//						System.out.println(subtask + " を処理するメンバを " + sortedTrueAgents[i] + " のエージェントにしました");
						sortedTemporaryTeamAgents.get(i).willBeExecutedSubTaskList.add(subtask);	//メンバが処理する予定のサブタスクを追加
						sortedTemporaryTeamAgents.get(i).addExecuteTime(calculateExecuteTime(sortedTemporaryTeamAgents.get(i), subtask));	//エージェントのタスク処理時間を保持
						
						/* メンバーリストに入っていなかったらメンバーリストに入れる */
						if(leader.getLeaderFields().memberList.contains(sortedTemporaryTeamAgents.get(i)) == false && sortedTemporaryTeamAgents.get(i) != leader){
							leader.getLeaderFields().memberList.add(sortedTemporaryTeamAgents.get(i));
						}
						
						isAssign = true;
						break;
					}
				}
				
				/* サブタスクを割り当てられなかったときはチーム編成失敗 */
				if(isAssign == false){
//					System.out.println(subtask + " 処理するメンバがいないため、チーム編成失敗となりました");
					leader.getLeaderFields().isTeaming = false;
					leader.getLeaderFields().isTeamingAgainAllocation = false;
					break;
				}
			}
		}
		else{
			leader.getLeaderFields().isTeamingAgainAllocation = false;
		}
		
		/* チーム編成に成功した場合は、チーム情報にメンバを加える */
		if(leader.getLeaderFields().isTeaming){
			leader.getTeamInfo().addTeamMate(leader);
			for(Agent member : leader.getLeaderFields().memberList){
				leader.getTeamInfo().addMembers(member);
			}
		}
	}
	
	/**
	 * notAssignedSubTaskのリストをリソース順にソートする
	 * @param leader 
	 */
	public void sortNotAssignedSubTaskList(Agent leader){
		Collections.shuffle(leader.getLeaderFields().notAssignedSubTask, Administrator.sort_random4);
		Collections.sort(leader.getLeaderFields().notAssignedSubTask, new Comparator<SubTask>(){
			public int compare(SubTask st1, SubTask st2){
				return st2.getRequireSum() - st1.getRequireSum();
			}
		});
	}
	
	/**
	 * リーダ以外が処理する残りサブタスクリソース量を求める
	 * @param leader
	 * @return
	 */
	@Override
	public int calculateLeftRequireSum(Agent leader) {
		int leftRequireSum = leader.getMarkingTask().getTaskRequireSum();
		
		for(SubTask subtask : leader.getExecutedSubTaskList()){
			leftRequireSum -= subtask.getRequireSum();	//リーダ以外が処理するサブタスクのリソースの合計
		}
		return leftRequireSum;
	}
	
	/**
	 * メンバ候補からメンバを１人絞りこむ
	 * @param candidates
	 * @return
	 */
	public Agent selectMember(Agent leader, SubTask subtask){
		Collections.shuffle(subtask.candidates, Administrator.sort_random3);
		Agent selectedMember;
		
		selectedMember = subtask.candidates.get(Administrator.select_random4.nextInt(subtask.candidates.size()));
		for(int i = 0; i < subtask.candidates.size(); i++){
			if(subtask.candidates.get(i) != selectedMember){
				leader.getLeaderFields().notAssignToMemberList.add(subtask.candidates.get(i));
			}
		}
		
		return selectedMember;
	}

	/**
	 * エージェントがデッドラインまでにサブタスクを処理できるかどうか
	 * @param agent
	 * @param subtask
	 * @param taskDeadline 
	 * @return
	 */
	public boolean isExecuteSubTask(Agent agent, SubTask subtask, int taskDeadline){
		if(calculateExecuteTime(agent, subtask) <= taskDeadline){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * エージェントのサブタスクの処理時間を計算
	 * @param agent
	 * @param subtask
	 * @return
	 */
	public int calculateExecuteTime(Agent agent, SubTask subtask){
		int[] time = new int[Constant.RESOURCE_NUM];
		int executeTime = 0;
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			/* タスク処理にかかる時間の計算 */
			time[i] = (int)Math.ceil((double)subtask.getRequire()[i] / (double)agent.getAbility()[i]);
			
			if(executeTime < time[i]){
				executeTime = time[i];
			}
		}
//		System.out.println("エージェント" + agent + "がサブタスク" + subtask + "の処理にかかる時間 = " + executeTime);
		return executeTime;
	}

	/**
	 * リーダ状態の戦略を返す
	 */
	public String toString(){
		return "ランダムに再割り当てを行う";
	}

}
