package config;

import log.Log;
import log.logger.Type;


public class Configuration {
	
	public static String path = "log/debug.log";
	public static final Type TYPE = Type.FILE_DEBUG;

	public static Log log = new Log(TYPE, path);
}
