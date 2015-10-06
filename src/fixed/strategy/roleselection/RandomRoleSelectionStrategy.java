package fixed.strategy.roleselection;

import java.util.ArrayList;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.library.AgentTaskLibrary;
import fixed.main.RandomKey;
import fixed.main.RandomManager;
import fixed.message.FixedOfferMessage;
import fixed.task.FixedTask;

public class RandomRoleSelectionStrategy implements FixedRoleSelectionStrategy {

	@Override
	public FixedOfferMessage getOfferMessageOfMaxExpectedMemberReward(
			ArrayList<FixedOfferMessage> messages, FixedAgent agent) {
		return null;
	}

	@Override
	public FixedOfferMessage selectMessage(
			ArrayList<FixedOfferMessage> messages, FixedAgent agent) {
		FixedOfferMessage maxExpectedRewardMessage;
		// ランダムに選ぶ
		maxExpectedRewardMessage = messages.get(RandomManager.getRandom(RandomKey.SELECT_RANDOM_1).nextInt(messages.size()));
//		System.out.println(maxExpectedRewardMessage + " をランダムで選びました");

		return maxExpectedRewardMessage;
	}

	@Override
	public double calculateExpectedLeaderReward(FixedAgent agent, FixedTask task) {
		double leaderReward;	//リーダの期待報酬
		
		if(task == null){
			leaderReward = 0;
		}
		else{
			// taskがあるときは、リーダになるか、ランダムで決めるか
			leaderReward = FixedConstant.NO_PAST_TEAMS;	//履歴なし
		}

		return leaderReward;
	}

	@Override
	public double calculateExpectedMemberReward(FixedAgent agent,
			ArrayList<FixedOfferMessage> messages) {
		// 来ている提案メッセージから処理できるメッセージを抽出
		ArrayList<FixedOfferMessage> canBeExecutedMessages = AgentTaskLibrary.getCanExecuteOfferMessages(messages, agent);
//		System.out.println("来ている提案メッセージの中で処理できるメッセージ");
//		debugOfferMessage(canBeExecutedMessages);

		double memberReward;

		if(canBeExecutedMessages.isEmpty()){
//			System.out.println("処理できるメッセージは来ませんでした");
			memberReward = 0;
		}
		else{
			FixedOfferMessage selectedMessage = selectMessage(canBeExecutedMessages, agent);
			agent.getParameter().setSelectedOfferMessage(selectedMessage);
			memberReward = getExpectedMemberReward(agent, selectedMessage);
		}
		return memberReward;
	}
	
	private double getExpectedMemberReward(FixedAgent agent, FixedOfferMessage message) {
		int expectedExecuteTime = AgentTaskLibrary.calculateExecuteTime(agent, message.getSubtask());
		double memberReward = (double)expectedExecuteTime * agent.getRewardExpectation(message.getFrom());
		
		return memberReward;
	}

}
