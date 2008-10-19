package edu.hawaii.ics.csdl.jupiter.event;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewIssueXmlSerializer;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;
import edu.hawaii.ics.csdl.jupiter.util.ReviewDialog;

/**
 * Provides the listener to listen to the model elements for <code>ReviewPlugin</code>.
 * The main purpose of this listener is to write the review issue model change into the associated 
 * file.
 * @author Takuya Yamashita
 * @version $Id$
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
          
          ReviewIssueXmlSerializer.write(reviewId, reviewIssueModel, file, iFile);
          
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
        catch (XMLStreamException e) {
          // should the ReviewIssue be removed?
          e.printStackTrace();
        }
      }
    }
  }
}
