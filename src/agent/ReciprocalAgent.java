package agent;

import java.util.Arrays;

import team.Team;
import constant.Constant;

/**
 * リーダに対する信頼度を持つエージェント
 */
public class ReciprocalAgent extends Agent {
	
	double[] trustToLeader = new double[Constant.AGENT_NUM];

	public ReciprocalAgent(int id) {
		super(id);
		
		Arrays.fill(trustToLeader, Constant.INITIAL_TRUST_TO_LEADER);
		trustToLeader[id] = 0.0;
	}

	public ReciprocalAgent(int id, int[] ability) {
		super(id, ability);
		
		Arrays.fill(trustToLeader, Constant.INITIAL_TRUST_TO_LEADER);
		trustToLeader[id] = 0.0;
	}
	
	public void feedbackTrustToLeader(Agent you, Team team, boolean isok) {
		double value;
		if(isok){
			/* 自分の実行時間より長い時間かかる場合 */
			if(this.parameter.getExecuteTime() < team.getTeamExecuteTime()){
				value = (double)this.parameter.getExecuteTime() / (double)team.getTeamExecuteTime();
			}
			else{
				value = 1.0;
			}
		}
		else{
			value = 0.0;
		}
		
		//リーダに対する信頼度の更新
		trustToLeader[you.id] = Constant.LEARN_RATE_TRUST_TO_LEADER * value + (1.0 - Constant.LEARN_RATE_TRUST_TO_LEADER) * trustToLeader[you.id];
	}
	
	public double getTrustToLeader(Agent you) {
		return trustToLeader[you.id];
	}
	
	public double[] getTrustToLeader() {
		return trustToLeader;
	}

}
