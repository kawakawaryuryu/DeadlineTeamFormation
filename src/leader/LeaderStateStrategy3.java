package leader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Administrator;
import main.Constant;
import task.SubTask;
import agent.Agent;

/**
 * 割り当てが決まらなかったサブタスクはリーダとメンバの中で余裕があるエージェント（ここでは実行時間の短い順）に再割り当てを行う
 */
public class LeaderStateStrategy3 implements LeaderStrategy {

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
		
		/* まだ割り当てが決まっていないサブタスクをエージェントに割り当て */
		if(leader.getLeaderFields().notAssignedSubTask.isEmpty() == false){
			double[] temporaryTeamAgentsExecutingTime = new double[leader.getLeaderFields().trueAgentAndLeaderList.size()];	//ソート後のエージェントを保持する配列
	
			sortNotAssignedSubTaskList(leader);
			
			for(SubTask subtask : leader.getLeaderFields().notAssignedSubTask){
//				System.out.println("仮チームのエージェントを処理時間の短い順でソート");
				for(int i = 0; i < leader.getLeaderFields().trueAgentAndLeaderList.size(); i++){
					temporaryTeamAgentsExecutingTime[i] = leader.getLeaderFields().trueAgentAndLeaderList.get(i).getExecuteTime();
				}
				Agent[] sortedTemporaryTeamAgents = getSortArrayOrder(temporaryTeamAgentsExecutingTime ,leader.getLeaderFields().trueAgentAndLeaderList);	//仮チームのエージェントを提案受託期待度でソート
				
				boolean isAssign = false;	//サブタスクが割り当てられたかどうか
				for(int i = 0; i < sortedTemporaryTeamAgents.length; i++){
					int taskDeadline = subtask.getDeadline() - sortedTemporaryTeamAgents[i].getExecuteTime() - 1 * Constant.WAIT_TURN;
					if(isExecuteSubTask(sortedTemporaryTeamAgents[i], subtask, taskDeadline) == true){
//						System.out.println(subtask + " を処理するメンバを " + sortedTrueAgents[i] + " のエージェントにしました");
						sortedTemporaryTeamAgents[i].willBeExecutedSubTaskList.add(subtask);	//メンバが処理する予定のサブタスクを追加
						sortedTemporaryTeamAgents[i].addExecuteTime(calculateExecuteTime(sortedTemporaryTeamAgents[i], subtask));	//エージェントのタスク処理時間を保持
						
						/* メンバーリストに入っていなかったらメンバーリストに入れる */
						if(leader.getLeaderFields().memberList.contains(sortedTemporaryTeamAgents[i]) == false && sortedTemporaryTeamAgents[i] != leader){
							leader.getLeaderFields().memberList.add(sortedTemporaryTeamAgents[i]);
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
		double probability = Administrator.epsilon_greedy_random3.nextDouble();	//ε-greedyの確率
		Agent maxTrustMember;
		/* εの確率でランダムにメンバを決める */
		if(probability <= Constant.EPSILON){
			maxTrustMember = subtask.candidates.get(Administrator.select_random4.nextInt(subtask.candidates.size()));
			for(int i = 0; i < subtask.candidates.size(); i++){
				if(subtask.candidates.get(i) != maxTrustMember){
					leader.getLeaderFields().notAssignToMemberList.add(subtask.candidates.get(i));
				}
			}
		}
		else{
			/* 提案受託期待度の高いメンバーを選ぶ */
			maxTrustMember = subtask.candidates.get(0);
			for(int i = 1; i < subtask.candidates.size(); i++){
				if(leader.getTrust(maxTrustMember) < leader.getTrust(subtask.candidates.get(i))){
					leader.getLeaderFields().notAssignToMemberList.add(maxTrustMember);
					maxTrustMember = subtask.candidates.get(i);
				}
				else{
					leader.getLeaderFields().notAssignToMemberList.add(subtask.candidates.get(i));
				}
			}
		}
		
		return maxTrustMember;
	}
	

	/**
	 * 配列を降順にソートして、その添字であるエージェントの配列を返す
	 * @param array ソートしたい配列
	 * @param agents 配列の値を持つエージェント
	 * @return
	 */
	public Agent[] getSortArrayOrder(double[] array, List<Agent> agents){
		Agent[] arrayOrder = new Agent[array.length];	//ソート後のエージェントを保持
		Map<Agent, Double> copy = new HashMap<Agent, Double>();	//配列を一端コピーするマップ
		
		/* 配列のコピー */
		for(int i = 0; i < array.length; i++){
			copy.put(agents.get(i), array[i]);
		}
		
		/* 配列のソート */
		List<Map.Entry<Agent, Double>> entries = new ArrayList<Map.Entry<Agent, Double>>(copy.entrySet());
		Collections.shuffle(entries, Administrator.sort_random2);
		Collections.sort(entries, new Comparator<Map.Entry<Agent, Double>>(){
			public int compare(Map.Entry<Agent, Double> map1, Map.Entry<Agent, Double> map2){
				if(map2.getValue() - map1.getValue() > 0) return 1;
				else if(map2.getValue() - map1.getValue() < 0) return -1;
				else return 0;
			}
		});
		
		/* ソートされた配列の表示、配列のインデックスの保持 */
		int order = 0;
		for(Map.Entry<Agent, Double> entry : entries){
//			System.out.println("key:" + entry.getKey() + " / value:" + entry.getValue() + " / order = " + order);
			arrayOrder[order] = entry.getKey();
			order++;
		}
		return arrayOrder;
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
	 * 
	 */
	public String toString(){
		return "リーダとメンバの中で処理時間の短い順に再割り当てを行う";
	}

}
