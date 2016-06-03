package state;

import agent.ConcreteAgent;

public interface State {
	public abstract void agentAction(ConcreteAgent agent);
}
