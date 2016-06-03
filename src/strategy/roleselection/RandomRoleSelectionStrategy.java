package strategy.roleselection;

import java.util.ArrayList;

import random.RandomKey;
import random.RandomManager;
import task.Task;
import library.AgentTaskLibrary;
import log.Log;
import message.OfferMessage;
import constant.Constant;
import agent.ConcreteAgent;

public class RandomRoleSelectionStrategy implements RoleSelectionStrategy {
	
	@Override
	public OfferMessage selectMessage(
			ArrayList<OfferMessage> messages, ConcreteAgent agent) {
		OfferMessage maxExpectedRewardMessage;
		// ランダムに選ぶ
		maxExpectedRewardMessage = messages.get(RandomManager.getRandom(RandomKey.SELECT_RANDOM_1).nextInt(messages.size()));
		Log.log.debugln(maxExpectedRewardMessage + " をランダムで選びました");

		return maxExpectedRewardMessage;
	}

	@Override
	public double calculateExpectedLeaderReward(ConcreteAgent agent, Task task) {
		double leaderReward;	//リーダの期待報酬
		
		if(task == null){
			leaderReward = 0;
		}
		else{
			// taskがあるときは、リーダになるか、ランダムで決めるか
			leaderReward = Constant.NO_PAST_TEAMS;	//履歴なし
		}

		return leaderReward;
	}

	@Override
	public double calculateExpectedMemberReward(ConcreteAgent agent,
			ArrayList<OfferMessage> messages) {
		// 来ている提案メッセージから処理できるメッセージを抽出
		ArrayList<OfferMessage> canBeExecutedMessages = AgentTaskLibrary.getCanExecuteOfferMessages(messages, agent);
		Log.log.debugln("来ている提案メッセージの中で処理できるメッセージ");
		debugOfferMessage(canBeExecutedMessages);

		double memberReward;

		if(canBeExecutedMessages.isEmpty()){
			Log.log.debugln("処理できるメッセージは来ませんでした");
			memberReward = 0;
		}
		else{
			OfferMessage selectedMessage = selectMessage(canBeExecutedMessages, agent);
			agent.getParameter().setSelectedOfferMessage(selectedMessage);
			memberReward = getExpectedMemberReward(agent, selectedMessage);
		}
		return memberReward;
	}
	
	private double getExpectedMemberReward(ConcreteAgent agent, OfferMessage message) {
		int expectedExecuteTime = AgentTaskLibrary.calculateExecuteTime(agent, message.getSubtask());
		double memberReward = (double)expectedExecuteTime * agent.getRewardExpectation(message.getFrom());
		
		return memberReward;
	}
	
	private void debugOfferMessage(ArrayList<OfferMessage> messages) {
		for(OfferMessage message : messages){
			Log.log.debugln(message);
		}
	}
	
	public String toString() {
		return "（リーダにもメンバにもなれるときは）ランダムに役割を選ぶ";
	}

}
