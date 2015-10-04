package initialRoleSelect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Administrator;
import main.Constant;
import main.Main;
import message.OfferMessage;
import task.SubTask;
import task.Task;
import agent.Agent;

/**
 * 完全にランダムにタスクを割り当てる
 * エージェントのリソースを未知として
 * 1tickごとの期待報酬
 * リーダの期待報酬 = (タスクの全報酬 / タスク処理想定時間) × 欲張り度
 * タスク処理想定時間 = タスクのリソース和 / チーム履歴の平均リソース
 * メンバの期待報酬 = (サブタスクのリソース / タスク処理想定時間) × 報酬期待度
 */
public class RandomRoleSelectStrategy2 implements InitialRoleSelectStrategy {

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
	 * @param messages 受信した提案メッセージ
	 * @param agent
	 * @return
	 */
	@Override
	public OfferMessage selectMessage(List<OfferMessage> messages, Agent agent) {
		Collections.shuffle(messages, Administrator.shuffle_random1);
		OfferMessage message = messages.get(Administrator.select_random1.nextInt(messages.size()));
		return message;
	}

	/**
	 * リーダの期待報酬を計算する
	 * リーダの期待報酬は全報酬×欲張り度
	 * @param agent
	 * @param task
	 * @return
	 */
	@Override
	public double calculateExpectedLeaderReward(Agent agent, Task task) {
		double leaderReward;	//リーダの期待報酬
		int expectedExecuteTime;	//予想されるタスク実行時間
		
		/* チーム履歴を持っている場合 */
		if(!agent.getPastTeams().isEmpty()){
			expectedExecuteTime = (int)Math.ceil(task.getTaskRequireSum() / agent.getAverageTeamResource());
			
			leaderReward = ((double)task.getTaskRequireSum() / (double)expectedExecuteTime) * agent.getGreedy();	//リーダ時の期待報酬の計算
		}
		/* チーム履歴を持っていない場合は、全エージェントのリソースの平均 × サブタスク数をチームリソースとする */
		else{
//			double expectedTeamResources = (Constant.AGENT_ABILITY_INIT + Constant.AGENT_ABILITY_INIT + Constant.AGENT_ABILITY_MAX - 1) / 2 * Constant.RESOURCE_NUM * task.getSubTaskNum();
//			double expectedTeamResources = 0.0;
//			for(int i = 0; i < Constant.RESOURCE_NUM; i++){
//				expectedTeamResources += Main.getAgentAbilityAverage()[i];
//			}
//			expectedTeamResources *= task.getSubTaskNum();
//			
//			expectedExecuteTime = (int)Math.ceil(task.getTaskRequireSum() / expectedTeamResources);
			
			leaderReward = -1;	//履歴なし
		}
		
		return leaderReward;
	}

	/**
	 * メンバの期待報酬を計算する
	 * メンバの期待報酬はサブタスク×リーダの報酬期待度
	 * @param agent
	 * @param message
	 * @return
	 */
	@Override
	public double calculateExpectedMemberReward(Agent agent, OfferMessage message) {
		int expectedExecuteTime = calculateExecuteTime(agent, message.getSubTask());
		double memberReward = ((double)message.getSubTask().getRequireSum() / (double)expectedExecuteTime) * agent.getExpectedReward(message.getFrom());
		return memberReward;
	}

	/**
	 * メンバ候補を探す
	 * （メンバ候補がいなかったら、falseを返す）
	 * @param agent
	 * @param task
	 * @return
	 */
	@Override
	public boolean searchMemberCandidates(Agent agent, Task task) {
		
		List<Agent> selected_agents = new ArrayList<Agent>();	//すでにメッセージを送ったエージェントを記憶
		task.sortSubTaskListByRequire();	//タスクの各サブタスクのリソースを降順にソート
		
		/* リーダが処理するサブタスクをリストから取り出す */
		pullLeaderExecuteSubTask(agent, task);
		selected_agents.add(agent);	//リーダを格納
		
		List<Agent> canExecuteSubTaskAgents = new ArrayList<Agent>(Main.getAgent());
		Collections.shuffle(canExecuteSubTaskAgents, Administrator.shuffle_random2);	//エージェントリストをシャッフル
		
		/* サブタスクごとにメンバ候補を選択 */
		for(SubTask subtask : task.subtasksByMembers){
			int selected = 0;
			Agent selected_member;	//メンバ候補
			
			while(selected < Constant.SELECT_MEMBER_NUM){
//				System.out.println(subtask + " のサブタスク　" + (selected + 1) + "回目:メンバ候補を選択します");
				
				/* エージェントがいなければチーム編成実施を断念 */
				if(canExecuteSubTaskAgents.isEmpty() == true){
//					System.out.println("デッドラインまでに処理できるエージェントが見つかりませんでした");
					return false;
				}
				
				/* メンバ候補をランダムで選ぶ */
				selected_member = canExecuteSubTaskAgents.remove(Administrator.select_random2.nextInt(canExecuteSubTaskAgents.size()));
				
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
	public void pullLeaderExecuteSubTask(Agent agent, Task task) {
		int leftDeadline = task.getDeadlineInTask() - 2 * Constant.WAIT_TURN;
		for(int i = 0; i < task.getSubTaskList().size(); i++){
			if(isExecuteSubTask(agent, task.getSubTaskList(i), leftDeadline) == true){
				//リーダの処理するサブタスクをexecuted_subtaskに代入
				agent.addExecutedSubTaskList(task.getSubTaskList(i));
				agent.addExecuteTime(calculateExecuteTime(agent, task.getSubTaskList(i)));
				leftDeadline -= calculateExecuteTime(agent, task.getSubTaskList(i));
			}
			else{
				task.subtasksByMembers.add(task.getSubTaskList(i));
			}
		}

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
//		System.out.println("エージェント" + agent + "がサブタスク" + subtask + "の処理にかかる時間 = " + executeTime);
		return executeTime;
	}

	/**
	 * 初期役割選択状態での戦略を返す
	 */
	public String toString(){
		return "ランダムな戦略（1tickごとの報酬期待度で学習）";
	}
}
