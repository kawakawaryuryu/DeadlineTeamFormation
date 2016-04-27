package log;

import log.logger.FileLogger;
import log.logger.Logger;
import log.logger.NoLogger;
import log.logger.StdLogger;
import log.logger.Type;

public class Log {

	private Logger logger;

	public Log(Type type, String path) {
		if (type == Type.FILE_DEBUG) {
			logger = new FileLogger(path);
		}
		else if (type == Type.STD_DEBUG) {
			logger = new StdLogger();
		}
		else if (type == Type.NOLOG) {
			logger = new NoLogger();
		}
	}



	public void debug(String msg) {
		logger.debug(msg);
	}

	public void debugln(String msg) {
		logger.debugln(msg);
	}

	public void close() {
		logger.close();
	}
}
