package edu.hawaii.ics.csdl.jupiter.file;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.jdom.Element;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.KeyManager;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.FilterEntry;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.FilterPhase;

/**
 * Provides review resource.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewResource {
  private Element reviewElement;

  /**
   * Instantiates this with review element.
   * 
   * @param reviewElement the review element.
   */
  public ReviewResource(Element reviewElement) {
    this.reviewElement = reviewElement;
  }

  /**
   * Gets the 'Review' <code>Element</code>.
   * 
   * @return the 'Review' <code>Element</code>.
   */
  public Element getReviewElement() {
    return this.reviewElement;
  }

  /**
   * Loads field item entry key into key manager.
   * 
   * @param fieldId the field id.
   * @param keyManager the key manager.
   */
  public void loadEntryKey(String fieldId, KeyManager keyManager) {
    keyManager.clear();
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    List itemElementList = factory.getFieldItemEntryElementList(fieldId);
    for (Iterator i = itemElementList.iterator(); i.hasNext();) {
      Element itemElement = (Element) i.next();
      String itemName = itemElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_NAME);
      String value = itemElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_VALUE);
      keyManager.add(itemName, value);
    }
  }

  /**
   * Gets the list of the String field item IDs.
   * 
   * @return the list of the String field item IDs.
   */
  public List<String> getFieldItemIdList() {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    List fieldItemElementList = factory.getFieldItemElementList();
    List<String> fieldItemIdList = new ArrayList<String>();
    for (Iterator i = fieldItemElementList.iterator(); i.hasNext();) {
      Element fieldItemElement = (Element) i.next();
      String fieldItemId = fieldItemElement
          .getAttributeValue(PropertyConstraints.ATTRIBUTE_ID);
      fieldItemIdList.add(fieldItemId);
    }
    return fieldItemIdList;
  }

  /**
   * gets the map of the String fieldItem id - <code>FieldItem</code> instance.
   * 
   * @return the list of the <code>FieldItem</code> instances.
   */
  public Map<String, FieldItem> getFieldItemMap() {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    List fieldItemElementList = factory.getFieldItemElementList();
    Map<String, FieldItem> fieldItemMap = new TreeMap<String, FieldItem>();
    for (Iterator i = fieldItemElementList.iterator(); i.hasNext();) {
      Element fieldItemElement = (Element) i.next();
      String fieldItemId = fieldItemElement
          .getAttributeValue(PropertyConstraints.ATTRIBUTE_ID);
      String defaultKey = fieldItemElement
          .getAttributeValue(PropertyConstraints.ATTRIBUTE_DEFAULT);
      FieldItem fieldItem = getFieldItem(fieldItemId);
      fieldItemMap.put(fieldItemId, fieldItem);
    }
    return fieldItemMap;
  }

  /**
   * Gets the <code>FieldItem</code>.
   * 
   * @param fieldItemId the field item id.
   * @return the <code>FieldItem</code>.
   */
  public FieldItem getFieldItem(String fieldItemId) {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    List entryElementList = factory.getFieldItemEntryElementList(fieldItemId);
    Element fieldItemElement = factory.getFieldItemElement(fieldItemId);
    String defaultKey = fieldItemElement
        .getAttributeValue(PropertyConstraints.ATTRIBUTE_DEFAULT);
    List<String> entryKeyList = new ArrayList<String>();
    for (Iterator j = entryElementList.iterator(); j.hasNext();) {
      Element entryElement = (Element) j.next();
      String entryNameKey = entryElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_NAME);
      String entryName = ReviewI18n.getString(entryNameKey);
      entryKeyList.add(entryName);
    }
    return new FieldItem(fieldItemId, defaultKey, entryKeyList);
  }

  /**
   * Sets the map of the String fieldItem id - <code>FieldItem</code> instance.
   * 
   * @param fieldItemIdFieldItemMap the map of the String fieldItem id - <code>FieldItem</code>
   *          instance.
   */
  public void setFieldItemMap(Map<String, FieldItem> fieldItemIdFieldItemMap) {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    for (Iterator<Entry<String, FieldItem>> i = fieldItemIdFieldItemMap.entrySet().iterator(); i
        .hasNext();) {
      Map.Entry<String, FieldItem> entry = i.next();
      String fieldItemId = entry.getKey();
      FieldItem fieldItem = entry.getValue();
      String defaultKey = fieldItem.getDefaultKey();
      List<String> entryNameList = fieldItem.getEntryNameList();
      Element fieldItemElement = factory.getFieldItemElement(fieldItemId);
      fieldItemElement.removeContent();
      fieldItemElement.setAttribute(PropertyConstraints.ATTRIBUTE_DEFAULT, defaultKey);
      for (Iterator<String> j = entryNameList.iterator(); j.hasNext();) {
        String entryNameKey = ReviewI18n.getKey(j.next());
        Element entryElement = new Element(PropertyConstraints.ELEMENT_ENTRY);
        entryElement.setAttribute(PropertyConstraints.ATTRIBUTE_NAME, entryNameKey);
        fieldItemElement.addContent(entryElement);
      }
    }
  }

  /**
   * Gets the default field value if any. Returns empty string if not found.
   * 
   * @param fieldId the field id. e.g. Type, Severity, etc.
   * @return the default field value if any. Returns empty string if not found.
   */
  public String getDefaultField(String fieldId) {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    Element fieldItemElement = factory.getFieldItemElement(fieldId);
    if (fieldItemElement != null) {
      return fieldItemElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_DEFAULT);
    }
    return "";
  }

  /**
   * Gets the ReviewId instance.
   * 
   * @return the ReviewId instance.
   */
  public ReviewId getReviewId() {
    String reviewId = getReviewIdString();
    String description = getDescription();
    String author = getAuthor();
    String directory = getDirectory();
    Map<String, ReviewerId> reviewers = getReviewers();
    Date date = getCreationDate();
    return new ReviewId(reviewId, description, author, directory, reviewers,
        new TreeMap<String, List<String>>(), date);
  }

  /**
   * Gets the review id in the Review element.
   * 
   * @return the review id.
   */
  public String getReviewIdString() {
    return this.reviewElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_ID);
  }

  /**
   * Gets the description in the Description element.
   * 
   * @return the description.
   */
  public String getDescription() {
    String descriptionElementName = PropertyConstraints.ELEMENT_DESCRIPTION;
    Element descriptionElement = this.reviewElement.getChild(descriptionElementName);
    return descriptionElement.getText();
  }

  /**
   * Gets the author in the Author element.
   * 
   * @return the author.
   */
  public String getAuthor() {
    Element authorElement = this.reviewElement.getChild(PropertyConstraints.ELEMENT_AUTHOR);
    return authorElement.getText();
  }

  /**
   * Gets the directory in the Directory element.
   * 
   * @return the directory.
   */
  public String getDirectory() {
    Element directoryElement = this.reviewElement
        .getChild(PropertyConstraints.ELEMENT_DIRECTORY);
    return directoryElement.getText();
  }

  /**
   * Gets the map of the reviewer id string - ReviewerId instance.
   * 
   * @return the map of the reviewer id string - ReviewrId instance.
   */
  public Map<String, ReviewerId> getReviewers() {
    Element reviewersElement = this.reviewElement
        .getChild(PropertyConstraints.ELEMENT_REVIEWERS);
    List reviewerIdElementList = reviewersElement
        .getChildren(PropertyConstraints.ELEMENT_ENTRY);
    Map<String, ReviewerId> reviewers = new TreeMap<String, ReviewerId>();
    for (Iterator i = reviewerIdElementList.iterator(); i.hasNext();) {
      Element reviewerIdElement = (Element) i.next();
      String reviewerId = reviewerIdElement
          .getAttributeValue(PropertyConstraints.ATTRIBUTE_ID);
      String reviewerName = reviewerIdElement
          .getAttributeValue(PropertyConstraints.ATTRIBUTE_NAME);
      reviewers.put(reviewerId, new ReviewerId(reviewerId, reviewerName));
    }
    return reviewers;
  }

  /**
   * Gets the set of the String target files.
   * 
   * @return the set of the String target files.
   */
  public Set<String> getFileSet() {
    Element filesElement = this.reviewElement.getChild(PropertyConstraints.ELEMENT_FILES);
    List filesEntryElementList = filesElement.getChildren(PropertyConstraints.ELEMENT_ENTRY);
    Set<String> targetFileSet = new LinkedHashSet<String>();
    for (Iterator i = filesEntryElementList.iterator(); i.hasNext();) {
      Element filesEntryElement = (Element) i.next();
      String file = filesEntryElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_NAME);
      targetFileSet.add(file);
    }
    return targetFileSet;
  }

  /**
   * Gets the map of the String phase name - FilterPhase instance.
   * 
   * @return the map of the String phase name - FilterPhase instance.
   */
  public Map<String, FilterPhase> getPhaseNameToFilterPhaseMap() {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    List phaseElementList = factory.getPhaseElementList();
    Map<String, FilterPhase> phaseNameFilterPhaseMap = new TreeMap<String, FilterPhase>();
    for (Iterator i = phaseElementList.iterator(); i.hasNext();) {
      Element phaseElement = (Element) i.next();
      String phaseNameKey = phaseElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_NAME);
      FilterPhase phase = getFilterPhase(phaseNameKey);
      phaseNameFilterPhaseMap.put(ReviewI18n.getString(phaseNameKey), phase);
    }
    return phaseNameFilterPhaseMap;
  }

  /**
   * Sets the map of the String phase name - <code>FilterPhase</code>.
   * 
   * @param phaseNameFilterPhaseMap the map of the String phase name - <code>FilterPhase</code>.
   */
  public void setPhaseNameFilterPhaseMap(Map<String, FilterPhase> phaseNameFilterPhaseMap) {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    for (Iterator<Entry<String, FilterPhase>> i = phaseNameFilterPhaseMap.entrySet()
        .iterator(); i.hasNext();) {
      Map.Entry<String, FilterPhase> entry = i.next();
      String phaseName = entry.getKey();
      String phaseNameKey = ReviewI18n.getKey(phaseName);
      Element phaseElemnt = factory.getPhaseElement(phaseNameKey);
      phaseElemnt.removeContent();
      FilterPhase filterPhase = entry.getValue();
      phaseElemnt.setAttribute(PropertyConstraints.ATTRIBUTE_ENABLED, filterPhase.isEnabled()
          + "");
      for (Iterator<FilterEntry> j = filterPhase.iterator(); j.hasNext();) {
        FilterEntry filterEntry = (FilterEntry) j.next();
        String filterName = filterEntry.getFilterName();
        String valueKey = filterEntry.getValueKey();
        String enabled = filterEntry.isEnabled() + "";
        Element filterElement = new Element(PropertyConstraints.ELEMENT_FILTER);
        filterElement.setAttribute(PropertyConstraints.ATTRIBUTE_NAME, filterName);
        filterElement.setAttribute(PropertyConstraints.ATTRIBUTE_VALUE, valueKey);
        filterElement.setAttribute(PropertyConstraints.ATTRIBUTE_ENABLED, enabled);
        phaseElemnt.addContent(filterElement);
      }
    }
  }

  /**
   * Gets the XML ordered phase name list.
   * 
   * @return the XML ordered phase name list.
   */
  public List<String> getPhaseNameList() {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    List phaseElementList = factory.getPhaseElementList();
    List<String> phaseNameList = new ArrayList<String>();
    for (Iterator i = phaseElementList.iterator(); i.hasNext();) {
      Element phaseElement = (Element) i.next();
      String phaseNameKey = phaseElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_NAME);
      FilterPhase phase = getFilterPhase(phaseNameKey);
      phaseNameList.add(ReviewI18n.getString(phaseNameKey));
    }
    return phaseNameList;
  }

  /**
   * Gets the <code>FilterPhase</code> instance
   * 
   * @param phaseNameKey the phase name key.
   * @return the <code>FilterPhase</code> instance.
   */
  public FilterPhase getFilterPhase(String phaseNameKey) {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    Element phaseElement = factory.getPhaseElement(phaseNameKey);
    String isEnabled = phaseElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_ENABLED);
    boolean isFilterEnabled = new Boolean(isEnabled).booleanValue();
    List filterElementList = phaseElement.getChildren(PropertyConstraints.ELEMENT_FILTER);
    List<FilterEntry> filterEntryList = new ArrayList<FilterEntry>();
    for (Iterator j = filterElementList.iterator(); j.hasNext();) {
      Element filterElement = (Element) j.next();
      String filterName = filterElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_NAME);
      String valueKey = filterElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_VALUE);
      String enabled = filterElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_ENABLED);
      boolean isEntryFilterEnabled = new Boolean(enabled).booleanValue();
      FilterEntry entry = new FilterEntry(filterName, valueKey, isEntryFilterEnabled);
      filterEntryList.add(entry);
    }
    return new FilterPhase(phaseNameKey, isFilterEnabled, filterEntryList);
  }

  /**
   * Gets the creation Date instance in the CreationDate element.
   * 
   * @return the creation Date instance.
   */
  public Date getCreationDate() {
    String creationDateElementName = PropertyConstraints.ELEMENT_CREATION_DATE;
    Element creationDateElement = this.reviewElement.getChild(creationDateElementName);
    String format = creationDateElement
        .getAttributeValue(PropertyConstraints.ATTRIBUTE_FORMAT);
    String date = creationDateElement.getText();
    return createDate(date, format);
  }

  /**
   * Creates the <code>Date</code> instance associated with the <code>dateString</code>.
   * Note the this returns current time <code>Date</code> instance if <code>dateString</code>
   * could not be parsed with <code>dateFormat</code>.
   * 
   * @param dateString the date string to be parsed.
   * @param dateFormat the date format to let parser know the date string to be parsed.
   * 
   * @return the <code>Date</code> instance associated with the <code>dateString</code>.
   */
  private static Date createDate(String dateString, String dateFormat) {
    try {
      return new SimpleDateFormat(dateFormat).parse(dateString);
    }
    catch (ParseException e) {
      return new Date();
    }
  }

  /**
   * Sets the author in the Author element.
   * 
   * @param author the author.
   */
  private void setAuthor(String author) {
    Element authorElement = this.reviewElement.getChild(PropertyConstraints.ELEMENT_AUTHOR);
    authorElement.setText(author);
  }

  /**
   * Sets the date in the CreationDate element.
   * 
   * @param date the date.
   */
  private void setCreationDate(Date date) {
    String creationDateElementName = PropertyConstraints.ELEMENT_CREATION_DATE;
    Element creationDateElement = this.reviewElement.getChild(creationDateElementName);
    String format = creationDateElement
        .getAttributeValue(PropertyConstraints.ATTRIBUTE_FORMAT);
    String value = new SimpleDateFormat(format).format(date);
    creationDateElement.setText(value);
  }

  /**
   * Sets the description in the Description element.
   * 
   * @param description the description.
   */
  private void setDescription(String description) {
    String descriptionElementName = PropertyConstraints.ELEMENT_DESCRIPTION;
    Element descriptionElement = this.reviewElement.getChild(descriptionElementName);
    descriptionElement.setText(description);
  }

  /**
   * Sets the directory in the Directory element.
   * 
   * @param directory the directory in which review files are stored.
   */
  private void setDirectory(String directory) {
    Element directoryElement = this.reviewElement
        .getChild(PropertyConstraints.ELEMENT_DIRECTORY);
    directoryElement.setText(directory);
  }

  /**
   * Sets review id in the Review element.
   * 
   * @param reviewId the review id.
   */
  private void setReviewId(String reviewId) {
    this.reviewElement.setAttribute(PropertyConstraints.ATTRIBUTE_ID, reviewId);
  }

  /**
   * Sets the <code>ReviewId</code>.
   * 
   * @param reviewId the <code>ReviewId</code>.
   */
  public void setReviewId(ReviewId reviewId) {
    setReviewId(reviewId.getReviewId());
    setDescription(reviewId.getDescription());
    setAuthor(reviewId.getAuthor());
    setCreationDate(reviewId.getDate());
    setDirectory(reviewId.getDirectory());
    setReviewers(reviewId.getReviewers());
  }

  /**
   * Sets the default key value in the 'default' attribute.
   * 
   * @param fieldId the fieldId.
   * @param defautKey the default key.
   */
  public void setDefaultField(String fieldId, String defautKey) {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    Element fieldElement = factory.getFieldItemElement(fieldId);
    if (fieldElement != null) {
      fieldElement.setAttribute(PropertyConstraints.ATTRIBUTE_DEFAULT, defautKey);
    }
  }

  /**
   * Sets the items for the field id.
   * 
   * @param fieldId the field id.
   * @param itemList the list of item name.
   */
  public void setIFieldtems(String fieldId, List<String> itemList) {
    ReviewElementFactory factory = ReviewElementFactory.getInstance(this.reviewElement);
    Element fieldItemElement = factory.getFieldItemElement(fieldId);
    if (fieldItemElement != null) {
      fieldItemElement.removeContent();
      for (Iterator<String> i = itemList.iterator(); i.hasNext();) {
        String itemName = i.next();
        String itemNameKey = ReviewI18n.getKey(itemName);
        Element itemElement = new Element(PropertyConstraints.ELEMENT_ENTRY);
        itemElement.setAttribute(PropertyConstraints.ATTRIBUTE_NAME, itemNameKey);
        fieldItemElement.addContent(itemElement);
      }
    }
  }

  /**
   * Sets the reviewers in the 'Reviewers' element.
   * 
   * @param reviewers the map of the reviewerId string - ReviewerId instance.
   */
  private void setReviewers(Map<String, ReviewerId> reviewers) {
    Element reviewersElement = this.reviewElement
        .getChild(PropertyConstraints.ELEMENT_REVIEWERS);
    reviewersElement.removeContent();
    for (Iterator<ReviewerId> i = reviewers.values().iterator(); i.hasNext();) {
      ReviewerId reviewerId = i.next();
      Element reviewersEntryElement = new Element(PropertyConstraints.ELEMENT_ENTRY);
      String attributeId = PropertyConstraints.ATTRIBUTE_ID;
      reviewersEntryElement.setAttribute(attributeId, reviewerId.getReviewerId());
      String nameAttributeName = PropertyConstraints.ATTRIBUTE_NAME;
      reviewersEntryElement.setAttribute(nameAttributeName, reviewerId.getReviewerName());
      reviewersElement.addContent(reviewersEntryElement);
    }
  }

  /**
   * Sets the target files in the 'Files' element.
   * 
   * @param targetFileSet the list of the String target files.
   */
  public void setTargetFiles(Set<String> targetFileSet) {
    Element filesElement = this.reviewElement.getChild(PropertyConstraints.ELEMENT_FILES);
    filesElement.removeContent();
    for (Iterator<String> i = targetFileSet.iterator(); i.hasNext();) {
      String targetFile = i.next();
      Element filesEntryElement = new Element(PropertyConstraints.ELEMENT_ENTRY);
      filesEntryElement.setAttribute(PropertyConstraints.ATTRIBUTE_NAME, targetFile);
      filesElement.addContent(filesEntryElement);
    }
  }
}
