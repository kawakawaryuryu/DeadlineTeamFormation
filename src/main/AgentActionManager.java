package main;

import log.Log;
import main.teamformation.TeamFormationInstances;
import state.TaskMarkedWaitingState;
import agent.Agent;

public class AgentActionManager {

	private TeamFormationInstances instance = new TeamFormationInstances();

	public void actionByMarkedWatingAgent() {
		// 自分に来ているメッセージを破棄
		for(Agent agent : instance.getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.getParameter().getOfferMessages().clear();
		}
		Log.log.debugln("------- タスクマーク待機状態のエージェントの行動 -------");
		for(Agent agent : instance.getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByInitialAgent() {
		for(Agent agent : instance.getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.getParameter().initialize();
		}
		Log.log.debugln("------- タスク選択状態のエージェントの行動 -------");
		for(Agent agent : instance.getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByRoleSelectionAgent() {
		Log.log.debugln("------- 役割選択状態のエージェントの行動 / タスクをマークしていないエージェント -------");
		for(Agent agent : instance.getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByLeaderOrMemberAgent() {
		Log.log.debugln("------- リーダ状態のエージェントの行動 -------");
		for(Agent agent : instance.getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
		Log.log.debugln("------- メンバ状態のエージェントの行動 -------");
		for(Agent agent : instance.getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByExecuteAgent() {
		Log.log.debugln("------- タスク実行状態のエージェントの行動 -------");
		for(Agent agent : instance.getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

}
