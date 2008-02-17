package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizardPage;
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
import org.eclipse.ui.IWorkbench;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;

/**
 * Provides ReviewIssue ID setting page.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdNewReviewIdPage extends WizardPage {
  private IWorkbench workbench;
  private Combo projectCombo;
  private Text reviewIdText;
  private Label infoLabel;
  private List<String> tempReviewIdList = new ArrayList<String>();
  private Text descriptionText;
  private IProject project;

  /**
   * Instantiates the config review id page.
   * @param workbench the workbench
   * @param pageName the page name.
   * @param project the project.
   * @param imageFilePath the image file path.
   */
  public ReviewIdNewReviewIdPage(IWorkbench workbench, String pageName, IProject project, 
                                     String imageFilePath) {
    super(pageName);
    setImageDescriptor(ReviewPlugin.createImageDescriptor(imageFilePath));
    setTitle(ReviewI18n.getString("ReviewIdNewReviewIdPage.label.title.before")
             + project.getName()
             + ReviewI18n.getString("ReviewIdNewReviewIdPage.label.title.after"));
    String message = ReviewI18n.getString("ReviewIdNewReviewIdPage.label.title.description");
    applyToStatusLine(new Status(IStatus.OK, "not_used", 0, message , null));
    this.workbench = workbench;
    this.project = project;
  }

  /**
   * Creates control.
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite ancestor) {
    Composite parent = createsGeneralComposite(ancestor);
    createLabelContent(parent);
    createReviewIdContent(parent);
    createDescriptionContent(parent);
    //  set the composite as the control for this page
    setControl(parent);
    addListeners();
    setPageComplete(isTextNonEmpty(reviewIdText) && isTextNonEmpty(descriptionText));
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
   * Creates label content.
   * @param parent the parent composite.
   */
  private void createLabelContent(Composite parent) {
    this.infoLabel = new Label(parent, SWT.NULL);
    infoLabel.setText(ReviewI18n.getString("ReviewIdNewReviewIdPage.label.info"));
  }

  /**
   * Creates review ID content.
   * 
   * @param parent the parent.
   */
  private void createReviewIdContent(Composite parent) {
    Label reviewIdLabel = new Label(parent, SWT.NONE);
    reviewIdLabel.setText(ReviewI18n.getString("ReviewIdNewReviewIdPage.label.reviewId"));
    this.reviewIdText = new Text(parent, SWT.SINGLE | SWT.BORDER);
    FormData reviewIdLabelData = new FormData();
    reviewIdLabelData.top = new FormAttachment(reviewIdText, 0, SWT.CENTER);
    reviewIdLabel.setLayoutData(reviewIdLabelData);
    FormData reviewIdTextData = new FormData();
    reviewIdTextData.top = new FormAttachment(infoLabel, 5);
    reviewIdTextData.left = new FormAttachment(reviewIdLabel, 40);
    reviewIdTextData.right = new FormAttachment(100, 0);
    reviewIdText.setLayoutData(reviewIdTextData);
  }
  
  /**
   * Creates description content.
   * @param parent the parent.
   */
  private void createDescriptionContent(Composite parent) {
    Label descriptionLabel = new Label(parent, SWT.NONE);
    descriptionLabel.setText(ReviewI18n.getString("ReviewIdNewReviewIdPage.label.description"));
    this.descriptionText = new Text(parent, SWT.SINGLE | SWT.BORDER);
    FormData descriptionLabelData = new FormData();
    descriptionLabelData.top = new FormAttachment(descriptionText, 0, SWT.CENTER);
    descriptionLabel.setLayoutData(descriptionLabelData);
    FormData descriptionTextData = new FormData();
    descriptionTextData.top = new FormAttachment(reviewIdText, 5);
    descriptionTextData.left = new FormAttachment(reviewIdText, 0, SWT.LEFT);
    descriptionTextData.right = new FormAttachment(100, 0);
    descriptionText.setLayoutData(descriptionTextData);
  }

  /**
   * Handles project selection 
   * @param e the event.
   */
  protected void handleProjectSelection(Event e) {
    String projectName = ((Combo) e.widget.getData()).getText();
    IProject project = FileResource.getProject(projectName);
    PropertyResource reviewIdResource = PropertyResource.getInstance(this.project, false);
    this.tempReviewIdList = Arrays.asList(reviewIdResource.getReviewIdNames());
  }
  
  /**
   * Adds listeners.
   */
  private void addListeners() {
    this.reviewIdText.addListener(SWT.KeyUp, new ListenerAdapter());
    this.descriptionText.addListener(SWT.KeyUp, new ListenerAdapter());
  }
  
  /**
   * Checks if the wizard can flip to the next page.
   * @return <code>true</code> if it can flip to the next page.
   */
  public boolean canFlipToNextPage() {
    return isPageComplete();
  }

  /**
   * Checks if the review id is valid (i.e. not duplicated).
   * @param reviewId the review id.
   * @return <code>true</code> if the review id is valid. <code>false</code> otherwise.
   */
  private boolean isValidReviewId(String reviewId) {
    if (this.tempReviewIdList.contains(reviewId)) {
      String message = ReviewI18n.getString("ReviewIdNewReviewIdPage.label.waring.idDuplicate");
      applyToStatusLine(new Status(IStatus.WARNING, "not_used", 0, message , null));
      return false;
    }
    else {
      String message = ReviewI18n.getString("ReviewIdNewReviewIdPage.label.title.description");
      applyToStatusLine(new Status(IStatus.OK, "not_used", 0, message , null));
      return true;
    }
  }

  /**
   * Checks if the text is non empty. Returns <code>true</code> if the text is not empty. Returns
   * <code>false</code> otherwise.
   * 
   * @param text the text to be check.
   * @return <code>true</code> if the text is not empty. Returns <code>false</code> otherwise.
   */
  private static boolean isTextNonEmpty(Text text) {
    String string = text.getText();
    if ((string != null) && (string.trim().length() > 0)) {
      return true;
    }
    return false;
  }

  /**
   * Returns the next page. Saves the values from this page in the model associated with the wizard.
   * Initializes the widgets on the next page.
   * @return the next page.
   */
  public IWizardPage getNextPage() {
    return ((ReviewIdNewWizard) getWizard()).getPage(ReviewIdNewWizard.PAGE_FILE);
  }
  
  /**
   * Gets the review id.
   * @return the review id.
   */
  public String getReviewId() {
    return this.reviewIdText.getText();
  }
  
  /**
   * Gets the description.
   * @return the description.
   */
  public String getDescription() {
    return this.descriptionText.getText();
  }
  
  /**
   * Applies the status to the status line of a dialog page.
   * @param status the status.
   */
  private void applyToStatusLine(IStatus status) {
    String message = status.getMessage();
    if (message.length() == 0) {
      message = null;
    }
    switch (status.getSeverity()) {
      case IStatus.OK:
        setErrorMessage(null);
        setMessage(message);
        break;
      case IStatus.WARNING:
        setErrorMessage(null);
        setMessage(message, WizardPage.WARNING);
        break;        
      case IStatus.INFO:
        setErrorMessage(null);
        setMessage(message, WizardPage.INFORMATION);
        break;      
      default:
        setErrorMessage(message);
        setMessage(null);
        break;    
    }
  }
  
  /**
   * Provides a concrete listener adapter class.
   * 
   * @author Takuya Yamashita
   * @version $Id$
   */
  private class ListenerAdapter implements Listener {

    /**
     * Handles event
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event) {
      setPageComplete(isTextNonEmpty(reviewIdText) && isTextNonEmpty(descriptionText));
      getWizard().getContainer().updateButtons();
    }
  }
}