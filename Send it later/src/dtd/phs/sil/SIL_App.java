package dtd.phs.sil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;

public class SIL_App extends Application {
	public class ErrorReportHandle implements UncaughtExceptionHandler {

		private static final String PATH = "/sdcard/SIL";
		private static final String FILE_NAME = "Error_report.txt";

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
	        final Writer result = new StringWriter();
	        final PrintWriter printWriter = new PrintWriter(result);
	        ex.printStackTrace(printWriter);
	        String stacktrace = result.toString();
	        printWriter.close();
	        try {
	            BufferedWriter bos = new BufferedWriter(new FileWriter(PATH + "/" + FILE_NAME));
	            bos.write(stacktrace);
	            bos.flush();
	            bos.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, ex);
		}


		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new ErrorReportHandle());
	}
}
