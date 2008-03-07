package edu.hawaii.ics.csdl.jupiter.ui.view.editor;

import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.TextActionHandler;
import org.eclipse.ui.part.ViewPart;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.event.EventFileManager;
import edu.hawaii.ics.csdl.jupiter.event.ReviewEvent;
import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
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
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableView;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableViewAction;
import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;
import edu.hawaii.ics.csdl.jupiter.util.TabFolderLayout;


/**
 * Provides Jupiter editor view to edit an issue.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewEditorView extends ViewPart {
  private static final String EDITOR_VIEW_ID = "edu.hawaii.ics.csdl.jupiter.ui.view.editor";
  private static ReviewEditorView theInstance;
  private Text descriptionText;
  private Text summaryText;
  private Combo typeCombo;
  private Combo severityCombo;
  private Combo assignedToCombo;
  private Combo resolutionCombo;
  private Text annotationText;
  private Text teamDescriptionText;
  private Combo statusCombo;
  private Text revisionText;
  private Text reworkDescriptionText;
  private ReviewIssue reviewIssue;
  private String individualTitle = "";
  private TabFolder folder;
  private Label assignedToLabel;
  private Label statusLabel;
  private Text resolutionText;
  private String activeTabNameKey = ResourceBundleKey.PHASE_INFIVIDUAL;
  private ReviewIssueModel reviewIssueModel;
  /** The listener to listen to the part change. */
  private ReviewEditorViewPartListenerAdapter partListener;
  private Composite ancestor;
  
  /**
   * Instantiates this instance for the Eclipse platform. Clients should not call this. 
   * Instead, call <code>getInstance</code>.
   */
  public ReviewEditorView() {
    theInstance = this;
  }
  
  /**
   * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite)
   */
  public void init(IViewSite site) throws PartInitException {
    super.init(site);
    this.partListener = new ReviewEditorViewPartListenerAdapter();
    ReviewIssueModelManager reviewIssueModelManager = ReviewIssueModelManager.getInstance();
    this.reviewIssueModel = reviewIssueModelManager.getCurrentModel();
  }
  
  /**
   * Gets the singleton instance.
   * @return the singleton instance.
   */
  public static ReviewEditorView getInstance() {
    return theInstance;
  }


  /**
   * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
   */
  public void createPartControl(Composite ancestor) {
    this.ancestor = ancestor;
    IWorkbenchPage page = getSite().getPage();
    page.addPartListener(this.partListener);
    Composite dialog = createGeneralComposite(ancestor, 0, 0);
    this.folder = createTabFolder(dialog);
    createIndividualPhaseTabContent(folder);
    createTeamPhaseTabContent(folder);
    createReworkPhaseTabContent(folder);
    hookListenerOnTabFolder(folder);
    hookTextActionHandler();
    createActions();
    this.setEnable(false);
  }
  
  /**
   * Hooks listener on tab folder.
   * @param folder the folder
   */
  private void hookListenerOnTabFolder(TabFolder folder) {
    folder.setData(folder);
    folder.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        TabFolder folder = (TabFolder) event.widget.getData();
        setActiveTabNameKey(folder);
        updateTitle();
        handleTabChangeEvent();
      }
    });
  }
  
  /**
   * Sets the active tab name key.
   * @param folder the tab folder.
   */
  private void setActiveTabNameKey(TabFolder folder) {
    TabItem tabItem = folder.getItem(folder.getSelectionIndex());
    this.activeTabNameKey = ReviewI18n.getKey(tabItem.getText());
  }
  
  /**
   * Gets the active tab name.
   * @return the active tab name.
   */
  public String getActiveTabNameKey() {
   return this.activeTabNameKey;
  }

  /**
   * Creates view preference frame and return the child composite.
   * @param parent the parent composite.
   * @param height the margin height.
   * @param width the margin width.
   * @return the child composite.
   */
  private Composite createGeneralComposite(Composite parent, int height, int width) {
    Composite child = new Composite(parent, SWT.LEFT);
    FormLayout layout = new FormLayout();
    layout.marginHeight = height;
    layout.marginWidth = width;
    child.setLayout(layout);
    return child;
  }
  
  /**
   * Creates tab folder.
   * @param dialog the composite.
   * @return the <code>TabFolder</code> instance.
   */
  private TabFolder createTabFolder(Composite dialog) {
    TabFolder folder = new TabFolder(dialog, SWT.BOTTOM);
    folder.setLayout(new TabFolderLayout());
    FormData folderData = new FormData();
    folderData.top = new FormAttachment(0, -3);
    folderData.left = new FormAttachment(0, -2);
    folderData.right = new FormAttachment(100, +2);
    folderData.bottom = new FormAttachment(100, 0);
    folder.setLayoutData(folderData);
    return folder;
  }
  
  /**
   * Creates actions.
   */
  private void createActions() {
    IActionBars bars = getViewSite().getActionBars();
    fillLocalToolBar(bars.getToolBarManager());
  }
  
  /**
   * Fills the tool bar menu. Sub extending class should implements this.
   *
   * @param manager The <code>IMenuManager</code> instance.
   */
  private void fillLocalToolBar(IToolBarManager manager) {
//    manager.add(ReviewEditorViewAction.UNDO);
    manager.add(ReviewEditorViewAction.GOTO);
    manager.add(new Separator("Additions"));
    manager.add(ReviewEditorViewAction.NEXT);
    manager.add(ReviewEditorViewAction.PREVIOUS);
    manager.add(new Separator("Additions"));
    manager.add(ReviewEditorViewAction.SAVE);
    manager.add(ReviewEditorViewAction.CLEAR);
  }

  /**
   * Hooks text action handler.
   */
  private void hookTextActionHandler() {
    TextActionHandler textActionHandler = new TextActionHandler(getViewSite().getActionBars());
    textActionHandler.addText(this.summaryText);
    textActionHandler.addText(this.descriptionText);
    textActionHandler.addText(this.annotationText);
    textActionHandler.addText(this.revisionText);
    textActionHandler.addText(this.teamDescriptionText);
    textActionHandler.addText(this.reworkDescriptionText);
    getViewSite().getActionBars().setGlobalActionHandler("reviewSave", ReviewEditorViewAction.SAVE);
//    RetargetAction action = new RetargetAction();
//    getViewSite().getPage().getWorkbenchWindow().getPartService().
//    addPartListener(ReviewEditorViewAction.SAVE);
  }
  
  /**
   * Creates individual phase tab content in the <code>TabFolder</code>.
   * @param folder the <code>TabFolder</code> instance.
   */
  private void createIndividualPhaseTabContent(TabFolder folder) {
    TabItem individualTabItem = new TabItem(folder, SWT.NONE);
    String individualKey = ResourceBundleKey.PHASE_INFIVIDUAL;
    String individualLabel = ReviewI18n.getString(individualKey);
    individualTabItem.setText(individualLabel);
    Composite composite = createGeneralComposite(folder, 5, 5);
    createTypeSeverityContent(composite);
    createSummaryContent(composite);
    createIndividaulDescriptionContent(composite); 
    individualTabItem.setControl(composite);   
  }
  
  /**
   * Creates type content.
   * @param parent the parent composite
   */
  private void createTypeSeverityContent(Composite parent) {    
    Label typeLabel = new Label(parent, SWT.NONE);
    typeLabel.setText(ReviewI18n.getString("ReviewEditorView.label.type"));
    this.typeCombo = new Combo(parent, SWT.READ_ONLY);
    FormData typeLabelData = new FormData();
    typeLabelData.top = new FormAttachment(typeCombo, 0, SWT.CENTER);
    typeLabel.setLayoutData(typeLabelData);
    FormData typeComboData = new FormData();
    typeComboData.left = new FormAttachment(typeLabel, 75, SWT.LEFT);
    typeComboData.right = new FormAttachment(50, 0);
    typeCombo.setLayoutData(typeComboData);
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    String[] typeItems = TypeKeyManager.getInstance(project, reviewId).getElements();
    typeCombo.setItems(typeItems);
    typeCombo.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
        int type = ReviewEvent.TYPE_FOCUS;
        int kind = ReviewEvent.KIND_EDITOR;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public void focusLost(FocusEvent event) {
      }     
    });

    Label severityLabel = new Label(parent, SWT.NONE);
    severityLabel.setText(ReviewI18n.getString("ReviewEditorView.label.severity"));
    this.severityCombo = new Combo(parent, SWT.READ_ONLY);
    FormData severityLabelData = new FormData();
    severityLabelData.left = new FormAttachment(typeCombo, 10);
    severityLabelData.top = new FormAttachment(typeCombo, 0, SWT.CENTER);
    severityLabel.setLayoutData(severityLabelData);
    FormData severityComboData = new FormData();
    severityComboData.top = new FormAttachment(typeCombo, 0, SWT.CENTER);
    severityComboData.left = new FormAttachment(severityLabel, 15);
    severityComboData.right = new FormAttachment(100, 0);
    severityCombo.setLayoutData(severityComboData);
    String[] severityItems = SeverityKeyManager.getInstance(project, reviewId).getElements();
    severityCombo.setItems(severityItems);
    severityCombo.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
        int type = ReviewEvent.TYPE_FOCUS;
        int kind = ReviewEvent.KIND_EDITOR;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public void focusLost(FocusEvent event) {
      }     
    });
  }
  
  /**
   * Creates summary content.
   * @param parent the composite.
   */
  private void createSummaryContent(Composite parent) {
    Label summaryLabel = new Label(parent, SWT.NONE);
    summaryLabel.setText(ReviewI18n.getString("ReviewEditorView.label.summary"));
    this.summaryText = new Text(parent, SWT.SINGLE | SWT.BORDER);
    FormData summaryLabelData = new FormData();
    summaryLabelData.top = new FormAttachment(summaryText, 0, SWT.CENTER);
    summaryLabel.setLayoutData(summaryLabelData);
    FormData summaryTextData = new FormData();
    summaryTextData.width = 350;
    summaryTextData.top = new FormAttachment(typeCombo, 5);
    summaryTextData.left = new FormAttachment(typeCombo, 0, SWT.LEFT);
    summaryTextData.right = new FormAttachment(100, 0);
    summaryText.setLayoutData(summaryTextData);
    summaryText.setText("");
    summaryText.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
        int type = ReviewEvent.TYPE_FOCUS;
        int kind = ReviewEvent.KIND_EDITOR;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public void focusLost(FocusEvent event) {
      }     
    });
  }
  
  /**
   * Creates description content.
   * @param parent the composite.
   */
  private void createIndividaulDescriptionContent(Composite parent) {
    Label descriptionLabel = new Label(parent, SWT.NONE);
    descriptionLabel.setText(ReviewI18n.getString("ReviewEditorView.label.description"));
    this.descriptionText = new Text(parent, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
    FormData descriptionLabelData = new FormData();
    descriptionLabelData.top = new FormAttachment(descriptionText, 0, SWT.TOP);
    descriptionLabel.setLayoutData(descriptionLabelData);    
    
    FormData descriptionTextData = new FormData();
    descriptionTextData.top = new FormAttachment(summaryText, 5);
    descriptionTextData.left = new FormAttachment(typeCombo, 0, SWT.LEFT);
    descriptionTextData.right = new FormAttachment(100, 0);
    descriptionTextData.bottom = new FormAttachment(100, 0);
    descriptionText.setLayoutData(descriptionTextData);
    descriptionText.setText("");
    descriptionText.setData(descriptionText);
    descriptionText.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
        int type = ReviewEvent.TYPE_FOCUS;
        int kind = ReviewEvent.KIND_EDITOR;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public void focusLost(FocusEvent event) {
      }     
    });
  }
  
  /**
   * Creates team phase tab content in the <code>TabFolder</code>.
   * @param folder the <code>TabFolder</code> instance.
   */
  private void createTeamPhaseTabContent(TabFolder folder) {
    TabItem teamTabItem = new TabItem(folder, SWT.NONE);
    String teamKey = ResourceBundleKey.PHASE_TEAM;
    String teamLabel = ReviewI18n.getString(teamKey);
    teamTabItem.setText(teamLabel);
    Composite composite = createGeneralComposite(folder, 5, 5);
    createAssignedToResolutionContent(composite);
    createAnnotationContent(composite);
    createTeamDescriptionContent(composite); 
    teamTabItem.setControl(composite);
  }
  
  /**
   * Creates assigned to and resolution content.
   * @param parent the parent composite
   */
  private void createAssignedToResolutionContent(Composite parent) {
    this.assignedToLabel = new Label(parent, SWT.NONE);
    assignedToLabel.setText(ReviewI18n.getString("ReviewEditorView.label.assignedTo"));
    this.assignedToCombo = new Combo(parent, SWT.READ_ONLY);
    FormData assignedToLabelData = new FormData();
    assignedToLabelData.top = new FormAttachment(assignedToCombo, 0, SWT.CENTER);
    assignedToLabel.setLayoutData(assignedToLabelData);
    FormData assginedToComboData = new FormData();
    assginedToComboData.left = new FormAttachment(assignedToLabel, 75, SWT.LEFT);
    assginedToComboData.right = new FormAttachment(50, 0);
    assignedToCombo.setLayoutData(assginedToComboData);
    assignedToCombo.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
        int type = ReviewEvent.TYPE_FOCUS;
        int kind = ReviewEvent.KIND_EDITOR;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public void focusLost(FocusEvent event) {
      }     
    });
    ReviewModel reviewModel = ReviewModel.getInstance();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    IProject project = reviewModel.getProjectManager().getProject();
    if (reviewId != null) {
      String reviewIdString = reviewId.getReviewId();
      PropertyResource reviewIdResource = PropertyResource.getInstance(project, false);
      String[] items = reviewIdResource.getReviewerIdNames(reviewIdString);
      assignedToCombo.setItems((items == null) ? new String[] {} : items);
      reviewModel = ReviewModel.getInstance();
      String reviewerId = reviewModel.getReviewerIdManager().getReviewerId().getReviewerId();
      assignedToCombo.setText(reviewerId);
    }
      
    Label resolutionLabel = new Label(parent, SWT.NONE);
    resolutionLabel.setText(ReviewI18n.getString("ReviewEditorView.label.resolution"));
    this.resolutionCombo = new Combo(parent, SWT.READ_ONLY);
    FormData resolutionLabelData = new FormData();
    resolutionLabelData.left = new FormAttachment(assignedToCombo, 10);
    resolutionLabelData.top = new FormAttachment(assignedToCombo, 0, SWT.CENTER);
    resolutionLabel.setLayoutData(resolutionLabelData);
    FormData resolutionComboData = new FormData();
    resolutionComboData.top = new FormAttachment(assignedToCombo, 0, SWT.CENTER);
    resolutionComboData.left = new FormAttachment(resolutionLabel, 15);
    resolutionComboData.right = new FormAttachment(100, 0);
    resolutionCombo.setLayoutData(resolutionComboData);
    String[] resolutionItems = ResolutionKeyManager.getInstance(project, reviewId).getElements();
    resolutionCombo.setItems(resolutionItems);
    resolutionCombo.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
        int type = ReviewEvent.TYPE_FOCUS;
        int kind = ReviewEvent.KIND_EDITOR;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public void focusLost(FocusEvent event) {
      }     
    });
  }
  
  /**
   * Creates annotation content.
   * @param parent the parent composite
   */
  private void createAnnotationContent(Composite parent) {
    Label annotationLabel = new Label(parent, SWT.NONE);
    annotationLabel.setText(ReviewI18n.getString("ReviewEditorView.label.annotation"));
    this.annotationText = new Text(parent, SWT.SINGLE | SWT.BORDER);
    FormData annotationLabelData = new FormData();
    annotationLabelData.top = new FormAttachment(annotationText, 0, SWT.CENTER);
    annotationLabel.setLayoutData(annotationLabelData);
    FormData annotationTextData = new FormData();
    annotationTextData.width = 350;
    annotationTextData.top = new FormAttachment(assignedToCombo, 5);
    annotationTextData.left = new FormAttachment(assignedToCombo, 0, SWT.LEFT);
    annotationTextData.right = new FormAttachment(100, 0);
    annotationText.setLayoutData(annotationTextData);
    annotationText.setText("");
    annotationText.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
        int type = ReviewEvent.TYPE_FOCUS;
        int kind = ReviewEvent.KIND_EDITOR;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public void focusLost(FocusEvent event) {
      }     
    });
  }
  
  /**
   * Creates team description content.
   * @param parent the parent composite
   */
  private void createTeamDescriptionContent(Composite parent) {
    Group teamDescriptionGroup = new Group(parent, SWT.NONE);
    FormLayout teamDescriptionGroupLayout = new FormLayout();
    teamDescriptionGroupLayout.marginWidth = 0;
    teamDescriptionGroupLayout.marginHeight = 0;
    teamDescriptionGroup.setLayout(teamDescriptionGroupLayout);
    FormData teamDescriptionGroupData = new FormData();
    teamDescriptionGroupData.top = new FormAttachment(annotationText, 0);
    teamDescriptionGroupData.left = new FormAttachment(assignedToLabel, 0, SWT.LEFT);
    teamDescriptionGroupData.right = new FormAttachment(100, 0);
    teamDescriptionGroupData.bottom = new FormAttachment(100, 0);
    teamDescriptionGroup.setLayoutData(teamDescriptionGroupData);
    this.teamDescriptionText = new Text(teamDescriptionGroup, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
    FormData teamDescriptionTextData = new FormData();
    teamDescriptionTextData.top = new FormAttachment(0, 0);
    teamDescriptionTextData.left = new FormAttachment(0, 0);
    teamDescriptionTextData.right = new FormAttachment(100, 0);
    teamDescriptionTextData.bottom = new FormAttachment(100, 0);
    teamDescriptionText.setLayoutData(teamDescriptionTextData);
    teamDescriptionText.setEditable(false);
  }

  /**
   * Creates rework phase tab content in the <code>TabFolder</code>.
   * @param folder the <code>TabFolder</code> instance.
   */
  private void createReworkPhaseTabContent(TabFolder folder) {
    TabItem reworkTabItem = new TabItem(folder, SWT.NONE);
    String reworkKey = ResourceBundleKey.PHASE_REWORK;
    String reworkLabel = ReviewI18n.getString(reworkKey);
    reworkTabItem.setText(reworkLabel);
    Composite composite = createGeneralComposite(folder, 5, 5);
    createStatusContent(composite);
    createRevisionContent(composite);
    createReworkDescriptionContent(composite); 
    reworkTabItem.setControl(composite);   
  }
 
  /**
   * Creates status content.
   * @param parent the parent composite
   */
  private void createStatusContent(Composite parent) {
    this.statusLabel = new Label(parent, SWT.NONE);
    statusLabel.setText(ReviewI18n.getString("ReviewEditorView.label.status"));
    this.statusCombo = new Combo(parent, SWT.READ_ONLY);
    FormData statusLabelData = new FormData();
    statusLabelData.top = new FormAttachment(statusCombo, 0, SWT.CENTER);
    statusLabel.setLayoutData(statusLabelData);
    FormData statusComboData = new FormData();
    statusComboData.left = new FormAttachment(statusLabel, 75, SWT.LEFT);
    statusComboData.right = new FormAttachment(50, 0);
    statusCombo.setLayoutData(statusComboData);
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    String[] statusItems = StatusKeyManager.getInstance(project, reviewId).getElements();
    statusCombo.setItems(statusItems);
    statusCombo.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
        int type = ReviewEvent.TYPE_FOCUS;
        int kind = ReviewEvent.KIND_EDITOR;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public void focusLost(FocusEvent event) {
      }     
    });
    
    Label resolutionLabel = new Label(parent, SWT.NONE);
    resolutionLabel.setText(ReviewI18n.getString("ReviewEditorView.label.resolution"));
    this.resolutionText = new Text(parent, SWT.READ_ONLY | SWT.BORDER);
    FormData resolutionLabelData = new FormData();
    resolutionLabelData.left = new FormAttachment(statusCombo, 10);
    resolutionLabelData.top = new FormAttachment(statusCombo, 0, SWT.CENTER);
    resolutionLabel.setLayoutData(resolutionLabelData);
    FormData resolutionComboData = new FormData();
    resolutionComboData.top = new FormAttachment(statusCombo, 0, SWT.CENTER);
    resolutionComboData.left = new FormAttachment(resolutionLabel, 15);
    resolutionComboData.right = new FormAttachment(100, 0);
    resolutionText.setLayoutData(resolutionComboData);
    String[] resolutionItems = ResolutionKeyManager.getInstance(project, reviewId).getElements();
  }
  
  /**
   * Creates revision content.
   * @param parent the parent composite
   */
  private void createRevisionContent(Composite parent) {
    Label revisionLabel = new Label(parent, SWT.NONE);
    revisionLabel.setText(ReviewI18n.getString("ReviewEditorView.label.revision"));
    this.revisionText = new Text(parent, SWT.SINGLE | SWT.BORDER);
    FormData revisionLabelData = new FormData();
    revisionLabelData.top = new FormAttachment(revisionText, 0, SWT.CENTER);
    revisionLabel.setLayoutData(revisionLabelData);
    FormData revisionTextData = new FormData();
    revisionTextData.width = 350;
    revisionTextData.top = new FormAttachment(statusCombo, 5);
    revisionTextData.left = new FormAttachment(statusCombo, 0, SWT.LEFT);
    revisionTextData.right = new FormAttachment(100, 0);
    revisionText.setLayoutData(revisionTextData);
    revisionText.setText("");
    revisionText.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent event) {
        int type = ReviewEvent.TYPE_FOCUS;
        int kind = ReviewEvent.KIND_EDITOR;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public void focusLost(FocusEvent event) {
      }     
    });
  }
  
  /**
   * Creates rework description content.
   * @param parent the parent composite
   */
  private void createReworkDescriptionContent(Composite parent) {
    Group reworkDescriptionGroup = new Group(parent, SWT.NONE);
    FormLayout reworkDescriptionGroupLayout = new FormLayout();
    reworkDescriptionGroupLayout.marginWidth = 0;
    reworkDescriptionGroupLayout.marginHeight = 0;
    reworkDescriptionGroup.setLayout(reworkDescriptionGroupLayout);
    FormData reworkDescriptionGroupData = new FormData();
    reworkDescriptionGroupData.top = new FormAttachment(revisionText, 0);
    reworkDescriptionGroupData.left = new FormAttachment(statusLabel, 0, SWT.LEFT);
    reworkDescriptionGroupData.right = new FormAttachment(100, 0);
    reworkDescriptionGroupData.bottom = new FormAttachment(100, 0);
    reworkDescriptionGroup.setLayoutData(reworkDescriptionGroupData);
    int reworkDescriptionTextStyle = SWT.MULTI | SWT.V_SCROLL | SWT.WRAP;
    this.reworkDescriptionText = new Text(reworkDescriptionGroup, reworkDescriptionTextStyle);
    FormData reworkDescriptionTextData = new FormData();
    reworkDescriptionTextData.top = new FormAttachment(0, 0);
    reworkDescriptionTextData.left = new FormAttachment(0, 0);
    reworkDescriptionTextData.right = new FormAttachment(100, 0);
    reworkDescriptionTextData.bottom = new FormAttachment(100, 0);
    reworkDescriptionText.setLayoutData(reworkDescriptionTextData);
    reworkDescriptionText.setEditable(false);
  }
  
  /**
   * Handles the tab change event.
   */
  protected void handleTabChangeEvent() {
    String description = this.descriptionText.getText();
    this.teamDescriptionText.setText(description);
    String newDescription = createAnnotationDescription(annotationText.getText(), description);
    this.reworkDescriptionText.setText(newDescription);
    this.resolutionText.setText(this.resolutionCombo.getText());
  }
  
  /**
   * Creates the formatted annotation and description text.
   * @param annotation the annotation.
   * @param description the description.
   * @return the formatted annotation and description text.
   */
  private String createAnnotationDescription(String annotation, String description) {
    annotation = annotation.trim();
    if (!annotation.equals("")) {
      annotation = ReviewI18n.getString("ReviewEditorView.label.annotation.prefix")
                   + annotation
                   + ReviewI18n.getString("ReviewEditorView.label.annotation.suffix");
    }
    return annotation + description;
  }

  /**
   * Sets focus on a specific field depending upon a phase. If the phase is individual mode,
   * the type combo will be focused. If it is team mode, the assigned to combo will be focused.
   * If it is rework mode, the status combo will be focused.
   * 
   * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
   */
  public void setFocus() {
    String phaseNameKey = ReviewModel.getInstance().getPhaseManager().getPhaseNameKey();
    if (phaseNameKey.equals(ResourceBundleKey.PHASE_INFIVIDUAL)) {
      this.typeCombo.setFocus();
    }
    else if (phaseNameKey.equals(ResourceBundleKey.PHASE_TEAM)) {
      this.assignedToCombo.setFocus();
    }
    else if (phaseNameKey.equals(ResourceBundleKey.PHASE_REWORK)) {
      this.statusCombo.setFocus();
    }
  }
  
  /**
   * Brings the code review view to the top no matter when review view is closed or not. Means that
   * it is opened if it has not been opened yet, or it is brought to top if it already has been
   * opened, but not brought to top.
   * 
   * @return the <code>ReviewTableView</code> instance to be opened and/or bright to top.
   * @throws ReviewException if part could not be initialized.
   */
  public static ReviewEditorView bringViewToTop() throws ReviewException {
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
    IViewPart viewPart = page.findView(EDITOR_VIEW_ID);
    // if  the review view is not showed yet,
    if (viewPart == null) {
      try {
        viewPart = page.showView(EDITOR_VIEW_ID);
      }
      catch (PartInitException e) {
        throw new ReviewException(e.getMessage());
      }
    }
    // if there exists the view, but if not on the top,
    // then brings it to top when the view is already showed.
    else if (!page.isPartVisible(viewPart)) {
      page.bringToTop(viewPart);
    }
    if (viewPart instanceof ReviewEditorView) {
      return (ReviewEditorView) viewPart;
    }
    else {
      throw new ReviewException("The instance is not ReviewEditorView.");
    }
  }
  
  /**
   * Provides the active view of the Review editor view. This active review editor view
   * associates with the active window. Returns <code>null</code> if there is no active
   * review editor view in the active window.
   * @return the active review editor view. Returns <code>null</code> if there is no active
   * review editor view in the active window.
   */
  public static ReviewEditorView getActiveView() {
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
    IViewPart viewPart = page.findView(EDITOR_VIEW_ID);
    return (ReviewEditorView) viewPart;
  }
  
  /**
   * Provides the array of the Review editor view. A review editor view in the array
   * associates with the opened window. The array might contain <code>null</code> 
   * if there is no active review editor view in the active window.
   * @return the array of the review editor views. 
   */
  public static ReviewEditorView[] getViews() {
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
    ReviewEditorView[] views = new ReviewEditorView[windows.length];
    for (int i = 0; i < windows.length; i++) {
      IWorkbenchPage page = windows[i].getActivePage();
      IViewPart viewPart = page.findView(EDITOR_VIEW_ID);
      views[i] = (ReviewEditorView) viewPart;
    }
    return views;
  }
  
  /**
   * Hides this view part.
   */
  public static void hideView() {
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow window = workbench.getWorkbenchWindows()[0];
    if (window != null) {
      final IWorkbenchPage page = window.getActivePage();
      if (page != null) {
        final IViewPart viewPart = page.findView(EDITOR_VIEW_ID);
        if (viewPart != null) {
          Display.getDefault().asyncExec(new Runnable() {
            public void run() {
              page.hideView(viewPart);
            }
          });
        }
      }
    }
  }
  
  /**
   * Sets the enable status of the editor.
   * @param isEnable set <code>true</code> if all the fields are enable.
   */
  public static void setViewEnable(final boolean isEnable) {
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow window = workbench.getWorkbenchWindows()[0];
    if (window != null) {
      final IWorkbenchPage page = window.getActivePage();
      if (page != null) {
        Display.getDefault().asyncExec(new Runnable() {
          public void run() {
            IViewPart viewPart = page.findView(EDITOR_VIEW_ID);
            if (viewPart != null && viewPart instanceof ReviewEditorView) {
              ((ReviewEditorView) viewPart).setEnable(isEnable);
            }
          }
        });
      }
    }
  }
    
  /**
   * Sets the enable status of this editor.
   * @param isEnabled set <code>true</code> if all the fields are enable.
   */
  public void setEnable(boolean isEnabled) {
    // set fields.
    this.typeCombo.setEnabled(isEnabled);
    this.severityCombo.setEnabled(isEnabled);
    this.summaryText.setEnabled(isEnabled);
    this.descriptionText.setEnabled(isEnabled);
    this.assignedToCombo.setEnabled(isEnabled);
    this.resolutionCombo.setEnabled(isEnabled);
    this.annotationText.setEnabled(isEnabled);
    this.statusCombo.setEnabled(isEnabled);
    this.resolutionText.setEnabled(isEnabled);
    this.revisionText.setEnabled(isEnabled);
    // set action icons.
//    ReviewTableViewAction.GOTO.setEnabled(isEnabled);
    ReviewTableViewAction.EDIT.setEnabled(isEnabled);
    ReviewTableViewAction.DELETE.setEnabled(isEnabled);
//    ReviewTableViewAction.FILTER.setEnabled(isEnabled);
    ReviewTableViewAction.PROPERTY_SETTING.setEnabled(isEnabled);
    ReviewEditorViewAction.UNDO.setEnabled(isEnabled);
    ReviewEditorViewAction.GOTO.setEnabled(isEnabled);
    ReviewEditorViewAction.NEXT.setEnabled(isEnabled);
    ReviewEditorViewAction.PREVIOUS.setEnabled(isEnabled);
    ReviewEditorViewAction.SAVE.setEnabled(isEnabled);
    ReviewEditorViewAction.CLEAR.setEnabled(isEnabled);    
  }
  
  /**
   * Sets the item fields.
   * @param project the project.
   * @param reviewId the review id.
   */
  public void setItemFields(IProject project, ReviewId reviewId) {
    PropertyResource propertyResource = PropertyResource.getInstance(project, false);
    String reviewIdString = reviewId.getReviewId();
    ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);
    this.typeCombo.setItems(TypeKeyManager.getInstance(project, reviewId).getElements());
    String typeName = PropertyConstraints.ATTRIBUTE_VALUE_TYPE;
    String typeDefaultKey = (reviewResource != null) ? reviewResource.getDefaultField(typeName)
                                                     : "";
    this.typeCombo.setText(ReviewI18n.getString(typeDefaultKey));
    this.severityCombo.setItems(SeverityKeyManager.getInstance(project, reviewId).getElements());
    String severityName = PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY;
    String severityDefaultKey = (reviewResource != null) 
                                 ? reviewResource.getDefaultField(severityName) : "";
    this.severityCombo.setText(ReviewI18n.getString(severityDefaultKey));
    PropertyResource reviewIdResource = PropertyResource.getInstance(project, false);
    String[] items = reviewIdResource.getReviewerIdNames(reviewId.getReviewId());
    this.assignedToCombo.setItems((items == null) ? new String[] {} : items);
    String author = ReviewModel.getInstance().getReviewIdManager().getReviewId().getAuthor();
    assignedToCombo.setText(author);
    ResolutionKeyManager resolutionKeyManager = ResolutionKeyManager.getInstance(project, reviewId);
    this.resolutionCombo.setItems(resolutionKeyManager.getElements());
    String resolutionName = PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION;
    String resolutionDefaultKey = (reviewResource != null) 
                                   ? reviewResource.getDefaultField(resolutionName) : "";
    this.resolutionCombo.setText(ReviewI18n.getString(resolutionDefaultKey));
    this.statusCombo.setItems(StatusKeyManager.getInstance(project, reviewId).getElements());
    String statusConnstraint = PropertyConstraints.ATTRIBUTE_VALUE_STATUS;
    String statusDefaultKey = (reviewResource != null)
                               ? reviewResource.getDefaultField(statusConnstraint) : "";
    this.statusCombo.setText(ReviewI18n.getString(statusDefaultKey));
  }

  /**
   * Sets <code>ReviewIssue</code> instance.
   * @param reviewIssue the code review instance.
   */
  public void setReviewIssue(ReviewIssue reviewIssue) {
    if (!this.ancestor.isDisposed()) {
      this.reviewIssue = reviewIssue;
      ReviewModel reviewModel = ReviewModel.getInstance();
      IProject project = reviewModel.getProjectManager().getProject();
      ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
      TypeKeyManager typeKeyManager = TypeKeyManager.getInstance(project, reviewId);
      String localizedTypeText = typeKeyManager.getLocalizedLabel(reviewIssue.getType().getKey());
      this.typeCombo.select(typeCombo.indexOf(localizedTypeText));
      SeverityKeyManager severityKeyManager = SeverityKeyManager.getInstance(project, reviewId);
      String severityKey = reviewIssue.getSeverity().getKey();
      String localizedSeverityText = severityKeyManager.getLocalizedLabel(severityKey);
      this.severityCombo.select(severityCombo.indexOf(localizedSeverityText));
      this.summaryText.setText(reviewIssue.getSummary());
      String description = reviewIssue.getDescription();
      this.descriptionText.setText(description);
      this.teamDescriptionText.setText(description);
      String newDescription = createAnnotationDescription(reviewIssue.getAnnotation(), description);
      this.reworkDescriptionText.setText(newDescription);
      this.annotationText.setText(reviewIssue.getAnnotation());
      this.revisionText.setText(reviewIssue.getRevision());
      if (reviewId != null) {
        // if no assigned to person was set yet, then it would be set.
        String reviewIdString = reviewId.getReviewId();
        PropertyResource reviewIdResource = PropertyResource.getInstance(project, false);
        String[] items = reviewIdResource.getReviewerIdNames(reviewIdString);
        assignedToCombo.setItems((items == null) ? new String[] {} : items);
        String assginedTo = reviewIssue.getAssignedTo();
        if (assginedTo.equals("")) {
          this.assignedToCombo.setText(reviewId.getAuthor());
        }
        else {
          this.assignedToCombo.setText(reviewIssue.getAssignedTo());
        }
      }
      ResolutionKeyManager resolutionKeyManager = ResolutionKeyManager.getInstance(project, 
                                                                                   reviewId);
      String resolutionKey = reviewIssue.getResolution().getKey();
      String localizedResolutionText = resolutionKeyManager.getLocalizedLabel(resolutionKey);
      this.resolutionCombo.select(resolutionCombo.indexOf(localizedResolutionText));
      this.resolutionText.setText(localizedResolutionText);
      StatusKeyManager statusKeyManager = StatusKeyManager.getInstance(project, reviewId);
      String statusKey = reviewIssue.getStatus().getKey();
      String localizedStatusText = statusKeyManager.getLocalizedLabel(statusKey);
      this.statusCombo.select(statusCombo.indexOf(localizedStatusText));
      this.updateTitle();
      ReviewEditorViewAction.GOTO.setEnabled(reviewIssue.isLinked());
      EventFileManager.getInstance().setEventFilePath(this.reviewIssue.getTargetFile());
    }
  }
  
  /**
   * Gets the code review instance. Returns null if this view instance does not contains
   * <code>ReviewIssue</code> instance.
   * @return the code review instance.
   */
  public ReviewIssue getReviewIssue() {
    if (this.reviewIssue == null) {
     return null; 
    }
    try {
      ReviewModel reviewModel = ReviewModel.getInstance();
      IProject project = reviewModel.getProjectManager().getProject();
      ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
      TypeKeyManager typeKeyManager = TypeKeyManager.getInstance(project, reviewId);
      SeverityKeyManager severityKeyManager = SeverityKeyManager.getInstance(project, reviewId);
      ResolutionKeyManager resolutionKeyManager = ResolutionKeyManager.getInstance(project, 
                                                                                   reviewId);
      StatusKeyManager statusKeyManager = StatusKeyManager.getInstance(project, reviewId);
      String resolutionKey = resolutionKeyManager.getKey(resolutionCombo.getText());
      String severityKey = severityKeyManager.getKey(severityCombo.getText());
      return new ReviewIssue(this.reviewIssue.getCreationDate(), new Date(), 
            this.reviewIssue.getReviewer(), this.assignedToCombo.getText(),
            this.reviewIssue.getTargetFile(), this.reviewIssue.getLine(),
            (Type) typeKeyManager.getItemObject(typeKeyManager.getKey(typeCombo.getText())),
            (Severity) severityKeyManager.getItemObject(severityKey),
            this.summaryText.getText(),
            this.descriptionText.getText(), this.annotationText.getText(), 
            this.revisionText.getText(),
            (Resolution) resolutionKeyManager.getItemObject(resolutionKey),
            (Status) statusKeyManager.getItemObject(statusKeyManager.getKey(statusCombo.getText())),
            this.reviewIssue.getReviewIFile());
    }
    catch (ReviewException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  /**
   * Clears all fields.
   */
  public void clearAllFields() {
    this.typeCombo.setText("");
    this.severityCombo.setText("");
    this.summaryText.setText("");
    this.descriptionText.setText("");
    this.assignedToCombo.setText("");
    this.resolutionCombo.setText("");
    this.annotationText.setText("");
    this.teamDescriptionText.setText("");
    this.statusCombo.setText("");
    this.revisionText.setText("");
    this.reworkDescriptionText.setText("");
    this.reviewIssue = null;
    this.updateTitle();
  }
  
  /**
   * Brings the phase-assoicated tag to the top.
   * @param reviewPhaseNameKey the review phase name key.
   */
  public void bringTagToTop(String reviewPhaseNameKey) {
    String reviewPhase = ReviewI18n.getString(reviewPhaseNameKey);
    TabItem[] tabItems = this.folder.getItems();
    for (int i = 0; i < tabItems.length; i++) {
      if (tabItems[i].getText().equals(reviewPhase)) {
        this.folder.setSelection(i);
        break;
      }
    }
    //set active tab name.
    setActiveTabNameKey(this.folder);
  }
  
  /**
   * Checks if the Widget is disposed or not. Returns <code>true</code> if the Widget is disposed.
   * @return <code>true</code> if the Widget is disposed.
   */
  public boolean isDisposed() {
    return this.ancestor.isDisposed();
  }
  
  /**
   * Updates title.
   */
  private void updateTitle() {
    String title = "";
    if (reviewIssue != null) {
      if (this.activeTabNameKey.equals(ResourceBundleKey.PHASE_INFIVIDUAL)) {
        String individualTitle = reviewIssue.getReviewer() + " : " + reviewIssue.getTargetFile()
        + " : " + reviewIssue.getLine();
        title = individualTitle;
      }
      else if (this.activeTabNameKey.equals(ResourceBundleKey.PHASE_TEAM)) {
        String teamTitle = reviewIssue.getReviewer() + " : " + summaryText.getText();
        title = teamTitle;
      }
      else if (this.activeTabNameKey.equals(ResourceBundleKey.PHASE_REWORK)) {
        String reworkTitle = reviewIssue.getReviewer() + " : " + summaryText.getText();
        title = reworkTitle;
      }
      ReviewTableView tableView = ReviewTableView.getActiveView();
      if (tableView == null) {
        tableView = ReviewTableView.bringViewToTop();
      }
    }
    this.setContentDescription(title);
  }

  /**
   * Sets new empty <code>ReviewIssue</code> instance and set it to the editor view.
   * @param iReviewFile the <code>IFile</code> review file instance.
   * @throws ReviewException if <code>ReviewIssue</code> instance was not created.
   */
  public void setNewEmptyReviewIssue(IFile iReviewFile) throws ReviewException {
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    ReviewerId reviewerId = reviewModel.getReviewerIdManager().getReviewerId();
    ReviewIssue reviewIssue = new ReviewIssue(new Date(), new Date(), reviewerId.getReviewerId(),
        "", "", "", (Type) TypeKeyManager.getInstance(project, reviewId).getItem(0),
        (Severity) SeverityKeyManager.getInstance(project, reviewId).getItem(0), "", "", "", "",
        (Resolution) ResolutionKeyManager.getInstance(project, reviewId).getItem(0),
        (Status) StatusKeyManager.getInstance(project, reviewId).getItem(0), iReviewFile);
    ReviewEditorViewAction.NEXT.setEnabled(false);
    ReviewEditorViewAction.PREVIOUS.setEnabled(false);
    this.setReviewIssue(reviewIssue);
    EventFileManager.getInstance().setEventFilePath(reviewIssue.getTargetFile());
  }  
  
  /**
   * Disposes listeners.
   */
  public void dispose() {
    super.dispose();
    getViewSite().getPage().removePartListener(partListener);
    ReviewIssueModelManager reviewIssueModelManager = ReviewIssueModelManager.getInstance();
    this.reviewIssueModel = reviewIssueModelManager.getCurrentModel();
  }
}
