package log.logger;

public abstract class Logger {

	public abstract void debug(String msg);
	
	public abstract void debugln(String msg);
	
	public abstract void close();
	
}
