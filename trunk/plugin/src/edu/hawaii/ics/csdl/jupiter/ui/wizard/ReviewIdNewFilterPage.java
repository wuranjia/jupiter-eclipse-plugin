package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Text;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.FieldItem;
import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ResolutionKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.SeverityKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.StatusKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.TypeKeyManager;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.FilterEntry;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.FilterPhase;

/**
 * Provides filter page.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdNewFilterPage extends WizardPage {

  private IProject project;
  private Map phaseNameFilterPhaseMap;
  private Combo phaseCombo;
  private Button enabledCheckButton;
  private Button intervalCheckButton;
  private Text intervalFilterText;
  private Button reviewerCheckButton;
  private Combo reviewerFilterCombo;
  private Button typeCheckButton;
  private Combo typeFilterCombo;
  private Button severityCheckButton;
  private Combo severityFilterCombo;
  private Button assignedToCheckButton;
  private Combo assignedToFilterCombo;
  private Button resolutionCheckButton;
  private Combo resolutionFilterCombo;
  private Button statusCheckButton;
  private Combo statusFilterCombo;
  private Button fileCheckButton;
  private Combo fileFilterCombo;

  /**
   * @param project the project.
   * @param pageName the page name.
   * @param imageFilePath the image file path.
   */
  protected ReviewIdNewFilterPage(IProject project, String pageName, String imageFilePath) {
    super(pageName);
    setImageDescriptor(ReviewPlugin.createImageDescriptor(imageFilePath));
    setTitle(ReviewI18n.getString("ReviewIdNewFilterPage.label.title"));
    setDescription(ReviewI18n.getString("ReviewIdNewFilterPage.label.title.description"));
    this.project = project;
  }

  /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite ancestor) {
    Composite parent = createsGeneralComposite(ancestor);
    createFilterContent(parent);
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
   * Creates filter content.
   * @param composite the composite.
   */
  private void createFilterContent(Composite composite) {
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    String reviewIdString = PropertyConstraints.DEFAULT_REVIEW_ID;
    ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);
    final ReviewId reviewId = reviewResource.getReviewId();
    this.phaseNameFilterPhaseMap = reviewResource.getPhaseNameToFilterPhaseMap();
    List phaseNameList = reviewResource.getPhaseNameList();
    this.phaseCombo = new Combo(composite, SWT.READ_ONLY);
    phaseCombo.setData(phaseCombo);
    String[] items = (String[]) phaseNameList.toArray(new String[] {});
    phaseCombo.setItems(items);
    phaseCombo.setText(items[0]);
    phaseCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        fillNewValueInCombos(phaseCombo.getText());
      }
    });
    FormData phaseComboData = new FormData();
    phaseComboData.left = new FormAttachment(0, 0);
    phaseComboData.right = new FormAttachment(100, 0);
    phaseCombo.setLayoutData(phaseComboData);
    
    this.enabledCheckButton = new Button(composite, SWT.CHECK);
    String enableKey = "ReviewIdEditDialog.label.tab.filters.check.enabled";
    enabledCheckButton.setText(ReviewI18n.getString(enableKey));
    String phaseName = this.phaseCombo.getText();
    FilterPhase filterPhase = (FilterPhase) this.phaseNameFilterPhaseMap.get(phaseName);
    enabledCheckButton.setSelection(filterPhase.isEnabled());
    enabledCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        handleEnabledCheck();
        boolean isEnabled = enabledCheckButton.getSelection();
        updateFilterEnabled(null, isEnabled);
      }
    });
    
    FormData enabledCheckButtonData = new FormData();
    enabledCheckButtonData.top = new FormAttachment(phaseCombo, 10);
    enabledCheckButtonData.left = new FormAttachment(phaseCombo, 0, SWT.LEFT);
    enabledCheckButtonData.right = new FormAttachment(100, 0);
    enabledCheckButton.setLayoutData(enabledCheckButtonData);
    
    this.intervalCheckButton = new Button(composite, SWT.CHECK);
    String intervalKey = "ReviewIdEditDialog.label.tab.filters.check.interval";
    intervalCheckButton.setText(ReviewI18n.getString(intervalKey));
    int x = intervalCheckButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;   
    FormData intervalCheckButtonData = new FormData();
    intervalCheckButtonData.top = new FormAttachment(enabledCheckButton, 20);
    intervalCheckButton.setLayoutData(intervalCheckButtonData);
    FilterEntry entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_INTERVAL);
    intervalCheckButton.setSelection(entry.isEnabled());
    this.intervalFilterText = new Text(composite, SWT.BORDER);
    intervalFilterText.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
      }
      public void focusLost(FocusEvent event) {
        String value = intervalFilterText.getText();
        updateFilterValue(PropertyConstraints.ATTRIBUTE_VALUE_INTERVAL, value);
      }
    });
    FormData intervalFilterTextData = new FormData();
    intervalFilterTextData.top = new FormAttachment(intervalCheckButton, 0, SWT.CENTER);
    intervalFilterTextData.left = new FormAttachment(intervalCheckButton, x + 20, SWT.LEFT);
    intervalFilterTextData.right = new FormAttachment(100, 0);
    intervalFilterText.setLayoutData(intervalFilterTextData);
    intervalFilterText.setText(ReviewI18n.getString(entry.getValueKey()));
    intervalFilterText.setEnabled(intervalCheckButton.getSelection());
    intervalCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        boolean isEnabled = intervalCheckButton.getSelection();
        handleFilterEnabledCheck(isEnabled, intervalFilterText);
        updateFilterEnabled(PropertyConstraints.ATTRIBUTE_VALUE_INTERVAL, isEnabled);
      }
    });
    
    this.reviewerCheckButton = new Button(composite, SWT.CHECK);
    String reviewerKey = "ReviewIdEditDialog.label.tab.filters.check.reviewer";
    reviewerCheckButton.setText(ReviewI18n.getString(reviewerKey));
    FormData reviewerCheckButtonData = new FormData();
    reviewerCheckButtonData.top = new FormAttachment(intervalCheckButton, 10);
    reviewerCheckButton.setLayoutData(reviewerCheckButtonData);
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_REVIEWER);
    reviewerCheckButton.setSelection(entry.isEnabled());
    this.reviewerFilterCombo = new Combo(composite, SWT.READ_ONLY);
    reviewerFilterCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String value = reviewerFilterCombo.getText();
        if (value.equals(ReviewI18n.getString(ReviewerId.AUTOMATIC_KEY))) {
          value = ReviewerId.AUTOMATIC_KEY;
        }
        updateFilterValue(PropertyConstraints.ATTRIBUTE_VALUE_REVIEWER, value);
      }
    });
    FormData reviewerFilterComboData = new FormData();
    reviewerFilterComboData.top = new FormAttachment(reviewerCheckButton, 0, SWT.CENTER);
    reviewerFilterComboData.left = new FormAttachment(reviewerCheckButton, x + 20, SWT.LEFT);
    reviewerFilterComboData.right = new FormAttachment(100, 0);
    reviewerFilterCombo.setLayoutData(reviewerFilterComboData);
    reviewerFilterCombo.setEnabled(reviewerCheckButton.getSelection());
    String reviewerNameId = PropertyConstraints.ATTRIBUTE_VALUE_REVIEWER;
    Map reviewers = reviewResource.getReviewers();
    reviewerFilterCombo.setItems((String[]) reviewers.keySet().toArray(new String[] {}));
    reviewerFilterCombo.add(ReviewI18n.getString(ReviewerId.AUTOMATIC_KEY), 0);
    reviewerFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    reviewerCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        boolean isEnabled = reviewerCheckButton.getSelection();
        handleFilterEnabledCheck(isEnabled, reviewerFilterCombo);
        updateFilterEnabled(PropertyConstraints.ATTRIBUTE_VALUE_REVIEWER, isEnabled);
      }
    });
    
    this.typeCheckButton = new Button(composite, SWT.CHECK);
    String typeKey = "ReviewIdEditDialog.label.tab.filters.check.type";
    typeCheckButton.setText(ReviewI18n.getString(typeKey));
    FormData typeCheckButtonData = new FormData();
    typeCheckButtonData.top = new FormAttachment(reviewerCheckButton, 10);
    typeCheckButton.setLayoutData(typeCheckButtonData);
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_TYPE);
    typeCheckButton.setSelection(entry.isEnabled());
    this.typeFilterCombo = new Combo(composite, SWT.READ_ONLY);
    typeFilterCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String value = typeFilterCombo.getText();
        String key = TypeKeyManager.getInstance(project, reviewId).getKey(value);
        updateFilterValue(PropertyConstraints.ATTRIBUTE_VALUE_TYPE, key);
      }
    });
    FormData typeFilterComboData = new FormData();
    typeFilterComboData.top = new FormAttachment(typeCheckButton, 0, SWT.CENTER);
    typeFilterComboData.left = new FormAttachment(typeCheckButton, x + 20, SWT.LEFT);
    typeFilterComboData.right = new FormAttachment(100, 0);
    typeFilterCombo.setLayoutData(typeFilterComboData);
    typeFilterCombo.setEnabled(typeCheckButton.getSelection());
    String typeNameId = PropertyConstraints.ATTRIBUTE_VALUE_TYPE;
    IWizardPage page = getWizard().getPage(ReviewIdNewWizard.PAGE_ITEM_ENTRIES);
    ReviewIdNewItemEntriesPage itemEntryPage = (ReviewIdNewItemEntriesPage) page;
    Map fieldItemIdFieldItemMap = itemEntryPage.getFieldItemIdFieldItemMap();
    FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(typeNameId);
    if (fieldItem != null) {
      typeFilterCombo.setItems((String[]) fieldItem.getEntryNameList().toArray(new String[] {}));
    }
    typeFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    typeCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        boolean isEnabled = typeCheckButton.getSelection();
        handleFilterEnabledCheck(isEnabled, typeFilterCombo);
        updateFilterEnabled(PropertyConstraints.ATTRIBUTE_VALUE_TYPE, isEnabled);
      }
    });
    
    this.severityCheckButton = new Button(composite, SWT.CHECK);
    String severityKey = "ReviewIdEditDialog.label.tab.filters.check.severity";
    severityCheckButton.setText(ReviewI18n.getString(severityKey));
    FormData severityCheckButtonData = new FormData();
    severityCheckButtonData.top = new FormAttachment(typeCheckButton, 10);
    severityCheckButton.setLayoutData(severityCheckButtonData);
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY);
    severityCheckButton.setSelection(entry.isEnabled());
    this.severityFilterCombo = new Combo(composite, SWT.READ_ONLY);
    severityFilterCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String value = severityFilterCombo.getText();
        String key = SeverityKeyManager.getInstance(project, reviewId).getKey(value);
        updateFilterValue(PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY, key);
      }
    });
    FormData severityFilterComboData = new FormData();
    severityFilterComboData.top = new FormAttachment(severityCheckButton, 0, SWT.CENTER);
    severityFilterComboData.left = new FormAttachment(severityCheckButton, x + 20, SWT.LEFT);
    severityFilterComboData.right = new FormAttachment(100, 0);
    severityFilterCombo.setLayoutData(severityFilterComboData);
    severityFilterCombo.setEnabled(severityCheckButton.getSelection());
    String severityNameId = PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY;
    fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(severityNameId);
    if (fieldItem != null) {
      List itemNameList = fieldItem.getEntryNameList();
      severityFilterCombo.setItems((String[]) itemNameList.toArray(new String[] {}));
    }
    severityFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    severityCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        boolean isEnabled = severityCheckButton.getSelection();
        handleFilterEnabledCheck(isEnabled, severityFilterCombo);
        updateFilterEnabled(PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY, isEnabled);
      }
    });
    
    this.assignedToCheckButton = new Button(composite, SWT.CHECK);
    String assignedToKey = "ReviewIdEditDialog.label.tab.filters.check.assignedTo";
    assignedToCheckButton.setText(ReviewI18n.getString(assignedToKey));
    FormData assignedToCheckButtonData = new FormData();
    assignedToCheckButtonData.top = new FormAttachment(severityCheckButton, 10);
    assignedToCheckButton.setLayoutData(assignedToCheckButtonData);
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_ASSIGNED_TO);
    assignedToCheckButton.setSelection(entry.isEnabled());
    this.assignedToFilterCombo = new Combo(composite, SWT.READ_ONLY);
    assignedToFilterCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String value = assignedToFilterCombo.getText();
        if (value.equals(ReviewI18n.getString(ReviewerId.AUTOMATIC_KEY))) {
          value = ReviewerId.AUTOMATIC_KEY;
        }
        updateFilterValue(PropertyConstraints.ATTRIBUTE_VALUE_ASSIGNED_TO, value);
      }
    });
    FormData assignedToFilterComboData = new FormData();
    assignedToFilterComboData.top = new FormAttachment(assignedToCheckButton, 0, SWT.CENTER);
    assignedToFilterComboData.left = new FormAttachment(assignedToCheckButton, x + 20, SWT.LEFT);
    assignedToFilterComboData.right = new FormAttachment(100, 0);
    assignedToFilterCombo.setLayoutData(assignedToFilterComboData);
    assignedToFilterCombo.setEnabled(assignedToCheckButton.getSelection());
    reviewers = reviewResource.getReviewers();
    assignedToFilterCombo.setItems((String[]) reviewers.keySet().toArray(new String[] {}));
    assignedToFilterCombo.add(ReviewI18n.getString(ReviewerId.AUTOMATIC_KEY), 0);
    assignedToFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    assignedToCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        boolean isEnabled = assignedToCheckButton.getSelection();
        handleFilterEnabledCheck(isEnabled, assignedToFilterCombo);
        updateFilterEnabled(PropertyConstraints.ATTRIBUTE_VALUE_ASSIGNED_TO, isEnabled);
      }
    });
    
    this.resolutionCheckButton = new Button(composite, SWT.CHECK);
    String resolutionKey = "ReviewIdEditDialog.label.tab.filters.check.resolution";
    resolutionCheckButton.setText(ReviewI18n.getString(resolutionKey));
    FormData resolutionCheckButtonData = new FormData();
    resolutionCheckButtonData.top = new FormAttachment(assignedToCheckButton, 10);
    resolutionCheckButton.setLayoutData(resolutionCheckButtonData);
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION);
    resolutionCheckButton.setSelection(entry.isEnabled());
    this.resolutionFilterCombo = new Combo(composite, SWT.READ_ONLY);
    resolutionFilterCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String value = resolutionFilterCombo.getText();
        String key = ResolutionKeyManager.getInstance(project, reviewId).getKey(value);
        updateFilterValue(PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION, key);
      }
    });
    FormData resolutionFilterComboData = new FormData();
    resolutionFilterComboData.top = new FormAttachment(resolutionCheckButton, 0, SWT.CENTER);
    resolutionFilterComboData.left = new FormAttachment(resolutionCheckButton, x + 20, SWT.LEFT);
    resolutionFilterComboData.right = new FormAttachment(100, 0);
    resolutionFilterCombo.setLayoutData(resolutionFilterComboData);
    resolutionFilterCombo.setEnabled(resolutionCheckButton.getSelection());
    String resolutionNameId = PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION;
    fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(resolutionNameId);
    if (fieldItem != null) {
      List itemNameList = fieldItem.getEntryNameList();
      resolutionFilterCombo.setItems((String[]) itemNameList.toArray(new String[] {}));
    }
    resolutionFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    resolutionCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        boolean isEnabled = resolutionCheckButton.getSelection();
        handleFilterEnabledCheck(isEnabled, resolutionFilterCombo);
        updateFilterEnabled(PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION, isEnabled);
      }
    });
    
    this.statusCheckButton = new Button(composite, SWT.CHECK);
    String statusKey = "ReviewIdEditDialog.label.tab.filters.check.status";
    statusCheckButton.setText(ReviewI18n.getString(statusKey));
    FormData statusCheckButtonData = new FormData();
    statusCheckButtonData.top = new FormAttachment(resolutionCheckButton, 10);
    statusCheckButton.setLayoutData(statusCheckButtonData);
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_STATUS);
    statusCheckButton.setSelection(entry.isEnabled());
    this.statusFilterCombo = new Combo(composite, SWT.READ_ONLY);
    statusFilterCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String value = statusFilterCombo.getText();
        String key = StatusKeyManager.getInstance(project, reviewId).getKey(value);
        updateFilterValue(PropertyConstraints.ATTRIBUTE_VALUE_STATUS, key);
      }
    });
    FormData statusFilterComboData = new FormData();
    statusFilterComboData.top = new FormAttachment(statusCheckButton, 0, SWT.CENTER);
    statusFilterComboData.left = new FormAttachment(statusCheckButton, x + 20, SWT.LEFT);
    statusFilterComboData.right = new FormAttachment(100, 0);
    statusFilterCombo.setLayoutData(statusFilterComboData);
    statusFilterCombo.setEnabled(statusCheckButton.getSelection());
    String statusNameId = PropertyConstraints.ATTRIBUTE_VALUE_STATUS;
    fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(statusNameId);
    if (fieldItem != null) {
      List itemNameList = fieldItem.getEntryNameList();
      statusFilterCombo.setItems((String[]) itemNameList.toArray(new String[] {}));
    }
    statusFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    statusCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        boolean isEnabled = statusCheckButton.getSelection();
        handleFilterEnabledCheck(isEnabled, statusFilterCombo);
        updateFilterEnabled(PropertyConstraints.ATTRIBUTE_VALUE_STATUS, isEnabled);
      }
    });
    
    this.fileCheckButton = new Button(composite, SWT.CHECK);
    String fileKey = "ReviewIdEditDialog.label.tab.filters.check.file";
    fileCheckButton.setText(ReviewI18n.getString(fileKey));
    FormData fileCheckButtonData = new FormData();
    fileCheckButtonData.top = new FormAttachment(statusCheckButton, 10);
    fileCheckButton.setLayoutData(fileCheckButtonData);
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_FILE);
    fileCheckButton.setSelection(entry.isEnabled());
    this.fileFilterCombo = new Combo(composite, SWT.READ_ONLY);
    fileFilterCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String value = fileFilterCombo.getText();
        updateFilterValue(PropertyConstraints.ATTRIBUTE_VALUE_FILE, value);
      }
    });
    FormData fileFilterComboData = new FormData();
    fileFilterComboData.top = new FormAttachment(fileCheckButton, 0, SWT.CENTER);
    fileFilterComboData.left = new FormAttachment(fileCheckButton, x + 20, SWT.LEFT);
    fileFilterComboData.right = new FormAttachment(100, 0);
    fileFilterCombo.setLayoutData(fileFilterComboData);
    fileFilterCombo.setEnabled(fileCheckButton.getSelection());
    Set fileSet = reviewResource.getFileSet();
    fileFilterCombo.setItems((String[]) fileSet.toArray(new String[] {}));
    fileFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    fileCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        boolean isEnabled = fileCheckButton.getSelection();
        handleFilterEnabledCheck(isEnabled, fileFilterCombo);
        updateFilterEnabled(PropertyConstraints.ATTRIBUTE_VALUE_FILE, isEnabled);
      }
    });
    handleEnabledCheck();
  }
  
  /**
   * Fills new values in combos
   * @param phaseName the phase name.
   */
  protected void fillNewValueInCombos(String phaseName) {
    FilterPhase filterPhase = (FilterPhase) this.phaseNameFilterPhaseMap.get(phaseName);
        
    FilterEntry entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_INTERVAL);
    this.intervalCheckButton.setSelection(entry.isEnabled());
    this.intervalFilterText.setText(ReviewI18n.getString(entry.getValueKey()));
    
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_REVIEWER);
    this.reviewerCheckButton.setSelection(entry.isEnabled());
    this.reviewerFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_TYPE);
    this.typeCheckButton.setSelection(entry.isEnabled());
    this.typeFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY);
    this.severityCheckButton.setSelection(entry.isEnabled());
    this.severityFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_ASSIGNED_TO);
    this.assignedToCheckButton.setSelection(entry.isEnabled());
    this.assignedToFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION);
    this.resolutionCheckButton.setSelection(entry.isEnabled());
    this.resolutionFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_STATUS);
    this.statusCheckButton.setSelection(entry.isEnabled());
    this.statusFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    
    entry = filterPhase.getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_FILE);
    this.fileCheckButton.setSelection(entry.isEnabled());
    this.fileFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    
    this.enabledCheckButton.setSelection(filterPhase.isEnabled());
    handleEnabledCheck();
  }
  
  /**
   * Handles the enabled check status.
   */
  protected void handleEnabledCheck() {
    boolean isEnabled = this.enabledCheckButton.getSelection();
    this.intervalCheckButton.setEnabled(isEnabled);
    this.reviewerCheckButton.setEnabled(isEnabled);
    this.typeCheckButton.setEnabled(isEnabled);
    this.severityCheckButton.setEnabled(isEnabled);
    this.assignedToCheckButton.setEnabled(isEnabled);
    this.resolutionCheckButton.setEnabled(isEnabled);
    this.statusCheckButton.setEnabled(isEnabled);
    this.fileCheckButton.setEnabled(isEnabled);
    if (isEnabled) {
      handleFilterEnabledCheck(intervalCheckButton.getSelection(), intervalFilterText);
      handleFilterEnabledCheck(reviewerCheckButton.getSelection(), reviewerFilterCombo);
      handleFilterEnabledCheck(typeCheckButton.getSelection(), typeFilterCombo);
      handleFilterEnabledCheck(severityCheckButton.getSelection(), severityFilterCombo);
      handleFilterEnabledCheck(assignedToCheckButton.getSelection(), assignedToFilterCombo);
      handleFilterEnabledCheck(resolutionCheckButton.getSelection(), resolutionFilterCombo);
      handleFilterEnabledCheck(statusCheckButton.getSelection(), statusFilterCombo);
      handleFilterEnabledCheck(fileCheckButton.getSelection(), fileFilterCombo);
    }
    else {
      this.intervalFilterText.setEnabled(isEnabled);
      this.reviewerFilterCombo.setEnabled(isEnabled);
      this.typeFilterCombo.setEnabled(isEnabled);
      this.severityFilterCombo.setEnabled(isEnabled);
      this.assignedToFilterCombo.setEnabled(isEnabled);
      this.resolutionFilterCombo.setEnabled(isEnabled);
      this.statusFilterCombo.setEnabled(isEnabled);
      this.fileFilterCombo.setEnabled(isEnabled);
    }
  }
  
  /**
   * updates the filter enable status.
   * @param filterName the filter name. null if the filter in the phase is to be set.
   * @param isEnabled the enabled status of the filter. <code>true</code> if the filter is enabled.
   */
  private void updateFilterEnabled(String filterName, boolean isEnabled) {
    String phaseName = this.phaseCombo.getText();
    FilterPhase filterPhase = (FilterPhase) this.phaseNameFilterPhaseMap.get(phaseName);
    if (filterName != null) {
      FilterEntry entry = filterPhase.getFilterEntry(filterName);
      entry.setEnabled(isEnabled);
    }
    else {
      filterPhase.setEnabled(isEnabled);
    }
  }
  
  /**
   * updates the filter value.
   * @param filterName the filter name.
   * @param key the key of the filter.
   */
  private void updateFilterValue(String filterName, String key) {
    String phaseName = this.phaseCombo.getText();
    FilterPhase filterPhase = (FilterPhase) this.phaseNameFilterPhaseMap.get(phaseName);
    FilterEntry entry = filterPhase.getFilterEntry(filterName);
    entry.setKey(key);
  }
  
  /**
   * Handles the filter enabled check status.
   * @param isEnabled <code>true</code>if the combo associating check box is  enabled.
   * @param scrollable the scrollable to be reflected.
   */
  protected void handleFilterEnabledCheck(boolean isEnabled, Scrollable scrollable) {
    scrollable.setEnabled(isEnabled);
  }
  
  /**
   * Updates the filter item folder.
   * @param fieldItemId the field item id.
   */
  public void updateFilterItems(String fieldItemId) {
    IWizardPage page = getWizard().getPage(ReviewIdNewWizard.PAGE_ITEM_ENTRIES);
    ReviewIdNewItemEntriesPage itemEntryPage = (ReviewIdNewItemEntriesPage) page;
    Map fieldItemIdFieldItemMap = itemEntryPage.getFieldItemIdFieldItemMap();
    FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List itemNameList = fieldItem.getEntryNameList();
      if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_TYPE)) {
        String currentType = this.typeFilterCombo.getText();
        this.typeFilterCombo.setItems((String[]) itemNameList.toArray(new String[] {}));
        this.typeFilterCombo.setText(currentType);
      }
      else if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY)) {
        String currentSeverity = this.severityFilterCombo.getText();
        this.severityFilterCombo.setItems((String[]) itemNameList.toArray(new String[] {}));
        this.severityFilterCombo.setText(currentSeverity);
      }
      else if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION)) {
        String currentResolution = this.resolutionFilterCombo.getText();
        this.severityFilterCombo.setItems((String[]) itemNameList.toArray(new String[] {}));
        this.severityFilterCombo.setText(currentResolution);
      }
      else if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_STATUS)) {
        String currentStatus = this.statusFilterCombo.getText();
        this.statusFilterCombo.setItems((String[]) itemNameList.toArray(new String[] {}));
        this.statusFilterCombo.setText(currentStatus);
      }
    }
  }
  
  /**
   * Gets the map of the String phase name - <code>FilterPhase</code> instance.
   * @return the map of the String phase name - <code>FilterPhase</code> instance.
   */
  public Map getPhaseNameFilterPhaseMap() {
    return this.phaseNameFilterPhaseMap;
  }
}
