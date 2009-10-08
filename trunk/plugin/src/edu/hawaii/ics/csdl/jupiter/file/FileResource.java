package edu.hawaii.ics.csdl.jupiter.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides a way to create a review file.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class FileResource {
  /** Jupiter logger */
  private static JupiterLogger log = JupiterLogger.getLogger();

  /** The active resource. */
  private static IResource selectedResource;
  /** Used for the fully qualified class search. */
//  private static IJavaElement javaElement;
  /** The default text editor ID. */
  private static final String DEFAULT_TEXT_EDITOR_ID = "org.eclipse.ui.DefaultTextEditor";
  /** The C editor ID. */
  private static final String C_EDITOR_ID = "org.eclipse.cdt.ui.editor.CEditor";
  /** The assembly editor ID. */
  private static final String ASSEMBLY_EDITOR_ID = "org.eclipse.cdt.ui.editor.asm.AsmEditor";

  /**
   * Sets selected file and selected project from the file.
   * 
   * @param resource the active resource.
   */
  public static void setSelectedResource(IResource resource) {
    selectedResource = resource;
  }

  /**
   * Gets file instance from the given project and the relative path to the review file to be
   * created. Returns <code>null</code> if problems occur on the new file creation.
   * 
   * @param project the active project.
   * @param relativePath the relative path to the review file to be created.
   * @param isReviewFile sets <code>true</code> if the creating file is review file. sets
   *        <code>false</code> otherwise.
   * @return the file instance which is guaranteed to exist unless <code>null</code> is returned.
   * @throws CoreException thrown if problems occur.
   * @throws IOException thrown if problems occur.
   * @throws ReviewException thrown if problems occur.
   */
  public static IFile createIFile(IProject project, String relativePath, boolean isReviewFile)
      throws IOException, CoreException, ReviewException {
    if (project == null) {
      return null;
    }
    IFile iFile = project.getFile(relativePath);
    return createIFile(iFile, isReviewFile);
  }

  /**
   * Creates <code>IFile</code> instance from the <code>File</code>. Sets <code>true</code>
   * if the file is review file. Sets <code>false</code> otherwise.
   * 
   * @param iFile the iFile
   * @param isReviewFile Sets <code>true</code> if the file is review file. Sets
   *        <code>false</code> otherwise.
   * @return the iFile
   * @throws IOException if problems occur.
   * @throws CoreException if problems occur.
   * @throws ReviewException if problems occur.
   */
  public static IFile createIFile(IFile iFile, boolean isReviewFile) throws IOException,
      CoreException, ReviewException {
    File file = iFile.getLocation().toFile();
    if (iFile.exists()) {
      if (!file.exists()) {
        // Makes sure the parent directories of the review file are created.
        file.getParentFile().mkdirs();
        file.createNewFile();
        iFile.refreshLocal(IResource.DEPTH_ONE, null);
        iFile.setContents(new FileInputStream(file), true, false, null);
        // Writes root XML elements. Otherwise the org.jdom.input.JDOMParseException exception
        // will be thrown when the XML file is read.
        if (isReviewFile) {
          ReviewIssueXmlSerializer.writeEmptyCodeReview(file);
        }
      }
    }
    else {
      if (!file.exists()) {
        // makes sure the parent directories of the review file are created.
        file.getParentFile().mkdirs();
        file.createNewFile();
        iFile.refreshLocal(IResource.DEPTH_ONE, null);
        iFile.setContents(new FileInputStream(file), true, false, null);
        // Writes root XML elements. Otherwise the org.jdom.input.JDOMParseException exception
        // will be thrown when the XML file is read.
        if (isReviewFile) {
          ReviewIssueXmlSerializer.writeEmptyCodeReview(file);
        }
      }
      else {
        iFile.create(new FileInputStream(file), true, null);
      }
    }
    return iFile;
  }

  /**
   * Gets the relative file name to the review XML file. Returns null if the review id is
   * not contained in the list of config file. Throws the <code>IllegalArgumentException</code>
   * if at least either project, reviewId, or reviewerId, or both.
   * 
   * @param project the project.
   * @param reviewId the review id.
   * @param reviewerId the reviewer id.
   * @return the relative file name to the review XML file. Returns null if the review id is
   * not contained in the list of config file. 
   */
  public static String getPathToReviewFile(IProject project, ReviewId reviewId,
      ReviewerId reviewerId) {
    if (project == null || reviewId == null || reviewerId == null) {
      throw new IllegalArgumentException("argument(s) is null");
    }
    String relativeFolder = getPathToReviewFolder(project, reviewId);
    if (relativeFolder != null) {
      String fileName = reviewId.getReviewId() + "-" + reviewerId.getReviewerId();
      return relativeFolder + "/" + fileName + ".review";
    }
    return null;
  }

  /**
   * Gets the review <code>IFile</code> instance of the XML file. The project, review id, and
   * reviewer id determines an unique review file. Returns null if either project, reviewId, 
   * or reviewerId is null, or if the review id is not contained in the list of config file.
   * 
   * @param project the project.
   * @param reviewId the review id.
   * @param reviewerId the reviewer id.
   * @return the review <code>IFile</code> instance of the XML file. 
   * Returns null if either project, reviewId, or reviewerId is null, 
   * or if the review id is not contained in the list of config file.
   */
  public static IFile getReviewFile(IProject project, ReviewId reviewId, ReviewerId reviewerId) {
    if (project == null || reviewId == null || reviewerId == null) {
      return null;
    }
    String pathToReviewFile = getPathToReviewFile(project, reviewId, reviewerId);
    if (pathToReviewFile != null) {
      try {
        return FileResource.createIFile(project, pathToReviewFile, true);
      }
      catch (Exception e) {
        // ignore.
      }
    }
    return null;
  }

  /**
   * Gets the relative review folder. Returns null if the review id is not contained in the 
   * list of config file.
   * 
   * @param project the project.
   * @param reviewId the review id.
   * @return the relative review folder. Returns null if the review id is not contained in the 
   * list of config file.
   */
  public static String getPathToReviewFolder(IProject project, ReviewId reviewId) {
    if (project == null) {
      throw new IllegalArgumentException("project is null");
    }
    if (reviewId == null) {
      throw new IllegalArgumentException("review id is null");
    }
    PropertyResource propertyResource = PropertyResource.getInstance(project, false);
    List<ReviewId> reviewIdList = propertyResource.getReviewIdList();
    for (Iterator<ReviewId> i = reviewIdList.iterator(); i.hasNext();) {
      ReviewId searchingReviewId = (ReviewId) i.next();
      if (searchingReviewId.getReviewId().equals(reviewId.getReviewId())) {
        return searchingReviewId.getDirectory();
      }
    }
    return null;
  }

  /**
   * Gets the selected <code>IProject</code> instance regardless of an active file or not. In
   * other words, it is not affected by the active file. Returns null if there is no selected
   * project.
   * 
   * @return the selected <code>IProject</code> instance
   */
  public static IProject getSelectedProject() {
    return selectedResource.getProject();
  }

  /**
   * Gets the active <code>IProject</code> instance, whose file is active. The active project
   * means the project to which an active editor belongs. Returns <code>null</code> if an active
   * editor does not exist and any project is not opened yet, or <code>IFileEditorInput</code>
   * instance does not exist (meaning the case that welcome page is opened, for example).
   * 
   * @return the <code>IProject</code> instance. <code>null</code> if an active editor does not
   *         exist or <code>IFileEditorInput</code> instance does not exist (meaning the case that
   *         welcome page is opened, for example).
   */
  public static IProject getActiveProject() {
    if (selectedResource != null) {
      return selectedResource.getProject();
    }
    else {
      IFile activeFile = getActiveFile();
      return (activeFile != null) ? activeFile.getProject() : null;
    }
  }

  /**
   * Gets the <code>IProject</code> instance from the given <code>IResource</code> instance,
   * which belongs to the project.
   * 
   * @param iResource the the <code>IResource</code> instance.
   * @return the <code>IProject</code> instance. <code>null</code> if the passing parameter is
   *         null.
   */
  public static IProject getProject(IResource iResource) {
    if (iResource == null) {
      return null;
    }
    else {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      return root.getProject(iResource.getProject().getName());
    }
  }

  /**
   * Gets the <code>IProject</code> instance from the project name string.
   * 
   * @param projectName the project name string.
   * @return the <code>IProject</code> instance.
   */
  public static IProject getProject(String projectName) {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    return root.getProject(projectName);
  }

  /**
   * Gets the selected <code>IFile</code> instance. Returns <code>null</code> if selected file
   * does not exist (i.e. selection changed was not called before) nor an active editor does not
   * exist (any editor part is not opened), nor active editor exists, but
   * <code>IFileEditorInput</code> instance does not exist (i.e the case that welcome page is
   * opened, for example).
   * 
   * @return the <code>IFile</code> instance. Returns <code>null</code> if selected file does
   *         not exist (i.e. selection changed was not called before) nor an active editor does not
   *         exist (any editor part is not opened), nor active editor exists, but
   *         <code>IFileEditorInput</code> instance does not exist (i.e the case that welcome page
   *         is opened, for example).
   */
  public static IFile getSelectedIFile() {
    // if active editor is not opened, but users click the file in the package explore, for example.
    if (selectedResource != null && selectedResource instanceof IFile) {
      return (IFile) selectedResource;
    }
    else {
      IFile activeFile = getActiveFile();
      return (activeFile != null) ? activeFile : null;
    }
  }

  /**
   * Gets the active file which is active in the editor part.
   * 
   * @return the active <code>IFile</code> instance.
   */
  public static IFile getActiveFile() {
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
    if (activeWindow != null) {
      IWorkbenchPage activePage = activeWindow.getActivePage();
      if (activePage != null) {
        IEditorPart part = activePage.getActiveEditor();
        if (part != null) {
          IEditorInput editorInput = part.getEditorInput();
          if (editorInput instanceof IFileEditorInput) {
            try {
              return ((IFileEditorInput) editorInput).getFile();
            }
            // This happens when the welcome page is opened at the beginning.
            catch (ClassCastException e) {
              log.debug(e.getMessage());
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Gets the set of the <code>IFile</code> instances associated with the <code>ReviewId</code>
   * instance in a project.
   * 
   * @param project the project to search.
   * @param reviewId the review ID instance which is associated with the files.
   * @return the set of the review <code>IFile</code> instances associated with the review ID in
   *         the project.
   */
  public static IFile[] getReviewIFiles(IProject project, ReviewId reviewId) {
    IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
    String relativePath = reviewId.getDirectory();
    List<IFile> filesList = new ArrayList<IFile>();
    // Gathers all review File instances among all opened projects.
    File folder = project.getFile(relativePath).getLocation().toFile();
    if (folder.exists() && folder.isDirectory()) {
      File[] files = folder.listFiles(new FileFilter() {
        public boolean accept(File pathname) {
          if (pathname.getName().endsWith(".review")) {
            return true;
          }
          return false;
        }
      });
      for (int j = 0; j < files.length; j++) {
        try {
          if (ReviewIssueXmlSerializer.isReviewIdAssociatedFile(reviewId.getReviewId(), files[j])) {
            IFile iFile = project.getFile(relativePath + "/" + files[j].getName());
            filesList.add(iFile);
          }
        }
        catch (ReviewException e) {
          log.error(e);
        }
      }
    }
    return (IFile[]) filesList.toArray(new IFile[]{});
  }

  /**
   * Gets the String array of the opened project names in workspace.
   * 
   * @return the String array of the opened project names in workspace.
   */
  public static String[] getOpenedProjects() {
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    List<String> projectsList = new ArrayList<String>();
    for (int i = 0; i < projects.length; i++) {
      if (projects[i].isOpen()) {
        projectsList.add(projects[i].getName());
      }
    }
    return (String[]) projectsList.toArray(new String[]{});
  }

  /**
   * Gets the String array of the opened and review ID contained project names in workspace.
   * 
   * @return the String array of the opened and review ID contained project names in workspace.
   */
  public static String[] getOpenedAndReviewIdContainedProjects() {
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    Set<String> projectSet = new LinkedHashSet<String>();
    for (int i = 0; i < projects.length; i++) {
      if (projects[i].isOpen()) {
        PropertyResource propertyResource = PropertyResource.getInstance(projects[i], false);
        List<ReviewId> reviewIds = propertyResource.getReviewIdList();
        if (reviewIds.size() > 0) {
          projectSet.add(projects[i].getName());
        }
      }
    }
    return (String[]) projectSet.toArray(new String[]{});
  }
  
  /**
   * Links to the source code associated with the target file if any.
   * 
   * @param targetIFile the target review file to open.
   * @param lineNumber the line number of the target file.
   */
  public static void goToLine(IFile targetIFile, int lineNumber) {
    if (targetIFile == null || targetIFile.equals("")) {
      return;
    }
    lineNumber = (lineNumber >= 0) ?  --lineNumber : -1;
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
    
    //optimization: if the active editor has the same input as the selected target file then 
    //activate the editor
    IEditorPart editor = page.getActiveEditor();
    if (editor != null) {
    IEditorInput input = editor.getEditorInput();
      if (input instanceof IFileEditorInput) {
        IFile file = ((IFileEditorInput) input).getFile();
        if (targetIFile.equals(file)) {
          page.activate(editor);
        }
      }
    }

    try {
      // if jarIFile is java file,
      String editorId = IDE.getEditorDescriptor(targetIFile).getId();
      editor = IDE.openEditor(page, targetIFile, editorId);
      IEditorInput editorInput = editor.getEditorInput();
      if (editor instanceof ITextEditor) {
        ITextEditor textEditor = (ITextEditor) editor;
        IDocument document = textEditor.getDocumentProvider().getDocument(editorInput);
        IRegion lineInformation = document.getLineInformation(lineNumber);
        textEditor.selectAndReveal(lineInformation.getOffset(), 0);
      }
    }
    catch (Exception e) {
      log.debug(e.getMessage());
    }
//    ReviewMarker.updateMarkers(targetIFile);
  }

  /**
   * Removes all <code>IFile</code> instances.
   * 
   * @param iFiles the array of <code>IFile</code> instances.
   * @return <code>true</code> if all files are removed successfully. <code>false</code>
   *         otherwise.
   */
  public static boolean remove(IFile[] iFiles) {
    try {
      ReviewIssueXmlSerializer.remove(iFiles);
      return true;
    }
    catch (ReviewException e) {
      return false;
    }
  }
}