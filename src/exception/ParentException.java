package exception;

public class ParentException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Exception parentEx;


	public ParentException(Exception e) {
		super();
		this.parentEx = e;
	}

	public ParentException(Exception e, String msg) {
		super(msg);
		this.parentEx = e;
	}

	public String getError() {
		StringBuilder error = new StringBuilder();
		error.append(getMyError() + "\n");
		error.append(getParentError() + "\n");
		return error.toString();
	}

	public String getParentError() {
		StringBuilder error = new StringBuilder();
		error.append(this.parentEx);
		error.append("\n");
		for (StackTraceElement el : this.parentEx.getStackTrace()) {
			error.append(el + "\n");
		}
		return error.toString();
	}

	public String getMyError() {
		StringBuilder error = new StringBuilder();
		error.append(this);
		error.append("\n");
		for (StackTraceElement el : this.getStackTrace()) {
			error.append(el + "\n");
		}
		return error.toString();
	}


}
