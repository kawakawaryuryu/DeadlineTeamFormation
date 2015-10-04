package initialRoleSelect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Administrator;
import main.Constant;
import main.Main;
import message.OfferMessage;
import task.SubTask;
import task.Task;
import agent.Agent;

/**
 * リーダが取るサブタスクは一つだけ
 * 1tickごとの期待報酬
 * メンバの期待報酬は報酬期待度×依頼されたサブタスクの処理時間
 * リーダの期待報酬 = (タスクの全報酬 / タスク処理想定時間) × 欲張り度
 * タスク処理想定時間 = タスクのリソース和 / チーム履歴の平均リソース
 * メンバの期待報酬 = (サブタスクのリソース / タスク処理想定時間) × 報酬期待度
 */
public class RoleSelectStrategy3 implements InitialRoleSelectStrategy {

	/**
	 * 処理のできるメッセージを抽出する
	 * @param messages
	 * @param agent
	 * @return
	 */
	@Override
	public List<OfferMessage> getCanExecuteMessages(List<OfferMessage> messages, Agent agent){
		List<OfferMessage> canExecuteMessages = new ArrayList<OfferMessage>();
		/* 処理できる提案メッセージを抽出 */
		for(OfferMessage message : messages){
			if(isExecuteSubTask(agent, message.getSubTask(), message.getSubTask().getDeadline())){
				canExecuteMessages.add(message);
			}
		}
		
		return canExecuteMessages;
	}
	
	/**
	 * メッセージを選択する
	 * @param messages
	 * @param agent
	 * @return
	 */
	@Override
	public OfferMessage selectMessage(List<OfferMessage> messages, Agent agent) {
		OfferMessage maxExpectedRewardMessage;	//期待報酬が最大のメッセージ（選ばれたメッセージ）
		double probability = Administrator.epsilon_greedy_random1.nextDouble();
		Collections.shuffle(messages, Administrator.shuffle_random1);
		
		/* εの確率でランダムに選ぶ */
		if(probability <= Constant.EPSILON){
//			System.out.println("ランダムに選びます");
			maxExpectedRewardMessage = messages.get(Administrator.select_random1.nextInt(messages.size()));
		}
		/* 1-εの確率で期待報酬の大きいエージェントを選ぶ */
		else{
//			System.out.println("報酬期待度の大きいエージェントを選びます");
			maxExpectedRewardMessage = messages.get(0);
			for(int i = 1; i < messages.size(); i++){
				double expectedReward = (double)messages.get(i).getSubTask().getRequireSum() * agent.getExpectedReward(messages.get(i).getFrom());
				double maxExpectedReward = (double)maxExpectedRewardMessage.getSubTask().getRequireSum() * agent.getExpectedReward(maxExpectedRewardMessage.getFrom());
				if(expectedReward > maxExpectedReward){
					maxExpectedRewardMessage = messages.get(i);
				}
			}
		}
		return maxExpectedRewardMessage;
	}
	
	/**
	 * リーダの期待報酬を計算する
	 * リーダの期待報酬 = (タスクの全報酬 / タスク処理想定時間) × 欲張り度
	 * タスク処理想定時間 = タスクのリソース和 / チーム履歴の平均リソース
	 * @param agent
	 * @param task
	 * @return
	 */
	@Override
	public double calculateExpectedLeaderReward(Agent agent, Task task){
		double leaderReward;	//リーダの期待報酬
		int expectedExecuteTime;	//予想されるタスク実行時間
		
		/* チーム履歴を持っている場合 */
		if(!agent.getPastTeams().isEmpty()){
			//ただのチーム平均リソースの場合
			expectedExecuteTime = (int)Math.ceil((double)task.getTaskRequireSum() / (double)agent.getAverageTeamResource());
			//１人あたりの平均リソースの場合
//			expectedExecuteTime = (int)Math.ceil((double)task.getTaskRequireSum() / ((double)agent.getAverageTeamResource() * (double)task.getSubTaskNum()));
			
			leaderReward = ((double)task.getTaskRequireSum() / (double)expectedExecuteTime) * agent.getGreedy();	//リーダ時の期待報酬の計算
		}
		/* チーム履歴を持っていない場合は、全エージェントのリソースの平均 × サブタスク数をチームリソースとする */
		else{
			leaderReward = -1;	//履歴なし
		}
		
		return leaderReward;
	}
	
	/**
	 * メンバの期待報酬を計算する
	 * メンバの期待報酬 = タスク処理想定時間 × 報酬期待度
	 * @param agent
	 * @param message
	 * @return
	 */
	@Override
	public double calculateExpectedMemberReward(Agent agent, OfferMessage message){
		int expectedExecuteTime = calculateExecuteTime(agent, message.getSubTask());
		double memberReward = (double)expectedExecuteTime * agent.getExpectedReward(message.getFrom());
		return memberReward;
	}

	/**
	 * メンバ候補を探す
	 * （メンバ候補がいなかったら、falseを返す）
	 * @param agent
	 * @param task
	 * @return
	 */
	public boolean searchMemberCandidates(Agent agent, Task task){
		Agent[] trustOrder = new Agent[agent.getTrust().length];	//ソートされた提案受託期待度のインデックスを保持する
//		System.out.println("提案受託期待度でソート");
		trustOrder = getSortArrayOrder(agent.getTrust(), Main.getAgent());	//ソート後の提案受託期待度の添字
		List<Agent> selected_agents = new ArrayList<Agent>();	//すでにメッセージを送ったエージェントを記憶
		
		task.sortSubTaskListByRequire();	//タスクの各サブタスクのリソースを降順にソート
//		System.out.println("\nソートされた後のサブタスク");
//		for(SubTask subtask : task.getSubTaskList()){
//			System.out.println(subtask);
//		}
		
		/* リーダが処理するサブタスクをリストから取り出す */
		pullLeaderExecuteSubTask(agent, task);
		
		selected_agents.add(agent);	//リーダを格納
//		System.out.println("リーダが処理する分のサブタスクを抜き取った後のサブタスク");
//		for(SubTask subtask : task.memberSubTaskList){
//			System.out.println(subtask);
//		}
//		System.out.println("サブタスク数:" + task.memberSubTaskList.size() + "\n");
		
		/* サブタスクごとにメンバ候補を選択 */
		for(SubTask subtask : task.subtasksByMembers){
			int selected = 0;
			Agent selected_member;	//メンバ候補
			List<Agent> canExecuteSubTaskAgents = executeSubTaskAgents(subtask, trustOrder);	//subtaskを処理できるエージェントのリスト
			
			double probability = Administrator.epsilon_greedy_random2.nextDouble();	//ε-greedyの確率
			while(selected < Constant.SELECT_MEMBER_NUM){
//				System.out.println(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");
				
				/* デッドラインまでに処理できるエージェントがいなければ、 */
				if(canExecuteSubTaskAgents.isEmpty() == true){
//					System.out.println("デッドラインまでに処理できるエージェントが見つかりませんでした");
					return false;
				}
				
				/* εの確率でランダムにエージェントを選ぶ */
				if(probability <= Constant.EPSILON2){
//					System.out.println("ランダムに選びます");
					selected_member = canExecuteSubTaskAgents.remove(Administrator.select_random2.nextInt(canExecuteSubTaskAgents.size()));
				}
				/* 1-εの確率で提案受託期待度の高いエージェントを選ぶ */
				else{
//					System.out.println("提案受託期待度の高いエージェントを選びます");
					
					selected_member = canExecuteSubTaskAgents.remove(0);
				}
				
				/* すでに選ばれているエージェントの場合は、最初から */
				if(selected_agents.contains(selected_member) == true){
//					System.out.println(selected_member + "はすでに選ばれていたので、選び直し");
					continue;
				}
				else{
//					System.out.println(selected_member + " のエージェントを" + subtask + " のサブタスク処理のメンバ候補として選びました");
					selected_agents.add(selected_member);
					subtask.candidates.add(selected_member);	//サブタスクがメンバ候補を保持
//					System.out.println("サブタスクのcandidates: " + subtask.getMemberCandidate().get(selected));
					selected++;
				}
			}
//			System.out.println();
		}
		
		/* メンバ候補に提案メッセージを送る */
//		System.out.println("メンバ候補を全員探せたので、提案メッセージを送ります");
		for(SubTask subtask : task.subtasksByMembers){
			for(int i = 0; i < subtask.candidates.size(); i++){
				Agent selected_member = subtask.candidates.get(i);
				selected_member.addOfferMessage(new OfferMessage(agent, subtask));	//メンバ候補に提案メッセージを送る
				agent.addSendAgent(selected_member);	//エージェントがメッセージを送ったメンバ候補を保持
//				System.out.println(selected_member + "に提案メッセージを送ります");
			}
		}
		
//		System.out.println("メンバ候補選択を終了します");
		return true;
	}
	
	/**
	 * リーダが処理するサブタスクをリストから引き抜き、リーダ以外が処理するサブタスクリストはmemberSubTaskListに格納
	 * @param agent
	 * @param task リーダが取り出したタスク
	 */
	@Override
	public void pullLeaderExecuteSubTask(Agent agent, Task task){
		int leftDeadline = task.getDeadlineInTask() - 2 * Constant.WAIT_TURN;
		boolean isAssigned = false;	//リーダに割り当てたかどうか
		for(int i = 0; i < task.getSubTaskList().size(); i++){
			if(isExecuteSubTask(agent, task.getSubTaskList(i), leftDeadline) == true && isAssigned == false){
				//リーダの処理するサブタスクをexecuted_subtaskに代入
				agent.addExecutedSubTaskList(task.getSubTaskList(i));
				agent.addExecuteTime(calculateExecuteTime(agent, task.getSubTaskList(i)));
				isAssigned = true;
			}
			else{
				task.subtasksByMembers.add(task.getSubTaskList(i));
			}
		}
		
//		System.out.print(agent + "が処理するサブタスク : ");
//		for(SubTask subtask : agent.getExecutedSubTaskList()){
//			System.out.print(subtask + ", ");
//		}
//		System.out.println("\n" + agent + " の処理時間 = " + agent.getExecuteTime());
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
		Collections.shuffle(entries, Administrator.sort_random1);
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
	 * サブタスクをデッドラインまでに処理できるエージェントのリストを返す
	 * @param subtask
	 * @param agents 提案受託期待度によってソートされたエージェントの配列
	 * @return
	 */
	public List<Agent> executeSubTaskAgents(SubTask subtask, Agent[] agents){
		List<Agent> executeAgents = new ArrayList<Agent>();
		int taskDeadline = subtask.getDeadline() - 2 * Constant.WAIT_TURN;	//実行状態でのタスク処理デッドライン
		for(Agent agent : agents){
			if(isExecuteSubTask(agent, subtask, taskDeadline) == true){
				executeAgents.add(agent);
			}
		}
		return executeAgents;
	}
	
	/**
	 * 実行状態でのエージェントのサブタスクの処理時間を計算
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
		return executeTime;
	}

	/**
	 * 初期役割選択状態の戦略を返す
	 */
	public String toString(){
		return "1tickごとの報酬を学習する戦略 + リーダが取るサブタスクは一つのみ";
	}

}
