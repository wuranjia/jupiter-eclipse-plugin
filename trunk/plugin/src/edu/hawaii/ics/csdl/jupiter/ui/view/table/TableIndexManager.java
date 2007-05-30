package edu.hawaii.ics.csdl.jupiter.ui.view.table;

import org.eclipse.swt.widgets.Table;

/**
 * Provides the table index manager.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class TableIndexManager {
  private static TableIndexManager theInstance = new TableIndexManager();
  private int index = 0;
  private int size = 0;
  /**
   * Prohibits clients from instantiating this.
   */
  private TableIndexManager() {
  }
  
  /**
   * Gets the sigleton instance.
   * @return the sigleton instance.
   */
  public static TableIndexManager getInstance() {
    theInstance.computeCurrentIndexAndSize();
    return theInstance;
  }
  
  /**
   * Computes the current index.
   */
  private void computeCurrentIndexAndSize() {
    Table table = ReviewTableView.getInstance().getTable();
    int selectionIndex = table.getSelectionIndex();
    int newSize = table.getItemCount();
    // if this size != new size (the filter case), then decrements index by one.
    this.index = (selectionIndex >= 0) ?  selectionIndex 
                                       : (newSize == this.size) ? this.index 
                                                                 : (this.index > 0) ? --this.index
                                                                                    : 0; 
    this.size = newSize;

  }
  
  /**
   * Gets the current index.
   * @return the current index.
   */
  public int getIndex() {
    return this.index;
  }
  
  /**
   * Increments the current index and returns the incremented index.
   * @return the incremented index.
   */
  public int increment() {
    if (this.index < size - 1) {
      this.index++;
    }
    return this.index;
  }
  
  /**
   * Decrements the current index and returns the decremented index.
   * @return the decremented index.
   */
  public int decrement() {
    if (this.index > 0) {
      this.index--;
    }
    return this.index;
  }
  
  /**
   * Gets the size of the table elements.
   * @return the size of the table elements.
   */
  public int getSize() {
    return this.size;
  }
}
