package factory.agent;

import agent.Agent;
import agent.StructuredAgent;
import agent.paramter.AbstractAgentParameter;
import agent.paramter.StructuredAgentParameter;

public class StructuredAgentFactory implements AgentFactory {

	public StructuredAgentFactory() {

	}

	@Override
	public Agent makeAgent(int id) {
		AbstractAgentParameter parameter = new StructuredAgentParameter();
		return new StructuredAgent(id, parameter);
	}

}
