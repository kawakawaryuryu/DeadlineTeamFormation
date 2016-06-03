package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import random.RandomKey;
import random.RandomManager;
import task.Subtask;
import message.OfferMessage;
import constant.Constant;
import agent.ConcreteAgent;

public class AgentTaskLibrary {
	
	public static int calculateExecuteTime(ConcreteAgent agent, Subtask subtask){
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
	
	public static int calculateExecuteTimeSum(ConcreteAgent agent, ArrayList<Subtask> subtasks){
		int executeTimeSum = 0;
		for(Subtask subtask : subtasks){
			executeTimeSum += calculateExecuteTime(agent, subtask);
		}
		return executeTimeSum;
	}
	
	public static ConcreteAgent[] getSortedAgentsFromArray(double[] array, ArrayList<ConcreteAgent> agents){
		// エージェントと信頼度を対応させたクラス
		class AgentToArray {
			ConcreteAgent agent;
			double trust;
			AgentToArray(ConcreteAgent agent, double trust) {
				this.agent = agent;
				this.trust = trust;
			}
		}
		
		ConcreteAgent[] sortedAgents = new ConcreteAgent[agents.size()];
		ArrayList<AgentToArray> sorted = new ArrayList<AgentToArray>();
		
		// リストを生成
		for(ConcreteAgent you : agents){
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
	
	public static boolean isExecuteSubTask(ConcreteAgent agent, Subtask subtask, int taskDeadline){
		if(calculateExecuteTime(agent, subtask) <= taskDeadline){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static ArrayList<ConcreteAgent> getAgentsCanExecuteSubtask(Subtask subtask, ConcreteAgent[] agents) {
		ArrayList<ConcreteAgent> agentsCanExecuteSubtask = new ArrayList<ConcreteAgent>();
		int taskDeadline = subtask.getDeadline() - Constant.DEADLINE_MIN_2;
		for(ConcreteAgent agent : agents){
			if(isExecuteSubTask(agent, subtask, taskDeadline)){
				agentsCanExecuteSubtask.add(agent);
			}
		}
		return agentsCanExecuteSubtask;
	}
	
	public static ArrayList<OfferMessage> getCanExecuteOfferMessages(ArrayList<OfferMessage> messages, ConcreteAgent agent) {
		ArrayList<OfferMessage> canExecuteMessages = new ArrayList<OfferMessage>();
		/* 処理できる提案メッセージを抽出 */
		for(OfferMessage message : messages){
			if(isExecuteSubTask(agent, message.getSubtask(), message.getSubtask().getDeadline() - Constant.DEADLINE_MIN_2)){
				canExecuteMessages.add(message);
			}
		}
		
		return canExecuteMessages;
	}
}
