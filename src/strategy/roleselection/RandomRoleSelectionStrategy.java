package strategy.roleselection;

import java.util.ArrayList;

import task.Task;
import library.AgentTaskLibrary;
import main.RandomKey;
import main.RandomManager;
import message.OfferMessage;
import constant.Constant;
import agent.Agent;

public class RandomRoleSelectionStrategy implements RoleSelectionStrategy {
	
	@Override
	public OfferMessage selectMessage(
			ArrayList<OfferMessage> messages, Agent agent) {
		OfferMessage maxExpectedRewardMessage;
		// ランダムに選ぶ
		maxExpectedRewardMessage = messages.get(RandomManager.getRandom(RandomKey.SELECT_RANDOM_1).nextInt(messages.size()));
//		System.out.println(maxExpectedRewardMessage + " をランダムで選びました");

		return maxExpectedRewardMessage;
	}

	@Override
	public double calculateExpectedLeaderReward(Agent agent, Task task) {
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
	public double calculateExpectedMemberReward(Agent agent,
			ArrayList<OfferMessage> messages) {
		// 来ている提案メッセージから処理できるメッセージを抽出
		ArrayList<OfferMessage> canBeExecutedMessages = AgentTaskLibrary.getCanExecuteOfferMessages(messages, agent);
//		System.out.println("来ている提案メッセージの中で処理できるメッセージ");
//		debugOfferMessage(canBeExecutedMessages);

		double memberReward;

		if(canBeExecutedMessages.isEmpty()){
//			System.out.println("処理できるメッセージは来ませんでした");
			memberReward = 0;
		}
		else{
			OfferMessage selectedMessage = selectMessage(canBeExecutedMessages, agent);
			agent.getParameter().setSelectedOfferMessage(selectedMessage);
			memberReward = getExpectedMemberReward(agent, selectedMessage);
		}
		return memberReward;
	}
	
	private double getExpectedMemberReward(Agent agent, OfferMessage message) {
		int expectedExecuteTime = AgentTaskLibrary.calculateExecuteTime(agent, message.getSubtask());
		double memberReward = (double)expectedExecuteTime * agent.getRewardExpectation(message.getFrom());
		
		return memberReward;
	}
	
	public String toString() {
		return "（リーダにもメンバにもなれるときは）ランダムに役割を選ぶ";
	}

}
