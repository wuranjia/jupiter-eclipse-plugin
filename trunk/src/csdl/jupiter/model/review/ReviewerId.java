package csdl.jupiter.model.review;

import csdl.jupiter.util.ResourceBundleKey;

/**
 * Provides reviewer information.
 * @author Takuya Yamashita
 * @version $Id: ReviewerId.java,v 1.3 2004/09/13 12:09:07 takuyay Exp $
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
