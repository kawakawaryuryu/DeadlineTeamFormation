package main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.List;

import agent.Agent;
import task.Task;
import team.Team;
import state.InitialMarkingTaskState;
import state.InitialRoleSelectState;
import state.LeaderState;

public class FileWriter {
	private static int fileNumber = 183;	//ファイルのnumber
	private static boolean isWrite = false;	//追加か上書きか
//	static int experimentNumber = 1;	//実験回数
	
	private static Agent agent = Main.getAgent(0);
	private static InitialMarkingTaskState initialMarkingTaskState = new InitialMarkingTaskState();
	private static InitialRoleSelectState initialRoleSelectState = new InitialRoleSelectState();
	private static LeaderState leaderState = new LeaderState();
	
	/**
	 * ファイルの内容説明の書き込み
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter fileExplain() throws IOException{
		File directory = new File("data/file/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/file/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/file_" + fileNumber + ".csv", false), "Shift_JIS")));
		pw.println("ファイル名" + "," + Constant.TURN_NUM + "turn_" + Constant.AGENT_NUM + "agents_" + fileNumber + ".csv");
		pw.println("FIFO、マークあり、チームの履歴を保持、学習あり、タスク処理の見積もりを行う、1tickごとの獲得報酬で報酬期待度を学習、リーダにはまず1個だけ割り当てる");
		pw.println("ターン" + "," + Constant.TURN_NUM);
		pw.println("試行回数" + "," + Administrator.EXPERIMENT_NUM);
		pw.println("エージェント数" + "," + Constant.AGENT_NUM);
		pw.println("リソースの種類数" + "," + Constant.RESOURCE_NUM);
		pw.println("欲張り度の初期値" + "," + Constant.INITIAL_GREEDY);
		pw.println("提案受託期待度の初期値" + "," + Constant.INITIAL_TRUST);
		pw.println("報酬期待度の初期値" + "," + Constant.INITIAL_EXPECTED_REWARD);
		pw.println("欲張り度の学習率" + "," + Constant.LEARN_RATE_GREEDY);
		pw.println("提案受託期待度の学習率" + "," + Constant.LEARN_RATE_TRUST);
		pw.println("報酬期待度の学習率" + "," + Constant.LEARN_RATE_REWARD);
		pw.println("チーム履歴を保持する数" + "," + Constant.PAST_TEAM_NUM);
		pw.println("タスクの中のサブタスク数" + "," + Constant.SUBTASK_IN_TASK_INIT + "　~　" + (Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM - 1));
		pw.println("エージェントのリソース" + "," + Constant.AGENT_ABILITY_INIT + "　~　" + (Constant.AGENT_ABILITY_INIT + Constant.AGENT_ABILITY_MAX - 1));
		pw.println("タスクのリソース" + "," + (Constant.TASK_REQUIRE_INIT * Constant.taskRequireLcm()) + " ~　" + ((Constant.TASK_REQUIRE_INIT + Constant.TASK_REQUIRE_MAX - 1) * Constant.taskRequireLcm()));
		pw.println("タスクのデッドライン" + "," + Constant.DEADLINE_INIT * Constant.WAIT_TURN + " ~ " + (Constant.DEADLINE_INIT + Constant.DEADLINE_MAX - 1) * Constant.WAIT_TURN);
//		pw.println("タスクのデッドライン（後半）" + "," + Constant.DEADLINE_INIT_LATTER + " ~ " + (Constant.DEADLINE_INIT_LATTER + Constant.DEADLINE_MAX_LATTER - 1));
		pw.println(Constant.WAIT_TURN + "tickに追加する平均タスク数" + "," + Constant.ADD_TASK_PER_TURN);
//		pw.println("1tickに追加する平均タスク数（後半）" + "," + Constant.ADD_TASK_PER_TURN_LATTER);
//		pw.println("常に存在するときのタスク数" + "," + Constant.ALWAYS_TASK_NUM);
		pw.println("ε-greedyのε" + "," + Constant.EPSILON);
		pw.println("ε-greedyのε（メンバ候補の決定時）" + "," + Constant.EPSILON2);
		pw.println("1サブタスクごとに送るメッセージ数" + "," + Constant.SELECT_MEMBER_NUM);
		pw.println("各状態にかかる時間、タスクを追加する時間間隔" + "," + Constant.WAIT_TURN);
		pw.println("タスクのリソースの最小公倍数" + "," + Constant.taskRequireLcm());
		pw.println("エージェントのタイプ" + "," + agent.getAgentType());
		pw.println("初期タスク選択状態での戦略" + "," + initialMarkingTaskState.getMarkingTaskStrategy());
		pw.println("初期役割選択状態での戦略" + "," + initialRoleSelectState.getRoleSelectStrategy());
		pw.println("リーダ状態での戦略" + "," + leaderState.getLeaderStrategy());
		pw.println("可視化のための閾値を格納する配列要素数" + "," + Constant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM);
		for(int i = 0; i < Constant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM; i++){
			pw.println(i + " 番目" + "," + Constant.TEAM_FORMATION_PERCENTAGE_BORDER[i]);
		}
		
//		pw.print("");
		return pw;
	}
	
	/**
	 * タスク処理リソース量の平均を書き込むためのメソッド（最初）
	 * @param allExperimentNum
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter averageRequireSumFirst(double allExperimentNum) throws IOException{
		File directory = new File("data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/average");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/average/require_average_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.println("平均" + allExperimentNum + "回");
		pw.print("経過ターン");
		pw.print(",");
		pw.print(Constant.MEASURE_TURN_NUM + "ターン毎のタスク処理リソース量");
		pw.print(",");
		pw.print(Constant.MEASURE_TURN_NUM + "ターン毎の廃棄タスクリソース量");
		pw.print(",");
		pw.print(Constant.MEASURE_TURN_NUM + "ターン毎のタスク廃棄数");
		pw.print(",");
		pw.print("チーム編成成功回数");
		pw.print(",");
		pw.print("チーム編成失敗回数");
		pw.print(",");
		pw.print("チーム編成断念回数");
		pw.print(",");
		pw.print("チーム編成合計回数");
		pw.print(",");
		pw.print("1チームあたりの平均拘束時間");
		pw.print(",");
		pw.print("1チーム中の1人あたりの平均拘束時間");
		pw.print(",");
		pw.print("1人あたりの拘束時間");
		pw.print(",");
		pw.println("再割り当てによるチーム編成成功回数");
		
//		pw.print("");
		return pw;
	}
	
	/**
	 * タスク処理量を書き込む
	 * @param pw
	 * @param turn ターン
	 * @param successTaskRequire 25ターン毎のタスク処理量
	 * @param failureTaskRequire 25ターン毎の廃棄タスク処理量
	 * @param failureTaskNum 廃棄タスク数
	 * @param successTeamingCount 25ターン毎のチーム編成成功回数
	 * @param failureTeamingCount 25ターン毎のチーム編成失敗回数
	 * @param giveUpTeamingCount チーム編成断念回数
	 * @param teamingCount チーム編成合計回数
	 * @param constraintTime tターンごとの拘束時間
	 * @param constraintTimePerAgentInATeam 1チーム中の1人あたりのtターンごとの拘束時間
	 * @param constraintTimePerAgent 1人あたりのtターンごとの拘束時間
	 * @param successTeamingCountAgainAllocation 再割り当てを行ってチーム編成が成功した回数
	 * @throws IOException
	 */
	public static void requireSumWrite(PrintWriter pw, int turn, double successTaskRequire, double failureTaskRequire
			, double failureTaskNum, double successTeamingCount, double failureTeamingCount, double giveUpTeamingCount
			, double teamingCount, double constraintTime, double constraintTimePerAgentInATeam
			, double constraintTimePerAgent, double successTeamingCountAgainAllocation) throws IOException{		
		pw.print(turn);
		pw.print(",");
		pw.print(successTaskRequire);
		pw.print(",");
		pw.print(failureTaskRequire);
		pw.print(",");
		pw.print(failureTaskNum);
		pw.print(",");
		pw.print(successTeamingCount);
		pw.print(",");
		pw.print(failureTeamingCount);
		pw.print(",");
		pw.print(giveUpTeamingCount);
		pw.print(",");
		pw.print(teamingCount);
		pw.print(",");
		pw.print(constraintTime);
		pw.print(",");
		pw.print(constraintTimePerAgentInATeam);
		pw.print(",");
		pw.print(constraintTimePerAgent);
		pw.print(",");
		pw.println(successTeamingCountAgainAllocation);

//		pw.print("");
	}
	
	/**
	 * チーム人数ごとのチーム編成成功回数の時間推移を書き込む（最初）
	 * @param allExperimentNum
	 * @throws IOException
	 */
	public static PrintWriter teamingSuccessPerTimeFirst(double allExperimentNum) throws IOException {
		File directory = new File("data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamingSuccess");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamingSuccess/teaming_success_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.println("平均" + allExperimentNum + "回 チーム人数ごとのチーム編成成功回数");
		pw.print("経過ターン");
		pw.print(",");
		for(int i = 1; i < Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1; i++){
			pw.print(i);
			pw.print(",");
		}
		pw.println();
		
		return pw;
	}
	
	/**
	 * チーム人数ごとのチーム編成成功回数の時間推移を書き込む（書き込み）
	 * @param pw
	 * @param turn
	 * @param successTeamingCount
	 * @throws IOException
	 */
	public static void teamingSuccessPerTimeWrite(PrintWriter pw, int turn, double[] successTeamingCount) throws IOException {
		pw.print(turn);
		pw.print(",");
		for(int i = 1; i < Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1; i++){
			pw.print(successTeamingCount[i]/Administrator.EXPERIMENT_NUM);
			pw.print(",");
		}
		pw.println();
	}
	
	/**
	 * 欲張り度を書き込む（最初）
	 * @param agents
	 * @param experimentNumber 実験回数
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter greedyFirst(List<Agent> agents, int experimentNumber) throws IOException{
		File directory = new File("data/greedy/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/greedy/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/greedy_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.println(experimentNumber + "回目");
		pw.print("経過ターン");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
		}
		pw.println();
		
//		pw.print("");
		return pw;
	}
	
	/**
	 * 欲張り度を書き込む
	 * @param pw
	 * @param turn
	 * @param agents
	 * @throws IOException
	 */
	public static void greedyWrite(PrintWriter pw, int turn, List<Agent> agents) throws IOException{
		pw.print(turn);
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i).getGreedy());
			pw.print(",");
		}
		pw.println();
		
//		pw.print("");
	}
	
	/**
	 * 提案受託期待度の書き込み（結果）
	 * @param agents
	 * @param turn
	 * @param experimentNumber 実験回数
	 * @throws IOException
	 */
	public static void trustWrite(List<Agent> agents, int turn, int experimentNumber) throws IOException{
		File directory = new File("data/trust/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/result/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/trust/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/result/trust_" + turn + ".csv", isWrite), "Shift_JIS")));
//		pw.println(experimentNumber + "回目");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
		}
		pw.println();
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
			for(int j = 0; j < Constant.AGENT_NUM; j++){
				pw.print(agents.get(i).getTrust(agents.get(j)));
				pw.print(",");
			}
			pw.println();
		}
		pw.println();
		
//		pw.print("");
		pw.close();
	}
	
	/**
	 * 報酬期待度の書き込み（結果）
	 * @param agents
	 * @param turn
	 * @param experimentNumber 実験回数
	 * @throws IOException
	 */
	public static void expectedRewardWrite(List<Agent> agents, int turn, int experimentNumber) throws IOException{
		File directory = new File("data/expectedReward/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/result/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/expectedReward/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/result/expectedReward_" + turn + ".csv", isWrite), "Shift_JIS")));
//		pw.println(experimentNumber + "回目");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
		}
		pw.println();
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
			for(int j = 0; j < Constant.AGENT_NUM; j++){
				pw.print(agents.get(i).getExpectedReward(agents.get(j)));
				pw.print(",");
			}
			pw.println();
		}
		pw.println();
		
//		pw.print("");
		pw.close();
	}
	
	/**
	 * 一定ターンまでのエージェントの主な役割を返す
	 * @param agent
	 * @return
	 */
	public static String getMainRole(Agent agent){
		if(agent.getLeaderNumPerTurn() > agent.getMemberNumPerTurn() * 2){
			return "leader";
		}
		else if(agent.getLeaderNumPerTurn() * 2 < agent.getMemberNumPerTurn()){
			return "member";
		}
		else{
			return "neither";
		}
	}
	
	/**
	 * 相手とチーム編成をする際に担当する主な役割を返す
	 * @param me
	 * @param you
	 * @return
	 */
	public static String getMainRoleWithYou(Agent me, Agent you){
		if(me.getTeamingWithMemberNumPerTurn(you) > me.getTeamingWithLeaderNumPerTurn(you) * 2){
			return "asLeader";
		}
		else if(me.getTeamingWithMemberNumPerTurn(you) * 2 < me.getTeamingWithLeaderNumPerTurn(you)){
			return "asMember";
		}
		else{
			return "asNeither";
		}
	}
	
	/**
	 * 可視化のための書き込み
	 * ＜有向グラフ用＞
	 * 自分のID | 相手のID | 自分の能力 | 自分の能力の合計 | メンバとのチーム編成回数 | メンバとのチーム編成回数の割合
	 *  | メンバとのチーム編成回数の割合が閾値以上か | 主に担当した役割
	 *  ＜無向グラフ用＞
	 *  自分のID | 相手のID | 自分の能力 | 自分の能力の合計 | 相手とのチーム編成合計回数 | 相手とのチーム編成合計回数の割合
	 *   | 相手とのチーム編成合計回数の割合が閾値以上か | 主に担当した役割
	 * @param agents
	 * @param turn
	 * @param experimentNumber 実験回数
	 * @param successTeamingEdgeNum ターンごとのチーム編成成功回数
	 * @throws IOException
	 */
	public static void visualizationDataWrite(List<Agent> agents, int turn, int experimentNumber, int successTeamingEdgeNum) throws IOException{
		File directory = new File("data/visualization/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/visual/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		//無向グラフ
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/visualization/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/visual/non_directed_" + turn + ".csv", isWrite), "Shift_JIS")));
		pw.print("my_id" + "," + "your_id");
		pw.print(",");
		pw.print("my_ability");
		pw.print(",");
		pw.print("my_ability_sum");
		pw.print(",");
		pw.print("main_role");
		pw.print(",");
		pw.print("leaderNum" + "," + "memberNum");
		pw.print(",");
		pw.print("team_formation_sum");
		pw.print(",");
		pw.print("team_formation_num_as_leader" + "," + "team_formation_num_as_member");
		pw.print(",");
		pw.print("main_role_with_you");
		pw.print(",");
		pw.print("team_formation_sum_percentage");
		pw.print(",");
		pw.print("team_formation_sum_percentage_over_border");
		pw.println();
		
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			Agent me = agents.get(i);
			
			//無向グラフ用
			for(int j = i; j < Constant.AGENT_NUM; j++){
				boolean isWrite = false;	//書き込むかどうか
				double threshold = 0.00;	//可視化の閾値
				Agent you = agents.get(j);
				
				//相手とのチーム編成回数の割合
				double teamingRateWithYou = (double)(me.getTeamingWithLeaderNumPerTurn(you) + me.getTeamingWithMemberNumPerTurn(you)) / (double)successTeamingEdgeNum * 100;
				
				//チーム編成割合がどの閾値よりも下回る場合は書き込まない
				for(int k = 0; k < Constant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM; k++){
					if(teamingRateWithYou > Constant.TEAM_FORMATION_PERCENTAGE_BORDER[k]){
						isWrite = true;
						threshold = Constant.TEAM_FORMATION_PERCENTAGE_BORDER[k];
						break;
					}
				}
				if(!isWrite && i != j){
					continue;
				}
				
				//自分id、相手id
				pw.print(me.getId() + "," + you.getId());
				pw.print(",");
				
				//自分の能力、自分の能力の合計
				pw.print(me.getAbility()[0] + " " + me.getAbility()[1] + "," + me.getAbilitySum());
				pw.print(",");
				
				//主に担当した役割
				pw.print(getMainRole(me));
				pw.print(",");
				
				//リーダ回数、メンバ回数
				pw.print(me.getLeaderNumPerTurn() + "," + me.getMemberNumPerTurn());
				pw.print(",");
				
				if(j != i){
					//相手とのチーム編成合計回数
					pw.print(me.getTeamingWithLeaderNumPerTurn(you) + me.getTeamingWithMemberNumPerTurn(you));
					pw.print(",");
					
					//相手とのリーダとしての回数、相手とのメンバとしての回数
					pw.print(me.getTeamingWithMemberNumPerTurn(you) + "," + me.getTeamingWithLeaderNumPerTurn(you));
					pw.print(",");
					
					//相手とチーム編成をする際に担当する主な役割
					pw.print(getMainRoleWithYou(me, you));
					pw.print(",");

					//相手とのチーム編成合計回数の割合
					pw.print(teamingRateWithYou);
					pw.print(",");
					
					//チーム編成合計回数の割合が閾値以上か（パーセンテージ）
					pw.print("more_than_" + threshold);
				}
//				else{
//					pw.print("0" + "," + "0" + "," + "0" + "," + "0" + "," + "0");
//				}
				
				pw.println();
			}
		}

		pw.close();
	}
	
	/**
	 * 可視化の際の補足的データ
	 * @param agents
	 * @param turn
	 * @param experimentNumber
	 * @param successTeamingEdgeNum
	 * @throws IOException
	 */
	public static void visualizationTeamingDataWrite(List<Agent> agents, int turn) throws IOException{
		File directory = new File("data/visualization/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/data/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/visualization/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/data/teaming_" + turn + ".csv", isWrite), "Shift_JIS")));
		pw.println(turn + "ターン目");
		pw.print("エージェントID");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent.getId());
		}
		pw.println();
		pw.print("リソース");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent.getAbility()[0] + " " + agent.getAbility()[1]);
		}
		pw.println();
		pw.print("リーダ回数");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent.getLeaderNumPerTurn());
		}
		pw.println();
		pw.print("メンバ回数");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent.getMemberNumPerTurn());
		}
		pw.println();
		pw.print("合計回数");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent.getLeaderNumPerTurn() + agent.getMemberNumPerTurn());
		}
		pw.println();
		//それぞれの役割担当エージェント数
		int leaders = 0;
		int members = 0;
		int neithers = 0;
		pw.print("主な役割");
		for(Agent agent : agents){
			String role = getMainRole(agent);
			pw.print(",");
			pw.print(role);
			if(role.equals("leader")){
				leaders++;
			}
			else if(role.equals("member")){
				members++;
			}
			else{
				neithers++;
			}
		}
		pw.println();
		
		pw.println();
		pw.println("リーダ数" + "," + leaders);
		pw.println("メンバ数" + "," + members);
		pw.println("どちらでもない数" + "," + neithers); 
		
		pw.close();
	}
	
	/**
	 * 役割の担当回数の書き込み（最初）
	 * @param agents
	 * @param experimentNumber 実験回数
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter roleNumberFirst(List<Agent> agents, int experimentNumber) throws IOException{
		File directory = new File("data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/role/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/role/roleNumber_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.println(experimentNumber + "回目");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
		}
		pw.println();
		
//		pw.print("");
		return pw;
	}
	
	/**
	 * 役割の担当回数、各状態にかけた時間の合計の書き込み
	 * @param pw
	 * @param agents
	 * @throws IOException
	 */
	public static void roleNumberWrite(PrintWriter pw, List<Agent> agents) throws IOException{
		pw.print("Leader");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i).getLeaderNum());
			pw.print(",");
		}
		pw.println();
		pw.print("Member");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i).getMemberNum());
			pw.print(",");
		}
		pw.println();
		pw.println();
		pw.print("初期状態にかけた時間");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i).getInitialStateTime());
			pw.print(",");
		}
		pw.println();
		pw.print("リーダ状態にかけた時間");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i).getLeaderStateTime());
			pw.print(",");
		}
		pw.println();
		pw.print("メンバ状態にかけた時間");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i).getMemberStateTime());
			pw.print(",");
		}
		pw.println();
		pw.print("実行状態にかけた時間");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i).getExecuteStateTime());
			pw.print(",");
		}
		pw.println();
		pw.print("1回あたりの実行状態において無駄に拘束された時間");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print((double)agents.get(i).getExecuteStateConstraintTime() / (double)(agents.get(i).getLeaderNum() + agents.get(i).getMemberNum()));
			pw.print(",");
		}
		pw.println();
		
//		pw.print("");
	}
	
	/**
	 * 各エージェントとのチーム編成回数を記録
	 * @param agents
	 * @throws IOException
	 */
	public static void teamingNumWrite(List<Agent> agents) throws IOException{
		File directory = new File("data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teaming/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teaming/teamingNumber_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		
		/* メンバとのチーム編成回数を書き込む */
		pw.print("," + "Leader" + ",");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent);
		}
		pw.println();
		for(Agent me : agents){
			pw.print(me);
			pw.print("," + me.getLeaderNum() + ",");
			
			for(Agent you : agents){
				pw.print(",");
				pw.print(me.getTeamingWithMemberNumSum(you));
			}
			pw.println();
		}
		pw.println();
		
		/* リーダとのチーム編成回数を書き込む */
		pw.print("," + "Member" + ",");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent);
		}
		pw.println();
		for(Agent me : agents){
			pw.print(me);
			pw.print("," + me.getMemberNum() + ",");
			
			for(Agent you : agents){
				pw.print(",");
				pw.print(me.getTeamingWithLeaderNumSum(you));
			}
			pw.println();
		}
		
		pw.close();
	}
	
	/**
	 * エージェントの保持しているチーム平均リソースの書き込み（最初）
	 * @param agents
	 * @param experimentNumber
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter teamResourceAverageFirst(List<Agent> agents, int experimentNumber) throws IOException{
		File directory = new File("data/team/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/team/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamResourceAverage_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.println(experimentNumber + "回目");
		pw.print("ターン" + ",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
		}
		pw.println();

		return pw;
	}
	
	/**
	 * エージェントのチーム平均リソースを書き込む
	 * １人あたりの平均リソースに変更
	 * @param pw
	 * @param turn
	 * @param agents
	 * @throws IOException
	 */
	public static void teamResourceAverageWrite(PrintWriter pw, int turn, List<Agent> agents) throws IOException{
		pw.print(turn);
		pw.print(",");
		for(Agent agent : agents){
			pw.print(agent.getAverageTeamResource());
			pw.print(",");
		}
		pw.println();
	}
	
	/**
	 * タスクキューの書き込み（最初）
	 * @param experimentNumber 実験回数
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter taskQueueFirst(int experimentNumber) throws IOException{
		File directory = new File("data/queue/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/queue/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/taskQueue_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.println(experimentNumber + "回目");
		pw.print("経過ターン");
		pw.print(",");
		pw.print("キュー内のタスク数");
		pw.print(",");
		pw.println("マークなしのキュー内のタスク数");
		
//		pw.print("");
		return pw;
	}
	
	/**
	 * タスクキューの書き込み
	 * @param pw
	 * @param turn
	 * @param queue
	 * @throws IOException
	 */
	public static void taskQueueWrite(PrintWriter pw, int turn, List<Task> queue) throws IOException{
		pw.print(turn);
		pw.print(",");
		pw.print(queue.size());
		pw.print(",");
		pw.print(Main.getNoMarkTaskQueueSize());
		pw.print(",");
		for(Task task : queue){
			if(!task.getMark()){
				pw.print(task);
				pw.print(",");
			}
		}
		pw.println();
		
//		pw.print("");
	}
	
	/**
	 * 各状態にいるエージェント数を書き込む（最初）
	 * @param experimentNumber
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter agentStateNumFirst(int experimentNumber) throws IOException{
		File directory = new File("data/state/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/state/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/agentStateNum_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.println(experimentNumber + "回目");
		pw.print("経過ターン");
		pw.print(",");
		pw.println("キュー内のマークなしタスク数"  + "," + "暇なエージェント数" + "," + "リーダorメンバのエージェント数" + "," + "実行状態のエージェント数");
		
		return pw;
	}
	
	/**
	 * 暇、リーダorメンバ、忙しいエージェント数を書き込む
	 * @param pw
	 * @param turn
	 * @param noMarkTaskQueueSize
	 * @param stateAgentNum
	 * @throws IOException
	 */
	public static void agentStateNumWrite(PrintWriter pw, int turn, int noMarkTaskQueueSize, int[] stateAgentNum) throws IOException{
		pw.print(turn);
		pw.print(",");
		pw.println(noMarkTaskQueueSize + "," + stateAgentNum[Constant.FREE_STATE] + "," + stateAgentNum[Constant.BUSY_LEADER_OR_MEMBER_STATE] + "," + stateAgentNum[Constant.BUSY_WORK_STATE]);
	}
	
	/**
	 * チーム数のヒストグラム用のデータを書き込む（最初）
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter teamNumHistogramFirst() throws IOException{
		File directory = new File("data/histogram/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/histogram/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamNumHistogram_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.print("人数" + "," + "タスク処理時間" + "," + "1タスクのリソース量" + "," + "1タスク中のサブタスク数");
		pw.print(",");
		pw.println("リソース合計" + "," + "1人あたりのリソース" + "," + "リーダのリソース" + "," + "メンバ1人あたりのリソース");
		
		return pw;
	}
	
	/**
	 * チーム数のヒストグラムを書き込む
	 * @param pw
	 * @param team
	 * @param task
	 * @throws IOException
	 */
	public static void teamNumHistogramWrite(PrintWriter pw, Team team, Task task) throws IOException{
		pw.print(team.getSize() + "," + team.getExecuteTime() + "," + task.getTaskRequireSum() + "," + task.getSubTaskNum());
		pw.print(",");
		pw.println(team.getTeamResourceSum() + "," + (double)team.getTeamResourceSum() / (double)team.getSize() + "," + team.getLeaderResource() + "," + (double)team.getMemberResource() / (double)(team.getSize() - 1));
	}
	
	/**
	 * その他の情報を書き込む
	 * @param temporaryTeamSizeAverage 仮チームの平均サイズ
	 * @param teamSizeAverage チームの平均サイズ
	 * @param teamExecuteTimeAverage チームでの平均タスク処理時間
	 * @param teamingSuccess チームでのタスク処理成功数
	 * @param teamConstraintTimeDifferenceAverage チーム拘束時間の差分平均
	 * @param leaderMainAverage 主にリーダを担当したエージェント数
	 * @param memberMainAverage 主にメンバを担当したエージェント数
	 * @param neitherLeaderNorMemberAverage どちらでもないエージェント数
	 * @param initialStateTimeAverage 初期状態にかけた時間
	 * @param leaderStateTimeAverage リーダ状態にかけた時間
	 * @param memberStateTimeAverage メンバ状態にかけた時間
	 * @param executeStateTimeAverage 実行状態にかけた時間
	 * @param executeStateTimeOnceTimeAverage 1回のチーム編成にかけた時間
	 * @param executeStateExecutingTimeAverage 1回のチーム編成における実行状態において処理していた時間
	 * @param executeStateConstraintTimeAverage 1回のチーム編成における実行状態において無駄に拘束された時間
	 * @param executeStateExecutingTimeAtEndAverage 最後の数ターンの処理していた時間
	 * @param executeStateConstraintTimeAtEndAverage 最後の数ターンの拘束されていた時間
	 * @param executingTimePerAgentInATeamAtEndAverage 1チーム中の1人あたりの最後の数ターンに処理していた時間
	 * @param constraintTimePerAgentInATeamAtEndAverage 1チーム中の1人あたりの最後の数ターンに拘束されていた時間
	 * @param allSuccessTaskRequireAvarage 総タスク処理リソース量の平均
	 * @param allFailureTaskRequireAverage 総タスク廃棄リソース量の平均
	 * @param stateAgentNum 暇、リーダorメンバ、仕事中のエージェント数の平均
	 * @param stateAgentResources 暇、リーダorメンバ、仕事中の1人あたりのエージェントリソースの平均
	 * @throws IOException
	 */
	public static void otherInfoWrite(double temporaryTeamSizeAverage, double teamSizeAverage, double teamExecuteTimeAverage
			, double[] teamingSuccess, double[] teamConstraintTimeDifferenceAverage, double leaderMainAverage
			, double memberMainAverage, double neitherLeaderNorMemberAverage, double initialStateTimeAverage
			, double leaderStateTimeAverage, double memberStateTimeAverage, double executeStateTimeAverage
			, double executeStateTimeOnceTimeAverage, double executeStateExecutingTimeAverage
			, double executeStateConstraintTimeAverage, double executeStateExecutingTimeAtEndAverage
			, double executeStateConstraintTimeAtEndAverage, double executingTimePerAgentInATeamAtEndAverage
			, double constraintTimePerAgentInATeamAtEndAverage, double allSuccessTaskRequireAvarage
			, double allFailureTaskRequireAverage, double stateAgentNum[], double[] stateAgentResources) throws IOException {
		File directory = new File("data/other/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/other/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/otherInfo_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		
		pw.println("仮チームの平均サイズ" + "," + temporaryTeamSizeAverage);
		pw.println("チームの平均サイズ" + "," + teamSizeAverage);
		pw.println("チームの平均処理時間" + "," + teamExecuteTimeAverage);
		pw.println("主にリーダを担当したエージェント数" + "," + leaderMainAverage);
		pw.println("主にメンバを担当したエージェント数" + "," + memberMainAverage);
		pw.println("主な役割がリーダでもメンバでもないエージェント数" + "," + neitherLeaderNorMemberAverage);
		pw.println("1人あたりの初期状態にかけた時間" + "," + initialStateTimeAverage);
		pw.println("1人あたりのリーダ状態にかけた時間" + "," + leaderStateTimeAverage);
		pw.println("1人あたりのメンバ状態にかけた時間" + "," + memberStateTimeAverage);
		pw.println("1人あたりの実行状態にかけた時間" + "," + executeStateTimeAverage);
		pw.println("1人あたりの1回の実行状態にかけた時間" + "," + executeStateTimeOnceTimeAverage);
		pw.println("1人あたりの1回のチーム編成の実行状態において処理していた時間" + "," + executeStateExecutingTimeAverage);
		pw.println("1人あたりの1回のチーム編成の実行状態において無駄に拘束された時間" + "," + executeStateConstraintTimeAverage);
		pw.println("1人あたりの最後の数ターンに処理していた時間" + "," + executeStateExecutingTimeAtEndAverage);
		pw.println("1人あたりの最後の数ターンに拘束されていた時間" + "," + executeStateConstraintTimeAtEndAverage);
		pw.println("1チーム中の1人あたりの最後の数ターンに処理していた時間" + "," + executingTimePerAgentInATeamAtEndAverage);
		pw.println("1チーム中の1人あたりの最後の数ターンに拘束されていた時間" + "," + constraintTimePerAgentInATeamAtEndAverage);
		pw.println("総タスク処理リソースの平均" + "," + allSuccessTaskRequireAvarage);
		pw.println("総タスク廃棄リソースの平均" + "," + allFailureTaskRequireAverage);
		pw.println("暇な（初期状態）エージェント数の平均" + "," + stateAgentNum[Constant.FREE_STATE]/Administrator.EXPERIMENT_NUM);
		pw.println("忙しい（リーダorメンバ）エージェント数の平均" + "," + stateAgentNum[Constant.BUSY_LEADER_OR_MEMBER_STATE]/Administrator.EXPERIMENT_NUM);
		pw.println("忙しい（実行状態）エージェント数の平均" + "," + stateAgentNum[Constant.BUSY_WORK_STATE]/Administrator.EXPERIMENT_NUM);
		pw.println("暇な（初期状態）エージェントの1人あたりのリソースの平均" + "," + stateAgentResources[Constant.FREE_STATE]/Administrator.EXPERIMENT_NUM);
		pw.println("忙しい（リーダorメンバ）エージェントの1人あたりのリソースの平均" + "," + stateAgentResources[Constant.BUSY_LEADER_OR_MEMBER_STATE]/Administrator.EXPERIMENT_NUM);
		pw.println("忙しい（実行状態）エージェントの1人あたりのリソースの平均" + "," + stateAgentResources[Constant.BUSY_WORK_STATE]/Administrator.EXPERIMENT_NUM);
		pw.println();
		
		pw.println("チーム内人数" + "," + "拘束時間の差分" + "," + "1人あたりの平均差分" + "," + "チーム編成成功数");
		for(int i = 0; i < teamConstraintTimeDifferenceAverage.length; i++){
			pw.println(i + "," + teamConstraintTimeDifferenceAverage[i] + "," + teamConstraintTimeDifferenceAverage[i] / (double)(i-1) + "," + teamingSuccess[i]/Administrator.EXPERIMENT_NUM);
		}
		
		pw.close();
	}

}
