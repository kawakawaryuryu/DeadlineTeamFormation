package strategy.roleselection;

import java.util.ArrayList;
import java.util.Collections;

import constant.Constant;
import library.AgentTaskLibrary;
import main.RandomKey;
import main.RandomManager;
import message.OfferMessage;
import task.Task;
import agent.Agent;

public class ReciprocalRoleSelectionStrategy implements RoleSelectionStrategy {

	@Override
	public OfferMessage selectMessage(ArrayList<OfferMessage> messages,
			Agent agent) {
		Collections.shuffle(messages, RandomManager.getRandom(RandomKey.SHUFFLE_RANDOM_1));
		
		OfferMessage offerMessage;
		
		offerMessage = selectOfferMessage(messages, agent);
		
		return offerMessage;
	}
	
	private OfferMessage selectOfferMessage(ArrayList<OfferMessage> messages, Agent agent) {
		
		OfferMessage offerMessage;
		
		double probability = RandomManager.getRandom(RandomKey.EPSILON_GREEDY_RANDOM_1).nextDouble();
		
		// εの確率でランダムに選ぶ
		if(probability <= Constant.EPSILON){
			offerMessage = messages.get(RandomManager.getRandom(RandomKey.SELECT_RANDOM_1).nextInt(messages.size()));
			// System.out.println(maxExpectedRewardMessage + " をランダムで選びました");
		}
		// 1-εの確率で期待報酬の大きいエージェントを選ぶ
		else{
			offerMessage = getOfferMessage(messages, agent);
			//	System.out.println(maxExpectedRewardMessage + " を報酬期待度順で選びました");
		}
		return offerMessage;
	}
	
	private OfferMessage getOfferMessage(ArrayList<OfferMessage> messages, Agent agent) {
		OfferMessage maxExpectedRewardMessage = messages.get(0);
		// 信頼エージェントがいれば
		if (agent.getParameter().getTrustLeaders().contains(maxExpectedRewardMessage.getFrom())) {
			return maxExpectedRewardMessage;
		}
					
		for(int i = 1; i < messages.size(); i++){
			OfferMessage message = messages.get(i);
			
			// 信頼エージェントがいれば
			if (agent.getParameter().getTrustLeaders().contains(message.getFrom())) {
				return message;
			}
			
			double expectedReward = getExpectedMemberReward(agent, message);
			double maxExpectedReward = getExpectedMemberReward(agent, maxExpectedRewardMessage);
			
			if(expectedReward > maxExpectedReward){
				maxExpectedRewardMessage = message;
			}
		}
		
		return maxExpectedRewardMessage;
	}

	@Override
	public double calculateExpectedLeaderReward(Agent agent, Task task) {
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
				leaderReward = Constant.NO_PAST_TEAMS;	//履歴なし
			}
		}
		
		return leaderReward;
	}

	@Override
	public double calculateExpectedMemberReward(Agent agent,
			ArrayList<OfferMessage> messages) {
		// 来ている提案メッセージから処理できるメッセージを抽出
		ArrayList<OfferMessage> canBeExecutedMessages = AgentTaskLibrary.getCanExecuteOfferMessages(messages, agent);
		// System.out.println("来ている提案メッセージの中で処理できるメッセージ");
		// debugOfferMessage(canBeExecutedMessages);

		double memberReward;

		// 処理できるメッセージがない場合
		if(canBeExecutedMessages.isEmpty()){
			//	System.out.println("処理できるメッセージは来ませんでした");
			memberReward = 0;
		}
		
		// 処理できるメッセージがあり、信頼エージェントもいる場合
		else if (!canBeExecutedMessages.isEmpty() && !agent.getParameter().getTrustLeaders().isEmpty()) {
			
			// 信頼エージェントが処理できるメッセージの中にいるか判定
			boolean isContains = false;
			OfferMessage trustMessage = null;
			for (OfferMessage message : canBeExecutedMessages) {
				Agent leader = message.getFrom(); 
				if (agent.getParameter().getTrustLeaders().contains(leader)) {
					trustMessage = message;
					isContains = true;
					break;
				}
			}
			
			
			// 信頼エージェントがいればそれを選択
			if(isContains) {
				OfferMessage selectedMessage = trustMessage;
				agent.getParameter().setSelectedOfferMessage(selectedMessage);
				memberReward = getExpectedMemberReward(agent, selectedMessage);
			}
			// いなければすべてのメッセージを断る
			else {
				memberReward = 0;
			}
			
		}
		
		// 処理できるメッセージがあるが、信頼エージェントがいない場合
		else if (!canBeExecutedMessages.isEmpty() && agent.getParameter().getTrustLeaders().isEmpty()) {
			OfferMessage selectedMessage = selectMessage(canBeExecutedMessages, agent);
			agent.getParameter().setSelectedOfferMessage(selectedMessage);
			memberReward = getExpectedMemberReward(agent, selectedMessage);
		}
		else {
			memberReward = -1;
			System.err.println("そのような場合は存在しません");
			System.exit(-1);
		}
		return memberReward;
	}
	
	private double getExpectedMemberReward(Agent agent, OfferMessage message) {
		int expectedExecuteTime = AgentTaskLibrary.calculateExecuteTime(agent, message.getSubtask());
		double memberReward = (double)expectedExecuteTime * agent.getRewardExpectation(message.getFrom());
		
		return memberReward;
	}
	
	public String toString() {
		return "リーダとメンバの期待報酬を比較して、多い方の役割を選ぶ（ただし信頼するリーダエージェントがいる場合はそのリーダを選ぶ）";
	}

}
