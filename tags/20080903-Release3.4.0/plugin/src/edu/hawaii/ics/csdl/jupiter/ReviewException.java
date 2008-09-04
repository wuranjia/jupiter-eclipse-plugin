package edu.hawaii.ics.csdl.jupiter;

/**
 * Thrown when exceptions occur during code review processing.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewException extends Exception {
  /** The serial version UID. */
  private static final long serialVersionUID = 3256728389887931954L;

  /**
   * Thrown when exceptions occur during code review processing.
   *
   * @param detailMessage A detailed string describing the error.
   */
  public ReviewException(String detailMessage) {
    super(detailMessage);
  }

  /**
   * Thrown when exceptions occur during code review processing.
   *
   * @param detailMessage A detailed string describing the error.
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *        (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or
   *        unknown.)
   */
  public ReviewException(String detailMessage, Throwable cause) {
    super(detailMessage, cause);
  }
}
