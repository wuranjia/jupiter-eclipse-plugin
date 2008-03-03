package edu.hawaii.ics.csdl.jupiter.model.review;

/**
 * Provides the listener for the element change of the review model.
 * @author Takuya Yamashita
 * @version $Id$
 */
interface IReviewModelElementChangeListener {

  /**
   * Notified when an element of the ReviewModel is changed. Clients should check
   * if the notified object is instance of the element instance (e.g. IProject, ReviewId, String).
   * @param object The object to be notified.
   */
  void elementChanged(Object object);
}
