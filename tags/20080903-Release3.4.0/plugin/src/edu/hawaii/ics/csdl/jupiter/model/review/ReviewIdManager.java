package edu.hawaii.ics.csdl.jupiter.model.review;


/**
 * Provides the singleton review id manager class.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdManager implements IReviewModelElementChangeListener {
  private static ReviewIdManager theInstance = new ReviewIdManager();
  private ReviewId reviewId;
  
  /**
   * Prohibits clients from instantiating this.
   */
  private ReviewIdManager() {
  }
  
  /**
   * Gets the singleton instance.
   * @return the singleton instance.
   */
  static ReviewIdManager getInstance() {
    return theInstance;
  }
  
  /**
   * Sets the ReviewId if the object is the instance of the ReviewId.
   * @param object The object to be notified.
   */
  public void elementChanged(Object object) {
    if (object instanceof ReviewId) {
      setReviewId((ReviewId) object);
    }
  }
  
  /**
   * Sets the <code>ReviewId</code> instance.
   * @param reviewId the <code>ReviewId</code> instance.
   */
  private void setReviewId(ReviewId reviewId) {
    this.reviewId = reviewId;
  }
  
  /**
   * Gets the <code>ReviewId</code> instance. Returns null if there is no <code>ReviewId</code>
   * instance is set.
   * @return the <code>ReviewId</code> instance.
   */
  public ReviewId getReviewId() {
    return this.reviewId;
  }
}
