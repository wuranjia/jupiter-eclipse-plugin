package edu.hawaii.ics.csdl.jupiter.ui.marker;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableView;

/**
 * Provides the marker selection listener. When a marker is selected, selects the review issue
 * located in the marker line number, then run NOTIFY_EDITOR to show the issue information in
 * the review editor view.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class MarkerSelectionListener implements ISelectionListener {

  /**
   * @see org.eclipse.ui.ISelectionListener #selectionChanged(org.eclipse.ui.IWorkbenchPart,
   *      org.eclipse.jface.viewers.ISelection)
   */
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    if (selection instanceof ITextSelection) {
      ITextSelection textSelection = (ITextSelection) selection;
      String lineNumber = String.valueOf(textSelection.getStartLine() + 1);
      
      ReviewModel reviewModel = ReviewModel.getInstance();
      IProject project = reviewModel.getProjectManager().getProject();

      if (project == null) {
        return;
      }

      // get the current file that is open in the editor
      IEditorInput editorInput = null;
      try {
        editorInput = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
            .getActivePage().getActiveEditor().getEditorInput();
      }
      catch (NullPointerException e) {
        return;
      }
      
      File activeFile = null;
      if (editorInput instanceof IPathEditorInput) {
        IPath path = ((IPathEditorInput) editorInput).getPath();
        activeFile = path.toFile();
        
        if (activeFile == null) {
          return;
        }
      }

      ReviewTableView[] tableViews = ReviewTableView.getViews();
      for (int i = 0; i < tableViews.length; i++) {
        ReviewTableView tableView = tableViews[i];
        if (tableView != null) {
          
          Table table = tableView.getTable();
          TableItem[] items = table.getItems();
          for (int j = 0; j < items.length; j++) {
            ReviewIssue reviewIssue = (ReviewIssue) items[j].getData();

            String targetFileRelativePath = reviewIssue.getTargetFile();
            if (targetFileRelativePath == null || "".equals(targetFileRelativePath)) {
              continue;
            }
            IFile targetFileInReviewIssue = project.getFile(targetFileRelativePath);
            File targetFile = targetFileInReviewIssue.getRawLocation().toFile();
            
            // verify that the issue is in the file that is currently open, and that the line number matches
            if (activeFile.equals(targetFile) && lineNumber.equals(reviewIssue.getLine())) {
              //tableView.getViewer().setSelection(new StructuredSelection(reviewIssue), true);
              //ReviewTableViewAction.NOTIFY_EDITOR.run();
              break;
            }
          }
        }
      }
    }
  }
}
