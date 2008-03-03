package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewIssueXmlSerializer;
import edu.hawaii.ics.csdl.jupiter.file.Ver1ReviewHelper;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;

/**
 * Provides review issue model manager.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIssueModelManager {
  private static ReviewIssueModelManager theInstance = new ReviewIssueModelManager();
  private ReviewIssueModel model;
  
  /**
   * Prohibits clients from instantiating this.
   */
  private ReviewIssueModelManager() {
    this.model = new ReviewIssueModel();
  }
  
  /**
   * Gets the singleton instance.
   * @return the singleton instance.
   */
  public static ReviewIssueModelManager getInstance() {
    return theInstance;
  }
  
  /**
   * Gets the <code>ReviewIssueModel</code> instance. Note that 
   * <code>IllegalArgumentException</code> will be thrown if either <code>IProject</code> or 
   * <code>ReviewId</code> were null.
   * 
   * @param project the project.
   * @param reviewId the review id.
   * @return the <code>ReviewIssueModel</code> instance.
   */
  public ReviewIssueModel getModel(IProject project, ReviewId reviewId) {
    if (project == null) {
      throw new IllegalArgumentException("project is null");
    }
    if (reviewId == null) {
      throw new IllegalArgumentException("reviewId is null");
    }
    this.model.clear();
    IFile[] reviewIFiles = FileResource.getReviewIFiles(project, reviewId);
    ReviewIssueXmlSerializer.read(reviewId, this.model, reviewIFiles);
    // compatible to the version 1.
    if (this.model.size() <= 0) {
      Ver1ReviewHelper.read(reviewId, this.model, reviewIFiles);
    }
    this.model.sortByPreviousComparator();
    this.model.clearComparator();
    return this.model;
  }
  
  /**
   * Gets the current model.
   * @return the current model.
   */
  public ReviewIssueModel getCurrentModel() {
    return this.model;
  }
}

