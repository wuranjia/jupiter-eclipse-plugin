package csdl.jupiter.event;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import csdl.jupiter.ui.marker.MarkerTextPartListener;
import csdl.jupiter.util.JupiterLogger;

/**
 *
 * @author Takuya Yamashita
 * @version $Id: WindowListenerAdapter.java,v 1.2 2005/03/26 07:13:36 takuyay Exp $
 */
public class WindowListenerAdapter implements IWindowListener {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  /**
   * @see org.eclipse.ui.IWindowListener#windowActivated(org.eclipse.ui.IWorkbenchWindow)
   */
  public void windowActivated(IWorkbenchWindow window) {
  }

  /**
   * @see org.eclipse.ui.IWindowListener#windowDeactivated(org.eclipse.ui.IWorkbenchWindow)
   */
  public void windowDeactivated(IWorkbenchWindow window) {
  }

  /**
   * @see org.eclipse.ui.IWindowListener#windowClosed(org.eclipse.ui.IWorkbenchWindow)
   */
  public void windowClosed(IWorkbenchWindow window) {
    log.debug("the window was closed.");
    
  }

  /**
   * @see org.eclipse.ui.IWindowListener#windowOpened(org.eclipse.ui.IWorkbenchWindow)
   */
  public void windowOpened(IWorkbenchWindow window) {
    log.debug("New window was opened.");
    IWorkbenchPage page = window.getActivePage();
    try {
      page.addSelectionListener(new ReviewSelectionListener());
      page.addPartListener(new MarkerTextPartListener());
    }
    catch (NullPointerException e) {
      log.warning("Could not register either selection listener or part listener.");
    }
  }

}
