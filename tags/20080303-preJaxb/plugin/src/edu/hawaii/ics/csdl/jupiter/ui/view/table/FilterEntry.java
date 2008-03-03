package edu.hawaii.ics.csdl.jupiter.ui.view.table;

/**
 * Provides the filter entry data structure.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class FilterEntry {
  private String filterName;
  private String valueKey;
  private boolean isEnabled;
  
  /**
   * Instantiates the filter entry.
   * @param filterName the filter name.
   * @param valueKey the filter value key.
   * @param isEnabled <code>true</code> if the filter entry is enabled.
   */
  public FilterEntry(String filterName, String valueKey, boolean isEnabled) {
    this.filterName = filterName;
    this.valueKey = valueKey;
    this.isEnabled = isEnabled;
  }
 
  /**
   * Gets the filter name.
   * @return the filter name.
   */
  public String getFilterName() {
    return filterName;
  }
  
  /**
   * Checks if the filter entry is enabled. Returns <code>true</code> if the filter entry is
   * enabled.
   * @return <code>true</code> if the filter entry is enabled.
   */
  public boolean isEnabled() {
    return isEnabled;
  }
  
  /**
   * Sets the enable status of the filter.
   * @param isEnabled <code>true</code> if the filter is enabled.
   */
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }
  
  /**
   * Sets the value key of the filter entry.
   * @return the value key of the filter entry.
   */
  public String getValueKey() {
    return this.valueKey;
  }
  
  /**
   * Sets the default value key of the filter.
   * @param valueKey the default value key of the filter.
   */
  public void setKey(String valueKey) {
    this.valueKey = valueKey;
  }
}
