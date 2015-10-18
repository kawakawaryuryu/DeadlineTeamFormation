package fixed.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import fixed.agent.FixedAgent;
import fixed.role.Role;
import fixed.strategy.StrategyManager;
import fixed.strategy.memberselection.TentativeMemberSelectionStrategy;
import fixed.strategy.roleselection.FixedRoleSelectionStrategy;
import fixed.strategy.subtaskallocation.SubtaskAllocationStrategy;
import fixed.strategy.taskselection.FixedTaskSelectionStrategy;
import fixed.constant.FixedConstant;
import fixed.task.FixedTask;
import fixed.team.FixedTeam;

public class FileWriteManager {
	static int fileNumber = 8;	//ファイルのnumber
	static boolean isWrite = false;	//追加か上書きか
	static String path = "../../Dropbox/research/";
	
	/**
	 * ファイルの内容説明の書き込み
	 * @return
	 * @throws IOException
	 */
	public static void fileExplain() throws IOException{
		FixedTaskSelectionStrategy taskSelectionStrategy = StrategyManager.getTaskSelectionStrategy();
		FixedRoleSelectionStrategy roleSelectionStrategy = StrategyManager.getRoleSelectionStrategy();
		SubtaskAllocationStrategy allocationStrategy = StrategyManager.getAllocationStrategy();
		TentativeMemberSelectionStrategy memberSelectionStrategy = StrategyManager.getMemberSelectionStrategy();
		
		File directory = new File(path + "data/file/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "data/file/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/file_" + fileNumber + ".csv", false), "Shift_JIS")));
		pw.println("ファイル名" + "," + FixedConstant.TURN_NUM + "turn_" + FixedConstant.AGENT_NUM + "agents_" + fileNumber + ".csv");
		pw.println("FIFO、マークあり、チームの履歴を保持、学習あり、タスク処理の見積もりを行う、1tickごとの獲得報酬で報酬期待度を学習、リーダにはまず1個だけ割り当てる");
		pw.println("ターン" + "," + FixedConstant.TURN_NUM);
		pw.println("試行回数" + "," + MainMain.EXPERIMENT_NUM);
		pw.println("エージェント数" + "," + FixedConstant.AGENT_NUM);
		pw.println("リソースの種類数" + "," + FixedConstant.RESOURCE_NUM);
		pw.println("欲張り度の初期値" + "," + FixedConstant.INITIAL_GREEDY);
		pw.println("提案受託期待度の初期値" + "," + FixedConstant.INITIAL_TRUST);
		pw.println("報酬期待度の初期値" + "," + FixedConstant.INITIAL_EXPECTED_REWARD);
		pw.println("欲張り度の学習率" + "," + FixedConstant.LEARN_RATE_GREEDY);
		pw.println("提案受託期待度の学習率" + "," + FixedConstant.LEARN_RATE_TRUST);
		pw.println("報酬期待度の学習率" + "," + FixedConstant.LEARN_RATE_REWARD);
		pw.println("チーム履歴を保持する数" + "," + FixedConstant.PAST_TEAM_NUM);
		pw.println("タスクの中のサブタスク数" + "," + FixedConstant.SUBTASK_IN_TASK_INIT + "　~　" + (FixedConstant.SUBTASK_IN_TASK_INIT + FixedConstant.SUBTASK_IN_TASK_NUM - 1));
		pw.println("エージェントのリソース" + "," + FixedConstant.AGENT_ABILITY_INIT + "　~　" + (FixedConstant.AGENT_ABILITY_INIT + FixedConstant.AGENT_ABILITY_MAX - 1));
		pw.println("タスクのリソース" + "," + (FixedConstant.TASK_REQUIRE_INIT * FixedConstant.TASK_REQUIRE_MALTIPLE) + " ~　" + ((FixedConstant.TASK_REQUIRE_INIT + FixedConstant.TASK_REQUIRE_MAX - 1) * FixedConstant.TASK_REQUIRE_MALTIPLE));
		pw.println("タスクのデッドライン" + "," + FixedConstant.DEADLINE_INIT + " ~ " + (FixedConstant.DEADLINE_INIT + FixedConstant.DEADLINE_MAX - 1));
		pw.println("1tickに追加する平均タスク数" + "," + FixedConstant.ADD_TASK_PER_TURN);
		pw.println("ε-greedyのε" + "," + FixedConstant.EPSILON);
		pw.println("ε-greedyのε（メンバ候補の決定時）" + "," + FixedConstant.EPSILON2);
		pw.println("1サブタスクごとに送るメッセージ数" + "," + FixedConstant.SELECT_MEMBER_NUM);
		pw.println("タスク選択の戦略" + "," + taskSelectionStrategy.toString());
		pw.println("役割選択の戦略" + "," + roleSelectionStrategy.toString());
		pw.println("サブタスク割り当ての戦略" + "," + allocationStrategy.toString());
		pw.println("仮メンバ選択の戦略" + "," + memberSelectionStrategy.toString());
		pw.println("可視化のための閾値を格納する配列要素数" + "," + FixedConstant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM);
		for(int i = 0; i < FixedConstant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM; i++){
			pw.println(i + " 番目" + "," + FixedConstant.TEAM_FORMATION_PERCENTAGE_BORDER[i]);
		}
		
//		pw.print("");
		pw.close();
	}
	
	/**
	 * 時間ごとの計測データのヘッダを書き込む
	 * @param allExperimentNum
	 * @return
	 * @throws IOException
	 */
	private static PrintWriter writeHeaderOfMeasuredDataPerTurn(int allExperimentNum) throws IOException{
		File directory = new File(path + "data/TaskRequireResult/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/average");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/TaskRequireResult/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/average/require_average_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.println("平均" + allExperimentNum + "回");
		pw.print("経過ターン");
		pw.print(",");
		pw.print(FixedConstant.MEASURE_TURN_NUM + "ターン毎のタスク処理リソース量");
		pw.print(",");
		pw.print(FixedConstant.MEASURE_TURN_NUM + "ターン毎の廃棄タスクリソース量");
		pw.print(",");
		pw.print(FixedConstant.MEASURE_TURN_NUM + "ターン毎のタスク廃棄数");
		pw.print(",");
		pw.print("チーム編成成功回数");
		pw.print(",");
		pw.print("チーム編成失敗回数");
		pw.print(",");
		pw.print("チーム編成断念回数");
		pw.print(",");
		pw.print("チーム編成合計回数");
		pw.print(",");
		pw.print("1チーム中の不要な平均拘束時間");
		pw.print(",");
		pw.print("1チーム中の1人あたりの平均処理時間");
		pw.print(",");
		pw.println("1チーム中の1人あたりの不要な平均拘束時間");
		
		return pw;
	}
	
	/**
	 * 時間ごとの計測を書き込む
	 * @throws IOException
	 */
	public static void writeBodyOfMeasuredDataPerTurn() throws IOException{	
		PrintWriter pw = writeHeaderOfMeasuredDataPerTurn(MainMain.EXPERIMENT_NUM);
		
		for(int i = 0; i < FixedConstant.ARRAY_SIZE_FOR_MEASURE; i++){
			int turn = FixedConstant.MEASURE_TURN_NUM * (i + 1);
			pw.print(turn);
			pw.print(",");
			pw.print(MainMain.measure.successTaskRequire[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(MainMain.measure.failureTaskRequire[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(MainMain.measure.failureTaskNum[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(MainMain.measure.successTeamFormationNum[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(MainMain.measure.failureTeamFormationNum[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(MainMain.measure.giveUpTeamFormationNum[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(MainMain.measure.tryingTeamFormationNum[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(MainMain.measure.bindingTimeInTeam[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(MainMain.measure.executingTimePerAgentInTeam[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.println(MainMain.measure.bindingTimePerAgentInTeam[i] / (double)MainMain.EXPERIMENT_NUM);
		}

		pw.close();
	}
	
	/**
	 * チーム人数ごとのチーム編成成功回数の時間推移を書き込む（最初）
	 * @param allExperimentNum
	 * @throws IOException
	 */
	private static PrintWriter writeHeaderOfTeamMeasuredData(int allExperimentNum) throws IOException {
		File directory = new File(path + "data/TaskRequireResult/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/teamingSuccess");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/TaskRequireResult/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/teamingSuccess/teaming_success_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.println("平均" + allExperimentNum + "回 チーム人数ごとのチーム編成成功回数");
		pw.print("経過ターン");
		pw.print(",");
		for(int i = 1; i < FixedConstant.ARRAY_SIZE_FOR_TEAM; i++){
			pw.print(i);
			pw.print(",");
		}
		pw.println();
		
		return pw;
	}
	
	/**
	 * チーム人数ごとのチーム編成成功回数の時間推移を書き込む（書き込み）
	 * @throws IOException
	 */
	public static void writeBodyOfTeamMeasuredData() throws IOException {
		PrintWriter pw = writeHeaderOfTeamMeasuredData(MainMain.EXPERIMENT_NUM);
		
		for(int i = 0; i < FixedConstant.ARRAY_SIZE_FOR_MEASURE; i++){
			int turn = FixedConstant.ARRAY_SIZE_FOR_MEASURE * (i + 1);
			pw.print(turn);
			pw.print(",");
			for(int j = 0; j < FixedConstant.ARRAY_SIZE_FOR_TEAM; j++){
				pw.print(MainMain.measure.successTeamFormationNumEveryTeamSize[i][j] / (double)MainMain.EXPERIMENT_NUM);
				pw.print(",");
			}
			pw.println();
		}
		pw.close();
	}
	
	/**
	 * 欲張り度を書き込む（最初）
	 * @param agents
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter writeHeaderOfGreedy(ArrayList<FixedAgent> agents) throws IOException{
		File directory = new File(path + "data/greedy/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/greedy/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/greedy_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.print("経過ターン");
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
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
	public static void writeBodyOfGreedy(PrintWriter pw, int turn, ArrayList<FixedAgent> agents) throws IOException{
		pw.print(turn);
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print(agents.get(i).getGreedy());
			pw.print(",");
		}
		pw.println();
		
//		pw.print("");
	}
	
	/**
	 * 信頼度の書き込み
	 * @param agents
	 * @param turn
	 * @throws IOException
	 */
	public static void writeTrust(ArrayList<FixedAgent> agents, int turn) throws IOException{
		File directory = new File(path + "data/trust/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/" + fileNumber + "/result/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/trust/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/" + fileNumber + "/result/trust_" + turn + ".csv", isWrite), "Shift_JIS")));

		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
		}
		pw.println();
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
			for(int j = 0; j < FixedConstant.AGENT_NUM; j++){
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
	 * 報酬期待度の書き込み
	 * @param agents
	 * @param turn
	 * @throws IOException
	 */
	public static void writeRewardExpectation(ArrayList<FixedAgent> agents, int turn) throws IOException{
		File directory = new File(path + "data/expectedReward/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/" + fileNumber + "/result/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/expectedReward/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/" + fileNumber + "/result/expectedReward_" + turn + ".csv", isWrite), "Shift_JIS")));
//		pw.println(experimentNumber + "回目");
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
		}
		pw.println();
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
			for(int j = 0; j < FixedConstant.AGENT_NUM; j++){
				pw.print(agents.get(i).getRewardExpectation(agents.get(j)));
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
	 * @throws Exception 
	 */
	public static String getStringMainRole(FixedAgent agent) throws Exception{
		if(agent.getParameter().getElement(Role.LEADER).getRoleNum() > agent.getParameter().getElement(Role.MEMBER).getRoleNum() * 2){
			return "leader";
		}
		else if(agent.getParameter().getElement(Role.LEADER).getRoleNum() * 2 < agent.getParameter().getElement(Role.MEMBER).getRoleNum()){
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
	public static String getStringMainRoleWithYou(FixedAgent me, FixedAgent you){
		if(me.getMeasure().getTeamFormationNumWithMember(you) > me.getMeasure().getTeamFormationNumWithLeader(you) * 2){
			return "asLeader";
		}
		else if(me.getMeasure().getTeamFormationNumWithMember(you) * 2 < me.getMeasure().getTeamFormationNumWithLeader(you)){
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
	/*public static void writeVisualizedData(ArrayList<FixedAgent> agents, int turn, int experimentNumber, int successTeamingEdgeNum) throws IOException{
		File directory = new File(path + "data/visualization/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/visual/");
		 ディレクトリが存在しない場合はディレクトリを作成 
		if(!directory.exists()){
			directory.mkdirs();
		}
		//無向グラフ
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "data/visualization/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/visual/non_directed_" + turn + ".csv", isWrite), "Shift_JIS")));
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
			FixedAgent me = agents.get(i);
			
			//無向グラフ用
			for(int j = i; j < Constant.AGENT_NUM; j++){
				boolean isWrite = false;	//書き込むかどうか
				double threshold = 0.00;	//可視化の閾値
				FixedAgent you = agents.get(j);
				
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
				pw.print(me.getAbility(0) + " " + me.getAbility(1) + "," + me.getAbilitySum());
				pw.print(",");
				
				//主に担当した役割
				pw.print(getStringMainRole(me));
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
					pw.print(getStringMainRoleWithYou(me, you));
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
	}*/
	
	/**
	 * 可視化の際の補足的データ
	 * @param agents
	 * @param turn
	 * @param experimentNumber
	 * @param successTeamingEdgeNum
	 * @throws IOException
	 */
	/*public static void writeVisualizedMoreData(ArrayList<FixedAgent> agents, int turn) throws IOException{
		File directory = new File(path + "data/visualization/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/data/");
		 ディレクトリが存在しない場合はディレクトリを作成 
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/visualization/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber + "/data/teaming_" + turn + ".csv", isWrite), "Shift_JIS")));
		pw.println(turn + "ターン目");
		pw.print("エージェントID");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getId());
		}
		pw.println();
		pw.print("リソース");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getAbility(0) + " " + agent.getAbility(1));
		}
		pw.println();
		pw.print("リーダ回数");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getLeaderNumPerTurn());
		}
		pw.println();
		pw.print("メンバ回数");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getMemberNumPerTurn());
		}
		pw.println();
		pw.print("合計回数");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getLeaderNumPerTurn() + agent.getMemberNumPerTurn());
		}
		pw.println();
		//それぞれの役割担当エージェント数
		int leaders = 0;
		int members = 0;
		int neithers = 0;
		pw.print("主な役割");
		for(FixedAgent agent : agents){
			String role = getStringMainRole(agent);
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
	}*/
	
	/**
	 * 役割の担当回数の書き込み（最初）
	 * @param agents
	 * @return
	 * @throws IOException
	 */
	private static PrintWriter writeHeaderOfRoleNumber(ArrayList<FixedAgent> agents) throws IOException{
		File directory = new File(path + "data/role/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/role/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/role/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/role/roleNumber_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print(agents.get(i));
			pw.print(",");
		}
		pw.println();
		
//		pw.print("");
		return pw;
	}
	
	/**
	 * 役割の担当回数、各状態にかけた時間の合計の書き込み
	 * @param agents
	 * @param exprimentNumber TODO
	 * @throws Exception 
	 */
	public static void writeBodyOfRoleNumber(ArrayList<FixedAgent> agents) throws IOException{
		PrintWriter pw = writeHeaderOfRoleNumber(agents);
		pw.print("Leader");
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print(agents.get(i).getParameter().getElement(Role.LEADER).getRoleNum());
			pw.print(",");
		}
		pw.println();
		pw.print("Member");
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print(agents.get(i).getParameter().getElement(Role.MEMBER).getRoleNum());
			pw.print(",");
		}
		pw.println();
		pw.close();
		/*pw.println();
		pw.print("初期状態にかけた時間割合");
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print((double)agents.get(i).getParameter().getElement(Role.INITIAL).getStateTime() / (double)FixedConstant.TURN_NUM * 100);
			pw.print(",");
		}
		pw.println();
		pw.print("リーダ状態にかけた時間割合");
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print((double)agents.get(i).getParameter().getElement(Role.LEADER).getStateTime() / (double)FixedConstant.TURN_NUM * 100);
			pw.print(",");
		}
		pw.println();
		pw.print("メンバ状態にかけた時間割合");
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print((double)agents.get(i).getParameter().getElement(Role.MEMBER).getStateTime() / (double)FixedConstant.TURN_NUM * 100);
			pw.print(",");
		}
		pw.println();
		pw.print("実行状態にかけた時間割合");
		pw.print(",");
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			pw.print((double)agents.get(i).getParameter().getElement(Role.EXECUTE).getStateTime() / (double)FixedConstant.TURN_NUM * 100);
			pw.print(",");
		}*/
		
//		pw.print("");
	}
	
	/**
	 * 各エージェントとのチーム編成回数を記録
	 * @param agents
	 * @throws Exception 
	 */
	public static void writeTeamFormationWithAgent(ArrayList<FixedAgent> agents) throws IOException{
		File directory = new File(path + "data/role/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/teaming/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/role/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/teaming/teamingNumber_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		
		/* メンバとのチーム編成回数を書き込む */
		pw.print("," + "Leader" + ",");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent);
		}
		pw.println();
		for(FixedAgent me : agents){
			pw.print(me);
			pw.print("," + me.getParameter().getElement(Role.LEADER).getRoleNum() + ",");
			
			for(FixedAgent you : agents){
				pw.print(",");
				pw.print(me.getMeasure().getTeamFormationNumWithMember(you));
			}
			pw.println();
		}
		pw.println();
		
		/* リーダとのチーム編成回数を書き込む */
		pw.print("," + "Member" + ",");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent);
		}
		pw.println();
		for(FixedAgent me : agents){
			pw.print(me);
			pw.print("," + me.getParameter().getElement(Role.LEADER).getRoleNum() + ",");
			
			for(FixedAgent you : agents){
				pw.print(",");
				pw.print(me.getMeasure().getTeamFormationNumWithLeader(you));
			}
			pw.println();
		}
		
		pw.close();
	}
	
	/**
	 * チーム数のヒストグラム用のデータを書き込む（最初）
	 * @return
	 * @throws IOException
	 */
	private static PrintWriter writeHeaderOfTeamFormationHistogramData() throws IOException{
		File directory = new File(path + "data/histogram/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/histogram/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/teamNumHistogram_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		pw.print("人数" + "," + "タスク処理時間" + "," + "1タスクのリソース量" + "," + "1タスク中のサブタスク数");
		pw.print(",");
		pw.println("リソース合計" + "," + "1人あたりのリソース" + "," + "リーダのリソース" + "," + "メンバ1人あたりのリソース");
		
		return pw;
	}
	
	/**
	 * チーム数のヒストグラムを書き込む
	 * @param team
	 * @param task
	 * @throws IOException
	 */
	public static void writeBodyOfTeamFormationHistogramData(FixedTeam team, FixedTask task) throws IOException{
		PrintWriter pw = writeHeaderOfTeamFormationHistogramData();
		pw.print(team.getTeamMate().size() + "," + team.getTeamExecuteTime() + "," + task.getTaskRequireSum() + "," + task.getSubTaskNum());
		pw.print(",");
		pw.println(team.getTeamResourceSum() + "," + (double)team.getTeamResourceSum() / (double)team.getTeamMate().size() + "," + team.getLeaderResource() + "," + team.getMemberResourcePerAgent());
	}
	
	/**
	 * その他の情報を書き込む
	 * @throws IOException
	 */
	public static void writeOtherData() throws IOException {
		File directory = new File(path + "data/other/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/other/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/otherInfo_" + fileNumber + ".csv", isWrite), "Shift_JIS")));
		
		pw.println("仮チームの平均サイズ" + "," + MainMain.measure.tentativeTeamSize / (double)MainMain.EXPERIMENT_NUM);
		pw.println("チームの平均サイズ" + "," + MainMain.measure.teamSize / (double)MainMain.EXPERIMENT_NUM);
		pw.println("チームの平均処理時間" + "," + MainMain.measure.teamExecuteTime / (double)MainMain.EXPERIMENT_NUM);
		pw.println("主にリーダを担当したエージェント数" + "," + (double)MainMain.measure.leaderMain / (double)MainMain.EXPERIMENT_NUM);
		pw.println("主にメンバを担当したエージェント数" + "," + (double)MainMain.measure.memberMain / (double)MainMain.EXPERIMENT_NUM);
		pw.println("主な役割がリーダでもメンバでもないエージェント数" + "," + (double)MainMain.measure.neitherLeaderNorMember / (double)MainMain.EXPERIMENT_NUM);
		pw.println("1人あたりの初期状態にかけた時間" + "," + (double)MainMain.measure.initialTime / (double)MainMain.EXPERIMENT_NUM);
		pw.println("1人あたりのリーダ状態にかけた時間" + "," + (double)MainMain.measure.leaderTime / (double)MainMain.EXPERIMENT_NUM);
		pw.println("1人あたりのメンバ状態にかけた時間" + "," + (double)MainMain.measure.memberTime / (double)MainMain.EXPERIMENT_NUM);
		pw.println("1人あたりの実行状態にかけた時間" + "," + (double)MainMain.measure.executeTime / (double)MainMain.EXPERIMENT_NUM);
		pw.println("1チーム中の1人あたりの最後の数ターンに処理していた時間" + "," + MainMain.measure.executingTimePerAgentInTeamAtEnd / (double)MainMain.EXPERIMENT_NUM);
		pw.println("1チーム中の1人あたりの最後の数ターンに拘束されていた時間" + "," + MainMain.measure.bindingTimePerAgentInTeamAtEnd / (double)MainMain.EXPERIMENT_NUM);
		pw.println("総タスク処理リソースの平均" + "," + (double)MainMain.measure.allSuccessTaskRequire / (double)MainMain.EXPERIMENT_NUM);
		pw.println("総タスク廃棄リソースの平均" + "," + (double)MainMain.measure.allFailureTaskRequire / (double)MainMain.EXPERIMENT_NUM);
		pw.println();
		
		pw.println("チーム内人数" + "," + "1チームの不要な拘束時間" + "," + "1チーム中の1人あたりの不要な拘束時間" + "," + "チーム編成成功数");
		for(int i = 0; i < MainMain.measure.bindingTimeInTeamEveryTeamSize.length; i++){
			pw.println(i + "," + MainMain.measure.bindingTimeInTeamEveryTeamSize[i] / (double)MainMain.EXPERIMENT_NUM + ","
					+ (MainMain.measure.bindingTimeInTeamEveryTeamSize[i] / (double)i) / (double)MainMain.EXPERIMENT_NUM + ","
					+ MainMain.measure.allSuccessTeamFormationNumEveryTeamSize[i] / (double)MainMain.EXPERIMENT_NUM);
		}
		
		pw.close();
	}


}
