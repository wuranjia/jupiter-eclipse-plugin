package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import org.eclipse.core.resources.IProject;

import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;

/**
 * Provides the keys for the <code>Type</code>.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class TypeKeyManager extends KeyManager {
  /** The singleton instance. */
  private static TypeKeyManager theInstance;

  /**
   * Instantiates supper class and fill key type item into this instance from XML file.
   */
  private TypeKeyManager() {
    super();
  }

  /**
   * Gets the <code>TypeKeyManager</code> instance.
   * 
   * @param project the project.
   * @param reviewId the review id.
   * @return the <code>TypeKeyManager</code> instance.
   */
  public static TypeKeyManager getInstance(IProject project, ReviewId reviewId) {
    if (theInstance == null) {
      theInstance = new TypeKeyManager();
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
      reviewResource.loadEntryKey(PropertyConstraints.ATTRIBUTE_VALUE_TYPE, this);
    }
  }

  /**
   * Gets the <code>Type</code> instance from the type key. Returns the instance with
   * empty string key and Integer.MAX_VALUE ordinal number if the corresponding item is not found.
   *
   * @param key the type key.
   *
   * @return the object, which is able to cast <code>Type</code> instance.
   */
  public Object getItemObject(String key) {
    if ((key != null) && (!key.equals(""))) {
      return new Type(key, getOrdinal(key));
    }
    return new Type("", Integer.MAX_VALUE);
  }

  /**
   * Gets the <code>Type</code> instance in the index. Returns the instance with
   * empty string key and Integer.MAX_VALUE ordinal number if the corresponding item is not found.
   *
   * @param index the index of the <code>Type</code> instance.
   *
   * @return the <code>Type</code> instance in the index.
   */
  public Object getItem(int index) {
    try {
      String key = (String) ordinalKeys.get(index);
      return new Type(key, getOrdinal(key));
    }
    catch (IndexOutOfBoundsException e) {
      return new Type("", Integer.MAX_VALUE);
    }
  }
}
