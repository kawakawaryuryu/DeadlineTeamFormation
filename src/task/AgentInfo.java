package task;

import java.util.ArrayList;

import constant.Constant;
import agent.Agent;

public class AgentInfo {

	private ArrayList<Agent> agents = new ArrayList<Agent>();
	
	public void addSelectedAgent(Agent agent) {
		agents.add(agent);
		isOverSelectedNum();
	}
	
	public ArrayList<Agent> getSelectedAgents() {
		return agents;
	}
	
	public Agent getSelectedAgent(int index) {
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
