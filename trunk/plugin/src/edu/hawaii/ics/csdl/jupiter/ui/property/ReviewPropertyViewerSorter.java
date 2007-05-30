package edu.hawaii.ics.csdl.jupiter.ui.property;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;

/**
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewPropertyViewerSorter  {
  
  private static Map viewerSorterMap = new HashMap();
  private static boolean isReverse;
  /**
   * The viewer sorter which sorts by review ID.
   */
  public static final ViewerSorter REVIEW_ID = new ViewerSorter() {
    /**
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, 
     * java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object objectElement1, Object objectElement2) {
      if ((objectElement1 instanceof ReviewId) && (objectElement2 instanceof ReviewId)) {
        ReviewId reviewId1 = (ReviewId) objectElement1;
        ReviewId reviewId2 = (ReviewId) objectElement2;
        if (isReverse) {
          return reviewId2.getReviewId().compareTo(reviewId1.getReviewId());
        }
        else {
          return reviewId1.getReviewId().compareTo(reviewId2.getReviewId());
        }
      }
      else {
        return super.compare(viewer, objectElement1, objectElement2);
      }
      
    }
  };
  
  /**
   * The viewer sorter which sorts by description.
   */
  public static final ViewerSorter DESCRIPTION = new ViewerSorter() {
    /**
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, 
     * java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object objectElement1, Object objectElement2) {
      if ((objectElement1 instanceof ReviewId) && (objectElement2 instanceof ReviewId)) {
        ReviewId reviewId1 = (ReviewId) objectElement1;
        ReviewId reviewId2 = (ReviewId) objectElement2;
        if (isReverse) {
          return reviewId2.getDescription().compareTo(reviewId1.getDescription());
        }
        else {
          return reviewId1.getDescription().compareTo(reviewId2.getDescription());
        }
      }
      else {
        return super.compare(viewer, objectElement1, objectElement2);
      }
    }
  }; 
  
  /**
   * The viewer sorter which sorts by creation date.
   */
  public static final ViewerSorter DATE = new ViewerSorter() {
    /**
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, 
     * java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object objectElement1, Object objectElement2) {
      if ((objectElement1 instanceof ReviewId) && (objectElement2 instanceof ReviewId)) {
        ReviewId reviewId1 = (ReviewId) objectElement1;
        ReviewId reviewId2 = (ReviewId) objectElement2;
        if (isReverse) {
          return reviewId1.getDate().compareTo(reviewId2.getDate());
        }
        else {
          return reviewId2.getDate().compareTo(reviewId1.getDate());         
        }
      }
      else {
        return super.compare(viewer, objectElement1, objectElement2);
      }
    }
  }; 
  
  /**
   * Checks if the viewer sorters are reverse mode. Returns <code>true</code> if the sorters are
   * reverse mode. Returns <code>false</code> otherwise.
   * @return Returns <code>true</code> if the sorter is
   * reverse mode. Returns <code>false</code> otherwise.
   */
  public static boolean isReverse() {
    return isReverse;
  }
  
  /**
   * Sets the reverse status for viewer sorters. Sets <code>true</code> if the sorters are to be
   * reverse mode. Sets <code>false</code> otherwise.
   * @param reverse Sets <code>true</code> if the sorters are to be reverse mode. 
   * Sets <code>false</code> otherwise.
   */
  public static void setReverse(boolean reverse) {
    isReverse = reverse;
  }
  
  static {
    viewerSorterMap.put(ReviewPropertyPage.COLUMN_REVIEW_ID_KEY, REVIEW_ID);
    viewerSorterMap.put(ReviewPropertyPage.COLUMN_DESCRIPTION_KEY, DESCRIPTION);
    viewerSorterMap.put(ReviewPropertyPage.COLUMN_DATE_KEY, DATE);
  }
  
  /**
   * Gets the viewer sorter by the column name key.
   * @param columnKey the coumn name key to get the assocaited viewer sorter.
   * @return the viewer sorter by the column name key.
   */
  public static ViewerSorter getViewerSorter(String columnKey) {
    return (ViewerSorter) viewerSorterMap.get(columnKey);
  }
}
