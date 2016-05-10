package main;

import log.Log;
import state.RoleSelectionState;
import state.SubtaskAllocationState;
import state.SubtaskReceptionState;
import state.TaskExecuteState;
import state.TaskMarkedWaitingState;
import state.TaskSelectionState;
import agent.Agent;

public class AgentActionManager {

	public void actionByMarkedWatingAgent() {
		// 自分に来ているメッセージを破棄
		for(Agent agent : TeamFormationMain.getParameter().agentsMap.get(TaskMarkedWaitingState.getState())){
			agent.getParameter().getOfferMessages().clear();
		}
		Log.log.debugln("------- タスクマーク待機状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationMain.getParameter().agentsMap.get(TaskMarkedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}
	
	public void actionByInitialAgent() {
		for(Agent agent : TeamFormationMain.getParameter().agentsMap.get(TaskSelectionState.getState())){
			agent.getParameter().initialize();
		}
		Log.log.debugln("------- タスク選択状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationMain.getParameter().agentsMap.get(TaskSelectionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}
	
	public void actionByRoleSelectionAgent() {
		Log.log.debugln("------- 役割選択状態のエージェントの行動 / タスクをマークしていないエージェント -------");
		for(Agent agent : TeamFormationMain.getParameter().agentsMap.get(RoleSelectionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}
	
	public void actionByLeaderOrMemberAgent() {
		Log.log.debugln("------- リーダ状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationMain.getParameter().agentsMap.get(SubtaskAllocationState.getState())){
			agent.action();
		}
		Log.log.debugln();
		Log.log.debugln("------- メンバ状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationMain.getParameter().agentsMap.get(SubtaskReceptionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}
	
	public void actionByExecuteAgent() {
		Log.log.debugln("------- タスク実行状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationMain.getParameter().agentsMap.get(TaskExecuteState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

}
