package main;

import java.io.IOException;

import random.RandomManager;
import log.Log;
import main.teamformation.TeamFormationMain;
import config.Configuration;
import constant.Constant;
import experiment.Experiment;

public class MainMain {
	static long start = System.currentTimeMillis();	//実行スタート時間
	
	
	static MeasuredDataManager measure = new MeasuredDataManager();
	private static RandomManager random = new RandomManager();
	
	
	public static void main(String[] args) {
		
		//-------------------------------------------
		//
		// コマンドライン引数 args について
		// args[0] : experiment or debug
		// args[1] : exprimentNumber (learning+estimation=1, noLearning+estimation=2, learning+noEstimation=3, random+noEstimation=4)
		// args[2] : learning or noLearning or random
		// args[3] : estimation or noEstimation
		//
		//--------------------------------------------
		Configuration.setParameters(args);
		FileWriteManager.set();
		Experiment.set();
		
		
		// チーム編成可視化の閾値のセット
		Constant.setTeamFormationPercentageBorder();
		
		// 実験を行う
		for(int experimentNumber = 1; experimentNumber <= Constant.EXPERIMENT_NUM; experimentNumber++){
			System.out.print("Team formation at " + experimentNumber + " times starts!");
			System.out.println("  Learning: " + Configuration.LEARNING + " / Estimation: " + Configuration.ESTIMATION);
			
			// ログ設定
			Configuration.setLog(experimentNumber);


			// 乱数の初期化
			random.initialize(experimentNumber);
			
			// チーム編成を行う
			TeamFormationMain.teamFormation(experimentNumber);
			
			// 1回の実験で計測したデータを保存
			measure.saveAllMeasuredData();
			
			// ログファイルclose
			Log.log.close();

//			System.out.println("Team formation at " + experimentNumber + " times finish!");
		}
		
		// 実験データを書き込み
		writeMeasuredData(Configuration.LEARNING, Configuration.ESTIMATION);
		
		
		
		long stop = System.currentTimeMillis();
		int time = (int)(stop - start)/1000;
		int hour = time/3600;
		int minute = (time%3600) / 60;
		int second = (time%3600) % 60;
		System.out.print("Executed time = " + time + "秒 = " + hour + "時間" + minute + "分" + second + "秒");
		System.out.println("  Learning: " + args[2] + " / Estimation: " + args[3]);
	}
	
	private static void writeMeasuredData(String learning, String estimation) {
		try {
			FileWriteManager.fileExplain(learning, estimation);
			FileWriteManager.writeBodyOfMeasuredDataPerTurn();
			FileWriteManager.writeBodyOfTeamMeasuredData();
			FileWriteManager.writeOtherData();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
