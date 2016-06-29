package main.teamformation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import constant.Constant;
import agent.Agent;
import agent.StructuredAgent;
import file.FileWriteManager;
import file.VisualFileWriter;

public class TeamFormationFileManager {

	public TeamFormationFileManager() {

	}
	
	
	// ファイル書き込み用のPrintWriterインスタンスを取得
	PrintWriter greedyWriter;

	// タスクキュー書き込み用PrintWriterインスタンスを取得
	PrintWriter taskQueueWriter;
	
	// 信頼エージェントリスト書き込み用PrintWriterインスタンスを取得
	PrintWriter trustLeadersWriter;

	// チームリソース書き込み用PrintWriterインスタンスを取得
	PrintWriter teamResourceWriter;
	
	
	public void setFileInstances(int experimentNumber) {
		try {
			greedyWriter = getGreedyWriter(experimentNumber);
			taskQueueWriter = getTaskQueueWriter(experimentNumber);
			trustLeadersWriter
				= getTrustLeadersWriter(experimentNumber, TeamFormationInstances.getInstance().getParameter().getAgents());
			teamResourceWriter
				= getTeamResourceWriter(experimentNumber, TeamFormationInstances.getInstance().getParameter().getAgents());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	public void write(int turn, int experimentNumber, int noMarkTaskNum) {
		try{
			// 一定時間ごとに、計測データを退避
			if(turn % Constant.MEASURE_TURN_NUM == 0){
				// 計測データの配列添え字をインクリメント
				TeamFormationInstances.getInstance().getMeasure().addArrayIndex();
			}

			// Q値をファイルに書き込み
			if(turn % Constant.MEASURE_Q_TURN_NUM == 0){
				writeMeasuredDataPerTurn(greedyWriter, turn, TeamFormationInstances.getInstance().getParameter().getAgents(), experimentNumber);
			}

			// 可視化用計測データをファイルに書き込み
			if(turn % Constant.MEASURE_VISUALIZATION_TURN_NUM == 0){
				writeVisualData(TeamFormationInstances.getInstance().getParameter().getAgents(), turn, experimentNumber, TeamFormationInstances.getInstance().getMeasure().getAllSuccessTeamFormationEdge());
			}

			// タスクキューの中身を書き込み
			if(Constant.TURN_NUM - turn < Constant.END_TURN_NUM){
				writeTaskQueue(experimentNumber, turn, taskQueueWriter, noMarkTaskNum);

				// 信頼エージェントリストの書き込み
				writeTrustLeaders(experimentNumber, turn, trustLeadersWriter, TeamFormationInstances.getInstance().getParameter().getAgents());

				// チームリソースの書き込み
				writeTeamResource(experimentNumber, turn, teamResourceWriter, TeamFormationInstances.getInstance().getParameter().getAgents());
			}

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * チーム編成一回につき一回だけ書き込めばいいファイルの書き込み
	 */
	public void writeOnce(int experimentNumber) {
		try {
			// チーム編成1回のみに必要なデータを計測
			writeMeasuredData(TeamFormationInstances.getInstance().getParameter().getAgents(), experimentNumber);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(int experimentNumber, ArrayList<Agent> agents) {
		try {
			// greedyWriterをclose
			closeGreedyWrite(experimentNumber, greedyWriter);

			// taskQueueWriterをclose
			closeTaskQueueWrite(experimentNumber, taskQueueWriter);

			// trustLeadersWriterをclose
			closeTrustLeaderWrite(experimentNumber, trustLeadersWriter, agents.get(0));

			// teamResourceWriterをclose
			closeTeamResourceWrite(experimentNumber, teamResourceWriter);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private PrintWriter getGreedyWriter(int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			return FileWriteManager.writeHeaderOfGreedy(TeamFormationInstances.getInstance().getParameter().getAgents());
		}
		else{
			return null;
		}
	}
	
	private void closeGreedyWrite(int experimentNumber, PrintWriter greedyWriter) {
		if(experimentNumber == 1){
			greedyWriter.close();
		}
	}
	
	private void writeMeasuredData(ArrayList<Agent> agents, int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			FileWriteManager.writeBodyOfRoleNumber(agents);
			FileWriteManager.writeTeamFormationWithAgent(agents);
		}
	}
	
	private void writeMeasuredDataPerTurn(PrintWriter greedy, int turn, ArrayList<Agent> agents, int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			FileWriteManager.writeBodyOfGreedy(greedy, turn, agents);
			FileWriteManager.writeTrustToMember(agents, turn);
			FileWriteManager.writeRewardExpectation(agents, turn);
			if (agents.get(0) instanceof StructuredAgent) {
				FileWriteManager.writeTrustToLeader(agents, turn);
			}
		}
	}
	
	private void writeVisualData(ArrayList<Agent> agents, int turn, int experimentNumber, int successTeamFormationEdge) throws IOException {
		if(experimentNumber == 1){
			VisualFileWriter.writeVisualizedData(agents, turn, experimentNumber, successTeamFormationEdge);
			VisualFileWriter.writeVisualizedMoreData(agents, turn);
		}
	}
	
	private PrintWriter getTaskQueueWriter(int experimentNumber) throws IOException {
		if(experimentNumber == 1){
			return FileWriteManager.writeHeaderOfTaskQueue();
		}
		else{
			return null;
		}
	}
	
	private void writeTaskQueue(int experimentNumber, int turn, PrintWriter taskQueueWriter, int noMarkTaskNum) throws IOException {
		if(experimentNumber == 1){
			FileWriteManager.writeBodyOfTaskQueue(taskQueueWriter, turn, TeamFormationInstances.getInstance().getParameter().taskQueue
					, noMarkTaskNum);
		}
	}
	
	private void closeTaskQueueWrite(int experimentNumber, PrintWriter taskQueueWriter) throws IOException {
		if(experimentNumber == 1){
			taskQueueWriter.close();
		}
	}

	private PrintWriter getTrustLeadersWriter(int experimentNumber, ArrayList<Agent> agents) throws IOException {
		if (experimentNumber == 1) {
			if (agents.get(0) instanceof StructuredAgent) {
				return FileWriteManager.writeHeaderOfTrustLeaders(agents);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}

	private void writeTrustLeaders(int experimentNumber, int turn, PrintWriter trustLeadersWriter, ArrayList<Agent> agents)
			throws IOException {
		if (experimentNumber == 1) {
			if (agents.get(0) instanceof StructuredAgent) {
				FileWriteManager.writeBodyOfTrustLeaders(trustLeadersWriter, turn, agents);
			}
		}
	}

	private void closeTrustLeaderWrite(int experimentNumber, PrintWriter trustLeadersWriter, Agent agent) {
		if (experimentNumber == 1) {
			if (agent instanceof StructuredAgent) {
				trustLeadersWriter.close();
			}
		}
	}

	private PrintWriter getTeamResourceWriter(int experimentNumber, ArrayList<Agent> agents) throws IOException {
		if (experimentNumber == 1) {
			return FileWriteManager.writeHeaderOfTeamResources(agents);
		}
		else {
			return null;
		}
	}

	private void writeTeamResource(int experimentNumber, int turn, PrintWriter teamResourceWriter, ArrayList<Agent> agents) throws IOException {
		if (experimentNumber == 1) {
			FileWriteManager.writeBodyOfTeamResource(teamResourceWriter, turn, agents);
		}
	}

	private void closeTeamResourceWrite(int experimentNumber, PrintWriter teamResourceWriter) {
		if (experimentNumber == 1) {
			teamResourceWriter.close();
		}
	}
}
