package edu.hawaii.ics.csdl.jupiter.event;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ResolutionKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.SeverityKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.StatusKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.TypeKeyManager;


/**
 * Provides review event. This wraps this with an event type and event kind. This
 * is used for the argument of the <code>reviewInvoked</code> in the
 * <code>IReviewListener</code> interface.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewEvent {
  /** The type of the command invocation. */
  public static final int TYPE_COMMAND = 1;
  /** The type of the active views. */
  public static final int TYPE_ACTIVATE = 2;
  /** The type of the focus views. */
  public static final int TYPE_FOCUS = 4;
  /** The kind of the table for types. */
  public static final int KIND_TABLE = 1;
  /** The kind of the editor for types. */
  public static final int KIND_EDITOR = 2;
  /** The kind of the edit for the command type. */
  public static final int KIND_EDIT = 4;
  /** The kind of the notify editor for the command type. */
  public static final int KIND_NOTIFY_EDITOR = 8;
  /** The kind of the goto for the command type. */
  public static final int KIND_GOTO = 16;
  /** The kind of the add for the command type. */
  public static final int KIND_ADD = 32;
  /** The kind of the delete for the command type. */
  public static final int KIND_DELETE = 64;
  /** The kind of the filter for the command type. */
  public static final int KIND_FILTER = 128;
  /** The kind of the filter setting for the command type. */
  public static final int KIND_FILTER_SETTING = 256;
  /** The kind of the phase selection for the command type. */
  public static final int KIND_PHASE_SELECTION = 512;
  /** The kind of the next for the command type. */
  public static final int KIND_NEXT = 1024;
  /** The kind of the previous for the command type. */
  public static final int KIND_PREVIOUS = 2048;
  /** The kind of the save for the command type. */
  public static final int KIND_SAVE = 4096;
  /** The kind of the clear for the command type. */
  public static final int KIND_CLEAR = 8192;
  /** Type item category key */
  public static final String TYPE = "type";
  /** Severity item category key */
  public static final String SEVERITY = "severity";
  /** Resolution item category key */
  public static final String RESOLUTION = "resolution";
  /** Status item category key */
  public static final String STATUS = "status";
  /** The type of this event. */
  private int type;
  /** The kind of a type */
  private int kind;
  /** The reviewing file path on the event. */
  private String filePath;
  /** The review model containing project, phase, review id, and reviewer id. */
  private ReviewModel reviewModel;
  /** String category key -> the String array of the item value.*/
  private Map categoryToItems;
  
  /**
   * Instantiates review event with type and kind.
   * @param type the type of this event.
   * @param kind the kind of the type.
   */
  public ReviewEvent(int type, int kind) {
    this.reviewModel = ReviewModel.getInstance();
    this.type = type;
    this.kind = kind;
    this.filePath = EventFileManager.getInstance().getEventFilePath();
    this.categoryToItems = new HashMap();
    IProject project = this.reviewModel.getProjectManager().getProject();
    ReviewId reviewId = this.reviewModel.getReviewIdManager().getReviewId();
    if (project == null || reviewId == null) {
      return;
    }
    String[] types = TypeKeyManager.getInstance(project, reviewId).getElements();
    this.categoryToItems.put(TYPE, types);
    String[] severities = SeverityKeyManager.getInstance(project, reviewId).getElements();
    this.categoryToItems.put(SEVERITY, severities);
    String[] resolutions = ResolutionKeyManager.getInstance(project, reviewId).getElements();
    this.categoryToItems.put(RESOLUTION, resolutions);
    String[] status = StatusKeyManager.getInstance(project, reviewId).getElements();
    this.categoryToItems.put(STATUS, status);
  }
  
  /**
   * Returns the type of this event.
   * @return the type of this event.
   */
  public int getType() {
    return this.type;
  }
  
  /**
   * Returns the kind of the type.
   * @return the kind of the type.
   */
  public int getKind() {
    return this.kind;
  }
  
  /**
   * Returns the event associated fully qualified file path. Returns empty string if there is no
   * associated file on the event.
   * @return the associated fully qualified file path. Returns empty string if there is no
   * associated file on the event.
   */
  public String getFilePath() {
    return this.filePath;
  }
  
  /**
   * Returns the <code>ReviewModel</code> instance.
   * @return the <code>ReviewModel</code> instance.
   */
  public ReviewModel getReviewModel() {
    return this.reviewModel;
  }
  
  /**
   * Gets the map of the String category key -> the String ordinal array of 
   * the item value.
   * For example, 
   * <pre>
   * String[] types = (String[]) event.getCategories.get(ReviewEvent.TYPE);
   * </pre>
   * @return the map of the String category key -> the String array of the item value.
   */
  public Map getCategories() {
    return this.categoryToItems;
  }
}
