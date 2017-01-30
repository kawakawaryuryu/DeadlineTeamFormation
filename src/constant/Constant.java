package constant;

public class Constant {
	
	public static final int EXPERIMENT_NUM = 50;
	
	// タスクコピー時間
	// 現在の実装モデル的にタスク返却にコスト（時間）がかかるモデルのときはタスクコピー時間が通信遅延時間よりも小さくなければならない！
	public static final int WAIT_TURN = 3;

	// メッセージ遅延時間
	// 現在の実装モデル的にタスク返却にコスト（時間）がかかるモデルのときはタスクコピー時間が通信遅延時間よりも小さくなければならない！
	public static final int MESSAGE_DELAY = 0;
	
	/* Main */
	public static final int AGENT_NUM = 350;	//エージェント数
	public static final int TURN_NUM = 30000;	//実行ターン数
	public static final double ADD_TASK_PER_TURN = 3;	//1ターンあたりに追加する平均タスク数
	
	// TODO ADD_TASK_INTERVAL=1のときはTeamFormationMainの処理を変える必要性
	public static final int ADD_TASK_INTERVAL = 1;

	// 見積もるタスクの上限数
	public static final int ESTIMATION_TASK_LIMIT = 10;
	
	public static final int AGENT_ABILITY_MAX = 6;	//エージェントリソースの最大値（１〜１０）
	public static final int AGENT_ABILITY_INIT = 1;	//エージェントリソースの最小値
	public static final int SUBTASK_IN_TASK_NUM = 3;	//1タスク中にあるサブタスクの数（２〜４）
	public static final int SUBTASK_IN_TASK_INIT = 4;	//1タスク中のサブタスクの最小数
	public static final int SUBTASK_IN_TASK_FIXED_NUM = 5;	//サブタスク数を固定値で与えてタスクを生成するときに使用する
	public static final int RESOURCE_NUM = 2;	//リソースの種類数
	public static final int DEADLINE_MAX = 16;	//デッドラインの最大値（４〜７）
	public static final int DEADLINE_INIT = 15;	//デッドラインの初期値
	public static final int DEADLINE_MIN_2 = 2;
	public static final int DEADLINE_MIN_1 = 1;
	
	/* Task */
	public static final int TASK_REQUIRE_MAX = 10;	//タスクのリソースの最大値（１〜２０）
	public static final int TASK_REQUIRE_INIT = 1;	//タスクのリソースの最小値
	public static final int TASK_REQUIRE_MALTIPLE = 6;	//タスクリソースの倍数
	public static final int TASK_REQUIRE_FIXED_VALUE = 5;
	public static final int TASK_DEADLINE_MULTIPLE = 1;
	public static final int TASK_DISPOSAL_THREASHOLD = 3;	// チーム編成失敗によって廃棄する閾値
	
	/* Agent */
	public static final double INITIAL_GREEDY = 0.5;	//欲張り度の初期値
//	public static final double initialGreedy = Administrator.greedy_random.nextDouble();	//欲張り度の初期値
	public static final double INITIAL_TRUST_TO_MEMBER = 0.5;	//提案受託期待度の初期値
	public static final double INITIAL_EXPECTED_REWARD = 1.0;	//報酬期待度の初期値
	public static final double INITIAL_TRUST_TO_LEADER = 0.5;
	public static final double TRUST_DECREMENT_VALUE = 0.00001;	// 信頼度の減少値（フェロモンモデル）
	public static final double INITIAL_LEADER_REWARD_EXPECTATION = 1.0;
	public static final double INITIAL_MEMBER_REWARD_EXPECTATION = 1.0;
	public static double LEARN_RATE_GREEDY;	//欲張り度の学習率
	public static double LEARN_RATE_TRUST_TO_MEMBER;	//提案受託期待度の学習率
	public static double LEARN_RATE_REWARD;	//報酬期待度の学習率
	public static double LEARN_RATE_TRUST_TO_LEADER;
	public static double LEARN_RATE_LEADER_REWARD_EXPECTATION = 0.05;
	public static double LEARN_RATE_MEMBER_REWARD_EXPECTATION = 0.05;
	public static final int PAST_TEAM_NUM = 10;	//チーム履歴を保持する数
	
	 /* 信頼エージェントの閾値 */
	public static final double TRUST_LEADER_THREASHOLD = 0.5;
	public static final int TRUST_LEADER_LIMIT = 1;
	
	/* 可視化のための閾値等 */
	public static final int TEAM_FORMATION_PERCENTAGE_BORDER_NUM = 20;	//TEAM_FORMATION_PERCENTAGE_BORDERの配列数
	public static final double[] TEAM_FORMATION_PERCENTAGE_BORDER = new double[TEAM_FORMATION_PERCENTAGE_BORDER_NUM];
	
	public static final double EPSILON = 0.05;	//ε-greedyの確率0.05
	public static final double EPSILON2 = 0.05;	//メンバ候補決定のε-greedyの確率0.00625
	public static final double EPSILON_ROLE = 0.05; //役割決定時の確率
	public static final int SELECT_MEMBER_NUM = 2;	//メンバ候補の選択数
	
	public static final int MEASURE_TURN_NUM = 50 * ADD_TASK_INTERVAL;	//処理、廃棄リソース量の計測ターン数
	public static final int MEASURE_Q_TURN_NUM = 10000;	//Q値の計測ターン数
	public static final int MEASURE_Q_INIT_TURN_NUM = 10000;
	public static final int MEASURE_VISUALIZATION_TURN_NUM = TURN_NUM;	//可視化のための結果の計測ターン
	public static final int MEASURE_ROLE_REWARD_EXPECTATION_TURN_NUM = 500;	//リーダ時報酬期待度・メンバ時報酬期待度の計測ターン
	public static final int END_TURN_NUM_FOR_AVERAGE = 1000;	//平均として算出するものを計測する用のターン
	public static final int END_TURN_NUM = 500;
	
	/* エージェントの状態 */
	public static final int ARRAY_SIZE_FOR_MEASURE = TURN_NUM/MEASURE_TURN_NUM;
	public static final int ARRAY_SIZE_FOR_TEAM = SUBTASK_IN_TASK_INIT + SUBTASK_IN_TASK_NUM + 1;
	
	public static final int NO_PAST_TEAMS = -1;	// チーム履歴がないとき
	
	
	/**
	 * 学習ありのときの学習率を設定する
	 * @param greedy
	 * @param trustToMember
	 * @param reward
	 */
	public static void setLearnRateWhenLearning() {
		LEARN_RATE_GREEDY = 0.05;
		LEARN_RATE_TRUST_TO_MEMBER = 0.05;
		LEARN_RATE_REWARD = 0.05;
		LEARN_RATE_TRUST_TO_LEADER = 0.05;
	}
	
	/**
	 * 学習なし、ランダムのときの学習率を設定する
	 */
	public static void setLearnRateWhenNoLearning() {
		LEARN_RATE_GREEDY = 0.00;
		LEARN_RATE_TRUST_TO_MEMBER = 0.00;
		LEARN_RATE_REWARD = 0.00;
		LEARN_RATE_TRUST_TO_LEADER = 0.00;
	}
	
	/**
	 * 可視化のためのエージェント間のエッジの閾値を配列に代入
	 */
	public static void setTeamFormationPercentageBorder(){
		double value = 0.005d;
		for(int i = 0; i < TEAM_FORMATION_PERCENTAGE_BORDER_NUM; i++){
			TEAM_FORMATION_PERCENTAGE_BORDER[TEAM_FORMATION_PERCENTAGE_BORDER_NUM - 1 - i] = value * (i + 1);
//			Log.log.debugln("TEAM_FORMATION_PECENTAGE_BORDER[" + (TEAM_FORMATION_PERCENTAGE_BORDER_NUM - 1 - i) + "] = " + TEAM_FORMATION_PERCENTAGE_BORDER[TEAM_FORMATION_PERCENTAGE_BORDER_NUM - 1 - i]);
		}
	}
}
