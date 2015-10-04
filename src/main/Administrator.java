package main;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Mainを管理するクラス
 * 乱数シードはここで設定
 */
public class Administrator {
	static long start = System.currentTimeMillis();	//実行スタート時間
	
	public static final double EXPERIMENT_NUM = 50;	//実験回数
	
	public static int[] random_array = new int[50];	//乱数を入れる配列
	
	/* Main */
	public static Random agent_random;	//1000000009エージェント生成のためのランダムインスタンス
	public static Random task_random;	//3タスク生成のためのランダムインスタンス
	public static Random deadline_random;	//5デッドラインのランダムインスタンス
	public static Random initial_random;	//7initialAgentsリストシャッフルのためのランダムインスタンス
	public static Random initial_wait_random;	//initialWaitAgents
	public static Random leader_random;	//11leaderAgentsリストシャッフルのためのランダムインスタンス
	public static Random leader_wait_random;	//leaderWaitAgents
	public static Random member_random;	//13memberAgentsリストシャッフルのためのランダムインスタンス
	public static Random member_wait_random;	//memberWaitAgents
	public static Random executing_random;	//17executingAgentsリストシャッフルのためのランダムインスタンス
	public static Random deadline_sort_random;	//デッドラインの早い順に並び替えるときのランダムインスタンス
	public static Random require_sort_random;	//リソースの多い順に並び替えるときのランダムインスタンス
	public static Random poisson_random;	//ポアソン分布のランダムインスタンス
	public static Random poisson_random2;
	
	/* InitialStrategy */
	public static Random sort_random1;	//1007ソートのランダムインスタンス
	public static Random epsilon_greedy_random1;	//101
	public static Random epsilon_greedy_random2;	//103
	public static Random shuffle_random1;	//107
	public static Random shuffle_random2;
	public static Random select_random1;	//109
	public static Random select_random2;	//113
	public static Random select_random3;	//127
	public static Random select_random5;
	
	/* LeaderStrategy */
	public static Random sort_random3;	//221メンバ候補を一端並び替えるときのランダムインスタンス
	public static Random epsilon_greedy_random3;	//223ε-greedyのときのランダムインスタンス
	public static Random select_random4;	//227ランダムにメンバを決めるときのランダムインスタンス
	public static Random sort_random2;	//229配列ソート時のランダムインスタンス
	public static Random sort_random4;	//まだ割り当てられていないサブタスクをリソース順に並び替えるランダムインスタンス
	
	/* Task */
	public static Random require_random;	//1009リソースのランダムインスタンス
	
	/* Constant */
	public static Random greedy_random;	//欲張り率のランダムインスタンス
	
	
	/* タスク処理リソース量を保持する配列 */
	static double[] successTaskRequireResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] failureTaskRequireResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] failureTaskNumResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] successTeamingCountResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] failureTeamingCountResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] giveUpTeamingCountResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] teamingCountResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] constraintTimeResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] constraintTimePerAgentInATeamResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] constraintTimePerAgentResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	static double[] successTeamingCountAgainAllocationResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM];
	
	/* 総タスク処理・廃棄リソース量を保持する */
	public static double allSuccessRequireSum = 0;
	public static double allFailureRequireSum = 0;
	
	/* チームについて */
	public static double temporaryTeamSizeSum = 0;
	public static double teamSizeSum = 0;
	public static double teamExecuteTimeSum = 0;
	public static double[] teamingSuccess = new double[Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1];
	public static double[][] teamingSuccessPerTimeResult = new double[Constant.TURN_NUM/Constant.MEASURE_TURN_NUM][Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1];
	public static double[] teamConstraintTimeDifference = new double[Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1];
	public static double[] teamConstraintTimeDifferenceAverage = new double[Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1];
	
	/* 主に担当した役割の回数を保持する */
	public static double leaderMainSum = 0;
	public static double memberMainSum = 0;
	public static double neitherLeaderNorMemberSum = 0;
	
	/* 各状態にかけた時間を保持 */
	public static double initialStateTime = 0;
	public static double leaderStateTime = 0;
	public static double memberStateTime = 0;
	public static double executeStateTime = 0;
	public static double executeStateTimeOnceTime = 0;
	public static double executeStateExecutingTime = 0;
	public static double executeStateConstraintTime = 0;
	
	/* 最後の数ターンで計測した時間を保持 */
	public static double executeStateExecutingTimeAtEnd = 0;
	public static double executeStateConstraintTimeAtEnd = 0;
	
	/* 1チーム中の1人あたりの最後の数ターンで計測した処理・拘束時間を保持 */
	public static double executingTimePerAgentInATeamAtEnd = 0;
	public static double constraintTimePerAgentInATeamAtEnd = 0;
	
	/* 暇or忙しいエージェント数、1人あたりのリソースの平均 */
	public static double[] stateAgentNumAverage = new double[Constant.STATE_NUM];
	public static double[] stateAgentResourcesAverage = new double[Constant.STATE_NUM];
	
	/**
	 * 乱数シードを配列に保存
	 * @param experimentNumber 実験回数
	 * @throws IOException
	 */
	public static void makeRandom(int experimentNumber) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("prime/prime" + experimentNumber + ".txt")));
		for(int i = 0; i < 50; i++){
			String str = br.readLine();
			random_array[i] = Integer.parseInt(str);
		}
		br.close();
	}
	
	/**
	 * タスク処理リソース量の結果を配列に足していく
	 * @param successTaskRequire
	 * @param failureTaskRequire
	 * @param failureTaskNum
	 * @param successTeamingCount
	 * @param failureTeamingCount
	 * @param giveUpTeamingCount
	 * @param teamingCount
	 * @param constraintTime
	 * @param constraintTimePerAgentInATeam
	 * @param constraintTimePerAgent
	 * @param teamingSuccessPerTime
	 * @param successTeamingCountAgainAllocation
	 */
	public static void addTaskRequire(double successTaskRequire[], double failureTaskRequire[], double[] failureTaskNum
			, double successTeamingCount[], double failureTeamingCount[], double[] giveUpTeamingCount, double[] teamingCount
			, double[] constraintTime, double[] constraintTimePerAgentInATeam, double[] constraintTimePerAgent
			, double[][] teamingSuccessPerTime, double[] successTeamingCountAgainAllocation){
		for(int i = 0; i < Constant.TURN_NUM/Constant.MEASURE_TURN_NUM; i++){
			successTaskRequireResult[i] += successTaskRequire[i]; 
			failureTaskRequireResult[i] += failureTaskRequire[i];
			failureTaskNumResult[i] += failureTaskNum[i];
			successTeamingCountResult[i] += successTeamingCount[i];
			failureTeamingCountResult[i] += failureTeamingCount[i];
			giveUpTeamingCountResult[i] += giveUpTeamingCount[i];
			teamingCountResult[i] += teamingCount[i];
			constraintTimeResult[i] += constraintTime[i];
			constraintTimePerAgentInATeamResult[i] += constraintTimePerAgentInATeam[i];
			constraintTimePerAgentResult[i] += constraintTimePerAgent[i];
			for(int j = 0; j < Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1; j++){
				teamingSuccessPerTimeResult[i][j] += teamingSuccessPerTime[i][j];
			}
			successTeamingCountAgainAllocationResult[i] += successTeamingCountAgainAllocation[i];
		}
		
	}
	
	/**
	 * 総タスク処理・廃棄リソース量を保持
	 */
	public static void addAllRequire(){
		allSuccessRequireSum += Main.getAllSuccessTaskRequire();
		allFailureRequireSum += Main.getAllFailureTaskRequire();
	}
	
	/**
	 * チームについての値を保持
	 */
	public static void addTeamOtherValue(){
		temporaryTeamSizeSum += Main.getTemporaryTeamSizeAverage();
		teamSizeSum += Main.getTeamSizeAverage();
		teamExecuteTimeSum += Main.getTeamExecuteTimeAverage();
		for(int i = 0; i < Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1; i++){
			teamConstraintTimeDifference[i] += MeasureConstraintTimeData.teamConstraintTimeDifferenceAverage[i];
		}
	}
	
	/**
	 * チーム拘束時間の差分平均を求める
	 */
	public static void calculateTeamConstraintTimeAverage(){
		for(int i = 0; i < Constant.SUBTASK_IN_TASK_INIT + Constant.SUBTASK_IN_TASK_NUM + 1; i++){
			teamingSuccess[i] = MeasureConstraintTimeData.teamingSuccess[i];
			if(teamingSuccess[i] != 0){
				teamConstraintTimeDifferenceAverage[i] = teamConstraintTimeDifference[i] / (double)teamingSuccess[i];
			}
			else{
				teamConstraintTimeDifferenceAverage[i] = 0;
			}
		}
	}
	
	/**
	 * 主に役割を担当したエージェント数を保持
	 */
	public static void addRoleNum(){
		leaderMainSum += Main.getLeaderMain();
		memberMainSum += Main.getMemberMain();
		neitherLeaderNorMemberSum += Main.getNeitherLeaderNorMember();
	}
	
	/**
	 * 各状態にかけた時間を保持
	 */
	public static void addEachStateTimeAverage(){
		initialStateTime += Main.getInitialStateTimeAverage();
		leaderStateTime += Main.getLeaderStateTimeAverage();
		memberStateTime += Main.getMemberStateTimeAverage();
		executeStateTime += Main.getExecuteStateTimeAverage();
		executeStateTimeOnceTime += Main.getExecuteStateTimeOnceTimeAverage();
		executeStateExecutingTime += Main.getExecuteStateExecutingTimeAverage();
		executeStateConstraintTime += Main.getExecuteStateConstraintTimeAverage();
		executeStateExecutingTimeAtEnd += Main.getExecuteStateExecutingTimeAtEndAverage();
		executeStateConstraintTimeAtEnd += Main.getExecuteStateConstraintTimeAtEndAverage();
	}
	
	/**
	 * 最後の数ターンの処理・拘束時間を足す
	 */
	public static void addExecutingAndConstraintTimePerAgentInATeam(){
		executingTimePerAgentInATeamAtEnd += Main.getExecutingTimePerAgentInATeamAtEnd();
		constraintTimePerAgentInATeamAtEnd += Main.getConstraintTimePerAentInATeamAtEnd();
	}
	
	/**
	 * 暇or忙しいエージェント数と1人あたりのリソースを加算していく
	 * @param stateAgentNum
	 * @param stateAgentResouces
	 */
	public static void addStateAgentNumAndResouces(double[] stateAgentNum, double[] stateAgentResouces){
		for(int i = 0; i < stateAgentNum.length; i++){
			stateAgentNumAverage[i] += stateAgentNum[i];
			stateAgentResourcesAverage[i] += stateAgentResouces[i];
		}
	}

	/***********************************************************************************/
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		Constant.setTeamFormationPercentageBorder();	//可視化のためのチーム編成回数の割合の閾値をセット
		try{
			for(int experimentNumber = 1; experimentNumber <= EXPERIMENT_NUM; experimentNumber++){
//				int experimentNumber = 1;	//実験回数

				makeRandom(experimentNumber);

				/* Main */
				agent_random = new Random(random_array[0]);	//1000000009エージェント生成のためのランダムインスタンス
				task_random = new Random(random_array[1]);	//3タスク生成のためのランダムインスタンス
				deadline_random = new Random(random_array[2]);	//5デッドラインのランダムインスタンス
				initial_random = new Random(random_array[3]);	//7initialAgentsリストシャッフルのためのランダムインスタンス
				initial_wait_random = new Random(random_array[48]);
				leader_random = new Random(random_array[4]);	//11leaderAgentsリストシャッフルのためのランダムインスタンス
				leader_wait_random = new Random(random_array[47]);
				member_random = new Random(random_array[5]);	//13memberAgentsリストシャッフルのためのランダムインスタンス
				member_wait_random = new Random(random_array[46]);
				executing_random = new Random(random_array[6]);	//17executingAgentsリストシャッフルのためのランダムインスタンス
				deadline_sort_random = new Random(random_array[7]);
				require_sort_random = new Random(random_array[8]);
				poisson_random = new Random(random_array[9]);
				poisson_random2 = new Random(random_array[49]);
				
				/* InitialStrategy */
				sort_random1 = new Random(random_array[10]);	//1007ソートのランダムインスタンス
				epsilon_greedy_random1 = new Random(random_array[11]);	//101
				epsilon_greedy_random2 = new Random(random_array[12]);	//103
				shuffle_random1 = new Random(random_array[13]);	//107
				shuffle_random2 = new Random(random_array[17]);
				select_random1 = new Random(random_array[14]);	//109
				select_random2 = new Random(random_array[15]);	//113
				select_random3 = new Random(random_array[16]);	//127
				select_random5 = new Random(random_array[17]);

				/* LeaderStrategy */
				sort_random3 = new Random(random_array[20]);	//221メンバ候補を一端並び替えるときのランダムインスタンス
				epsilon_greedy_random3 = new Random(random_array[21]);	//223ε-greedyのときのランダムインスタンス
				select_random4 = new Random(random_array[22]);	//227ランダムにメンバを決めるときのランダムインスタンス
				sort_random2 = new Random(random_array[23]);	//229配列ソート時のランダムインスタンス
				sort_random4 = new Random(random_array[24]);
				
				/* Task */
				require_random = new Random(random_array[30]);	//1009リソースのランダムインスタンス
				
				/* Constant */
				greedy_random = new Random(random_array[40]);	//欲張り率のランダムインスタンス

				System.out.println("チーム編成を行います");
				Main.teamFormation(experimentNumber);	//チーム編成を始める
				
				addTaskRequire(MeasureDataPerTime.successTaskRequireArray, MeasureDataPerTime.failureTaskRequireArray
						, MeasureDataPerTime.failureTaskNumArray, MeasureDataPerTime.successTeamingCountArray
						, MeasureDataPerTime.failureTeamingCountArray, MeasureDataPerTime.giveUpTeamingCountArray
						, MeasureDataPerTime.teamingCountArray, MeasureDataPerTime.constraintTimeInATeamArray
						, MeasureDataPerTime.constraintTimePerAgentInATeamArray, MeasureDataPerTime.constraintTimePerAgentArray
						, MeasureConstraintTimeData.teamingSuccessPerTimeArray
						, MeasureDataPerTime.successTeamingCountAgainAllocationEndArray);
				addTeamOtherValue();	//チームサイズの保持
				addRoleNum();	//役割回数の保持
				addAllRequire();	//処理・廃棄リソース量の保持
				addEachStateTimeAverage();	//各状態にかけた時間を保持
				addExecutingAndConstraintTimePerAgentInATeam();	//最後の数ターンの処理・拘束時間を足して保持

				System.out.println(experimentNumber + "回目の計測終了");
			}
			
			/* タスク処理量の平均の書き込み、チーム人数ごとのチーム編成成功回数 */
			PrintWriter averageRequire = FileWriter.averageRequireSumFirst(EXPERIMENT_NUM);
			PrintWriter teamingSuccessPerTimeWriter = FileWriter.teamingSuccessPerTimeFirst(EXPERIMENT_NUM);
			for(int i = 0; i < Constant.TURN_NUM/Constant.MEASURE_TURN_NUM; i++){
				int turn = Constant.MEASURE_TURN_NUM * (i + 1);
				FileWriter.requireSumWrite(averageRequire, turn, successTaskRequireResult[i]/EXPERIMENT_NUM
						, failureTaskRequireResult[i]/EXPERIMENT_NUM, failureTaskNumResult[i]/EXPERIMENT_NUM
						, successTeamingCountResult[i]/EXPERIMENT_NUM, failureTeamingCountResult[i]/EXPERIMENT_NUM
						, giveUpTeamingCountResult[i]/EXPERIMENT_NUM, teamingCountResult[i]/EXPERIMENT_NUM
						, constraintTimeResult[i]/EXPERIMENT_NUM, constraintTimePerAgentInATeamResult[i]/EXPERIMENT_NUM
						, constraintTimePerAgentResult[i]/EXPERIMENT_NUM
						, successTeamingCountAgainAllocationResult[i]/EXPERIMENT_NUM);
				
				FileWriter.teamingSuccessPerTimeWrite(teamingSuccessPerTimeWriter, turn, teamingSuccessPerTimeResult[i]);
			}
			
			averageRequire.close();
			teamingSuccessPerTimeWriter.close();
			
			calculateTeamConstraintTimeAverage();
			/* その他情報を書き込み */
			FileWriter.otherInfoWrite(temporaryTeamSizeSum/EXPERIMENT_NUM, teamSizeSum/EXPERIMENT_NUM
					, teamExecuteTimeSum/EXPERIMENT_NUM, teamingSuccess, teamConstraintTimeDifferenceAverage
					, leaderMainSum/EXPERIMENT_NUM, memberMainSum/EXPERIMENT_NUM
					, neitherLeaderNorMemberSum/EXPERIMENT_NUM, initialStateTime/EXPERIMENT_NUM, leaderStateTime/EXPERIMENT_NUM
					, memberStateTime/EXPERIMENT_NUM, executeStateTime/EXPERIMENT_NUM
					, executeStateTimeOnceTime/EXPERIMENT_NUM, executeStateExecutingTime/EXPERIMENT_NUM
					, executeStateConstraintTime/EXPERIMENT_NUM, executeStateExecutingTimeAtEnd/EXPERIMENT_NUM
					, executeStateConstraintTimeAtEnd/EXPERIMENT_NUM, executingTimePerAgentInATeamAtEnd/EXPERIMENT_NUM
					, constraintTimePerAgentInATeamAtEnd/EXPERIMENT_NUM, allSuccessRequireSum/EXPERIMENT_NUM
					, allFailureRequireSum/EXPERIMENT_NUM, stateAgentNumAverage, stateAgentResourcesAverage);
				
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		long stop = System.currentTimeMillis();	//実行終了時間
		int time = (int)(stop - start)/1000;
		int hour = time/3600;
		int minute = (time%3600) / 60;
		int second = (time%3600) % 60;
		System.out.println("実行時間 = " + time + "秒 = " + hour + "時間" + minute + "分" + second + "秒");
	}

}
