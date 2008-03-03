package edu.hawaii.ics.csdl.jupiter.model.review;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;

import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;

/**
 * Provides review model. Clients may get the singleton instance. All get's methods are guaranteed
 * to return non-null and updated information. This may used to retrieved the review issue model
 * through ReviewIssueManager, the column data model through ColumnDataModelManager.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewModel {
  /** The key for  the individual phase. */
  public static final String PHASE_INDIVIDUAL = ResourceBundleKey.PHASE_INFIVIDUAL;
  /** The key for the team phase. */
  public static final String PHASE_TEAM = ResourceBundleKey.PHASE_TEAM;
  /** The key for the rework phase. */
  public static final String PHASE_REWORK = ResourceBundleKey.PHASE_REWORK;

  private static ReviewModel theInstance = new ReviewModel();
  private PhaseManager phaseManager;
  private ReviewIdManager reviewIdManager;
  private ReviewerIdManager reviewerIdManager;
  private ProjectManager projectManager;
  private List<IReviewModelElementChangeListener> listeners;
  
  /**
   * Prohibits clients from instantiating this.
   */
  private ReviewModel() {
    this.listeners = new ArrayList<IReviewModelElementChangeListener>();
    addIReviewModelElementListener(PhaseManager.getInstance());
    addIReviewModelElementListener(ProjectManager.getInstance());
    addIReviewModelElementListener(ReviewIdManager.getInstance());
    addIReviewModelElementListener(ReviewerIdManager.getInstance());
  }
  
  /**
   * Gets the singleton instance. Throws exception if at least one of either review phase name key,
   * <code>ReviewId</code>, <code>ReviewerId</code>, or <code>IProject</code> instance is null.
   * @return the singleton instance.
   */
  public static ReviewModel getInstance() {
    theInstance.computeFields();
    return theInstance;
  }
  
  
  /**
   * Computes review phase name key string , <code>ReviewId</code>, <code>ReviewerId</code>,
   * and <code>IProject</code> instance. 
   */
  private void computeFields() {
    this.phaseManager = PhaseManager.getInstance();
    this.projectManager = ProjectManager.getInstance();
    this.reviewIdManager = ReviewIdManager.getInstance();
    this.reviewerIdManager = ReviewerIdManager.getInstance();
  }

  /**
   * Gets the <code>ProjectManager</code> instance.
   * @return the <code>ProjectManager</code> instance.
   */
  public ProjectManager getProjectManager() {
    return projectManager;
  }
  
  /**
   * Gets the <code>ReviewIdManager</code> instance.
   * @return the <code>ReviewIdManager</code> instance.
   */
  public ReviewIdManager getReviewIdManager() {
    return reviewIdManager;
  }
  
  /**
   * Gets the <code>PhaseManager</code> instance.
   * @return the <code>PhaseManager</code> instance.
   */
  public PhaseManager getPhaseManager() {
    return phaseManager;
  }
  
  /**
   * Gets the <code>ReviewerIdManager</code> instance.
   * @return the <code>ReviewerIdManager</code> instance.
   */
  public ReviewerIdManager getReviewerIdManager() {
    return reviewerIdManager;
  }
  
  /**
   * Adds the IReviewModelElementChangeListener implementing listener.
   * @param listener The IReviewModelElementChangeListener implementing listener.
   */
  void addIReviewModelElementListener(IReviewModelElementChangeListener listener) {
    this.listeners.add(listener);
  }
  
  /**
   * Notifies IReviewModelElementChangeListener implementing listeners.
   * @param object The object to be notified.
   */
  public void notifyListeners(Object object) {
    for (Iterator<IReviewModelElementChangeListener> i = this.listeners.iterator(); i.hasNext();) {
      i.next().elementChanged(object);
    }
  }
  
  /**
   * Get the <code>ReviewId</code> instance.
   * @return the <code>ReviewId</code> instance.
   * @deprecated Since 2.2.328, Use <code>getReviewIdManager</code>.
   */
  public ReviewId getReviewId() {
    return getReviewIdManager().getReviewId();
  }
  
  /**
   * Get the <code>ReviewerId</code> instance.
   * @return the <code>ReviewerId</code> instance.
   * @deprecated Since 2.2.328, Use <code>getReviewerIdManager</code>.
   */
  public ReviewerId getReviewerId() {
    return getReviewerIdManager().getReviewerId();
  }
  
  /**
   * Get the <code>String</code> phase name key.
   * @return the <code>String</code> phase name key.
   * @deprecated Since 2.2.328, Use <code>getPhaseManager</code>.
   */
  public String getPhaseNameKey() {
    return getPhaseManager().getPhaseNameKey();
  }
  
  /**
   * Get the <code>IProject</code> instance.
   * @return the <code>IProject</code> instance.
   * @deprecated Since 2.2.328, Use <code>getProjectManager</code>.
   */
  public IProject getProject() {
    return getProjectManager().getProject();
  }
}
