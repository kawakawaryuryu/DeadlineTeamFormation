package main.agent;

import agent.Agent;
import agent.RationalAgent;

public class RationalAgentFactory implements AgentFactory {

	public RationalAgentFactory() {

	}

	@Override
	public Agent makeAgent(int id) {
		return new RationalAgent(id);
	}

}
