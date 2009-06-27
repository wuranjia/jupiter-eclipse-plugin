package edu.hawaii.ics.csdl.jupiter;

import junit.framework.TestCase;

/**
 * Provides the test cases for the <code>CodeReviewException</code> class.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class TestCodeReviewException extends TestCase {
  /**
   * Tests the <code>CodeReviewException</code> constructor.
   */
  public void testCodeReviewException() {
    ReviewException codeReviewException = new ReviewException("one parameter");
    assertEquals("Testing one parameter constructor: message", "one parameter",
                                                      codeReviewException.getMessage());
    codeReviewException = new ReviewException("two parameter", new Exception("cause instance"));
    assertEquals("Testing two parameter constructor: message", "two parameter", 
                                                      codeReviewException.getMessage());
    assertEquals("Testing two parameter constructor: cause message", "cause instance", 
        codeReviewException.getCause().getMessage());
  }
  
}
