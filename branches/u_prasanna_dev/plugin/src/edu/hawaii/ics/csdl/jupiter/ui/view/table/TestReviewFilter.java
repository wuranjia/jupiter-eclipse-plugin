package edu.hawaii.ics.csdl.jupiter.ui.view.table;

import java.util.Calendar;
import java.util.Date;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewTestCase;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;

/**
 * Provides the test cases for the <code>ReviewFilter</code> class.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class TestReviewFilter extends ReviewTestCase {
  /** The code review instance to be filtered. */
  private ReviewIssue codeReview;

  /**
   * Sets up the code review instance.
   *
   * @throws Exception if problems occur.
   */
  public void setUp() throws Exception {
    super.setUp();
    try {
      this.codeReview = new ReviewIssue(new Date(), new Date(), "Taro Yamada", "John Smith",
          "doc/review/bar/baz/Foo.java", "10", praise, normal, "This is summary.",
          "This is nice description.!? The second sentence continues.",
          "This is annotation for review.", "This is revision comment.", validFixlater, unresolved,
          null);
    }
    catch (ReviewException e) {
      fail("Should not throw CodeReviewException: " + e.getMessage());
    }
  }

  /**
   * Tests the <code>select(Viewer, Object, Object)</code> method, given the each one filter is
   * set.
   */
  public void testEachCodeReviewFilter() {
    ReviewFilter filter = new ReviewFilter();
    assertEquals("Checking the no filtering.", true, filter.select(null, null, this.codeReview));
    // Sets filter type.
    filter.setTypeFilter(praise);
    assertEquals("Checking the filter matching with type.", true,
      filter.select(null, null, this.codeReview));
    filter.setTypeFilter(defect);
    assertEquals("Checking the filter no matching with type.", false,
      filter.select(null, null, codeReview));
    // sets filter status.
    filter.clearAllFilters();
    filter.setStatusFilter(unresolved);
    assertEquals("Checking the filter matching with status.", true,
      filter.select(null, null, this.codeReview));
    filter.setStatusFilter(resolved);
    assertEquals("Checking the filter no matching with status.", false,
      filter.select(null, null, this.codeReview));
    // Sets filter resolution.
    filter.clearAllFilters();
    filter.setResolutionFilter(validFixlater);
    assertEquals("Checking the filter matching with resolution.", true,
      filter.select(null, null, this.codeReview));
    filter.setResolutionFilter(validNeedsfixing);
    assertEquals("Checking the filter no matching with resolution.", false,
      filter.select(null, null, this.codeReview));
    // Sets filter assigned to.
    filter.clearAllFilters();
    filter.setAssignedToFilter("John Smith");
    assertEquals("Checking the filter matching with assigned to.", true,
      filter.select(null, null, this.codeReview));
    filter.setAssignedToFilter("Joe Danoe");
    assertEquals("Checking the filter no matching with assigned to.", false,
      filter.select(null, null, this.codeReview));
    // Sets filter previous days.
    filter.clearAllFilters();
    filter.setIntervalFilter(0);
    assertEquals("Checking the filter matching with previous days.", true,
      filter.select(null, null, this.codeReview));
    filter.setIntervalFilter(3);
    assertEquals("Checking the filter matching with previous days.", true,
      filter.select(null, null, this.codeReview));
    Calendar fourDaysAgo = Calendar.getInstance();
    fourDaysAgo.add(Calendar.DATE, -4);
  }
}
