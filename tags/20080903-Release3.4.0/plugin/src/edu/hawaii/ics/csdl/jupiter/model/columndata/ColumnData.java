package edu.hawaii.ics.csdl.jupiter.model.columndata;

import org.eclipse.jface.viewers.ColumnPixelData;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;

/**
 * Provides the column data.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ColumnData {
  /** The key for the column name. */
  private String columnNameKey;
  /** The column header name. */
  private String columnName;
  /** The <code>ColumnPixelData</code> instance to hold the column width. */
  private ColumnPixelData columnPixelData;
  /** The boolean flag to see if this column is enabled. */
  private boolean isEnabled;

  /**
   * Instantiates the object with the column name and <code>ColumnPixelData</code> instance.
   * Note that the default column means that the column is provided in the config.xml file so that
   * the column name supports i18n.
   *
   * @param columnNameKey the key for the column header name.
   * @param columnPixelData the <code>ColumnPixelData</code> instance to hold the column width.
   * @param isEnabled sets <code>true</code> if this column can be enabled (visible). 
   * <code>false</code> otherwise.
   */
  public ColumnData(String columnNameKey, ColumnPixelData columnPixelData, boolean isEnabled) {
    this.columnNameKey = columnNameKey;
    this.columnName = ReviewI18n.getString(columnNameKey);
    this.columnPixelData = columnPixelData;
    this.isEnabled = isEnabled;
  }

  /**
   * Gets the column header name.
   *
   * @return the column header name.
   */
  public String getColumnName() {
    return this.columnName;
  }
  
  /**
   * Gets the column header name key.
   * @return the column header name key.
   */
  public String getColumnNameKey() {
    return this.columnNameKey;
  }
  
  /**
   * Sets the <code>ColumnPixelData</code> instance.
   * @param columnPixelData the <code>ColumnPixelData</code> instance.
   */
  public void setColumnPixelData(ColumnPixelData columnPixelData) {
    this.columnPixelData = columnPixelData;
  }
  
  /**
   * Sets <code>true</code> if this column can be enabled (visible). <code>false</code> otherwise.
   * @param isEnabled sets <code>true</code> if this column can be enabled (visible).
   *  <code>false</code> otherwise.
   */
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }
  
  /**
   * Checks if this column can be enabled (visible). Returns <code>true</code> if this column
   * can be enabled (visible). <code>false</code> otherwise.
   * @return <code>true</code> if this column can be enabled (visible). 
   * <code>false</code> otherwise.
   */
  public boolean isEnabled() {
    return this.isEnabled;
  }

  /**
   * Gets the <code>ColumnPixelData</code> instance to hold the column width.
   *
   * @return the <code>ColumnPixelData</code> instance to hold the column width.
   */
  public ColumnPixelData getColumnPixelData() {
    return this.columnPixelData;
  }
}
