package edu.hawaii.ics.csdl.jupiter;

import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Resolution;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Severity;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Status;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Type;
import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;

import junit.framework.TestCase;

/**
 * Provides test case abstract class for code review system. Sets up necessary item type instances.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewTestCase extends TestCase {
  /** The defect <code>Type</code> instance. */
  protected Type defect;
  /** The externalIssue <code>Type</code> instance. */
  protected Type externalIssue;
  /** The question <code>Type</code> instance. */
  protected Type question;
  /** The praise <code>Type</code> instance. */
  protected Type praise;
  /** The critical <code>Severity</code> instance. */
  protected Severity critical;
  /** The major <code>Severity</code> instance. */
  protected Severity major;
  /** The normal <code>Severity</code> instance. */
  protected Severity normal;
  /** The minor <code>Severity</code> instance. */
  protected Severity minor;
  /** The trivial <code>Severity</code> instance. */
  protected Severity trivial;
  /** The unset <code>Resolution</code> instance. */
  protected Resolution unset;
  /** The validNeedsfixing <code>Resolution</code> instance. */
  protected Resolution validNeedsfixing;
  /** The validFixlater <code>Resolution</code> instance. */
  protected Resolution validFixlater;
  /** The validDuplicate <code>Resolution</code> instance. */
  protected Resolution validDuplicate;
  /** The validWontfix <code>Resolution</code> instance. */
  protected Resolution validWontfix;
  /** The invalidWontfix <code>Resolution</code> instance. */
  protected Resolution invalidWontfix;
  /** The unsureValidity <code>Resolution</code> instance. */
  protected Resolution unsureValidity;
  /** The unresolved <code>Status</code> instance. */
  protected Status unresolved;
  /** The resolved <code>Status</code> instance. */
  protected Status resolved;

  /**
   * Sets up the necessary item type instances.
   *
   * @exception Exception if errors occur in implementing class.
   */
  protected void setUp() throws Exception {
    this.defect = new Type(ResourceBundleKey.ITEM_KEY_TYPE_DEFECT, 0);
    this.externalIssue = new Type(ResourceBundleKey.ITEM_KEY_TYPE_EXTERNAL_ISSUE, 1);
    this.question = new Type(ResourceBundleKey.ITEM_KEY_TYPE_QUESTION, 2);
    this.praise = new Type(ResourceBundleKey.ITEM_KEY_TYPE_PRAISE, 3);
    this.critical = new Severity(ResourceBundleKey.ITEM_KEY_SEVERITY_CRITICAL, 0);
    this.major = new Severity(ResourceBundleKey.ITEM_KEY_SEVERITY_MAJOR, 1);
    this.normal = new Severity(ResourceBundleKey.ITEM_KEY_SEVERITY_NORMAL, 2);
    this.minor = new Severity(ResourceBundleKey.ITEM_KEY_SEVERITY_MINOR, 3);
    this.trivial = new Severity(ResourceBundleKey.ITEM_KEY_SEVERITY_TRIVIAL, 4);
    String resolutionKey = ResourceBundleKey.ITEM_KEY_UNSET;
    this.unset = new Resolution(resolutionKey, 0);
    resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_NEEDSFIXING;
    this.validNeedsfixing = new Resolution(resolutionKey, 1);
    resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_FIXLATER;
    this.validFixlater = new Resolution(resolutionKey, 2);
    resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_DUPLICATE;
    this.validDuplicate = new Resolution(resolutionKey, 3);
    resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_WONTFIX;
    this.validWontfix = new Resolution(resolutionKey, 4);
    resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_INVALID_WONTFIX;
    this.invalidWontfix = new Resolution(resolutionKey, 5);
    resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_UNSURE_VALIDITY;
    this.unsureValidity = new Resolution(resolutionKey, 6);
    this.unresolved = new Status(ResourceBundleKey.ITEM_KEY_STATUS_UNRESOLVED, 0);
    this.resolved = new Status(ResourceBundleKey.ITEM_KEY_STATUS_RESOLVED, 1);
  }
  
  /**
   * Checks if the instantiated references are not null.
   */
  public void testNullInstances() {
    assertNotNull("Checking the defect type instance.", this.defect);
    assertNotNull("Checking the defect type instance.", this.externalIssue);
    assertNotNull("Checking the defect type instance.", this.question);
    assertNotNull("Checking the defect type instance.", this.praise);
    assertNotNull("Checking the defect type instance.", this.critical);
    assertNotNull("Checking the defect type instance.", this.major);
    assertNotNull("Checking the defect type instance.", this.normal);
    assertNotNull("Checking the defect type instance.", this.minor);
    assertNotNull("Checking the defect type instance.", this.trivial);
    assertNotNull("Checking the defect type instance.", this.unset);
    assertNotNull("Checking the defect type instance.", this.validNeedsfixing);
    assertNotNull("Checking the defect type instance.", this.validFixlater);
    assertNotNull("Checking the defect type instance.", this.validDuplicate);
    assertNotNull("Checking the defect type instance.", this.validWontfix);
    assertNotNull("Checking the defect type instance.", this.unsureValidity);
    assertNotNull("Checking the defect type instance.", this.validWontfix);
    assertNotNull("Checking the defect type instance.", this.unresolved);
    assertNotNull("Checking the defect type instance.", this.resolved);
  }
}
