package edu.hawaii.ics.csdl.jupiter.ui.perspective;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides a code review perspective.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewPerspectiveFactory implements IPerspectiveFactory {
  /** jupiter perspective ID. */
  public static final String ID = "edu.hawaii.ics.csdl.jupiter.ui.perspective";
  
  private static final String ID_REVIEW_EDITOR_VIEW = "edu.hawaii.ics.csdl.jupiter.ui.view.editor";
  private static final String ID_REVIEW_TABLE_VIEW = "edu.hawaii.ics.csdl.jupiter.ui.view.table";
  private static final String BOTTOM = "bottom";

  /** Jupiter logger */
  private static JupiterLogger log = JupiterLogger.getLogger();

  /**
   * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
   */
  public void createInitialLayout(IPageLayout layout) {
    defineLayout(layout);
  }

  /**
   * Defines layout.
   * @param layout the layout.
   */
  private void defineLayout(IPageLayout layout) {
    String editorArea = layout.getEditorArea();  
    
    IFolderLayout bottom = layout.createFolder(BOTTOM, IPageLayout.BOTTOM, (float) 0.70,
                                              editorArea);
    bottom.addView(ID_REVIEW_TABLE_VIEW);
    IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.RIGHT, (float) 0.50,
                                                     BOTTOM);
    bottomRight.addView(ID_REVIEW_EDITOR_VIEW);
    bottomRight.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);
//    bottomRight.addPlaceholder(JavaUI.ID_JAVADOC_VIEW);
//    bottomRight.addPlaceholder(JavaUI.ID_SOURCE_VIEW);
    bottomRight.addPlaceholder(NewSearchUI.SEARCH_VIEW_ID);
    bottomRight.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
    bottomRight.addPlaceholder(IPageLayout.ID_BOOKMARKS);
    bottomRight.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);
    
    IFolderLayout folder = layout.createFolder("topLeft", IPageLayout.LEFT, (float) 0.25,
                                               editorArea);
    // TODO Use this constant instead of the string after support for Eclipse 3.4 has been dropped
//    folder.addView(IPageLayout.ID_PROJECT_EXPLORER);
    folder.addView("org.eclipse.ui.navigator.ProjectExplorer");
    
//    folder.addView(JavaUI.ID_PACKAGES);
//    folder.addView(IPageLayout.ID_RES_NAV);
//    folder.addPlaceholder(JavaUI.ID_TYPE_HIERARCHY);
    
    layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, (float) 0.75, editorArea);
    
    layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
//    layout.addActionSet(JavaUI.ID_ACTION_SET);
//    layout.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
    layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
    
    // views - java
//    layout.addShowViewShortcut(JavaUI.ID_PACKAGES);
//    layout.addShowViewShortcut(JavaUI.ID_TYPE_HIERARCHY);
//    layout.addShowViewShortcut(JavaUI.ID_SOURCE_VIEW);
//    layout.addShowViewShortcut(JavaUI.ID_JAVADOC_VIEW);
  }
  
  /**
   * Switches to the review perspective.
   */
  public static void showPerspective() {
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
    if (window != null) {
      try {
        workbench.showPerspective(ReviewPerspectiveFactory.ID, window);
      }
      catch (WorkbenchException e) {
        log.error(e);
      }
    }
  }
}
