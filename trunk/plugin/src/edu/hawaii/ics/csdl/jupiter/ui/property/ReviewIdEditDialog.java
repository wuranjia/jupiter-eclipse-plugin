package edu.hawaii.ics.csdl.jupiter.ui.property;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.internal.filesystem.local.LocalFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.dialogs.FileFolderSelectionDialog;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.FieldItem;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyXmlSerializer;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.file.property.Review;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ResolutionKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.SeverityKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.StatusKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.TypeKeyManager;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.FilterEntry;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.FilterPhase;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;
import edu.hawaii.ics.csdl.jupiter.util.TabFolderLayout;

/**
 * Provides review id edit dialog.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdEditDialog extends Dialog {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  private Text reviewIdText;
  private Text reviewIdDescriptionText;
  private IProject project;
  private ReviewId reviewId;
  private Table reviewerListTable;
  private Button addButtonInReviewer;
  private Button removeButtonInReviewer;
  private Button addButtonInFile;
  private Button removeButtonInFile;
  private Map<String, ReviewerId> reviewers;
  private Set<String> files;
  private Combo authorCombo;
  private Text storageText;
  private static final int WIDTH = 400;
  private int folderWidth;
  private Combo defaultTypeCombo;
  private Combo defaultSeverityCombo;
  private Combo defaultResolutionCombo;
  private Combo defaultStatusCombo;
  private Table fileListTable;
  private Combo itemCombo;
  /** The map of the String field item id - <code>FieldItem</code> instance. */
  private Map<String, FieldItem> fieldItemIdFieldItemMap;
  /** The list of the String field item IDs. */
  private List<String> fieldItemIdList;
  private Table itemListTable;
  private Button newButtonInItemEntries;
  private Button removeButtonInItemEntries;
  private Button editButtonInItemEntries;
  private Button upButtonInItemEntries;
  private Button downButtonInItemEntries;
  private Button restoreButtonInItemEntries;
  private Combo phaseCombo;
  private Map<String, FilterPhase> phaseNameFilterPhaseMap;
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
   * Instantiates the review id edit dialog.
   * 
   * @param parentShell the shell.
   * @param project the project.
   * @param reviewId the review id.
   */
  public ReviewIdEditDialog(Shell parentShell, IProject project, ReviewId reviewId) {
    super(parentShell);
    String imageFilePath = "icons/jupiter.gif";
    Image iconJupiter = ReviewPlugin.createImageDescriptor(imageFilePath).createImage();
    setDefaultImage(iconJupiter);
    this.project = project;
    this.reviewId = reviewId;
  }

  /**
   * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
   */
  protected Control createDialogArea(Composite parent) {
    getShell().setText(ReviewI18n.getString("ReviewIdEditDialog.label.title"));
    Composite composite = (Composite) super.createDialogArea(parent);
    Composite dialog = createsGeneralComposite(composite);
    FormLayout layout = (FormLayout) dialog.getLayout();
    folderWidth += (WIDTH - layout.marginWidth * 2);
    createReviewIdContent(dialog);
    createReviewIdDescriptionContent(dialog);
    TabFolder folder = createTabFolder(dialog);
    FormData folderData = (FormData) folder.getLayoutData();
    createFileTabContent(folder);
    createReviewerTabContent(folder);
    craeteAuthorTabContent(folder);
    createStorageTabContent(folder);
    createItemEntriesTabContent(folder);
    createDefaultItemsTabContent(folder);
    createFiltersTabContent(folder);
    return composite;
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
   * Creates review ID content.
   * 
   * @param parent the composite.
   */
  private void createReviewIdContent(Composite parent) {
    Label reviewIdLabel = new Label(parent, SWT.NONE);
    reviewIdLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.reviewId"));
    this.reviewIdText = new Text(parent, SWT.SINGLE | SWT.BORDER);
    FormData reviewIdLabelData = new FormData();
    reviewIdLabelData.top = new FormAttachment(reviewIdText, 0, SWT.CENTER);
    reviewIdLabel.setLayoutData(reviewIdLabelData);
    FormData reviewIdTextData = new FormData();
    reviewIdTextData.left = new FormAttachment(reviewIdLabel, 20, SWT.RIGHT);
    reviewIdTextData.right = new FormAttachment(100, 0);
    reviewIdText.setLayoutData(reviewIdTextData);
    reviewIdText.setEditable(false);
    reviewIdText.setText(reviewId.getReviewId());
  }

  /**
   * Creates review id description content.
   * 
   * @param parent the composite.
   */
  private void createReviewIdDescriptionContent(Composite parent) {
    Label reviewIdDescriptionLabel = new Label(parent, SWT.NONE);
    String description = ReviewI18n.getString("ReviewIdEditDialog.label.description");
    reviewIdDescriptionLabel.setText(description);
    this.reviewIdDescriptionText = new Text(parent, SWT.SINGLE | SWT.BORDER);
    FormData reviewIdDescriptionLabelData = new FormData();
    reviewIdDescriptionLabelData.top = new FormAttachment(reviewIdDescriptionText, 0,
        SWT.CENTER);
    reviewIdDescriptionLabel.setLayoutData(reviewIdDescriptionLabelData);
    FormData reviewIdDescriptionTextData = new FormData();
    reviewIdDescriptionTextData.top = new FormAttachment(reviewIdText, 5);
    reviewIdDescriptionTextData.left = new FormAttachment(reviewIdText, 0, SWT.LEFT);
    reviewIdDescriptionTextData.right = new FormAttachment(100, 0);
    reviewIdDescriptionText.setLayoutData(reviewIdDescriptionTextData);
    reviewIdDescriptionText.setText(ReviewI18n.getString(reviewId.getDescription()));
    if (reviewId.getReviewId().equals(PropertyConstraints.DEFAULT_REVIEW_ID)) {
      reviewIdDescriptionText.setEditable(false);
    }
  }

  /**
   * Creates tab folder.
   * 
   * @param dialog the composite.
   * @return the <code>TabFolder</code> instance.
   */
  private TabFolder createTabFolder(Composite dialog) {
    TabFolder folder = new TabFolder(dialog, SWT.NONE);
    folder.setLayout(new TabFolderLayout());
    FormData folderData = new FormData();
    folderData.width = folderWidth;
    folderData.top = new FormAttachment(reviewIdDescriptionText, 5);
    folderData.left = new FormAttachment(0, 0);
    folderData.right = new FormAttachment(100, 0);
    folder.setLayoutData(folderData);
    return folder;
  }

  /**
   * Creates reviewer tab content in the <code>TabFolder</code>.
   * 
   * @param folder the <code>TabFolder</code> instance.
   */
  private void createReviewerTabContent(TabFolder folder) {
    TabItem reviewerTabItem = new TabItem(folder, SWT.NONE);
    String reviewerLabel = ReviewI18n.getString("ReviewIdEditDialog.label.tab.reviewer");
    reviewerTabItem.setText(reviewerLabel);
    reviewerTabItem.setControl(createReviewerFolder(folder));
  }

  /**
   * Creates author tab content in the <code>TabFolder</code>.
   * 
   * @param folder the <code>TabFolder</code> instance.
   */
  private void craeteAuthorTabContent(TabFolder folder) {
    TabItem authorTabItem = new TabItem(folder, SWT.NONE);
    String authorLabel = ReviewI18n.getString("ReviewIdEditDialog.label.tab.author");
    authorTabItem.setText(authorLabel);
    authorTabItem.setControl(createAuthorFolder(folder));
  }

  /**
   * Creates storage tab content in the <code>TabFolder</code>.
   * 
   * @param folder the <code>TabFolder</code> instance.
   */
  private void createStorageTabContent(TabFolder folder) {
    TabItem storageTabItem = new TabItem(folder, SWT.NONE);
    String authorLabel = ReviewI18n.getString("ReviewIdEditDialog.label.tab.storage");
    storageTabItem.setText(authorLabel);
    storageTabItem.setControl(createStorageFolder(folder));
  }

  /**
   * Creates the default items tab content in the <code>TabFolder</code>.
   * 
   * @param folder the <code>TabFolder</code> instance.
   */
  private void createDefaultItemsTabContent(TabFolder folder) {
    TabItem defaultItemsTabItem = new TabItem(folder, SWT.NONE);
    String defaultItemsLabel = ReviewI18n
        .getString("ReviewIdEditDialog.label.tab.defaultItems");
    defaultItemsTabItem.setText(defaultItemsLabel);
    defaultItemsTabItem.setControl(createDefaultItemsFolder(folder));
  }

  /**
   * Creates the filters tab content in the <code>TabFolder</code>.
   * 
   * @param folder the <code>TabFolder</code> instance.
   */
  private void createFiltersTabContent(TabFolder folder) {
    TabItem filtersTabItem = new TabItem(folder, SWT.NONE);
    String filtersLabel = ReviewI18n.getString("ReviewIdEditDialog.label.tab.filters");
    filtersTabItem.setText(filtersLabel);
    filtersTabItem.setControl(createfiltersFolder(folder));
  }

  /**
   * Creates the item entries tab content in the <code>TabFolder</code>.
   * 
   * @param folder the <code>TabFolder</code> instance.
   */
  private void createItemEntriesTabContent(TabFolder folder) {
    TabItem itemEntriesTabItem = new TabItem(folder, SWT.NONE);
    String itemEntriesLabel = ReviewI18n.getString("ReviewIdEditDialog.label.tab.itemEntries");
    itemEntriesTabItem.setText(itemEntriesLabel);
    itemEntriesTabItem.setControl(createItemEntriesFolder(folder));
  }

  /**
   * Creates the file tab content in the <code>TabFolder</code>.
   * 
   * @param folder the <code>TabFolder</code> instance.
   */
  private void createFileTabContent(TabFolder folder) {
    TabItem fileTabItem = new TabItem(folder, SWT.NONE);
    String fileLabel = ReviewI18n.getString("ReviewIdEditDialog.label.tab.file");
    fileTabItem.setText(fileLabel);
    fileTabItem.setControl(createFileFolder(folder));
  }

  /**
   * Creates reviewer table folder.
   * 
   * @param folder the folder.
   * @return the control.
   */
  private Control createReviewerFolder(TabFolder folder) {
    Composite composite = createsGeneralComposite(folder);
    this.reviewerListTable = new Table(composite, SWT.BORDER | SWT.MULTI);
    FormData reviewerListTableData = new FormData();
    reviewerListTableData.left = new FormAttachment(0, 0);
    reviewerListTableData.right = new FormAttachment(80, 0);
    reviewerListTableData.top = new FormAttachment(0, 0);
    reviewerListTableData.bottom = new FormAttachment(100, 0);
    reviewerListTableData.height = 150;
    reviewerListTable.setLayoutData(reviewerListTableData);
    reviewerListTable.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        handleReviewListSelection();
      }
    });
    fillReviewerTable(false);

    this.addButtonInReviewer = new Button(composite, SWT.PUSH);
    String addKey = "ReviewIdEditDialog.label.tab.reviewer.button.add";
    addButtonInReviewer.setText(ReviewI18n.getString(addKey));
    addButtonInReviewer.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        addReviewer();
      }
    });
    FormData addButtonData = new FormData();
    addButtonData.top = new FormAttachment(reviewerListTable, 0, SWT.TOP);
    addButtonData.left = new FormAttachment(reviewerListTable, 10);
    addButtonData.right = new FormAttachment(100, 0);
    addButtonInReviewer.setLayoutData(addButtonData);

    this.removeButtonInReviewer = new Button(composite, SWT.PUSH);
    String removeKey = "ReviewIdEditDialog.label.tab.reviewer.button.remove";
    removeButtonInReviewer.setText(ReviewI18n.getString(removeKey));
    removeButtonInReviewer.setEnabled(false);
    removeButtonInReviewer.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        removeReviewer();
      }
    });
    FormData removeButtonData = new FormData();
    removeButtonData.top = new FormAttachment(addButtonInReviewer, 5);
    removeButtonData.left = new FormAttachment(addButtonInReviewer, 0, SWT.LEFT);
    removeButtonData.right = new FormAttachment(100, 0);
    removeButtonInReviewer.setLayoutData(removeButtonData);

    return composite;
  }

  /**
   * Creates author folder.
   * 
   * @param folder the folder.
   * @return the control.
   */
  private Control createAuthorFolder(TabFolder folder) {
    Composite composite = createsGeneralComposite(folder);
    int marginWidth = ((FormLayout) composite.getLayout()).marginWidth;
    Label authorLabel = new Label(composite, SWT.NONE);
    authorLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.author"));
    this.authorCombo = new Combo(composite, SWT.READ_ONLY);
    authorCombo.setData(authorCombo);
    authorCombo.setItems((String[]) this.reviewers.keySet().toArray(new String[] {}));
    authorCombo.setText(reviewId.getAuthor());

    FormData authorLabelData = new FormData();
    authorLabelData.width = (int) ((folderWidth - marginWidth * 2) * 0.45);
    authorLabelData.top = new FormAttachment(authorCombo, 0, SWT.CENTER);
    authorLabel.setLayoutData(authorLabelData);
    FormData authorComboData = new FormData();
    authorComboData.left = new FormAttachment(authorLabel, 0);
    authorComboData.right = new FormAttachment(100, 0);
    authorCombo.setLayoutData(authorComboData);
    return composite;
  }

  /**
   * Creates storage folder.
   * 
   * @param folder the folder.
   * @return the control.
   */
  private Control createStorageFolder(TabFolder folder) {
    Composite composite = createsGeneralComposite(folder);
    int marginWidth = ((FormLayout) composite.getLayout()).marginWidth;
    Label storageLabel = new Label(composite, SWT.NONE);
    storageLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.storage"));
    this.storageText = new Text(composite, SWT.BORDER);
    storageText.setText(reviewId.getDirectory());

    FormData storageLabelData = new FormData();
    storageLabelData.width = (int) ((folderWidth - marginWidth * 2) * 0.45);
    storageLabelData.top = new FormAttachment(storageText, 0, SWT.CENTER);
    storageLabel.setLayoutData(storageLabelData);
    FormData storageTextData = new FormData();
    storageTextData.left = new FormAttachment(storageLabel, 0);
    storageTextData.right = new FormAttachment(100, 0);
    storageText.setLayoutData(storageTextData);
    return composite;
  }

  /**
   * Creates default items folder.
   * 
   * @param folder the folder.
   * @return the control.
   */
  private Control createDefaultItemsFolder(TabFolder folder) {
    Composite composite = createsGeneralComposite(folder);
    int marginWidth = ((FormLayout) composite.getLayout()).marginWidth;
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    String reviewIdString = reviewId.getReviewId();
    ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);

    // create type label and its combo.
    Label defaultTypeLabel = new Label(composite, SWT.NONE);
    defaultTypeLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.type"));
    this.defaultTypeCombo = new Combo(composite, SWT.READ_ONLY);
    defaultTypeCombo.setData(defaultTypeCombo);
    defaultTypeCombo.setItems(TypeKeyManager.getInstance(project, reviewId).getElements());
    String typeName = PropertyConstraints.ATTRIBUTE_VALUE_TYPE;
    String typeKey = (reviewResource != null) ? reviewResource.getDefaultField(typeName) : "";
    defaultTypeCombo.setText(ReviewI18n.getString(typeKey));
    defaultTypeCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String nameId = PropertyConstraints.ATTRIBUTE_VALUE_TYPE;
        FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(nameId);
        fieldItem.setDefaultKey(ReviewI18n.getKey(defaultTypeCombo.getText()));
      }
    });

    FormData defaultTypeLabelData = new FormData();
    defaultTypeLabelData.width = (int) ((folderWidth - marginWidth * 2) * 0.45);
    defaultTypeLabelData.top = new FormAttachment(defaultTypeCombo, 0, SWT.CENTER);
    defaultTypeLabel.setLayoutData(defaultTypeLabelData);
    FormData defaultTypeComboData = new FormData();
    defaultTypeComboData.left = new FormAttachment(defaultTypeLabel, 0);
    defaultTypeComboData.right = new FormAttachment(100, 0);
    defaultTypeCombo.setLayoutData(defaultTypeComboData);

    // create severity label and its combo.
    Label defaultSeverityLabel = new Label(composite, SWT.NONE);
    defaultSeverityLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.severity"));
    this.defaultSeverityCombo = new Combo(composite, SWT.READ_ONLY);
    defaultSeverityCombo.setData(defaultSeverityCombo);
    defaultSeverityCombo.setItems(SeverityKeyManager.getInstance(project, reviewId)
        .getElements());
    String severityName = PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY;
    String severityKey = (reviewResource != null) ? reviewResource
        .getDefaultField(severityName) : "";
    defaultSeverityCombo.setText(ReviewI18n.getString(severityKey));
    defaultSeverityCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String nameId = PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY;
        FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(nameId);
        fieldItem.setDefaultKey(ReviewI18n.getKey(defaultSeverityCombo.getText()));
      }
    });

    FormData defaultSeverityLabelData = new FormData();
    defaultSeverityLabelData.width = (int) ((folderWidth - marginWidth * 2) * 0.45);
    defaultSeverityLabelData.top = new FormAttachment(defaultSeverityCombo, 0, SWT.CENTER);
    defaultSeverityLabel.setLayoutData(defaultSeverityLabelData);
    FormData defaultSeverityComboData = new FormData();
    defaultSeverityComboData.top = new FormAttachment(defaultTypeCombo, 5);
    defaultSeverityComboData.left = new FormAttachment(defaultSeverityLabel, 0);
    defaultSeverityComboData.right = new FormAttachment(100, 0);
    defaultSeverityCombo.setLayoutData(defaultSeverityComboData);

    // create resolution label and its combo.
    Label defaultResolutionLabel = new Label(composite, SWT.NONE);
    defaultResolutionLabel
        .setText(ReviewI18n.getString("ReviewIdEditDialog.label.resolution"));
    this.defaultResolutionCombo = new Combo(composite, SWT.READ_ONLY);
    defaultResolutionCombo.setData(defaultResolutionCombo);
    ResolutionKeyManager resolutionKeyManager = ResolutionKeyManager.getInstance(project,
        reviewId);
    defaultResolutionCombo.setItems(resolutionKeyManager.getElements());
    String resolutionName = PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION;
    String resolutionKey = (reviewResource != null) ? reviewResource
        .getDefaultField(resolutionName) : "";
    defaultResolutionCombo.setText(ReviewI18n.getString(resolutionKey));
    defaultResolutionCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String nameId = PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION;
        FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(nameId);
        fieldItem.setDefaultKey(ReviewI18n.getKey(defaultResolutionCombo.getText()));
      }
    });

    FormData defaultResolutionLabelData = new FormData();
    defaultResolutionLabelData.width = (int) ((folderWidth - marginWidth * 2) * 0.45);
    defaultResolutionLabelData.top = new FormAttachment(defaultResolutionCombo, 0, SWT.CENTER);
    defaultResolutionLabel.setLayoutData(defaultResolutionLabelData);
    FormData defaultResolutionComboData = new FormData();
    defaultResolutionComboData.top = new FormAttachment(defaultSeverityCombo, 5);
    defaultResolutionComboData.left = new FormAttachment(defaultResolutionLabel, 0);
    defaultResolutionComboData.right = new FormAttachment(100, 0);
    defaultResolutionCombo.setLayoutData(defaultResolutionComboData);

    // create status label and its combo.
    Label defaultStatusLabel = new Label(composite, SWT.NONE);
    defaultStatusLabel.setText(ReviewI18n.getString("ReviewIdEditDialog.label.status"));
    this.defaultStatusCombo = new Combo(composite, SWT.READ_ONLY);
    defaultStatusCombo.setData(defaultStatusCombo);
    defaultStatusCombo.setItems(StatusKeyManager.getInstance(project, reviewId).getElements());
    String statusName = PropertyConstraints.ATTRIBUTE_VALUE_STATUS;
    String statusKey = (reviewResource != null) ? reviewResource.getDefaultField(statusName)
        : "";
    defaultStatusCombo.setText(ReviewI18n.getString(statusKey));
    defaultStatusCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String nameId = PropertyConstraints.ATTRIBUTE_VALUE_STATUS;
        FieldItem fieldItem = (FieldItem) fieldItemIdFieldItemMap.get(nameId);
        fieldItem.setDefaultKey(ReviewI18n.getKey(defaultStatusCombo.getText()));
      }
    });

    FormData defaultStatusLabelData = new FormData();
    defaultStatusLabelData.width = (int) ((folderWidth - marginWidth * 2) * 0.45);
    defaultStatusLabelData.top = new FormAttachment(defaultStatusCombo, 0, SWT.CENTER);
    defaultStatusLabel.setLayoutData(defaultStatusLabelData);
    FormData defaultStatusComboData = new FormData();
    defaultStatusComboData.top = new FormAttachment(defaultResolutionCombo, 5);
    defaultStatusComboData.left = new FormAttachment(defaultStatusLabel, 0);
    defaultStatusComboData.right = new FormAttachment(100, 0);
    defaultStatusCombo.setLayoutData(defaultStatusComboData);
    return composite;
  }

  /**
   * Creates filters folder.
   * 
   * @param folder the folder.
   * @return the control.
   */
  private Control createfiltersFolder(TabFolder folder) {
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    String reviewIdString = reviewId.getReviewId();
    ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);
    this.phaseNameFilterPhaseMap = reviewResource.getPhaseNameToFilterPhaseMap();
    List<String> phaseNameList = reviewResource.getPhaseNameList();
    Composite composite = createsGeneralComposite(folder);
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
    FilterEntry entry = filterPhase
        .getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_INTERVAL);
    intervalCheckButton.setSelection(entry.isEnabled());
    this.intervalFilterText = new Text(composite, SWT.BORDER);
    intervalFilterText.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
      }

      public void focusLost(FocusEvent event) {
        String key = intervalFilterText.getText();
        updateFilterValue(PropertyConstraints.ATTRIBUTE_VALUE_INTERVAL, key);
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
    Map<String, ReviewerId> reviewers = reviewResource.getReviewers();
    reviewerFilterCombo.setItems(reviewers.keySet().toArray(new String[] {}));
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
    FieldItem fieldItem = this.fieldItemIdFieldItemMap.get(typeNameId);
    if (fieldItem != null) {
      typeFilterCombo.setItems(fieldItem.getEntryNameList().toArray(new String[] {}));
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
    fieldItem = this.fieldItemIdFieldItemMap.get(severityNameId);
    if (fieldItem != null) {
      List<String> itemNameList = fieldItem.getEntryNameList();
      severityFilterCombo.setItems(itemNameList.toArray(new String[] {}));
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
    assignedToFilterComboData.left = new FormAttachment(assignedToCheckButton, x + 20,
        SWT.LEFT);
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
    resolutionFilterComboData.left = new FormAttachment(resolutionCheckButton, x + 20,
        SWT.LEFT);
    resolutionFilterComboData.right = new FormAttachment(100, 0);
    resolutionFilterCombo.setLayoutData(resolutionFilterComboData);
    resolutionFilterCombo.setEnabled(resolutionCheckButton.getSelection());
    String resolutionNameId = PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION;
    fieldItem = this.fieldItemIdFieldItemMap.get(resolutionNameId);
    if (fieldItem != null) {
      List<String> itemNameList = fieldItem.getEntryNameList();
      resolutionFilterCombo.setItems(itemNameList.toArray(new String[] {}));
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
    fieldItem = this.fieldItemIdFieldItemMap.get(statusNameId);
    if (fieldItem != null) {
      List<String> itemNameList = fieldItem.getEntryNameList();
      statusFilterCombo.setItems(itemNameList.toArray(new String[] {}));
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
    Set<String> fileSet = reviewResource.getFileSet();
    fileFilterCombo.setItems(fileSet.toArray(new String[] {}));
    fileFilterCombo.setText(ReviewI18n.getString(entry.getValueKey()));
    fileCheckButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        boolean isEnabled = fileCheckButton.getSelection();
        handleFilterEnabledCheck(isEnabled, fileFilterCombo);
        updateFilterEnabled(PropertyConstraints.ATTRIBUTE_VALUE_FILE, isEnabled);
      }
    });
    handleEnabledCheck();
    return composite;
  }

  /**
   * updates the filter value.
   * 
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
   * updates the filter enable status.
   * 
   * @param filterName the filter name. null if the filter in the phase is to be set.
   * @param isEnabled the enabled status of the filter. <code>true</code> if the filter is
   *          enabled.
   */
  private void updateFilterEnabled(String filterName, boolean isEnabled) {
    String phaseName = this.phaseCombo.getText();
    FilterPhase filterPhase = this.phaseNameFilterPhaseMap.get(phaseName);
    if (filterName != null) {
      FilterEntry entry = filterPhase.getFilterEntry(filterName);
      entry.setEnabled(isEnabled);
    }
    else {
      filterPhase.setEnabled(isEnabled);
    }
  }

  /**
   * Fills new values in combos
   * 
   * @param phaseName the phase name.
   */
  protected void fillNewValueInCombos(String phaseName) {
    FilterPhase filterPhase = this.phaseNameFilterPhaseMap.get(phaseName);

    FilterEntry entry = filterPhase
        .getFilterEntry(PropertyConstraints.ATTRIBUTE_VALUE_INTERVAL);
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
   * Handles the filter enabled check status.
   * 
   * @param isEnabled <code>true</code>if the combo associating check box is enabled.
   * @param scrollable the scrollable to be reflected.
   */
  protected void handleFilterEnabledCheck(boolean isEnabled, Scrollable scrollable) {
    scrollable.setEnabled(isEnabled);
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
   * Creates item entries folder.
   * 
   * @param folder the folder.
   * @return the control.
   */
  private Control createItemEntriesFolder(TabFolder folder) {
    PropertyResource propertyResource = PropertyResource.getInstance(this.project, true);
    String reviewIdString = this.reviewId.getReviewId();
    ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);
    if (reviewResource != null) {
      this.fieldItemIdFieldItemMap = reviewResource.getFieldItemMap();
      this.fieldItemIdList = reviewResource.getFieldItemIdList();
    }
    Composite composite = createsGeneralComposite(folder);
    this.itemCombo = new Combo(composite, SWT.READ_ONLY);
    itemCombo.setData(itemCombo);
    itemCombo.setItems((String[]) fieldItemIdList.toArray(new String[] {}));
    itemCombo.setText((String) fieldItemIdList.get(0));
    itemCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        Combo selectedItemCombo = (Combo) event.widget.getData();
        handleItemIdComboSelection(selectedItemCombo);
      }
    });
    FormData itemComboData = new FormData();
    itemComboData.left = new FormAttachment(0, 0);
    itemComboData.right = new FormAttachment(100, 0);
    itemCombo.setLayoutData(itemComboData);

    createItemEntriesTable(composite);
    return composite;
  }

  /**
   * Creates item entries table.
   * 
   * @param composite the composite.
   */
  private void createItemEntriesTable(Composite composite) {
    int verticalSpan = 3;
    this.itemListTable = new Table(composite, SWT.BORDER | SWT.MULTI);
    FormData itemListTableData = new FormData();
    itemListTableData.left = new FormAttachment(0, 0);
    itemListTableData.right = new FormAttachment(80, 0);
    itemListTableData.top = new FormAttachment(this.itemCombo, 10);
    itemListTableData.bottom = new FormAttachment(100, 0);
    itemListTableData.height = 150;
    itemListTable.setLayoutData(itemListTableData);
    itemListTable.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        handleItemListTableSelection();
      }
    });
    fillItemTable(this.itemCombo.getText());

    this.newButtonInItemEntries = new Button(composite, SWT.PUSH);
    String newKey = "ReviewIdEditDialog.label.tab.itemEntries.button.new";
    newButtonInItemEntries.setText(ReviewI18n.getString(newKey));
    newButtonInItemEntries.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        newItemEntry();
      }
    });
    FormData newButtonData = new FormData();
    newButtonData.top = new FormAttachment(this.itemListTable, 0, SWT.TOP);
    newButtonData.left = new FormAttachment(this.itemListTable, 10);
    newButtonData.right = new FormAttachment(100, 0);
    newButtonInItemEntries.setLayoutData(newButtonData);

    this.editButtonInItemEntries = new Button(composite, SWT.PUSH);
    String editKey = "ReviewIdEditDialog.label.tab.itemEntries.button.edit";
    editButtonInItemEntries.setText(ReviewI18n.getString(editKey));
    editButtonInItemEntries.setEnabled(false);
    editButtonInItemEntries.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        editItemEntry();
      }
    });
    FormData editButtonData = new FormData();
    editButtonData.top = new FormAttachment(newButtonInItemEntries, verticalSpan);
    editButtonData.left = new FormAttachment(this.itemListTable, 10);
    editButtonData.right = new FormAttachment(100, 0);
    editButtonInItemEntries.setLayoutData(editButtonData);

    this.removeButtonInItemEntries = new Button(composite, SWT.PUSH);
    String removeKey = "ReviewIdEditDialog.label.tab.itemEntries.button.remove";
    removeButtonInItemEntries.setText(ReviewI18n.getString(removeKey));
    removeButtonInItemEntries.setEnabled(false);
    removeButtonInItemEntries.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        removeItemEntries();
      }
    });
    FormData removeButtonData = new FormData();
    removeButtonData.top = new FormAttachment(editButtonInItemEntries, verticalSpan);
    removeButtonData.left = new FormAttachment(newButtonInItemEntries, 0, SWT.LEFT);
    removeButtonData.right = new FormAttachment(100, 0);
    removeButtonInItemEntries.setLayoutData(removeButtonData);

    this.upButtonInItemEntries = new Button(composite, SWT.PUSH);
    Image upImage = ReviewPlugin.createImageDescriptor("icons/up.gif").createImage();
    upButtonInItemEntries.setImage(upImage);
    upButtonInItemEntries.setEnabled(false);
    upButtonInItemEntries.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        moveItemEntry(true);
      }
    });
    FormData upButtonData = new FormData();
    upButtonData.top = new FormAttachment(removeButtonInItemEntries, verticalSpan);
    upButtonData.left = new FormAttachment(newButtonInItemEntries, 0, SWT.LEFT);
    upButtonData.right = new FormAttachment(100, 0);
    upButtonInItemEntries.setLayoutData(upButtonData);

    this.downButtonInItemEntries = new Button(composite, SWT.PUSH);
    Image downImage = ReviewPlugin.createImageDescriptor("icons/down.gif").createImage();
    downButtonInItemEntries.setImage(downImage);
    downButtonInItemEntries.setEnabled(false);
    downButtonInItemEntries.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        moveItemEntry(false);
      }
    });
    FormData downButtonData = new FormData();
    downButtonData.top = new FormAttachment(upButtonInItemEntries, verticalSpan);
    downButtonData.left = new FormAttachment(newButtonInItemEntries, 0, SWT.LEFT);
    downButtonData.right = new FormAttachment(100, 0);
    downButtonInItemEntries.setLayoutData(downButtonData);

    this.restoreButtonInItemEntries = new Button(composite, SWT.PUSH);
    String restoreKey = "ReviewIdEditDialog.label.tab.itemEntries.button.restore";
    restoreButtonInItemEntries.setText(ReviewI18n.getString(restoreKey));
    restoreButtonInItemEntries.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        restoreItemEntries();
      }
    });
    FormData restoreButtonData = new FormData();
    restoreButtonData.top = new FormAttachment(downButtonInItemEntries, verticalSpan + 30);
    restoreButtonData.left = new FormAttachment(newButtonInItemEntries, 0, SWT.LEFT);
    restoreButtonData.right = new FormAttachment(100, 0);
    restoreButtonInItemEntries.setLayoutData(restoreButtonData);
  }

  /**
   * Moves the selected item entry by one upward if <code>isUpward</code> is
   * <code>true</code>. Otherwise, moves the selected item entry by one downward.
   * 
   * @param isUpward <code>true</code> if moving the selected item entry by one upward.
   *          <code>false</code> if moving the selected item entry by one downward.
   */
  protected void moveItemEntry(boolean isUpward) {
    String fieldItemId = this.itemCombo.getText();
    FieldItem fieldItem = this.fieldItemIdFieldItemMap.get(fieldItemId);
    TableItem[] selectedItems = this.itemListTable.getSelection();
    if (selectedItems.length > 0 && fieldItem != null) {
      TableItem selectedItem = selectedItems[0];
      String itemName = selectedItem.getText();
      List<String> itemList = fieldItem.getEntryNameList();
      int index = itemList.indexOf(itemName);
      if ((isUpward) ? index > 0 : index < itemList.size() - 1) {
        int nextIndex = (isUpward) ? index - 1 : index + 1;
        itemList.remove(index);
        itemList.add(nextIndex, itemName);
        fillItemTable(fieldItemId);
        this.itemListTable.select(nextIndex);
        handleItemListTableSelection();
        updateDefaultItems(fieldItemId);
        updateFilterItems(fieldItemId);
      }
    }
  }

  /**
   * Creates new item entry.
   */
  protected void newItemEntry() {
    String shortMessageKey = "ReviewIdEditDialog.dialogMessage.label.tab.itemEntries.new.short";
    String longMessageKey = "ReviewIdEditDialog.dialogMessage.label.tab.itemEntries.new.long";
    InputDialog dialog = openDialog("", shortMessageKey, longMessageKey);
    if (dialog.getReturnCode() != InputDialog.OK) {
      return;
    }
    String itemName = dialog.getValue();
    TableItem item = new TableItem(this.itemListTable, SWT.NONE);
    String fieldItemId = this.itemCombo.getText();
    FieldItem fieldItem = this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List<String> itemList = fieldItem.getEntryNameList();
      itemList.add(itemName);
      fillItemTable(fieldItemId);
      updateDefaultItems(fieldItemId);
      updateFilterItems(fieldItemId);
    }
  }

  /**
   * Edits the item entry.
   */
  protected void editItemEntry() {
    String shortMessageKey = "ReviewIdEditDialog.dialogMessage.label.tab.itemEntries.edit.short";
    String longMessageKey = "ReviewIdEditDialog.dialogMessage.label.tab.itemEntries.edit.long";
    TableItem item = this.itemListTable.getSelection()[0];
    String oldName = item.getText();
    InputDialog dialog = openDialog(oldName, shortMessageKey, longMessageKey);
    if (dialog.getReturnCode() != InputDialog.OK) {
      return;
    }
    String newName = dialog.getValue();
    item.setText(newName);
    String fieldItemId = this.itemCombo.getText();
    FieldItem fieldItem = this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List<String> itemList = fieldItem.getEntryNameList();
      int index = itemList.indexOf(oldName);
      itemList.remove(index);
      itemList.add(index, newName);
      fillItemTable(fieldItemId);
      updateDefaultItems(fieldItemId);
      updateFilterItems(fieldItemId);
    }
  }

  /**
   * Opens the dialog window.
   * 
   * @param existingItemName the existing item name.
   * @param shortMessageKey the short message key.
   * @param longMessageKey the long message key.
   * @return the input dialog the <code>InputDialog</code>.
   */
  private InputDialog openDialog(String existingItemName, String shortMessageKey,
      String longMessageKey) {
    FieldItem fieldItem = this.fieldItemIdFieldItemMap.get(this.itemCombo
        .getText());
    if (fieldItem != null) {
      final List<String> itemList = fieldItem.getEntryNameList();
      IInputValidator validator = new IInputValidator() {
        public String isValid(String newText) {
          if (!itemList.contains(ReviewI18n.getKey(newText))) {
            return null;
          }
          else {
            String errorKey = "ReviewIdEditDialog.dialogMessage.label.tab.itemEntries.error";
            return ReviewI18n.getString(errorKey);
          }
        }
      };
      InputDialog dialog = new InputDialog(getShell(), ReviewI18n.getString(shortMessageKey),
          ReviewI18n.getString(longMessageKey), existingItemName, validator); //$NON-NLS-1$ //$NON-NLS-2$
      dialog.open();
      return dialog;
    }
    return null;
  }

  /**
   * Removes item entries form the table.
   */
  protected void removeItemEntries() {
    String fieldItemId = this.itemCombo.getText();
    FieldItem fieldItem = this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List<String> itemList = fieldItem.getEntryNameList();
      TableItem[] items = this.itemListTable.getSelection();
      for (int i = 0; i < items.length; i++) {
        String itemName = items[i].getText();
        itemList.remove(itemName);
      }
      fillItemTable(fieldItemId);
      updateDefaultItems(fieldItemId);
      updateFilterItems(fieldItemId);
    }
  }

  /**
   * Restores the item entries from the default review id.
   */
  protected void restoreItemEntries() {
    ReviewResource reviewResource = null;
    if (reviewId.getReviewId().equals(PropertyConstraints.DEFAULT_REVIEW_ID)) {
      // read from master property.xml
      Review defaultReview = PropertyXmlSerializer.cloneDefaultReview();
      reviewResource = new ReviewResource(defaultReview);
      // TODO REMOVE THIS
//      Element reviewElement = PropertyXmlSerializer.cloneDefaultReviewElement();
//      reviewResource = new ReviewResource(reviewElement);
    }
    else {
      // read from .jupiter.
      PropertyResource propertyResource = PropertyResource.getInstance(project, true);
      reviewResource = propertyResource.getReviewResource(reviewId.getReviewId(), true);
    }
    if (reviewResource != null) {
      String fieldItemId = this.itemCombo.getText();
      FieldItem fieldItem = reviewResource.getFieldItem(fieldItemId);
      this.fieldItemIdFieldItemMap.put(fieldItemId, fieldItem);
      fillItemTable(fieldItemId);
      updateDefaultItems(fieldItemId);
      updateFilterItems(fieldItemId);
    }
  }

  /**
   * Updates the filter item folder.
   * 
   * @param fieldItemId the field item id.
   */
  private void updateFilterItems(String fieldItemId) {
    FieldItem fieldItem = this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List<String> itemNameList = fieldItem.getEntryNameList();
      if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_TYPE)) {
        String currentType = this.typeFilterCombo.getText();
        this.typeFilterCombo.setItems(itemNameList.toArray(new String[] {}));
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
   * Updates the default items folder.
   * 
   * @param fieldItemId the field item id.
   */
  private void updateDefaultItems(String fieldItemId) {
    FieldItem fieldItem = this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List<String> itemNameList = fieldItem.getEntryNameList();
      if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_TYPE)) {
        String currentType = this.defaultTypeCombo.getText();
        this.defaultTypeCombo.setItems(itemNameList.toArray(new String[] {}));
        this.defaultTypeCombo.setText(currentType);
      }
      else if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY)) {
        String currentSeverity = this.defaultSeverityCombo.getText();
        this.defaultSeverityCombo.setItems(itemNameList.toArray(new String[] {}));
        this.defaultSeverityCombo.setText(currentSeverity);
      }
      else if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION)) {
        String currentResolution = this.defaultResolutionCombo.getText();
        this.defaultResolutionCombo.setItems(itemNameList.toArray(new String[] {}));
        this.defaultResolutionCombo.setText(currentResolution);
      }
      else if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_STATUS)) {
        String currentStatus = this.defaultStatusCombo.getText();
        this.defaultStatusCombo.setItems(itemNameList.toArray(new String[] {}));
        this.defaultStatusCombo.setText(currentStatus);
      }
    }
  }

  /**
   * Handles the item id combo selection.
   * 
   * @param itemCombo the item combo.
   */
  protected void handleItemIdComboSelection(Combo itemCombo) {
    fillItemTable(itemCombo.getText());
    handleItemListTableSelection();
  }

  /**
   * Handles the item list table selection.
   */
  protected void handleItemListTableSelection() {
    boolean isOneItemEntry = (this.itemListTable.getSelectionCount() == 1);
    int index = this.itemListTable.getSelectionIndex();
    boolean isSelected = (index >= 0);
    this.editButtonInItemEntries.setEnabled(isOneItemEntry);
    this.removeButtonInItemEntries.setEnabled(isSelected);
    this.upButtonInItemEntries.setEnabled((index > 0) && isOneItemEntry);
    int lastIndex = this.itemListTable.getItemCount() - 1;
    this.downButtonInItemEntries.setEnabled((index < lastIndex) && isOneItemEntry);
  }

  /**
   * Fills item data in the item table.
   * 
   * @param fieldItemId the field item id.
   */
  private void fillItemTable(String fieldItemId) {
    removeAllItemsInItemListTable();
    FieldItem fieldItem = this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      for (Iterator<String> i = fieldItem.getEntryNameList().iterator(); i.hasNext();) {
        String itemEntry = ReviewI18n.getString(i.next());
        TableItem item = new TableItem(this.itemListTable, SWT.NONE);
        item.setText(itemEntry);
      }
    }
  }

  /**
   * Removes item data from the item table.
   */
  private void removeAllItemsInItemListTable() {
    TableItem[] items = this.itemListTable.getItems();
    for (int i = 0; i < items.length; i++) {
      TableItem item = items[i];
      item.dispose();
    }
  }

  /**
   * Creates the file folder.
   * 
   * @param folder the folder.
   * @return the control.
   */
  private Control createFileFolder(TabFolder folder) {
    Composite composite = createsGeneralComposite(folder);
    this.fileListTable = new Table(composite, SWT.BORDER | SWT.MULTI);
    FormData fileListTableData = new FormData();
    fileListTableData.left = new FormAttachment(0, 0);
    fileListTableData.right = new FormAttachment(80, 0);
    fileListTableData.top = new FormAttachment(0, 0);
    fileListTableData.bottom = new FormAttachment(100, 0);
    fileListTableData.height = 150;
    fileListTable.setLayoutData(fileListTableData);
    fileListTable.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        handleFileListSelection();
      }
    });
    fillFileTable(this.project.getName(), reviewId.getReviewId());

    this.addButtonInFile = new Button(composite, SWT.PUSH);
    String addKey = "ReviewIdEditDialog.label.tab.file.button.add";
    addButtonInFile.setText(ReviewI18n.getString(addKey));
    addButtonInFile.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        addFile();
      }
    });
    FormData addButtonData = new FormData();
    addButtonData.top = new FormAttachment(fileListTable, 0, SWT.TOP);
    addButtonData.left = new FormAttachment(fileListTable, 10);
    addButtonData.right = new FormAttachment(100, 0);
    addButtonInFile.setLayoutData(addButtonData);

    this.removeButtonInFile = new Button(composite, SWT.PUSH);
    String removeKey = "ReviewIdEditDialog.label.tab.file.button.remove";
    removeButtonInFile.setText(ReviewI18n.getString(removeKey));
    removeButtonInFile.setEnabled(false);
    removeButtonInFile.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        removeFile();
      }
    });
    FormData removeButtonData = new FormData();
    removeButtonData.top = new FormAttachment(addButtonInFile, 5);
    removeButtonData.left = new FormAttachment(addButtonInFile, 0, SWT.LEFT);
    removeButtonData.right = new FormAttachment(100, 0);
    removeButtonInFile.setLayoutData(removeButtonData);

    return composite;
  }

  /**
   * Fills the reviewer table with reviewers. Sets <code>true</code> if items are just
   * updated. Sets <code>false</code> if items are read from the property resource.
   * 
   * @param isUpdate <code>true</code> if items are just updated. <code>false</code> if
   *          items are read from the property resource.
   */
  public void fillReviewerTable(boolean isUpdate) {
    removeAllItemsInReviewerTable();
    if (!isUpdate) {
      IProject project = FileResource.getProject(this.project.getName());
      String reviewIdString = reviewId.getReviewId();
      Map<String, ReviewerId> reviewersMap = PropertyResource.getInstance(project, true)
          .getReviewers(reviewIdString);
      this.reviewers = new TreeMap<String, ReviewerId>(reviewersMap);
    }
    for (Iterator<String> i = this.reviewers.keySet().iterator(); i.hasNext();) {
      String reviewerId = i.next();
      TableItem item = new TableItem(this.reviewerListTable, SWT.NONE);
      item.setText(reviewerId);
    }
  }

  /**
   * Removes all items in the review table.
   */
  private void removeAllItemsInReviewerTable() {
    TableItem[] items = this.reviewerListTable.getItems();
    for (int i = 0; i < items.length; i++) {
      items[i].dispose();
    }
  }

  /**
   * Fills the file table with files.
   * 
   * @param projectName the project name.
   * @param reviewId the review id.
   */
  public void fillFileTable(String projectName, String reviewId) {
    IProject project = FileResource.getProject(projectName);
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    ReviewResource reviewResource = propertyResource.getReviewResource(reviewId, true);
    if (reviewResource != null) {
      Set<String> targetFiles = reviewResource.getFileSet();
      this.files = targetFiles;
      for (Iterator<String> i = this.files.iterator(); i.hasNext();) {
        String file = i.next();
        TableItem item = new TableItem(this.fileListTable, SWT.NONE);
        item.setText(file);
      }
    }
  }

  /**
   * Adds a reviewer to the reviewer list.
   */
  protected void addReviewer() {
    IInputValidator validator = new IInputValidator() {
      public String isValid(String newText) {
        if (!reviewers.containsKey(newText)) {
          return null;
        }
        else {
          String errorKey = "ReviewIdNewReviewerPage.dialogMessage.label.error";
          return ReviewI18n.getString(errorKey);
        }
      }
    };
    InputDialog dialog = new InputDialog(getShell(), ReviewI18n
        .getString("ReviewIdNewReviewerPage.dialogMessage.label.short"), ReviewI18n
        .getString("ReviewIdNewReviewerPage.dialogMessage.label.long"), null, validator); //$NON-NLS-1$ //$NON-NLS-2$
    dialog.open();
    if (dialog.getReturnCode() != InputDialog.OK) {
      return;
    }
    String reviewerId = dialog.getValue();
    ReviewerId reviewer = new ReviewerId(reviewerId, reviewerId);
    this.reviewers.put(reviewerId, reviewer);
    fillReviewerTable(true);
    updateAuthorCandidates(this.reviewers);
  }

  /**
   * Adds a file to the file list.
   */
  protected void addFile() {
    IWorkbench workbench = PlatformUI.getWorkbench();
    Shell shell = workbench.getActiveWorkbenchWindow().getShell();
    FileFolderSelectionDialog dialog = new FileFolderSelectionDialog(shell, true,
        IResource.FILE);
    dialog.setTitle(ReviewI18n.getString("ReviewIdEditDialog.label.tab.file.add.title"));
    dialog.setMessage(ReviewI18n.getString("ReviewIdEditDialog.label.tab.file.add.message"));
    // dialog.setInput(project.getLocation().toFile());
    try {
      IFileStore store = EFS.getStore(project.getLocationURI());
      dialog.setInput(store);
    }
    catch (Exception e) {
      e.printStackTrace();
      // Ignore
    }
    if (dialog.open() == FileFolderSelectionDialog.OK) {
      Object[] results = (Object[]) dialog.getResult();
      for (int i = 0; i < results.length; i++) {
        LocalFile file = (LocalFile) results[i];
        String filePath = file.toString();
        String projectPath = project.getLocation().toFile().toString();
        int index = projectPath.length();
        String projectToFilePath = filePath.substring(index + 1);
        String targetFile = project.getFile(projectToFilePath).getProjectRelativePath()
            .toString();
        if (this.files.add(targetFile)) {
          TableItem item = new TableItem(this.fileListTable, SWT.NONE);
          item.setText(targetFile);
        }
      }
    }
  }

  /**
   * Updates the author candidate list.
   * 
   * @param reviewers the reviewers which would be the candidates for the author.
   */
  private void updateAuthorCandidates(Map<String, ReviewerId> reviewers) {
    authorCombo.setItems(reviewers.keySet().toArray(new String[] {}));
    authorCombo.setText(reviewId.getAuthor());
  }

  /**
   * Removes a reviewer form the reviewer list.
   */
  private void removeReviewer() {
    TableItem[] items = this.reviewerListTable.getSelection();
    for (int i = 0; i < items.length; i++) {
      TableItem item = items[i];
      this.reviewers.remove(item.getText());
      item.dispose();
    }
    fillReviewerTable(true);
    this.removeButtonInReviewer.setEnabled(this.reviewerListTable.getItemCount() > 0);
  }

  /**
   * Removes a file from the file list.
   */
  protected void removeFile() {
    TableItem[] items = this.fileListTable.getSelection();
    for (int i = 0; i < items.length; i++) {
      TableItem item = items[i];
      this.files.remove(item.getText());
      item.dispose();
    }
    this.removeButtonInFile.setEnabled(this.fileListTable.getItemCount() > 0);
  }

  /**
   * Handles the review list table selection.
   */
  private void handleReviewListSelection() {
    boolean isSelected = (this.reviewerListTable.getSelectionIndex() >= 0);
    this.addButtonInReviewer.setEnabled(isSelected);
    this.removeButtonInReviewer.setEnabled(isSelected);
  }

  /**
   * Handles the file list table selection.
   */
  protected void handleFileListSelection() {
    boolean isSelected = (this.fileListTable.getSelectionIndex() >= 0);
    this.addButtonInFile.setEnabled(isSelected);
    this.removeButtonInFile.setEnabled(isSelected);
  }

  /**
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  protected void okPressed() {
    this.reviewId.setDescription(ReviewI18n.getKey(this.reviewIdDescriptionText.getText()));
    this.reviewId.setReviewers(this.reviewers);
    this.reviewId.setAuthor(this.authorCombo.getText());
    this.reviewId.setDirectory(this.storageText.getText());
    try {
      PropertyResource propertyResource = PropertyResource.getInstance(this.project, true);
      String reviewIdString = this.reviewId.getReviewId();
      ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);
      if (reviewResource != null) {
        reviewResource.setReviewId(this.reviewId);
        reviewResource.setTargetFiles(this.files);
        String typeKey = ReviewI18n.getKey(this.defaultTypeCombo.getText());
        reviewResource.setDefaultField(PropertyConstraints.ATTRIBUTE_VALUE_TYPE, typeKey);
        String severityKey = ReviewI18n.getKey(this.defaultSeverityCombo.getText());
        reviewResource.setDefaultField(PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY,
            severityKey);
        String resolutionKey = ReviewI18n.getKey(this.defaultResolutionCombo.getText());
        String resolutionName = PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION;
        reviewResource.setDefaultField(resolutionName, resolutionKey);
        String statusKey = ReviewI18n.getKey(this.defaultStatusCombo.getText());
        reviewResource.setDefaultField(PropertyConstraints.ATTRIBUTE_VALUE_STATUS, statusKey);
        reviewResource.setFieldItemMap(this.fieldItemIdFieldItemMap);
        reviewResource.setPhaseNameFilterPhaseMap(this.phaseNameFilterPhaseMap);

        propertyResource.removeReviewResource(this.reviewId);
        propertyResource.addReviewResource(reviewResource);
      }
    }
    catch (ReviewException e) {
      log.error(e);
    }
    super.okPressed();
  }
}
