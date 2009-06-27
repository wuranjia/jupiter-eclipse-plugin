package edu.hawaii.ics.csdl.jupiter.ui.view.editor;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.event.ReviewEvent;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides the listener to listen the part change for <code>ReviewEditorView</code>.
 * @author Takuya Yamashita
 * @version $Id$
 */
class ReviewEditorViewPartListenerAdapter implements IPartListener {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  /**
   * Notifies this listener that the given part has been activated.
   *
   * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
   */
  public void partActivated(IWorkbenchPart part) {
    if (part instanceof ReviewEditorView) {
      log.debug("part is activate.");
      int type = ReviewEvent.TYPE_ACTIVATE;
      int kind = ReviewEvent.KIND_EDITOR;
      ReviewPlugin.getInstance().notifyListeners(type, kind);
    }
  }

  /**
   * Notifies this listener that the given part has been brought to the top. 
   *
   * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
   */
  public void partBroughtToTop(IWorkbenchPart part) {
    if (part instanceof ReviewEditorView) {
      log.debug("part is brought to top.");
    }
  }

  /**
   * Notifies this listener that the given part has been closed.
   *
   * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
   */
  public void partClosed(IWorkbenchPart part) {
    if (part instanceof ReviewEditorView) {
      log.debug("part is closed.");
    }
  }

  /**
   * Notifies this listener that the given part has been deactivated.
   *
   * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
   */
  public void partDeactivated(IWorkbenchPart part) {
    if (part instanceof ReviewEditorView) {
      log.debug("part is deactviated.");
      ReviewEditorViewAction.SAVE.run();
    }
  }

  /**
   * Notifies this listener that the given part has been opened.
   *
   * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
   */
  public void partOpened(IWorkbenchPart part) {
    if (part instanceof ReviewEditorView) {
      log.debug("part is opened.");
    }
  }
}
