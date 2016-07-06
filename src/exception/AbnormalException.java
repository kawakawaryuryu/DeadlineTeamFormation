package exception;

public class AbnormalException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	public AbnormalException() {
		super();
	}

	public AbnormalException(String msg) {
		super(msg);
	}

	public String getError() {
		StringBuilder error = new StringBuilder();
		error.append(this);
		error.append("\n");
		for (StackTraceElement el : this.getStackTrace()) {
			error.append(el + "\n");
		}
		return error.toString();
	}
}
