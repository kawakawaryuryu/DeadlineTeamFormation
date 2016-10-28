package main.model;

import log.Log;
import main.teamformation.TeamFormationInstances;
import state.InitialLeaderState;
import state.InitialMemberState;
import state.InitialRoleDecisionState;
import state.LeaderTaskExecuteState;
import state.LeaderWaitingState;
import state.MemberTaskExecuteState;
import state.MemberTeamDissolutionConfirmationState;
import state.MemberWaitingState;
import state.RoleSelectionState;
import state.SubtaskAllocationState;
import state.SubtaskReceptionState;
import state.TaskExecuteState;
import state.TaskMarkedWaitingState;
import state.TaskReturnedWaitingState;
import state.TaskSelectionState;
import agent.Agent;

public class AgentActionManager {


	public void actionByMarkedWatingAgent() {
		// 自分に来ているメッセージを破棄
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.getParameter().getOfferMessages().clear();
		}
		Log.log.debugln("------- タスクマーク待機状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(TaskMarkedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByReturnedWaitingAgent() {
		Log.log.debugln("------- タスク返却待機状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(TaskReturnedWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByInitialAgent() {
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(TaskSelectionState.getState())){
			agent.getParameter().initialize();
		}
		Log.log.debugln("------- タスク選択状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(TaskSelectionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByTaskSelectionAgent() {
		Log.log.debugln("------- タスク選択状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(TaskSelectionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByInitialRoleDecisionAgent() {
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(InitialRoleDecisionState.getState())){
			agent.getParameter().initialize();
		}
		Log.log.debugln("------- 初期役割決定状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(InitialRoleDecisionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByInitialLeaderAgent() {
		Log.log.debugln("------- 初期リーダ状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(InitialLeaderState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByInitialMemberAgent() {
		Log.log.debugln("------- 初期メンバ状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(InitialMemberState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByRoleSelectionAgent() {
		Log.log.debugln("------- 役割選択状態のエージェントの行動 / タスクをマークしていないエージェント -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(RoleSelectionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByLeaderOrMemberAgent() {
		Log.log.debugln("------- リーダ状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(SubtaskAllocationState.getState())){
			agent.action();
		}
		Log.log.debugln();
		Log.log.debugln("------- メンバ状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(SubtaskReceptionState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByLeaderOrMemberWaitingAgent() {
		Log.log.debugln("------- リーダ前待機状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(LeaderWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
		Log.log.debugln("------- メンバ待機状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(MemberWaitingState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByExecuteAgent() {
		Log.log.debugln("------- タスク実行状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(TaskExecuteState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByLeaderExecuteAgent() {
		Log.log.debugln("------- リーダタスク実行状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(LeaderTaskExecuteState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByMemberExecuteAgent() {
		Log.log.debugln("------- メンバタスク実行状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(MemberTaskExecuteState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

	public void actionByMemberTeamDissolutionConfirmationAgent() {
		Log.log.debugln("------- メンバチーム解散通知確認状態のエージェントの行動 -------");
		for(Agent agent : TeamFormationInstances.getInstance().getParameter().getAgentsFromMap(MemberTeamDissolutionConfirmationState.getState())){
			agent.action();
		}
		Log.log.debugln();
	}

}
