package config;

import log.Log;
import log.logger.Type;


public class Configuration {
	
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
