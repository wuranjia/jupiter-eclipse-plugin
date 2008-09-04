package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import java.util.Date;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewTestCase;


/**
 * Provides test cases for the <code>ReviewIssue</code> class.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class TestReviewIssue extends ReviewTestCase {
  /** The <code>ReviewIssue</code> instance to test. */
  private ReviewIssue codeReview;
  /**
   * The <code>ReviewIssue</code> clone instance to test. This instance is made with the
   * same parameter values as the <code>codeReview</code> reference.
   */
  private ReviewIssue codeReviewClone;
  /**
   * The <code>ReviewIssue</code> imitation instance to test. This instance is the almost same
   * as the <code>codeReview</code> reference, but a parameter value is slightly different.
   */
  private ReviewIssue codeReviewImitation;

  /**
   * Sets up the <code>ReviewIssue</code> instance.
   *
   * @throws Exception thrown if problems occur.
   */
  public void setUp() throws Exception {
    super.setUp();
    this.codeReview = new ReviewIssue(new Date(), new Date(), "reviewer", "assigned to",
        "doc/review/foo/bar/Baz.java", "10", praise, trivial, "This is summary.",
        "This is nice description.!? The second sentence continues.",
        "This is annotation for review.", "This is revision comment.", validFixlater, unresolved,
        null);
    this.codeReviewClone = new ReviewIssue(new Date(), new Date(), "reviewer", "assigned to",
        "doc/review/foo/bar/Baz.java", "10", praise, trivial, "This is summary.",
        "This is nice description.!? The second sentence continues.",
        "This is annotation for review.", "This is revision comment.", validFixlater, unresolved,
        null);
    
    this.codeReviewImitation = new ReviewIssue(new Date(), new Date(), "reviewer", "assigned to",
        "doc/review/foo/bar/Baz.java", "10", praise, trivial, "This is summary.",
        "This is slightly different description.!? The second sentence continues.",
        "This is annotation for review.", "This is revision comment.", validFixlater, unresolved,
        null);
  }
  
  /**
   * Tests the <code>equals(Object)</code> method.
   */
  public void testEquals() {
    assertEquals("Testing equals(): codeReview and codeReviewClone", true,
        this.codeReview.equals(this.codeReview));
    assertEquals("Testing equals(): codeReview and codeReviewClone", true,
                 this.codeReview.equals(this.codeReviewClone));
    assertEquals("Testing equals(): codeReview and codeReviewClone", false,
        this.codeReview.equals(this.codeReviewImitation));
  }
  
  /**
   * Tests the <code>setCodeReview(ReviewIssue)</code> method.
   * @throws ReviewException if problems occur.
   */
  public void testSetCodeReview() throws ReviewException {
    this.codeReviewImitation.setReviewIssue(codeReview);
    assertEquals("Testing equals(): codeReview and codeReviewClone", true,
        this.codeReview.equals(this.codeReviewImitation));
  }
}
