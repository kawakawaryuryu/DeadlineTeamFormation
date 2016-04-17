package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;

import role.Role;
import strategy.StrategyManager;
import strategy.memberselection.TentativeMemberSelectionStrategy;
import strategy.roleselection.RoleSelectionStrategy;
import strategy.subtaskallocation.SubtaskAllocationStrategy;
import strategy.taskselection.TaskSelectionStrategy;
import task.Task;
import team.Team;
import constant.Constant;
import agent.Agent;

public class FileWriteManager {
	static int fileNumber;	//ファイルのnumber
	static boolean isWrite = false;	//追加か上書きか
	static String path = "../../Dropbox/research/";
	static String fileName;
	
	/**
	 * ファイル番号とパスを設定する
	 * @param isExperiment TODO
	 * @param number
	 */
	public static void set(String isExperiment, int number) {
		fileNumber = number;
		
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH-mm");
		path += isExperiment + "/" + sdf1.format(date) + "/";
		fileName = sdf2.format(date) + "_" + fileNumber;
	}
	
	/**
	 * ファイルの内容説明の書き込み
	 * @param learning TODO
	 * @param estimation TODO
	 * @return
	 * @throws IOException
	 */
	public static void fileExplain(String learning, String estimation) throws IOException{
		TaskSelectionStrategy taskSelectionStrategy = StrategyManager.getTaskSelectionStrategy();
		RoleSelectionStrategy roleSelectionStrategy = StrategyManager.getRoleSelectionStrategy();
		SubtaskAllocationStrategy allocationStrategy = StrategyManager.getAllocationStrategy();
		TentativeMemberSelectionStrategy memberSelectionStrategy = StrategyManager.getMemberSelectionStrategy();
		
		File directory = new File(path + "data/file/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "data/file/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/file_" + fileName + ".csv", false), "Shift_JIS")));
		pw.println("ファイル名" + "," + Constant.TURN_NUM + "turn_" + Constant.AGENT_NUM + "agents_" + fileName + ".csv");
		pw.println("FIFO、マークあり、チームの履歴を保持、1tickごとの獲得報酬で報酬期待度を学習、リーダにはまず1個だけ割り当てる、タスクコピーに時間がかかる（失敗時は拘束されない）");
		pw.println("学習" + "," + learning);
		pw.println("見積もり" + "," + estimation);
		pw.println("ターン" + "," + Constant.TURN_NUM);
		pw.println("試行回数" + "," + MainMain.EXPERIMENT_NUM);
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
		pw.println("タスクのリソース" + "," + (Constant.TASK_REQUIRE_INIT * Constant.TASK_REQUIRE_MALTIPLE) + " ~　" + ((Constant.TASK_REQUIRE_INIT + Constant.TASK_REQUIRE_MAX - 1) * Constant.TASK_REQUIRE_MALTIPLE));
		pw.println("タスクのデッドライン" + "," + Constant.DEADLINE_INIT + " ~ " + (Constant.DEADLINE_INIT + Constant.DEADLINE_MAX - 1));
		pw.println("1tickに追加する平均タスク数" + "," + Constant.ADD_TASK_PER_TURN);
		pw.println("タスク追加間隔" + "," + Constant.ADD_TASK_INTERVAL);
		pw.println("タスクコピーにかかる時間" + "," + Constant.WAIT_TURN);
		pw.println("ε-greedyのε" + "," + Constant.EPSILON);
		pw.println("ε-greedyのε（メンバ候補の決定時）" + "," + Constant.EPSILON2);
		pw.println("1サブタスクごとに送るメッセージ数" + "," + Constant.SELECT_MEMBER_NUM);
		pw.println("タスク選択の戦略" + "," + taskSelectionStrategy.toString());
		pw.println("役割選択の戦略" + "," + roleSelectionStrategy.toString());
		pw.println("サブタスク割り当ての戦略" + "," + allocationStrategy.toString());
		pw.println("仮メンバ選択の戦略" + "," + memberSelectionStrategy.toString());
		pw.println("可視化のための閾値を格納する配列要素数" + "," + Constant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM);
		for(int i = 0; i < Constant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM; i++){
			pw.println(i + " 番目" + "," + Constant.TEAM_FORMATION_PERCENTAGE_BORDER[i]);
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
		File directory = new File(path + "data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/average");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/average/require_average_" + fileName + ".csv", isWrite), "Shift_JIS")));
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
		pw.print("1チーム中の不要な平均拘束時間");
		pw.print(",");
		pw.print("1チーム中の1人あたりの平均処理時間");
		pw.print(",");
		pw.print("1チーム中の1人あたりの不要な平均拘束時間");
		pw.print(",");
		pw.print("マークしたタスクのリソース");
		pw.print(",");
		pw.println("マークしたタスクの残りデッドライン");
		
		return pw;
	}
	
	/**
	 * 時間ごとの計測を書き込む
	 * @throws IOException
	 */
	public static void writeBodyOfMeasuredDataPerTurn() throws IOException{	
		PrintWriter pw = writeHeaderOfMeasuredDataPerTurn(MainMain.EXPERIMENT_NUM);
		
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			int turn = Constant.MEASURE_TURN_NUM * (i + 1);
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
			pw.print(MainMain.measure.bindingTimePerAgentInTeam[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(MainMain.measure.markedTaskRequire[i] / (double)MainMain.EXPERIMENT_NUM);
			pw.print(",");
			pw.println(MainMain.measure.markedTaskDeadline[i] / (double)MainMain.EXPERIMENT_NUM);
		}

		pw.close();
	}
	
	/**
	 * チーム人数ごとのチーム編成成功回数の時間推移を書き込む（最初）
	 * @param allExperimentNum
	 * @throws IOException
	 */
	private static PrintWriter writeHeaderOfTeamMeasuredData(int allExperimentNum) throws IOException {
		File directory = new File(path + "data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamingSuccess");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamingSuccess/teaming_success_" + fileName + ".csv", isWrite), "Shift_JIS")));
		pw.println("平均" + allExperimentNum + "回 チーム人数ごとのチーム編成成功回数");
		pw.print("経過ターン");
		pw.print(",");
		for(int i = 1; i < Constant.ARRAY_SIZE_FOR_TEAM; i++){
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
		
		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			int turn = Constant.ARRAY_SIZE_FOR_MEASURE * (i + 1);
			pw.print(turn);
			pw.print(",");
			for(int j = 0; j < Constant.ARRAY_SIZE_FOR_TEAM; j++){
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
	public static PrintWriter writeHeaderOfGreedy(ArrayList<Agent> agents) throws IOException{
		File directory = new File(path + "data/greedy/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/greedy/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/greedy_" + fileName + ".csv", isWrite), "Shift_JIS")));
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
	public static void writeBodyOfGreedy(PrintWriter pw, int turn, ArrayList<Agent> agents) throws IOException{
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
	 * 信頼度の書き込み
	 * @param agents
	 * @param turn
	 * @throws IOException
	 */
	public static void writeTrust(ArrayList<Agent> agents, int turn) throws IOException{
		File directory = new File(path + "data/trust/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileName + "/result/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/trust/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileName + "/result/trust_" + turn + ".csv", isWrite), "Shift_JIS")));

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
	 * 報酬期待度の書き込み
	 * @param agents
	 * @param turn
	 * @throws IOException
	 */
	public static void writeRewardExpectation(ArrayList<Agent> agents, int turn) throws IOException{
		File directory = new File(path + "data/expectedReward/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileName + "/result/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/expectedReward/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileName + "/result/expectedReward_" + turn + ".csv", isWrite), "Shift_JIS")));
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
	 * 役割の担当回数の書き込み（最初）
	 * @param agents
	 * @return
	 * @throws IOException
	 */
	private static PrintWriter writeHeaderOfRoleNumber(ArrayList<Agent> agents) throws IOException{
		File directory = new File(path + "data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/role/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/role/roleNumber_" + fileName + ".csv", isWrite), "Shift_JIS")));
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
	 * @param agents
	 * @param exprimentNumber TODO
	 * @throws Exception 
	 */
	public static void writeBodyOfRoleNumber(ArrayList<Agent> agents) throws IOException{
		PrintWriter pw = writeHeaderOfRoleNumber(agents);
		pw.print("Leader");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i).getParameter().getElement(Role.LEADER).getRoleNum());
			pw.print(",");
		}
		pw.println();
		pw.print("Member");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print(agents.get(i).getParameter().getElement(Role.MEMBER).getRoleNum());
			pw.print(",");
		}
		pw.println();
		pw.close();
		/*pw.println();
		pw.print("初期状態にかけた時間割合");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print((double)agents.get(i).getParameter().getElement(Role.INITIAL).getStateTime() / (double)Constant.TURN_NUM * 100);
			pw.print(",");
		}
		pw.println();
		pw.print("リーダ状態にかけた時間割合");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print((double)agents.get(i).getParameter().getElement(Role.LEADER).getStateTime() / (double)Constant.TURN_NUM * 100);
			pw.print(",");
		}
		pw.println();
		pw.print("メンバ状態にかけた時間割合");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print((double)agents.get(i).getParameter().getElement(Role.MEMBER).getStateTime() / (double)Constant.TURN_NUM * 100);
			pw.print(",");
		}
		pw.println();
		pw.print("実行状態にかけた時間割合");
		pw.print(",");
		for(int i = 0; i < Constant.AGENT_NUM; i++){
			pw.print((double)agents.get(i).getParameter().getElement(Role.EXECUTE).getStateTime() / (double)Constant.TURN_NUM * 100);
			pw.print(",");
		}*/
		
//		pw.print("");
	}
	
	private static ArrayList<Integer> makeAgentAbilitySumList(ArrayList<Agent> agents) {
		ArrayList<Integer> abilitySumList = new ArrayList<Integer>();
		for(Agent agent : agents){
			if(!abilitySumList.contains(agent.getAbilitySum())){
				abilitySumList.add(agent.getAbilitySum());
			}
		}
		
		// リソースの昇順にソート
		Collections.sort(abilitySumList);
		
		return abilitySumList;
	}
	
	/**
	 * 引数のマップと同じキーを持つマップを生成する
	 * @param agents
	 * @param abilitySumList
	 * @return
	 */
	private static TreeMap<Integer, Integer> cloneTeamFormationNumMap(ArrayList<Agent> agents, ArrayList<Integer> abilitySumList){
		TreeMap<Integer, Integer> teamFormationNumMap = new TreeMap<Integer, Integer>();
		for(int abilitySum : abilitySumList){
			teamFormationNumMap.put(abilitySum, 0);
		}
		return teamFormationNumMap;
	}
	
	/**
	 * 各エージェントとのチーム編成回数を記録
	 * @param agents
	 * @throws Exception 
	 */
	public static void writeTeamFormationWithAgent(ArrayList<Agent> agents) throws IOException{
		File directory = new File(path + "data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teaming/");
		// ディレクトリが存在しない場合はディレクトリを作成
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teaming/teamingNumber_" + fileName + ".csv", isWrite), "Shift_JIS")));
		
		// メンバとのチーム編成回数を書き込む
		pw.print("," + "Leader" + ",");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent);
		}
		pw.println();
		for(Agent me : agents){
			pw.print(me);
			pw.print("," + me.getParameter().getElement(Role.LEADER).getRoleNum() + ",");
			
			for(Agent you : agents){
				pw.print(",");
				pw.print(me.getMeasure().getTeamFormationNumWithMember(you));
			}
			pw.println();
		}
		pw.println();
		
		// リーダとのチーム編成回数を書き込む
		pw.print("," + "Member" + ",");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent);
		}
		pw.println();
		for(Agent me : agents){
			pw.print(me);
			pw.print("," + me.getParameter().getElement(Role.LEADER).getRoleNum() + ",");
			
			for(Agent you : agents){
				pw.print(",");
				pw.print(me.getMeasure().getTeamFormationNumWithLeader(you));
			}
			pw.println();
		}
		pw.println();
		
		// チーム編成合計回数を書き込む
		pw.print("," + "TeamFormationSum" + ",");
		for(Agent agent : agents){
			pw.print(",");
			pw.print(agent);
		}
		pw.print(",");
		// エージェントの相手の能力ごとのチーム編成合計回数を書き込む
		ArrayList<Integer> abilitySumList = makeAgentAbilitySumList(agents);
		for(int abilitySum : abilitySumList){
			pw.print(",");
			pw.print(abilitySum);
		}
		pw.println();
		
		for(Agent me : agents){
			pw.print(me);
			pw.print("," + ",");
			TreeMap<Integer, Integer> teamFormationNumMap = cloneTeamFormationNumMap(agents, abilitySumList);
			for(Agent you : agents){
				pw.print(",");
				pw.print(me.getMeasure().getTeamFormationNumWithLeader(you) + me.getMeasure().getTeamFormationNumWithMember(you));
				teamFormationNumMap.put(you.getAbilitySum(), 
						teamFormationNumMap.get(you.getAbilitySum()) + 
						me.getMeasure().getTeamFormationNumWithLeader(you) + me.getMeasure().getTeamFormationNumWithMember(you));
			}
			pw.print(",");
			for(Entry<Integer, Integer> map : teamFormationNumMap.entrySet()){
				pw.print(",");
				pw.print(map.getValue());
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
		File directory = new File(path + "data/histogram/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/histogram/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamNumHistogram_" + fileName + ".csv", isWrite), "Shift_JIS")));
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
	public static void writeBodyOfTeamFormationHistogramData(Team team, Task task) throws IOException{
		PrintWriter pw = writeHeaderOfTeamFormationHistogramData();
		pw.print(team.getTeamMate().size() + "," + team.getTeamExecuteTime() + "," + task.getTaskRequireSum() + "," + task.getSubTaskNum());
		pw.print(",");
		pw.println(team.getTeamResourceSum() + "," + (double)team.getTeamResourceSum() / (double)team.getTeamMate().size() + "," + team.getLeaderResource() + "," + team.getMemberResourcePerAgent());
	}
	
	/**
	 * タスクキューの中身を書き込む（最初）
	 * @return
	 * @throws IOException
	 */
	public static PrintWriter writeHeaderOfTaskQueue() throws IOException {
		File directory = new File(path + "data/queue/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/queue/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/taskQueue_" + fileName + ".csv", isWrite), "Shift_JIS")));
		pw.println("経過ターン" + "," + "タスクキューサイズ" + "," + "マークあり数" + "," + "マークなし数" + "," + "タスクキューの中身");
		
		return pw;
	}
	
	/**
	 * タスクキューの中身を書き込む
	 * @param pw
	 * @param turn
	 * @param taskQueue
	 * @param noMarkTaskNum
	 * @throws IOException
	 */
	public static void writeBodyOfTaskQueue(PrintWriter pw, int turn, ArrayList<Task> taskQueue, int noMarkTaskNum)
			throws IOException {
		// マークありのタスク数
		int markTaskNum = taskQueue.size() - noMarkTaskNum;
		pw.print(turn + "," + taskQueue.size() + "," + markTaskNum + "," + noMarkTaskNum);
		for(Task task : taskQueue){
			pw.print(",");
			pw.print(task);
		}
		pw.println();
	}
	
	/**
	 * その他の情報を書き込む
	 * @throws IOException
	 */
	public static void writeOtherData() throws IOException {
		File directory = new File(path + "data/other/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/");
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(path + "data/other/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/otherInfo_" + fileName + ".csv", isWrite), "Shift_JIS")));
		
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
		pw.println("タスクキューの平均サイズ" + "," + MainMain.measure.taskQueueNum / (double)MainMain.EXPERIMENT_NUM);
		pw.println("マークなしのタスクキューの平均サイズ" + "," + MainMain.measure.unmarkedTaskQueueNum / (double)MainMain.EXPERIMENT_NUM);
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
