package edu.hawaii.ics.csdl.jupiter.file;

import java.util.List;

/**
 * Provides field item data structure.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class FieldItem {
  private String fieldItemId;
  private String defaultKey;
  private List<String> entryNameList;
  
  /**
   * Instantiates field item data structure.
   * @param fieldItemId the field item id.
   * @param defaultKey the default key.
   * @param entryNameList the entry name list.
   */
  public FieldItem(String fieldItemId, String defaultKey, List<String> entryNameList) {
    this.fieldItemId = fieldItemId;
    this.defaultKey = defaultKey;
    this.entryNameList = entryNameList;
  }
  
  /**
   * Gets the default key of the field item.
   * @return the default key of the field item.
   */
  public String getDefaultKey() {
    return defaultKey;
  }
  
  /**
   * Sets the default key of the field item.
   * @param defaultKey the default key.
   */
  public void setDefaultKey(String defaultKey) {
    this.defaultKey = defaultKey;
  }
  
  /**
   * Gets the list of the <code>String</code> entry name.
   * @return the list of the <code>String</code> entry name.
   */
  public List<String> getEntryNameList() {
    return entryNameList;
  }
  
  /**
   * gets the field item id.
   * @return the field item id.
   */
  public String getFieldItemId() {
    return fieldItemId;
  }
  
  /**
   * sets the field item id.
   * @param fieldItemId the field item id.
   */
  public void setFieldItemId(String fieldItemId) {
    this.fieldItemId = fieldItemId;
  }
}
