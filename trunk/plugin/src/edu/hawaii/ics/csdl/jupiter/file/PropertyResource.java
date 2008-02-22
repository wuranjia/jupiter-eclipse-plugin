package edu.hawaii.ics.csdl.jupiter.file;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.jdom.Document;
import org.jdom.Element;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides the review id wrapper singleton class.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class PropertyResource {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  private static PropertyResource theInstance = new PropertyResource();
  private Map<String, ReviewId> reviewIdMap;
  private IProject project;
  private boolean isDefaultLoaded;
  private Document propertyDocument;
  private Map<String, Element> reviewIdReviewElementMap;

  /** The default review id. */
  public static final String DEFAULT_ID = PropertyConstraints.DEFAULT_REVIEW_ID;

  /**
   * Prohibits clients from instantiating this.
   */
  private PropertyResource() {
    this.reviewIdMap = new TreeMap<String, ReviewId>();
    this.reviewIdReviewElementMap = new TreeMap<String, Element>();
  }

  /**
   * Gets the review id wrapper class for the project.
   * 
   * @param project the project.
   * @param isDefaultLoaded sets <code>true</code> if the default review id is loaded too.
   * @return the review id wrapper class which contains set of <code>ReviewID</code>
   *         instances.
   */
  public static PropertyResource getInstance(IProject project, boolean isDefaultLoaded) {
    theInstance.setValues(project, isDefaultLoaded);
    theInstance.fillReviewIdReviewElementMap(project, isDefaultLoaded);
    theInstance.fillReviewIdMap();
    return theInstance;
  }

  /**
   * Sets project and isDefaultLoaded values.
   * 
   * @param project the project.
   * @param isDefaultLoaded set true if default review id is also loaded.
   */
  private void setValues(IProject project, boolean isDefaultLoaded) {
    this.project = project;
    this.isDefaultLoaded = isDefaultLoaded;
  }

  /**
   * Loads the 'Review' <code>Element</code>.
   * 
   * @param project the project.
   * @param isDefaultLoaded true if default review id is loaded too.
   */
  private void fillReviewIdReviewElementMap(IProject project, boolean isDefaultLoaded) {
    try {
      this.propertyDocument = PropertyXmlSerializer.newDocument(project);
    }
    catch (ReviewException e) {
      log.error(e);
    }
    if (this.propertyDocument == null) {
      return;
    }
    Element propertyElement = this.propertyDocument.getRootElement();
    List reviewElementList = propertyElement.getChildren(PropertyConstraints.ELEMENT_REVIEW);
    this.reviewIdReviewElementMap.clear();
    for (Iterator i = reviewElementList.iterator(); i.hasNext();) {
      Element reviewElement = (Element) i.next();
      String reviewId = reviewElement.getAttributeValue(PropertyConstraints.ATTRIBUTE_ID);
      if (reviewId.equals(PropertyConstraints.DEFAULT_REVIEW_ID) && !isDefaultLoaded) {
        continue;
      }
      this.reviewIdReviewElementMap.put(reviewId, reviewElement);
    }
  }

  /**
   * Gets the <code>ReviewResource</code> instance associating with the review id. Returns
   * null if the review id does not exist.
   * 
   * @param reviewId the review id.
   * @param isClone true if the <code>ReviewResource</code> contains the clone of the review
   *          element. false if it contains the review element of the document.
   * @return the <code>ReviewResource</code> instance associating with the review id. Returns
   *         null if the review id does not exist.
   */
  public ReviewResource getReviewResource(String reviewId, boolean isClone) {
    Element reviewElement = (Element) this.reviewIdReviewElementMap.get(reviewId);
    if (reviewElement != null) {
      reviewElement = (isClone) ? (Element) reviewElement.clone() : reviewElement;
      return new ReviewResource(reviewElement);
    }
    return null;
  }

  /**
   * Loads <code>ReviewId</code> instances for the project into review id map.
   */
  private void fillReviewIdMap() {
    this.reviewIdMap.clear();
    for (Iterator<Element> i = this.reviewIdReviewElementMap.values().iterator(); i.hasNext();) {
      Element reviewIdElement = i.next();
      ReviewResource reviewResource = new ReviewResource(reviewIdElement);
      ReviewId reviewId = reviewResource.getReviewId();
      this.reviewIdMap.put(reviewId.getReviewId(), reviewId);
    }
  }

  /**
   * Gets the array of the <code>String</code> review id names. Note that the order of the
   * elements is reverse chronological order in which review id was created.
   * 
   * @return the array of the <code>String</code> review id names
   */
  public String[] getReviewIdNames() {
    List<ReviewId> reviewIdList = getReviewIdList();
    Map<Date, String> reviewIdNameMap = new TreeMap<Date, String>(new Comparator<Date>() {
      public int compare(Date object1, Date object2) {
        return object2.compareTo(object1);
      }
    });
    for (Iterator<ReviewId> i = reviewIdList.iterator(); i.hasNext();) {
      ReviewId reviewId = (ReviewId) i.next();
      reviewIdNameMap.put(reviewId.getDate(), reviewId.getReviewId());
    }
    return new ArrayList<String>(reviewIdNameMap.values()).toArray(new String[] {});
  }

  /**
   * Gets the array of the <code>String</code> reviewer id names.
   * 
   * @param reviewIdName the review id.
   * @return the array of the <code>String</code> review id names
   */
  public String[] getReviewerIdNames(String reviewIdName) {
    Map<String, ReviewerId> reviewers = getReviewers(reviewIdName);
    return new ArrayList<String>(reviewers.keySet()).toArray(new String[] {});
  }

  /**
   * Gets the map of the <code>ReviewerId</code> instances. Returns the map of default
   * reviewers if no reviewer exists.
   * 
   * @param reviewIdName the review id name.
   * @return the <code>Map</code> of the <code>ReviewerId</code> instance.
   */
  public Map<String, ReviewerId> getReviewers(String reviewIdName) {
    ReviewId reviewId = this.reviewIdMap.get(reviewIdName);
    Map<String, ReviewerId> reviewersMap = new TreeMap<String, ReviewerId>();
    if (reviewId != null) {
      reviewersMap = reviewId.getReviewers();
    }
    return reviewersMap;
  }

  /**
   * Gets the list of the <code>ReviewId</code> instances.
   * 
   * @return the list of the <code>ReviewId</code> instances.
   */
  public List<ReviewId> getReviewIdList() {
    return new ArrayList<ReviewId>(this.reviewIdMap.values());
  }

  /**
   * Gets the <code>ReviewId</code> instance from the reviewIdName. Returns null if the
   * <code>reviewIdName</code> does not exist.
   * 
   * @param reviewIdName the review id name.
   * @return the <code>ReviewId</code> instance from the reviewIdName. Returns null if the
   *         review id name does not exist.
   */
  public ReviewId getReviewId(String reviewIdName) {
    return this.reviewIdMap.get(reviewIdName);
  }

  /**
   * Adds <code>ReviewResource</code> instance to the property config file.
   * 
   * @param reviewResource the <code>ReviewResource</code> instance.
   * @throws ReviewException if the review id could not be written.
   * @return <code>true</code> if review id does not exist and could be written.
   *         <code>false</code> if review id already exist.
   */
  public boolean addReviewResource(ReviewResource reviewResource) throws ReviewException {
    Element propertyElement = this.propertyDocument.getRootElement();
    if (reviewResource != null) {
      Element reviewElement = reviewResource.getReviewElement();
      ReviewId reviewId = reviewResource.getReviewId();
      propertyElement.addContent(reviewElement);
      PropertyXmlSerializer.serializeDocument(propertyDocument, project);
      this.reviewIdReviewElementMap.put(reviewId.getReviewId(), reviewElement);
      this.reviewIdMap.put(reviewId.getReviewId(), reviewId);
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Removes <code>ReviewId</code> instance from the property config file.
   * 
   * @param reviewId the <code>ReviewId</code> instance.
   * @throws ReviewException if the review id could not be written.
   * @return <code>true</code> if review id exists and could be written. <code>false</code>
   *         if review id does not exist.
   */
  public boolean removeReviewResource(ReviewId reviewId) throws ReviewException {
    Element propertyElement = this.propertyDocument.getRootElement();
    ReviewResource reviewResource = getReviewResource(reviewId.getReviewId(), false);
    // if the element exists, i.e. the review id exists.
    if (reviewResource != null) {
      Element targetReviewElement = reviewResource.getReviewElement();
      propertyElement.removeContent(targetReviewElement);
      PropertyXmlSerializer.serializeDocument(this.propertyDocument, project);
      this.reviewIdReviewElementMap.remove(reviewId.getReviewId());
      this.reviewIdMap.remove(reviewId.getReviewId());
      return true;
    }
    else {
      return false;
    }
  }
}
