package edu.hawaii.ics.csdl.jupiter.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * Tests the <code>TestProjectMockup</code> class.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ProjectMockupTest extends TestCase {
  
  private ProjectMockup projectMockup;
  private static final String TEST_JUPITER_PROJECT = "testJupiterProject";
  private static final String OUTPUT_DIR = "bin";
  private static final String OUTPUT_LOCATION =  "/" + TEST_JUPITER_PROJECT + "/" + OUTPUT_DIR;
  private static final String SRC_DIR = "src";
  private static final String TEST_PACKAGE = "edu.hawaii.ics.csdl.jupiter.test";
  private static final String FILE_SEPARATED_TEST_PACKAGE = "csdl/jupiter/test";
  /**
   * Sets up the project mock up.
   * @throws CoreException if problems occur.
   */
  public void setUp() throws CoreException {
    this.projectMockup = new ProjectMockup(TEST_JUPITER_PROJECT, OUTPUT_DIR);
  }
  
  /**
   * Tears down the project mock up.
   * @throws CoreException if problems occur.
   */
  public void tearDown() throws CoreException {
    this.projectMockup.dispose();
  }
  
  /**
   * Tests the default project which does nothing after the <code>ProjectMockup</code>
   * instantiation.
   * @throws CoreException if problems occur.
   */
  public void testDefaultProject() throws CoreException {
    IProject project = this.projectMockup.getProject();
    assertEquals("Checking project name.", TEST_JUPITER_PROJECT, project.getName());
    IJavaProject javaProject = this.projectMockup.getJavaProject();
    assertEquals("Checking java project name.", TEST_JUPITER_PROJECT, javaProject.getElementName());
    IPath outputLocation = javaProject.getOutputLocation();
    assertEquals("Checking output location.", OUTPUT_LOCATION, outputLocation.toString());
    IClasspathEntry[] classpathEntries = javaProject.getRawClasspath();
    assertEquals("Checking classpath entry size.", 1, classpathEntries.length);    
    IClasspathEntry classpathEntry = classpathEntries[0];
    IClasspathEntry defaultJreContainerEntry = JavaRuntime.getDefaultJREContainerEntry();
    assertEquals("Checking classpath entry.", defaultJreContainerEntry, classpathEntry);
    IPath classpathLocation = classpathEntry.getOutputLocation();
    assertEquals("Checking classpath location.", null, classpathLocation);
    String natureId = project.getDescription().getNatureIds()[0];
    assertEquals("Checking nature id.", JavaCore.NATURE_ID, natureId);
  }
  
  /**
   * Tests the project.
   * @throws CoreException if problems occur.
   */
  public void testProject() throws CoreException {
    IPackageFragment packageFragment = this.projectMockup.createPackage(TEST_PACKAGE, SRC_DIR);
    String fullPath = "/" + TEST_JUPITER_PROJECT + "/" + SRC_DIR + "/" 
                      + FILE_SEPARATED_TEST_PACKAGE;
    IResource resource = packageFragment.getCorrespondingResource();
    assertTrue("Checking source directory resource type.", resource instanceof IFolder);
    assertEquals("Checking the full path.", fullPath, resource.getFullPath().toString());
  }
    

}
