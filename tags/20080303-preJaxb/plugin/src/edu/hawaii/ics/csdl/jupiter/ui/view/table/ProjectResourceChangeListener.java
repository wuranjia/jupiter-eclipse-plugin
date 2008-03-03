package edu.hawaii.ics.csdl.jupiter.ui.view.table;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;

import edu.hawaii.ics.csdl.jupiter.event.ReviewIssueModelEvent;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides the resource change listener to listen to the project resource change.
 * The main purpose of this class is to clear all information in the table and editor view when
 * a project is closed.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ProjectResourceChangeListener implements IResourceChangeListener,
                                                             IResourceDeltaVisitor {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  /**
   * @see org.eclipse.core.resources.IResourceChangeListener
   * #resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
   */
  public void resourceChanged(IResourceChangeEvent event) {
    IResource resource = event.getResource();
    if (((event.getType() & IResourceChangeEvent.POST_CHANGE) != 0)) {
      try {
        IResourceDelta rootDelta = event.getDelta();
        // Accepts the class instance to let the instance be able to visit resource delta.
        rootDelta.accept(this);
      }
      catch (CoreException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @see org.eclipse.core.resources.IResourceDeltaVisitor
   * #visit(org.eclipse.core.resources.IResourceDelta)
   */
  public boolean visit(IResourceDelta delta) throws CoreException {
    IResource resource = delta.getResource();
    int flag = delta.getFlags();
    int kind = delta.getKind();
    if (resource instanceof IProject && ((flag & IResourceDelta.OPEN) != 0)) {
      if (!((IProject) resource).isOpen()) {
        log.debug("project was closed: " + ((IProject) resource).getName());
        ReviewIssueModel model = ReviewIssueModelManager.getInstance().getCurrentModel();
        model.clear();
        model.notifyListeners(ReviewIssueModelEvent.CLEAR);
        Display.getDefault().asyncExec(new Runnable() {
          public void run() {
            ReviewTableViewAction.NOTIFY_EDITOR.run();
          } 
        });
      }
      return false;
    }
    return true;
  }
}
