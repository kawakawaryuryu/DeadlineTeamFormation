package post;

import java.util.ArrayList;
import java.util.Iterator;

import agent.Agent;
import message.AnswerMessage;
import message.OfferMessage;
import message.TeamFormationMessage;

/**
 * 通信遅延も考えたポスト
 */
public class DelayPost extends Post {

	private ArrayList<OfferMessage> offerMessages;
	private ArrayList<AnswerMessage> answerMessages;
	private ArrayList<TeamFormationMessage> teamFormationMessages;

	public DelayPost() {
		offerMessages = new ArrayList<OfferMessage>();
		answerMessages = new ArrayList<AnswerMessage>();
		teamFormationMessages = new ArrayList<TeamFormationMessage>();
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

	public void sendOfferMessages() {
		Iterator<OfferMessage> it = offerMessages.iterator();
		while (it.hasNext()) {
			OfferMessage message = it.next();
			// 遅延時間に達したら
			if (message.getDelayCount() == message.getDelayTime()) {
				Agent to = message.getTo();
				to.getParameter().addOfferMessage(message);
				it.remove();
			}
			else {
				message.countDelay();
			}
		}
	}

	public void sendAnswerMessages() {
		Iterator<AnswerMessage> it = answerMessages.iterator();
		while (it.hasNext()) {
			AnswerMessage message = it.next();
			// 遅延時間に達したら
			if (message.getDelayCount() == message.getDelayTime()) {
				Agent to = message.getTo();
				to.getParameter().addAnswerMessage(message);
				it.remove();
			}
			else {
				message.countDelay();
			}
		}
	}

	public void sendTeamFormationMessages() {
		Iterator<TeamFormationMessage> it = teamFormationMessages.iterator();
		while (it.hasNext()) {
			TeamFormationMessage message = it.next();
			// 遅延時間に達したら
			if (message.getDelayCount() == message.getDelayTime()) {
				Agent to = message.getTo();
				to.getParameter().setTeamFormationMessage(message);
				it.remove();
			}
			else {
				message.countDelay();
			}
		}
	}

}
