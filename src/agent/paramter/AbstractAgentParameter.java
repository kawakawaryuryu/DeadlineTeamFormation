package agent.paramter;

import java.util.ArrayList;
import java.util.HashMap;

import agent.Agent;
import agent.TimerField;
import agent.leader.LeaderField;
import agent.member.MemberField;
import message.AnswerMessage;
import message.OfferMessage;
import message.TeamFormationMessage;
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
import team.PastTeam;
import team.Team;

public abstract class AbstractAgentParameter {

	State state;
	Role role;
	final HashMap<Role, MeasuredDataForEachRole> elements = new HashMap<Role, MeasuredDataForEachRole>();
	final ArrayList<Agent> sendAgents = new ArrayList<Agent>();
	Task markedTask;
	int executeTime;
	final ArrayList<Subtask> executedSubtasks = new ArrayList<Subtask>();;
	Team participatingTeam;

	final ArrayList<OfferMessage> offerMessages = new ArrayList<OfferMessage>();
	final ArrayList<AnswerMessage> answerMessages = new ArrayList<AnswerMessage>();
	TeamFormationMessage teamFormationMessage;
	OfferMessage selectedOfferMessage;

	final LeaderField leaderField = new LeaderField();
	final MemberField memberField = new MemberField();

	final PastTeam pastTeam = new PastTeam();

	final TimerField timerField = new TimerField();

	State nextState;

	public AbstractAgentParameter() {
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

	public void addSendAgents(Agent agent) {
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

	public ArrayList<Agent> getSendAgents() {
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

	public void setTrustLeaders(Agent leader) {
		throw new UnsupportedOperationException("setTrustLeadersは実装されていません");
	}

	public void removeTrustLeader(Agent leader) {
		throw new UnsupportedOperationException("removeTrustLeaderは実装されていません");
	}

	public ArrayList<Agent> getTrustLeaders() {
		throw new UnsupportedOperationException("getTrustLeadersは実装されていません");
	}

	public Agent getTrustLeaders(int index) {
		throw new UnsupportedOperationException("getTrustLeadersは実装されていません");
	}

	public boolean containsTrustLeader(Agent you) {
		throw new UnsupportedOperationException("containsTrustLeadersは実装されていません");
	}

}
