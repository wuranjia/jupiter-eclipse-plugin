package edu.hawaii.ics.csdl.jupiter.ui.action;


import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.event.ReviewEvent;
import edu.hawaii.ics.csdl.jupiter.event.ReviewIssueModelEvent;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnDataModel;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnDataModelManager;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Resolution;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ResolutionKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Severity;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.SeverityKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Status;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.StatusKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Type;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.TypeKeyManager;
import edu.hawaii.ics.csdl.jupiter.ui.view.editor.ReviewEditorView;
import edu.hawaii.ics.csdl.jupiter.ui.view.editor.ReviewEditorViewAction;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.FilterPhase;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableView;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;
import edu.hawaii.ics.csdl.jupiter.util.ReviewDialog;

/**
 * Provides an action triggered when Code Review pop up menu on the Compilation Unit is selected. Do
 * the action that the code review editor is opened to edit comments with class name, method name,
 * and line number if applicable.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewAdditionActionDelegate implements IEditorActionDelegate {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  /** The target file path from a project */
  private String targetFilePath = "";
  /** The line number to be recored in the opened editor. */
  private String lineNumber = "";
  /** The selectedText when code review action is invoked. */
  private String selectedText = "";
  private ITextSelection texSelection;

  /**
   * Runs logic implementation when the menu, whose this implementing target is defined in the
   * plugin.xml, is selected. See the plugin.xml file. Clients should not call this method.
   * 
   * @param action The action proxy that handles the presentation portion of the action
   * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
   */
  public void run(IAction action) { 
    boolean isDetermined = determineProjectReviewIdReviewerId();
    if (!isDetermined) {
      return;
    }
    IFile selectedFile = FileResource.getSelectedIFile();  
    this.targetFilePath = (selectedFile != null) 
                             ? selectedFile.getProjectRelativePath().toString() : "";
    
    // Creates ReviewIssue instance, and fill it into editor view.
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    ReviewerId reviewerId = reviewModel.getReviewerIdManager().getReviewerId();
    IFile iReviewFile = FileResource.getReviewFile(project, reviewId, reviewerId);
    if (iReviewFile == null) {
      String titleKey = "ReviewDialog.noReviewFileDetermined.simpleConfirm.messageDialog.title";
      String title = ReviewI18n.getString(titleKey);
      String messageKey = "ReviewDialog.noReviewFileDetermined.simpleConfirm.messageDialog.message";
      String message = ReviewI18n.getString(messageKey);
      ReviewDialog.openSimpleComfirmMessageDialog(title, message);
      log.debug(message);
      return;
    }
    // check file written permission
    if (iReviewFile.isReadOnly()) {
        String message = "Review file " + iReviewFile + " is readonly. Please make it writable to" +
                         " save your issues.";
        ReviewDialog.openSimpleComfirmMessageDialog("Review Management", message);
    }
    
    try {
      ReviewEditorView editorView = ReviewEditorView.bringViewToTop();
      editorView.setEnable(true);
      String line = getLineNumber();
      if (!targetFilePath.equals("")) {
        ReviewIssue reviewIssue = new ReviewIssue(new Date(), new Date(), 
          reviewerId.getReviewerId(), "", targetFilePath, line, 
          (Type) TypeKeyManager.getInstance(project, reviewId).getItem(0),
          (Severity) SeverityKeyManager.getInstance(project, reviewId).getItem(0),
          "", this.selectedText, "", "",
          (Resolution) ResolutionKeyManager.getInstance(project, reviewId).getItem(0),
          (Status) StatusKeyManager.getInstance(project, reviewId).getItem(0),
          iReviewFile);
        reviewIssue.setLinkStatus(true);
        ReviewEditorViewAction.NEXT.setEnabled(false);
        ReviewEditorViewAction.PREVIOUS.setEnabled(false);
        editorView.setReviewIssue(reviewIssue);
      }
      else {
        editorView.setNewEmptyReviewIssue(iReviewFile);
      }
      editorView.setItemFields(project, reviewId);
      editorView.setFocus();
    }
    catch (ReviewException e) {
      e.printStackTrace();
     log.debug(e.getMessage());
    }
    int type = ReviewEvent.TYPE_COMMAND;
    int kind = ReviewEvent.KIND_ADD;
    ReviewPlugin.getInstance().notifyListeners(type, kind);
  }

  /**
   * Processes opening review table and review editor. If the necessary value such as project,
   * review id, reviewer id, phase name key are not set, prompt the proper dialog to let user
   * specify these value before the open.
   * 
   * @return <code>true</code> if the project, review id, and reviewer id, were determined.
   */
  public static boolean determineProjectReviewIdReviewerId() {
    boolean isReviewSelectionWizardInvoked = false;
    IProject project = FileResource.getActiveProject();
    // assertion project should not be null.
    if (project == null) {
      ReviewDialog.processNonProjectSelectionDialog();
      return false;
    }
        
    // assertion review id should not be null.
    ReviewModel reviewModel = ReviewModel.getInstance();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    String reviewPhaseNameKey = reviewModel.getPhaseManager().getPhaseNameKey();
    IProject reviewModelProject = reviewModel.getProjectManager().getProject();
    reviewModelProject = (reviewModelProject != null) ? reviewModelProject : project;
    if (reviewId == null 
        || (reviewId != null && !isActiveEditorInCurrentProject(reviewModelProject))) {
      int result = ReviewDialog.processReviewIdSelectionWizardDialog(reviewPhaseNameKey,
                                                                     project, false);
      if (result == WizardDialog.CANCEL) {
        return false;
      }
      isReviewSelectionWizardInvoked = true;
    }
    
    // update review Id.
    reviewModel = ReviewModel.getInstance();
    project = reviewModel.getProjectManager().getProject();
    reviewId = reviewModel.getReviewIdManager().getReviewId();
    ReviewerId reviewerId = reviewModel.getReviewerIdManager().getReviewerId();
    // assertion review file should not be null.
    IFile iReviewFile = FileResource.getReviewFile(project, reviewId, reviewerId);
    if (iReviewFile == null) {
      String titleKey = "ReviewDialog.noReviewFileDetermined.simpleConfirm.messageDialog.title";
      String title = ReviewI18n.getString(titleKey);
      String messageKey = "ReviewDialog.noReviewFileDetermined.simpleConfirm.messageDialog.message";
      String message = ReviewI18n.getString(messageKey);
      ReviewDialog.openSimpleComfirmMessageDialog(title, message);
      return false;
    }
    
    // fill review issues into model.
    ReviewIssueModelManager reviewIssueModelManager = ReviewIssueModelManager.getInstance();
    ReviewPlugin plugin = ReviewPlugin.getInstance();
    IPreferenceStore store = plugin.getPreferenceStore();
    if (isReviewSelectionWizardInvoked) {
      ReviewIssueModel reviewIssueModel = reviewIssueModelManager.getModel(project, reviewId);
      reviewIssueModel.notifyListeners(ReviewIssueModelEvent.MERGE);
      // fill the specified phase's column data into model
      ColumnDataModelManager columnDataModelManager = ColumnDataModelManager.getInstance();
      ColumnDataModel columnDataModel = columnDataModelManager.getModel(reviewPhaseNameKey);

      ReviewTableView view = ReviewTableView.getActiveView();
      // set column data model into view.
      // null happens when the view is not opened yet after Eclipse startup.
      if (view == null) {
        view = ReviewTableView.bringViewToTop();
      }
      view.createColumns(columnDataModel);
      PropertyResource propertyResource = PropertyResource.getInstance(project, true);
      String reviewIdString = reviewId.getReviewId();
      ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);
      if (reviewResource != null) {
        FilterPhase filterPhase = reviewResource.getFilterPhase(reviewPhaseNameKey);
        view.setFilterStatus(filterPhase.isEnabled());
      }
    }
    else {
      reviewIssueModelManager.getCurrentModel();
    }
    return true;
  }

  /**
   * Gets the line number of the active file if any.
   * 
   * @return the line number of the active file if any.
   */
  public String getLineNumber() {
    ReviewPlugin plugin = ReviewPlugin.getInstance();
    IWorkbenchWindow window = plugin.getWorkbench().getActiveWorkbenchWindow();
    if (window != null) {
      IWorkbenchPage page = window.getActivePage();
      IEditorPart activeEditorPart = page.getActiveEditor();
      if (activeEditorPart != null) {
        IEditorSite editorSite = page.getActiveEditor().getEditorSite();
        ISelectionProvider selectionProvider = editorSite.getSelectionProvider();
        if (selectionProvider != null) {
          ISelection selection = selectionProvider.getSelection();
          if (selection instanceof ITextSelection) {
            this.lineNumber = String.valueOf(((ITextSelection) selection).getStartLine() + 1);
          }
        }
      }
    }
    return this.lineNumber;
  }

  /**
   * Checks if the active editor belongs to the current project.
   * 
   * @param currentProject the <code>IProject</code>.
   * @return <code>true</code> if the active editor belongs to the current project.
   *         <code>false</code> otherwise.
   */
  private static boolean isActiveEditorInCurrentProject(IProject currentProject) {
    return (currentProject.getName().equals(FileResource.getActiveProject().getName()));
  }

  /**
   * Notifies this action delegate that the selection in the workbench has changed. Gets a fully
   * qualified class name, method name, line number if applicable. Clients should not call this
   * method.
   * 
   * @see org.eclipse.ui.IActionDelegate #selectionChanged(org.eclipse.jface.action.IAction,
   *      org.eclipse.jface.viewers.ISelection)
   */
  public void selectionChanged(IAction action, ISelection selection) {
    if (selection instanceof ITextSelection) {
      ITextSelection textSelection = (ITextSelection) selection;
      this.texSelection = textSelection;
      this.lineNumber = String.valueOf(textSelection.getStartLine() + 1);
      this.selectedText = textSelection.getText();
    }
    IFile selectedFile = FileResource.getSelectedIFile();
    this.targetFilePath = (selectedFile != null) 
                             ? selectedFile.getProjectRelativePath().toString() : "";
  }

  /**
   * Gets the <code>ICompilationUnit</code> instance. Returns <code>null</code> if corresponding
   * compilation unit does not exist.
   * 
   * @param file the <code>File</code> instance to convert to the compilation unit.
   * @return the <code>ICompilationUnit</code> instance.
   */
  private ICompilationUnit getCompilationUnit(IFile file) {
    if ((file != null) && file.getLocation().toString().endsWith(".java")) {
      return JavaCore.createCompilationUnitFrom(file);
    }
    else {
      return null;
    }
  }

  /**
   * Sets the active editor for the delegate.
   * 
   * @see org.eclipse.ui.IEditorActionDelegate #setActiveEditor(org.eclipse.jface.action.IAction,
   *      org.eclipse.ui.IEditorPart)
   */
  public void setActiveEditor(IAction action, IEditorPart targetEditor) {
//    log.debug("setActiveEditor was called.");
    // Do nothing so far.
  }
}