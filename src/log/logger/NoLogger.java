package log.logger;

public class NoLogger extends Logger {
	
	public NoLogger() {}

	@Override
	public void debug(String msg) {
		// Do not anything
	}

	@Override
	public void debugln(String msg) {
		// Do not anything
	}

	@Override
	public void close() {
		// Do not anything
	}

}
