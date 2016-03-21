package fixed.constant;

public class FixedConstant {
	
	public static final int WAIT_TURN = 3;
	
	/* Main */
	public static final int AGENT_NUM = 350;	//エージェント数
	public static final int TURN_NUM = 30000;	//実行ターン数
	public static final double ADD_TASK_PER_TURN = 3;	//1ターンあたりに追加する平均タスク数
	
	// TODO ADD_TASK_INTERVAL=1のときはTeamFormationMainの処理を変える必要性
	public static final int ADD_TASK_INTERVAL = 1;
	
	public static final int AGENT_ABILITY_MAX = 6;	//エージェントリソースの最大値（１〜１０）
	public static final int AGENT_ABILITY_INIT = 1;	//エージェントリソースの最小値
	public static final int SUBTASK_IN_TASK_NUM = 3;	//1タスク中にあるサブタスクの数（２〜４）
	public static final int SUBTASK_IN_TASK_INIT = 4;	//1タスク中のサブタスクの最小数
	public static final int RESOURCE_NUM = 2;	//リソースの種類数
	public static final int DEADLINE_MAX = 16;	//デッドラインの最大値（４〜７）
	public static final int DEADLINE_INIT = 15;	//デッドラインの初期値
	public static final int DEADLINE_MIN_2 = 2;
	public static final int DEADLINE_MIN_1 = 1;
	
	/* Task */
	public static final int TASK_REQUIRE_MAX = 10;	//タスクのリソースの最大値（１〜２０）
	public static final int TASK_REQUIRE_INIT = 1;	//タスクのリソースの最小値
	public static final int TASK_REQUIRE_MALTIPLE = 6;	//タスクリソースの倍数
	public static final int TASK_DEADLINE_MULTIPLE = 1;
	
	/* Agent */
	public static final double INITIAL_GREEDY = 0.5;	//欲張り度の初期値
//	public static final double initialGreedy = Administrator.greedy_random.nextDouble();	//欲張り度の初期値
	public static final double INITIAL_TRUST = 0.5;	//提案受託期待度の初期値
	public static final double INITIAL_EXPECTED_REWARD = 1.0;	//報酬期待度の初期値
	public static double LEARN_RATE_GREEDY;	//欲張り度の学習率
	public static double LEARN_RATE_TRUST;	//提案受託期待度の学習率
	public static double LEARN_RATE_REWARD;	//報酬期待度の学習率
	public static final int PAST_TEAM_NUM = 10;	//チーム履歴を保持する数
	
	/* 可視化のための閾値等 */
	public static final int TEAM_FORMATION_PERCENTAGE_BORDER_NUM = 20;	//TEAM_FORMATION_PERCENTAGE_BORDERの配列数
	public static final double[] TEAM_FORMATION_PERCENTAGE_BORDER = new double[TEAM_FORMATION_PERCENTAGE_BORDER_NUM];
	
	public static final double EPSILON = 0.05;	//ε-greedyの確率0.05
	public static final double EPSILON2 = 0.05;	//メンバ候補決定のε-greedyの確率0.00625
	public static final int SELECT_MEMBER_NUM = 2;	//メンバ候補の選択数
	
	public static final int MEASURE_TURN_NUM = 50 * ADD_TASK_INTERVAL;	//処理、廃棄リソース量の計測ターン数
	public static final int MEASURE_Q_TURN_NUM = 10000;	//Q値の計測ターン数
	public static final int MEASURE_Q_INIT_TURN_NUM = 10000;
	public static final int MEASURE_VISUALIZATION_TURN_NUM = TURN_NUM;	//可視化のための結果の計測ターン
	public static final int MEASURE_SUCCESS_AT_END_TURN_NUM = 1000;	//チーム編成成功回数を計測する最後のターン数
	public static final int END_TURN_NUM = 1000;
	
	/* エージェントの状態 */
	public static final int ARRAY_SIZE_FOR_MEASURE = TURN_NUM/MEASURE_TURN_NUM;
	public static final int ARRAY_SIZE_FOR_TEAM = SUBTASK_IN_TASK_INIT + SUBTASK_IN_TASK_NUM + 1;
	
	public static final int NO_PAST_TEAMS = -1;	// チーム履歴がないとき
	
	
	/**
	 * 学習ありのときの学習率を設定する
	 * @param greedy
	 * @param trust
	 * @param reward
	 */
	public static void setLearnRateWhenLearning() {
		LEARN_RATE_GREEDY = 0.05;
		LEARN_RATE_TRUST = 0.05;
		LEARN_RATE_REWARD = 0.05;
	}
	
	/**
	 * 学習なし、ランダムのときの学習率を設定する
	 */
	public static void setLearnRateWhenNoLearning() {
		LEARN_RATE_GREEDY = 0.00;
		LEARN_RATE_TRUST = 0.00;
		LEARN_RATE_REWARD = 0.00;
	}
	
	/**
	 * 可視化のためのエージェント間のエッジの閾値を配列に代入
	 */
	public static void setTeamFormationPercentageBorder(){
		double value = 0.005d;
		for(int i = 0; i < TEAM_FORMATION_PERCENTAGE_BORDER_NUM; i++){
			TEAM_FORMATION_PERCENTAGE_BORDER[TEAM_FORMATION_PERCENTAGE_BORDER_NUM - 1 - i] = value * (i + 1);
//			System.out.println("TEAM_FORMATION_PECENTAGE_BORDER[" + (TEAM_FORMATION_PERCENTAGE_BORDER_NUM - 1 - i) + "] = " + TEAM_FORMATION_PERCENTAGE_BORDER[TEAM_FORMATION_PERCENTAGE_BORDER_NUM - 1 - i]);
		}
	}
}
