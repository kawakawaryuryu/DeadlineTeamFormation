package action;


public class ActionManager {

	public static Action toTaskReturnedWaitingStateAction = new ToTaskReturnedWaitingStateAction();
	public static Action taskReturnAction = new TaskReturnUnmarkedWaitingAction();

}
