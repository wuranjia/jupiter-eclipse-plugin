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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.FieldItem;
import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ResolutionKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.SeverityKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.StatusKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.TypeKeyManager;

/**
 * Provides default items page
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdNewDefaultItemsPage extends WizardPage {
  private IProject project;
  private Combo typeCombo;
  private Combo severityCombo;
  private Combo resolutionCombo;
  private Combo statusCombo;
  private int clientWidth;
  private static final double RATIO = 0.6;
  /**
   * Instantiates the default items configuration page.
   * @param project the project.
   * @param pageName the page name.
   * @param imageFilePath the image file path.
   */
  protected ReviewIdNewDefaultItemsPage(IProject project, String pageName, String imageFilePath) {
    super(pageName);
    setImageDescriptor(ReviewPlugin.createImageDescriptor(imageFilePath));
    setTitle(ReviewI18n.getString("ReviewIdNewDefaultItemsPage.label.title"));
    setDescription(ReviewI18n.getString("ReviewIdNewDefaultItemsPage.label.title.description"));
    this.project = project;
  }

  /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite ancestor) {
    this.clientWidth = ancestor.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
    Composite parent = createsGeneralComposite(ancestor);
    createDefaultItems(parent);
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
   * Creates default items content.
   * @param composite the composite.
   * @return the control.
   */
  private Control createDefaultItems(Composite composite) {
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    String reviewIdString = PropertyConstraints.DEFAULT_REVIEW_ID;
    ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);
    ReviewId reviewId = null;
    if (reviewResource != null) {
      reviewId = reviewResource.getReviewId();
    }
    IWizardPage page = getWizard().getPage(ReviewIdNewWizard.PAGE_ITEM_ENTRIES);
    ReviewIdNewItemEntriesPage itemEntryPage = (ReviewIdNewItemEntriesPage) page;
    final Map fieldItemIdFieldItemMap = itemEntryPage.getFieldItemIdFieldItemMap();
    
    // create type label and its combo.
    Label typeLabel = new Label(composite, SWT.NONE);
    typeLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.type"));
    this.typeCombo = new Combo(composite, SWT.READ_ONLY);
    typeCombo.setData(typeCombo);
    TypeKeyManager manager = TypeKeyManager.getInstance(project, reviewId);
    String[] elements = (reviewId != null) ? manager.getElements() : new String[]{""};
    typeCombo.setItems(elements);
    String typeName = PropertyConstraints.ATTRIBUTE_VALUE_TYPE;
    String typeKey = (reviewResource != null) ? reviewResource.getDefaultField(typeName) : "";
    typeCombo.setText(ReviewI18n.getString(typeKey));
    typeCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String nameId = PropertyConstraints.ATTRIBUTE_VALUE_TYPE;
        FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(nameId);
        fieldItem.setDefaultKey(ReviewI18n.getKey(typeCombo.getText()));
      }
    });
    
    FormData typeLabelData = new FormData();
    typeLabelData.width = (int) (clientWidth * RATIO);
    typeLabelData.top = new FormAttachment(typeCombo, 0, SWT.CENTER);
    typeLabel.setLayoutData(typeLabelData);
    FormData typeComboData = new FormData();
    typeComboData.left = new FormAttachment(typeLabel, 0);
    typeComboData.right = new FormAttachment(100, 0);
    typeCombo.setLayoutData(typeComboData);
    
    // create severity label and its combo.
    Label severityLabel = new Label(composite, SWT.NONE);
    severityLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.severity"));
    this.severityCombo = new Combo(composite, SWT.READ_ONLY);
    severityCombo.setData(severityCombo);
    severityCombo.setItems(SeverityKeyManager.getInstance(project, reviewId).getElements());
    String severityName = PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY;
    String severityKey = (reviewResource != null) ? reviewResource.getDefaultField(severityName)
                                                  : "";
    severityCombo.setText(ReviewI18n.getString(severityKey));
    severityCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String nameId = PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY;
        FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(nameId);
        fieldItem.setDefaultKey(ReviewI18n.getKey(severityCombo.getText()));
      }
    });
    
    FormData severityLabelData = new FormData();
    severityLabelData.width = (int) (clientWidth * RATIO);
    severityLabelData.top = new FormAttachment(severityCombo, 0, SWT.CENTER);
    severityLabel.setLayoutData(severityLabelData);
    FormData severityComboData = new FormData();
    severityComboData.top = new FormAttachment(typeCombo, 5);
    severityComboData.left = new FormAttachment(severityLabel, 0);
    severityComboData.right = new FormAttachment(100, 0);
    severityCombo.setLayoutData(severityComboData);
    
    // create resolution label and its combo.
    Label resolutionLabel = new Label(composite, SWT.NONE);
    resolutionLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.resolution"));
    this.resolutionCombo = new Combo(composite, SWT.READ_ONLY);
    resolutionCombo.setData(resolutionCombo);
    resolutionCombo.setItems(ResolutionKeyManager.getInstance(project, reviewId).getElements());
    String resolutionName = PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION;
    String resolutionKey = (reviewResource != null) 
                            ? reviewResource.getDefaultField(resolutionName) : "";
    resolutionCombo.setText(ReviewI18n.getString(resolutionKey));
    resolutionCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String nameId = PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION;
        FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(nameId);
        fieldItem.setDefaultKey(ReviewI18n.getKey(resolutionCombo.getText()));
      }
    });
    
    FormData resolutionLabelData = new FormData();
    resolutionLabelData.width = (int) (clientWidth * RATIO);
    resolutionLabelData.top = new FormAttachment(resolutionCombo, 0, SWT.CENTER);
    resolutionLabel.setLayoutData(resolutionLabelData);
    FormData resolutionComboData = new FormData();
    resolutionComboData.top = new FormAttachment(severityCombo, 5);
    resolutionComboData.left = new FormAttachment(resolutionLabel, 0);
    resolutionComboData.right = new FormAttachment(100, 0);
    resolutionCombo.setLayoutData(resolutionComboData);
    
    // create status label and its combo.
    Label statusLabel = new Label(composite, SWT.NONE);
    statusLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.status"));
    this.statusCombo = new Combo(composite, SWT.READ_ONLY);
    statusCombo.setData(statusCombo);
    statusCombo.setItems(StatusKeyManager.getInstance(project, reviewId).getElements());
    String statusName = PropertyConstraints.ATTRIBUTE_VALUE_STATUS;
    String statusKey = (reviewResource != null) ? reviewResource.getDefaultField(statusName) : "";
    statusCombo.setText(ReviewI18n.getString(statusKey));
    statusCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String nameId = PropertyConstraints.ATTRIBUTE_VALUE_STATUS;
        FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(nameId);
        fieldItem.setDefaultKey(ReviewI18n.getKey(statusCombo.getText()));
      }
    });
    
    FormData statusLabelData = new FormData();
    statusLabelData.width = (int) (clientWidth * RATIO);
    statusLabelData.top = new FormAttachment(statusCombo, 0, SWT.CENTER);
    statusLabel.setLayoutData(statusLabelData);
    FormData statusComboData = new FormData();
    statusComboData.top = new FormAttachment(resolutionCombo, 5);
    statusComboData.left = new FormAttachment(statusLabel, 0);
    statusComboData.right = new FormAttachment(100, 0);
    statusCombo.setLayoutData(statusComboData);
    return composite;
  }
  
  /**
   * Returns the next page. Saves the values from this page in the model associated with the wizard.
   * Initializes the widgets on the next page.
   * @return the next page.
   */
  public IWizardPage getNextPage() {
    return ((ReviewIdNewWizard) getWizard()).getPage(ReviewIdNewWizard.PAGE_STORAGE);
  }
  
  /**
   * Gets the default type key.
   * @return the default type key.
   */
  public String getDefaultTypeKey() {
    return ReviewI18n.getKey(this.typeCombo.getText());
  }
  
  /**
   * sets the items type.
   * @param typeArray the array of the String type items.
   */
  public void setItemType(String[] typeArray) {
    String currentText = this.typeCombo.getText();
    this.typeCombo.setItems(typeArray);
    this.typeCombo.setText(currentText);
  }
  
  /**
   * Gets the default severity key.
   * @return the default severity key.
   */
  public String getDefaultSeverityKey() {
    return ReviewI18n.getKey(this.severityCombo.getText());
  }
  
  /**
   * sets the items severity.
   * @param severityArray the array of the String severity items.
   */
  public void setItemSeverity(String[] severityArray) {
    String currentText = this.severityCombo.getText();
    this.severityCombo.setItems(severityArray);
    this.severityCombo.setText(currentText);
  }
  
  /**
   * Gets the default resolution key.
   * @return the default resolution key.
   */
  public String getDefaultResolutionKey() {
    return ReviewI18n.getKey(this.resolutionCombo.getText());
  }
  
  /**
   * sets the items resolution.
   * @param resolutionArray the array of the String resolution items.
   */
  public void setItemResolution(String[] resolutionArray) {
    String currentText = this.resolutionCombo.getText();
    this.resolutionCombo.setItems(resolutionArray);
    this.resolutionCombo.setText(currentText);
  }
  
  /**
   * Gets the default status key.
   * @return the default status key.
   */
  public String getDefaultStatusKey() {
    return ReviewI18n.getKey(this.statusCombo.getText());
  }
  
  /**
   * sets the items status.
   * @param statusArray the array of the String status items.
   */
  public void setItemStatus(String[] statusArray) {
    String currentText = this.statusCombo.getText();
    this.statusCombo.setItems(statusArray);
    this.statusCombo.setText(currentText);
  }

}
