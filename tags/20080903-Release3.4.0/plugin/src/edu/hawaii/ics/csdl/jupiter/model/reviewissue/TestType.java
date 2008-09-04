package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import edu.hawaii.ics.csdl.jupiter.ReviewTestCase;
import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;

/**
 * Test for the Type class.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class TestType extends ReviewTestCase {
  /**
   * Tests getKey() method. The return value should be equal to each string below.
   */
  public void testGetkey() {
    String key = ResourceBundleKey.ITEM_KEY_TYPE_DEFECT;
    assertEquals("Testing the key of DEFECT", key, defect.getKey());
    key = ResourceBundleKey.ITEM_KEY_TYPE_QUESTION;
    assertEquals("Testing the key of QUESTION", key, question.getKey());
    key = ResourceBundleKey.ITEM_KEY_TYPE_EXTERNAL_ISSUE;
    assertEquals("Testing the key of EXTERNAL_ISSUE", key, externalIssue.getKey());
    key = ResourceBundleKey.ITEM_KEY_TYPE_PRAISE;
    assertEquals("Testing the key of PRAISE", key, praise.getKey());
  }

  /**
   * Tests compareTo method. Since the order of dispositions is defect &lt; externalIssue &lt;
   * question &lt; praise, <code>defect.compareTo(externalIssue))</code> should return
   * <code>-1</code>, <code>externalIssue.compareTo(question))</code> should return
   * <code>-1</code>, <code>question.compareTo(praise))</code> should return
   * <code>-1</code>.
   */
  public void testCompareTo() {
    assertEquals("Testing compareTo with defect and externalIssue", -1,
      defect.compareTo(externalIssue));
    assertEquals("Testing compareTo with externalIssue and question", -1,
        externalIssue.compareTo(question));
    assertEquals("Testing compareTo with question and praise", -1,
        question.compareTo(praise));
  }
}
