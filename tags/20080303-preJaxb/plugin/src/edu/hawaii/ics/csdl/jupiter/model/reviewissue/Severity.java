package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

/**
 * Provides severity information Clients can instantiate this with a key and its ordinal number.
 * The ordinal number is used for the comparison of this instances.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class Severity {
  /** The key of the severity. */
  private String key;
  /** Assigns an ordinal to this key. */
  private int ordinal;

  /**
   * Constructor for the Severity object. Sets the key string and its ordinal number.
   *
   * @param key the severity of the code review.
   * @param ordinal the ordinal of the key.
   */
  public Severity(String key, int ordinal) {
    this.key = key;
    this.ordinal = ordinal;
  }

  /**
   * Gets the key for the severity value.
   *
   * @return the key for the severity value.
   */
  public String getKey() {
    return this.key;
  }

  /**
   * Compare this object by the ordinal number.
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object object) {
    return ordinal - ((Severity) object).ordinal;
  }
}
