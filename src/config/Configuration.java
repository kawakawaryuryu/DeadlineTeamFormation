package config;

import java.text.SimpleDateFormat;
import java.util.Date;

import log.Log;
import log.logger.Type;
import main.agent.AgentFactory;
import main.agent.RationalAgentFactory;
import main.agent.StructuredAgentFactory;
import main.model.AgentActionManager;
import main.model.Model;
import main.model.TaskCopy;


public class Configuration {
	
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
	public static Model model = new TaskCopy();

	// AgentFactory
	public static AgentFactory factory;

	// AgetnactionManager
	public static AgentActionManager action = new AgentActionManager();
	
	
	// Mail
	public static final boolean MAIL_SENT = true;
	public static final String MAIL_HOST = "smtp.gmail.com";
	public static final String MAIL_FROM = "kawakawaryuryu@gmail.com";
	public static final String MAIL_TO = "ryu-kawakawa.0216@ezweb.ne.jp";
	public static final String MAIL_USER = "kawakawaryuryu";
	public static final String MAIL_PASS = "ryu-5216";

	
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

	public static void setLog(int experimentNumber) {
		if (experimentNumber == 1) {
			Log.setLogInstance(LOG_TYPE, LOG_PATH);
		}
		else {
			Log.setLogInstance(Type.NOLOG, LOG_PATH);
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
			System.err.println("そのようなAgentFactoryは存在しません");
			System.exit(-1);
		}
	}

}
