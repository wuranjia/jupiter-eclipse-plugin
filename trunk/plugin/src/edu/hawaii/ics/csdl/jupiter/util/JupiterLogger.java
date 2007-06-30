package edu.hawaii.ics.csdl.jupiter.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;

/**
 * Provides the logging error function in Eclipse. Facilitates the logging in source codes. Clients
 * can use something like:
 * <pre>
 * Exception exception = ...;
 * Logger.log(Logger.ERROR, exception.getMessage(), exception);
 * </pre>
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class JupiterLogger {
  /** Singleton logger object. */
  private static JupiterLogger theInstance = new JupiterLogger();
  
  /** Console log for debug. */
  private Logger consoleLog;
  
  /**
   * Construct an empty logger object.
   *
   */
  private JupiterLogger() {
    this.consoleLog = Logger.getLogger("edu.hawaii.ics.csdl.jupiter");
    
    String levelName = System.getProperty("LogLevel", Level.WARNING.getName()); 
    this.consoleLog.setLevel(Level.parse(levelName));
  }
  
  /**
   * Gets singleton logger object. 
   * 
   * @return Logger object.
   */
  public static JupiterLogger getLogger() {
    return theInstance;
  }

  /**
   * Debugger message.
   *
   * @param message Error message.
   */
  public void debug(String message) {
    this.consoleLog.info(message);
  }

  
  /**
   * Logs an warning information with exception into Eclipse log.
   *
   * @param message Error message.
   * @param e The exception is thrown.
   */
  public void warning(String message, Exception e) {
    this.log(IStatus.WARNING, message, e);
  }

  /**
   * Logs an warning information with exception into Eclipse log.
   *
   * @param message Error message.
   */
  public void warning(String message) {
    this.log(IStatus.WARNING, message, null);
  }

  /**
   * Logs an information into Eclipse log.
   *
   * @param message Error message.
   */
  public void info(String message) {
    this.log(IStatus.INFO, message, null);
  }

  /**
   * Logs exception into eclipse error log
   *
   * @param e The exception is thrown.
   */
  public void error(Exception e) {
    this.log(IStatus.ERROR, "", e);
  }
  
  /**
   * Logs an error message and exception into eclipse error log
   *
   * @param message Error message.
   * @param e The exception is thrown.
   */
  public void error(String message, Exception e) {
    this.log(IStatus.ERROR, message, e);
  }
  
  /**
   * Logs an error message into eclipse error log. Severity should be one of the type of
   * <code>org.eclipse.core.runtime.IStatus</code>. For example, for the error, the severity
   * should be <code>IStatus.ERROR</code>. Message summary parameter might be the message, which
   * is the  <code>Exception.getMessage()</code> value.
   *
   * @param severity the severity type represented by the static field value of this
   *        <code>Logger</code>
   * @param messageSummary the message summary.
   * @param exception the exception to be thrown.
   */
  private void log(int severity, String messageSummary, Exception exception) {
    ReviewPlugin plugin = ReviewPlugin.getInstance();
    IStatus status = new Status(severity, plugin.getBundle().getSymbolicName(), IStatus.OK,
        messageSummary, exception);
    plugin.getLog().log(status);
  }
}
