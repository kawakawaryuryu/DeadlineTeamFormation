package state;

import java.util.List;

import main.Administrator;
import main.Constant;
import main.Main;
import message.AnswerMessage;
import message.OfferMessage;
import agent.Agent;
import team.Team;
import initialRoleSelect.InitialRoleSelectStrategy;
import initialRoleSelect.RandomRoleSelectStrategy1;
import initialRoleSelect.RandomRoleSelectStrategy2;
import initialRoleSelect.RandomRoleSelectStrategy3;
import initialRoleSelect.RandomRoleSelectStrategy4;
import initialRoleSelect.RoleSelectStrategy1;
import initialRoleSelect.RoleSelectStrategy2;
import initialRoleSelect.RoleSelectStrategy3;

/**
 * 初期役割選択状態
 */
public class InitialRoleSelectState implements State {

	private static State onlyState = new InitialRoleSelectState();	//状態を保持する（singletonパターン）
//	private InitialRoleSelectStrategy strategy = new RoleSelectStrategy1();	//全報酬を学習する戦略
//	private InitialRoleSelectStrategy strategy = new RoleSelectStrategy2();	//1tickごとの報酬を学習する戦略
	private InitialRoleSelectStrategy strategy = new RoleSelectStrategy3();	//リーダには1個しかサブタスクを割り当てない+1tickごとの獲得報酬で学習した報酬期待度を用いる
//	private InitialRoleSelectStrategy strategy = new RandomRoleSelectStrategy1(); //すべてランダムに行動する戦略
//	private InitialRoleSelectStrategy strategy = new RandomRoleSelectStrategy2();	//ランダムな戦略（1tickごとの報酬期待度で学習）
//	private InitialRoleSelectStrategy strategy = new RandomRoleSelectStrategy3();	//ランダムな戦略（1tickごとの報酬期待度で学習＋チーム履歴なし）
//	private InitialRoleSelectStrategy strategy = new RandomRoleSelectStrategy4();	//ランダムな戦略（1tickごとの獲得報酬で学習した報酬期待度を用いる＋リーダには1個しかサブタスクを割り当てない）
	private double expectedLeaderReward;	//リーダ時の期待報酬
	private double expectedMemberReward;	//メンバ時の期待報酬	
	private static final int LEADER = 0;	//リーダ
	private static final int MEMBER = 1;	//メンバ
	private static final int NOROLE = 2;	//どちらでもない
	private int role = -1;	//役割を保持

	/**
	 * エージェントの行動
	 * リーダになるかメンバになるかを決める
	 * リーダ→メンバ候補に参加提案メッセージを送る
	 * メンバ→参加提案メッセージを一つ選び、返答メッセージを送る
	 */
	@Override
	public void agentAction(Agent agent){
		
		/* リーダの期待報酬の計算 */
//		System.out.println("リーダの期待報酬を計算します");
		
		/* 参照するタスクがない場合 */
		if(agent.getMarkingTask() == null){
			expectedLeaderReward = 0.0;			
		}
		
		else{
			expectedLeaderReward = strategy.calculateExpectedLeaderReward(agent, agent.getMarkingTask());	//リーダ時の期待報酬の計算
		}
//		System.out.println("リーダの期待報酬 = " + expected_leader_reward);
		
		/* メンバの期待報酬の計算 */
		OfferMessage msg = null;	//参加するチームのリーダから送られてきたチーム参加提案メッセージ
//		System.out.println("メンバの期待報酬を計算します");
		
		List<OfferMessage> messages = strategy.getCanExecuteMessages(agent.getOfferMessage(), agent);	//来ているチーム参加提案メッセージを抽出
		if(messages.isEmpty()){
			expectedMemberReward = 0.0;
		}
		else{
			msg = strategy.selectMessage(messages, agent);	//メッセージを選択
			agent.haveSelectedOfferMessage(msg);	//メッセージを保持させる
//			System.out.println(msg.getFrom() + " からのメッセージを選びました");
			expectedMemberReward = strategy.calculateExpectedMemberReward(agent, msg);	//メンバ時期待報酬の計算
		}
//		System.out.println("メンバの期待報酬 = " + expected_member_reward);
		
		/* チーム履歴がない場合 */
		if(expectedLeaderReward == -1.0){
//			System.out.println("履歴がない");
			
			//リーダかメンバになるかランダムで選ぶ
			if(expectedMemberReward > 0){
				int value = Administrator.select_random5.nextInt(2);	//リーダになるかメンバになるか
				if(value == 0){
//					System.out.println("履歴なし、リーダになる");
					role = LEADER;
				}
				else{
					role = MEMBER;
				}
			}
			//リーダになる
			else{
				role = LEADER;
			}
		}
		
		//リーダ時とメンバ時の期待報酬が0のとき
		else if(expectedLeaderReward == 0 && expectedMemberReward == 0){
			role = NOROLE;
		}
		//リーダの期待報酬 > メンバの期待報酬
		else if(expectedLeaderReward >= expectedMemberReward){
			role = LEADER;
		}
		//リーダの期待報酬 < メンバの期待報酬
		else if(expectedLeaderReward < expectedMemberReward){
			role = MEMBER;
		}
			
//		System.out.println("リーダとメンバの期待報酬を比較します");
		/* リーダ時の期待報酬とメンバ時の期待報酬の比較 */
		/* リーダにもメンバにもならない場合 */
		if(role == NOROLE){
			agent.changeState(InitialMarkingTaskState.getInstance());
		}
		
		/* リーダになる場合 */
		else if(role == LEADER){
//			System.out.println("リーダになります");
			/* 自分に来たメッセージを全て断る */
			for(OfferMessage offer : agent.getOfferMessage()){
//				System.out.println(offer.getFrom() + "からのメッセージを断ります");
				offer.getFrom().addAnswerMessage(new AnswerMessage(agent, false, offer.getSubTask()));
			}
			
			/* メンバ候補を探す */
			boolean isCandidates = strategy.searchMemberCandidates(agent, agent.getMarkingTask());	//メンバ候補を選んで提案メッセージを送る	
			
			/* メンバ候補を探せた場合 */
			if(isCandidates == true){
				agent.haveTeamInfo(new Team(agent));	//チーム情報を保持
				agent.haveRole(Constant.LEADER_STATE);	//リーダの役割を保持
				agent.changeState(InitialWaitState.getInstance());	//待機状態に移行
			}
			/* メンバ候補を探せなかった場合 */
			else{
//				System.out.println("メンバ候補を探せなかったので、チーム編成を中断します");
//				System.exit(0);
				agent.getMarkingTask().clear();
				agent.getMarkingTask().markingTask(false);	//タスクのマークを外す（マークあり）
//				Main.addFailureResource(agent.getMarkingTask().getTaskRequireSum());	//タスクを廃棄する（マークなし）
//				Main.getTask(selected_task);	//マークしたタスクを廃棄する（マークなし）
				Main.addGiveUpTeaming();	//チーム編成実施断念回数を数える
				
				agent.haveRole(Constant.INITIAL_STATE);
				agent.changeState(InitialWaitState.getInstance());	//初期状態に移行
			}
		}
		
		/* メンバになる場合 */
		else if(role == MEMBER){
//			System.out.println("メンバになります");
			for(OfferMessage offer : agent.getOfferMessage()){
				if(offer != msg){
//					System.out.println(offer.getFrom() + "からのメッセージを断ります");
					offer.getFrom().addAnswerMessage(new AnswerMessage(agent, false, offer.getSubTask()));	//チームに参加しないという返答メッセージを送る
				}
				else{
//					System.out.println(offer.getFrom() + " がリーダであるチームに参加します");
					offer.getFrom().addAnswerMessage(new AnswerMessage(agent, true, offer.getSubTask()));	//チームに参加するという返答メッセージを送る
				}
			}
			
			//マークしているタスクがある場合
			if(agent.getMarkingTask() != null){
//				System.out.println("マークしていたタスクのマークを外します");
				agent.getMarkingTask().markingTask(false);	//マークしていたタスクのマークを外す
			}
			
			agent.haveRole(Constant.MEMBER_STATE);	//メンバの役割を保持
			agent.changeState(InitialWaitState.getInstance());	//初期待機状態に移行
		}
		
		agent.addInitialTimer();
		agent.addInitialStateTime();
	}
	
	/**
	 * 状態インスタンスを返す
	 * @return
	 */
	public static State getInstance(){
		return onlyState;
	}
	
	/**
	 * 初期役割選択状態の戦略を返す
	 * @return
	 */
	public InitialRoleSelectStrategy getRoleSelectStrategy(){
		return strategy;
	}
	
	/**
	 * 状態を表す
	 */
	public String toString(){
		return "初期役割選択状態";
	}
}
