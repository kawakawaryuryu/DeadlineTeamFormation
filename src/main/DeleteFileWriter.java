package main;

import java.io.File;
import java.io.IOException;

/**
 * ファイルの中身の内容を消去
 */
public class DeleteFileWriter {
	
	private static int fileNumber = 184;
	
	/**
	 * ファイルを消去
	 */
	public static void fileDeleteWrite() {
		File file = new File("data/file/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/file_" + fileNumber + ".csv");
		file.delete();
	}
	
	/**
	 * ファイルの場合は削除
	 * ディレクトリの場合は再帰
	 */
	public static void delete(File file){
		if(file.isFile()){
			file.delete();
		}
		
		else if(file.isDirectory()){
			File[] f = file.listFiles();
			for(int i = 0; i < f.length; i++){
				delete(f[i]);
			}
			file.delete();
		}
		
		else{
			System.err.println(file.getName() + "は存在しません");
		}
	}
	
	/**
	 * タスク処理量（結果）の消去
	 * @throws IOException
	 */
//	public static void taskRequireResultDeleteWrite() throws IOException {
//		File directory = new File("data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/result");
//		/* ディレクトリが存在しない場合はディレクトリを作成 */
//		if(!directory.exists()){
//			directory.mkdirs();
//		}
//		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/result/require_" + fileNumber + ".csv", false), "Shift_JIS")));
//		pw.print("");
//		pw.close();
//	}
	
	/**
	 * タスク処理リソース量の消去
	 */
	public static void taskRequireAverageDeleteWrite() {
		File file = new File("data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/average/require_average_" + fileNumber + ".csv");
		delete(file);
	}
	
	/**
	 * チーム人数ごとのチーム編成成功回数の消去
	 */
	public static void teamingSuccessDeleteWrite() {
		File file = new File("data/TaskRequireResult/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamingSuccess/teaming_success_" + fileNumber + ".csv");
		delete(file);
	}
	
	/**
	 * 欲張り度の消去
	 */
	public static void greedyDeleteWrite() {
		File file = new File("data/greedy/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/greedy_" + fileNumber + ".csv");
		delete(file);
	}
	
	/**
	 * 提案受託期待度を消去
	 * @throws IOException 
	 */
	public static void trustDeleteWrite() {
		File file = new File("data/trust/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber);
		delete(file);
	}
	
	/**
	 * 可視化のファイルを消去
	 */
	public static void visualizationDataDeleteWrite() {
		File file = new File("data/visualization/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber);
		delete(file);
	}
	
	/**
	 * 報酬期待度の消去
	 */
	public static void expectedRewardDeleteWrite() {
		File file = new File("data/expectedReward/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/" + fileNumber);
		delete(file);
	}
	
	/**
	 * 役割担当回数の消去
	 */
	public static void roleNumberDeleteWrite() {
		File file = new File("data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/role/roleNumber_" + fileNumber + ".csv");
		delete(file);
	}
	
	/**
	 * 各エージェントとのチーム編成回数記録を消去
	 */
	public static void teamingNumDeleteWrite() {
		File file = new File("data/role/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teaming/teamingNumber_" + fileNumber + ".csv");
		delete(file);
	}
	
	/**
	 * タスクキューの消去
	 */
	public static void taskQueueDeleteWrite() {
		File file = new File("data/queue/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/taskQueue_" + fileNumber + ".csv");
		delete(file);
	}
	
	/**
	 * その他情を消去
	 */
	public static void otherInfoDeleteWrite(){
		File file = new File("data/other/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/otherInfo_" + fileNumber + ".csv");
		delete(file);
	}
	
	/**
	 * チーム平均リソースを消去
	 */
	public static void teamResourceAverageDeleteWrite(){
		File file = new File("data/team/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamResourceAverage_" + fileNumber + ".csv");
		delete(file);
	}
	
	/**
	 * 状態ごとのエージェント数を消去
	 */
	public static void agentStateNumDeleteWrite(){
		File file = new File("data/state/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/agentStateNum_" + fileNumber + ".csv");
		delete(file);
	}
	
	/**
	 * ヒストグラム用のデータを消去
	 */
	public static void teamNumHistogramDeleteWrite(){
		File file = new File("data/histogram/" + Constant.AGENT_NUM + "agents/" + Constant.TURN_NUM + "t/teamNumHistogram_" + fileNumber + ".csv");
		delete(file);
	}
	
	public static void main(String[] args) {
		fileDeleteWrite();
		//taskRequireResultDeleteWrite();
		taskRequireAverageDeleteWrite();
		teamingSuccessDeleteWrite();
		greedyDeleteWrite();
		trustDeleteWrite();
		visualizationDataDeleteWrite();
		expectedRewardDeleteWrite();
		roleNumberDeleteWrite();
		teamingNumDeleteWrite();
		taskQueueDeleteWrite();
		otherInfoDeleteWrite();
		teamResourceAverageDeleteWrite();
		agentStateNumDeleteWrite();
		teamNumHistogramDeleteWrite();
	}


}
