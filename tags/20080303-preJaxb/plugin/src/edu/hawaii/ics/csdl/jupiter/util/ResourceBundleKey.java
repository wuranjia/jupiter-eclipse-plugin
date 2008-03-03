package edu.hawaii.ics.csdl.jupiter.util;

/**
 * Provides the keys in the resource bundle. Used as constant value if the keys are supposed to
 * embedded in a source code. The main purpose of this is for the maintenance to reduce the cost
 * to replace resource bundle key if they are supposed to be changed.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ResourceBundleKey {
  /** The key for  the individual phase. */
  public static final String PHASE_INFIVIDUAL = "phase.individual";
  /** The key for the team phase. */
  public static final String PHASE_TEAM = "phase.team";
  /** The key for the rework phase. */
  public static final String PHASE_REWORK = "phase.rework";
  /** The key for the column header link icon label. */
  public static final String COLUMN_HEADER_LINK_ICON = "columnHeader.label.linkIcon";
  /** The key for the column header severity label. */
  public static final String COLUMN_HEADER_SEVERITY = "columnHeader.label.severity";
  /** The key for the column header type label. */
  public static final String COLUMN_HEADER_TYPE = "columnHeader.label.type";
  /** The key for the column header summary label. */
  public static final String COLUMN_HEADER_SUMMARY = "columnHeader.label.summary";
  /** The key for the column header description label. */
  public static final String COLUMN_HEADER_DESCRIPTION = "columnHeader.label.description";
  /** The key for the column header annotation label. */
  public static final String COLUMN_HEADER_ANNOTATION = "columnHeader.label.annotation";
  /** The key for the column header revision label. */
  public static final String COLUMN_HEADER_REVISION = "columnHeader.label.revision";
  /** The key for the column header resolution label. */
  public static final String COLUMN_HEADER_RESOLUTION = "columnHeader.label.resolution";
  /** The key for the column header status label. */
  public static final String COLUMN_HEADER_STATUS = "columnHeader.label.status";
  /** The key for the column header assigned to label. */
  public static final String COLUMN_HEADER_ASSGINED_TO = "columnHeader.label.assignedTo";
  /** The key for the column header file label. */
  public static final String COLUMN_HEADER_FILE = "columnHeader.label.file";
  /** The key for the column header line label. */
  public static final String COLUMN_HEADER_LINE = "columnHeader.label.line";
  /** The key for the column header reviewer label. */
  public static final String COLUMN_HEADER_REVIEWER = "columnHeader.label.reviewer";
  /** The key for the column header creation date label. */
  public static final String COLUMN_HEADER_CREATED_DATE = "columnHeader.label.creationDate";
  /** The key for the column header modification date label. */
  public static final String COLUMN_HEADER_MODIFIED_DATE = "columnHeader.label.modificationDate";
  /** The key for the column header id label. */
  public static final String COLUMN_HEADER_ID = "columnHeader.label.id";
  /** The key for the reviewer automatic label. */
  public static final String ITEM_KEY_REVIEWER_AUTOMATIC = "item.reviewer.label.automatic";
  /** The key for the unset label. */
  public static final String ITEM_KEY_UNSET = "item.label.unset";
  /** The key for the defect label of the type. */
  public static final String ITEM_KEY_TYPE_DEFECT = "item.type.label.defect";
  /** The key for the external issue label of the type. */
  public static final String ITEM_KEY_TYPE_EXTERNAL_ISSUE = "item.type.label.externalIssue";
  /** The key for the question label of the type. */
  public static final String ITEM_KEY_TYPE_QUESTION = "item.type.label.question";
  /** The key for the praise label of the type. */
  public static final String ITEM_KEY_TYPE_PRAISE = "item.type.label.praise";
  /** The key for the critical of the severity. */
  public static final String ITEM_KEY_SEVERITY_CRITICAL = "item.severity.label.critical";
  /** The key for the major of the severity. */
  public static final String ITEM_KEY_SEVERITY_MAJOR = "item.severity.label.major";
  /** The key for the normal of the severity. */
  public static final String ITEM_KEY_SEVERITY_NORMAL = "item.severity.label.normal";
  /** The key for the minor of the severity. */
  public static final String ITEM_KEY_SEVERITY_MINOR = "item.severity.label.minor";
  /** The key for the trivial of the severity. */
  public static final String ITEM_KEY_SEVERITY_TRIVIAL = "item.severity.label.trivial";
  /** The key for the valid needsfixing of the resolution. */
  public static final String ITEM_KEY_RESOLUTION_VALID_NEEDSFIXING =
                                                        "item.resolution.label.validNeedsfixing";
  /** The key for the valid fixlater of the resolution. */
  public static final String ITEM_KEY_RESOLUTION_VALID_FIXLATER = 
                                                        "item.resolution.label.validFixlater";
  /** The key for the valid duplicate of the resolution. */
  public static final String ITEM_KEY_RESOLUTION_VALID_DUPLICATE = 
                                                        "item.resolution.label.validDuplicate";
  /** The key for the valid wontfix of the resolution. */
  public static final String ITEM_KEY_RESOLUTION_VALID_WONTFIX = 
                                                        "item.resolution.label.validWontfix";
  /** The key for the invalid wontfix of the resolution. */
  public static final String ITEM_KEY_RESOLUTION_INVALID_WONTFIX =
                                                        "item.resolution.label.invalidWontfix";
  /** The key for the unsure validity of the resolution. */
  public static final String ITEM_KEY_RESOLUTION_UNSURE_VALIDITY =
                                                        "item.resolution.label.unsureValidity";
  /** The key for the unresolved of the status. */
  public static final String ITEM_KEY_STATUS_UNRESOLVED = "item.status.label.unresolved";
  /** The key for the resolved of the status. */
  public static final String ITEM_KEY_STATUS_RESOLVED = "item.status.label.resolved";
  /** The key for the property default description. */
  public static final String PROPERTY_DEFAULT_DESCRIPTION = "property.default.description";
  
  /** Prohibits clients' instantiation. */
  private ResourceBundleKey() {
  }
}
