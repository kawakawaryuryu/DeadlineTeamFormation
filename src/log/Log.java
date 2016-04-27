package log;

import log.logger.FileLogger;
import log.logger.Logger;
import log.logger.NoLogger;
import log.logger.StdLogger;
import log.logger.Type;

public class Log {

	public static Logger getLogger(Type type, String path) throws Exception {
		if (type == Type.FILE_DEBUG) {
			return new FileLogger(path);
		}
		else if (type == Type.STD_DEBUG) {
			return new StdLogger();
		}
		else if (type == Type.NOLOG) {
			return new NoLogger();
		}
		else {
			throw new Exception("そのようなログタイプは存在しません");
		}
	}
}
