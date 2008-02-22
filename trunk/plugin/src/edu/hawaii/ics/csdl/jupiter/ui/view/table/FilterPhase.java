package edu.hawaii.ics.csdl.jupiter.ui.view.table;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Provides the phase based filter entry data structure.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class FilterPhase {
  private String phaseName;
  private boolean isEnabled;
  private Map<String, FilterEntry> filterNameFilterEntryMap;
  private List<FilterEntry> filterEntryList;
  
  /**
   * Instantiates the phase based filter entry data structure.
   * @param phaseName the phase name.
   * @param isEnabled <code>true</code> if the phase based filter is enabled.
   * @param filterEntryList the list of the <code>FilterEntry</code>.
   */
  public FilterPhase(String phaseName, boolean isEnabled, List<FilterEntry> filterEntryList) {
    this.phaseName = phaseName;
    this.isEnabled = isEnabled;
    this.filterEntryList = filterEntryList;
    this.filterNameFilterEntryMap = new TreeMap<String, FilterEntry>();
    for (Iterator<FilterEntry> i = filterEntryList.iterator(); i.hasNext();) {
      FilterEntry entry = i.next();
      this.filterNameFilterEntryMap.put(entry.getFilterName(), entry);
    }
  }
  
  /**
   * Gets the FilterEntry instance. Returns null if not found.
   * @param filterName the filter name.
   * @return the FilterEntry instance. Returns null if not found.
   */
  public FilterEntry getFilterEntry(String filterName) {
    return (FilterEntry) this.filterNameFilterEntryMap.get(filterName);
  }
  
  /**
   * Checks if the phase based filter is enabled. Returns <code>true</code> if the filter is 
   * enabled.
   * @return <code>true</code> if the filter is enabled.
   */
  public boolean isEnabled() {
    return isEnabled;
  }
  
  /**
   * Sets the enable status of the filter in the phase.
   * @param isEnabled the enable status of the filter in the phase. <code>true</code> if
   * the filter in the phase is enabled.
   */
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }
  
  /**
   * Gets the phase name.
   * @return the phase name.
   */
  public String getPhaseName() {
    return this.phaseName;
  }
  
  /**
   * Iterates over <code>FilterEntry</code>.
   * @return the iterator of the <code>FilterEntry</code>.
   */
  public Iterator<FilterEntry> iterator() {
    return this.filterEntryList.iterator();
  }
}
