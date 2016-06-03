package agent;

import java.util.ArrayList;
import java.util.HashMap;

import constant.Constant;
import role.ExecuteMeasuredData;
import role.InitialMeasuredData;
import role.LeaderMeasuredData;
import role.MeasuredDataForEachRole;
import role.MemberMeasuredData;
import role.Role;
import state.State;
import state.TaskSelectionState;
import task.Subtask;
import task.Task;
import team.Team;
import team.PastTeam;
import message.AnswerMessage;
import message.OfferMessage;
import message.TeamFormationMessage;
import agent.leader.LeaderField;
import agent.member.MemberField;

public class AgentParameter {
	State state;
	Role role;
	final HashMap<Role, MeasuredDataForEachRole> elements = new HashMap<Role, MeasuredDataForEachRole>();
	final ArrayList<ConcreteAgent> sendAgents = new ArrayList<ConcreteAgent>();
	Task markedTask;
	int executeTime;
	final ArrayList<Subtask> executedSubtasks = new ArrayList<Subtask>();;
	Team participatingTeam;
	ArrayList<ConcreteAgent> trustLeaders = new ArrayList<ConcreteAgent>();
	
	final ArrayList<OfferMessage> offerMessages = new ArrayList<OfferMessage>();
	final ArrayList<AnswerMessage> answerMessages = new ArrayList<AnswerMessage>();
	TeamFormationMessage teamFormationMessage;
	OfferMessage selectedOfferMessage;
	
	final LeaderField leaderField = new LeaderField();
	final MemberField memberField = new MemberField();
	
	final PastTeam pastTeam = new PastTeam();
	
	final TimerField timerField = new TimerField();
	
	State nextState;
	
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
	
	public void changeState(State state) {
		this.state = state;
	}
	
	public void changeRole(Role role) {
		this.role = role;
	}
	
	public void addSendAgents(ConcreteAgent agent) {
		sendAgents.add(agent);
	}
	
	public void setMarkedTask(Task task) {
		markedTask = task;
	}

	public void setExecutedSubtasks(Subtask subtask, int time) {
		executedSubtasks.add(subtask);
		executeTime += time;
	}
	
	public void setParticipatingTeam(Team team) {
		participatingTeam = team;
	}
	
	public void addToPastTeams() {
		pastTeam.addTeam(participatingTeam);
	}
	
	public void addOfferMessage(OfferMessage message) {
		offerMessages.add(message);
	}
	
	public void addAnswerMessage(AnswerMessage message) {
		answerMessages.add(message);
	}
	
	public void setTeamFormationMessage(TeamFormationMessage message) {
		teamFormationMessage = message;
	}
	
	public void setSelectedOfferMessage(OfferMessage message) {
		selectedOfferMessage = message;
	}
	
	public void setNextState(State nextState) {
		this.nextState = nextState;
	}
	
	public State getState() {
		return state;
	}
	
	public MeasuredDataForEachRole getElement(Role role) {
		return elements.get(role);
	}
	
	public ArrayList<ConcreteAgent> getSendAgents() {
		return sendAgents;
	}
	
	public Task getMarkedTask() {
		return markedTask;
	}
	
	public ArrayList<Subtask> getExecutedSubtasks() {
		return executedSubtasks;
	}
	
	public int getExecuteTime() {
		return executeTime;
	}
	
	public Team getParticipatingTeam() {
		return participatingTeam;
	}
	
	public PastTeam getPastTeam() {
		return pastTeam;
	}
	
	public ArrayList<OfferMessage> getOfferMessages() {
		return offerMessages;
	}
	
	public OfferMessage getSelectedOfferMessage() {
		return selectedOfferMessage;
	}
	
	public ArrayList<AnswerMessage> getAnswerMessages() {
		return answerMessages;
	}
	
	public TeamFormationMessage getTeamFormationMessage() {
		return teamFormationMessage;
	}
	
	public LeaderField getLeaderField() {
		return leaderField;
	}
	
	public MemberField getMemberField() {
		return memberField;
	}
	
	public TimerField getTimerField() {
		return timerField;
	}
	
	public State getNextState() {
		return nextState;
	}
	
	public void setTrustLeaders(ConcreteAgent leader) {
		if (trustLeaders.size() < Constant.TRUST_LEADER_LIMIT)
		trustLeaders.add(leader);
	}
	
	public void removeTrustLeader(ConcreteAgent leader) {
		trustLeaders.remove(leader);
	}
	
	public ArrayList<ConcreteAgent> getTrustLeaders() {
		return trustLeaders;
	}
	
	public ConcreteAgent getTrustLeaders(int index) {
		return trustLeaders.get(index);
	}
	
}
