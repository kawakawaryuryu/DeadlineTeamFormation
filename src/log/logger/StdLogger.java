package log.logger;

public class StdLogger extends Logger {
	
	public StdLogger() {}
	

	@Override
	public void debug(String msg) {
		System.out.print(msg);
	}


	@Override
	public void debugln(String msg) {
		System.out.println(msg);
	}


	@Override
	public void close() {
		// Do not anything
	}

}
