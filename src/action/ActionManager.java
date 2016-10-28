package action;


public class ActionManager {

	public static Action toTaskReturnedWaitingStateAction = new ToTaskReturnedWaitingStateAction();
	public static Action toRoleSelectionStateAction = new ToRoleSelectionStateAction();
	public static Action toInitialStateAction = new ToInitialStateAction();
}
