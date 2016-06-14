package agent.paramter;

import java.util.ArrayList;

import agent.Agent;
import constant.Constant;

public class StructuredAgentParameter extends AbstractAgentParameter {

	ArrayList<Agent> trustLeaders = new ArrayList<Agent>();

	public StructuredAgentParameter() {
		super();
	}


	public void setTrustLeaders(Agent leader) {
		if (trustLeaders.size() < Constant.TRUST_LEADER_LIMIT)
		trustLeaders.add(leader);
	}

	public void removeTrustLeader(Agent leader) {
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

}
