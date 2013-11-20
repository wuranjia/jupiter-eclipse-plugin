package edu.hawaii.ics.csdl.jupiter.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchWindow;

import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;


/**
 * Provides review conversation.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewConversation {
  protected ProjectMockup fooProjectMockup;
  
  protected ProjectMockup barProjectMockup;
  
  protected ProjectMockup bazProjectMockup;
  
  protected static final String REVIEW_DIR = "review";
  protected static final String FOO_REVIEW = "foo.review";
  protected static final String BAR_REVIEW = "bar.review";
  protected static final String BAZ_REVIEW = "baz.review";
  protected static final String FOO_REVIEW_PATH = REVIEW_DIR + "/" +  FOO_REVIEW;
  protected static final String BAR_REVIEW_PATH = REVIEW_DIR + "/" +  BAR_REVIEW;
  protected static final String BAZ_REVIEW_PATH = REVIEW_DIR + "/" +  BAZ_REVIEW;
  protected static final String FOO_PROJECT = "fooProject";
  protected static final String BAR_PROJECT = "barProject";  
  protected static final String BAZ_PROJECT = "bazProject"; 
  

  /**
   * Sets up 
   * @throws CoreException if problems occur.
   */
  public ReviewConversation() throws CoreException {
    ReviewPlugin plugin = ReviewPlugin.getInstance();
    IWorkbenchWindow[] windows = plugin.getWorkbench().getWorkbenchWindows();
    this.fooProjectMockup = new ProjectMockup(FOO_PROJECT, "bin");
    this.barProjectMockup = new ProjectMockup(BAR_PROJECT, "bin");
    this.bazProjectMockup = new ProjectMockup(BAZ_PROJECT, "bin");
    IProject fooProject = this.fooProjectMockup.getProject();
    IProject barProject = this.barProjectMockup.getProject();
    IProject bazProject = this.bazProjectMockup.getProject();
    this.fooProjectMockup.copy(FOO_REVIEW, FOO_REVIEW_PATH);
    this.barProjectMockup.copy(BAR_REVIEW, BAR_REVIEW_PATH);
    IFile fooReviewFile = fooProject.getFile(FOO_REVIEW_PATH);
    IFile barReviewFile = barProject.getFile(BAR_REVIEW_PATH);
  }
}
