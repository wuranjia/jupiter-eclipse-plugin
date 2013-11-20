package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.ui.marker.ReviewMarker;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides review id selection wizard.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdSelectionWizard extends Wizard implements INewWizard {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  private static final String PAGE_REVIEW_ID_SELECTION = "ReviewIdSelection";
  private WizardNewFileCreationPage configFileCreationPage;
  /** the reviewer id to be kept for the next default reviewer. */
  public static final String REVIEWER_ID = "ReviewerId";
  
//  private IStructuredSelection selection;
  
  private IWorkbench workbench;
  private String reviewPhaseNameKey;
  private boolean isMultipleChoiceEnabled;
  private IProject defaultProject;
  
  /**
   * Instantiates review id selection wizard. After this wizard is finished,
   * 1) review ID (determined by defaultProject name and review id) will be set to content
   *  provider model,
   * 2) reviewer ID was set to preference store.
   * 
   * @param reviewPhaseNameKey the review phase name key.
   * @param defaultProject the defaultProject to be shown as a default in the dialog. Clients can
   * not pass defaultProject
   * as <code>null</code>. so that they should check the null before instantiating this.
   * @param isMultipleChoiceEnabled set <code>true</code> if the defaultProject choice is multiple.
   * <code>false</code> if the defaultProject choice is only the passing defaultProject.
   */
  public ReviewIdSelectionWizard(String reviewPhaseNameKey, IProject defaultProject,
                                 boolean isMultipleChoiceEnabled) {
    super();
    this.reviewPhaseNameKey = reviewPhaseNameKey;
    this.defaultProject = defaultProject;
    this.isMultipleChoiceEnabled = isMultipleChoiceEnabled;
  }

  /**
   * Performs finish. After this method is invoked.
   * 1) review ID (determined by defaultProject name and review id) will be set to content
   *  provider model,
   * 2) reviewer ID was set to preference store.
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  public boolean performFinish() {
    // clear all markers in the current project first.
    ReviewMarker.clearMarkersInReviewId();
    ReviewIdSelectionPage page = (ReviewIdSelectionPage) getPage(PAGE_REVIEW_ID_SELECTION);
    String projectName = page.getProjectName();
    String reviewIdName = page.getReviewIdName();
    String reviewerIdName = page.getReviewerIdName();
    IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
    // for the next default reviewer in the selection page.
    store.setValue(ReviewIdSelectionWizard.REVIEWER_ID, reviewerIdName);
    // new structure 7/23/04
    IProject project = FileResource.getProject(projectName);
    ReviewModel.getInstance().notifyListeners(project);
    PropertyResource reviewIdResource = PropertyResource.getInstance(project, false);
    ReviewId reviewId = reviewIdResource.getReviewId(reviewIdName);
    log.debug("directory: " + ((reviewId != null) ? reviewId.getDirectory() : "null"));
    ReviewModel.getInstance().notifyListeners(reviewId);
    ReviewerId reviewerId = new ReviewerId(reviewerIdName, "");
    ReviewModel.getInstance().notifyListeners(reviewerId);
    return true;
  }
  
  /**
   * Initializes the wizard.
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, 
   * org.eclipse.jface.viewers.IStructuredSelection)
   */
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.workbench = workbench;
//    this.selection = selection;
  }

  /**
   * Adds pages.
   * @see org.eclipse.jface.wizard.IWizard#addPages()
   */
  public void addPages() {
    WizardPage reviewIdSelectionPage = new ReviewIdSelectionPage(this.defaultProject,
                                                                 this.isMultipleChoiceEnabled,
                                                                 PAGE_REVIEW_ID_SELECTION);
    addPage(reviewIdSelectionPage);
  }
  
  /**
   * Checks if the wizard can finish.
   * @return <code>true</code> if it can finish.
   * @see org.eclipse.jface.wizard.IWizard#canFinish()
   */
  public boolean canFinish() {
    return true;
  }
}

