package file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;

import main.manager.MeasuredDataManager;
import role.Role;
import strategy.StrategyManager;
import strategy.memberselection.TentativeMemberSelectionStrategy;
import strategy.roleselection.RoleSelectionStrategy;
import strategy.subtaskallocation.SubtaskAllocationStrategy;
import strategy.taskselection.TaskSelectionStrategy;
import task.Task;
import team.Team;
import config.Configuration;
import constant.Constant;
import action.ActionManager;
import agent.Agent;
import agent.StructuredAgent;
import agent.paramter.StructuredAgentParameter;

public class FileWriteManager {
	static int fileNumber;	//ファイルのnumber
	static boolean isWrite;	//追加か上書きか
	static String path;
	static String fileName;
	static String method;
	static String agentType;

	/**
	 * ファイル番号とパスを設定する
	 * @param isExperiment TODO
	 * @param number
	 */
	public static void set() {
		isWrite = Configuration.ADD_WRITE;
		path = Configuration.FILE_PATH;
		fileNumber = Configuration.FILE_NUMBER;

		path += Configuration.EXPERIMET_TYPE + "/" + Configuration.DATE + "/";
		path += "data_" + Configuration.REVISION + "/";

		fileName = Configuration.TIME + "_" + fileNumber;
		method = Configuration.METHOD_NAME;
		agentType = Configuration.AGENT_TYPE;
	}

	/**
	 * ファイルパスを返す
	 * @param dataType
	 * @param tailDir 後ろのディレクトリ
	 * @return
	 */
	private static String getPath(String dataType, String tailDir) {
		return path + dataType + "/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + tailDir;
	}

	/**
	 * 書き込みファイルパスまでのディレクトリを作成する
	 * @param dataType
	 * @param tailDir 後ろのディレクトリ
	 */
	private static void makeDirectory(String dataType, String tailDir) {
		File directory = new File(getPath(dataType, tailDir));
		/* ディレクトリが存在しない場合はディレクトリを作成 */
		if(!directory.exists()){
			directory.mkdirs();
		}
	}

	/**
	 * ファイル書き込みのPrintWriterのインスタンスを生成して返す
	 * @param dataType
	 * @param tailDir
	 * @param file 時刻 + Number
	 * @return
	 * @throws IOException
	 */
	private static PrintWriter getPrintWriter(String dataType, String tailDir, String file) throws IOException {
		return new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(getPath(dataType, tailDir) + "/" + file, isWrite), "Shift_JIS")));
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

		makeDirectory("file", "");

		String file = "file" + "_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("file", "", file);

		pw.println("ファイル名" + "," + Constant.TURN_NUM + "turn_" + Constant.AGENT_NUM + "agents_" + fileName + ".csv");
		pw.println("FIFO、マークあり、チームの履歴を保持、1tickごとの獲得報酬で報酬期待度を学習、リーダにはまず1個だけ割り当てる、タスクコピーに時間がかかる（失敗時は拘束されない）");
		pw.println("学習" + "," + learning);
		pw.println("見積もり" + "," + estimation);
		pw.println("手法" + "," + method);
		pw.println("ターン" + "," + Constant.TURN_NUM);
		pw.println("試行回数" + "," + Constant.EXPERIMENT_NUM);
		pw.println("エージェント数" + "," + Constant.AGENT_NUM);
		pw.println("エージェントタイプ" + "," + agentType);
		pw.println("リソースの種類数" + "," + Constant.RESOURCE_NUM);
		pw.println("欲張り度の初期値" + "," + Constant.INITIAL_GREEDY);
		pw.println("メンバに対する信頼度の初期値" + "," + Constant.INITIAL_TRUST_TO_MEMBER);
		pw.println("報酬期待度の初期値" + "," + Constant.INITIAL_EXPECTED_REWARD);
		pw.println("リーダに対する信頼度の初期値" + "," + Constant.INITIAL_TRUST_TO_LEADER);
		pw.println("欲張り度の学習率" + "," + Constant.LEARN_RATE_GREEDY);
		pw.println("メンバに対する信頼度の学習率" + "," + Constant.LEARN_RATE_TRUST_TO_MEMBER);
		pw.println("報酬期待度の学習率" + "," + Constant.LEARN_RATE_REWARD);
		pw.println("リーダに対する信頼度の学習率" + "," + Constant.LEARN_RATE_TRUST_TO_LEADER);
		pw.println("信頼エージェントの閾値" + "," + Constant.TRUST_LEADER_THREASHOLD);
		pw.println("信頼エージェントの保持上限数" + "," + Constant.TRUST_LEADER_LIMIT);
		pw.println("1tickごとに減少させていく信頼度の値" + "," + Constant.TRUST_DECREMENT_VALUE);
		pw.println("チーム履歴を保持する数" + "," + Constant.PAST_TEAM_NUM);
		pw.println("タスクの中のサブタスク数" + "," + Constant.SUBTASK_IN_TASK_INIT + "　~　" + (Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM - 1));
		pw.println("エージェントのリソース" + "," + Constant.AGENT_ABILITY_INIT + "　~　" + (Constant.AGENT_ABILITY_INIT + Constant.AGENT_ABILITY_MAX - 1));
		pw.println("タスクのリソース" + "," + (Constant.TASK_REQUIRE_INIT * Constant.TASK_REQUIRE_MALTIPLE) + " ~　" + ((Constant.TASK_REQUIRE_INIT + Constant.TASK_REQUIRE_MAX - 1) * Constant.TASK_REQUIRE_MALTIPLE));
		pw.println("タスクのデッドライン" + "," + Constant.DEADLINE_INIT + " ~ " + (Constant.DEADLINE_INIT + Constant.DEADLINE_MAX - 1));
		pw.println("1tickに追加する平均タスク数" + "," + Constant.ADD_TASK_PER_TURN);
		pw.println("タスク追加間隔" + "," + Constant.ADD_TASK_INTERVAL);
		pw.println("タスクコピーにかかる時間" + "," + Constant.WAIT_TURN);
		pw.println("通信遅延時間" + "," + Constant.MESSAGE_DELAY);
		pw.println("ε-greedyのε" + "," + Constant.EPSILON);
		pw.println("ε-greedyのε（メンバ候補の決定時）" + "," + Constant.EPSILON2);
		pw.println("1サブタスクごとに送るメッセージ数" + "," + Constant.SELECT_MEMBER_NUM);
		pw.println("タスク選択の戦略" + "," + taskSelectionStrategy.toString());
		pw.println("役割選択の戦略" + "," + roleSelectionStrategy.toString());
		pw.println("サブタスク割り当ての戦略" + "," + allocationStrategy.toString());
		pw.println("仮メンバ選択の戦略" + "," + memberSelectionStrategy.toString());
		pw.println("エージェント行動の流れ（モデル）" + "," + Configuration.model);
		pw.println("タスク返却の行動" + "," + ActionManager.taskReturnAction);
		pw.println("可視化のための閾値を格納する配列要素数" + "," + Constant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM);
		for(int i = 0; i < Constant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM; i++){
			pw.println(i + " 番目" + "," + Constant.TEAM_FORMATION_PERCENTAGE_BORDER[i]);
		}

//		pw.print("");
		pw.close();
	}

	/**
	 * 時間ごとの計測データのヘッダを書き込む
	 * @return
	 * @throws IOException
	 */
	private static PrintWriter writeHeaderOfMeasuredDataPerTurn() throws IOException{
		makeDirectory("TaskRequireResult", "/average");

		String file = "require_average" + "_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("TaskRequireResult", "/average", file);

		pw.println("平均" + Constant.EXPERIMENT_NUM + "回" + "," + method + "," + agentType);
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
	 * @param measure TODO
	 * @throws IOException
	 */
	public static void writeBodyOfMeasuredDataPerTurn(MeasuredDataManager measure) throws IOException{
		PrintWriter pw = writeHeaderOfMeasuredDataPerTurn();

		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			int turn = Constant.MEASURE_TURN_NUM * (i + 1);
			pw.print(turn);
			pw.print(",");
			pw.print(measure.successTaskRequire[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.failureTaskRequire[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.failureTaskNum[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.successTeamFormationNum[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.failureTeamFormationNum[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.giveUpTeamFormationNum[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.tryingTeamFormationNum[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.bindingTimeInTeam[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.executingTimePerAgentInTeam[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.bindingTimePerAgentInTeam[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.markedTaskRequire[i] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.println(measure.markedTaskDeadline[i] / (double)Constant.EXPERIMENT_NUM);
		}

		pw.close();
	}

	/**
	 * チーム人数ごとのチーム編成成功回数の時間推移を書き込む（最初）
	 * @throws IOException
	 */
	private static PrintWriter writeHeaderOfTeamMeasuredData() throws IOException {
		makeDirectory("TaskRequireResult", "/teamingSuccess");

		String file = "teaming_success" + "_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("TaskRequireResult", "teamingSuccess", file);

		pw.println("平均" + Constant.EXPERIMENT_NUM + "回 チーム人数ごとのチーム編成成功回数");
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
	 * @param measure TODO
	 * @throws IOException
	 */
	public static void writeBodyOfTeamMeasuredData(MeasuredDataManager measure) throws IOException {
		PrintWriter pw = writeHeaderOfTeamMeasuredData();

		for(int i = 0; i < Constant.ARRAY_SIZE_FOR_MEASURE; i++){
			int turn = Constant.ARRAY_SIZE_FOR_MEASURE * (i + 1);
			pw.print(turn);
			pw.print(",");
			for(int j = 0; j < Constant.ARRAY_SIZE_FOR_TEAM; j++){
				pw.print(measure.successTeamFormationNumEveryTeamSize[i][j] / (double)Constant.EXPERIMENT_NUM);
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
		makeDirectory("greedy", "");

		String file = "greedy" + "_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("greedy", "", file);

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
	 * メンバに対する信頼度の書き込み
	 * @param agents
	 * @param turn
	 * @throws IOException
	 */
	public static void writeTrustToMember(ArrayList<Agent> agents, int turn) throws IOException{
		makeDirectory("trust/toMember", "/" + fileName + "/result");

		String file = "trust_" + turn + ".csv";
		PrintWriter pw = getPrintWriter("trust/toMember", "/" + fileName + "/result", file);

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
				pw.print(agents.get(i).getTrustToMember(agents.get(j)));
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
		makeDirectory("expectedReward", "/" + fileName + "/result/");

		String file = "expectedReward_" + turn + ".csv";
		PrintWriter pw = getPrintWriter("expectedReward", "/" + fileName + "/result/", file);

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
	 * リーダに対する信頼度の書き込み
	 * @param agents
	 * @param turn
	 * @throws IOException
	 */
	public static void writeTrustToLeader(ArrayList<Agent> agents, int turn) throws IOException{
		makeDirectory("trust/toLeader", "/" + fileName + "/result");

		String file = "trust_" + turn + ".csv";
		PrintWriter pw = getPrintWriter("trust/toLeader", "/" + fileName + "/result", file);


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
				pw.print(((StructuredAgent)agents.get(i)).getTrustToLeader(agents.get(j)));
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
		makeDirectory("role", "/role");

		String file = "roleNumber_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("role", "/role", file);

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
		makeDirectory("role", "/teaming");

		String file = "teamingNumber_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("role", "/teaming", file);

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
			pw.print("," + me.getParameter().getElement(Role.MEMBER).getRoleNum() + ",");

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
		makeDirectory("histogram", "");

		String file = "teamNumHistogram_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("histogram", "", file);

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
		makeDirectory("queue", "");

		String file = "taskQueue_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("queue", "", file);

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

	public static PrintWriter writeHeaderOfTrustLeaders(ArrayList<Agent> agents) throws IOException {
		makeDirectory("trustLeaders", "/agent");

		String file = "trustLeaders_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("trustLeaders", "/agent", file);

		pw.print("経過ターン");
		for (int i = 0; i < agents.size(); i++) {
			pw.print(",");
			pw.print(agents.get(i));
		}
		pw.print(",");
		pw.print(",");
		pw.print("信頼エージェントを保持しているエージェント数");
		pw.println();

		return pw;
	}

	public static void writeBodyOfTrustLeaders(PrintWriter pw, int turn, ArrayList<Agent> agents) throws IOException {
		// 信頼エージェントを保持しているエージェント数
		int having = 0;

		pw.print(turn);
		for (int i = 0; i < agents.size(); i++) {
			pw.print(",");
			StructuredAgentParameter parameter = (StructuredAgentParameter)agents.get(i).getParameter();
			for (Agent agent : parameter.getTrustLeaders()) {
				pw.print(agent.getId() + "  ");
			}
			if(parameter.getTrustLeaders().size() > 0) having++;
		}
		pw.print(",");
		pw.print(",");
		pw.print(having);

		pw.println();
	}

	public static PrintWriter writeHeaderOfTrustLeadersNum(ArrayList<Agent> agents) throws IOException {
		makeDirectory("trustLeaders", "/num");

		String file = "trustLeadersNum_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("trustLeaders", "/num", file);

		pw.print("経過ターン");
		for (int i = 0; i < agents.size(); i++) {
			pw.print(",");
			pw.print(agents.get(i));
		}
		pw.print(",");
		pw.print(",");
		pw.print("信頼エージェントを保持しているエージェント数");
		pw.println();

		return pw;
	}

	public static void writeBodyOfTrustLeadersNum(PrintWriter pw, int turn, ArrayList<Agent> agents) throws IOException {
		// 信頼エージェントを保持しているエージェント数
		int having = 0;

		pw.print(turn);
		for (int i = 0; i < agents.size(); i++) {
			pw.print(",");
			StructuredAgentParameter parameter = (StructuredAgentParameter)agents.get(i).getParameter();
			pw.print(parameter.getTrustLeaders().size());
			if(parameter.getTrustLeaders().size() > 0) having++;
		}
		pw.print(",");
		pw.print(",");
		pw.print(having);

		pw.println();
	}

	public static PrintWriter writeHeaderOfTeamResources(ArrayList<Agent> agents) throws IOException {
		makeDirectory("TeamResources", "");

		String file = "TeamResources_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("TeamResources", "", file);

		pw.print("経過ターン");
		for (int i = 0; i < agents.size(); i++) {
			pw.print(",");
			pw.print(agents.get(i));
		}
		pw.println();

		return pw;
	}

	public static void writeBodyOfTeamResource(PrintWriter pw, int turn, ArrayList<Agent> agents) throws IOException {
		pw.print(turn);
		for (int i = 0; i < agents.size(); i++) {
			pw.print(",");
			pw.print(agents.get(i).getParameter().getPastTeam().getAverageAbilitiesPerTeam());
		}

		pw.println();
	}

	private static PrintWriter writeHeaderOfAgentsNum() throws IOException {
		makeDirectory("agentsNum", "");

		String file = "agentsNum_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("agentsNum", "", file);

		pw.print("経過ターン");
		pw.print(",");
		pw.print("初期状態のエージェント数");
		pw.print(",");
		pw.print("リーダ or メンバ状態のエージェント数");
		pw.print(",");
		pw.print("タスク実行状態のエージェント数");
		pw.println();

		return pw;
	}

	public static void writeBodyOfAgentsNum(MeasuredDataManager measure) throws IOException {
		PrintWriter pw = writeHeaderOfAgentsNum();

		for (int i = 1; i <= Constant.TURN_NUM; i++) {
			pw.print(i);
			pw.print(",");
			pw.print(measure.initialStateAgentNumPerTurn[i-1] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.leaderOrMemberStateAgentNumPerTurn[i-1] / (double)Constant.EXPERIMENT_NUM);
			pw.print(",");
			pw.print(measure.executeStateAgentNumPerTurn[i-1] / (double)Constant.EXPERIMENT_NUM);
			pw.println();
		}

		pw.close();
	}

	/**
	 * その他の情報を書き込む
	 * @param measure TODO
	 * @throws IOException
	 */
	public static void writeOtherData(MeasuredDataManager measure) throws IOException {
		makeDirectory("other", "");

		String file = "otherInfo_" + fileName + ".csv";
		PrintWriter pw = getPrintWriter("other", "", file);

		pw.println("仮チームの平均サイズ" + "," + measure.tentativeTeamSize / (double)Constant.EXPERIMENT_NUM);
		pw.println("チームの平均サイズ" + "," + measure.teamSize / (double)Constant.EXPERIMENT_NUM);
		pw.println("チームの平均処理時間" + "," + measure.teamExecuteTime / (double)Constant.EXPERIMENT_NUM);
		pw.println("主にリーダを担当したエージェント数" + "," + (double)measure.leaderMain / (double)Constant.EXPERIMENT_NUM);
		pw.println("主にメンバを担当したエージェント数" + "," + (double)measure.memberMain / (double)Constant.EXPERIMENT_NUM);
		pw.println("主な役割がリーダでもメンバでもないエージェント数" + "," + (double)measure.neitherLeaderNorMember / (double)Constant.EXPERIMENT_NUM);
		pw.println("1人あたりの初期状態にかけた時間" + "," + (double)measure.initialTime / (double)Constant.EXPERIMENT_NUM);
		pw.println("1人あたりのリーダ状態にかけた時間" + "," + (double)measure.leaderTime / (double)Constant.EXPERIMENT_NUM);
		pw.println("1人あたりのメンバ状態にかけた時間" + "," + (double)measure.memberTime / (double)Constant.EXPERIMENT_NUM);
		pw.println("1人あたりの実行状態にかけた時間" + "," + (double)measure.executeTime / (double)Constant.EXPERIMENT_NUM);
		pw.println("初期状態のエージェント数" + "," + (double)measure.initialStateAgentNum / (double)Constant.EXPERIMENT_NUM);
		pw.println("リーダ or メンバ状態のエージェント数" + "," + (double)measure.leaderOrMemberStateAgentNum / (double)Constant.EXPERIMENT_NUM);
		pw.println("タスク実行状態のエージェント数" + "," + (double)measure.executeStateAgentNum / (double)Constant.EXPERIMENT_NUM);
		pw.println("1チーム中の1人あたりの最後の数ターンに処理していた時間" + "," + measure.executingTimePerAgentInTeamAtEnd / (double)Constant.EXPERIMENT_NUM);
		pw.println("1チーム中の1人あたりの最後の数ターンに拘束されていた時間" + "," + measure.bindingTimePerAgentInTeamAtEnd / (double)Constant.EXPERIMENT_NUM);
		pw.println("総タスク処理リソースの平均" + "," + (double)measure.allSuccessTaskRequire / (double)Constant.EXPERIMENT_NUM);
		pw.println("総タスク廃棄リソースの平均" + "," + (double)measure.allFailureTaskRequire / (double)Constant.EXPERIMENT_NUM);
		pw.println("タスクキューの平均サイズ" + "," + measure.taskQueueNum / (double)Constant.EXPERIMENT_NUM);
		pw.println("マークなしのタスクキューの平均サイズ" + "," + measure.unmarkedTaskQueueNum / (double)Constant.EXPERIMENT_NUM);
		pw.println();

		pw.println("チーム内人数" + "," + "1チームの不要な拘束時間" + "," + "1チーム中の1人あたりの不要な拘束時間" + "," + "チーム編成成功数");
		for(int i = 0; i < measure.bindingTimeInTeamEveryTeamSize.length; i++){
			pw.println(i + "," + measure.bindingTimeInTeamEveryTeamSize[i] / (double)Constant.EXPERIMENT_NUM + ","
					+ (measure.bindingTimeInTeamEveryTeamSize[i] / (double)i) / (double)Constant.EXPERIMENT_NUM + ","
					+ measure.allSuccessTeamFormationNumEveryTeamSize[i] / (double)Constant.EXPERIMENT_NUM);
		}

		pw.close();
	}


}
