package main;

import java.io.IOException;

import random.RandomManager;
import log.Log;
import mail.Slack;
import file.FileWriteManager;
import file.VisualFileWriter;
import main.manager.InstanceManager;
import main.teamformation.TeamFormationMain;
import config.Configuration;
import constant.Constant;
import exception.AbnormalException;
import exception.ParentException;
import experiment.Experiment;

public class Main {
	static long start = System.currentTimeMillis();	//実行スタート時間


	private static RandomManager random = new RandomManager();


	public static void main(String[] args) {

		Slack slack;

		try {

			//-------------------------------------------
			//
			// コマンドライン引数 args について
			// args[0] : experiment or debug
			// args[1] : exprimentNumber (learning+estimation=1, noLearning+estimation=2, learning+noEstimation=3, random+noEstimation=4)
			// args[2] : Type of agent (Rational, Structured, ...)
			// args[3] : learning or noLearning or random
			// args[4] : estimation or noEstimation
			// args[5] : Name for graph
			//
			//--------------------------------------------
			Configuration.setParameters(args);
			Configuration.getHeadRevision();
			Configuration.setDateTime();
			FileWriteManager.set();
			VisualFileWriter.set();
			Experiment.set();
			Configuration.setAgentFactory();
			Configuration.readProperties();


			// チーム編成可視化の閾値のセット
			Constant.setTeamFormationPercentageBorder();

			// 実験を行う
			for(int experimentNumber = 1; experimentNumber <= Constant.EXPERIMENT_NUM; experimentNumber++){
				System.out.print("Team formation at " + experimentNumber + " times starts!");
				System.out.println("  Agent: " + Configuration.AGENT_TYPE + " / Learning: " + Configuration.LEARNING + " / Estimation: " + Configuration.ESTIMATION);

				// ログ設定
				Configuration.setLog(experimentNumber);


				// 乱数の初期化
				random.initialize(experimentNumber);

				// チーム編成を行う
				TeamFormationMain.teamFormation(experimentNumber, Configuration.model, Configuration.agentFactory);

				// 1回の実験で計測したデータを保存
				InstanceManager.getInstance().getMeasure().saveAllMeasuredData();

				// ログファイルclose
				Log.log.close();

				// System.out.println("Team formation at " + experimentNumber + " times finish!");
			}

			// 実験データを書き込み
			writeMeasuredData(Configuration.LEARNING, Configuration.ESTIMATION, Configuration.AGENT_TYPE);



			long stop = System.currentTimeMillis();
			int time = (int)(stop - start)/1000;
			int hour = time/3600;
			int minute = (time%3600) / 60;
			int second = (time%3600) % 60;
			System.out.print("Executed time = " + time + "秒 = " + hour + "時間" + minute + "分" + second + "秒");
			System.out.println("  Agent: " + Configuration.AGENT_TYPE + " / Learning: " + Configuration.LEARNING + " / Estimation: " + Configuration.ESTIMATION);

			// 実験終了のメール送信
			String subject = "実験終了";
			String msg = "以下の実験が終了しました\n\n"
					+ "ExperimentType: " + Configuration.EXPERIMET_TYPE + "\n"
					+ "Date: " + Configuration.DATE + "\n"
					+ "Time: " + Configuration.TIME + "\n"
					+ "FileNumber: " + Configuration.FILE_NUMBER + "\n"
					+ "Agent Type: " + Configuration.AGENT_TYPE + "\n"
					+ "Learning: " + Configuration.LEARNING + "\n"
					+ "Estimation: " + Configuration.ESTIMATION + "\n"
					+ "Executed Time: " + hour + "時間" + minute + "分" + second + "秒" + "\n";

			slack = new Slack();
			if(Configuration.SLACK_SENT) slack.notifyToSlack(subject, msg, true);

		} catch(AbnormalException e) {
			System.err.println(e.getError());
			String subject = "エラー報告";
			String msg = e.getError();

			slack = new Slack();
			slack.notifyToSlack(subject, msg, false);

		} catch(ParentException e) {
			System.err.println(e.getError());
			String subject = "エラー報告";
			String msg = e.getError();

			slack = new Slack();
			slack.notifyToSlack(subject, msg, false);

		} catch(RuntimeException e) {
			System.err.println(e.toString());
			for (StackTraceElement el : e.getStackTrace()) {
				System.err.println("\t" + el);
			}

			String subject = "エラー報告";
			StringBuilder msg = new StringBuilder();
			msg.append(e.toString() + "\n");
			for (StackTraceElement el : e.getStackTrace()) {
				msg.append("\t" + el + "\n");
			}

			slack = new Slack();
			slack.notifyToSlack(subject, msg.toString(), false);
		}

	}

	private static void writeMeasuredData(String learning, String estimation, String agentType) {
		try {
			FileWriteManager.fileExplain(learning, estimation);
			FileWriteManager.writeBodyOfMeasuredDataPerTurn(InstanceManager.getInstance().getMeasure());
			FileWriteManager.writeBodyOfTeamMeasuredData(InstanceManager.getInstance().getMeasure());
			//FileWriteManager.writeBodyOfAgentsNum(InstanceManager.getInstance().getMeasure());
			FileWriteManager.writeOtherData(InstanceManager.getInstance().getMeasure());

			// TODO agentTypeを文字列やめる
			if(agentType.equals("Structured")) {
				FileWriteManager.writeBodyOfTrustLeadersNumEveryTurn(InstanceManager.getInstance().getMeasure());
			}
		} catch(IOException e) {
			throw new ParentException(e);
		}
	}

}
