package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

/**
 * Provides resolution information. Clients can instantiate this with a key and its ordinal number.
 * The ordinal number is used for the comparison of this instances.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class Resolution implements Comparable<Resolution> {
  /** The key of the resolution. */
  private String key;
  /** Assigns an ordinal to this key. */
  private int ordinal;

  /**
   * Constructor for the Resolution object. Sets the key string and its ordinal number.
   *
   * @param key the key of the code review.
   * @param ordinal the ordinal of the key.
   */
  public Resolution(String key, int ordinal) {
    this.key = key;
    this.ordinal = ordinal;
  }

  /**
   * Gets the key for the resolution value.
   *
   * @return the key for the resolution value.
   */
  public String getKey() {
    return this.key;
  }

  /**
   * Compare this object by the ordinal number.
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Resolution resolution) {
    return ordinal - resolution.ordinal;
  }
}
