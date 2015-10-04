package fixed.main;

import java.io.IOException;

public class MainMain {
	static long start = System.currentTimeMillis();	//実行スタート時間
	
	public static final int EXPERIMENT_NUM = 50;
	
	static MeasuredDataManager measure = new MeasuredDataManager();
	private static RandomManager random = new RandomManager();
	
	
	public static void main(String[] args) throws Exception {
		
		// 実験を行う
		for(int experimentNumber = 1; experimentNumber <= EXPERIMENT_NUM; experimentNumber++){
			System.out.println("Team formation starts!");
			
			// 乱数の初期化
			random.initialize(experimentNumber);
			
			// 実験条件のファイル書き込み
			FileWriteManager.fileExplain();
			
			// チーム編成を行う
			TeamFormationMain.teamFormation(experimentNumber);
			
			System.out.println("Team formation at " + experimentNumber + " times finish!");
		}
		
		// 1回の実験で計測したデータを保存
		measure.saveAllMeasuredData();
		
		// 実験データを書き込み
		writeMeasuredData();
		
		
		long stop = System.currentTimeMillis();
		int time = (int)(stop - start)/1000;
		int hour = time/3600;
		int minute = (time%3600) / 60;
		int second = (time%3600) % 60;
		System.out.println("Executed time = " + time + "秒 = " + hour + "時間" + minute + "分" + second + "秒");
	}
	
	private static void writeMeasuredData() throws IOException {
		FileWriteManager.fileExplain();
		FileWriteManager.writeBodyOfMeasuredDataPerTurn();
		FileWriteManager.writeBodyOfTeamMeasuredData();
		FileWriteManager.writeOtherData();
	}

}
