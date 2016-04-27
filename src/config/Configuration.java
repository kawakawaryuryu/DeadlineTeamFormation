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
	public static int EXPERIMENT_NUMBER;
	public static String LEARNING;
	public static String ESTIMATION;



	public static void setParameters(String args[]) {
		EXPERIMET_TYPE = args[0];
		EXPERIMENT_NUMBER = Integer.parseInt(args[1]);
		LEARNING = args[2];
		ESTIMATION = args[3];

		LOG_PATH = "log/debug_" + EXPERIMENT_NUMBER + ".log";
	}

}
