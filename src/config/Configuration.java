package config;

import java.text.SimpleDateFormat;
import java.util.Date;

import log.Log;
import log.logger.Type;
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
	public static final String FILE_PATH = "../../Dropbox/research/";
	public static final boolean ADD_WRITE = false;

	// Java arguments
	// Experiment Setting
	public static String EXPERIMET_TYPE;
	public static int FILE_NUMBER;
	public static String LEARNING;
	public static String ESTIMATION;


	// Model
	public static Model model = new TaskCopy();

	// AgetnactionManager
	public static AgentActionManager action = new AgentActionManager();
	
	
	// Mail
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
		LEARNING = args[2];
		ESTIMATION = args[3];

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

}
