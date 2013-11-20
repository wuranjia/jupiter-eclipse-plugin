package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import edu.hawaii.ics.csdl.jupiter.ReviewTestCase;
import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;

/**
 * Tests for Resolution class.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class TestResolution extends ReviewTestCase {
  /**
   * Test getKey() method. The return value should be equal to each string below.
   */
  public void testGetKey() {
    String key = ResourceBundleKey.ITEM_KEY_UNSET;
    assertEquals("Testing key of UNSET", key, unset.getKey());
    key = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_NEEDSFIXING;
    assertEquals("Testing key of VALID_NEEDS_FIXING", key, validNeedsfixing.getKey());
    key = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_FIXLATER;
    assertEquals("Testing key of VALID_FIX_LATER", key, validFixlater.getKey());
    key = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_DUPLICATE;
    assertEquals("Testing key of VALID_DUPLICATE", key, validDuplicate.getKey());
    key = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_WONTFIX;
    assertEquals("Testing key of VALID_WONT_FIX", key, validWontfix.getKey());
    key = ResourceBundleKey.ITEM_KEY_RESOLUTION_INVALID_WONTFIX;
    assertEquals("Testing key of InVALID_WONTFIX", key, invalidWontfix.getKey());
    key = ResourceBundleKey.ITEM_KEY_RESOLUTION_UNSURE_VALIDITY;
    assertEquals("Testing key of UNSURE_VALIDITY", key, unsureValidity.getKey());
  }

  /**
   * Since the order of dispositions is UNSET &lt; VALID_NEEDS_FIXING &lt; VALID_WONT_FIX &lt;
   * VALID_DUPLICATE &lt; VALID_FIX_LATER,
   * <code>Resolution.VALID_NEEDS_FIXING.compareTo(Resolution.VALID_WONT_FIX))</code> should
   * return <code>-1</code>, <code>Resolution.DEFERcompareTo(Resolution.VALID_DUPLICATE))</code>
   * should return <code>-1</code>,
   * <code>Resolution.VALID_DUPLICATE.compareTo(Resolution.VALID_FIX_LATER))</code> should return
   * <code>-1</code>.
   */
  public void testCompareTo() {
    assertEquals("Testing compareTo with UNSET and VALID_NEEDS_FIXING", -1,
      unset.compareTo(validNeedsfixing));
    assertEquals("Testing compareTo with VALID_NEEDS_FIXING and VALID_FIX_LATER", -1,
      validNeedsfixing.compareTo(validFixlater));
    assertEquals("Testing compareTo with VALID_FIX_LATER and VALID_DUPLICATE", -1, 
        validFixlater.compareTo(validDuplicate));
    assertEquals("Testing compareTo with VALID_DUPLICATE and VALID_WONTFIX", -1,
      validDuplicate.compareTo(validWontfix));
    assertEquals("Testing compareTo with VALID_WONTFIX and INVALID_WONTFIX", -1,
        validWontfix.compareTo(invalidWontfix));
    assertEquals("Testing compareTo with INVALID_WONTFIX and UNSURE_VALIDITY", -1,
        invalidWontfix.compareTo(unsureValidity));
  }
}
