package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

/**
 * Provides type information. Clients can instantiate this with a key and its ordinal number.
 * The ordinal number is used for the comparison of this instances.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class Type implements Comparable {
  /** The key of the type. */
  private String key;
  /** Assigns an ordinal to this key. */
  private int ordinal;

  /**
   * Constructor for the Type object. Sets the Type key and its ordinal number.
   *
   * @param key the key of the code review.
   * @param ordinal the ordinal of the key.
   */
  public Type(String key, int ordinal) {
    this.key = key;
    this.ordinal = ordinal;
  }

  /**
   * Gets the key for the type value.
   *
   * @return the key for the type value.
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
    return ordinal - ((Type) object).ordinal;
  }
}
