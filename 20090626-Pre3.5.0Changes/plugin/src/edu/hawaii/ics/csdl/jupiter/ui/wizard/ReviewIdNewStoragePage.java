package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;

/**
 * Provides storage page.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdNewStoragePage extends WizardPage {
  private Text storageText;
  private IProject project;
  private int clientWidth;
  private static final double RATIO = 0.6;
  /**
   * Instantiates the review file configuration page.
   * @param project the project.
   * @param pageName the page name
   * @param imageFilePath the image file path.
   */
  protected ReviewIdNewStoragePage(IProject project, String pageName, String imageFilePath) {
    super(pageName);
    setImageDescriptor(ReviewPlugin.createImageDescriptor(imageFilePath));
    setTitle(ReviewI18n.getString("ReviewIdNewStoragePage.label.title"));
    setDescription(ReviewI18n.getString("ReviewIdNewStoragePage.label.title.description"));
    this.project = project;
  }

  /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite ancestor) {
    this.clientWidth = ancestor.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
    Composite parent = createsGeneralComposite(ancestor);
    createStorage(parent);
    setControl(parent);
  }
  
  /**
   * Creates view preference frame and return the child composite.
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
   * Creates the storage content.
   * @param composite the composite.
   * @return the control.
   */
  private Control createStorage(Composite composite) {
    Label storageLabel = new Label(composite, SWT.NONE);
    storageLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.storage"));
    this.storageText = new Text(composite, SWT.BORDER);
    IProject project = FileResource.getProject(this.project);
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    String defaultReviewId = PropertyConstraints.DEFAULT_REVIEW_ID;
    ReviewResource reviewResource = propertyResource.getReviewResource(defaultReviewId, true);
    if (reviewResource != null) {
      storageText.setText(reviewResource.getReviewId().getDirectory());
    }
    
    FormData storageLabelData = new FormData();
    storageLabelData.width = (int) (clientWidth * RATIO);
    storageLabelData.top = new FormAttachment(storageText, 0, SWT.CENTER);
    storageLabel.setLayoutData(storageLabelData);
    FormData storageTextData = new FormData();
    storageTextData.left = new FormAttachment(storageLabel, 0);
    storageTextData.right = new FormAttachment(100, 0);
    storageText.setLayoutData(storageTextData);
    return composite;
  }
  
  /**
   * Gets the directory.
   * @return the directory.
   */
  public String getDirectory() {
    return this.storageText.getText();
  }

}
