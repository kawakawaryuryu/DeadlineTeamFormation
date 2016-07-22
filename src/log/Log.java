package log;

import java.io.IOException;

import exception.ParentException;
import log.logger.FileLogger;
import log.logger.Logger;
import log.logger.NoLogger;
import log.logger.StdLogger;
import log.logger.Type;

public class Log {

	private static Logger logger;

	public static Log log;

	public Log(Type type, String path) {
		if (type == Type.FILE_DEBUG) {
			try{
				logger = new FileLogger(path);
			} catch(IOException e) {
				throw new ParentException(e);
			}
		}
		else if (type == Type.STD_DEBUG) {
			logger = new StdLogger();
		}
		else if (type == Type.NOLOG) {
			logger = new NoLogger();
		}
	}

	
	public static void setLogInstance(Type type, String path) {
		log = new Log(type, path);
	}


	public void debug(String msg) {
		logger.debug(msg);
	}
	
	public void debug(Object obj) {
		logger.debug(obj.toString());
	}

	public void debugln(String msg) {
		logger.debugln(msg);
	}
	
	public void debugln() {
		logger.debugln("");
	}

	public void debugln(Object obj) {
		logger.debugln(obj.toString());
	}

	public void close() {
		logger.close();
	}
}
