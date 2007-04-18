package csdl.jupiter.ui.property;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import csdl.jupiter.ReviewI18n;
import csdl.jupiter.model.review.ReviewId;

/**
 * Provides config property label provider.
 * @author Takuya Yamashita
 * @version $Id: ReviewPropertyLabelProvider.java,v 1.1 2004/08/19 12:40:32 takuyay Exp $
 */
public class ReviewPropertyLabelProvider extends LabelProvider implements ITableLabelProvider {
   
  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  /**
   * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
   */
  public Image getColumnImage(Object element, int columnIndex) {
    return null;
  }

  /**
   * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
   */
  public String getColumnText(Object element, int columnIndex) {
    String columnText = element.toString();
    if (element instanceof ReviewId) {
      ReviewId reviewId = (ReviewId) element;
      switch (columnIndex) {
        case 0:
          columnText = reviewId.getReviewId();
          break;
        case 1:
          columnText = ReviewI18n.getString(reviewId.getDescription());
          break;
        case 2:
          columnText = this.format.format(reviewId.getDate());
          break;
        default:
          break;
      }
    } 
    return columnText;
  }

}
