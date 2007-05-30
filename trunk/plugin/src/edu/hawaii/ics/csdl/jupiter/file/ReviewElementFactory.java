package edu.hawaii.ics.csdl.jupiter.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 * Provides the review element wrapper class.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewElementFactory {
  private static ReviewElementFactory theInstance = new ReviewElementFactory();
  private Element reviewElement;
  /**
   * Instantiates the singleton review element. Prohibits clients from instantiating this.
   */
  private ReviewElementFactory() {
  }
  
  /**
   * Gets the singleton instance.
   * @param reviewElement the review element.
   * @return the singleton instance.
   */
  public static ReviewElementFactory getInstance(Element reviewElement) {
    theInstance.setReviewElement(reviewElement);
    return theInstance;
  }
  
  /**
   * Sets the review element.
   * @param revieweElement the review element.
   */
  private void setReviewElement(Element revieweElement) {
    this.reviewElement = revieweElement;
  }
  
  /**
   * Gets the 'Description' <code>Element</code>.
   * @return the 'Description' <code>Element</code>.
   */
  public Element getDescriptionElement() {
    String descriptionElementName = PropertyConstraints.ELEMENT_DESCRIPTION;
    return this.reviewElement.getChild(descriptionElementName);
  }
  
  /**
   * Gets the 'Author' <code>Element</code>.
   * @return the 'Author' <code>Element</code>.
   */
  public Element getAuthorElement() {
    return this.reviewElement.getChild(PropertyConstraints.ELEMENT_AUTHOR);
  }
  
  /**
   * Gets the 'CreationDate' <code>Element</code>.
   * @return the 'CreationDate' <code>Element</code>.
   */
  public Element getCreationDateElement() {
    return this.reviewElement.getChild(PropertyConstraints.ELEMENT_CREATION_DATE);
  }
  
  /**
   * Gets the 'Directory' <code>Element</code>.
   * @return the 'Directory' <code>Element</code>.
   */
  public Element getDirectoryElement() {
    return this.reviewElement.getChild(PropertyConstraints.ELEMENT_DIRECTORY);
  }
  
  /**
   * Gets the list of the 'FieldItem' <code>Element</code>.
   * @return the list of the 'FieldItem' <code>Element</code>.
   */
  public List getFieldItemElementList() {
    String filedItemsElementName = PropertyConstraints.ELEMENT_FIELD_ITEMS;
    Element fieldItemsElement = this.reviewElement.getChild(filedItemsElementName);
    return  fieldItemsElement.getChildren(PropertyConstraints.ELEMENT_FIELD_ITEM);
  }
  
  /**
   * Gets the list of the 'Entry' <code>Element</code> instances of the 'Reviewers'.
   * @return the list of the 'ReviewerId' <code>Element</code> instances of the 'Reviewers'.
   */
  public List getReviewersEntryElementList() {
    Element reviewersElement = this.reviewElement.getChild(PropertyConstraints.ELEMENT_REVIEWERS);
    return reviewersElement.getChildren(PropertyConstraints.ELEMENT_ENTRY);
  }
  
  
  /**
   * Gets the 'Entry' <code>Element</code> list of the 'FieldItem'if found. 
   * Returns empty list if not found.
   * @param fieldId the field id.
   * @return the 'Entry' <code>Element</code> list of the 'FieldItem' if found. 
   * Returns empty list if not found.
   */
  public List getFieldItemEntryElementList(String fieldId) {
    Element fieldElement = getFieldItemElement(fieldId);
    if (fieldElement != null) {
      return fieldElement.getChildren(PropertyConstraints.ELEMENT_ENTRY);
    }
    return new ArrayList();
  }
  
  /**
   * Gets the 'FieldItem' <code>Element</code> if any. Returns null if not found.
   * @param fieldId the field id.
   * @return the 'FieldItem' <code>Element</code> if any. Returns null if not found.
   */
  public Element getFieldItemElement(String fieldId) {
    List fieldItemElementList = getFieldItemElementList();
    for (Iterator i = fieldItemElementList.iterator(); i.hasNext();) {
      Element fieldItemElement = (Element) i.next();
      if (fieldItemElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_ID).equals(fieldId)) {
        return fieldItemElement;
      }
    }
    return null;
  }
  
  /**
   * Gets the 'Filter' <code>Element</code> list if found. Returns empty list if not found.
   * @param phaseNameKey the review phase id. e.g. phase.individual, etc.
   * @return the 'Filter' <code>Element</code> list if found. Returns empty list if not found.
   */
  public List getFilterElementList(String phaseNameKey) {
    Element phaseElement = getPhaseElement(phaseNameKey);
    if (phaseElement != null) {
      return phaseElement.getChildren(PropertyConstraints.ELEMENT_FILTER);
    }
    return new ArrayList();
  }
  
  /**
   * Gets the 'Phase' <code>Element</code> if any. Returns null if not found.
   * @param phaseNameKey the review phase id. e.g. phase.individual, etc.
   * @return the 'Phase' <code>Element</code> if any. returns null if not found.
   */
  public Element getPhaseElement(String phaseNameKey) {
    List phaseElementList = getPhaseElementList();
    for (Iterator i = phaseElementList.iterator(); i.hasNext();) {
      Element phaseElement = (Element) i.next();
      String nameAttribute = PropertyConstraints.ATTRIBUTE_NAME;
      String phaseNameKeyInElement = phaseElement.getAttributeValue(nameAttribute);
      if (phaseNameKeyInElement.equals(phaseNameKey)) {
        return phaseElement;
      }
    }
    return null;
  }
  
  /**
   * Gets the list of 'Phase' <code>Element</code> instances.
   * @return the list of 'Phase' <code>Element</code> instances.
   */
  public List getPhaseElementList() {
    Element filtersElement = this.reviewElement.getChild(PropertyConstraints.ELEMENT_FILTERS);
    return filtersElement.getChildren(PropertyConstraints.ELEMENT_PHASE);
  }
  
  /**
   * Gets the list of the 'Entry' <code>Element</code> of the 'Files'.
   * @return the list of the 'Entry' <code>Element</code> of the 'Files'.
   */
  public List getFilesEntryElementList() {
    Element filesElement = this.reviewElement.getChild(PropertyConstraints.ELEMENT_FILES);
    return filesElement.getChildren(PropertyConstraints.ELEMENT_ENTRY);
  }
}
