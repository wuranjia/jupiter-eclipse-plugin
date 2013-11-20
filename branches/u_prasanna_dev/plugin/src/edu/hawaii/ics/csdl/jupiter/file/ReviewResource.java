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

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.file.property.CreationDate;
import edu.hawaii.ics.csdl.jupiter.file.property.Files;
import edu.hawaii.ics.csdl.jupiter.file.property.Filter;
import edu.hawaii.ics.csdl.jupiter.file.property.Filters;
import edu.hawaii.ics.csdl.jupiter.file.property.Phase;
import edu.hawaii.ics.csdl.jupiter.file.property.Review;
import edu.hawaii.ics.csdl.jupiter.file.property.Reviewers;
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
  private Review review;

  /**
   * Instantiates the resource with the <code>Review</code>.
   * 
   * @param review The review that the resource will use.
   */
  public ReviewResource(Review review) {
    this.review = review;
  }

  /**
   * Gets the <code>Review</code> associated with the resource.
   * 
   * @return Returns the review.
   */
  public Review getReview() {
    return this.review;
  }

  /**
   * Loads field item entry key into key manager.
   * 
   * @param fieldId the field id.
   * @param keyManager the key manager.
   */
  public void loadEntryKey(String fieldId, KeyManager keyManager) {
    keyManager.clear();
    // FieldItem entries
    List<edu.hawaii.ics.csdl.jupiter.file.property.FieldItem.Entry> entries = getEntryList(fieldId);

    if (entries != null) {
      for (edu.hawaii.ics.csdl.jupiter.file.property.FieldItem.Entry entry : entries) {
        // TODO The value is not actually used... this should be fixed
        keyManager.add(entry.getName(), null);
      }
    }
  }

  /**
   * Gets the list of the String field item IDs.
   * 
   * @return the list of the String field item IDs.
   */
  public List<String> getFieldItemIdList() {
    List<String> fieldItemIdList = new ArrayList<String>();
    // get the 'FieldItem's from the JAXB objects
    List<edu.hawaii.ics.csdl.jupiter.file.property.FieldItem> fieldItemList = this.review
        .getFieldItems().getFieldItem();
    for (edu.hawaii.ics.csdl.jupiter.file.property.FieldItem fieldItem : fieldItemList) {
      fieldItemIdList.add(fieldItem.getId());
    }

    return fieldItemIdList;
  }

  /**
   * gets the map of the String fieldItem id - <code>FieldItem</code> instance.
   * 
   * @return the list of the <code>FieldItem</code> instances.
   */
  public Map<String, FieldItem> getFieldItemMap() {
    Map<String, FieldItem> fieldItemMap = new TreeMap<String, FieldItem>();

    // get the 'FieldItem's from the JAXB objects
    List<edu.hawaii.ics.csdl.jupiter.file.property.FieldItem> fieldItemList = this.review
        .getFieldItems().getFieldItem();
    for (edu.hawaii.ics.csdl.jupiter.file.property.FieldItem jaxbFieldItem : fieldItemList) {
      String fieldItemId = jaxbFieldItem.getId();
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
    edu.hawaii.ics.csdl.jupiter.file.property.FieldItem jaxbFieldItem = null;
    List<edu.hawaii.ics.csdl.jupiter.file.property.FieldItem.Entry> entryList = null;

    // get the 'FieldItem's from the JAXB objects
    List<edu.hawaii.ics.csdl.jupiter.file.property.FieldItem> fieldItemList = this.review
        .getFieldItems().getFieldItem();
    // find the XML FieldItem that matches the fieldId
    for (edu.hawaii.ics.csdl.jupiter.file.property.FieldItem fieldItem : fieldItemList) {
      if (fieldItem.getId().equals(fieldItemId)) {
        jaxbFieldItem = fieldItem;
        entryList = fieldItem.getEntry();
        break;
      }
    }

    String defaultKey = null;
    if (jaxbFieldItem != null) {
      defaultKey = jaxbFieldItem.getDefault();
    }

    List<String> entryKeyList = new ArrayList<String>();
    if (entryList != null) {
      for (edu.hawaii.ics.csdl.jupiter.file.property.FieldItem.Entry entry : entryList) {
        String entryNameKey = entry.getName();
        String entryName = ReviewI18n.getString(entryNameKey);
        entryKeyList.add(entryName);
      }
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
    for (Entry<String, FieldItem> mapEntry : fieldItemIdFieldItemMap.entrySet()) {
      String fieldItemId = mapEntry.getKey();
      FieldItem fieldItem = mapEntry.getValue();
      String defaultKey = fieldItem.getDefaultKey();
      List<String> entryNameList = fieldItem.getEntryNameList();

      edu.hawaii.ics.csdl.jupiter.file.property.FieldItem jaxbFieldItem = getJAXBFieldItem(fieldItemId);
      if (jaxbFieldItem != null) {
        jaxbFieldItem.setDefault(defaultKey);

        // clear out all existing entries
        jaxbFieldItem.getEntry().clear();

        // add new entries
        for (String entryName : entryNameList) {
          String entryNameKey = ReviewI18n.getKey(entryName);

          edu.hawaii.ics.csdl.jupiter.file.property.FieldItem.Entry entry = new edu.hawaii.ics.csdl.jupiter.file.property.FieldItem.Entry();
          entry.setName(entryNameKey);

          jaxbFieldItem.getEntry().add(entry);
        }
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
    edu.hawaii.ics.csdl.jupiter.file.property.FieldItem fieldItem = getJAXBFieldItem(fieldId);
    if (fieldItem != null) {
      return fieldItem.getDefault();
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
    return this.review.getId();
  }

  /**
   * Gets the description in the Description element.
   * 
   * @return the description.
   */
  public String getDescription() {
    return this.review.getDescription();
  }

  /**
   * Gets the author in the Author element.
   * 
   * @return the author.
   */
  public String getAuthor() {
    return this.review.getAuthor();
  }

  /**
   * Gets the directory in the Directory element.
   * 
   * @return the directory.
   */
  public String getDirectory() {
    return this.review.getDirectory();
  }

  /**
   * Gets the map of the reviewer id string - ReviewerId instance.
   * 
   * @return the map of the reviewer id string - ReviewrId instance.
   */
  public Map<String, ReviewerId> getReviewers() {
    Map<String, ReviewerId> reviewerIdMap = new TreeMap<String, ReviewerId>();
    Reviewers reviewers = this.review.getReviewers();
    List<edu.hawaii.ics.csdl.jupiter.file.property.Reviewers.Entry> reviewersList = reviewers
        .getEntry();
    for (edu.hawaii.ics.csdl.jupiter.file.property.Reviewers.Entry entry : reviewersList) {
      String reviewerId = entry.getId();
      String reviewerName = entry.getName();
      reviewerIdMap.put(reviewerId, new ReviewerId(reviewerId, reviewerName));
    }

    return reviewerIdMap;
  }

  /**
   * Gets the set of the String target files.
   * 
   * @return the set of the String target files.
   */
  public Set<String> getFileSet() {
    Files files = this.review.getFiles();
    List<edu.hawaii.ics.csdl.jupiter.file.property.Files.Entry> fileList = files.getEntry();
    Set<String> targetFileSet = new LinkedHashSet<String>();
    for (edu.hawaii.ics.csdl.jupiter.file.property.Files.Entry entry : fileList) {
      targetFileSet.add(entry.getName());
    }
    return targetFileSet;
  }

  /**
   * Gets the map of the String phase name - FilterPhase instance.
   * 
   * @return the map of the String phase name - FilterPhase instance.
   */
  public Map<String, FilterPhase> getPhaseNameToFilterPhaseMap() {
    Filters filters = this.review.getFilters();
    List<Phase> phases = filters.getPhase();
    Map<String, FilterPhase> phaseNameFilterPhaseMap = new TreeMap<String, FilterPhase>();
    for (Phase phase : phases) {
      String phaseNameKey = phase.getName();
      FilterPhase filterPhase = getFilterPhase(phaseNameKey);
      phaseNameFilterPhaseMap.put(ReviewI18n.getString(phaseNameKey), filterPhase);
    }
    return phaseNameFilterPhaseMap;
  }

  /**
   * Sets the map of the String phase name - <code>FilterPhase</code>.
   * 
   * @param phaseNameFilterPhaseMap the map of the String phase name - <code>FilterPhase</code>.
   */
  public void setPhaseNameFilterPhaseMap(Map<String, FilterPhase> phaseNameFilterPhaseMap) {
    for (Entry<String, FilterPhase> mapEntry : phaseNameFilterPhaseMap.entrySet()) {
      String phaseName = mapEntry.getKey();
      String phaseNameKey = ReviewI18n.getKey(phaseName);
      FilterPhase filterPhase = mapEntry.getValue();
      
      Phase jaxbPhase = getJAXBPhase(phaseNameKey);
      jaxbPhase.setEnabled(filterPhase.isEnabled());
      
      List<Filter> filterList = jaxbPhase.getFilter();
      // clear out any existing filters
      filterList.clear();
      
      for (Iterator<FilterEntry> j = filterPhase.iterator(); j.hasNext();) {
        FilterEntry filterEntry = j.next();
        Filter filter = new Filter();
        filter.setName(filterEntry.getFilterName());
        filter.setValue(filterEntry.getValueKey());
        filter.setEnabled(filterEntry.isEnabled());
        
        filterList.add(filter);
      }
    }
  }

  /**
   * Gets the XML ordered phase name list.
   * 
   * @return the XML ordered phase name list.
   */
  public List<String> getPhaseNameList() {
    List<Phase> phaseList = this.review.getFilters().getPhase();
    List<String> phaseNameList = new ArrayList<String>();
    for (Phase phase : phaseList) {
      String phaseNameKey = phase.getName();
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
    Phase jaxbPhase = getJAXBPhase(phaseNameKey);

    List<FilterEntry> filterEntryList = new ArrayList<FilterEntry>();
    if (jaxbPhase != null) {
      List<Filter> filterList = jaxbPhase.getFilter();
      for (Filter filter : filterList) {
        String filterName = filter.getName();
        String valueKey = filter.getValue();
        Boolean enabled = filter.isEnabled();

        FilterEntry entry = new FilterEntry(filterName, valueKey, enabled);
        filterEntryList.add(entry);
      }
    }

    return new FilterPhase(phaseNameKey, jaxbPhase.isEnabled(), filterEntryList);
  }

  /**
   * Gets the JAXB <code>Phase</code> instance with the given phase name key.
   * 
   * @param phaseNameKey The key of the phase to use when searching for the JAXB <code>Phase</code>.
   * @return Returns the JAXB phase associated with the given phase name key.
   */
  private Phase getJAXBPhase(String phaseNameKey) {
    List<Phase> phaseList = this.review.getFilters().getPhase();
    Phase jaxbPhase = null;
    // find phase matching the phase name key
    for (Phase phase : phaseList) {
      if (phase.getName().equals(phaseNameKey)) {
        jaxbPhase = phase;
        break;
      }
    }
    return jaxbPhase;
  }

  /**
   * Gets the creation Date for the review.
   * 
   * @return the creation Date instance.
   */
  public Date getCreationDate() {
    CreationDate creationDate = this.review.getCreationDate();
    String format = creationDate.getFormat();
    String date = creationDate.getValue();
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
   * Sets the review author.
   * 
   * @param author the author.
   */
  private void setAuthor(String author) {
    this.review.setAuthor(author);
  }

  /**
   * Sets the review creation date.
   * 
   * @param date the date.
   */
  private void setCreationDate(Date date) {
    CreationDate creationDate = this.review.getCreationDate();
    String format = creationDate.getFormat();
    String value = new SimpleDateFormat(format).format(date);
    creationDate.setValue(value);
  }

  /**
   * Sets the review description.
   * 
   * @param description the description.
   */
  private void setDescription(String description) {
    this.review.setDescription(description);
  }

  /**
   * Sets the review directory.
   * 
   * @param directory the directory in which review files are stored.
   */
  private void setDirectory(String directory) {
    this.review.setDirectory(directory);
  }

  /**
   * Sets the review id.
   * 
   * @param reviewId the review id.
   */
  private void setReviewId(String reviewId) {
    this.review.setId(reviewId);
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
    edu.hawaii.ics.csdl.jupiter.file.property.FieldItem fieldItem = getJAXBFieldItem(fieldId);
    if (fieldItem != null) {
      fieldItem.setDefault(defautKey);
    }
  }

  /**
   * Sets the items for the field id.
   * 
   * @param fieldId the field id.
   * @param itemList the list of item name.
   */
  public void setFieldItems(String fieldId, List<String> itemList) {
    edu.hawaii.ics.csdl.jupiter.file.property.FieldItem fieldItem = getJAXBFieldItem(fieldId);
    if (fieldItem != null) {
      // clear out existing entries
      fieldItem.getEntry().clear();
      
      for (String itemName : itemList) {
        String itemNameKey = ReviewI18n.getKey(itemName);
        
        edu.hawaii.ics.csdl.jupiter.file.property.FieldItem.Entry entry = 
          new edu.hawaii.ics.csdl.jupiter.file.property.FieldItem.Entry();
        
        entry.setName(itemNameKey);
        fieldItem.getEntry().add(entry);
      }
    }
  }

  /**
   * Sets the reviewers in the 'Reviewers' element.
   * 
   * @param reviewers the map of the reviewerId string - ReviewerId instance.
   */
  private void setReviewers(Map<String, ReviewerId> reviewers) {
    // clear out existing reviewers
    this.review.getReviewers().getEntry().clear();
    
    // add all reviewers in the new map
    for (ReviewerId reviewerId : reviewers.values()) {
      edu.hawaii.ics.csdl.jupiter.file.property.Reviewers.Entry entry = 
        new edu.hawaii.ics.csdl.jupiter.file.property.Reviewers.Entry();
      
      entry.setId(reviewerId.getReviewerId());
      entry.setName(reviewerId.getReviewerName());
      this.review.getReviewers().getEntry().add(entry);
    }
  }

  /**
   * Sets the target files in the 'Files' element.
   * 
   * @param targetFileSet the list of the String target files.
   */
  public void setTargetFiles(Set<String> targetFileSet) {
    // clear all file entries
    this.review.getFiles().getEntry().clear();
    
    for (String file : targetFileSet) {
      edu.hawaii.ics.csdl.jupiter.file.property.Files.Entry entry = 
        new edu.hawaii.ics.csdl.jupiter.file.property.Files.Entry();
      
      entry.setName(file);
      this.review.getFiles().getEntry().add(entry);
    }
  }

  /**
   * Gets the list of <code>Entry</code> objects belonging to the JAXB <code>FieldItem</code>
   * that matches the fieldId given.
   * 
   * @param fieldId The field id that will be compared to the field item ids to find entry
   *          objects.
   * @return Returns a list if entries or null if the fieldId did not match any FieldItems.
   */
  private List<edu.hawaii.ics.csdl.jupiter.file.property.FieldItem.Entry> getEntryList(
      String fieldId) {
    // get the 'FieldItem's from the JAXB objects
    List<edu.hawaii.ics.csdl.jupiter.file.property.FieldItem> fieldItemList = this.review
        .getFieldItems().getFieldItem();
    // find the XML FieldItem that matches the fieldId
    for (edu.hawaii.ics.csdl.jupiter.file.property.FieldItem fieldItem : fieldItemList) {
      if (fieldItem.getId().equals(fieldId)) {
        return fieldItem.getEntry();
      }
    }
    return null;
  }

  /**
   * Gets the JAXB <code>FieldItem</code> with the id matching the given field id.
   * 
   * @param fieldId The field id of the FieldItem.
   * @return Returns the field item or null if none is found.
   */
  private edu.hawaii.ics.csdl.jupiter.file.property.FieldItem getJAXBFieldItem(String fieldId) {
    // get the 'FieldItem's from the JAXB objects
    List<edu.hawaii.ics.csdl.jupiter.file.property.FieldItem> fieldItemList = this.review
        .getFieldItems().getFieldItem();
    // find the XML FieldItem that matches the fieldId
    for (edu.hawaii.ics.csdl.jupiter.file.property.FieldItem fieldItem : fieldItemList) {
      if (fieldItem.getId().equals(fieldId)) {
        return fieldItem;
      }
    }
    return null;
  }
}
