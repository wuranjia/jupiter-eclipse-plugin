package edu.hawaii.ics.csdl.jupiter.model.columndata;

import edu.hawaii.ics.csdl.jupiter.file.PrefResource;

/**
 * Provides the column data model manager.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ColumnDataModelManager {
  private static ColumnDataModelManager theInstance = new ColumnDataModelManager();
  private ColumnDataModel model;
  /**
   * Instantiates <code>ColumnDataModel</code> instance. Prohibits clients from instantiating this.
   */
  private ColumnDataModelManager() {
    this.model = new ColumnDataModel();
  }
  
  /**
   * Gets the singleton instance.
   * @return the singleton instance.
   */
  public static ColumnDataModelManager getInstance() {
    return theInstance;
  }
  
  /**
   * Gets the <code>ColumnDataModel</code> instance.
   * @param reviewPhaseNameKey the review phase name key.
   * @return the <code>ColumnDataModel</code> instance.
   */
  public ColumnDataModel getModel(String reviewPhaseNameKey) {
    this.model.clear();
    PrefResource.getInstance().loadColumnData(reviewPhaseNameKey, this.model);
//    PrefXmlSerializer.getInstance().read(reviewPhaseNameKey, this.model, false);
    return this.model;
  }
  
  /**
   * Gets the empty <code>ColumnDataModel</code> instance.
   * @return the empty <code>ColumnDataModel</code> instance.
   */
  public ColumnDataModel getModel() {
    return this.model;
  }
}
