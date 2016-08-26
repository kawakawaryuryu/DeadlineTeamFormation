package roleaction;

public class RoleActionManager {

	public static RoleAction tentativeMemberSelectionAction = new TentativeMemberSelectionAction();
	public static RoleAction participatingTeamDecisionAction = new ParticipatingTeamDecisionAction();
	public static RoleAction backToInitialStateAction = new BackToInitialStateAction();
	public static RoleAction moveToWaitingAction = new MoveToWaitingAction();

}
