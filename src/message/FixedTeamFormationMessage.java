package message;

import java.util.ArrayList;

import task.FixedSubtask;
import team.FixedTeam;
import agent.FixedAgent;

public class FixedTeamFormationMessage extends Message {
	private boolean isok;
	private ArrayList<FixedSubtask> subtasks = new ArrayList<FixedSubtask>();
	private double leftReward;
	private int leftRequireSum;
	private FixedTeam team;
	private int subtaskRequireSum = 0;
	
	/**
	 * チーム編成に失敗した場合
	 * @param from
	 * @param to
	 * @param isok
	 */
	public FixedTeamFormationMessage(FixedAgent from, FixedAgent to, boolean isok) {
		super(from, to);
		this.isok = isok;
	}

	/**
	 * チーム編成に成功した場合
	 * @param from
	 * @param to
	 * @param isok
	 * @param subtasks
	 * @param leftReward
	 * @param leftRequireSum
	 * @param team
	 */
	public FixedTeamFormationMessage(FixedAgent from, FixedAgent to, boolean isok, ArrayList<FixedSubtask> subtasks, 
			double leftReward, int leftRequireSum, FixedTeam team) {
		this(from, to, isok);
		this.subtasks = subtasks;
		this.leftReward = leftReward;
		this.leftRequireSum = leftRequireSum;
		this.team = team;
		calculateSubtaskRequireSum();
	}
	
	private void calculateSubtaskRequireSum() {
		for(FixedSubtask subtask : subtasks){
			subtaskRequireSum += subtask.getRequireSum();
		}
	}
	
	public boolean getIsOk() {
		return isok;
	}
	
	public ArrayList<FixedSubtask> getSubtasks() {
		return subtasks;
	}
	
	public double getLeftReward() {
		return leftReward;
	}
	
	public int getLeftRequireSum() {
		return leftRequireSum;
	}
	
	public FixedTeam getTeam() {
		return team;
	}
	
	public int getSubtaskRequireSum() {
		return subtaskRequireSum;
	}
}
