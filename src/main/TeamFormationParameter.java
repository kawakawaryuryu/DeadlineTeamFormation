package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import state.FixedState;
import state.RoleSelectionState;
import state.SubtaskAllocationState;
import state.SubtaskReceptionState;
import state.TaskExecuteState;
import state.TaskMarkedWaitingState;
import state.TaskSelectionState;
import task.FixedTask;
import constant.FixedConstant;
import agent.FixedAgent;

public class TeamFormationParameter {
	final ArrayList<FixedAgent> agents = new ArrayList<FixedAgent>();
	final HashMap<FixedState, ArrayList<FixedAgent>> agentsMap = new HashMap<FixedState, ArrayList<FixedAgent>>();
	final HashMap<TaskMarking, ArrayList<FixedAgent>> taskMarkingAgentMap = new HashMap<TaskMarking, ArrayList<FixedAgent>>();
	public final ArrayList<FixedTask> taskQueue = new ArrayList<FixedTask>();
	private int taskId = 0;
	
	void initialize() {
		initializeAgentsMap();
		initializeTaskMarkingAgentsMap();
		agents.clear();
		clearAgentsMap();
		clearTaskMarkingAgentsMap();
		taskQueue.clear();
	}
	
	private void initializeAgentsMap() {
		agentsMap.clear();
		agentsMap.put(TaskSelectionState.getState(), new ArrayList<FixedAgent>());
		agentsMap.put(RoleSelectionState.getState(), new ArrayList<FixedAgent>());
		agentsMap.put(SubtaskAllocationState.getState(), new ArrayList<FixedAgent>());
		agentsMap.put(SubtaskReceptionState.getState(), new ArrayList<FixedAgent>());
		agentsMap.put(TaskExecuteState.getState(), new ArrayList<FixedAgent>());
		agentsMap.put(TaskMarkedWaitingState.getState(), new ArrayList<FixedAgent>());
	}
	
	private void initializeTaskMarkingAgentsMap() {
		taskMarkingAgentMap.clear();
//		taskMarkingAgentMap.put(TaskMarking.TASK_MARKING, new ArrayList<FixedAgent>());
//		taskMarkingAgentMap.put(TaskMarking.NO_TASK_MARKING, new ArrayList<FixedAgent>());
	}
	
	public FixedAgent getAgent(int i){
		return agents.get(i);
	}
	
	public ArrayList<FixedAgent> getAgent(){
		return agents;
	}
	
	public void debugAgents() {
		System.out.println("エージェントリスト");
		for(FixedAgent agent : agents){
			System.out.println(agent);
		}
		System.out.println();
	}
	
	void clearAgentsMap() {
		for(ArrayList<FixedAgent> agentsList : agentsMap.values()){
			agentsList.clear();
		}
	}
	
	void clearTaskMarkingAgentsMap() {
		for(ArrayList<FixedAgent> agentsList : taskMarkingAgentMap.values()){
			agentsList.clear();
		}
	}
	
	void shuffleAgentsMap() {
		for(ArrayList<FixedAgent> agentsList : agentsMap.values()){
			Collections.shuffle(agentsList, RandomManager.getRandom(RandomKey.AGENT_MAP_RANDOM));
		}
	}
	
	void classifyAgentIntoState() {
		for(FixedAgent agent : agents){
			agentsMap.get(agent.getParameter().getState()).add(agent);
		}
	}
	
	public void addAgentToAgentsMap(FixedState state, FixedAgent agent) {
		if(state.equals(agent.getParameter().getState())){
			agentsMap.get(state).add(agent);
		}
		else{
			System.err.println("Agentの状態とagentsMapのキーが不一致");
			System.exit(-1);
		}
	}
	
	public void addAgentToTaskMarkingAgentsMap(TaskMarking isMarked, FixedAgent agent) {
		if(agent.getParameter().getState().equals(TaskSelectionState.getState())
				|| agent.getParameter().getState().equals(TaskMarkedWaitingState.getState())){
			taskMarkingAgentMap.get(isMarked).add(agent);
		}
		else{
			System.err.println("Agentの状態とtaskMarkingAgentsMapのキーが不一致");
			System.exit(-1);
		}
	}
	
	void addTaskToQueue() {
		int taskAdditionNum = getPoissonTaskAdditionNum(FixedConstant.ADD_TASK_PER_TURN);
		for(int id = 0; id < taskAdditionNum; id++){
			taskQueue.add(new FixedTask(taskId++, 
					RandomManager.getRandom(RandomKey.TASK_RANDOM).nextInt(FixedConstant.SUBTASK_IN_TASK_NUM) + FixedConstant.SUBTASK_IN_TASK_INIT, 
					FixedConstant.TASK_DEADLINE_MULTIPLE *
					(RandomManager.getRandom(RandomKey.DEADLINE_RANDOM).nextInt(FixedConstant.DEADLINE_MAX) 
							+ FixedConstant.DEADLINE_INIT)));
		}
//		debugTaskQueue();
	}
	
	public void debugTaskQueue() {
		System.out.println("タスクキュー");
		for(FixedTask task : taskQueue){
			System.out.println(task);
		}
		System.out.println();
	}
	
	private int getPoissonTaskAdditionNum(double lambda){
		double xp = RandomManager.getRandom(RandomKey.POISSON_RANDOM).nextDouble();
		int k = 0;
		while(xp >= Math.exp(-lambda)){
			xp = xp * RandomManager.getRandom(RandomKey.POISSON_RANDOM_2).nextDouble();
			k++;
		}
		
		return k;
	}
	
	public ArrayList<FixedTask> lookingTaskQueue(){
		return taskQueue;
	}
	
	public void removeTask(FixedTask task){
		taskQueue.remove(task);
	}
	
	void decreaseTaskDeadline(TeamFormationMeasuredData measure) {
		for(int i = 0; i < taskQueue.size(); ){
			taskQueue.get(i).subtractDeadlineInTask();
			// タスクのデッドラインが処理できない時間だったらキューから削除する
			if(taskQueue.get(i).getDeadlineInTask() <= (FixedConstant.WAIT_TURN + FixedConstant.DEADLINE_MIN_2) 
					&& !taskQueue.get(i).getMark()){
				measure.countFailure(taskQueue.get(i).getTaskRequireSum());
				removeTask(taskQueue.get(i));
			}
			else i++;
		}
	}
	
	public int getNoMarkingTaskNum() {
		int noMarkSize = 0;
		for(FixedTask task : taskQueue){
			if(!task.getMark()){
				noMarkSize++;
			}
		}
		
		return noMarkSize;
	}
}
