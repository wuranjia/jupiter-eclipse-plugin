package edu.hawaii.ics.csdl.jupiter.model.review;

import edu.hawaii.ics.csdl.jupiter.file.PrefResource;

/**
 * Provides the singleton review phase manager class.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class PhaseManager implements IReviewModelElementChangeListener {
  private static PhaseManager theInstance = new PhaseManager();
  private String phaseNameKey;
  private String[] ordinalPhaseArray;
  
  /**
   * Sets default review phase name key. Prohibits clients from instantiating this.
   */
  private PhaseManager() {
    this.phaseNameKey = PrefResource.getInstance().getDefaultPhaseNameKey();
    this.ordinalPhaseArray = PrefResource.getInstance().getPhaseArray(false);
  }
   
  /**
   * Gets the singleton instance.
   * @return the singleton instance.
   */
  static PhaseManager getInstance() {
    return theInstance;
  }
  
  /**
   * Sets phase name key if the notified object is instance of String.
   * @param object The object to be notified.
   */
  public void elementChanged(Object object) {
    if (object instanceof String) {
      setPhaseNameKey((String) object);
    }
  }
  
  /**
   * Sets the review phase name key string.
   * @param phaseNameKey the review phase name string.
   */
  private void setPhaseNameKey(String phaseNameKey) {
    this.phaseNameKey = phaseNameKey;
  }
  
  /**
   * Gets the review phase name key string.
   * @return the review phase name key string.
   */
  public String getPhaseNameKey() {
    return this.phaseNameKey;
  }
  
  /**
   * Gets the elements of the String review phase array.
   * @return the elements of the String review phase array.
   */
  public String[] getElements() {
    return this.ordinalPhaseArray;
  }
}
