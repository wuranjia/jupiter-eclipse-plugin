package csdl.jupiter.event;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import csdl.jupiter.ReviewI18n;
import csdl.jupiter.file.FileResource;
import csdl.jupiter.file.ReviewIssueXmlSerializer;
import csdl.jupiter.model.review.ReviewId;
import csdl.jupiter.model.review.ReviewModel;
import csdl.jupiter.model.review.ReviewerId;
import csdl.jupiter.model.reviewissue.ReviewIssue;
import csdl.jupiter.model.reviewissue.ReviewIssueModel;
import csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import csdl.jupiter.util.JupiterLogger;
import csdl.jupiter.util.ReviewDialog;

/**
 * Provides the listener to listen to the model elements for <code>ReviewPlugin</code>.
 * The main purpose of this listener is to write the review issue model change into the associated 
 * file.
 * @author Takuya Yamashita
 * @version $Id: ReviewIssueModelListenerAdapter.java,v 1.4 2005/03/28 14:39:31 takuyay Exp $
 */
public class ReviewIssueModelListenerAdapter implements IReviewIssueModelListener {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();
  
  /**
   * Notified when code review model was changed so that refresh the table view.
   * The main purpose of this method is to write the review issue model change into the associated 
   * file. Thus only 'add', 'delete', and 'edit' event flag are checked to see if the 
   * review issue mode is changed or not.
   *
   * @see csdl.jupiter.event.IReviewIssueModelListener
   *      #reviewIssueModelChanged(csdl.jupiter.model.ReviewIssue)
   */
  public void reviewIssueModelChanged(ReviewIssueModelEvent event) {
    int add = ReviewIssueModelEvent.ADD;
    int delete = ReviewIssueModelEvent.DELETE;
    int edit = ReviewIssueModelEvent.EDIT;
    if ((event.getEventType() & (add | delete | edit)) != 0) {
      ReviewIssue reviewIssue = event.getReviewIssue();
      if (reviewIssue != null) {
        try {
          ReviewModel reviewModel = ReviewModel.getInstance();
          IProject project = reviewModel.getProjectManager().getProject();
          ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
          ReviewIssueModelManager reviewIssueModelManager = ReviewIssueModelManager.getInstance();
          ReviewIssueModel reviewIssueModel = reviewIssueModelManager.getCurrentModel();
          String reviewer = reviewIssue.getReviewer();
          ReviewerId reviewerId = new ReviewerId(reviewer, "");
          
          IFile iFile = FileResource.getReviewFile(project, reviewId, reviewerId);
          if (iFile == null) {
            String titleKey = "ReviewDialog.noReviewFileDetermined"
                              + ".simpleConfirm.messageDialog.title";
            String title = ReviewI18n.getString(titleKey);
            String messageKey = "ReviewDialog.noReviewFileDetermined"
                                + ".simpleConfirm.messageDialog.message";
            String message = ReviewI18n.getString(messageKey);
            ReviewDialog.openSimpleComfirmMessageDialog(title, message);
            log.debug(message);
            return;
          }
          File file = iFile.getLocation().toFile();
          // Check file written permission
          if (!file.canWrite()) {
            String message = "Review file " + file + " is not writable. Changes will be lost " + 
                             "after Eclipse is closed.";
            ReviewDialog.openSimpleComfirmMessageDialog("Review Management", message);
            return;
          }
          
          ReviewIssueXmlSerializer.write(reviewId, reviewIssueModel, file);
          
          try {
            if (iFile.isDerived()) {
              iFile.refreshLocal(IResource.DEPTH_ONE, null);
            }
          }
          catch (CoreException e) {
            e.printStackTrace();
          }
        }
        catch (IOException e) {
          // should remove the ReviewIssue instance from the model because
          // it was not written in a file.
          e.printStackTrace();
        }
      }
    }
  }
}
