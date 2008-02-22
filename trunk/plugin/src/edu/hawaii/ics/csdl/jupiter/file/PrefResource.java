package edu.hawaii.ics.csdl.jupiter.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ColumnPixelData;
import org.jdom.Element;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
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
  /** The PrefElementFactory instance. */
  private PrefElementFactory factory;
  
  /**
   * Prohibits the clients' instantiation.
   */
  private PrefResource() {
    this.factory = PrefElementFactory.getInstance(PrefXmlSerializer.loadPreferenceElement());
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
    Element phaseElement = this.factory.getPhaseElement(reviewPhaseNameKey);
    phaseElement.removeContent();
    List<ColumnData> columnDataList = new ArrayList<ColumnData>();
    ColumnData[] columnDataArray = columnDataModel.getAllColumnDataArray();
    for (int i = 0; i < columnDataArray.length; i++) {
      Element columnHeaderElement = new Element(PrefConstraints.ELEMENT_COLUMN_ENTRY);
      int width = columnDataArray[i].getColumnPixelData().width;
      boolean resizable = columnDataArray[i].getColumnPixelData().resizable;
      String columnNameKey = columnDataArray[i].getColumnNameKey();
      columnHeaderElement.setAttribute(PrefConstraints.ATTRIBUTE_NAME, columnNameKey);
      columnHeaderElement.setAttribute(PrefConstraints.ATTRIBUTE_WIDTH, width + "");
      columnHeaderElement.setAttribute(PrefConstraints.ATTRIBUTE_RESIZALE, resizable + "");
      boolean isEnabled = columnDataArray[i].isEnabled();
      columnHeaderElement.setAttribute(PrefConstraints.ATTRIBUTE_ENABLE, isEnabled + "");
      phaseElement.addContent(columnHeaderElement);
    }
    try {
      PrefXmlSerializer.serializeDocument(phaseElement.getDocument());
    }
    catch (ReviewException e) {
      log.error(e);
    }
  }

  /**
   * Gets the default review phase name.
   * @return the default review phase name.
   */
  public String getDefaultPhaseNameKey() {
    return this.factory.getViewElement().getAttributeValue(PrefConstraints.ATTRIBUTE_DEFAULT);
  }
  
  /**
   * Gets the array of <code>String</code> review phase names or phase name keys depending upon
   * the argument option. Returns the phase name keys if isKey is true. 
   * Otherwise returns the phase names
   * @param isKey true if returning values are array of phase name keys.
   * @return the array of <code>String</code> review phase name keys or phase names.
   */
  public String[] getPhaseArray(boolean isKey) {
    List phaseElementList = this.factory.getPhaseElementList();
    List<String> phaseList = new ArrayList<String>();
    for (Iterator i = phaseElementList.iterator(); i.hasNext();) {
      Element phaseElement = (Element) i.next();
      String phaseNameKey = phaseElement.getAttributeValue(PrefConstraints.ATTRIBUTE_NAME);
      String phase = (isKey) ? phaseNameKey : ReviewI18n.getString(phaseNameKey);
      phaseList.add(phase);
    }
    return (String[]) phaseList.toArray(new String[] {});
  }
  
  /**
   * Gets the update URL string.
   * @return the update URL string.
   */
  public String getUpdateUrl() {
    return this.factory.getUpdateUrlElement().getText();
  }
  
  /**
   * Gets the boolean enable update value.
   * @return true if update is enabled.
   */
  public boolean getEnableUpdate() {
    return new Boolean(this.factory.getEnableUpdateElement().getText()).booleanValue();
  }
  
  /**
   * Gets the boolean enable filter value.
   * @return true if filter is enabled.
   */
  public boolean getEnableFilter() {
    return new Boolean(this.factory.getEnableFilterElement().getText()).booleanValue();
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
    List columnHeaderElementList = this.factory.getColumnEntryElementList(phaseNameKey);
    List<ColumnData> columnDataList = new ArrayList<ColumnData>();
    for (Iterator i = columnHeaderElementList.iterator(); i.hasNext();) {
      Element columnHeaderElement = (Element) i.next();
      ColumnData columnData = createColumnData(columnHeaderElement);
      columnDataList.add(columnData);
    }
    return columnDataList;
  }
  
  /**
   * Creates the ColumnData instance.
   * @param columnHeaderElement the ColumnHeader element.
   * @return the ColumnData instance.
   */
  private ColumnData createColumnData(Element columnHeaderElement) {
    String columnNameKey = columnHeaderElement.getAttributeValue(PrefConstraints.ATTRIBUTE_NAME);
    String enableString = columnHeaderElement.getAttributeValue(PrefConstraints.ATTRIBUTE_ENABLE);
    boolean enable = new Boolean(enableString).booleanValue();
    String widthString = columnHeaderElement.getAttributeValue(PrefConstraints.ATTRIBUTE_WIDTH);
    int width = Integer.parseInt(widthString);
    String resizableAttributeName = PrefConstraints.ATTRIBUTE_RESIZALE;
    String resizableString  = columnHeaderElement.getAttributeValue(resizableAttributeName);
    boolean resizable = new Boolean(resizableString).booleanValue();
    ColumnPixelData pixelData = new ColumnPixelData(width, resizable);
    return new ColumnData(columnNameKey, pixelData, enable);
  }
}
