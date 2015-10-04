package initialRoleSelect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import main.Constant;
import main.Main;
import message.OfferMessage;
import task.SubTask;
import task.Task;
import agent.Agent;

/**
 * キューからはデッドラインの早いタスクから選択する
 * リーダの期待報酬は(全報酬/実行時間)×欲張り度
 * メンバの期待報酬は(サブタスク/実行時間)×報酬期待度
 */
public class InitialStateStrategy2 implements InitialRoleSelectStrategy {
	private static Random sort_random1 = new Random(1007);	//1007ソートのランダムインスタンス
	private static Random epsilon_greedy_random1 = new Random(101);	//101
	private static Random epsilon_greedy_random2 = new Random(103);	//103
	private static Random shuffle_random1 = new Random(107);	//107
	private static Random select_random1 = new Random(109);	//109
	private static Random select_random2 = new Random(113);	//113
	private static Random select_random3 = new Random(127);	//127
	
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
		double probability = epsilon_greedy_random1.nextDouble();
		Collections.shuffle(messages, shuffle_random1);
		
		/* εの確率でランダムに選ぶ */
		if(probability <= Constant.EPSILON){
			System.out.println("p < εでした");
			maxExpectedRewardMessage = messages.get(select_random1.nextInt(messages.size()));
		}
		
		/* 1-εの確率で報酬期待度の大きいエージェントを選ぶ */
		else{
			System.out.println("ε < pでした");
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
	 * リーダの期待報酬は(全報酬/実行時間)×欲張り度
	 * @param agent
	 * @param task
	 * @return
	 */
	@Override
	public double calculateExpectedLeaderReward(Agent agent, Task task){
//		double time = 
		double leader_reward = (double)task.getTaskRequireSum() / (double)task.getDeadlineInTask() * agent.getGreedy();
		return leader_reward;
	}
	
	/**
	 * メンバの期待報酬を計算する
	 * メンバの期待報酬は(サブタスク/実行時間)×報酬期待度
	 * @param agent
	 * @param message
	 * @return
	 */
	@Override
	public double calculateExpectedMemberReward(Agent agent, OfferMessage message){
		double time = calculateExecuteTime(agent, message.getSubTask());	//メッセージで送られてきたサブタスクを処理する実行時間
		double member_reward = (double)message.getSubTask().getRequireSum() / (time - 2) * agent.getExpectedReward(message.getFrom());
		return member_reward;
	}

	/**
	 * メンバ候補を探す
	 * @param agent
	 * @param task リーダが取り出したタスク
	 */
	@Override
	/*public void selectMemberCandidates(Agent agent, Task task) {
		Agent[] trustOrder = new Agent[agent.getTrust().length];	//ソートされた提案受託期待度のインデックスを保持する
		System.out.println("提案受託期待度でソート");
		trustOrder = getSortArrayOrder(agent.getTrust(), Main.getAgent());	//ソート後の提案受託期待度の添字
		List<Agent> selected_agents = new ArrayList<Agent>();	//すでにメッセージを送ったエージェントを記憶
		
		task.sortSubTaskListByRequire();	//タスクの各サブタスクのリソースを降順にソート
		System.out.println("\nソートされた後のサブタスク");
		for(SubTask subtask : task.getSubTaskList()){
			System.out.println(subtask);
		}
		
		 リーダが処理するサブタスクをリストから取り出す 
		pullLeaderExecuteSubTask(agent, task);
		
		selected_agents.add(agent);	//リーダを格納
		System.out.println("リーダが処理する分のサブタスクを抜き取った後のサブタスク");
		for(SubTask subtask : task.memberSubTaskList){
			System.out.println(subtask);
		}
		System.out.println("サブタスク数:" + task.memberSubTaskList.size() + "\n");
		
		 サブタスクごとにメンバ候補を選択 
		for(SubTask subtask : task.memberSubTaskList){
			boolean isOutOfArrayIndex = false;	//indexが配列の長さを超えているかどうか
			int selected = 0;
			int index = 0;
			Agent selected_member;	//メンバ候補
			
			while(selected < Constant.SELECT_MEMBER_NUM){
				System.out.println(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");
				double probability = epsilon_greedy_random2.nextDouble();	//ε-greedyの確率
				
				 εの確率でランダムにエージェントを選ぶ 
				if(probability <= Constant.epsilon){
					System.out.println("p < εでした");
					selected_member = Main.getAgent(select_random2.nextInt(Constant.AGENT_NUM));
				}
				
				 1-εの確率で提案受託期待度の高いエージェントを選ぶ 
				else{
					System.out.println("ε < pでした");
					if(index >= trustOrder.length){
						isOutOfArrayIndex = true;
					}
					
					if(isOutOfArrayIndex == false){
						selected_member = Main.getAgent(trustOrder[index++].getId());
					}
					else{
						selected_member = Main.getAgent(select_random3.nextInt(Constant.AGENT_NUM));
					}
				}
				
				//すでに選ばれているエージェント、もしくはサブタスクのリソースより小さいエージェントの場合は、最初から
				if(selected_agents.contains(selected_member) == true || isExecuteSubTask(selected_member, subtask, subtask.getDeadline()) == false || isOutOfArrayIndex == true){
					continue;
				}
				else{
					System.out.println(selected_member + " のエージェントを" + subtask + " のサブタスク処理のメンバ候補として選びました");
					selected_member.addOfferMessage(new OfferMessage(agent, subtask));	//メンバ候補に提案メッセージを送る
					selected_agents.add(selected_member);
					subtask.candidates.add(selected_member);	//サブタスクがメンバ候補を保持
					agent.addSendAgent(selected_member);
					System.out.println("サブタスクのcandidates: " + subtask.getMemberCandidate().get(selected));
					selected++;
				}
				
			}
			System.out.println();
		}
		System.out.println("メンバ候補選択を終了します");
	}*/
	
	/**
	 * メンバ候補を探す
	 * （メンバ候補がいなかったら、falseを返す）
	 * @param agent
	 * @param task
	 * @return
	 */
	public boolean searchMemberCandidates(Agent agent, Task task){
		Agent[] trustOrder = new Agent[agent.getTrust().length];	//ソートされた提案受託期待度のインデックスを保持する
		System.out.println("提案受託期待度でソート");
		trustOrder = getSortArrayOrder(agent.getTrust(), Main.getAgent());	//ソート後の提案受託期待度の添字
		List<Agent> selected_agents = new ArrayList<Agent>();	//すでにメッセージを送ったエージェントを記憶
		
		task.sortSubTaskListByRequire();	//タスクの各サブタスクのリソースを降順にソート
		System.out.println("\nソートされた後のサブタスク");
		for(SubTask subtask : task.getSubTaskList()){
			System.out.println(subtask);
		}
		
		/* リーダが処理するサブタスクをリストから取り出す */
		pullLeaderExecuteSubTask(agent, task);
		
		selected_agents.add(agent);	//リーダを格納
		System.out.println("リーダが処理する分のサブタスクを抜き取った後のサブタスク");
		for(SubTask subtask : task.subtasksByMembers){
			System.out.println(subtask);
		}
		System.out.println("サブタスク数:" + task.subtasksByMembers.size() + "\n");
		
		/* サブタスクごとにメンバ候補を選択 */
		for(SubTask subtask : task.subtasksByMembers){
			int selected = 0;
			Agent selected_member;	//メンバ候補
			List<Agent> canExecuteSubTaskAgents = executeSubTaskAgents(subtask, trustOrder);	//subtaskを処理できるエージェントのリスト
			
			while(selected < Constant.SELECT_MEMBER_NUM){
				System.out.println(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");
				double probability = epsilon_greedy_random2.nextDouble();	//ε-greedyの確率
				
				/* デッドラインまでに処理できるエージェントがいなければ、 */
				if(canExecuteSubTaskAgents.isEmpty() == true){
					System.out.println("デッドラインまでに処理できるエージェントが見つかりませんでした");
					return false;
				}
				
				/* εの確率でランダムにエージェントを選ぶ */
				if(probability <= Constant.EPSILON){
					System.out.println("p < εでした");
					selected_member = canExecuteSubTaskAgents.remove(select_random2.nextInt(canExecuteSubTaskAgents.size()));
				}
				/* 1-εの確率で提案受託期待度の高いエージェントを選ぶ */
				else{
					System.out.println("ε < pでした");
					
					selected_member = canExecuteSubTaskAgents.remove(0);
				}
				
				/* すでに選ばれているエージェントの場合は、最初から */
				if(selected_agents.contains(selected_member) == true){
					continue;
				}
				else{
					System.out.println(selected_member + " のエージェントを" + subtask + " のサブタスク処理のメンバ候補として選びました");
					selected_member.addOfferMessage(new OfferMessage(agent, subtask));	//メンバ候補に提案メッセージを送る
					selected_agents.add(selected_member);
					subtask.candidates.add(selected_member);	//サブタスクがメンバ候補を保持
					agent.addSendAgent(selected_member);
					System.out.println("サブタスクのcandidates: " + subtask.getMemberCandidate().get(selected));
					selected++;
				}
				
			}
			System.out.println();
		}
		System.out.println("メンバ候補選択を終了します");
		return true;
	}
	
	/**
	 * リーダが処理するサブタスクをリストから引き抜き、リーダ以外が処理するサブタスクリストはmemberSubTaskListに格納
	 * @param agent
	 * @param task リーダが取り出したタスク
	 */
	@Override
	public void pullLeaderExecuteSubTask(Agent agent, Task task){
		int leftDeadline = task.getDeadlineInTask();
		for(int i = 0; i < task.getSubTaskList().size(); i++){
			if(isExecuteSubTask(agent, task.getSubTaskList(i), leftDeadline) == true){
				//リーダの処理するサブタスクをexecuted_subtaskに代入
				agent.addExecutedSubTaskList(task.getSubTaskList(i));
				agent.subtractResource(task.getSubTaskList(i).getRequire());
				agent.addExecuteTime(calculateExecuteTime(agent, task.getSubTaskList(i)));
				leftDeadline -= calculateExecuteTime(agent, task.getSubTaskList(i));
			}
			else{
				task.subtasksByMembers.add(task.getSubTaskList(i));
			}
		}
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
		Collections.shuffle(entries, sort_random1);
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
			System.out.println("key:" + entry.getKey() + " / value:" + entry.getValue() + " / order = " + order);
			arrayOrder[order] = entry.getKey();
			order++;
		}
		return arrayOrder;
	}
	
	/**
	 * エージェントがデッドラインまでにサブタスクを処理できるかどうか
	 * @param agent
	 * @param subtask
	 * @param deadline 
	 * @return
	 */
	public boolean isExecuteSubTask(Agent agent, SubTask subtask, int deadline){
		if(calculateExecuteTime(agent, subtask) <= deadline){
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
		for(Agent agent : agents){
			if(isExecuteSubTask(agent, subtask, subtask.getDeadline()) == true){
				executeAgents.add(agent);
			}
		}
		return executeAgents;
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
			if(subtask.getRequire()[i] % agent.getAbility()[i] == 0){
				time[i] = subtask.getRequire()[i] / agent.getAbility()[i] + 2;
			}
			else{
				time[i] = subtask.getRequire()[i] / agent.getAbility()[i] + 1 + 2;
			}
			
			if(executeTime < time[i]){
				executeTime = time[i];
			}
		}
		return executeTime;
	}

}
