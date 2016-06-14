package main.agent;

import agent.Agent;
import agent.StructuredAgent;

public class StructuredAgentFactory implements AgentFactory {

	public StructuredAgentFactory() {

	}

	@Override
	public Agent makeAgent(int id) {
		return new StructuredAgent(id);
	}

}
