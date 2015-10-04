package state;

import java.io.IOException;

import leader.LeaderStateStrategy3;
import leader.LeaderStrategy;
import leader.LeaderStateStrategy1;
import leader.RandomLeaderStrategy1;
import leader.RandomLeaderStrategy2;
import main.Constant;
import main.FileWriter;
import main.Main;
import main.MeasureConstraintTimeData;
import main.MeasureDataPerTime;
import message.AnswerMessage;
import message.TeamingMessage;
import agent.Agent;

/**
 * リーダ提案送信状態
 */
public class LeaderState implements State {

	private static State onlyState = new LeaderState();	//状態を保持する（singletonパターン）
	private LeaderStrategy strategy = new LeaderStateStrategy1();	//信頼度の大きいメンバ順に再割り当てを行う
//	private LeaderStrategy strategy = new LeaderStateStrategy2();
//	private LeaderStrategy strategy = new LeaderStateStrategy3();	//リーダとメンバの中から実行時間の短い順に再割り当て
//	private LeaderStrategy strategy = new RandomLeaderStrategy1();	//ランダムな戦略（再割り当てなし）
//	private LeaderStrategy strategy = new RandomLeaderStrategy2();	//ランダムな戦略（ランダムに再割り当てを行う）
	
	
	/**
	 * エージェントの行動
	 * 返答メッセージを見て、チーム編成の成功or失敗を判断する
	 * @param leader
	 */
	@Override
	public void agentAction(Agent leader){
		leader.getLeaderFields().initialize();	//初期化
		leader.getLeaderFields().messages = leader.getAnswerMessage();	//返答メッセージの取得
		leader.getLeaderFields().trueAgentAndLeaderList.add(leader);
		
		/* 返答メッセージの中からチーム参加OKとNGのメッセージを分ける */
		for(AnswerMessage message : leader.getLeaderFields().messages){
			if(message.getIsOk() == true){
				leader.getLeaderFields().trueAgentList.add(message.getFrom());
				leader.getLeaderFields().trueAgentAndLeaderList.add(message.getFrom());
//				System.out.println(message.getFrom() + "　からの OK のメッセージ");
			}
			else{
				leader.getLeaderFields().falseAgentList.add(message.getFrom());
//				System.out.println(message.getFrom() + "　からの NG のメッセージ");
			}
		}
		
		/* 提案メッセージを送ったが、返答メッセージが返ってきていないエージェントをfalseAgantListに追加 */
		for(Agent agent : leader.getSendAgents()){
			if(leader.getLeaderFields().trueAgentList.contains(agent) == false && leader.getLeaderFields().falseAgentList.contains(agent) == false){
				leader.getLeaderFields().falseAgentList.add(agent);
//				System.out.println(agent + " からはメッセージが返ってきませんでした");
			}
		}
		
//		System.out.println("\nチーム参加をOKしてくれたエージェント（仮チーム）");
//		for(Agent entry : leader.getLeaderFields().trueAgentList){
//			System.out.println("Agent: " + entry);
//		}
//		System.out.println("チーム参加を断ったエージェント");
//		for(Agent entry : leader.getLeaderFields().falseAgentList){
//			System.out.println("Agent: " + entry);
//		}
		
		/* 仮チームのメンバにサブタスクを割り振る */
		strategy.decideMember(leader);
		
		/* リーダ以外に与えられる残りの報酬と残りのタスクリソース量を計算する */
		double leftReward = (double)leader.getMarkingTask().getTaskRequireSum() * (1.0d - leader.getGreedy());	//リーダ以外に与えられる報酬
		int leftRequireSum = strategy.calculateLeftRequireSum(leader);	//リーダ以外が処理するサブタスクのリソースの合計を計算
		
		/* チーム編成に成功したかどうかのメッセージを送る */
		for(AnswerMessage message : leader.getLeaderFields().messages){
			if(message.getIsOk() == true){
				/* チームに参加してもらわないエージェントに対するメッセージ */
				if(leader.getLeaderFields().memberList.contains(message.getFrom()) == false || leader.getLeaderFields().isTeaming == false){
//					System.out.println(message.getFrom() + " のエージェントにチーム編成失敗メッセージを送りました");
					message.getFrom().addTeamingMessage(new TeamingMessage(leader, false, message.getFrom().willBeExecutedSubTaskList, 0.0, 1, null));
				}
				/* チームに参加してもらうエージェントに対するメッセージ */
				else{
//					System.out.println(message.getFrom() + " のエージェントにチーム編成成功メッセージを送りました");
					message.getFrom().addTeamingMessage(new TeamingMessage(leader, true, message.getFrom().willBeExecutedSubTaskList, leftReward, leftRequireSum, leader.getTeamInfo()));
				}
				
			}
		}
		
		/* 欲張り度フィードバック */
		leader.feedbackGreedy(leader.getLeaderFields().isTeaming);
		
		/* 提案受託期待度フィードバック */
		for(Agent agent : leader.getLeaderFields().trueAgentList){
			leader.feedbackTrust(agent, true);
		}
		for(Agent agent : leader.getLeaderFields().falseAgentList){
			leader.feedbackTrust(agent, false);
//			System.out.println("提案受託期待度を下げました");
//			System.out.println("エージェントの状態：" + agent.getNowState());
		}
		
		/* チーム編成に成功した場合 */
		if(leader.getLeaderFields().isTeaming == true){
			Main.getTask(leader.getMarkingTask());	//キューからタスクを取り出す（マークあり）
			Main.addSuccess(leader.getMarkingTask().getTaskRequireSum());	//タスク処理リソース量を数える
			if(leader.getLeaderFields().isTeamingAgainAllocation){
				Main.addSuccessAgainAllocation();	//再割り当てを行ってチーム編成が成功した回数をカウント
			}
			Main.addSuccessTeamingEdgeToAgent(leader.getTeamInfo().getMembers().size());	//各メンバとのエッジをカウント
			
			leader.addLeaderNum();	//リーダの回数を増やす
			leader.addTeamingWithMemberNum(leader.getTeamInfo().getMembers());	//メンバとのチーム編成回数を増やす
			
			leader.addTeamingSuccessNum();
			leader.addConstraintTimeSumPerAgent(leader.getTeamInfo().getExecuteTime() - leader.getExecuteTime());
			
			Main.addTemporaryTeamSize(leader.getTeamInfo().getTemporaryTeamMate().size());	//仮チームのサイズを数える
			Main.addTeamSize(leader.getTeamInfo().getSize());	//チームのサイズを数える
			Main.addTeamExecuteTime(leader.getTeamInfo().getExecuteTime());	//チームでのタスク処理時間を数える
			
			leader.getTeamInfo().calculateExecutingTimePerAgentAverage();	//チーム内の1人あたりの平均拘束時間を求める
			leader.getTeamInfo().calculateExecuteTimeDifference();	//チーム内のエージェントの拘束時間の差分を求める
			MeasureConstraintTimeData.measureTeamConstraintTimeDifference(leader.getTeamInfo());	//チーム拘束時間の差分を計測
			Main.addConstraintTimeInATeam(leader.getTeamInfo().getExecuteTimeDifference());	//チーム拘束時間を数える
			if(leader.getTeamInfo().getSize() != 1){
				Main.addConstraintTimePerAgentInATeam(leader.getTeamInfo().getExecuteTimeDifferencePerAgent());	//1人あたりのチーム拘束時間を数える
			}
//			System.out.println("チームサイズ = " + leader.getTeamInfo().getSize());
			
			if(leader.getTurn() > Constant.TURN_NUM - Constant.MEASURE_SUCCESS_AT_END_TURN_NUM){
				leader.addTeamingSuccessAtEnd();	//最後の数ターンのチーム編成成功回数を増やす
				leader.addExecuteStateExecutingTimeAtEnd(leader.getExecuteTime());	//処理時間をカウント
				leader.addExecuteStateConstraintTimeAtEnd(leader.getTeamInfo().getExecuteTime() - leader.getExecuteTime());	//無駄に拘束されている時間をカウント
				
				Main.addSuccessTeamingCountAtEnd();
				Main.addExecutingTimePerAgentInATeamAtEnd(leader.getTeamInfo().getExecutingTimePerAgentAverage());	//最後の数ターンの1人あたりの処理時間をカウント
				Main.addConstraintTimePerAgentInATeamAtEnd(leader.getTeamInfo().getExecuteTimeDifferencePerAgent());	//最後の数ターンの1人あたりの拘束時間をカウント
			}
			
			try{
				//ヒストグラム用のチームに関するデータを書き込み
				FileWriter.teamNumHistogramWrite(Main.teamNumHistogramWriter, leader.getTeamInfo(), leader.getMarkingTask());
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		
		/* チーム編成に失敗した場合 */
		else{
			leader.getMarkingTask().clear();
			leader.getMarkingTask().markingTask(false);	//マークしていたタスクからマークを外す（マークあり）
//			Main.addFailureResource(leader.getMarkingTask().getTaskRequireSum());	//タスクを廃棄する（マークなし）
//			Main.getTask(leader.getMarkingTask());	//マークしていたタスクを廃棄する（マークなし）
			Main.addFailureTeaming();	//チーム編成失敗回数を数える
			
		}
		leader.changeState(LeaderWaitState.getInstance());	//リーダ待機状態に移行
		
		Main.addTeaming();	//チーム編成回数を数える
		leader.addLeaderTimer();
		leader.addLeaderStateTime();
	}
	
	/**
	 * 状態インスタンスを返す
	 * @return
	 */
	public static State getInstance(){
		return onlyState;
	}
	
	/**
	 * リーダ状態の戦略を返す
	 * @return
	 */
	public LeaderStrategy getLeaderStrategy(){
		return strategy;
	}
	
	/**
	 * 状態を表す
	 */
	public String toString(){
		return "リーダ提案送信状態";
	}

}
