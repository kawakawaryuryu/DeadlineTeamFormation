package task;

import java.util.ArrayList;

import constant.Constant;
import agent.ConcreteAgent;

public class AgentInfo {

	private ArrayList<ConcreteAgent> agents = new ArrayList<ConcreteAgent>();
	
	public void addSelectedAgent(ConcreteAgent agent) {
		agents.add(agent);
		isOverSelectedNum();
	}
	
	public ArrayList<ConcreteAgent> getSelectedAgents() {
		return agents;
	}
	
	public ConcreteAgent getSelectedAgent(int index) {
		return agents.get(index);
	}
	
	public void removeAgent(int index) {
		agents.remove(index);
	}
	
	private void isOverSelectedNum() {
		if(agents.size() > Constant.SELECT_MEMBER_NUM){
			System.err.println("subtaskが保持するagentsのリストのサイズが" + Constant.SELECT_MEMBER_NUM + "を超えています");
			System.exit(-1);
		}
	}
	
	void clear() {
		agents.clear();
	}
}
