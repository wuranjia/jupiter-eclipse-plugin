package edu.hawaii.ics.csdl.jupiter.model.review;

/**
 * Provides the singleton reviewer id manager class.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewerIdManager implements IReviewModelElementChangeListener {
  private static ReviewerIdManager theInstance = new ReviewerIdManager();
  private ReviewerId reviewerId;
  
  /**
   * Prohibits clients from instantiating this.
   */
  private ReviewerIdManager() {
  }
  
  /**
   * Gets the singleton instance.
   * @return the singleton instance.
   */
  static ReviewerIdManager getInstance() {
    return theInstance;
  }
  
  /**
   * Sets the ReviewerId if the object is the instance of the ReviewerId.
   * @param object The object to be notified.
   */
  public void elementChanged(Object object) {
    if (object instanceof ReviewerId) {
      setReviewerId((ReviewerId) object);
    }
  }
  
  /**
   * Sets the <code>ReviewerId</code> instance.
   * @param reviewerId the <code>ReviewerId</code> instance.
   */
  private void setReviewerId(ReviewerId reviewerId) {
    this.reviewerId = reviewerId;
  }
  
  /**
   * Gets the <code>ReviewerId</code> instance. Returns null if there is no <code>ReviewerId</code>
   * instance is set.
   * @return the <code>ReviewerId</code> instance.
   */
  public ReviewerId getReviewerId() {
    return this.reviewerId;
  }
}
