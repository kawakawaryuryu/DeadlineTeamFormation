package fixed.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.main.RandomKey;
import fixed.main.RandomManager;
import fixed.message.FixedOfferMessage;
import fixed.task.FixedSubtask;

public class AgentTaskLibrary {
	
	public static int calculateExecuteTime(FixedAgent agent, FixedSubtask subtask){
		int[] time = new int[FixedConstant.RESOURCE_NUM];
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
	
	public static int calculateExecuteTimeSum(FixedAgent agent, ArrayList<FixedSubtask> subtasks){
		int executeTimeSum = 0;
		for(FixedSubtask subtask : subtasks){
			executeTimeSum += calculateExecuteTime(agent, subtask);
		}
		return executeTimeSum;
	}
	
	public static FixedAgent[] getSortedAgentsFromArray(double[] array, ArrayList<FixedAgent> agents){
		// エージェントと信頼度を対応させたクラス
		class AgentToArray {
			FixedAgent agent;
			double trust;
			AgentToArray(FixedAgent agent, double trust) {
				this.agent = agent;
				this.trust = trust;
			}
		}
		
		FixedAgent[] sortedAgents = new FixedAgent[agents.size()];
		ArrayList<AgentToArray> sorted = new ArrayList<AgentToArray>();
		
		// リストを生成
		for(FixedAgent you : agents){
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
	
	public static boolean isExecuteSubTask(FixedAgent agent, FixedSubtask subtask, int taskDeadline){
		if(calculateExecuteTime(agent, subtask) <= taskDeadline){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static ArrayList<FixedAgent> getAgentsCanExecuteSubtask(FixedSubtask subtask, FixedAgent[] agents) {
		ArrayList<FixedAgent> agentsCanExecuteSubtask = new ArrayList<FixedAgent>();
		int taskDeadline = subtask.getDeadline() - FixedConstant.DEADLINE_MIN_2;
		for(FixedAgent agent : agents){
			if(isExecuteSubTask(agent, subtask, taskDeadline)){
				agentsCanExecuteSubtask.add(agent);
			}
		}
		return agentsCanExecuteSubtask;
	}
	
	public static ArrayList<FixedOfferMessage> getCanExecuteOfferMessages(ArrayList<FixedOfferMessage> messages, FixedAgent agent) {
		ArrayList<FixedOfferMessage> canExecuteMessages = new ArrayList<FixedOfferMessage>();
		/* 処理できる提案メッセージを抽出 */
		for(FixedOfferMessage message : messages){
			if(isExecuteSubTask(agent, message.getSubtask(), message.getSubtask().getDeadline() - FixedConstant.DEADLINE_MIN_2)){
				canExecuteMessages.add(message);
			}
		}
		
		return canExecuteMessages;
	}
}
