package csdl.jupiter;

import junit.framework.TestCase;

/**
 * Provides the test cases for the <code>CodeReviewException</code> class.
 * @author Takuya Yamashita
 * @version $Id: TestCodeReviewException.java,v 1.2 2004/07/26 06:32:02 takuyay Exp $
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
