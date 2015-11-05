package fixed.main;

import java.io.IOException;

import fixed.constant.FixedConstant;
import fixed.expriment.Experiment;

public class MainMain {
	static long start = System.currentTimeMillis();	//実行スタート時間
	
	public static final int EXPERIMENT_NUM = 50;
	
	static MeasuredDataManager measure = new MeasuredDataManager();
	private static RandomManager random = new RandomManager();
	
	
	public static void main(String[] args) throws Exception {
		
		//-------------------------------------------
		//
		// コマンドライン引数 args について
		// args[1] : exprimentNumber
		// args[2] : learning or noLearning or random
		// args[3] : estimation or noEstimation
		//
		//--------------------------------------------
		FileWriteManager.setFileNumber(Integer.parseInt(args[0]));
		Experiment.set(args[1], args[2]);
		
		
		// チーム編成可視化の閾値のセット
		FixedConstant.setTeamFormationPercentageBorder();
		
		// 実験を行う
		for(int experimentNumber = 1; experimentNumber <= EXPERIMENT_NUM; experimentNumber++){
			System.out.println("Team formation at " + experimentNumber + " times starts!");
			System.out.println("Learning: " + args[1] + " / Estimation: " + args[2]);
			
			// 乱数の初期化
			random.initialize(experimentNumber);
			
			// 実験条件のファイル書き込み
			FileWriteManager.fileExplain(args[1], args[2]);
			
			// チーム編成を行う
			TeamFormationMain.teamFormation(experimentNumber);
			
			// 1回の実験で計測したデータを保存
			measure.saveAllMeasuredData();
			
//			System.out.println("Team formation at " + experimentNumber + " times finish!");
		}
		
		// 実験データを書き込み
		writeMeasuredData(args[1], args[2]);
		
		
		long stop = System.currentTimeMillis();
		int time = (int)(stop - start)/1000;
		int hour = time/3600;
		int minute = (time%3600) / 60;
		int second = (time%3600) % 60;
		System.out.println("Executed time = " + time + "秒 = " + hour + "時間" + minute + "分" + second + "秒");
		System.out.println("Learning: " + args[1] + " / Estimation: " + args[2]);
	}
	
	private static void writeMeasuredData(String learning, String estimation) throws IOException {
		FileWriteManager.fileExplain(learning, estimation);
		FileWriteManager.writeBodyOfMeasuredDataPerTurn();
		FileWriteManager.writeBodyOfTeamMeasuredData();
		FileWriteManager.writeOtherData();
	}

}
