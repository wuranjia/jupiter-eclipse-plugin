package edu.hawaii.ics.csdl.jupiter.event;

/**
 * Provides an interface for the Review event listener. 
 * The <code>ReviewEvent</code> will be notified when Review event is invoked in such a way
 * that review command (for example, go to button is clicked) is triggered. 
 * @author Takuya Yamashita
 * @version $Id$
 */
public interface IReviewListener {
  /**
   * Called when a notifier notifies to this listener. <code>ReviewEvent</code> instance might
   * be ant instance of the command.
   * @param event the review event to be notified.
   */
  void reviewInvoked(ReviewEvent event);
}
