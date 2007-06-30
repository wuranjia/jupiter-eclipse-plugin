package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;

/**
 * Provides review ID selection page.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdSelectionPage extends WizardPage {
  private Combo reviewIdCombo;
  private Combo projectCombo;
  private Combo reviewerIdCombo;
  private String reviewId;
  private Text reviewIdDescriptionText;
  private IProject selectedProject;
  private boolean isMultipleChoiceEnabled;
  /**
   * Instantiates review id selection page.
   * @param defaultProject the selectedProject to be shown as a default in the dialog.
   * @param isMultipleChoiceEnabled set <code>true</code> if the selectedProject choice
   * is multiple.
   * <code>false</code> if the selectedProject choice is only the passing selectedProject.
   * @param pageName the page name.
   */
  public ReviewIdSelectionPage(IProject defaultProject, boolean isMultipleChoiceEnabled,
                               String pageName) {
    super(pageName);
    String imageFilePath = "icons/selection_wiz.gif";
    setImageDescriptor(ReviewPlugin.createImageDescriptor(imageFilePath));
    setTitle(ReviewI18n.getString("ReviewIdSelectionPage.label.title"));
    setDescription(ReviewI18n.getString("ReviewIdSelectionPage.label.title.description"));
    this.selectedProject = defaultProject;
    this.isMultipleChoiceEnabled = isMultipleChoiceEnabled;
  }

  /**
   * Creates control.
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite ancestor) {
    Composite parent = createsGeneralComposite(ancestor);
    createProjectSelectionContent(parent);
    createReviewIdSelectionContent(parent);
    craeteReviewIdDescriptionContent(parent);
    createReviewerIdSelectionContent(parent);
    //  set the composite as the control for this page
    setControl(parent);
  }

  /**
   * Creates view preference frame and return the child composite.
   * 
   * @param parent the parent composite.
   * @return the child composite.
   */
  private Composite createsGeneralComposite(Composite parent) {
    Composite child = new Composite(parent, SWT.LEFT);
    FormLayout layout = new FormLayout();
    layout.marginWidth = 7;
    layout.marginHeight = 7;
    child.setLayout(layout);
    return child;
  }
    
  /**
   * Handles the selectedProject selection event.
   * @param e the event.
   */
  private void handleProjectSelection(Event e) {
    String projectName = ((Combo) e.widget.getData()).getText();
    this.selectedProject = FileResource.getProject(projectName);
    PropertyResource reviewIdResource = PropertyResource.getInstance(this.selectedProject, false);
    this.reviewIdCombo.setItems(reviewIdResource.getReviewIdNames());
    reviewIdCombo.select(0);
    this.reviewId = reviewIdCombo.getItem(0);
    ReviewId reviewId = reviewIdResource.getReviewId(this.reviewId);
    this.reviewIdDescriptionText.setText(reviewId.getDescription());
    String[] items = reviewIdResource.getReviewerIdNames(this.reviewId);
    this.reviewerIdCombo.setItems(items);
    IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
    String reviewerId = store.getString(ReviewIdSelectionWizard.REVIEWER_ID);
    int index = reviewerIdCombo.indexOf(reviewerId);
    reviewerIdCombo.select((index != -1) ? index : 0);
  }
  
  /**
   * Handles the review id selection event.
   * @param e the event.
   */
  private void handleReviewIdSelection(Event e) {
    this.reviewId = ((Combo) e.widget.getData()).getText();
    PropertyResource reviewIdResource = PropertyResource.getInstance(this.selectedProject, false);
    this.reviewerIdCombo.setItems(reviewIdResource.getReviewerIdNames(reviewId));
    IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
    String reviewerId = store.getString(ReviewIdSelectionWizard.REVIEWER_ID);
    int index = reviewerIdCombo.indexOf(reviewerId);
    reviewerIdCombo.select((index != -1) ? index : 0);
    ReviewId reviewId = reviewIdResource.getReviewId(this.reviewId);
    this.reviewIdDescriptionText.setText(reviewId.getDescription());
  }
  
  
  
  /**
   * Creates selectedProject selection content.
   * @param parent the parent.
   */
  private void createProjectSelectionContent(Composite parent) {
    Label projectLabel = new Label(parent, SWT.NONE);
    projectLabel.setText(ReviewI18n.getString("ReviewIdSelectionPage.label.project"));
    this.projectCombo = new Combo(parent, SWT.READ_ONLY);
    if (this.isMultipleChoiceEnabled) {
      if (this.selectedProject != null) {
        projectCombo.setItems(FileResource.getOpenedAndReviewIdContainedProjects());
        int index = projectCombo.indexOf(this.selectedProject.getName());
        projectCombo.select((index >= 0) ? index : 0);
      }
    }
    else {
      if (this.selectedProject != null) {
        projectCombo.setItems(new String[] {this.selectedProject.getName()});
        projectCombo.select(0);
      }
    }
    projectCombo.setData(projectCombo);
    projectCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        handleProjectSelection(e);
      }
    });

    FormData projectLabelData = new FormData();
    projectLabelData.top = new FormAttachment(projectCombo, 0, SWT.CENTER);
    projectLabel.setLayoutData(projectLabelData);
    FormData projectComboData = new FormData();
    projectComboData.left = new FormAttachment(projectLabel, 40);
    projectComboData.right = new FormAttachment(100, 0);
    projectCombo.setLayoutData(projectComboData);
  }

  /**
   * Creates review id selection content.
   * 
   * @param parent the parent.
   */
  private void createReviewIdSelectionContent(Composite parent) {
    Label reviewIdLabel = new Label(parent, SWT.NONE);
    reviewIdLabel.setText(ReviewI18n.getString("ReviewIdSelectionPage.label.reviewId"));
    this.reviewIdCombo = new Combo(parent, SWT.READ_ONLY);
    PropertyResource reviewIdResource = PropertyResource.getInstance(this.selectedProject, false);
    reviewIdCombo.setItems(reviewIdResource.getReviewIdNames());
    if (reviewIdCombo.getItemCount() > 0) {
      reviewIdCombo.select(0);
      this.reviewId = reviewIdCombo.getItem(0);
    }
    reviewIdCombo.setData(reviewIdCombo);
    reviewIdCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        handleReviewIdSelection(e);
      }
    });

    FormData reviewIdLabelData = new FormData();
    reviewIdLabelData.top = new FormAttachment(reviewIdCombo, 0, SWT.CENTER);
    reviewIdLabel.setLayoutData(reviewIdLabelData);
    FormData reviewIdComboData = new FormData();
    reviewIdComboData.top = new FormAttachment(projectCombo, 5);
    reviewIdComboData.left = new FormAttachment(projectCombo, 0, SWT.LEFT);
    reviewIdComboData.right = new FormAttachment(100, 0);
    reviewIdCombo.setLayoutData(reviewIdComboData);
  }
  
  /**
   * Creates review id description content.
   * @param parent the parent.
   */
  private void craeteReviewIdDescriptionContent(Composite parent) {
    Label reviewIdDescriptionLabel = new Label(parent, SWT.NONE);
    String reviewIdDescriptionKey = "ReviewIdSelectionPage.label.reviewIdDescription";
    reviewIdDescriptionLabel.setText(ReviewI18n.getString(reviewIdDescriptionKey));
    this.reviewIdDescriptionText = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
    PropertyResource reviewIdResource = PropertyResource.getInstance(this.selectedProject, false);
    ReviewId reviewId = reviewIdResource.getReviewId(this.reviewId);
    String reviewIdDescription = "";
    if (reviewId != null) {
      reviewIdDescription = reviewId.getDescription();
    }
    reviewIdDescriptionText.setText(reviewIdDescription);

    FormData reviewIdDescriptionLabelData = new FormData();
    reviewIdDescriptionLabelData.top = new FormAttachment(reviewIdDescriptionText, 0, SWT.CENTER);
    reviewIdDescriptionLabel.setLayoutData(reviewIdDescriptionLabelData);
    FormData reviewIdDescriptionTextData = new FormData();
    reviewIdDescriptionTextData.top = new FormAttachment(reviewIdCombo, 5);
    reviewIdDescriptionTextData.left = new FormAttachment(projectCombo, 0, SWT.LEFT);
    reviewIdDescriptionTextData.right = new FormAttachment(100, 0);
    reviewIdDescriptionText.setLayoutData(reviewIdDescriptionTextData);
  }
  
  /**
   * Creates reviewer id selection content.
   * @param parent the parent.
   */
  private void createReviewerIdSelectionContent(Composite parent) {
    Label reviewerIdLabel = new Label(parent, SWT.NONE);
    reviewerIdLabel.setText(ReviewI18n.getString("ReviewIdSelectionPage.label.reviewerId"));
    this.reviewerIdCombo = new Combo(parent, SWT.READ_ONLY);
    PropertyResource reviewIdResource = PropertyResource.getInstance(this.selectedProject, false);
    reviewerIdCombo.setItems(reviewIdResource.getReviewerIdNames(this.reviewId));
    IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
    String reviewerId = store.getString(ReviewIdSelectionWizard.REVIEWER_ID);
    int index = reviewerIdCombo.indexOf(reviewerId);
    reviewerIdCombo.select((index != -1) ? index : 0);

    FormData reviewerIdLabelData = new FormData();
    reviewerIdLabelData.top = new FormAttachment(reviewerIdCombo, 0, SWT.CENTER);
    reviewerIdLabel.setLayoutData(reviewerIdLabelData);
    FormData reviewerIdComboData = new FormData();
    reviewerIdComboData.top = new FormAttachment(reviewIdDescriptionText, 5);
    reviewerIdComboData.left = new FormAttachment(projectCombo, 0, SWT.LEFT);
    reviewerIdComboData.right = new FormAttachment(100, 0);
    reviewerIdCombo.setLayoutData(reviewerIdComboData);
  }
  
  /**
   * Gets the review id name.
   * @return the review id name.
   */
  public String getReviewIdName() {
    return this.reviewIdCombo.getText();
  }
  
  /**
   * Gets the project name.
   * @return the selectedProject name.
   */
  public String getProjectName() {
    return this.selectedProject.getName();
  }
  
  /**
   * Gets the reviewer id name.
   * @return the reviewer id name.
   */
  public String getReviewerIdName() {
    return this.reviewerIdCombo.getText();
  }
}
