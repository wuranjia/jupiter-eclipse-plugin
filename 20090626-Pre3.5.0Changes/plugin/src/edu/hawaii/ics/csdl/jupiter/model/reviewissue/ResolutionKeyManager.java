package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import org.eclipse.core.resources.IProject;

import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;

/**
 * Provides the keys for the <code>Resolution</code>.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ResolutionKeyManager extends KeyManager {
  /** The singleton instance. */
  private static ResolutionKeyManager theInstance;

  /**
   * Instantiates supper class and fill key resolution item into this instance from XML file.
   */
  private ResolutionKeyManager() {
    super();
  }

  /**
   * Gets the <code>ResolutionKeyManager</code> instance.
   * 
   * @param project the project.
   * @param reviewId the review id.
   * @return the <code>ResolutionKeyManager</code> instance.
   */
  public static ResolutionKeyManager getInstance(IProject project, ReviewId reviewId) {
    if (theInstance == null) {
      theInstance = new ResolutionKeyManager();
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
      reviewResource.loadEntryKey(PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION, this);
    }
  }

  /**
   * Gets the <code>Resolution</code> instance from the resolution key. Returns the instance with
   * empty string key and Integer.MAX_VALUE ordinal number if the corresponding item is not found.
   *
   * @param key the resolution key.
   *
   * @return the object, which is able to cast <code>Resolution</code> instance.
   */
  public Object getItemObject(String key) {
    if ((key != null) && (!key.equals(""))) {
      return new Resolution(key, getOrdinal(key));
    }
    return new Resolution("", Integer.MAX_VALUE);
  }

  /**
   * Gets the <code>Resolution</code> instance in the index.  Returns the instance with
   * empty string key and Integer.MAX_VALUE ordinal number if the corresponding item is not found.
   *
   * @param index the index of the <code>Resolution</code> instance.
   *
   * @return the <code>Resolution</code> instance in the index.
   */
  public Object getItem(int index) {
    try {
      String key = (String) ordinalKeys.get(index);
      return new Resolution(key, getOrdinal(key));
    }
    catch (IndexOutOfBoundsException e) {
      return new Resolution("", Integer.MAX_VALUE);
    }
  }
}
