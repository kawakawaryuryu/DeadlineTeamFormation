package task;

import java.util.ArrayList;

import constant.FixedConstant;
import agent.FixedAgent;

public class AgentInfo {

	private ArrayList<FixedAgent> agents = new ArrayList<FixedAgent>();
	
	public void addSelectedAgent(FixedAgent agent) {
		agents.add(agent);
		isOverSelectedNum();
	}
	
	public ArrayList<FixedAgent> getSelectedAgents() {
		return agents;
	}
	
	public FixedAgent getSelectedAgent(int index) {
		return agents.get(index);
	}
	
	public void removeAgent(int index) {
		agents.remove(index);
	}
	
	private void isOverSelectedNum() {
		if(agents.size() > FixedConstant.SELECT_MEMBER_NUM){
			System.err.println("subtaskが保持するagentsのリストのサイズが" + FixedConstant.SELECT_MEMBER_NUM + "を超えています");
			System.exit(-1);
		}
	}
	
	void clear() {
		agents.clear();
	}
}
