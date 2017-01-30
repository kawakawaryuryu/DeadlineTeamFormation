package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import random.RandomKey;
import random.RandomManager;
import task.Subtask;
import message.OfferMessage;
import constant.Constant;
import agent.Agent;
import agent.paramter.AbstractAgentParameter;

public class AgentTaskLibrary {
	
	public static int calculateExecuteTime(Agent agent, Subtask subtask){
		int[] time = new int[Constant.RESOURCE_NUM];
		int executeTime = 0;
		for(int i = 0; i < time.length; i++){
			/* タスク処理にかかる時間の計算 */
			time[i] = (int)Math.ceil((double)subtask.getRequire(i) / (double)agent.getAbility(i));
			
			if(executeTime < time[i]){
				executeTime = time[i];
			}
		}
		return executeTime;
	}
	
	public static int calculateExecuteTimeSum(Agent agent, ArrayList<Subtask> subtasks){
		int executeTimeSum = 0;
		for(Subtask subtask : subtasks){
			executeTimeSum += calculateExecuteTime(agent, subtask);
		}
		return executeTimeSum;
	}
	
	public static Agent[] getSortedAgentsFromArray(double[] array, ArrayList<Agent> agents){
		// エージェントと信頼度を対応させたクラス
		class AgentToArray {
			Agent agent;
			double trust;
			AgentToArray(Agent agent, double trust) {
				this.agent = agent;
				this.trust = trust;
			}
		}
		
		Agent[] sortedAgents = new Agent[agents.size()];
		ArrayList<AgentToArray> sorted = new ArrayList<AgentToArray>();
		
		// リストを生成
		for(Agent you : agents){
			sorted.add(new AgentToArray(you, array[you.getId()]));
		}
		
		// リストのソート
		Collections.shuffle(sorted, RandomManager.getRandom(RandomKey.SORT_RANDOM_1));
		Collections.sort(sorted, new Comparator<AgentToArray>() {
			@Override
			public int compare(AgentToArray o1, AgentToArray o2) {
				if(o2.trust - o1.trust > 0) return 1;
				else if(o2.trust - o1.trust < 0) return -1;
				else return 0;
			}
		});
		
		// ソートしたリストからエージェントを抽出してリスト化する
		int index = 0;
		for(AgentToArray obj : sorted){
			sortedAgents[index++] = obj.agent;
		}
		
		return sortedAgents;
	}
	
	public static boolean isExecuteSubTask(Agent agent, Subtask subtask, int taskDeadline){
		if(calculateExecuteTime(agent, subtask) <= taskDeadline){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static ArrayList<Agent> getAgentsCanExecuteSubtask(Subtask subtask, Agent[] agents) {
		ArrayList<Agent> agentsCanExecuteSubtask = new ArrayList<Agent>();
		int taskDeadline = subtask.getDeadline() - DeadlineLibrary.getReducedDeadlineAtFirstTurn(Constant.MESSAGE_DELAY);
		for(Agent agent : agents){
			if(isExecuteSubTask(agent, subtask, taskDeadline)){
				agentsCanExecuteSubtask.add(agent);
			}
		}
		return agentsCanExecuteSubtask;
	}
	
	public static ArrayList<OfferMessage> getCanExecuteOfferMessages(ArrayList<OfferMessage> messages, Agent agent) {
		ArrayList<OfferMessage> canExecuteMessages = new ArrayList<OfferMessage>();
		/* 処理できる提案メッセージを抽出 */
		for(OfferMessage message : messages){
			if(isExecuteSubTask(agent, message.getSubtask(), message.getSubtask().getDeadline()
					- DeadlineLibrary.getReducedDeadlineAtFirstTurn(Constant.MESSAGE_DELAY))){
				canExecuteMessages.add(message);
			}
		}
		
		return canExecuteMessages;
	}

	/**
	 * 1tickあたりの獲得報酬を返す
	 * @param parameter エージェントパラメータ
	 * @param isok チーム編成の成否
	 * @param reward 獲得報酬
	 * @return
	 */
	public static double getRewardPerTurn(AbstractAgentParameter parameter, boolean isok, double reward) {
		double executeTime;
		if(isok){
			executeTime = parameter.getParticipatingTeam().getTeamExecuteTime();
		}
		//チーム編成に失敗した場合は1とする（0だと割り切れないため）
		else{
			executeTime = 1;
		}
		return reward / (double)executeTime;
	}
}
