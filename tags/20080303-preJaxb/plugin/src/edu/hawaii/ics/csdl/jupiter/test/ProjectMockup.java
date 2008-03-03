/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Erich Gamma (erich_gamma@ch.ibm.com) and
 *     Kent Beck (kent@threeriversinstitute.org)
 *******************************************************************************/
package edu.hawaii.ics.csdl.jupiter.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.jdt.launching.JavaRuntime;

import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
/**
 * Provides project mock up used in the plug-in junit test.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ProjectMockup {
  private IProject project;
  private IJavaProject javaProject;
  private IPackageFragmentRoot sourceFolder;
  
  /**
   * Instantiates the project mock up.
   * @param projectName the project name.
   * @param outputpath the output path.
   * @throws CoreException if problems occur.
   */
  public ProjectMockup(String projectName, String outputpath) throws CoreException {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    project = root.getProject(projectName);
    project.create(null);
    project.open(null);
    javaProject = JavaCore.create(project);
    IFolder binFolder = createClassOutputFolder(outputpath);
    setJavaNature();
    javaProject.setRawClasspath(new IClasspathEntry[0], null);
    createOutputFolder(binFolder);
    addSystemLibraries();
  }
  
  /**
   * Gets the project.
   * @return the project.
   */
  public IProject getProject() {
    return project;
  }
  
  /**
   * Gets the java project.
   * @return the java project.
   */
  public IJavaProject getJavaProject() {
    return javaProject;
  }
  
  /**
   * Adds a jar to the project.
   * @param plugin the plug-in name.
   * @param jar the jar file name.
   * @throws MalformedURLException  if no protocol is specified, or an unknown protocol is found.
   * @throws IOException if unable to resolve URL
   * @throws JavaModelException if this element does not exist or if an
   *    exception occurs while accessing its corresponding resource
   */
  public void addJar(String plugin, String jar) throws MalformedURLException, IOException, 
                     JavaModelException {
    Path result = findFileInPlugin(plugin, jar);
    IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
    IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
    System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
    newEntries[oldEntries.length] = JavaCore.newLibraryEntry(result, null, null);
    javaProject.setRawClasspath(newEntries, null);
  }
  
  /**
   * Creates the class package with the source path.
   * @param name the given dot-separated package name
   * @param sourcePath the source path. e.g. src.
   * @return the <code>IPackageFragment</code> which contains the package.
   * @exception CoreException if this method fails. Reasons include:
   * <ul>
   * <li> This resource already exists in the workspace.</li>
   * <li> The workspace contains a resource of a different type 
   *      at the same path as this resource.</li>
   * <li> The parent of this resource does not exist.</li>
   * <li> The parent of this resource is a project that is not open.</li>
   * <li> The parent contains a resource of a different type 
   *      at the same path as this resource.</li>
   * <li> The name of this resource is not valid (according to 
   *    <code>IWorkspace.validateName</code>).</li>
   * <li> The corresponding location in the local file system is occupied
   *    by a file (as opposed to a directory).</li>
   * <li> The corresponding location in the local file system is occupied
   *    by a folder and <code>force </code> is <code>false</code>.</li>
   * <li> Resource changes are disallowed during certain types of resource change 
   *       event notification.  See <code>IResourceChangeEvent</code> for more details.</li>
   * </ul>
   * or if this element does not exist or if an exception occurs while accessing
   * its corresponding resource.
   * or if the classpath could not be set. Reasons include:
   * <ul>
   * <li> This Java element does not exist (ELEMENT_DOES_NOT_EXIST)</li>
   * <li> The classpath is being modified during resource change event notification
   * (CORE_EXCEPTION)
   * <li> The classpath failed the validation check as defined by 
   * <code>JavaConventions#validateClasspath</code>
   * </ul>
   * or if the operation is canceled. 
   */
  public IPackageFragment createPackage(String name, String sourcePath) throws CoreException {
    if (sourceFolder == null) {
      sourceFolder = createSourceFolder(sourcePath);
    }
    return sourceFolder.createPackageFragment(name, false, null);
  }

  /**
   * Creates type in the compilation unit.
   * @param packageName the package name.
   * @param javaFileName the java file name.
   * @param source the source of the java file.
   * @return the type.
   * @throws JavaModelException if the element could not be created. Reasons include:
   * <ul>
   * <li> This Java element does not exist (ELEMENT_DOES_NOT_EXIST)</li>
   * <li> A <code>CoreException</code> occurred while creating an underlying resource
   * <li> The name is not a valid compilation unit name (INVALID_NAME)
   * <li> The contents are <code>null</code> (INVALID_CONTENTS)
   * </ul>
   * , if this element does not exist or if an exception occurs 
   * while accessing its corresponding resource
   */
  public IType createType(IPackageFragment packageName, String javaFileName, String source) 
    throws JavaModelException {
    StringBuffer buf = new StringBuffer();
    buf.append("package " + packageName.getElementName() + ";\n");
    buf.append("\n");
    buf.append(source);
    ICompilationUnit cu = packageName.createCompilationUnit(javaFileName, buf.toString(), false, 
                                                           null);
    return cu.getTypes()[0];
  }
  
  /**
   * Deletes all project.
   * 
   * @exception CoreException if this method fails. Reasons include:
   * <ul>
   * <li> This project could not be deleted.</li>
   * <li> This project's contents could not be deleted.</li>
   * <li> Resource changes are disallowed during certain types of resource change 
   *       event notification. See <code>IResourceChangeEvent</code> for more details.</li>
   * </ul>
   */
  public void dispose() throws CoreException {
    waitForIndexer();
    project.delete(true, true, null);
  }
  
  /**
   * creates the class output folder.
   * @param outputPath the class output path.
   * @return the class output folder.
   * @exception CoreException if this method fails. Reasons include:
   * <ul>
   * <li> This resource already exists in the workspace.</li>
   * <li> The workspace contains a resource of a different type 
   *      at the same path as this resource.</li>
   * <li> The parent of this resource does not exist.</li>
   * <li> The parent of this resource is a project that is not open.</li>
   * <li> The parent contains a resource of a different type 
   *      at the same path as this resource.</li>
   * <li> The name of this resource is not valid (according to 
   *    <code>IWorkspace.validateName</code>).</li>
   * <li> The corresponding location in the local file system is occupied
   *    by a file (as opposed to a directory).</li>
   * <li> The corresponding location in the local file system is occupied
   *    by a folder and <code>force </code> is <code>false</code>.</li>
   * <li> Resource changes are disallowed during certain types of resource change 
   *       event notification.  See <code>IResourceChangeEvent</code> for more details.</li>
   * </ul>
   * @see IFolder#create(int,boolean,IProgressMonitor)
   */
  private IFolder createClassOutputFolder(String outputPath) throws CoreException {
    IFolder binFolder = project.getFolder(outputPath);
    binFolder.create(false, true, null);
    return binFolder;
  }

  /**
   * Sets java nature.
   * @throws CoreException if this method fails. Reasons include:
   * <ul>
   * <li> This project does not exist.</li>
   * <li> This project is not open.</li>
   * </ul>
   */
  private void setJavaNature() throws CoreException {
    IProjectDescription description = project.getDescription();
    description.setNatureIds(new String[] {JavaCore.NATURE_ID});
    project.setDescription(description, null);
  }

  /**
   * Creates output folder with the <code>IFolder</code>.
   * @param binFolder the output <code>IFolder</code>.
   * @exception JavaModelException if the classpath could not be set. Reasons include:
   * <ul>
   *  <li> This Java element does not exist (ELEMENT_DOES_NOT_EXIST)</li>
   *  <li> The path refers to a location not contained in this project 
   *  <code>PATH_OUTSIDE_PROJECT</code>)
   *  <li> The path is not an absolute path (<code>RELATIVE_PATH</code>)
   *  <li> The path is nested inside a package fragment root of this project 
   *  (<code>INVALID_PATH</code>)
   *  <li> The output location is being modified during resource change event notification
   *   (CORE_EXCEPTION)  
   * </ul>
   */
  private void createOutputFolder(IFolder binFolder) throws JavaModelException {
    IPath outputLocation = binFolder.getFullPath();
    javaProject.setOutputLocation(outputLocation, null);
  }

  /**
   * Creates the source folder with the source path.
   * @param sourcePath the source path.
   * @return the <code>IPackageFragmentRoot</code> in the <code>IFolder</code>.
   * @exception CoreException if this method fails. Reasons include:
   * <ul>
   * <li> This resource already exists in the workspace.</li>
   * <li> The workspace contains a resource of a different type 
   *      at the same path as this resource.</li>
   * <li> The parent of this resource does not exist.</li>
   * <li> The parent of this resource is a project that is not open.</li>
   * <li> The parent contains a resource of a different type 
   *      at the same path as this resource.</li>
   * <li> The name of this resource is not valid (according to 
   *    <code>IWorkspace.validateName</code>).</li>
   * <li> The corresponding location in the local file system is occupied
   *    by a file (as opposed to a directory).</li>
   * <li> The corresponding location in the local file system is occupied
   *    by a folder and <code>force </code> is <code>false</code>.</li>
   * <li> Resource changes are disallowed during certain types of resource change 
   *       event notification.  See <code>IResourceChangeEvent</code> for more details.</li>
   * </ul>
   * or if this element does not exist or if an exception occurs while accessing
   * its corresponding resource.
   * or if the classpath could not be set. Reasons include:
   * <ul>
   * <li> This Java element does not exist (ELEMENT_DOES_NOT_EXIST)</li>
   * <li> The classpath is being modified during resource change event notification
   * (CORE_EXCEPTION)
   * <li> The classpath failed the validation check as defined by 
   * <code>JavaConventions#validateClasspath</code>
   * </ul>
   * or if the operation is canceled. 
   */
  private IPackageFragmentRoot createSourceFolder(String sourcePath) throws CoreException {
    IFolder folder = project.getFolder(sourcePath);
    folder.create(false, true, null);
    IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(folder);

    IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
    IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
    System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
    newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
    javaProject.setRawClasspath(newEntries, null);
    return root;
  }

  /**
   * Adds system libraries.
   *  @exception JavaModelException if the classpath could not be set. Reasons include:
   * <ul>
   * <li> This Java element does not exist (ELEMENT_DOES_NOT_EXIST)</li>
   * <li> The classpath is being modified during resource change event notification (CORE_EXCEPTION)
   * <li> The classpath failed the validation check as defined by 
   * <code>JavaConventions#validateClasspath</code>
   * </ul>
   * or if this element does not exist or if an exception occurs while accessing
   *  its corresponding resource
   */
  private void addSystemLibraries() throws JavaModelException {
    IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
    IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
    System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
    newEntries[oldEntries.length] = JavaRuntime.getDefaultJREContainerEntry();
    javaProject.setRawClasspath(newEntries, null);
  }
  
  /**
   * Finds the file path in the plug-in.
   * @param plugin the plug-in name.
   * @param file the file string in the plug-in
   * @return The file path in the plug-in.
   * @throws MalformedURLException  if no protocol is specified, or an unknown protocol is found.
   * @throws IOException if unable to resolve URL
   */
  private Path findFileInPlugin(String plugin, String file) throws MalformedURLException, 
                                                                   IOException {
    URL pluginURL = ReviewPlugin.getInstance().getBundle().getEntry("/");
    URL jarURL = new URL(pluginURL, file);
    URL localJarURL = FileLocator.toFileURL(jarURL);
    return new Path(localJarURL.getPath());
  }
  
  /**
   * Waits for the indexer.
   * @throws JavaModelException if the search failed. Reasons include:
   *  <ul>
   *    <li>the classpath is incorrectly set</li>
   *  </ul>
   */
  private void waitForIndexer() throws JavaModelException {
    new SearchEngine()
      .searchAllTypeNames(
        null,
        null,
        SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE,
        IJavaSearchConstants.CLASS,
        SearchEngine.createJavaSearchScope(new IJavaElement[0]),
        new TypeNameRequestor() {
      public void acceptClass(char[] packageName, char[] simpleTypeName, 
                              char[][] enclosingTypeNames, String path) {
      }
      public void acceptInterface(char[] packageName, char[] simpleTypeName, 
                                  char[][] enclosingTypeNames, String path) {
      }
    }, IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);
  }
  
  /**
   * Gets file instance from the given project and the relative path to the review file to be
   * created. Returns <code>null</code> if problems occur on the new file creation.
   *
   * @param relativePath the relative path to the review file to be created.
   *
   * @return the file instance which is guaranteed to exist unless <code>null</code> is returned.
   *
   * @throws CoreException thrown if problems occur.
   * @throws IOException thrown if problems occur.
   */
  public IFile createIFile(String relativePath) throws IOException, CoreException {
    if (project == null) {
      return null;
    }
    IFile file = project.getFile(relativePath);
    File reviewFile = file.getLocation().toFile();
    if (file.exists()) {
      if (!reviewFile.exists()) {
        // Makes sure the parent directories of the review file are created.
        reviewFile.getParentFile().mkdirs();
        reviewFile.createNewFile();
        file.refreshLocal(IResource.DEPTH_ONE, null);
        file.setContents(new FileInputStream(reviewFile), true, false, null);
      }
    }
    else {
      if (!reviewFile.exists()) {
        // makes sure the parent directories of the review file are created.
        reviewFile.getParentFile().mkdirs();
        reviewFile.createNewFile();
        file.refreshLocal(IResource.DEPTH_ONE, null);
        file.setContents(new FileInputStream(reviewFile), true, false, null);
      }
      else {
        file.create(new FileInputStream(reviewFile), true, null);
      }
    }
    return file;
  }
  
  /**
   * Copy the source relative path file in this project to the destination path file in the
   * test project.
   * @param sourceRelativePathFile the source relative path file in this project
   * @param destinationRelativePathFile the destination path file in the test project.
   * @return <code>true</code> if the copy is success. <code>false</code> otherwise.
   */
  public boolean copy(String sourceRelativePathFile, String destinationRelativePathFile) {
    URL pluginUrl = ReviewPlugin.getInstance().getBundle().getEntry("/");
    try {
      IFile distinationIFile = this.createIFile(destinationRelativePathFile);
      URL xmlUrl = FileLocator.toFileURL(new URL(pluginUrl, sourceRelativePathFile));
      File sourceXmlFile = new File(xmlUrl.getFile());
      copy(sourceXmlFile, distinationIFile.getLocation().toFile());
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }
  
  /**
   * Copy the source file to the destination file.
   * @param srouceFile the source <code>File</code>.
   * @param destinationFile the destination <code>File</code>.
   * @throws IOException if problems occur.
   */
  private void copy(File srouceFile, File destinationFile) throws IOException {
    FileChannel sourceChannel = new FileInputStream(srouceFile).getChannel();
    FileChannel destinationChannel = new FileOutputStream(destinationFile).getChannel();
    destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
    sourceChannel.close();
    destinationChannel.close();
  }
  
}