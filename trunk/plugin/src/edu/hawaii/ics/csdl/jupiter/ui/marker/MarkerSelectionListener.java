package edu.hawaii.ics.csdl.jupiter.ui.marker;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableView;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableViewAction;

/**
 * Provides the marker selection listener. When a marker is selected, selects the review issue
 * located in the marker line number, then run NOTIFY_EDITOR to show the issue information in the
 * review editor view.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class MarkerSelectionListener implements ISelectionListener {
  
  /**
   * @see org.eclipse.ui.ISelectionListener
   * #selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
   */
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    if (selection instanceof ITextSelection) {
      String lineNumber = String.valueOf(((ITextSelection) selection).getStartLine() + 1);
      ReviewTableView[] tableViews = ReviewTableView.getViews();
      for (int i = 0; i < tableViews.length; i++) {
        ReviewTableView tableView = tableViews[i];
        if (tableView != null) {
          Table table = tableView.getTable();
          TableItem[] items = table.getItems();
          for (int j = 0; j < items.length; j++) {
            ReviewIssue reviewIssue = (ReviewIssue) items[j].getData();
            if (lineNumber.equals(reviewIssue.getLine())) {
              table.select(j);
              table.setTopIndex(j);
              break;
            }
          }
          ReviewTableViewAction.NOTIFY_EDITOR.run();
        }
      }
    }
  }
}
