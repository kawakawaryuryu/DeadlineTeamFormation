package config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import strategy.greedy.GreedyPenaltyStrategy;
import strategy.greedy.GreedyUnchangePenaltyStrategy;
import strategy.taskreturn.TaskReturnStrategy;
import strategy.taskreturn.TaskUnmarkStrategy;
import exception.AbnormalException;
import exception.ParentException;
import factory.agent.AgentFactory;
import factory.agent.RationalAgentFactory;
import factory.agent.StructuredAgentFactory;
import log.Log;
import log.logger.Type;
import main.model.AgentActionManager;
import main.model.Model;
import main.model.MessageDelay;


public class Configuration {

	// Git Revision num
	public static String REVISION;
	
	// Date, Time
	public static String DATE;
	public static String TIME;
	
	// Log
	public static String LOG_PATH;
	public static final Type LOG_TYPE = Type.NOLOG;

	// FileWrite
	public static final String FILE_PATH = "../../Box Sync/Personal/research/";
	public static final boolean ADD_WRITE = false;

	// Java arguments
	// Experiment Setting
	public static String EXPERIMET_TYPE;
	public static int FILE_NUMBER;
	public static String LEARNING;
	public static String ESTIMATION;
	public static String AGENT_TYPE;
	public static String METHOD_NAME;


	// Model
	public static Model model = new MessageDelay();

	// AgentFactory
	public static AgentFactory factory;

	// AgetnactionManager
	public static AgentActionManager action = new AgentActionManager();

	// TaskReturnStrategy
	public static TaskReturnStrategy taskReturnStrategy = new TaskUnmarkStrategy();

	// GreedyPenaltyStrategy
	public static GreedyPenaltyStrategy greedyPenaltyStrategy = new GreedyUnchangePenaltyStrategy();
	
	
	// Mail
	public static final boolean MAIL_SENT = true;
	public static String MAIL_HOST;
	public static String MAIL_FROM;
	public static String MAIL_TO;
	public static String MAIL_USER;
	public static String MAIL_PASS;

	// slack
	public static final boolean SLACK_SENT = true;

	
	public static void setDateTime() {
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH-mm");  
		DATE = sdf1.format(date);
		TIME = sdf2.format(date);
	}

	public static void setParameters(String args[]) {
		EXPERIMET_TYPE = args[0];
		FILE_NUMBER = Integer.parseInt(args[1]);
		AGENT_TYPE = args[2];
		LEARNING = args[3];
		ESTIMATION = args[4];
		METHOD_NAME = args[5];

		LOG_PATH = "log/debug_" + FILE_NUMBER + ".log";
	}

	public static void getHeadRevision() {
		try {
			ProcessBuilder pb = new ProcessBuilder("git", "rev-parse", "HEAD");
			Process process = pb.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String ret = reader.readLine();

			REVISION = ret.substring(0, 7);
			reader.close();
		} catch (IOException e) {
			throw new ParentException(e);
		}
	}

	public static void setLog(int experimentNumber) {
		if (experimentNumber == 1) {
			Log.setLogInstance(LOG_TYPE, LOG_PATH);
		}
		else {
			Log.setLogInstance(Type.NOLOG, LOG_PATH);
		}
	}

	public static void readProperties() {
		Properties prop = new Properties();
		String file = "properties/mail.properties";
		try {
			prop.load(new FileInputStream(file));

			MAIL_HOST = prop.getProperty("host");
			MAIL_FROM = prop.getProperty("from");
			MAIL_TO = prop.getProperty("to");
			MAIL_USER = prop.getProperty("user");
			MAIL_PASS = prop.getProperty("pass");
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void setAgentFactory() {
		if (AGENT_TYPE.equals("Rational")) {
			factory = new RationalAgentFactory();
		}
		else if (AGENT_TYPE.equals("Structured")) {
			factory = new StructuredAgentFactory();
		}
		else {
			throw new AbnormalException("そのようなAgentFactoryは存在しません");
		}
	}

}
