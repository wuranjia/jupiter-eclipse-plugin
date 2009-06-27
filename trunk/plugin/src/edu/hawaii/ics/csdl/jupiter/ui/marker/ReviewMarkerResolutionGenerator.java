package edu.hawaii.ics.csdl.jupiter.ui.marker;

import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.eclipse.ui.IMarkerResolutionGenerator2;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.ui.view.editor.ReviewEditorView;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableView;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableViewAction;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides the review marker resolution generator.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewMarkerResolutionGenerator implements IMarkerResolutionGenerator, 
                                                        IMarkerResolutionGenerator2 {
  private static final Image MARKER = ReviewPlugin
                              .createImageDescriptor("icons/review_marker.gif").createImage();
  private static final IMarkerResolution[] NO_RESOLUTIONS = new IMarkerResolution[0];
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  /**
   * @see org.eclipse.ui.IMarkerResolutionGenerator
   * #getResolutions(org.eclipse.core.resources.IMarker)
   */
  public IMarkerResolution[] getResolutions(IMarker marker) {
    if (!hasResolutions(marker)) {
      return NO_RESOLUTIONS; 
    }
    return new IMarkerResolution[] {new ReviewMarkerResolution(marker)};
  }

  /**
   * @see org.eclipse.ui.IMarkerResolutionGenerator2
   * #hasResolutions(org.eclipse.core.resources.IMarker)
   */
  public boolean hasResolutions(IMarker marker) {
    try {
      String markerType = marker.getType();
      return markerType.equals(ReviewMarker.REVIEW_MARKER);
    }
    catch (CoreException e) {
      return false;
    }
  }
  
  /**
   * Provides inner review marker resolution.
   *
   * @author Takuya Yamashita
   * @version $Id$
   */
  private class ReviewMarkerResolution implements IMarkerResolution, IMarkerResolution2 {
    private ReviewIssue reviewIssue;
    /**
     * Instantiates review marker resolution.
     * @param marker the marker.
     */
    private ReviewMarkerResolution(IMarker marker) {
      String reviewIssueKey = ReviewMarker.ATTRIBUTE_REVIEW_ISSUE;
      try {
        String reviewIssueId = (String) marker.getAttribute(reviewIssueKey);
        ReviewIssueModel model = ReviewIssueModelManager.getInstance().getCurrentModel();
        this.reviewIssue = model.get(reviewIssueId);
      } 
      catch (CoreException e) {
        e.printStackTrace();
      }
    }
    /**
     * @see org.eclipse.ui.IMarkerResolution#getLabel()
     */
    public String getLabel() {
      String summary = "";
      if (reviewIssue != null) {
        summary = reviewIssue.getSummary() + " [" + reviewIssue.getReviewer() + "]";
      }
      return summary;
    }

    /**
     * @see org.eclipse.ui.IMarkerResolution#run(org.eclipse.core.resources.IMarker)
     */
    public void run(IMarker marker) {
      if (reviewIssue != null) {
        String issueId = reviewIssue.getIssueId();
        ReviewIssueModel model = ReviewIssueModelManager.getInstance().getCurrentModel();
        ReviewTableView view = ReviewTableView.bringViewToTop();
        if (view == null) {
          log.warning("Review table view is null.");
          return;
        }
        Table table = view.getViewer().getTable();
        TableItem[] items = table.getItems();
        for (int i = 0; i < items.length; i++) {
          ReviewIssue reviewIssueInTable = (ReviewIssue) items[i].getData();
          if (reviewIssueInTable.getIssueId().equals(issueId)) {
            table.select(i);
          }
        }
        ReviewTableViewAction.NOTIFY_EDITOR.run();
        try {
          ReviewEditorView.bringViewToTop().setFocus();
        }
        catch (ReviewException e) {
          e.printStackTrace();
        }
      }
    }
    
    /**
     * @see org.eclipse.ui.IMarkerResolution2#getDescription()
     */
    public String getDescription() {
      String description = (reviewIssue != null) ? reviewIssue.getDescription() : "";
      return getFormatedString(description);
    }
    
    /**
     * @see org.eclipse.ui.IMarkerResolution2#getImage()
     */
    public Image getImage() {
      return MARKER;
    }
    
    /**
     * Gets the formatted description.
     * @param description the description.
     * @return the formatted description.
     */
    private String getFormatedString(String description) {
      StringTokenizer token = new StringTokenizer(description, "\r");
      StringBuffer buffer = new StringBuffer();
      for (; token.hasMoreTokens();) {
        buffer.append("<pre>").append(token.nextToken()).append("</pre>");
      }
      return buffer.toString();
    }
  }
}
