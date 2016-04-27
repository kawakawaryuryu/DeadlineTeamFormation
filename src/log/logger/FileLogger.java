package log.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLogger extends Logger {

	private PrintWriter pw;
	
	public FileLogger(String path) {
		try {
			File file = new File(path);
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}


	@Override
	public void debug(String msg) {
		pw.print(msg);
	}


	@Override
	public void debugln(String msg) {
		pw.println(msg);
	}


	@Override
	public void close() {
		pw.close();
	}

}
