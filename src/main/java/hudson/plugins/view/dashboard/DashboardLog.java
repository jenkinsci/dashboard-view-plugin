package hudson.plugins.view.dashboard;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marco Ambu
 */
public class DashboardLog {
   
   private static String loggerName = "DashboardView";
   
   public static void debug(String msg) {
      Logger l = Logger.getLogger(loggerName);
      l.log(Level.FINEST, msg);
   }
   
   public static void info(String msg) {
      Logger l = Logger.getLogger(loggerName);
      l.log(Level.INFO, msg);
   }
   
   public static void warning(String msg) {
      Logger l = Logger.getLogger(loggerName);
      l.log(Level.WARNING, msg);
   }
   
   public static void error(String msg) {
      Logger l = Logger.getLogger(loggerName);
      l.log(Level.SEVERE, msg);
   }
}
