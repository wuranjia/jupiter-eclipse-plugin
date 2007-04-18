package csdl.jupiter.model.reviewissue;

import csdl.jupiter.ReviewTestCase;
import csdl.jupiter.util.ResourceBundleKey;

/**
 * Tests the Status class.
 *
 * @author Takuya Yamashita
 * @version $Id: TestStatus.java,v 1.1 2004/07/26 06:32:08 takuyay Exp $
 */
public class TestStatus extends ReviewTestCase {
  /**
   * Tests for toString() method. The return value should be equal to each string below.
   */
  public void testToString() {
    String key = ResourceBundleKey.ITEM_KEY_STATUS_UNRESOLVED;
    assertEquals("Testing the key of UNRESOVLED", key, unresolved.getKey());
    key = ResourceBundleKey.ITEM_KEY_STATUS_RESOLVED;
    assertEquals("Testing the key of RESOVLED", key, resolved.getKey());
  }

  /**
   * Tests for compareTo() method. Since unresolved &lt; resolved,
   * <code>unresolved.compareTo(resolved)</code> should return <code>-1</code>,
   * <code>resolved.compareTo(unresolved.compareTo)</code> should return <code>1</code>,
   * <code>resolved.compareTo(resolved)</code> should return <code>0</code>.
   * <code>unresolved.compareTo(unresolved)</code> should return <code>0</code>.
   */
  public void testCompareTo() {
    assertEquals("Testing compareTo with unresolved and resolved", -1,
      unresolved.compareTo(resolved));
    assertEquals("Testing compareTo with resolved and unresolved", 1,
      resolved.compareTo(unresolved));
    assertEquals("Testing compareTo with resolved and resolved", 0,
      resolved.compareTo(resolved));
    assertEquals("Testing compareTo with unresolved and unresolved", 0,
        unresolved.compareTo(unresolved));
  }
}
