package edu.hawaii.ics.csdl.jupiter.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Severity;

/**
 * Provides static sets of Comparator implementing class for sort of the model of
 * CodeReviewContentProviderMode class.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewComparator {
  
  /** The comparator container to hold comparators. */
  private static Map comparators = new HashMap();
  /** The Comparator implementing class, which provides sort by reviewer's name. */
  public static final Comparator REVIEWER = new Comparator() {
      /**
       * Compares ReviewIssue instance by reviewer's name
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        String reviewer1 = ((ReviewIssue) object1).getReviewer();
        String reviewer2 = ((ReviewIssue) object2).getReviewer();
        return reviewer1.compareTo(reviewer2);
      }
    };
  /** The Comparator implementing class, which provides sort by respondent's name. */
  public static final Comparator ASSIGNED_TO = new Comparator() {
      /**
       * Compares ReviewIssue instance by respondent's name
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getAssignedTo().compareTo(((ReviewIssue) object2)
          .getAssignedTo());
      }
    };
  /** The Comparator implementing class, which provides sort by creation date. */
  public static final Comparator CREATION_DATE = new Comparator() {
      /**
       * Compares ReviewIssue instance by creation date.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getCreationDate().compareTo(((ReviewIssue) object2)
          .getCreationDate());
      }
    };
  /** The Comparator implementing class, which provides sort by modification date. */
  public static final Comparator MODIFICATION_DATE = new Comparator() {
      /**
       * Compares ReviewIssue instance by modification date.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getModificationDate().compareTo(((ReviewIssue) object2)
          .getModificationDate());
      }
    };
  /** The Comparator implementing class, which provides sort by summary. */
  public static final Comparator SUMMARY = new Comparator() {
      /**
       * Compares ReviewIssue instance by summary.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getSummary().compareTo(((ReviewIssue) object2).getSummary());
      }
    };
  /** The Comparator implementing class, which provides sort by description. */
  public static final Comparator DESCRIPTION = new Comparator() {
      /**
       * Compares ReviewIssue instance by description.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getDescription().compareTo(((ReviewIssue) object2)
          .getDescription());
      }
    };
  /** The Comparator implementing class, which provides sort by annotation. */
  public static final Comparator ANNOTATION = new Comparator() {
      /**
       * Compares ReviewIssue instance by annotation.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getAnnotation().compareTo(((ReviewIssue) object2)
          .getAnnotation());
      }
    };
  /** The Comparator implementing class, which provides sort by revision. */
  public static final Comparator REVISION = new Comparator() {
      /**
       * Compares ReviewIssue instance by revision.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        String revision1 = ((ReviewIssue) object1).getRevision();
        String revision2 = ((ReviewIssue) object2).getRevision();
        return revision1.compareTo(revision2);
      }
    };
  /** The Comparator implementing class, which provides sort by type. */
  public static final Comparator TYPE = new Comparator() {
      /**
       * Compares ReviewIssue instance by type.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getType().compareTo(((ReviewIssue) object2).getType());
      }
    };
  /** The Comparator implementing class, which provides sort by severity. */
  public static final Comparator SEVERITY = new Comparator() {
      /**
       * Compares ReviewIssue instance by severity.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        Severity severity1 = ((ReviewIssue) object1).getSeverity();
        Severity severity2 = ((ReviewIssue) object2).getSeverity();
        return severity1.compareTo(severity2);
      }
    };
  /** The Comparator implementing class, which provides sort by resolution. */
  public static final Comparator RESOLUTION = new Comparator() {
      /**
       * Compares ReviewIssue instance by resolution.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getResolution().compareTo(((ReviewIssue) object2)
          .getResolution());
      }
    };
  /** The Comparator implementing class, which provides sort by status. */
  public static final Comparator STATUS = new Comparator() {
      /**
       * Compares ReviewIssue instance by status.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getStatus().compareTo(((ReviewIssue) object2).getStatus());
      }
    };
  /** The Comparator implementing class, which provides sort by file name. */
  public static final Comparator FILE = new Comparator() {
      /**
       * Compares ReviewIssue instance by file name.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getTargetFile().compareTo(((ReviewIssue) object2)
          .getTargetFile());
      }
    };
  /** The Comparator implementing class, which provides sort by line number. */
  public static final Comparator LINE = new Comparator() {
      /**
       * Compares ReviewIssue instance by line number.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        int object1IntValue = Integer.MAX_VALUE;
        try {
          object1IntValue = Integer.parseInt(((ReviewIssue) object1).getLine());
        }
        catch (NumberFormatException e) {
          // use int max value;
        }
        int object2IntValue = Integer.MAX_VALUE;
        try {
          object2IntValue = Integer.parseInt(((ReviewIssue) object2).getLine());
        }
        catch (NumberFormatException e) {
          // use int max value;
        }
        return object1IntValue - object2IntValue;
      }
    };
  /** The Comparator implementing class, which provides sort by ID name. */
  public static final Comparator ID = new Comparator() {
      /**
       * Compares ReviewIssue instance by Id name.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(Object object1, Object object2) {
        return ((ReviewIssue) object1).getIssueId().compareTo(((ReviewIssue) object2).getIssueId());
      }
    };
    
  /** The Comparator implementing class, which provides sort by link icon. */
  public static final Comparator LINK_ICON = new Comparator() {
    /**
     * Compares ReviewIssue instance by link icon. The small number lists first.
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object object1, Object object2) {
      String object1Ordinal = (((ReviewIssue) object1).isLinked() ? "0" : "1");
      String orjbec2Ordinal = (((ReviewIssue) object2).isLinked() ? "0" : "1");
      return object1Ordinal.compareTo(orjbec2Ordinal);
    }
  };

  static {
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_REVIEWER), REVIEWER);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_ASSGINED_TO), ASSIGNED_TO);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_CREATED_DATE), 
                                         CREATION_DATE);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_MODIFIED_DATE), 
                                         MODIFICATION_DATE);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_SUMMARY), SUMMARY);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_DESCRIPTION), DESCRIPTION);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_ANNOTATION), ANNOTATION);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_REVISION), REVISION);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_TYPE), TYPE);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_SEVERITY), SEVERITY);    
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_RESOLUTION), RESOLUTION);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_STATUS), STATUS);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_FILE), FILE);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_LINE), LINE);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_ID), ID);
    comparators.put(ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_LINK_ICON), LINK_ICON);
  }

  /**
   * Gets the <code>Comparator</code> instance from the specified string. Returns <code>null</code>
   * if the corresponding <code>Comparator</code> instance does not exist.
   *
   * @param comparatorNameKey the comparator name key to retrieve the corresponding
   *        <code>Comparator</code>
   *
   * @return the <code>Comparator</code> instance. Returns <code>null</code> if the corresponding
   *         <code>Comparator</code> instance does not exist.
   */
  public static Comparator getComparator(String comparatorNameKey) {
    return (Comparator) comparators.get(comparatorNameKey);
  }
}
