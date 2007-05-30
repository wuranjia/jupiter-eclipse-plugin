package edu.hawaii.ics.csdl.jupiter.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.hawaii.ics.csdl.jupiter.ReviewTestCase;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;

/**
 * Tests the <code>ReviewComparator</code> insntace.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class TestCodeReviewComparator extends ReviewTestCase {
  /** The <code>List</code> interface to hold <code>ReviewIssue</code> instance. */
  List codeReviews = new ArrayList();

  /**
   * Sets up the initial condition. Instantiates three <code>ReviewIssue</code> instances, then adds
   * them to the List interface.
   * 
   * @throws Exception if problems occur.
   */
  protected void setUp() throws Exception {
    super.setUp();
    codeReviews.add(new ReviewIssue(new Date(), new Date(), "fooReviewer", "fooRespondent",
        "doc/review/foo/bar/Baz.java", "10", praise, major,
        "This is summary.", "This is nice method!", "This is annotation for review.",
        "This is revision comment.", validFixlater, unresolved, null));
    codeReviews.add(new ReviewIssue(new Date(), new Date(), "barReviewer", "barRespondent",
        "doc/review/bar/baz/Foo.java", "20", defect, minor,
        "This is summary.", "Should throw Exception.", "This is annotation for review.",
        "This is revision comment.", validNeedsfixing, resolved, null));
    codeReviews.add(new ReviewIssue(new Date(), new Date(), "bazReviewer", "bazRespondent",
        "doc/review/baz/foo/Bar.java", "30", question, trivial,
        "This is summary", "is this method needed?", "This is annotation for review.",
        "This is revision comment.", unset, unresolved, null));
  }

  /**
   * Tests <code>ReviewComparator.REVIEWER</code> instance.
   */
  public void testReviewNameComparator() {
    Collections.sort(codeReviews, ReviewComparator.REVIEWER);
    Iterator i = codeReviews.iterator();
    i.hasNext();
    assertEquals("Testing first element of the reviewer's name.", "barReviewer",
      ((ReviewIssue) i.next()).getReviewer());
    i.hasNext();
    assertEquals("Testing second element of the reviewer's name.", "bazReviewer",
      ((ReviewIssue) i.next()).getReviewer());
    i.hasNext();
    assertEquals("Testing third element of the reviewer's name.", "fooReviewer",
      ((ReviewIssue) i.next()).getReviewer());
  }

  /**
   * Tests <code>ReviewComparator.TYPE</code> instance.
   */
  public void testTypeComparator() {
    Collections.sort(codeReviews, ReviewComparator.TYPE);
    Iterator i = codeReviews.iterator();
    i.hasNext();
    assertEquals("Testing first element of the type.", defect,
      ((ReviewIssue) i.next()).getType());
    i.hasNext();
    assertEquals("Testing second element of the type.", question,
      ((ReviewIssue) i.next()).getType());
    i.hasNext();
    assertEquals("Testing third element of the type.", praise,
      ((ReviewIssue) i.next()).getType());
  }
  //
  //  /**
  //   * Tests <code>ReviewComparator.RESOLUTION</code> instance.
  //   */
  //  public void testDispositionComparator() {
  //    Collections.sort(codeReviews, ReviewComparator.RESOLUTION);
  //    assertEquals("Testing first element of the disposition.", Resolution.UNSET,
  //      ((ReviewIssue) codeReviews.get(0)).getDisposition());
  //    assertEquals("Testing second element of the disposition.", Resolution.VALID_NEEDS_FIXING,
  //      ((ReviewIssue) codeReviews.get(1)).getDisposition());
  //    assertEquals("Testing third element of the disposition.", Resolution.VALID_FIX_LATER,
  //      ((ReviewIssue) codeReviews.get(2)).getDisposition());
  //  }
  //
  //
  //  /**
  //   * Tests <code>ReviewComparator.STATUS</code> instance.
  //   */
  //  public void testStatusComparator() {
  //    Collections.sort(codeReviews, ReviewComparator.STATUS);
  //    assertEquals("Testing first element of the status.", Status.UNRESOLVED,
  //      ((ReviewIssue) codeReviews.get(0)).getStatus());
  //    assertEquals("Testing second element of the status.", Status.UNRESOLVED,
  //      ((ReviewIssue) codeReviews.get(1)).getStatus());
  //    assertEquals("Testing third element of the status.", Status.RESOLVED,
  //      ((ReviewIssue) codeReviews.get(2)).getStatus());
  //  }
  //
  //
  //  /**
  //   * Tests <code>ReviewComparator.FILE</code> instance.
  //   */
  //  public void testClassComparator() {
  //    Collections.sort(codeReviews, ReviewComparator.FILE);
  //    assertEquals("Testing first element of the class name.", "bar.baz.Foo",
  //      ((ReviewIssue) codeReviews.get(0)).getFullyQualifiedClassName());
  //    assertEquals("Testing second element of the class name.", "baz.foo.bar",
  //      ((ReviewIssue) codeReviews.get(1)).getFullyQualifiedClassName());
  //    assertEquals("Testing third element of the class name.", "foo.bar.Baz",
  //      ((ReviewIssue) codeReviews.get(2)).getFullyQualifiedClassName());
  //  }
}
