package edu.hawaii.ics.csdl.jupiter.ui.preference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ResolutionKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.SeverityKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.StatusKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.TypeKeyManager;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableView;

/**
 * Provides the filter preference page in the preferences.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class FilterPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
  /** The enable filter field key. */
  public static final String ENABLE_FILTER_STORE_KEY = "EnableFilter";
  /** The enable visible limit field key. */
  public static final String ENABLE_VISIBLE_LIMIT_STORE_KEY = "LimitVisible";
  /** The enable filter period key. */
  public static final String ENABLE_FILTER_INTERVAL_STORE_KEY = "FilterInterval";
  /** The enable filter project key. */
  public static final String ENABLE_FILTER_PROJECT_STORE_KEY = "FilterProject";
  /** The enable filter severity key. */
  public static final String ENABLE_FILTER_SEVERITY_STORE_KEY = "FilterSeverity";
  /** The enable filter status key. */
  public static final String ENABLE_FILTER_STATUS_STORE_KEY = "FilterStatus";
  /** The enable filter resolution key. */
  public static final String ENABLE_FILTER_RESOLUTION_STORE_KEY = "FilterResolution";
  /** The enable filter type key. */
  public static final String ENABLE_FILTER_TYPE_STORE_KEY = "FilterType";
  /** The filter assigned to key. */
  public static final String ENABLE_FILTER_ASSIGNED_TO_STORE_KEY = "FilterAssignedTo";
  /** The filter reviewer key. */
  public static final String ENABLE_FILTER_REVIEWER_STORE_KEY = "FilterReviewer";
  /** The filter file key. */
  public static final String ENABLE_FILTER_FILE_STORE_KEY = "FilterFile";
  /** The filter interval text key. */
  public static final String FILTER_INTERVAL_TEXT_KEY = "FilterIntervalText";
  /** The filter severity combo key. */
  public static final String FILTER_SEVERITY_COMBO_KEY = "FilterSeverityCombo";
  /** The filter status combo key. */
  public static final String FILTER_STATUS_COMBO_KEY = "FilterStatusCombo";
  /** The filter resolution combo key. */
  public static final String FILTER_RESOLUTION_COMBO_KEY = "FilterResolutionCombo";
  /** The filter type combo key. */
  public static final String FILTER_TYPE_COMBO_KEY = "FilterTypeCombo";
  /** The filter assigned combo key. */
  public static final String FILTER_ASSIGNED_TO_COMBO_KEY = "FilterAssignedToCombo";
  /** The filter reviewer combo key. */
  public static final String FILTER_REVIEWER_COMBO_KEY = "FilterReviewerCombo";
  /** The filter file combo key. */
  public static final String FILTER_FILE_COMBO_KEY = "FilterFileCombo";
  /** The preference store to hold the existing preference values. */
  private IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
  /** The field editor list to hold the field editors or composites. */
  private List<Object[]> fields = new ArrayList<Object[]>();
  /** The filter interval text to be used for storing and loading its selected item. */
  private Text filterIntervalText;
  /** The filter severity combo to be used for storing and loading its selected item. */
  private Combo filterSeverityCombo;
  /** The filter status combo to be used for storing and loading its selected item. */
  private Combo filterStatusCombo;
  /** The filter resolution combo to be used for storing and loading its selected item. */
  private Combo filterResolutionCombo;
  /** The filter type combo to be used for storing and loading its selected item. */
  private Combo filterTypeCombo;
  /** The filter assigned to combo to be used for storing and loading its selected item. */
  private Combo filterAssignedToCombo;
  /** The filter reviewer combo to be used for storing and loading its selected item. */
  private Combo filterReviewerCombo;
  /** The filter file combo to be used for storing and loading its selected item. */
  private Combo filterFileCombo;
  /**
   * Description of the Field
   */
  private BooleanFieldEditor filterEnabledField;

  /**
   * Creates the Jupiter preference contents.
   *
   * @param parent the parent composite
   *
   * @return the new control
   */
  protected Control createContents(Composite parent) {
    Composite top = createGeneralComposite(parent);
    createLabelContent(top);
    createFilterPreferenceGroup(top);
    // Loads the preference store setting.
    loadFieldEditors(false);
    loadComposites();
    // Initializes the enable setting for field editors and composites.
    initializeEnableSetting(this.store.getBoolean(ENABLE_FILTER_STORE_KEY));
    return top;
  }
  
  /**
   * Creates filter preference frame and return the child composite.
   * @param parent the parent composite.
   * @return the child composite.
   */
  private Composite createGeneralComposite(Composite parent) {
    Composite child = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.numColumns = 1;
    child.setLayout(layout);
    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    child.setLayoutData(data);
    return child;
  }
  
  /**
   * Creates label content.
   * @param parent the parent composite.
   */
  private void createLabelContent(Composite parent) {
    Label label = new Label(parent, SWT.NULL);
    label.setText(ReviewI18n.getString("FilterPreferencePage.filter.label.info"));
    GridData data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
    data.horizontalSpan = 1;
    label.setLayoutData(data);
  }

  /**
   * Initializes the all fields (either field editor or composite) to be either enabled or
   * disabled, depending upon the <code>isEnabled</code> parameter status.
   *
   * @param isEnabled <code>true</code> if all fields are enabled.
   */
  private void initializeEnableSetting(boolean isEnabled) {
    for (Iterator<Object[]> i = fields.iterator(); i.hasNext();) {
      Object[] object = i.next();

      // Excludes enable filter check box.
      if (object.length >= 2) {
        FieldEditor fieldEditor = (FieldEditor) object[0];
        Composite parent = (Composite) object[1];
        fieldEditor.setEnabled(isEnabled, parent);
        // if a filed editor or composite is hooked to the filedEditor property listener.
        if (object.length >= 3) {
          if (!isEnabled
              || (isEnabled && (fieldEditor instanceof BooleanFieldEditor)
              && ((BooleanFieldEditor) fieldEditor).getBooleanValue())) {
            ((Scrollable) object[2]).setEnabled(isEnabled);
          }
        }
      }
    }
  }

  /**
   * Creates file preference group.
   *
   * @param parent the parent composite to be hooked.
   */
  private void createFilterPreferenceGroup(Composite parent) {
    final Group filterPreferenceGroup = new Group(parent, SWT.NONE);
    String filterLabelPreferenceKey = "FilterPreferencePage.filter.label.preference";
    filterPreferenceGroup.setText(ReviewI18n.getString(filterLabelPreferenceKey));
    filterPreferenceGroup.setLayout(new GridLayout());
    filterPreferenceGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    createEnabledFilterSubGroup(filterPreferenceGroup);
    createCheckBoxFilterSubGroup(filterPreferenceGroup);
  }

  /**
   * Creates the enable filter check box sub group.
   *
   * @param parent the composite to be hooked.
   */
  private void createEnabledFilterSubGroup(Composite parent) {
    // Creates check box field sub group.
    Composite checkBoxEnableFieldSubGroup = new Composite(parent, SWT.NONE);
    GridLayout checkBoxEnableLayout = new GridLayout();
    checkBoxEnableLayout.numColumns = 2;
    checkBoxEnableFieldSubGroup.setLayout(checkBoxEnableLayout);
    Composite filterEnabledComposite = new Composite(checkBoxEnableFieldSubGroup, SWT.NONE);
    String filterLabelEnabledKey = "FilterPreferencePage.filter.label.enabled";
    this.filterEnabledField = new BooleanFieldEditor(ENABLE_FILTER_STORE_KEY,
                                                  ReviewI18n.getString(filterLabelEnabledKey),
                                                  filterEnabledComposite);
    filterEnabledField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          initializeEnableSetting(new Boolean(event.getNewValue().toString()).booleanValue());
        }
      });
    fields.add(new Object[] {filterEnabledField});
  }

  /**
   * Creates the status filter sub group.
   *
   * @param parent the composite to be hooked.
   */
  private void createCheckBoxFilterSubGroup(Composite parent) {
    Composite filterFieldSubGroup = new Composite(parent, SWT.NONE);
    GridLayout filterStatusLayout = new GridLayout();
    filterStatusLayout.numColumns = 2;
    filterFieldSubGroup.setLayout(filterStatusLayout);
    filterFieldSubGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    createIntervalFilterField(filterFieldSubGroup);
    createReviewerFilterField(filterFieldSubGroup);
    createTypeFilterField(filterFieldSubGroup);
    createSeverityFilterField(filterFieldSubGroup);
    createAssignedToFilterField(filterFieldSubGroup);
    createResolutionFilterField(filterFieldSubGroup);
    createStatusFilterField(filterFieldSubGroup);
    createFileFilterField(filterFieldSubGroup);
  }

  /**
   * Creates the period filter field.
   *
   * @param parent the composite to be hooked.
   */
  private void createIntervalFilterField(Composite parent) {
    Composite filterIntervalComposite = new Composite(parent, SWT.NONE);
    filterIntervalComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    String filterIntervalKey = ENABLE_FILTER_INTERVAL_STORE_KEY;
    String filterLabelIntervalKey = "FilterPreferencePage.filter.label.interval";
    BooleanFieldEditor filterIntervalField = new BooleanFieldEditor(filterIntervalKey,
        ReviewI18n.getString(filterLabelIntervalKey), filterIntervalComposite);
    this.filterIntervalText = new Text(parent, SWT.BORDER);
    GridData filterIntervalData = new GridData(GridData.FILL_HORIZONTAL);
    filterIntervalText.setLayoutData(filterIntervalData);
    String intervalString = this.store.getString(FILTER_INTERVAL_TEXT_KEY);
    intervalString = (intervalString.equals("")) ? "0" : intervalString;
    filterIntervalText.setText(intervalString);
    filterIntervalField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          boolean isEnabled = new Boolean(event.getNewValue().toString()).booleanValue();
          filterIntervalText.setEnabled(isEnabled);
        }
      });
    filterIntervalText.setEnabled(this.store.getBoolean(ENABLE_FILTER_INTERVAL_STORE_KEY));
    fields.add(new Object[] {filterIntervalField, filterIntervalComposite, filterIntervalText});
  }

  /**
   * Creates the severity filter field.
   *
   * @param parent the composite to be hooked.
   */
  private void createSeverityFilterField(Composite parent) {
    Composite filterSeverityComposite = new Composite(parent, SWT.NONE);
    filterSeverityComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    String filterSeverityKey = ENABLE_FILTER_SEVERITY_STORE_KEY;
    String filterLabelSeverityKey = "FilterPreferencePage.filter.label.severity";
    BooleanFieldEditor filterSeverityField = new BooleanFieldEditor(filterSeverityKey,
        ReviewI18n.getString(filterLabelSeverityKey), filterSeverityComposite);
    this.filterSeverityCombo = new Combo(parent, SWT.READ_ONLY);
    GridData filterSeverityData = new GridData(GridData.FILL_HORIZONTAL);
    this.filterSeverityCombo.setLayoutData(filterSeverityData);
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    SeverityKeyManager mananger = SeverityKeyManager.getInstance(project, reviewId);
    this.filterSeverityCombo.setItems(mananger.getElements());
    String severityKey = this.store.getString(FILTER_SEVERITY_COMBO_KEY);
    this.filterSeverityCombo.setText(ReviewI18n.getString(severityKey));
    filterSeverityField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          boolean isEnabled = new Boolean(event.getNewValue().toString()).booleanValue();
          filterSeverityCombo.setEnabled(isEnabled);
        }
      });
    this.filterSeverityCombo.setEnabled(this.store.getBoolean(ENABLE_FILTER_SEVERITY_STORE_KEY));
    fields.add(new Object[] {filterSeverityField, filterSeverityComposite, 
                             this.filterSeverityCombo});
  }

  /**
   * Creates the status filter field.
   *
   * @param parent the composite to be hooked.
   */
  private void createStatusFilterField(Composite parent) {
    Composite filterStatusComposite = new Composite(parent, SWT.NONE);
    filterStatusComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    String filterLabelStatusKey = "FilterPreferencePage.filter.label.status";
    BooleanFieldEditor filterStatusField = new BooleanFieldEditor(ENABLE_FILTER_STATUS_STORE_KEY,
        ReviewI18n.getString(filterLabelStatusKey), filterStatusComposite);
    this.filterStatusCombo = new Combo(parent, SWT.READ_ONLY);
    GridData filterStatusData = new GridData(GridData.FILL_HORIZONTAL);
    this.filterStatusCombo.setLayoutData(filterStatusData);
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    this.filterStatusCombo.setItems(StatusKeyManager.getInstance(project, reviewId).getElements());
    String statusKey = this.store.getString(FILTER_STATUS_COMBO_KEY);
    this.filterStatusCombo.setText(ReviewI18n.getString(statusKey));
    filterStatusField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          filterStatusCombo.setEnabled(new Boolean(event.getNewValue().toString()).booleanValue());
        }
      });
    this.filterStatusCombo.setEnabled(this.store.getBoolean(ENABLE_FILTER_STATUS_STORE_KEY));
    fields.add(new Object[] {filterStatusField, filterStatusComposite, filterStatusCombo});
  }
  
  /**
   * Creates the file filter field.
   * @param parent the composite to be hooked.
   */
  private void createFileFilterField(Composite parent) {
    Composite filterFileComposite = new Composite(parent, SWT.NONE);
    filterFileComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    String filterLabelFileKey = "FilterPreferencePage.filter.label.file";
    BooleanFieldEditor filterFileField = new BooleanFieldEditor(ENABLE_FILTER_FILE_STORE_KEY,
        ReviewI18n.getString(filterLabelFileKey), filterFileComposite);
    this.filterFileCombo = new Combo(parent, SWT.READ_ONLY);
    GridData filterFileData = new GridData(GridData.FILL_HORIZONTAL);
    this.filterFileCombo.setLayoutData(filterFileData);
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    String[] targetFileArray = {""};
    if (project != null && reviewId != null) {
      ReviewIssueModel model = ReviewIssueModelManager.getInstance().getModel(project, reviewId);
      targetFileArray = model.getTargetFileArray();
    }
    this.filterFileCombo.setItems(targetFileArray);
    String fileKey = this.store.getString(FILTER_FILE_COMBO_KEY);
    this.filterFileCombo.setText(ReviewI18n.getString(fileKey));
    filterFileField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          filterFileCombo.setEnabled(new Boolean(event.getNewValue().toString()).booleanValue());
        }
      });
    this.filterFileCombo.setEnabled(this.store.getBoolean(ENABLE_FILTER_FILE_STORE_KEY));
    fields.add(new Object[] {filterFileField, filterFileComposite, filterFileCombo}); 
  }

  /**
   * Creates the resolution filter field.
   *
   * @param parent the composite to be hooked.
   */
  private void createResolutionFilterField(Composite parent) {
    Composite filterResolutionComposite = new Composite(parent, SWT.NONE);
    filterResolutionComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    String filterResolutionKey = ENABLE_FILTER_RESOLUTION_STORE_KEY;
    String filterLabelResolutionKey = "FilterPreferencePage.filter.label.resolution";
    BooleanFieldEditor filterResolutionField = new BooleanFieldEditor(filterResolutionKey,
        ReviewI18n.getString(filterLabelResolutionKey), filterResolutionComposite);
    this.filterResolutionCombo = new Combo(parent, SWT.READ_ONLY);
    GridData filterResolutionData = new GridData(GridData.FILL_HORIZONTAL);
    this.filterResolutionCombo.setLayoutData(filterResolutionData);
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    ResolutionKeyManager manager = ResolutionKeyManager.getInstance(project, reviewId);
    this.filterResolutionCombo.setItems(manager.getElements());
    String resolutionKey = this.store.getString(FILTER_RESOLUTION_COMBO_KEY);
    this.filterResolutionCombo.setText(ReviewI18n.getString(resolutionKey));
    filterResolutionField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          boolean isResolutionEnabled = new Boolean(event.getNewValue().toString()).booleanValue();
          filterResolutionCombo.setEnabled(isResolutionEnabled);
        }
      });
    filterResolutionCombo.setEnabled(this.store.getBoolean(ENABLE_FILTER_RESOLUTION_STORE_KEY));
    fields.add(new Object[] {filterResolutionField, filterResolutionComposite, 
               this.filterResolutionCombo});
  }

  /**
   * Creates the type filter field.
   *
   * @param parent the composite to be hooked.
   */
  private void createTypeFilterField(Composite parent) {
    Composite filterTypeComposite = new Composite(parent, SWT.NONE);
    filterTypeComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    String filterLabelTypeKey = "FilterPreferencePage.filter.label.type";
    BooleanFieldEditor filterTypeField = new BooleanFieldEditor(ENABLE_FILTER_TYPE_STORE_KEY,
        ReviewI18n.getString(filterLabelTypeKey), filterTypeComposite);
    this.filterTypeCombo = new Combo(parent, SWT.READ_ONLY);
    GridData filterTypeData = new GridData(GridData.FILL_HORIZONTAL);
    this.filterTypeCombo.setLayoutData(filterTypeData);
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    this.filterTypeCombo.setItems(TypeKeyManager.getInstance(project, reviewId).getElements());
    String typeKey = this.store.getString(FILTER_TYPE_COMBO_KEY);
    this.filterTypeCombo.setText(ReviewI18n.getString(typeKey));
    filterTypeField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          filterTypeCombo.setEnabled(new Boolean(event.getNewValue().toString()).booleanValue());
        }
      });
    this.filterTypeCombo.setEnabled(this.store.getBoolean(ENABLE_FILTER_TYPE_STORE_KEY));
    fields.add(new Object[] {filterTypeField, filterTypeComposite, filterTypeCombo});
  }

  /**
   * Creates the assigned to filter field.
   *
   * @param parent the composite to be hooked.
   */
  private void createAssignedToFilterField(Composite parent) {
    Composite filterAssignedToComposite = new Composite(parent, SWT.NONE);
    filterAssignedToComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    String filterAssignedToKey = ENABLE_FILTER_ASSIGNED_TO_STORE_KEY;
    String filterLabelAssignedToKey = "FilterPreferencePage.filter.label.assignedTo";
    BooleanFieldEditor filterAssignedToField = new BooleanFieldEditor(filterAssignedToKey,
        ReviewI18n.getString(filterLabelAssignedToKey), filterAssignedToComposite);
    this.filterAssignedToCombo = new Combo(parent, SWT.READ_ONLY);
    GridData filterAssignedToData = new GridData(GridData.FILL_HORIZONTAL);
    this.filterAssignedToCombo.setLayoutData(filterAssignedToData);
    // set reviewer IDs to assigned to if any.
    ReviewModel reviewModel = ReviewModel.getInstance();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    IProject project = reviewModel.getProjectManager().getProject();
    if (reviewId != null) {
      String reviewIdString = reviewId.getReviewId();
      PropertyResource reviewidResource = PropertyResource.getInstance(project, false);
      String[] items = reviewidResource.getReviewerIdNames(reviewIdString);
      filterAssignedToCombo.setItems((items == null) ? new String[] {} : items);
      IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
    }
    if (filterAssignedToCombo.getItemCount() > 0) {
      String reviewerKey = this.store.getString(FILTER_ASSIGNED_TO_COMBO_KEY);
      this.filterAssignedToCombo.setText(ReviewI18n.getString(reviewerKey));
    }
    filterAssignedToField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          boolean isAssignedToEnabled = new Boolean(event.getNewValue().toString()).booleanValue();
          filterAssignedToCombo.setEnabled(isAssignedToEnabled);
        }
      });
    filterAssignedToCombo.setEnabled(this.store.getBoolean(ENABLE_FILTER_ASSIGNED_TO_STORE_KEY));
    fields.add(new Object[] {filterAssignedToField, filterAssignedToComposite, 
               this.filterAssignedToCombo});
  }

  /**
   * Creates the reviewer filter field.
   *
   * @param parent the composite to be hooked.
   */
  private void createReviewerFilterField(Composite parent) {
    Composite filterReviewerComposite = new Composite(parent, SWT.NONE);
    filterReviewerComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    String filterReviewerKey = ENABLE_FILTER_REVIEWER_STORE_KEY;
    String filterLabelReviewerKey = "FilterPreferencePage.filter.label.reviewer";
    BooleanFieldEditor filterReviewerField = new BooleanFieldEditor(filterReviewerKey,
        ReviewI18n.getString(filterLabelReviewerKey), filterReviewerComposite);
    this.filterReviewerCombo = new Combo(parent, SWT.READ_ONLY);
    GridData filterReviewerData = new GridData(GridData.FILL_HORIZONTAL);
    this.filterReviewerCombo.setLayoutData(filterReviewerData);   
    // set reviewer IDs to reviewer combo to if any.
    ReviewModel reviewModel = ReviewModel.getInstance();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    IProject project = reviewModel.getProjectManager().getProject();
    if (reviewId != null) {
      String reviewIdString = reviewId.getReviewId();
      PropertyResource reviewIdResource = PropertyResource.getInstance(project, false);
      String[] items = reviewIdResource.getReviewerIdNames(reviewIdString);
      filterReviewerCombo.setItems((items == null) ? new String[] {} : items);
      IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
    }
    if (filterReviewerCombo.getItemCount() > 0) {
      String reviewerKey = this.store.getString(FILTER_REVIEWER_COMBO_KEY);
      this.filterReviewerCombo.setText(ReviewI18n.getString(reviewerKey));
    }
    filterReviewerField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          boolean isReviewerEnabled = new Boolean(event.getNewValue().toString()).booleanValue();
          filterReviewerCombo.setEnabled(isReviewerEnabled);
        }
      });
    filterReviewerCombo.setEnabled(this.store.getBoolean(ENABLE_FILTER_REVIEWER_STORE_KEY));
    fields.add(new Object[] {filterReviewerField, filterReviewerComposite, 
               this.filterReviewerCombo});
  }

  /**
   * Initializes this preference page for the given workbench.
   *
   * @param workbench the workbench
   */
  public void init(IWorkbench workbench) {
  }

  /**
   * Restores all field editor's values.
   *
   * @param isDefautLoaded <code>true</code> if default field editors are loaded.
   *        <code>false</code> otherwise.
   */
  private void loadFieldEditors(boolean isDefautLoaded) {
    for (Iterator<Object[]> i = this.fields.iterator(); i.hasNext();) {
      Object[] object = i.next();
      FieldEditor fieldEditor = (FieldEditor) object[0];
      fieldEditor.setPreferenceStore(this.store);
      if (isDefautLoaded) {
        fieldEditor.loadDefault();
      }
      else {
        fieldEditor.load();
      }
    }
  }

  /**
   * Restores all necessary composite's value.
   */
  private void loadComposites() {
    String interval = this.store.getString(FILTER_INTERVAL_TEXT_KEY);
    String severity = this.store.getString(FILTER_SEVERITY_COMBO_KEY);
    String status = this.store.getString(FILTER_STATUS_COMBO_KEY);
    String type = this.store.getString(FILTER_TYPE_COMBO_KEY);
    String resolution = this.store.getString(FILTER_RESOLUTION_COMBO_KEY);
    String assignedTo = this.store.getString(FILTER_ASSIGNED_TO_COMBO_KEY);
    String reviewer = this.store.getString(FILTER_REVIEWER_COMBO_KEY);
    String file = this.store.getString(FILTER_FILE_COMBO_KEY);
    this.filterIntervalText.setText(interval);
    this.filterSeverityCombo.select(this.filterSeverityCombo.indexOf(severity));
    this.filterStatusCombo.select(this.filterStatusCombo.indexOf(status));
    this.filterTypeCombo.select(this.filterTypeCombo.indexOf(type));
    this.filterResolutionCombo.select(this.filterResolutionCombo.indexOf(resolution));
    this.filterAssignedToCombo.select(this.filterAssignedToCombo.indexOf(assignedTo));
    this.filterReviewerCombo.select(this.filterReviewerCombo.indexOf(reviewer));
    this.filterFileCombo.select(this.filterFileCombo.indexOf(file));
  }

  /**
   * Stores all <code>FiledEditor</code> instances.
   */
  private void storeFieldEditors() {
    for (Iterator<Object[]> i = this.fields.iterator(); i.hasNext();) {
      Object[] object = (Object[]) i.next();
      FieldEditor fieldEditor = (FieldEditor) object[0];
      fieldEditor.setPreferenceStore(this.store);
      fieldEditor.store();
    }
  }

  /**
   * Stores all <code>Composites</code> or its sub class instance.
   */
  private void storeComposites() {
    String intervalString = this.filterIntervalText.getText();
    int interval = 0;
    try {
      Integer.parseInt(intervalString);
    }
    catch (NumberFormatException e) {
      // set interval = 0;
    }
    int severityIndex = this.filterSeverityCombo.getSelectionIndex();
    String severityLabel = (severityIndex != -1) ? this.filterSeverityCombo.getItem(severityIndex)
                                                 : "";
    int statusIndex = this.filterStatusCombo.getSelectionIndex();
    String statusLabel = (statusIndex != -1) ? this.filterStatusCombo.getItem(statusIndex) : "";
    int typeIndex = this.filterTypeCombo.getSelectionIndex();
    String typeLabel = (typeIndex != -1) ? this.filterTypeCombo.getItem(typeIndex) : "";
    int resolutionIndex = this.filterResolutionCombo.getSelectionIndex();
    String resolutionLabel = (resolutionIndex != -1)
                              ? this.filterResolutionCombo.getItem(resolutionIndex) : "";
    int assignedToIndex = this.filterAssignedToCombo.getSelectionIndex();
    String assignedToLabel = "";
    // -1 happens if no reviews are loaded to review, i.e. no assigned to exists.
    if (assignedToIndex != -1) {
      assignedToLabel = this.filterAssignedToCombo.getItem(assignedToIndex);
    }
    int reviewerIndex = this.filterReviewerCombo.getSelectionIndex();
    // -1 happens if no reviews are loaded to review, i.e. no reviews to exists.
    String reviewerLabel = (reviewerIndex != -1) ? this.filterReviewerCombo.getItem(reviewerIndex)
                                                 : "";
    int fileIndex = this.filterFileCombo.getSelectionIndex();
    String fileLabel = (fileIndex != -1) ? this.filterFileCombo.getItem(fileIndex)
                                                 : "";
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();;
    this.store.setValue(FILTER_INTERVAL_TEXT_KEY, interval + "");
    SeverityKeyManager severityKeyManager = SeverityKeyManager.getInstance(project, reviewId);
    this.store.setValue(FILTER_SEVERITY_COMBO_KEY, severityKeyManager.getKey(severityLabel));
    StatusKeyManager statusKeyManager = StatusKeyManager.getInstance(project, reviewId);
    this.store.setValue(FILTER_STATUS_COMBO_KEY, statusKeyManager.getKey(statusLabel));
    TypeKeyManager typeKeyManager = TypeKeyManager.getInstance(project, reviewId);
    this.store.setValue(FILTER_TYPE_COMBO_KEY, typeKeyManager.getKey(typeLabel));
    ResolutionKeyManager resolutionKeyManager = ResolutionKeyManager.getInstance(project, reviewId);
    this.store.setValue(FILTER_RESOLUTION_COMBO_KEY, resolutionKeyManager.getKey(resolutionLabel));
    this.store.setValue(FILTER_ASSIGNED_TO_COMBO_KEY, assignedToLabel);
    this.store.setValue(FILTER_REVIEWER_COMBO_KEY, reviewerLabel);
    this.store.setValue(FILTER_FILE_COMBO_KEY, fileLabel);
  }

  /**
   * Performs to save the current data in the editors.
   *
   * @return <code>false</code> to abort the container's OK processing and <code>true</code> to
   *         allow the OK to happen.
   */
  public boolean performOk() {
    storeFieldEditors();
    storeComposites();
    ReviewTableView view = ReviewTableView.getActiveView();
    if (view != null) {
      boolean isFilterEnabled = this.store.getBoolean(ENABLE_FILTER_STORE_KEY);
      view.setFilterStatus(isFilterEnabled);
    }
    return super.performOk();
  }

  /**
   * Performs special processing when this page's Apply button has been pressed. The default
   * implementation of this framework method simply calls performOk to simulate the pressing of
   * the page's OK button.
   */
  public void performApply() {
    performOk();
  }

  /**
   * Performs reset of all field values to the default specified on the
   * <code>csdl.jupiter.ReviewPlugin.initializeDefaultPreferences(IPreferenceStore)</code>
   * method.
   */
  public void performDefaults() {
    loadFieldEditors(true);
    loadComposites();
    initializeEnableSetting(this.store.getBoolean(ENABLE_FILTER_STORE_KEY));
  }
}
