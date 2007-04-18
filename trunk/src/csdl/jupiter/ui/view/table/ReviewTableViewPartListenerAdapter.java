package csdl.jupiter.ui.view.table;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import csdl.jupiter.ReviewPlugin;
import csdl.jupiter.event.ReviewEvent;

/**
 * Provides the listener to listen the part change for <code>ReviewTableView</code>.
 * @author Takuya Yamashita
 * @version $Id: ReviewTableViewPartListenerAdapter.java,v 1.1 2004/09/06 09:59:51 takuyay Exp $
 */
class ReviewTableViewPartListenerAdapter implements IPartListener {
  /**
   * Notifies this listener that the given part has been activated.
   *
   * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
   */
  public void partActivated(IWorkbenchPart part) {
    if (part instanceof ReviewTableView) {
//      log.debug("part is activate.");
      int type = ReviewEvent.TYPE_ACTIVATE;
      int kind = ReviewEvent.KIND_TABLE;
      ReviewPlugin.getInstance().notifyListeners(type, kind);
    }
  }

  /**
   * Notifies this listener that the given part has been brought to the top. 
   *
   * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
   */
  public void partBroughtToTop(IWorkbenchPart part) {
//    if (part instanceof ReviewTableView) {
//      log.debug("part is brought to top.");
//    }
  }

  /**
   * Notifies this listener that the given part has been closed.
   *
   * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
   */
  public void partClosed(IWorkbenchPart part) {
//    if (part instanceof ReviewTableView) {
//      log.debug("part is closed.");
//    }
  }

  /**
   * Notifies this listener that the given part has been deactivated.
   *
   * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
   */
  public void partDeactivated(IWorkbenchPart part) {
//    if (part instanceof ReviewTableView) {
//      log.debug("part is deactviated.");
//    }
  }

  /**
   * Notifies this listener that the given part has been opened.
   *
   * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
   */
  public void partOpened(IWorkbenchPart part) {
//    if (part instanceof ReviewTableView) {
//      log.debug("part is opened.");
//    }
  }
}
