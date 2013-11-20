package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import java.util.Map;

import org.eclipse.core.resources.IProject;
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

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides author page.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdNewAuthorPage extends WizardPage {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  private IProject project;
  private Combo authorCombo;
  private int clientWidth;
  private static final double RATIO = 0.6;
  /**
   * Instantiates the config reviewer page.
   * @param project the project.
   * @param pageName the page name
   * @param imageFilePath the image file path.
   */
  protected ReviewIdNewAuthorPage(IProject project, String pageName, String imageFilePath) {
    super(pageName);
    setImageDescriptor(ReviewPlugin.createImageDescriptor(imageFilePath));
    setTitle(ReviewI18n.getString("ReviewIdNewAuthorPage.label.title"));
    setDescription(ReviewI18n.getString("ReviewIdNewAuthorPage.label.title.description"));
    this.project = project;
  }
  
  /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite ancestor) {
    this.clientWidth = ancestor.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
    Composite parent = createsGeneralComposite(ancestor);
    createProjectSelectionContent(parent);
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
    clientWidth = clientWidth - (7 * 2);
    layout.marginHeight = 7;
    child.setLayout(layout);
    return child;
  }
  
  /**
   * Creates project selection content.
   * @param parent the parent.
   */
  private void createProjectSelectionContent(Composite parent) {
    Label authorLabel = new Label(parent, SWT.NONE);
    authorLabel.setText(ReviewI18n.getString("ReviewIdNewAuthorPage.label.author"));
    this.authorCombo = new Combo(parent, SWT.READ_ONLY);
    authorCombo.setData(authorCombo);
    PropertyResource propertyResource = PropertyResource.getInstance(this.project, true);
    String defaultReviewId = PropertyConstraints.DEFAULT_REVIEW_ID;
    ReviewId reviewId = propertyResource.getReviewId(defaultReviewId);
    Map<String, ReviewerId> reviewers = reviewId.getReviewers();
    String author = reviewId.getAuthor();
    authorCombo.setItems(reviewers.keySet().toArray(new String[] {}));
    authorCombo.setText(author);
    authorCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        handleAuthorSelection(event);
      }
    });

    FormData authorLabelData = new FormData();
    authorLabelData.width = (int) (clientWidth * RATIO);
    authorLabelData.top = new FormAttachment(authorCombo, 0, SWT.CENTER);
    authorLabel.setLayoutData(authorLabelData);
    FormData authorComboData = new FormData();
    authorComboData.left = new FormAttachment(authorLabel, 0);
    authorComboData.right = new FormAttachment(100, 0);
    authorCombo.setLayoutData(authorComboData);
  }
  
  /**
   * Returns the next page. Saves the values from this page in the model associated with the wizard.
   * Initializes the widgets on the next page.
   * @return the next page.
   */
  public IWizardPage getNextPage() {
    return ((ReviewIdNewWizard) getWizard()).getPage(ReviewIdNewWizard.PAGE_ITEM_ENTRIES);
  }
  
  /**
   * Handles author selection
   * @param event the event.
   */
  protected void handleAuthorSelection(Event event) {
//    ((ReviewIdNewWizard) getWizard()).setAuthor(this.authorCombo.getText());
    getWizard().getContainer().updateButtons();
  }

  /**
   * Sets author items.
   * @param items the array of the author combo items.
   */
  public void setAuthorItems(String[] items) {
    this.authorCombo.setItems(items);
  }
  
  /**
   * Sets the default author.
   * @param author the default author.
   */
  public void setDefaultAuthor(String author) {
    this.authorCombo.setText(author);
  }
  
  /**
   * Gets the author of the review id.
   * @return the author of the review id.
   */
  public String getAuthor() {
    return this.authorCombo.getText();
  }

}
