package main.teamformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import library.DeadlineLibrary;
import log.Log;
import main.TaskMarking;
import main.agent.AgentFactory;
import random.RandomKey;
import random.RandomManager;
import state.LeaderTaskExecuteState;
import state.LeaderWaitingState;
import state.MemberTaskExecuteState;
import state.MemberTeamDissolutionConfirmationState;
import state.MemberWaitingState;
import state.State;
import state.RoleSelectionState;
import state.SubtaskAllocationState;
import state.SubtaskReceptionState;
import state.TaskExecuteState;
import state.TaskMarkedWaitingState;
import state.TaskReturnedWaitingState;
import state.TaskSelectionState;
import task.Task;
import constant.Constant;
import exception.AbnormalException;
import agent.Agent;

public class TeamFormationParameter {
	final ArrayList<Agent> agents = new ArrayList<Agent>();
	final HashMap<State, ArrayList<Agent>> agentsMap = new HashMap<State, ArrayList<Agent>>();
	final HashMap<TaskMarking, ArrayList<Agent>> taskMarkingAgentMap = new HashMap<TaskMarking, ArrayList<Agent>>();
	public final ArrayList<Task> taskQueue = new ArrayList<Task>();
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
		agentsMap.put(TaskSelectionState.getState(), new ArrayList<Agent>());
		agentsMap.put(RoleSelectionState.getState(), new ArrayList<Agent>());
		agentsMap.put(SubtaskAllocationState.getState(), new ArrayList<Agent>());
		agentsMap.put(SubtaskReceptionState.getState(), new ArrayList<Agent>());
		agentsMap.put(LeaderWaitingState.getState(), new ArrayList<Agent>());
		agentsMap.put(MemberWaitingState.getState(), new ArrayList<Agent>());
		agentsMap.put(TaskExecuteState.getState(), new ArrayList<Agent>());
		agentsMap.put(TaskMarkedWaitingState.getState(), new ArrayList<Agent>());
		agentsMap.put(TaskReturnedWaitingState.getState(), new ArrayList<Agent>());
		agentsMap.put(LeaderTaskExecuteState.getState(), new ArrayList<Agent>());
		agentsMap.put(MemberTaskExecuteState.getState(), new ArrayList<Agent>());
		agentsMap.put(MemberTeamDissolutionConfirmationState.getState(), new ArrayList<Agent>());
	}
	
	private void initializeTaskMarkingAgentsMap() {
		taskMarkingAgentMap.clear();
//		taskMarkingAgentMap.put(TaskMarking.TASK_MARKING, new ArrayList<Agent>());
//		taskMarkingAgentMap.put(TaskMarking.NO_TASK_MARKING, new ArrayList<Agent>());
	}
	
	public void makeAgents(AgentFactory factory) {
		// エージェントの生成
		for(int id = 0; id < Constant.AGENT_NUM; id++){
			agents.add(factory.makeAgent(id));
			
		}
	}
	
	public Agent getAgent(int i){
		return agents.get(i);
	}
	
	public ArrayList<Agent> getAgents(){
		return agents;
	}
	
	public ArrayList<Agent> getAgentsFromMap(State state) {
		return agentsMap.get(state);
	}
	
	public void debugAgents() {
		Log.log.debugln("エージェントリスト");
		for(Agent agent : agents){
			Log.log.debugln(agent);
		}
		Log.log.debugln();
	}
	
	void clearAgentsMap() {
		for(ArrayList<Agent> agentsList : agentsMap.values()){
			agentsList.clear();
		}
	}
	
	void clearTaskMarkingAgentsMap() {
		for(ArrayList<Agent> agentsList : taskMarkingAgentMap.values()){
			agentsList.clear();
		}
	}
	
	void shuffleAgentsMap() {
		for(ArrayList<Agent> agentsList : agentsMap.values()){
			Collections.shuffle(agentsList, RandomManager.getRandom(RandomKey.AGENT_MAP_RANDOM));
		}
	}
	
	void classifyAgentIntoState() {
		for(Agent agent : agents){
			agentsMap.get(agent.getParameter().getState()).add(agent);
		}
	}
	
	public void addAgentToAgentsMap(State state, Agent agent) {
		if(state.equals(agent.getParameter().getState())){
			agentsMap.get(state).add(agent);
		}
		else{
			throw new AbnormalException("Agentの状態とagentsMapのキーが不一致");
		}
	}
	
	public void addAgentToTaskMarkingAgentsMap(TaskMarking isMarked, Agent agent) {
		if(agent.getParameter().getState().equals(TaskSelectionState.getState())
				|| agent.getParameter().getState().equals(TaskMarkedWaitingState.getState())){
			taskMarkingAgentMap.get(isMarked).add(agent);
		}
		else{
			throw new AbnormalException("Agentの状態とtaskMarkingAgentsMapのキーが不一致");
		}
	}
	
	void addTaskToQueue() {
		int taskAdditionNum = getPoissonTaskAdditionNum(Constant.ADD_TASK_PER_TURN);
		for(int id = 0; id < taskAdditionNum; id++){
			taskQueue.add(new Task(taskId++, 
					RandomManager.getRandom(RandomKey.TASK_RANDOM).nextInt(Constant.SUBTASK_IN_TASK_NUM) + Constant.SUBTASK_IN_TASK_INIT, 
					Constant.TASK_DEADLINE_MULTIPLE *
					(RandomManager.getRandom(RandomKey.DEADLINE_RANDOM).nextInt(Constant.DEADLINE_MAX) 
							+ Constant.DEADLINE_INIT)));
		}
		debugTaskQueue();
	}
	
	public void debugTaskQueue() {
		Log.log.debugln("タスクキュー");
		for(Task task : taskQueue){
			Log.log.debugln(task);
		}
		Log.log.debugln();
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
	
	public ArrayList<Task> lookingTaskQueue(){
		return taskQueue;
	}
	
	public void removeTask(Task task){
		boolean removed = taskQueue.remove(task);
		if (!removed) {
			throw new AbnormalException("タスクをキューから除けませんでした");
		}
	}

	public void returnTask(Task task) {
		taskQueue.add(task);
	}
	
	void decreaseTaskDeadline(TeamFormationMeasuredData measure) {
		for(int i = 0; i < taskQueue.size(); ){
			taskQueue.get(i).subtractDeadlineInTask();
			// タスクのデッドラインが処理できない時間だったらキューから削除する
			if(taskQueue.get(i).getDeadlineInTask() <= (DeadlineLibrary.getReducedDeadlineAtInitialTurn(Constant.MESSAGE_DELAY))
					&& !taskQueue.get(i).getMark()){
				measure.countFailure(taskQueue.get(i).getTaskRequireSum());
				removeTask(taskQueue.get(i));
			}
			else i++;
		}
	}
	
	public int getNoMarkingTaskNum() {
		int noMarkSize = 0;
		for(Task task : taskQueue){
			if(!task.getMark()){
				noMarkSize++;
			}
		}
		
		return noMarkSize;
	}
}
