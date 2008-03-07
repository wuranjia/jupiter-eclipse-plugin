package edu.hawaii.ics.csdl.jupiter.file;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnPixelData;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.file.preference.ColumnEntry;
import edu.hawaii.ics.csdl.jupiter.file.preference.Phase;
import edu.hawaii.ics.csdl.jupiter.file.preference.Preference;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnData;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnDataModel;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides data-filled manager objects. Clients can pass an object to get proper data.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class PrefResource {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();
  /** The singleton instance. */
  private static PrefResource theInstance;

  /** The loaded preference. */
  private static Preference preference;
  
  /**
   * Prohibits the clients' instantiation.
   */
  private PrefResource() {
    preference = PrefXmlSerializer.loadPreference();
  }
  
  /**
   * Gets the instance of the <code>PrefResource</code>.
   * @return the instance of the <code>PrefResource</code>.
   */
  public static PrefResource getInstance() {
    if (theInstance == null) {
      theInstance = new PrefResource();
    }
    return theInstance;
  }

  /**
   * Stores the <code>ColumnDataManager</code> instance into configuration XML file.
   * @param reviewPhaseNameKey the review phase name key.
   * @param columnDataModel the <code>ColumnDataManager</code> instance.
   */
  public void storeColumnDataModel(String reviewPhaseNameKey, ColumnDataModel columnDataModel) {
    ColumnData[] columnDataArray = columnDataModel.getAllColumnDataArray();
    
    Phase phase = getPhase(reviewPhaseNameKey);
    if (phase != null) {
      // clear out all column entries
      phase.getColumnEntry().clear();
      
      for (int i = 0; i < columnDataArray.length; i++) {
        int width = columnDataArray[i].getColumnPixelData().width;
        boolean resizable = columnDataArray[i].getColumnPixelData().resizable;
        String columnNameKey = columnDataArray[i].getColumnNameKey();
        boolean isEnabled = columnDataArray[i].isEnabled();

        // create column data entry from scratch
        ColumnEntry columnEntry = new ColumnEntry();
        columnEntry.setWidth(width);
        columnEntry.setResizable(resizable);
        columnEntry.setName(columnNameKey);
        columnEntry.setEnable(isEnabled);

        phase.getColumnEntry().add(columnEntry);
      }
      try {
        PrefXmlSerializer.serializePreference(preference);
      }
      catch (ReviewException e) {
        log.error(e);
      }
    }
  }
  
  /**
   * Gets the default review phase name.
   * @return the default review phase name.
   */
  public String getDefaultPhaseNameKey() {
    return preference.getView().getDefault();
  }
  
  /**
   * Gets the array of <code>String</code> review phase names or phase name keys depending upon
   * the argument option. Returns the phase name keys if isKey is true. 
   * Otherwise returns the phase names
   * @param isKey true if returning values are array of phase name keys.
   * @return the array of <code>String</code> review phase name keys or phase names.
   */
  public String[] getPhaseArray(boolean isKey) {
    List<Phase> phases = preference.getView().getPhase();
    List<String> phaseList = new ArrayList<String>();
    for (Phase phase : phases) {
      String phaseNameKey = phase.getName();
      String phaseString = (isKey) ? phaseNameKey : ReviewI18n.getString(phaseNameKey);
      phaseList.add(phaseString);
    }
    return phaseList.toArray(new String[] {});
  }
  
  /**
   * Gets the update URL string.
   * @return the update URL string.
   */
  public String getUpdateUrl() {
    return preference.getGeneral().getUpdateUrl();
  }
  
  /**
   * Gets the boolean enable update value.
   * @return true if update is enabled.
   */
  public boolean getEnableUpdate() {
    return preference.getGeneral().isEnableUpdate();
  }
  
  /**
   * Gets the boolean enable filter value.
   * @return true if filter is enabled.
   */
  public boolean getEnableFilter() {
    return preference.getGeneral().isEnableFilter();
  }
  
  /**
   * Loads ColumnData instances to the column data model.
   * @param phaseNameKey the phase name key.
   * @param columnDataModel the column data model.
   */
  public void loadColumnData(String phaseNameKey, ColumnDataModel columnDataModel) {
    columnDataModel.clear();
    columnDataModel.addAll(getColumnDataList(phaseNameKey));
  }
  
  /**
   * Gets the list of the ColumnData instances given the phase name key.
   * @param phaseNameKey the phase name key.
   * @return the list of the ColumnData instances given the phase name key.
   */
  public List<ColumnData> getColumnDataList(String phaseNameKey) {
    List<ColumnData> columnDataList = new ArrayList<ColumnData>();
    Phase phase = getPhase(phaseNameKey);
    for (ColumnEntry entry : phase.getColumnEntry()) {
      String columnNameKey = entry.getName();
      boolean enable = entry.isEnable();
      int width = entry.getWidth();
      boolean resizeable = entry.isResizable();
      ColumnPixelData pixelData = new ColumnPixelData(width, resizeable);
      ColumnData columnData = new ColumnData(columnNameKey, pixelData, enable);
      columnDataList.add(columnData);
    }
    return columnDataList;
  }
  
  /**
   * Gets the <code>Phase</code> associated with the review phase name key.
   * 
   * @param reviewPhaseNameKey The name key of the review phase to get.
   * @return Returns the <code>Phase</code> with the given key or null if cannot be found.
   */
  private Phase getPhase(String reviewPhaseNameKey) {
    List<Phase> phases = preference.getView().getPhase();
    for (Phase phase : phases) {
      if (phase.getName().equals(reviewPhaseNameKey)) {
        return phase;
      }
    }
    return null;
  }
}
