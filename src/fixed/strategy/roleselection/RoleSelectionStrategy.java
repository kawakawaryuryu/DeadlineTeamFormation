package fixed.strategy.roleselection;

import java.util.ArrayList;
import java.util.Collections;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.library.AgentTaskLibrary;
import fixed.main.RandomKey;
import fixed.main.RandomManager;
import fixed.message.FixedOfferMessage;
import fixed.task.FixedTask;

public class RoleSelectionStrategy implements FixedRoleSelectionStrategy {
	
	@Override
	public FixedOfferMessage getOfferMessageOfMaxExpectedMemberReward(ArrayList<FixedOfferMessage> messages, FixedAgent agent) {
		FixedOfferMessage maxExpectedRewardMessage = messages.get(0);
		for(int i = 1; i < messages.size(); i++){
			FixedOfferMessage message = messages.get(i);
			double expectedReward = getExpectedMemberReward(agent, message);
			double maxExpectedReward = getExpectedMemberReward(agent, maxExpectedRewardMessage);
			
			if(expectedReward > maxExpectedReward){
				maxExpectedRewardMessage = message;
			}
		}
		
		return maxExpectedRewardMessage;
	}

	@Override
	public FixedOfferMessage selectMessage(ArrayList<FixedOfferMessage> messages,
			FixedAgent agent) {
		FixedOfferMessage maxExpectedRewardMessage;	//期待報酬が最大のメッセージ（選ばれたメッセージ）
		double probability = RandomManager.getRandom(RandomKey.EPSILON_GREEDY_RANDOM_1).nextDouble();
		Collections.shuffle(messages, RandomManager.getRandom(RandomKey.SHUFFLE_RANDOM_1));
		
		// εの確率でランダムに選ぶ
		if(probability <= FixedConstant.EPSILON){
			maxExpectedRewardMessage = messages.get(RandomManager.getRandom(RandomKey.SELECT_RANDOM_1).nextInt(messages.size()));
			System.out.println(maxExpectedRewardMessage + " をランダムで選びました");
		}
		// 1-εの確率で期待報酬の大きいエージェントを選ぶ
		else{
			maxExpectedRewardMessage = getOfferMessageOfMaxExpectedMemberReward(messages, agent);
			System.out.println(maxExpectedRewardMessage + " を報酬期待度順で選びました");
		}
		return maxExpectedRewardMessage;
	}

	@Override
	public double calculateExpectedLeaderReward(FixedAgent agent, FixedTask task) {
		double leaderReward;	//リーダの期待報酬
		int expectedExecuteTime;	//予想されるタスク実行時間
		
		if(task == null){
			leaderReward = 0;
		}
		else{
			// チーム履歴を持っている場合
			if(!agent.getParameter().getPastTeam().isEmptyPastTeams()){
				expectedExecuteTime = (int)Math.ceil((double)task.getTaskRequireSum() / agent.getParameter().getPastTeam().getAverageAbilitiesPerTeam());
				leaderReward = ((double)task.getTaskRequireSum() / (double)expectedExecuteTime) * agent.getGreedy();	//リーダ時の期待報酬の計算
			}
			// チーム履歴を持っていない場合はランダム
			else{
				leaderReward = FixedConstant.NO_PAST_TEAMS;	//履歴なし
			}
		}
		
		return leaderReward;
	}

	@Override
	public double calculateExpectedMemberReward(FixedAgent agent,
			ArrayList<FixedOfferMessage> messages) {
		
		// 来ている提案メッセージから処理できるメッセージを抽出
		ArrayList<FixedOfferMessage> canBeExecutedMessages = AgentTaskLibrary.getCanExecuteOfferMessages(agent.getParameter().getOfferMessages(), agent);
		System.out.println("来ている提案メッセージの中で処理できるメッセージ");
		debugOfferMessage(canBeExecutedMessages);
		
		double memberReward;
		
		if(canBeExecutedMessages.isEmpty()){
			System.out.println("処理できるメッセージは来ませんでした");
			memberReward = 0;
		}
		else{
			FixedOfferMessage selectedMessage = selectMessage(canBeExecutedMessages, agent);
			agent.getParameter().setSelectedOfferMessage(selectedMessage);
			memberReward = getExpectedMemberReward(agent, selectedMessage);
		}
		return memberReward;
	}
	
	private void debugOfferMessage(ArrayList<FixedOfferMessage> messages) {
		for(FixedOfferMessage message : messages){
			System.out.println(message);
		}
	}
	
	private double getExpectedMemberReward(FixedAgent agent, FixedOfferMessage message) {
		int expectedExecuteTime = AgentTaskLibrary.calculateExecuteTime(agent, message.getSubtask());
		double memberReward = (double)expectedExecuteTime * agent.getRewardExpectation(message.getFrom());
		
		return memberReward;
	}

}
