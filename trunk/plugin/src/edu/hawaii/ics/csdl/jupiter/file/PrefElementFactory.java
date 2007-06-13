package edu.hawaii.ics.csdl.jupiter.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 * Provides preference element wrapper class.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class PrefElementFactory {
  private static PrefElementFactory theInstance = new PrefElementFactory();
  private Element preferenceElement;
  
  /**
   * Prohibits clients from instantiating this.
   */
  private PrefElementFactory() {
  }
  
  /**
   * Gets the singleton instance.
   * @param preferenceElement the preference element.
   * @return the singleton instance.
   */
  public static PrefElementFactory getInstance(Element preferenceElement) {
    theInstance.setPreferenceElement(preferenceElement);
    return theInstance;
  }
  
  /**
   * Sets the 'Preference' <code>Element</code>.
   * @param preferenceElement the 'Preference' <code>Element</code>.
   */
  private void setPreferenceElement(Element preferenceElement) {
    this.preferenceElement = preferenceElement;
  }
  
  /**
   * Gets the 'Preference' <code>Element</code>.
   * @return the 'Preference' <code>Element</code>.
   */
  public Element getPreferenceElement() {
    return this.preferenceElement;
  }
  
  /**
   * Gets the 'UpdateUrl' <code>Element</code>.
   * @return the 'UpdateUrl' <code>Element</code>.
   */
  public Element getUpdateUrlElement() {
    Element generalElement = this.preferenceElement.getChild(PrefConstraints.ELEMENT_GENERAL);
    Element updateUrlElement = generalElement.getChild(PrefConstraints.ELEMENT_UPDATE_URL);
    return updateUrlElement;
  }
    
  /**
   * Gets the 'EnableUpdate' <code>Element</code>.
   * @return the 'EnableUpdate' <code>Element</code>.
   */
  public Element getEnableUpdateElement() {
    Element generalElement = this.preferenceElement.getChild(PrefConstraints.ELEMENT_GENERAL);
    Element enableUpdateElement = generalElement.getChild(PrefConstraints.ELEMENT_ENABLE_UPDATE);
    return enableUpdateElement;
  }
  
  /**
   * Gets the 'EnableFilter' <code>Element</code>.
   * @return the 'EnableFilter' <code>Element</code>.
   */
  public Element getEnableFilterElement() {
    Element generalElement = this.preferenceElement.getChild(PrefConstraints.ELEMENT_GENERAL);
    Element enableFilterElement = generalElement.getChild(PrefConstraints.ELEMENT_ENABLE_FILTER);
    return enableFilterElement;
  }
  
  /**
   * Gets the 'View' <code>Element</code>.
   * @return the 'View' <code>Element</code>.
   */
  public Element getViewElement() {
    return this.preferenceElement.getChild(PrefConstraints.ELEMENT_VIEW);
  }
  
  /**
   * Gets the 'Phase' <code>Element</code> if any. Returns <code>null</code> if not found.
   * @param phaseNameKey the phase name key.
   * @return the 'Phase' <code>Element</code> if any. Returns <code>null</code> if not found.
   */
  public Element getPhaseElement(String phaseNameKey) {
    Element viewElement = getViewElement();
    List phaseElementList = viewElement.getChildren(PrefConstraints.ELEMENT_PHASE);
    for (Iterator i = phaseElementList.iterator(); i.hasNext();) {
      Element phaseElement = (Element) i.next();
      if (phaseElement.getAttributeValue(PrefConstraints.ATTRIBUTE_NAME).equals(phaseNameKey)) {
        return phaseElement;
      }
    }
    return null;
  }
  
  /**
   * Gets the list of the 'Phase' <code>Element</code> instances.
   * @return the list of the 'Phase' <code>Element</code> instances.
   */
  public List getPhaseElementList() {
    Element viewElement = this.preferenceElement.getChild(PrefConstraints.ELEMENT_VIEW);
    return viewElement.getChildren(PrefConstraints.ELEMENT_PHASE);
  }
  
  /**
   * Gets the list of the 'ColumnEntry' <code>Element</code> instances given the phase name key.
   * @param phaseNameKey the phase name key.
   * @return the list of the 'ColumnEntry' <code>Element</code> instances given the phase name key.
   */
  public List getColumnEntryElementList(String phaseNameKey) {
    Element viewElement = this.preferenceElement.getChild(PrefConstraints.ELEMENT_VIEW);
    List phaseElementList = viewElement.getChildren(PrefConstraints.ELEMENT_PHASE);
    List columnHeaderElementList = new ArrayList();
    for (Iterator i = phaseElementList.iterator(); i.hasNext();) {
      Element phaseElement = (Element) i.next();
      if (phaseElement.getAttributeValue(PrefConstraints.ATTRIBUTE_NAME).equals(phaseNameKey)) {
        String columnHeaderElementName = PrefConstraints.ELEMENT_COLUMN_ENTRY;
        columnHeaderElementList = phaseElement.getChildren(columnHeaderElementName);
      }
    }
    return columnHeaderElementList;
  }
}
