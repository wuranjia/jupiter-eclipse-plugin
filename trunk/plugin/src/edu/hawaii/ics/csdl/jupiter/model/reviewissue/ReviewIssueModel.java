package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.hawaii.ics.csdl.jupiter.event.IReviewIssueModelListener;
import edu.hawaii.ics.csdl.jupiter.event.ReviewIssueModelEvent;
import edu.hawaii.ics.csdl.jupiter.ui.marker.ReviewMarker;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides a model for the Code ReviewIssue. Every time code review structure is changed such as
 * addition, modification, deletion, this notifier notifies the registered ICodeReivew Listener's
 * implementing class.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIssueModel implements IStructuredContentProvider {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  /** The listener container to contains IReviewIssueModelListener listeners. */
  private ListenerList listenerList = new ListenerList();
  /** The map contains the code review id and it mapped <code>ReviewIssue</code> instance. */
  private Map<String, ReviewIssue> reviewIssueMap = new TreeMap<String, ReviewIssue>();
  /**
   * The list container to contain the ReviewIssue id string.
   * This is used for the order of the list.
   */
  private List<ReviewIssue> reviewIssueOrdinalList = new LinkedList<ReviewIssue>();
  /** The comparator to be used for current sort. */
  private Comparator comparator;
  /** The code review instance to be notified to the listeners. */
  private ReviewIssue notifyTargetReviewIssue;
  /** The map of the String target file - String target file. */
  private Map<String, String> targetFileMap = new TreeMap<String, String>();
  
  private boolean isReversed = false;

  /**
   * Adds the IReviewIssueModelListener's implementing class to be notified when This model is
   * changed.
   *
   * @param listener Description of the Parameter
   */
  public void addListener(IReviewIssueModelListener listener) {
    listenerList.add(listener);
  }

  /**
   * Remove the IReviewIssueModelListener's implementing class from this listener list.
   *
   * @param listener Description of the Parameter
   */
  public void removeListener(IReviewIssueModelListener listener) {
    listenerList.remove(listener);
  }

  /**
   * Adds the new CodeReivew instance to this model. If addition was succeed, then The event are
   * notified to all the listeners.
   *
   * @param reviewIssue The ReviewIssue instance to be added.
   *
   * @return <code>true</code> if it's successfully added. <code>false</code> if the element
   *         already exists.
   */
  public boolean add(ReviewIssue reviewIssue) {
    if ((reviewIssue != null) && !this.contains(reviewIssue.getIssueId())) {
      this.reviewIssueMap.put(reviewIssue.getIssueId(), reviewIssue);
      ((LinkedList) this.reviewIssueOrdinalList).addFirst(reviewIssue);
      ReviewMarker.addMarker(reviewIssue);
      String targetFile = reviewIssue.getTargetFile();
      this.targetFileMap.put(targetFile, targetFile);
      this.notifyTargetReviewIssue = reviewIssue;
      return true;
    }
    else {
      this.notifyTargetReviewIssue = reviewIssue;
      return false;
    }
  }
  
  /**
   * Adds the list of the new code reviews to this model. No review is added if the
   * <code>List</code> is null.
   * @param reviewIssueList the list of the new code reviews.
   */
  public void addAll(List reviewIssueList) {
    if (reviewIssueList != null) {
      for (Iterator i = reviewIssueList.iterator(); i.hasNext();) {
        this.add((ReviewIssue) i.next());
      }
    }
  }

  /**
   * Check if the ReviewIssue instance contains from the issue ID.
   *
   * @param issueId The issue Id of a <code>ReviewIssue</code> instance to be checked.
   *
   * @return <code>true</code> if it contains. <code>false</code> otherwise.
   */
  public boolean contains(String issueId) {
    return this.reviewIssueMap.containsKey(issueId);
  }

  /**
   * Gets the <code>ReviewIssue</code> instance contained in this model. Clients can use this
   * to modify the content of the code review instance. Return null if the instance does not
   * exists in this model. Note that even though the content of the passing instance
   * is the same as the that of returning instance, the address of instance might be different.
   *
   * @param issueId the issue Id of the <code>ReviewIssue</code> instance to be retrieved.
   * @return the <code>ReviewIssue</code> instance contained in this model.
   */
  public ReviewIssue get(String issueId) {
    ReviewIssue reviewIssue = (ReviewIssue) this.reviewIssueMap.get(issueId);
    if (reviewIssue != null) {
      this.notifyTargetReviewIssue = reviewIssue;
      return this.notifyTargetReviewIssue;
    }
    else {
      return null;
    }
  }
  
  /**
   * Gets the array of the String target file.
   * @return the array of the String target file.
   */
  public String[] getTargetFileArray() {
    return (String[]) this.targetFileMap.values().toArray(new String[] {});
  }

  /**
   * Provides the size of this model.
   *
   * @return The size of this model.
   */
  public int size() {
    return this.reviewIssueMap.size();
  }

  /**
   * Removes the ReviewIssue instance form this model.If deletion was succeed, then The event are
   * notified to all the listeners.
   *
   * @param reviewIssue The ReviewIssue id key to be removed.
   *
   * @return <code>true</code> if it's successfully removed.
   */
  public boolean remove(ReviewIssue reviewIssue) {
    if (reviewIssue != null) {
      this.reviewIssueMap.remove(reviewIssue.getIssueId());
      ReviewMarker.removeMarker(reviewIssue);
      this.reviewIssueOrdinalList.remove(reviewIssue);
      this.notifyTargetReviewIssue = reviewIssue;
      return true;
    }
    else {
      this.notifyTargetReviewIssue = reviewIssue;
      return false;
    }
  }

  /**
   * Clears the model contents.
   */
  public void clear() {
    this.reviewIssueMap.clear();
    this.reviewIssueOrdinalList.clear();
    this.notifyTargetReviewIssue = null;
    this.targetFileMap.clear();
    ReviewMarker.clearMarkersInReviewId();
  }

  /**
   * Iterates the ReviewIssue instance.
   *
   * @return The Iterator instance.
   */
  public Iterator iterator() {
    return reviewIssueMap.values().iterator();
  }

  /**
   * Notifies the listeners who implement the <code>IReviewIssueModelListener</code>. This
   * method can be invoked anytime, but it is designed for the function to let listeners to know
   * the model is update so that it is ideal after <code>add(ReviewIssue)</code> or
   * <code>remove(ReviewIssue)</code> method invocation.
   * 
   * @param type the event type of the <code>ReviewIssueModelEvent</code>
   * when notifying to listeners.
   */
  public void notifyListeners(int type) {
    this.notifyListeners(new ReviewIssueModelEvent(this.notifyTargetReviewIssue, type));
  }

  /**
   * Notifies the ReviewIssue
   *
   * @param event the <code>CodeReviewModelEvent</code> instance.
   */
  private void notifyListeners(ReviewIssueModelEvent event) {
    Object[] listeners = listenerList.getListeners();
    for (int i = 0; i < listeners.length; i++) {
      ((IReviewIssueModelListener) listeners[i]).reviewIssueModelChanged(event);
    }
  }

  /**
   * Gets the elements of this model. Each element type is ReviewIssue instance.
   *
   * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
   */
  public Object[] getElements(Object inputElement) {
    return this.reviewIssueOrdinalList.toArray();
  }

  /**
   * Sorts this model by the order defined by Comparator implementing class. Reverse the model if
   * you have passed the same comparator instance as the previous comparator instance. Sorts by
   * natural order if <code>comparator</code> parameter is <code>null</code>.
   *
   * @param comparator the comparator to determine how this model is sorted.
   */
  public void sortBy(Comparator comparator) {
    if (this.comparator == comparator) {
      Collections.reverse(reviewIssueOrdinalList);
      isReversed = !isReversed;
    }
    else {
      Collections.sort(reviewIssueOrdinalList, comparator);
      isReversed = false;
      this.comparator = comparator;
    }
  }
  
  /**
   * Sorts this model by the previous comparator.
   */
  public void sortByPreviousComparator() {
    if (comparator != null) {
      Collections.sort(reviewIssueOrdinalList, comparator);
      if (isReversed) {
        Collections.reverse(reviewIssueOrdinalList);
      }
    }
  }
  
  /**
   * Clears the comparator used in this model.
   */
  public void clearComparator() {
    this.comparator = null;
  }

  /**
   * <b>Description copied from interface:</b>{@link org.eclipse.jface.viewers.IContentProvider}
   * Disposes of this content provider. This is called by the viewer when it is disposed.
   *
   * @see org.eclipse.jface.viewers.IContentProvider#dispose()
   */
  public void dispose() {
    // nothing so far.
  }

  /**
   * <b>Description copied from interface:</b>{@link org.eclipse.jface.viewers.IContentProvider}
   * Notifies this content provider that the given viewer's input has been switched to a different
   * element.  A typical use for this method is registering the content provider as a listener to
   * changes on the new input (using model-specific means), and deregistering the viewer from the
   * old input. In response to these change notifications, the content provider should update the
   * viewer (see the add, remove, update and refresh methods on the viewers).  The viewer should
   * not updated during this call, as it might be in the process of being disposed.
   *
   * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
   *      java.lang.Object, java.lang.Object)
   */
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    // nothing so far
    log.debug("inputChanage was called.");
  }
}
