package hudson.plugins.view.dashboard;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marco Ambu
 */
public class DashboardLog {
   
   private static String loggerName = "DashboardView";
   
   public static void debug(String component, String msg) {
      Logger l = Logger.getLogger(loggerName);
      l.log(Level.FINEST, "[" + component + "] " + msg);
   }
   
   public static void info(String component, String msg) {
      Logger l = Logger.getLogger(loggerName);
      l.log(Level.INFO, "[" + component + "] " + msg);
   }
   
   public static void warning(String component, String msg) {
      Logger l = Logger.getLogger(loggerName);
      l.log(Level.WARNING, "[" + component + "] " + msg);
   }
   
   public static void error(String component, String msg) {
      Logger l = Logger.getLogger(loggerName);
      l.log(Level.SEVERE, "[" + component + "] " + msg);
   }

}
