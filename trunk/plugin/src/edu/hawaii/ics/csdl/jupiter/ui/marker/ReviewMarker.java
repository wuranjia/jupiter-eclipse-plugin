package edu.hawaii.ics.csdl.jupiter.ui.marker;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableView;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides marker utilities.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewMarker {
  /** Jupiter logger */
  private static JupiterLogger log = JupiterLogger.getLogger();

  /** The review marker type. */
  public static final String REVIEW_MARKER = "edu.hawaii.ics.csdl.jupiter.reviewmarker";
  /** The review issue id attribute. */
  public static final String ATTRIBUTE_REVIEW_ISSUE = "ReviewIssue";
  /**
   * Adds marker.
   * 
   * @param reviewIssue the <code>ReviewIssue</code> instance.
   */
  public static void addMarker(ReviewIssue reviewIssue) {
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    if (project != null && !reviewIssue.getTargetFile().equals("")) {
      Map attributes = getInitialAttributes(reviewIssue);
      try {
        createMarker(project.getFile(reviewIssue.getTargetFile()), attributes);
        log.debug("Created marker in the add marker.");
      }
      catch (CoreException e) {
        log.error("Error when adding the review marker: ", e);
      }
    }
  }

  /**
   * Removes the marker associated with the <code>ReviewIssue</code> instance.
   * 
   * @param reviewIssue the <code>ReviewIssue</code> instance.
   */
  public static void removeMarker(ReviewIssue reviewIssue) {
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    String targetFile = reviewIssue.getTargetFile();
    String issueId = reviewIssue.getIssueId();
    if (project != null && !targetFile.equals("")) {
      IFile targetIFile = project.getFile(targetFile);
      try {
        IMarker[] markers = targetIFile.findMarkers(REVIEW_MARKER, true, IResource.DEPTH_ZERO);
        for (int i = 0; i < markers.length; i++) {
          IMarker marker = markers[i];
          String reviewIssueKey = ReviewMarker.ATTRIBUTE_REVIEW_ISSUE;
          ReviewIssue reviewIssueInMarker = (ReviewIssue) marker.getAttribute(reviewIssueKey);
          if (reviewIssueInMarker != null && reviewIssueInMarker.getIssueId().equals(issueId)) {
            marker.delete();
          }
        }
      }
      catch (CoreException e) {
        log.error("Error when removing the review marker: ", e);
      }
    }
  }

  /**
   * Updates the marker associated with the <code>ReviewIssue</code> instance.
   * 
   * @param reviewIssue the <code>ReviewIssue</code> instance.
   */
  public static void updateMarker(ReviewIssue reviewIssue) {
    if (reviewIssue != null) {
      ReviewModel reviewModel = ReviewModel.getInstance();
      IProject project = reviewModel.getProjectManager().getProject();
      String targetFile = reviewIssue.getTargetFile();
      if (project != null && !targetFile.equals("")) {
        IFile targetIFile = project.getFile(targetFile);
        try {
          IMarker[] markers = targetIFile.findMarkers(REVIEW_MARKER, true, IResource.DEPTH_ZERO);
          for (int i = 0; i < markers.length; i++) {
            IMarker marker = markers[i];
            String reviewIssueKey = ATTRIBUTE_REVIEW_ISSUE;
            ReviewIssue reviewIssueInMarker = (ReviewIssue) marker.getAttribute(reviewIssueKey);
            if (reviewIssueInMarker != null
                && reviewIssueInMarker.getIssueId().equals(reviewIssue.getIssueId())) {
              marker.setAttribute(IMarker.MESSAGE, reviewIssue.getSummary());
            }
          }
        }
        catch (CoreException e) {
          log.error("Error when updating (adding) the review marker: ", e);
        }
      }
    }
  }

  /**
   * Updates all markers in a review ID.
   */
  public static void updateAllMarkersInReviewId() {
    log.debug("updateAllMarkersInReviewId are called.");
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    if (project != null) {
      clearMarkersInReviewId();
      ReviewTableView view = ReviewTableView.getActiveView();
      if (view == null) {
        return;
      }
      TableItem[] items = view.getViewer().getTable().getItems();
      for (int i = 0; i < items.length; i++) {
        ReviewIssue reviewIssue = (ReviewIssue) items[i].getData();
        if (!reviewIssue.getTargetFile().equals("")) {
          Map attributes = getInitialAttributes(reviewIssue);
          try {
            createMarker(project.getFile(reviewIssue.getTargetFile()), attributes);
            log.debug("Created marker in the update marker.");
          }
          catch (CoreException e) {
            log.error("Error when updating (adding) the review markers: ", e);
          }
        }
      }
    }
  }
  
  /**
   * Updates markers in the file. What happens is that it cleans all markers for the file
   * and re-creates new markers for the file. 
   * @param file the file that contains updating markers.
   */
  public static void updateMarkers(IFile file) {
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    if (project != null) {
      String fileFullPath = file.getFullPath().toString();
      clearMarkers(file);
      ReviewTableView view = ReviewTableView.getActiveView();
      if (view == null) {
        return;
      }
      TableItem[] items = view.getViewer().getTable().getItems();
      for (int i = 0; i < items.length; i++) {
        ReviewIssue reviewIssue = (ReviewIssue) items[i].getData();
        if (!reviewIssue.getTargetFile().equals("")) {
          Map attributes = getInitialAttributes(reviewIssue);
          try {
            IFile targetFileInReviewIssue = project.getFile(reviewIssue.getTargetFile());
            String targetFileFullPath = targetFileInReviewIssue.getFullPath().toString();
            if (targetFileFullPath.equals(fileFullPath)) {
              createMarker(file, attributes);
            }
          }
          catch (CoreException e) {
            log.error("Error when updating (adding) the review markers: ", e);
          }
        }
      }
    }
  }

  /**
   * Creates a marker on the given resource with the given type and attributes.
   * <p>
   * This method modifies the workspace (progress is not reported to the user).
   * </p>
   * 
   * @param resource the resource
   * @param attributes the attribute map (key type: <code>String</code>, value type:
   *        <code>Object</code>)
   * @throws CoreException if this method fails
   * @see IResource#createMarker(java.lang.String)
   */
  private static void createMarker(final IResource resource, final Map attributes) 
  throws CoreException {
    IWorkspaceRunnable workbenchRunnable = new IWorkspaceRunnable() {
      public void run(IProgressMonitor monitor) throws CoreException {
        IMarker marker = resource.createMarker(REVIEW_MARKER);
        marker.setAttributes(attributes);
      }
    };
    resource.getWorkspace().run(workbenchRunnable, null, IWorkspace.AVOID_UPDATE, null);
  }

  /**
   * Returns the attributes with which a newly created marker will be initialized.
   * 
   * @param reviewIssue the <code>ReviewIssue</code> instance.
   * @return the initial marker attributes
   */
  private static Map getInitialAttributes(ReviewIssue reviewIssue) {
    Map attributes = new HashMap(11);
    if (reviewIssue != null) {
      int line = 0;
      try {
        line = Integer.parseInt(reviewIssue.getLine());
      }
      catch (NumberFormatException e) {
        // ignore.
      }
      ReviewModel reviewModel = ReviewModel.getInstance();
      IProject project = reviewModel.getProjectManager().getProject();
      IFile targetFile = project.getFile(reviewIssue.getTargetFile());
      IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
      final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
      IFileEditorInput fileEditorInput = new FileEditorInput(targetFile);
      IEditorPart editorPart = page.findEditor(fileEditorInput);
      if (editorPart != null && editorPart instanceof ITextEditor) {
        ITextEditor textEditor = (ITextEditor) editorPart;
        IDocument document = textEditor.getDocumentProvider().getDocument(fileEditorInput);
        int start = -1;
        int end = -1;
        int length = 0;
        try {
          IRegion lineInformation = document.getLineInformation(line - 1);
          start = lineInformation.getOffset();
          length = lineInformation.getLength();
          end = start + length;
  
        }
        catch (BadLocationException e) {
          // ignore.
        }
  
        // marker line numbers are 1-based
        attributes.put(IMarker.LINE_NUMBER, new Integer(line));
        attributes.put(IMarker.CHAR_START, new Integer(start));
        attributes.put(IMarker.CHAR_END, new Integer(start));
        String summary = reviewIssue.getSummary() + " [" + reviewIssue.getReviewer() + "]";
        attributes.put(IMarker.MESSAGE, summary);
        attributes.put(ATTRIBUTE_REVIEW_ISSUE, reviewIssue);
      }
    }
    return attributes;
  }

  /**
   * Clears all markers in the current review ID.
   */
  public static void clearMarkersInReviewId() {
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    if (project != null) {
      try {
        log.debug("clearing all markers in the review id...");
        IMarker[] markers = project.findMarkers(REVIEW_MARKER, true, IResource.DEPTH_INFINITE);
        for (int i = 0; i < markers.length; i++) {
          markers[i].delete();
        }
      }
      catch (CoreException e) {
        log.debug(e.getMessage());
      }
    }
  }
  
  /**
   * Clears markers in the file.
   * @param file the file that contains the deleting markers.
   */
  public static void clearMarkers(IFile file) {
    try {
      log.debug("clearing markers in a file...");
      IMarker[] markers = file.findMarkers(REVIEW_MARKER, true, IResource.DEPTH_ZERO);
      for (int i = 0; i < markers.length; i++) {
        markers[i].delete();
      }
    }
    catch (CoreException e) {
      log.debug(e.getMessage());
    }
  }
}