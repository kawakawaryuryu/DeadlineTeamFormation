package task;

import java.util.ArrayList;

import fixed.agent.FixedAgent;
import main.Constant;

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
		if(agents.size() > Constant.SELECT_MEMBER_NUM){
			System.err.println("subtaskが保持するagentsのリストのサイズが" + Constant.SELECT_MEMBER_NUM + "を超えています");
			System.exit(-1);
		}
	}
	
	void clear() {
		agents.clear();
	}
}
