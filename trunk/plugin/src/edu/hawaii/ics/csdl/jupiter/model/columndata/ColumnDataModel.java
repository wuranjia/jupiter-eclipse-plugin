package edu.hawaii.ics.csdl.jupiter.model.columndata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnPixelData;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;

/**
 * Provides the container class for <code>ColumnData</code> instances.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ColumnDataModel {
  /** The list of the all <code>ColumnData</code> instances. */
  private List<String> allColumnNameKeyList;
  /** The map for the column name key and <code>ColumnData</code> value. */
  private Map<String, ColumnData> columnNameKeyMap;

  /**
   * Initializes the lists of <code>ColumnData</code> and default review phase name.
   */
  public ColumnDataModel() {
    this.allColumnNameKeyList = new ArrayList<String>();
    this.columnNameKeyMap = new HashMap<String, ColumnData>();
  }

  /**
   * Adds the <code>ColumnData</code> instance.
   *
   * @param columnData the <code>ColumnData</code> instance
   */
  public void add(ColumnData columnData) {
    this.columnNameKeyMap.put(columnData.getColumnNameKey(), columnData);
    this.allColumnNameKeyList.add(columnData.getColumnNameKey());
  }
  
  /**
   * Adds <code>ColumnData</code> list. Throws <code>ClassCastException</code> if the element
   * of the list is not <code>ColumnData</code> instance.
   * @param columnDataList the <code>ColumnData</code> list.
   */
  public void addAll(List<ColumnData> columnDataList) {
    for (Iterator<ColumnData> i = columnDataList.iterator(); i.hasNext();) {
      add(i.next());
    }
  }
  
  /**
   * Removes the element at the specified position in this list. 
   * Shifts any subsequent elements to the left (subtracts one from their indices).
   * @param index the index of the element to removed.
   * @return the <code>ColumnData</code> instance to be removed.
   */
  public ColumnData remove(int index) {
    String removedColumnNameKey = (String) this.allColumnNameKeyList.remove(index);
    return (ColumnData) this.columnNameKeyMap.remove(removedColumnNameKey);
  }
  
  /**
   * Inserts the specified element at the specified position in this list. 
   * Shifts the element currently at that position (if any) and any subsequent elements 
   * to the right (adds one to their indices).
   * @param index index at which the specified element is to be inserted.
   * @param columnData column data to be inserted.
   */
  public void insert(int index, ColumnData columnData) {
    this.allColumnNameKeyList.add(index, columnData.getColumnNameKey());
    this.columnNameKeyMap.put(columnData.getColumnNameKey(), columnData);
  }
  
  /**
   * Gets the <code>ColumnData</code> instance from the <code>columnNameKey</code>.
   * @param columnNameKey the column name key.
   * @return the <code>ColumnData</code> instance.
   */
  public ColumnData get(String columnNameKey) {
    return (ColumnData) this.columnNameKeyMap.get(columnNameKey);
  }

  /**
   * Gets the array of the <code>String</code> column header names.
   *
   * @return the array of the <code>String</code> column header names
   */
  public String[] getEnabledColumnNameArray() {
    List<String> columnNameList = new ArrayList<String>();
    for (Iterator<String> i = this.allColumnNameKeyList.iterator(); i.hasNext();) {
      String columnNameKey = i.next();
      ColumnData columnData = this.columnNameKeyMap.get(columnNameKey);
      if (columnData.isEnabled()) {
        String columnName = ReviewI18n.getString(columnNameKey);
        if (columnNameKey.equals(ResourceBundleKey.COLUMN_HEADER_LINK_ICON)) {
          columnName = "";
        }
        columnNameList.add(columnName);
      }
    }
    return columnNameList.toArray(new String[] {});
  }
  
  /**
   * Gets the array of the <code>String</code> column header name keys.
   *
   * @return the array of the <code>String</code> column header name keys
   */
  public String[] getEnabledColumnNameKeyArray() {
    List<String> columnNameKeyList = new ArrayList<String>();
    for (Iterator<String> i = this.allColumnNameKeyList.iterator(); i.hasNext();) {
      String columnNameKey = i.next();
      ColumnData columnData = this.columnNameKeyMap.get(columnNameKey);
      if (columnData.isEnabled()) {
        columnNameKeyList.add(columnNameKey);
      }
    }
    return columnNameKeyList.toArray(new String[] {});
  }

  /**
   * Gets the array of the <code>ColumnPixelData</code> instances
   *
   * @return the array of the <code>ColumnPixelData</code> instances
   */
  public ColumnPixelData[] getEnabledColumnPixelDataArray() {
    List<ColumnPixelData> columnPixelDataList = new ArrayList<ColumnPixelData>();
    for (Iterator<String> i = this.allColumnNameKeyList.iterator(); i.hasNext();) {
      String columnNameKey = i.next();
      ColumnData columnData = this.columnNameKeyMap.get(columnNameKey);
      if (columnData.isEnabled()) {
        columnPixelDataList.add(columnData.getColumnPixelData());
      }
    }
    return columnPixelDataList.toArray(new ColumnPixelData[] {});
  }
    
  /**
   * Gets the array of the enabled <code>ColumnData</code> instances.
   * @return the array of the enabled <code>ColumnData</code> instances.
   */
  public ColumnData[] getEnabledColumnDataArray() {
    List<ColumnData> enabledColumnDataList = new ArrayList<ColumnData>();
    for (Iterator<String> i = this.allColumnNameKeyList.iterator(); i.hasNext();) {
      String columnNameKey = i.next();
      ColumnData columnData = this.columnNameKeyMap.get(columnNameKey);
      if (columnData.isEnabled()) {
        enabledColumnDataList.add(columnData);
      }
    }
    return enabledColumnDataList.toArray(new ColumnData[] {});
  }
  
  /**
   * Gets the array of all <code>ColumnData</code> instances.
   * @return the array of all <code>ColumnData</code> instances.
   */
  public ColumnData[] getAllColumnDataArray() {
    List<ColumnData> columnDataList = new ArrayList<ColumnData>();
    for (Iterator<String> i = this.allColumnNameKeyList.iterator(); i.hasNext();) {
      String columnNameKey = i.next();
      columnDataList.add(this.columnNameKeyMap.get(columnNameKey));
    }
    return columnDataList.toArray(new ColumnData[] {});
  }
  
  /**
   * Clears all elements in this model. Note that review phase name is not cleared.
   */
  public void clear() {
    this.columnNameKeyMap.clear();
    this.allColumnNameKeyList.clear();
  }
  
  /**
   * Provides the size of the all elements.
   *
   * @return the size of the all elements.
   */
  public int allSize() {
    return this.allColumnNameKeyList.size();
  }
}
