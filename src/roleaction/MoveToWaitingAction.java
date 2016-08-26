package roleaction;

import constant.Constant;
import state.TaskMarkedWaitingState;
import agent.Agent;

public class MoveToWaitingAction implements RoleAction {

	private int waitTurn = Constant.WAIT_TURN;

	@Override
	public void action(Agent agent) {
		if(waitTurn == 0) {
			// 待機時間（コピー時間）が0の場合はすぐ仮メンバ探しに移行
			RoleActionManager.tentativeMemberSelectionAction.action(agent);
		}
		else {
			// 待機状態に移行
			agent.getParameter().changeState(TaskMarkedWaitingState.getState());
		}

	}

}
