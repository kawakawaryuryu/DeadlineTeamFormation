package fixed.agent;

import java.util.ArrayList;
import java.util.HashMap;

import fixed.agent.leader.FixedLeaderField;
import fixed.agent.member.FixedMemberField;
import fixed.message.FixedAnswerMessage;
import fixed.message.FixedOfferMessage;
import fixed.message.FixedTeamFormationMessage;
import fixed.role.ExecuteMeasuredData;
import fixed.role.InitialMeasuredData;
import fixed.role.LeaderMeasuredData;
import fixed.role.MeasuredDataForEachRole;
import fixed.role.MemberMeasuredData;
import fixed.role.Role;
import fixed.state.FixedState;
import fixed.state.TaskSelectionState;
import fixed.team.FixedTeam;
import fixed.team.PastTeam;
import fixed.task.FixedSubtask;
import fixed.task.FixedTask;

public class AgentParameter {
	FixedState state;
	Role role;
	final HashMap<Role, MeasuredDataForEachRole> elements = new HashMap<Role, MeasuredDataForEachRole>();
	final ArrayList<FixedAgent> sendAgents = new ArrayList<FixedAgent>();
	FixedTask markedTask;
	int executeTime;
	final ArrayList<FixedSubtask> executedSubtasks = new ArrayList<FixedSubtask>();;
	FixedTeam participatingTeam;
	
	final ArrayList<FixedOfferMessage> offerMessages = new ArrayList<FixedOfferMessage>();
	final ArrayList<FixedAnswerMessage> answerMessages = new ArrayList<FixedAnswerMessage>();
	FixedTeamFormationMessage teamFormationMessage;
	FixedOfferMessage selectedOfferMessage;
	
	final FixedLeaderField leaderField = new FixedLeaderField();
	final FixedMemberField memberField = new FixedMemberField();
	
	final PastTeam pastTeam = new PastTeam();
	
	final TimerField timerField = new TimerField();
	
	FixedState nextState;
	
	AgentParameter() {
		elements.put(Role.INITIAL, new InitialMeasuredData());
		elements.put(Role.LEADER, new LeaderMeasuredData());
		elements.put(Role.MEMBER, new MemberMeasuredData());
		elements.put(Role.EXECUTE, new ExecuteMeasuredData());
		initialize();
	}
	
	public void initialize() {
		state = TaskSelectionState.getState();
		role = Role.INITIAL;
		sendAgents.clear();
		markedTask = null;
		executeTime = 0;
		executedSubtasks.clear();
		participatingTeam = null;
		
		offerMessages.clear();
		answerMessages.clear();
		teamFormationMessage = null;
		selectedOfferMessage = null;
		
		leaderField.initialize();
		
		timerField.initialize();
	}
	
	public void changeState(FixedState state) {
		this.state = state;
	}
	
	public void changeRole(Role role) {
		this.role = role;
	}
	
	public void addSendAgents(FixedAgent agent) {
		sendAgents.add(agent);
	}
	
	public void setMarkedTask(FixedTask task) {
		markedTask = task;
	}

	public void setExecutedSubtasks(FixedSubtask subtask, int time) {
		executedSubtasks.add(subtask);
		executeTime += time;
	}
	
	public void setParticipatingTeam(FixedTeam team) {
		participatingTeam = team;
	}
	
	public void addToPastTeams() {
		pastTeam.addTeam(participatingTeam);
	}
	
	public void addOfferMessage(FixedOfferMessage message) {
		offerMessages.add(message);
	}
	
	public void addAnswerMessage(FixedAnswerMessage message) {
		answerMessages.add(message);
	}
	
	public void setTeamFormationMessage(FixedTeamFormationMessage message) {
		teamFormationMessage = message;
	}
	
	public void setSelectedOfferMessage(FixedOfferMessage message) {
		selectedOfferMessage = message;
	}
	
	public void setNextState(FixedState nextState) {
		this.nextState = nextState;
	}
	
	public FixedState getState() {
		return state;
	}
	
	public MeasuredDataForEachRole getElement(Role role) {
		return elements.get(role);
	}
	
	public ArrayList<FixedAgent> getSendAgents() {
		return sendAgents;
	}
	
	public FixedTask getMarkedTask() {
		return markedTask;
	}
	
	public ArrayList<FixedSubtask> getExecutedSubtasks() {
		return executedSubtasks;
	}
	
	public int getExecuteTime() {
		return executeTime;
	}
	
	public FixedTeam getParticipatingTeam() {
		return participatingTeam;
	}
	
	public PastTeam getPastTeam() {
		return pastTeam;
	}
	
	public ArrayList<FixedOfferMessage> getOfferMessages() {
		return offerMessages;
	}
	
	public FixedOfferMessage getSelectedOfferMessage() {
		return selectedOfferMessage;
	}
	
	public ArrayList<FixedAnswerMessage> getAnswerMessages() {
		return answerMessages;
	}
	
	public FixedTeamFormationMessage getTeamFormationMessage() {
		return teamFormationMessage;
	}
	
	public FixedLeaderField getLeaderField() {
		return leaderField;
	}
	
	public FixedMemberField getMemberField() {
		return memberField;
	}
	
	public TimerField getTimerField() {
		return timerField;
	}
	
	public FixedState getNextState() {
		return nextState;
	}
}
