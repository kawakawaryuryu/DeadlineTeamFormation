package post;

import java.util.ArrayList;
import java.util.Iterator;

import agent.Agent;
import message.AnswerMessage;
import message.OfferMessage;
import message.SubtaskDoneMessage;
import message.TeamDissolutionMessage;
import message.TeamFormationMessage;

/**
 * 通信遅延も考えたポスト
 */
public class DelayPost extends Post {

	private ArrayList<OfferMessage> offerMessages;
	private ArrayList<AnswerMessage> answerMessages;
	private ArrayList<TeamFormationMessage> teamFormationMessages;
	private ArrayList<SubtaskDoneMessage> subtaskDoneMessages;
	private ArrayList<TeamDissolutionMessage> teamDissolutionMessages;

	public DelayPost() {
		offerMessages = new ArrayList<OfferMessage>();
		answerMessages = new ArrayList<AnswerMessage>();
		teamFormationMessages = new ArrayList<TeamFormationMessage>();
		subtaskDoneMessages = new ArrayList<SubtaskDoneMessage>();
		teamDissolutionMessages = new ArrayList<TeamDissolutionMessage>();
	}

	public void initialize() {
		offerMessages.clear();
		answerMessages.clear();
		teamFormationMessages.clear();
		subtaskDoneMessages.clear();
		teamDissolutionMessages.clear();
	}

	@Override
	public void postOfferMessage(Agent to, OfferMessage message) {
		if(message.getDelayTime() == 0) to.getParameter().addOfferMessage(message);
		else offerMessages.add(message);
	}
	
	@Override
	public void postAnswerMessage(Agent to, AnswerMessage message) {
		if(message.getDelayTime() == 0) to.getParameter().addAnswerMessage(message);
		else answerMessages.add(message);
	}
	
	@Override
	public void postTeamFormationMessage(Agent to, TeamFormationMessage message) {
		if(message.getDelayTime() == 0) to.getParameter().setTeamFormationMessage(message);
		else teamFormationMessages.add(message);
	}

	public void postSubtaskDoneMessage(Agent to, SubtaskDoneMessage message) {
		if(message.getDelayCount() == 0) to.getParameter().addSubtaskDoneMessage(message);
		else subtaskDoneMessages.add(message);
	}

	public void postTeamDissolutionMessage(Agent to, TeamDissolutionMessage message) {
		if(message.getDelayCount() == 0) to.getParameter().setTeamDissolutionMessage(message);
		else teamDissolutionMessages.add(message);
	}

	public void sendOfferMessages() {
		Iterator<OfferMessage> it = offerMessages.iterator();
		while (it.hasNext()) {
			OfferMessage message = it.next();
			// 通信時間を+1する
			message.countDelay();

			// 遅延時間に達したら
			if (message.getDelayCount() == message.getDelayTime()) {
				Agent to = message.getTo();
				to.getParameter().addOfferMessage(message);
				it.remove();
			}
		}
	}

	public void sendAnswerMessages() {
		Iterator<AnswerMessage> it = answerMessages.iterator();
		while (it.hasNext()) {
			AnswerMessage message = it.next();
			// 通信時間を+1する
			message.countDelay();

			// 遅延時間に達したら
			if (message.getDelayCount() == message.getDelayTime()) {
				Agent to = message.getTo();
				to.getParameter().addAnswerMessage(message);
				it.remove();
			}
		}
	}

	public void sendTeamFormationMessages() {
		Iterator<TeamFormationMessage> it = teamFormationMessages.iterator();
		while (it.hasNext()) {
			TeamFormationMessage message = it.next();
			// 通信時間を+1する
			message.countDelay();

			// 遅延時間に達したら
			if (message.getDelayCount() == message.getDelayTime()) {
				Agent to = message.getTo();
				to.getParameter().setTeamFormationMessage(message);
				it.remove();
			}
		}
	}

	public void sendSubtaskDoneMessages() {
		Iterator<SubtaskDoneMessage> it = subtaskDoneMessages.iterator();
		while (it.hasNext()) {
			SubtaskDoneMessage message = it.next();
			// 通信時間を+1する
			message.countDelay();

			// 遅延時間に達したら
			if (message.getDelayCount() == message.getDelayTime()) {
				Agent to = message.getTo();
				to.getParameter().addSubtaskDoneMessage(message);
				it.remove();
			}
		}
	}

	public void sendTeamDissolutionMessages() {
		Iterator<TeamDissolutionMessage> it = teamDissolutionMessages.iterator();
		while (it.hasNext()) {
			TeamDissolutionMessage message = it.next();
			// 通信時間を+1する
			message.countDelay();

			// 遅延時間に達したら
			if (message.getDelayCount() == message.getDelayTime()) {
				Agent to = message.getTo();
				to.getParameter().setTeamDissolutionMessage(message);
				it.remove();
			}
		}
	}

}
