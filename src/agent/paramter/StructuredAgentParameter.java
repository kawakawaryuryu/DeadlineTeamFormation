package agent.paramter;

import java.util.ArrayList;

import agent.Agent;
import constant.Constant;

public class StructuredAgentParameter extends AbstractAgentParameter {

	ArrayList<Agent> trustLeaders = new ArrayList<Agent>();

	// 信頼エージェントがリストに加えられた or 外れた場合にtrueになる
	boolean trustLeaderTriger;

	public StructuredAgentParameter() {
		super();
		trustLeaderTriger = false;
	}


	public void setTrustLeaders(Agent leader) {
		if (trustLeaders.size() < Constant.TRUST_LEADER_LIMIT) {
			trustLeaderTriger = true;
			trustLeaders.add(leader);
		}
	}

	public void removeTrustLeader(Agent leader) {
		trustLeaderTriger = true;
		trustLeaders.remove(leader);
	}

	public ArrayList<Agent> getTrustLeaders() {
		return trustLeaders;
	}

	public Agent getTrustLeaders(int index) {
		return trustLeaders.get(index);
	}

	public boolean containsTrustLeader(Agent you) {
		return trustLeaders.contains(you);
	}

	public boolean getTrustLeaderTriger() {
		return trustLeaderTriger;
	}

	public void setFalseTrustLeaderTriger() {
		trustLeaderTriger = false;
	}

}
