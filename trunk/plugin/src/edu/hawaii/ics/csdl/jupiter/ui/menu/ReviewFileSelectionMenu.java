package edu.hawaii.ics.csdl.jupiter.ui.menu;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;

/**
 * Provides review file selection menu.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewFileSelectionMenu {

  /**
   * Creates file selection pulldown menu.
   * @param menu the menu of the parent.
   * @return the menu of the pulldown menu.
   */
  public static Menu createPulldownMenu(Menu menu) {
    create(menu);
    return menu;
  }
  
  /**
   * Creates target file selection menu.
   * @param menu the menu of the parent.
   */
  private static void create(Menu menu) {
    ReviewModel reviewModel = ReviewModel.getInstance();
    final IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId(); 
    if (project != null && reviewId != null) {
      PropertyResource propertyResource = PropertyResource.getInstance(project, false);
      String reviewIdString = reviewId.getReviewId();
      ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);
      
      // Get current editor file to highlight it if it is in the list of
      // our review files
      IFile activeFile = FileResource.getActiveFile();
      String activeFileName = null;
      if (activeFile != null) {
        activeFileName = activeFile.getProjectRelativePath().toString();        
      }

      if (reviewResource != null) {
        Set targetFileSet = reviewResource.getFileSet();
        int index = 0;
        if (targetFileSet.size() > 0) {
          for (Iterator i = targetFileSet.iterator(); i.hasNext(); index++) {
            MenuItem menuItem = new MenuItem(menu, SWT.NONE);
            String targetFile = (String) i.next();
            menuItem.setText((index + 1) + " " + targetFile);
            menuItem.setData(targetFile);
            if (targetFile.equalsIgnoreCase(activeFileName)) {
              // Highlight current editor file
              menu.setDefaultItem(menuItem);
            }

            menuItem.addSelectionListener(new SelectionAdapter() {
              public void widgetSelected(SelectionEvent event) {
                String targetFile = (String) event.widget.getData();
                if (!targetFile.equals("")) {
                  IFile targetIFile = project.getFile(targetFile);
                  int lineNumber =  0;
                  FileResource.goToLine(targetIFile, lineNumber);
                }
              }
            });
          }
        }
        else {
          MenuItem menuItem = new MenuItem(menu, SWT.NONE);
          menuItem.setText(ReviewI18n.getString("ReviewFileSelectionMenu.menuItem.none"));
        }
      }
    }
  }

}
