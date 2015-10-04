package agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Constant;
import member.MemberField;
import message.AnswerMessage;
import message.OfferMessage;
import message.TeamingMessage;
import state.InitialMarkingTaskState;
import state.State;
import task.SubTask;
import task.Task;
import team.Team;
import leader.LeaderField;

public class Agent {
	protected int id;	//エージェント番号
	protected int[] ability = new int[Constant.RESOURCE_NUM];	//リソースの配列
	private int abilitySum;	//リソースの合計値
	private int[] leftResource = new int[Constant.RESOURCE_NUM];	//残りのリソースの配列
	private int executeTime = 0;	//サブタスクを処理するのにかかる時間
	private State nowState;	//今の状態
	private int role;	//リーダになったか、メンバになったかの役割
	private List<OfferMessage> offers = new ArrayList<OfferMessage>();	//提案メッセージリスト
	private List<AnswerMessage> answers = new ArrayList<AnswerMessage>();	//提案の返答メッセージリスト
	private TeamingMessage teaming;	//チーム編成に成功かどうかのメッセージ
	protected OfferMessage selectedOfferMessage;	//複数の提案メッセージの中から選択したメッセージ
	private Task markingTask;	//マークしているタスク
	private SubTask executedSubTask;	//処理するサブタスク
	private List<SubTask> executedSubTaskList = new ArrayList<SubTask>();	//エージェントが処理するサブタスクリスト
	public List<SubTask> willBeExecutedSubTaskList = new ArrayList<SubTask>();	//リーダが保持する、エージェントが処理するサブタスクリスト
	private List<Agent> sendAgents = new ArrayList<Agent>();	//提案メッセージが送られたエージェントリスト
	private Team teamInfo;	//エージェントが参加するチーム情報
	private List<Team> pastTeams = new ArrayList<Team>();	//今まで組んできたチーム
	private double averageTeamResource = 0.0;	//チーム履歴の平均リソース
	private double averageExecuteTimePerResource = 0.0;	//エージェントが今まで組んできたチームが1リソースあたりにかかる時間
	private int successNum = 0;	//チーム成功数
	private LeaderField leaderFields = new LeaderField();	//リーダ時に保持するフィールド
	private MemberField memberFields = new MemberField();	//メンバ時に保持するフィールド
	
	private int leaderNum = 0;	//リーダになった回数
	private int memberNum = 0;	//メンバになった回数
	private int leaderNumPerTurn = 0;	//一定ターンごとにリーダになった回数
	private int memberNumPerTurn = 0;	//一定ターンごとにメンバになった回数
	
	private int turn = 0;	//ターン
	private int teamingSuccessAtEnd = 0;	//最後の数ターンでのチーム編成成功回数
	
	//private static Random random = new Random(1000000007);	//ランダムインスタンス
	
	protected double reward;	//報酬
	protected double[] trust;	//提案受託期待度（iのjに対する信頼度）
	protected double[] expectedReward;	//報酬期待度
	private double greedy;	//欲張り度
	private int[] teamingWithMemberNumSum;	//リーダが各メンバとのチーム編成を行った合計回数
	private int[] teamingWithLeaderNumSum;	//メンバが各リーダとチーム編成を行った合計回数
	private int[] teamingWithMemberNumPerTurn;	//ターンごとのリーダがメンバとのチーム編成を行った回数
	private int[] teamingWithLeaderNumPerTurn;	//ターンごとのメンバがリーダとのチーム編成を行った回数
	
	private int initialTimer = 0;	//初期状態のタイマー
	private int leaderTimer = 0;	//リーダ状態のタイマー
	private int memberTimer = 0;	//メンバ状態のタイマー
	private int executeTimer = 0;	//タスク実行時間
	
	private int initialStateTime = 0;	//初期状態にいる合計時間
	private int leaderStateTime = 0;	//リーダ状態にいる合計時間
	private int memberStateTime = 0;	//メンバ状態にいる合計時間
	private int executeStateTime = 0;	//実行状態にいる合計時間
	private int executeStateConstraintTime = 0;	//実行状態での無駄に拘束されている合計時間
	
	private int executeStateExecutingTimeAtEnd = 0;		//最後の数ターンの処理していた合計時間
	private int executeStateConstraintTimeAtEnd = 0;	//最後の数ターンの拘束されていた合計時間
	
	private int constraintTimeSumPerTime = 0;	//一定時間ごとの不要に拘束された時間
	private int teamingSuccessNum = 0;			//一定時間ごとのチーム編成成功回数
	
	/**
	 * コンストラクタ
	 * @param id 
	 * @param ability
	 */
	public Agent(int id, int[] ability){
		this.id = id;	//エージェント番号
		abilitySum = 0;
		
		/* エージェントにリソースを割り当てる */
		for(int i = 0; i < ability.length; i++){
			this.ability[i] = ability[i];	//リソース
			leftResource[i] = ability[i];
			abilitySum += ability[i];
		}
		nowState = InitialMarkingTaskState.getInstance();	//エージェントの状態を取得
		
		greedy = Constant.INITIAL_GREEDY;	//欲張り度の初期設定
		trust = new double[Constant.AGENT_NUM];
		expectedReward = new double[Constant.AGENT_NUM];
		Arrays.fill(trust, Constant.INITIAL_TRUST);	//信頼度の初期設定
		trust[id] = 0.0;
		Arrays.fill(expectedReward, Constant.INITIAL_EXPECTED_REWARD);	//報酬期待度の初期設定
		expectedReward[id] = 0.0;
		
		teamingWithMemberNumSum = new int[Constant.AGENT_NUM];
		teamingWithLeaderNumSum = new int[Constant.AGENT_NUM];
		teamingWithMemberNumPerTurn = new int[Constant.AGENT_NUM];
		teamingWithLeaderNumPerTurn = new int[Constant.AGENT_NUM];
		Arrays.fill(teamingWithMemberNumSum, 0);	//メンバとのチーム編成回数の初期設定
		Arrays.fill(teamingWithLeaderNumSum, 0);	//リーダとのチーム編成回数の初期設定
		Arrays.fill(teamingWithMemberNumPerTurn, 0);
		Arrays.fill(teamingWithLeaderNumPerTurn, 0);
	}
	
	/**
	 * エージェントの自己紹介
	 */
	public void hello(){
		System.out.print("Agent:id = " + id + " / ability = ");
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			System.out.print(ability[i] + " ");
		}
		System.out.println();
	}
	
	/**
	 * エージェントの実際の行動
	 */
	public void action(){
//		System.out.println("エージェント（" + this + "） の行動");
		turn++;
		nowState.agentAction(this);
	}
	
	/**
	 * 変数、リストの初期化
	 */
	public void initialize(){
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			leftResource[i] = ability[i];
		}
		executeTime = 0;
		role = 0;
		offers.clear();
		answers.clear();
		teaming = null;
		selectedOfferMessage = null;
		markingTask = null;
		executedSubTask = null;
		executedSubTaskList.clear();
		willBeExecutedSubTaskList.clear();
		sendAgents.clear();
		teamInfo = null;
		reward = 0.0;
		executeTimer = 0;
		initialTimer = 0;
		leaderTimer = 0;
		memberTimer = 0;
	}
	
	/**
	 * 一定時間ごとに計測するデータを初期化
	 */
	public void initializeDataPerTime(){
		constraintTimeSumPerTime = 0;
		teamingSuccessNum = 0;
	}
	
	/**
	 * 一定時間ごとに計測するリーダとメンバとのチーム編成回数を初期化
	 */
	public void initializeTeamingWithLeaderAndMemberPerTime(){
		Arrays.fill(teamingWithLeaderNumPerTurn, 0);
		Arrays.fill(teamingWithMemberNumPerTurn, 0);
		leaderNumPerTurn = 0;
		memberNumPerTurn = 0;
	}
	
	/**
	 * ターンを返す
	 * @return
	 */
	public int getTurn(){
		return turn;
	}
	
	/**
	 * エージェントのidを返す
	 * @return
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * エージェントのリソースを返す
	 * @return
	 */
	public int[] getAbility(){
		return ability;
	}
	
	/**
	 * エージェントのリソースの合計値を返す
	 * @return
	 */
	public int getAbilitySum(){
		return abilitySum;
	}
	
	/**
	 * エージェントの残りのリソースを計算
	 * @param resource
	 */
	public void subtractResource(int[] resource){
		for(int i = 0; i < resource.length; i++){
			leftResource[i] -= resource[i];
		}
	}
	
	/**
	 * エージェントの残りのリソースを返す
	 * @return
	 */
	public int[] getLeftResource(){
		return leftResource;
	}
	
	/**
	 * サブタスクを処理する時間を足す
	 * @param time
	 */
	public void addExecuteTime(int time){
		executeTime += time;
	}
	
	/**
	 * サブタスクを処理する時間を返す
	 * @return
	 */
	public int getExecuteTime(){
		return executeTime;
	}
	
	/**
	 * 処理するサブタスクをサブタスクリストに代入
	 * @param subtask
	 */
	public void addExecutedSubTaskList(SubTask subtask){
		executedSubTaskList.add(subtask);
	}
	
	/**
	 * 処理するサブタスクを代入
	 * @param subtask
	 */
	public void addExecutedSubTask(SubTask subtask){
		executedSubTask = subtask;
	}
	
	/**
	 * 処理するサブタスクリストを返す
	 * @return
	 */
	public List<SubTask> getExecutedSubTaskList(){
		return executedSubTaskList;
	}
	
	/**
	 * 処理するサブタスクを返す
	 * @return
	 */
	public SubTask getExecutedSubTask(){
		return executedSubTask;
	}
	
	/**
	 * 提案メッセージを送るエージェントを追加する
	 * @param agent
	 */
	public void addSendAgent(Agent agent){
		sendAgents.add(agent);
	}
	
	/**
	 * 提案メッセージを送ったエージェントリストを返す
	 * @return
	 */
	public List<Agent> getSendAgents(){
		return sendAgents;
	}
	
	/**
	 * チーム編成提案メッセージを追加する
	 * @param msg
	 */
	public void addOfferMessage(OfferMessage msg){
		offers.add(msg);
	}
	
	/**
	 * チーム編成提案メッセージリストを返す
	 * @return
	 */
	public List<OfferMessage> getOfferMessage(){
		return offers;
	}
	
	/**
	 * チーム編成提案の返答メッセージを追加する
	 * @param msg
	 */
	public void addAnswerMessage(AnswerMessage msg){
		answers.add(msg);
	}
	
	/**
	 * チーム編成提案の返答メッセージを返す
	 * @return
	 */
	public List<AnswerMessage> getAnswerMessage(){
		return answers;
	}
	
	/**
	 * チーム編成成功かどうかのメッセージを追加する
	 * @param msg
	 */
	public void addTeamingMessage(TeamingMessage msg){
		teaming = msg;
	}
	
	/**
	 * チーム編成成功かどうかのメッセージを返す
	 * @return
	 */
	public TeamingMessage getTeamingMessage(){
		return teaming;
	}
	
	/**
	 * 選択したチーム参加提案メッセージを保持させる
	 * @param message
	 */
	public void haveSelectedOfferMessage(OfferMessage message){
		selectedOfferMessage = message;
	}
	
	/**
	 * キューからタスクをマークする
	 * @param task
	 */
	public void markingTask(Task task){
		task.markingTask(true);
		markingTask = task;
	}
	
	/**
	 * marking_taskを返す
	 * @return
	 */
	public Task getMarkingTask(){
		return markingTask;
	}
	
	/**
	 * エージェントの状態を変化させる
	 * @param state 次の状態
	 */
	public void changeState(State state){
//		System.out.println("\nエージェントは" + this.nowState + "から" + state + "に変化しました\n");
		this.nowState = state;
	}
	
	/**
	 * エージェントの今の状態を返す
	 * @return
	 */
	public State getNowState(){
		return nowState;
	}
	
	/**
	 * 役割を保持する
	 * @param role
	 */
	public void haveRole(int role){
		this.role = role;
	}
	
	/**
	 * エージェントの役割を返す
	 * @return
	 */
	public int getRole(){
		return role;
	}
	
	/**
	 * チーム情報を持つ
	 * @param teamInfo
	 */
	public void haveTeamInfo(Team teamInfo){
		this.teamInfo = teamInfo;
	}
	
	/**
	 * チーム情報を返す
	 * @return
	 */
	public Team getTeamInfo(){
		return this.teamInfo;
	}
	
	/**
	 * チーム履歴にチームを加える
	 * @param team
	 */
	public void addToPastTeam(Team team){
		pastTeams.add(team);
		if(pastTeams.size() > Constant.PAST_TEAM_NUM){
			pastTeams.remove(0);
		}
	}
	
	/**
	 * チーム履歴を返す
	 * @return
	 */
	public List<Team> getPastTeams(){
		return pastTeams;
	}
	
	/**
	 * チーム履歴の平均リソースを計算する
	 */
	public void calculateAverageTeamResource(){
		double averageResource = 0.0;
		for(Team team : pastTeams){
			averageResource += team.getTeamResourceSum();
//			averageResource += (double)team.getTeamResourceSum() / (double)team.getSize();	//１人あたりの平均リソースを算出
		}
		if(pastTeams.size() != 0){
			averageTeamResource = averageResource / pastTeams.size();
		}
	}
	
	/**
	 * チーム履歴の平均リソースを返す
	 * @return
	 */
	public double getAverageTeamResource(){
		return averageTeamResource;
	}
	
	/**
	 * 1リソースあたりにかかる時間を更新する
	 * @param executeTimePerResource
	 */
	public void updateAverageExecuteTimePerResource(){
		double executeTimePerResource = (double)this.getTeamInfo().getExecuteTime() / (double)this.getMarkingTask().getTaskRequireSum();
		averageExecuteTimePerResource = (averageExecuteTimePerResource * successNum + executeTimePerResource) / (double)(successNum + 1);
		successNum++;
	}
	
	/**
	 * 1リソースあたりにかかる時間を返す
	 * @return
	 */
	public double getAverageExecuteTimePerResource(){
		return averageExecuteTimePerResource;
	}
	
	/**
	 * リーダ時のフィールドを保持するインスタンスを返す
	 * @return
	 */
	public LeaderField getLeaderFields(){
		return leaderFields;
	}
	
	/**
	 * メンバ時のフィールドを保持するインスタンスを返す
	 * @return
	 */
	public MemberField getMemberFields(){
		return memberFields;
	}
	
	/**
	 * リーダの回数を増やす
	 */
	public void addLeaderNum(){
		leaderNum++;
		leaderNumPerTurn++;
	}
	
	/**
	 * リーダの回数を返す
	 * @return
	 */
	public int getLeaderNum(){
		return leaderNum;
	}
	
	/**
	 * 一定ターンごとのリーダ回数を返す
	 * @return
	 */
	public int getLeaderNumPerTurn(){
		return leaderNumPerTurn;
	}
	
	/**
	 * メンバの回数を増やす
	 */
	public void addMemberNum(){
		memberNum++;
		memberNumPerTurn++;
	}
	
	/**
	 * メンバの回数を返す
	 * @return
	 */
	public int getMemberNum(){
		return memberNum;
	}
	
	/**
	 * 一定ターンごとのメンバ回数を返す
	 * @return
	 */
	public int getMemberNumPerTurn(){
		return memberNumPerTurn;
	}
	
	/**
	 * 最後の数ターンのチーム編成成功回数を増やす
	 */
	public void addTeamingSuccessAtEnd(){
		teamingSuccessAtEnd++;
	}
	
	/**
	 * 最後の数ターンのチーム編成成功回数を返す
	 * @return
	 */
	public int getTeamingSuccessAtEnd(){
		return teamingSuccessAtEnd;
	}
	
	/**
	 * 初期状態のタイマーを1増やす
	 */
	public void addInitialTimer(){
		initialTimer++;
	}
	
	/**
	 * 初期状態のタイマーを返す
	 * @return
	 */
	public int getInitialTimer(){
		return initialTimer;
	}
	
	/**
	 * リーダ提案送信状態のタイマーを1増やす
	 */
	public void addLeaderTimer(){
		leaderTimer++;
	}
	
	/**
	 * リーダ提案送信状態のタイマーを返す
	 * @return
	 */
	public int getLeaderTimer(){
		return leaderTimer;
	}
	
	/**
	 * メンバ提案受託状態のタイマーを1増やす
	 */
	public void addMemberTimer(){
		memberTimer++;
	}
	
	/**
	 * メンバ提案受託状態のタイマーを返す
	 * @return
	 */
	public int getMemberTimer(){
		return memberTimer;
	}
	
	/**
	 * 実行時間を1増やす
	 */
	public void addExecuteTimer(){
		executeTimer++;
	}
	
	/**
	 * 実行時間を返す
	 * @return
	 */
	public int getExecuteTimer(){
		return executeTimer;
	}
	
	/**
	 * 欲張り度を返す
	 * @return
	 */
	public double getGreedy(){
		return greedy;
	}
	
	/**
	 * 提案受託期待度を返す
	 * @return
	 */
	public double[] getTrust(){
		return trust;
	}
	
	/**
	 * 提案受託期待度を返す
	 * @param you
	 * @return
	 */
	public double getTrust(Agent you){
		return trust[you.id];
	}
	
	/**
	 * 報酬期待度を返す
	 * @return
	 */
	public double[] getExpectedReward(){
		return expectedReward;
	}
	
	/**
	 * 報酬期待度を返す
	 * @param you 
	 * @return
	 */
	public double getExpectedReward(Agent you){
		return expectedReward[you.id];
	}
	
	/**
	 * リーダがメンバとのチーム編成回数を増やす
	 * @param members
	 */
	public void addTeamingWithMemberNum(List<Agent> members){
		for(Agent agent : members){
			teamingWithMemberNumSum[agent.id]++;
			teamingWithMemberNumPerTurn[agent.id]++;
		}
	}
	
	/**
	 * リーダのメンバとのチーム編成合計回数を返す
	 * @param member
	 * @return
	 */
	public int getTeamingWithMemberNumSum(Agent member){
		return teamingWithMemberNumSum[member.id];
	}
	
	/**
	 * ターンごとのリーダのメンバとのチーム編成回数を返す
	 * @param member
	 * @return
	 */
	public int getTeamingWithMemberNumPerTurn(Agent member){
		return teamingWithMemberNumPerTurn[member.id];
	}
	
	/**
	 * メンバがリーダとのチーム編成回数を増やす
	 * @param leader
	 */
	public void addTeamingWithLeaderNum(Agent leader){
		teamingWithLeaderNumSum[leader.id]++;
		teamingWithLeaderNumPerTurn[leader.id]++;
	}
	
	/**
	 * メンバのリーダとのチーム編成合計回数を返す
	 * @param leader
	 * @return
	 */
	public int getTeamingWithLeaderNumSum(Agent leader){
		return teamingWithLeaderNumSum[leader.id];
	}
	
	/**
	 * ターンごとのメンバのリーダとのチーム編成回数を返す
	 * @param leader
	 * @return
	 */
	public int getTeamingWithLeaderNumPerTurn(Agent leader){
		return teamingWithLeaderNumPerTurn[leader.id];
	}
	
	/**
	 * 初期状態にかかる時間をカウント
	 */
	public void addInitialStateTime(){
		initialStateTime++;
	}
	
	/**
	 * 初期状態にかかる時間を返す
	 * @return
	 */
	public int getInitialStateTime(){
		return initialStateTime;
	}
	
	/**
	 * リーダ状態にかかる時間をカウント
	 */
	public void addLeaderStateTime(){
		leaderStateTime++;
	}
	
	/**
	 * リーダ状態にかかる時間を返す
	 * @return
	 */
	public int getLeaderStateTime(){
		return leaderStateTime;
	}
	
	/**
	 * メンバ状態にかかる時間をカウント
	 */
	public void addMemberStateTime(){
		memberStateTime++;
	}
	
	/**
	 * メンバ状態にかかる時間を返す
	 * @return
	 */
	public int getMemberStateTime(){
		return memberStateTime;
	}
	
	/**
	 * 実行状態にかかる時間をカウント
	 */
	public void addExecuteStateTime(){
		executeStateTime++;
	}
	
	/**
	 * 実行状態にかかる時間を返す
	 * @return
	 */
	public int getExecuteStateTime(){
		return executeStateTime;
	}
	
	/**
	 * 実行状態において無駄に拘束されている時間をカウント
	 */
	public void addExecuteStateConstraintTime(){
		executeStateConstraintTime++;
	}
	
	/**
	 * 実行状態において無駄に拘束されている時間を返す
	 * @return
	 */
	public int getExecuteStateConstraintTime(){
		return executeStateConstraintTime;
	}
	
	/**
	 * 最後の数ターンの処理していた時間をカウント
	 */
	public void addExecuteStateExecutingTimeAtEnd(int time){
		executeStateExecutingTimeAtEnd += time;
	}
	
	/**
	 * 最後の数ターンの処理していた時間を返す
	 * @return
	 */
	public int getExecuteStateExecutingTimeAtEnd(){
		return executeStateExecutingTimeAtEnd;
	}
	
	/**
	 * 最後の数ターンの拘束されていた時間をカウント
	 */
	public void addExecuteStateConstraintTimeAtEnd(int time){
		executeStateConstraintTimeAtEnd += time;
	}
	
	/**
	 * 最後の数ターンの拘束されていた時間を返す
	 * @return
	 */
	public int getExecuteStateConstraintTimeAtEnd(){
		return executeStateConstraintTimeAtEnd;
	}
	
	/**
	 * 一定時間ごとの不要な拘束時間をカウント
	 * @param time
	 */
	public void addConstraintTimeSumPerAgent(int time){
		constraintTimeSumPerTime += time;
	}
	
	/**
	 * 一定時間ごとの不要な拘束時間を返す
	 * @return
	 */
	public int getConstraintTimeSumPerAgent(){
		return constraintTimeSumPerTime;
	}
	
	/**
	 * 一定時間ごとのチーム編成成功回数をカウント
	 */
	public void addTeamingSuccessNum(){
		teamingSuccessNum++;
	}
	
	/**
	 * 一定時間ごとのチーム編成成功回数を返す
	 * @return
	 */
	public int getTeamingSuccessNum(){
		return teamingSuccessNum;
	}
	
	/**
	 * 欲張り度の更新
	 * @param isok
	 */
	public void feedbackGreedy(boolean isok){
		double value = isok ? 1.0 : 0.0;
		calculateLeaderReward(isok);	//獲得報酬の計算
//		System.out.println(this + " の獲得報酬 = " + reward);
//		System.out.print("欲張り度：（前） = " + greedy);
		greedy = Constant.LEARN_RATE_GREEDY * value + (1.0 - Constant.LEARN_RATE_GREEDY) * greedy;	//欲張り度の更新
//		System.out.println(" / （後） = " + greedy);	
	}
	
	/**
	 * 提案受託期待度の更新
	 * @param you
	 * @param isok
	 */
	public void feedbackTrust(Agent you, boolean isok){
		double value = isok ? 1.0 : 0.0;
//		System.out.print(this + " の " + you + "　に対する提案受託期待度：（前） = " + trust[you.id]);
		trust[you.id] = Constant.LEARN_RATE_TRUST * value + (1.0 - Constant.LEARN_RATE_TRUST) * trust[you.id];	//提案受託期待度の更新
//		System.out.println(" / （後） = " + trust[you.id]);	
	}
	
	/**
	 * 報酬期待度の更新
	 * @param you リーダ
	 * @param isok チーム編成の成否
	 * @param subtaskRequire メンバが処理するサブタスクリソース
	 * @param leftReward リーダ以外が獲得できる残りの報酬
	 * @param leftRequireSum リーダ以外が処理するサブタスクリソースの合計
	 */
	public void feedbackExpectedReward(Agent you, boolean isok, int subtaskRequire, double leftReward, int leftRequireSum){
		calculateMemberReward(isok, subtaskRequire, leftReward, leftRequireSum);	//獲得報酬の計算
//		System.out.println(this + " の獲得報酬 = " + reward);
//		System.out.println("チーム参加提案時に提示されたサブタスクリソース = " + selectedOfferMessage.getSubTask().getRequireSum());
//		System.out.println("実際に処理を任されたサブタスクリソース = " + subtaskRequire);
//		System.out.print(this + " の " + you + " に対する報酬期待度：（前） = " + expectedReward[you.id]);
		expectedReward[you.id] = Constant.LEARN_RATE_REWARD * (reward / (double)selectedOfferMessage.getSubTask().getRequireSum()) + (1.0 - Constant.LEARN_RATE_REWARD) * expectedReward[you.id];	//報酬期待度の更新
//		System.out.println(" / （後） = " + expectedReward[you.id]);
	}
	
	/**
	 * リーダ時の獲得報酬を求める
	 * @param isok チーム編成の成否
	 */
	public void calculateLeaderReward(boolean isok){
		reward = isok ? getMarkingTask().getTaskRequireSum() * greedy : 0.0;	//獲得報酬
	}
	
	/**
	 * メンバ時の獲得報酬を求める
	 * @param isok チーム編成の成否
	 * @param subtaskRequire メンバが処理するサブタスクリソース
	 * @param leftReward リーダ以外が獲得できる残りの報酬の合計
	 * @param leftRequireSum リーダ以外が処理するサブタスクリソースの合計
	 */
	public void calculateMemberReward(boolean isok, int subtaskRequire, double leftReward, int leftRequireSum){
		reward = isok ? leftReward * ((double)subtaskRequire / (double)leftRequireSum) : 0.0;	//獲得報酬
	}
	
	/**
	 * 報酬を返す
	 * @return
	 */
	public double getReward(){
		return reward;
	}
	
	/**
	 * idとabilityを返す
	 */
	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append("id = " + id + " / ability = ");
		for(int i = 0; i < Constant.RESOURCE_NUM; i++){
			str.append(ability[i] + " ");
		}
		return str.toString();
	}
	
	/**
	 * エージェントの状態を返す
	 * @return
	 */
	public String getAgentType(){
		return "";
	}

}
