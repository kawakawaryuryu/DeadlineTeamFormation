package message;

import java.util.ArrayList;

import task.Subtask;
import team.Team;
import agent.Agent;

public class TeamFormationMessage extends Message {
	private boolean isok;
	private ArrayList<Subtask> subtasks = new ArrayList<Subtask>();
	private double leftReward;
	private int leftRequireSum;
	private Team team;
	private int subtaskRequireSum = 0;
	
	/**
	 * チーム編成に失敗した場合
	 * @param from
	 * @param to
	 * @param isok
	 */
	public TeamFormationMessage(Agent from, Agent to, boolean isok) {
		super(from, to);
		this.isok = isok;
	}

	public TeamFormationMessage(Agent from, Agent to, int delayTime, boolean isok) {
		super(from, to, delayTime);
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
	public TeamFormationMessage(Agent from, Agent to, boolean isok, ArrayList<Subtask> subtasks, 
			double leftReward, int leftRequireSum, Team team) {
		this(from, to, isok);
		this.subtasks = subtasks;
		this.leftReward = leftReward;
		this.leftRequireSum = leftRequireSum;
		this.team = team;
		calculateSubtaskRequireSum();
	}

	public TeamFormationMessage(Agent from, Agent to, int delayTime, boolean isok, ArrayList<Subtask> subtasks, 
			double leftReward, int leftRequireSum, Team team) {
		this(from, to, delayTime, isok);
		this.subtasks = subtasks;
		this.leftReward = leftReward;
		this.leftRequireSum = leftRequireSum;
		this.team = team;
		calculateSubtaskRequireSum();
	}
	
	private void calculateSubtaskRequireSum() {
		for(Subtask subtask : subtasks){
			subtaskRequireSum += subtask.getRequireSum();
		}
	}
	
	public boolean getIsOk() {
		return isok;
	}
	
	public ArrayList<Subtask> getSubtasks() {
		return subtasks;
	}
	
	public double getLeftReward() {
		return leftReward;
	}
	
	public int getLeftRequireSum() {
		return leftRequireSum;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public int getSubtaskRequireSum() {
		return subtaskRequireSum;
	}
}
