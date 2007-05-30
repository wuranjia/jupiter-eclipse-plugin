package edu.hawaii.ics.csdl.jupiter.ui.marker;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import edu.hawaii.ics.csdl.jupiter.event.ReviewIssueModelEvent;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides the resource change listener to check whether markers are moved along with the
 * text document modification.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class MarkerResourceChangeListener 
             implements IResourceChangeListener, IResourceDeltaVisitor {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

    /**
     * Implements resource delta for the active file and accepts the implementing
     * resource data visitor to check the marker change associating with the resource change.
     * This method is called by platform when resources are changed.
     * 
     * @param event A resource change event to describe changes to resources.
     */
    public void resourceChanged(IResourceChangeEvent event) {
      IResource resource = event.getResource();
      if (((event.getType() & IResourceChangeEvent.POST_CHANGE) != 0)) {
        try {
          IFile activeFile = FileResource.getActiveFile();
          if (activeFile == null) {
            return;
          }
          IResourceDelta rootDelta = event.getDelta();
          // only interested in the active file.
          IResourceDelta resourceDelta = rootDelta.findMember(activeFile.getFullPath());
          if (resourceDelta == null) {
            return;
          }
          resourceDelta.accept(this);
        }
        catch (CoreException e) {
          e.printStackTrace();
        }
      }
    }

    /**
     * Implements marker delta to check which markers are changed and retrieve the new line number
     * and set it to the <code>ReviewIssue</code> instance in the <code>ReviewIssueModel</code>. 
     * This method is called by EclipseSensorPlugin instance. Note that
     * <code>true</code> is returned if the parameter of IResourceDelta instance has children.
     * 
     * @param delta IResourceDelta instance to contains delta resource.
     * @return true if the resource delta's children should be visited; false if they should be
     *         skipped.
     * @throws CoreException if this method fails. Reasons include:
     * <ul>
     * <li> This marker does not exist.</li>
     * </ul>
     */
    public boolean visit(IResourceDelta delta) throws CoreException {
     // only interested in changed resources (not added or removed)
      if ((delta.getKind() & IResourceDelta.CHANGED) == 0) {
//         log.debug("added or removed.");
         return true;
      }
      //only interested in content changes
      if ((delta.getFlags() & IResourceDelta.CONTENT) == 0) {
//         log.debug("not content change.");
         return true;
      }
      IMarkerDelta[] markerdeltas = delta.getMarkerDeltas();
      boolean isReviewMarker = false;
      for (int i = 0; i < markerdeltas.length; i++) {
        IMarkerDelta markerDelta = markerdeltas[i];
        String markerType = markerDelta.getType();
        if (!markerDelta.getType().equals(ReviewMarker.REVIEW_MARKER)) {
          continue;
        }
        IMarker marker = markerDelta.getMarker();
        Integer oldLineNumber = (Integer) markerDelta.getAttribute(IMarker.LINE_NUMBER);
        Integer newLineNumber = (Integer) marker.getAttribute(IMarker.LINE_NUMBER);
        if (newLineNumber == null || oldLineNumber == null 
            || newLineNumber.equals(oldLineNumber)) {
          continue;
        }
        log.debug("old marker line number: " + oldLineNumber.intValue());
        log.debug("new marker line number: " + newLineNumber.intValue());
        isReviewMarker = true;
        String reviewIssueKey = ReviewMarker.ATTRIBUTE_REVIEW_ISSUE;
        ReviewIssue savingReviewIssue = (ReviewIssue) marker.getAttribute(reviewIssueKey);
        savingReviewIssue.setLine(newLineNumber.intValue() + "");
      }
      if (isReviewMarker) {
        log.debug("review marker was moved along with resource change.");
        ReviewIssueModelManager reviewIssueModelManager = ReviewIssueModelManager.getInstance();
        ReviewIssueModel reviewIssueModel = reviewIssueModelManager.getCurrentModel();
        reviewIssueModel.notifyListeners(ReviewIssueModelEvent.EDIT);
      }
      return true; // visit the children
    }

}
