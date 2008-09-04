package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import org.eclipse.core.resources.IProject;

import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;

/**
 * Provides the keys for the <code>Status</code>.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class StatusKeyManager extends KeyManager {
  /** The singleton instance. */
  private static StatusKeyManager theInstance;

  /**
   * Instantiates supper class and fill key status item into this instance from XML file.
   */
  private StatusKeyManager() {
    super();
  }

  /**
   * Gets the <code>StatusKeyManager</code> instance.
   * 
   * @param project the project.
   * @param reviewId the review id.
   * @return the <code>StatusKeyManager</code> instance.
   */
  public static StatusKeyManager getInstance(IProject project, ReviewId reviewId) {
    if (theInstance == null) {
      theInstance = new StatusKeyManager();
    }
    if (project != null && reviewId != null) {
      theInstance.loadKey(project, reviewId);
    }
    return theInstance;
  }
  
  /**
   * Loads resolution key.
   * @param project the project.
   * @param reviewId the review id.
   */
  private void loadKey(IProject project, ReviewId reviewId) {
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    String reviewIdName = reviewId.getReviewId();
    ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdName, true);
    if (reviewResource != null) {
      reviewResource.loadEntryKey(PropertyConstraints.ATTRIBUTE_VALUE_STATUS, this);
    }
  }

  /**
   * Gets the <code>Status</code> instance from the status key. Returns the instance with
   * empty string key and Integer.MAX_VALUE ordinal number if the corresponding item is not found.
   *
   * @param key the status key.
   *
   * @return the object, which is able to cast <code>Status</code> instance.
   */
  public Object getItemObject(String key) {
    if ((key != null) && (!key.equals(""))) {
      return new Status(key, getOrdinal(key));
    }
    return new Status("", Integer.MAX_VALUE);
  }

  /**
   * Gets the <code>Status</code> instance in the index. Returns the instance with empty string key
   * and Integer.MAX_VALUE ordinal number if the corresponding item is not found.
   *
   * @param index the index of the <code>Status</code> instance.
   *
   * @return the <code>Status</code> instance in the index.
   */
  public Object getItem(int index) {
    try {
      String key = (String) ordinalKeys.get(index);
      return new Status(key, getOrdinal(key));
    }
    catch (IndexOutOfBoundsException e) {
      return new Status("", Integer.MAX_VALUE);
    }
  }
}
