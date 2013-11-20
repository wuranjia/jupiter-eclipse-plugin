package edu.hawaii.ics.csdl.jupiter.model.review;

import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;

/**
 * Provides reviewer information.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewerId {
  /** The reviewer automatic key to use dynamic reviewer allocation. */
  public static final String AUTOMATIC_KEY = ResourceBundleKey.ITEM_KEY_REVIEWER_AUTOMATIC;
  /** The reviewer id. */
  private String reviewerId;
  /** The reviewer name. */
  private String reviewerName;
  
  /**
   * Instantiates reviewer instance.
   * @param reviewerId the review id.
   * @param reviewerName the review name.
   */
  public ReviewerId(String reviewerId, String reviewerName) {
    this.reviewerId = reviewerId;
    this.reviewerName = reviewerName;
  }
  
  /**
   * Gets the review id.
   * @return Returns the reviewerId.
   */
  public String getReviewerId() {
    return reviewerId;
  }
  
  /**
   * Gets the reviewer name.
   * @return Returns the reviewerName.
   */
  public String getReviewerName() {
    return reviewerName;
  }
}
