package main.agent;

import agent.Agent;

public interface AgentFactory {

	public Agent makeAgent(int id);
}
