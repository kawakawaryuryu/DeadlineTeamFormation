package main.agent;

import agent.Agent;
import agent.RationalAgent;
import agent.paramter.AbstractAgentParameter;
import agent.paramter.RationalAgentParameter;

public class RationalAgentFactory implements AgentFactory {

	public RationalAgentFactory() {

	}

	@Override
	public Agent makeAgent(int id) {
		AbstractAgentParameter parameter = new RationalAgentParameter();
		return new RationalAgent(id, parameter);
	}

}
