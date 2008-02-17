package edu.hawaii.ics.csdl.jupiter.event;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ResolutionKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.SeverityKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.StatusKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.TypeKeyManager;

/**
 * Provides review issue model event. This wraps review issue entry with an event type. This
 * is used for the argument of the <code>reviewIssueModelChanged</code> in the
 * <code>IReviewIssueModelListener</code> interface.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIssueModelEvent {
  // note that a new type should be the sequence of the power 2. next should be 2^4.
  /** The merge type to merge review issues in the model. */
  public static final int MERGE = 1;
  /** The save type to save a review issue in the model. */
  public static final int ADD = 2;
  /** The delete type to delete a review issue in the model. */
  public static final int DELETE = 4;
  /** The edit type to edit a review issue in the model. */
  public static final int EDIT = 8;
  /** The clear type to clear all review issues in the model. */
  public static final int CLEAR = 16;
  /** Type category key */
  public static final String TYPE = "type";
  /** Severity category key */
  public static final String SEVERITY = "severity";
  /** Resolution category key */
  public static final String RESOLUTION = "resolution";
  /** Status category key */
  public static final String STATUS = "status";
  /** The ReviewModel instance */
  private ReviewModel reviewModel;
  /** The ReviewIssue instance */
  private ReviewIssue reviewIssue;
  /** The event type */
  private int eventType;
  /** String category key -> the String array of the item value.*/
  private Map<String, String[]> categoryToItems;
  /**
   * Instantiates this event with <code>ReviewIssue</code> instance and event type.
   * @param reviewIssue the <code>ReviewIssue</code> instance.
   * @param eventType the event type.
   */
  public ReviewIssueModelEvent(ReviewIssue reviewIssue, int eventType) {
    this.reviewModel = ReviewModel.getInstance();
    this.reviewIssue = reviewIssue;
    this.eventType = eventType;
    this.categoryToItems = new HashMap<String, String[]>();
    IProject project = this.reviewModel.getProjectManager().getProject();
    ReviewId reviewId = this.reviewModel.getReviewIdManager().getReviewId();
    if (project == null || reviewId == null) {
      return;
    }
    String[] types = TypeKeyManager.getInstance(project, reviewId).getElements();
    this.categoryToItems.put(TYPE, types);
    String[] severities = SeverityKeyManager.getInstance(project, reviewId).getElements();
    this.categoryToItems.put(SEVERITY, severities);
    String[] resolutions = ResolutionKeyManager.getInstance(project, reviewId).getElements();
    this.categoryToItems.put(RESOLUTION, resolutions);
    String[] status = StatusKeyManager.getInstance(project, reviewId).getElements();
    this.categoryToItems.put(STATUS, status);
  }

  /**
   * Gets the <code>ReviewModel</code> instance.
   * @return the <code>ReviewModel</code> instance.
   */
  public ReviewModel getReviewModel() {
    return this.reviewModel;
  }
  
  /**
   * Gets the <code>ReviewIssue</code> instance.
   * 
   * @return the <code>ReviewIssue</code> instance.
   */
  public ReviewIssue getReviewIssue() {
    return this.reviewIssue;
  }

  /**
   * Gets the event type.
   * 
   * @return the event type.
   */
  public int getEventType() {
    return this.eventType;
  }
  
  /**
   * Gets the map of the String category key -> the String ordinal array of 
   * the item value.
   * For example, 
   * <pre>
   * String[] types = (String[]) event.getCategories.get(ReviewIssueModelEvent.TYPE);
   * </pre>
   * @return the map of the String category key -> the String array of the item value.
   */
  public Map getCategories() {
    return this.categoryToItems;
  }
}