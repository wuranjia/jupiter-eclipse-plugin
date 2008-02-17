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
  private static Map<String, Comparator<ReviewIssue>> comparators = new HashMap<String, Comparator<ReviewIssue>>();
  /** The Comparator implementing class, which provides sort by reviewer's name. */
  public static final Comparator<ReviewIssue> REVIEWER = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by reviewer's name
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        String reviewer1 = issue1.getReviewer();
        String reviewer2 = issue2.getReviewer();
        return reviewer1.compareTo(reviewer2);
      }
    };
  /** The Comparator implementing class, which provides sort by respondent's name. */
  public static final Comparator<ReviewIssue> ASSIGNED_TO = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by respondent's name
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getAssignedTo().compareTo(issue2.getAssignedTo());
      }
    };
  /** The Comparator implementing class, which provides sort by creation date. */
  public static final Comparator<ReviewIssue> CREATION_DATE = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by creation date.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getCreationDate().compareTo(issue2.getCreationDate());
      }
    };
  /** The Comparator implementing class, which provides sort by modification date. */
  public static final Comparator<ReviewIssue> MODIFICATION_DATE = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by modification date.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getModificationDate().compareTo(issue2.getModificationDate());
      }
    };
  /** The Comparator implementing class, which provides sort by summary. */
  public static final Comparator<ReviewIssue> SUMMARY = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by summary.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getSummary().compareTo(issue2.getSummary());
      }
    };
  /** The Comparator implementing class, which provides sort by description. */
  public static final Comparator<ReviewIssue> DESCRIPTION = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by description.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getDescription().compareTo(issue2.getDescription());
      }
    };
  /** The Comparator implementing class, which provides sort by annotation. */
  public static final Comparator<ReviewIssue> ANNOTATION = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by annotation.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getAnnotation().compareTo(issue2.getAnnotation());
      }
    };
  /** The Comparator implementing class, which provides sort by revision. */
  public static final Comparator<ReviewIssue> REVISION = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by revision.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        String revision1 = issue1.getRevision();
        String revision2 = issue2.getRevision();
        return revision1.compareTo(revision2);
      }
    };
  /** The Comparator implementing class, which provides sort by type. */
  public static final Comparator<ReviewIssue> TYPE = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by type.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getType().compareTo(issue2.getType());
      }
    };
  /** The Comparator implementing class, which provides sort by severity. */
  public static final Comparator<ReviewIssue> SEVERITY = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by severity.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        Severity severity1 = issue1.getSeverity();
        Severity severity2 = issue2.getSeverity();
        return severity1.compareTo(severity2);
      }
    };
  /** The Comparator implementing class, which provides sort by resolution. */
  public static final Comparator<ReviewIssue> RESOLUTION = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by resolution.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getResolution().compareTo(issue2.getResolution());
      }
    };
  /** The Comparator implementing class, which provides sort by status. */
  public static final Comparator<ReviewIssue> STATUS = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by status.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return ((ReviewIssue) issue1).getStatus().compareTo(((ReviewIssue) issue2).getStatus());
      }
    };
  /** The Comparator implementing class, which provides sort by file name. */
  public static final Comparator<ReviewIssue> FILE = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by file name.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getTargetFile().compareTo(issue2.getTargetFile());
      }
    };
  /** The Comparator implementing class, which provides sort by line number. */
  public static final Comparator<ReviewIssue> LINE = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by line number.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        int issue1IntValue = Integer.MAX_VALUE;
        try {
          issue1IntValue = Integer.parseInt(issue1.getLine());
        }
        catch (NumberFormatException e) {
          // use int max value;
        }
        int issue2IntValue = Integer.MAX_VALUE;
        try {
          issue2IntValue = Integer.parseInt(issue2.getLine());
        }
        catch (NumberFormatException e) {
          // use int max value;
        }
        return issue1IntValue - issue2IntValue;
      }
    };
  /** The Comparator implementing class, which provides sort by ID name. */
  public static final Comparator<ReviewIssue> ID = new Comparator<ReviewIssue>() {
      /**
       * Compares ReviewIssue instance by Id name.
       *
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      public int compare(ReviewIssue issue1, ReviewIssue issue2) {
        return issue1.getIssueId().compareTo(issue2.getIssueId());
      }
    };
    
  /** The Comparator implementing class, which provides sort by link icon. */
  public static final Comparator<ReviewIssue> LINK_ICON = new Comparator<ReviewIssue>() {
    /**
     * Compares ReviewIssue instance by link icon. The small number lists first.
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(ReviewIssue issue1, ReviewIssue issue2) {
      String issue1Ordinal = issue1.isLinked() ? "0" : "1";
      String orjbec2Ordinal = issue2.isLinked() ? "0" : "1";
      return issue1Ordinal.compareTo(orjbec2Ordinal);
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
    return comparators.get(comparatorNameKey);
  }
}
