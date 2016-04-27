package main;

import java.io.IOException;

import log.Log;
import constant.Constant;
import experiment.Experiment;

public class MainMain {
	static long start = System.currentTimeMillis();	//実行スタート時間
	
	public static final int EXPERIMENT_NUM = 50;
	
	static MeasuredDataManager measure = new MeasuredDataManager();
	private static RandomManager random = new RandomManager();
	
	
	public static void main(String[] args) throws Exception {
		
		//-------------------------------------------
		//
		// コマンドライン引数 args について
		// args[0] : experiment or debug
		// args[1] : exprimentNumber (learning+estimation=1, noLearning+estimation=2, learning+noEstimation=3, random+noEstimation=4)
		// args[2] : learning or noLearning or random
		// args[3] : estimation or noEstimation
		//
		//--------------------------------------------
		FileWriteManager.set(args[0], Integer.parseInt(args[1]));
		Experiment.set(args[2], args[3]);
		
		
		// チーム編成可視化の閾値のセット
		Constant.setTeamFormationPercentageBorder();
		
		// 実験を行う
		for(int experimentNumber = 1; experimentNumber <= EXPERIMENT_NUM; experimentNumber++){
			System.out.print("Team formation at " + experimentNumber + " times starts!");
			System.out.println("  Learning: " + args[2] + " / Estimation: " + args[3]);
			
			// 乱数の初期化
			random.initialize(experimentNumber);
			
			// チーム編成を行う
			TeamFormationMain.teamFormation(experimentNumber);
			
			// 1回の実験で計測したデータを保存
			measure.saveAllMeasuredData();
			
//			System.out.println("Team formation at " + experimentNumber + " times finish!");
		}
		
		// 実験データを書き込み
		writeMeasuredData(args[2], args[3]);
		
		// ログファイルclose
		Log.log.close();
		
		
		long stop = System.currentTimeMillis();
		int time = (int)(stop - start)/1000;
		int hour = time/3600;
		int minute = (time%3600) / 60;
		int second = (time%3600) % 60;
		System.out.print("Executed time = " + time + "秒 = " + hour + "時間" + minute + "分" + second + "秒");
		System.out.println("  Learning: " + args[2] + " / Estimation: " + args[3]);
	}
	
	private static void writeMeasuredData(String learning, String estimation) throws IOException {
		FileWriteManager.fileExplain(learning, estimation);
		FileWriteManager.writeBodyOfMeasuredDataPerTurn();
		FileWriteManager.writeBodyOfTeamMeasuredData();
		FileWriteManager.writeOtherData();
	}

}
