package edu.hawaii.ics.csdl.jupiter.ui.preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.PrefResource;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnData;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnDataModel;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnDataModelManager;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.ui.view.table.ReviewTableView;

/**
 * Provides view preference page.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ViewPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
  /** The view column table to contain reviewers. */
  private Table viewColumnTable;
  /** The add button. */
  private Button modifyButton;
  /** The up button. */
  private Button upButton;
  /** The down button. */
  private Button downButton;
  /** The review phase combo. */
  private Combo reviewPhaseCombo;
  /** The review phase map. */
  private Map reviewPhaseMap;
  /** The review phase name key. */
  private String reviewPhaseNameKey;
  /** The temporary review phase name key to remember the phase name in the view. */
  private String tempReviewPhaseNameKey;

  /**
   * Initializes this preference page for the given workbench.
   *
   * @param workbench the workbench.
   */
  public void init(IWorkbench workbench) {
    ReviewModel reviewModel = ReviewModel.getInstance();
    String reviewPhaseNameKey = reviewModel.getPhaseManager().getPhaseNameKey();
    ColumnDataModelManager columnDataModelManager = ColumnDataModelManager.getInstance();
    ColumnDataModel columnDataModel = columnDataModelManager.getModel(reviewPhaseNameKey);
    PrefResource configManager = PrefResource.getInstance();
    // Sets the initial phase to display in the view preference page.
    if (columnDataModel.allSize() <= 0) {
      this.reviewPhaseNameKey = configManager.getDefaultPhaseNameKey();
    }
    else {
      this.reviewPhaseNameKey = reviewPhaseNameKey;
    }
    this.tempReviewPhaseNameKey = this.reviewPhaseNameKey;
    String[] reviewPhaseNameKeys = configManager.getPhaseArray(true);
    this.reviewPhaseMap = new HashMap();
    for (int i = 0; i < reviewPhaseNameKeys.length; i++) {
      columnDataModel = columnDataModelManager.getModel(reviewPhaseNameKeys[i]);
      ColumnData[] columnDataArray = columnDataModel.getAllColumnDataArray();
      ArrayList columnDataList = new ArrayList(Arrays.asList(columnDataArray));
      this.reviewPhaseMap.put(reviewPhaseNameKeys[i], columnDataList);
    }
  }

  /**
   * Creates preference page controls on demand.
   *
   * @param ancestor the parent for the preference page
   *
   * @return the <code>Control</code> instance.
   */
  protected Control createContents(Composite ancestor) {
    Composite parent = createGeneralComposite(ancestor);
    createLabelContent(parent);
    createReviewPhaseContent(parent);
    createViewPreferenceContent(parent);
    fillTable((List) this.reviewPhaseMap.get(this.reviewPhaseNameKey));
    Dialog.applyDialogFont(ancestor);
    return parent;
  }
  
  /**
   * Creates view preference frame and return the child composite.
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
    label.setText(ReviewI18n.getString("ViewPreference.view.label.info"));
    GridData data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
    data.horizontalSpan = 1;
    label.setLayoutData(data);
  }
  
  /**
   * Creates review phase content.
   * @param parent the parent composite.
   */
  private void createReviewPhaseContent(Composite parent) {
    Composite reviewPhaseSubGroup = new Composite(parent, SWT.NONE);
    GridLayout reviewPhaseSubGroupLayout = new GridLayout();
    reviewPhaseSubGroupLayout.marginWidth = 0;
    reviewPhaseSubGroupLayout.marginHeight = 0;
    reviewPhaseSubGroupLayout.numColumns = 1;
    reviewPhaseSubGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
    reviewPhaseSubGroup.setLayout(reviewPhaseSubGroupLayout);
    
    this.reviewPhaseCombo = new Combo(reviewPhaseSubGroup, SWT.READ_ONLY);
    GridData reviewPhaseData = new GridData(GridData.FILL_HORIZONTAL);
    reviewPhaseCombo.setLayoutData(reviewPhaseData);
    reviewPhaseCombo.setItems(PrefResource.getInstance().getPhaseArray(false));
    ReviewPlugin plugin = ReviewPlugin.getInstance();
    int index = reviewPhaseCombo.indexOf(ReviewI18n.getString(reviewPhaseNameKey));
    reviewPhaseCombo.select(index);
    reviewPhaseCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) { 
        String reviewPhaseName = reviewPhaseCombo.getItem(reviewPhaseCombo.getSelectionIndex());
        reviewPhaseNameKey = ReviewI18n.getKey(reviewPhaseName);
        ColumnDataModelManager columnDataModelManager = ColumnDataModelManager.getInstance();
        ColumnDataModel columnDataModel = columnDataModelManager.getModel(reviewPhaseNameKey);
//        PrefResource.getInstance().loadColumnDataManager(columnDataManager, reviewPhaseNameKey,
//                                                          false);
        removeAllItems();
        fillTable((List) reviewPhaseMap.get(reviewPhaseNameKey));
      }
    });
  }
  
  /**
   * Creates view preference content.
   * @param parent the parent composite.
   */
  private void createViewPreferenceContent(Composite parent) {
    Composite viewPreferenceSubGroup = new Composite(parent, SWT.NONE);
    GridLayout viewPreferenceSubGroupLayout = new GridLayout();
    viewPreferenceSubGroupLayout.marginWidth = 0;
    viewPreferenceSubGroupLayout.marginHeight = 0;
    viewPreferenceSubGroupLayout.numColumns = 2;
    viewPreferenceSubGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
    viewPreferenceSubGroup.setLayout(viewPreferenceSubGroupLayout);
    
    viewColumnTable = new Table(viewPreferenceSubGroup, SWT.CHECK | SWT.BORDER);
    GridData gridData = new GridData(GridData.FILL_BOTH);

    //gd.widthHint = convertWidthInCharsToPixels(30);
    gridData.heightHint = 300;
    viewColumnTable.setLayoutData(gridData);
    viewColumnTable.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        handleSelection((TableItem) e.item);
      }
    });
    

    Composite buttons = new Composite(viewPreferenceSubGroup, SWT.NULL);
    buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
    GridLayout layout = new GridLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    buttons.setLayout(layout);
    modifyButton = new Button(buttons, SWT.PUSH);
    modifyButton.setText(ReviewI18n.getString("ViewPreference.button.label.modify"));
    modifyButton.setEnabled(false);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    // As to Eclipse Help, button layout is determined by layout.
    //data.heightHint = convertVerticalDLUsToPixels(IDialogConstants.BUTTON_HEIGHT);
    int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    int x = modifyButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
    data.widthHint = Math.max(widthHint, x);
    modifyButton.setLayoutData(data);
    modifyButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        modifyWidth();
      }
    });
    
    upButton = new Button(buttons, SWT.PUSH);
    Image upImage = ReviewPlugin.createImageDescriptor("lib/icons/up.gif").createImage();
    upButton.setImage(upImage);
    upButton.setEnabled(false);
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    // data.heightHint = convertVerticalDLUsToPixels(IDialogConstants.BUTTON_HEIGHT);
    widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    x = upButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
    data.widthHint = Math.max(widthHint, x);
    upButton.setLayoutData(data);
    upButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        moveUpItem(viewColumnTable.getSelectionIndex());
      }
    });
    
    downButton = new Button(buttons, SWT.PUSH);
    Image downImage = ReviewPlugin.createImageDescriptor("lib/icons/down.gif").createImage();
    downButton.setImage(downImage);
    downButton.setEnabled(false);
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    //data.heightHint = convertVerticalDLUsToPixels(IDialogConstants.BUTTON_HEIGHT);
    widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    x = downButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
    data.widthHint = Math.max(widthHint, x);
    downButton.setLayoutData(data);
    downButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        moveDownItem(viewColumnTable.getSelectionIndex());
      }
    });
  }
  
  /**
   * Moves up the indexed item by one.
   * @param index the index of the item to move up.
   *
   */
  private void moveUpItem(int index) {
    if (index > 0) {
      List columnDataList = (List) this.reviewPhaseMap.get(this.reviewPhaseNameKey);
      ColumnData columnData = (ColumnData) columnDataList.remove(index);
      int newIndex = index - 1;
      columnDataList.add(newIndex, columnData);
      removeAllItems();
      fillTable(columnDataList);
      viewColumnTable.select(newIndex);
    }
  }
  
  /**
   * Moves down the indexed item by one.
   * @param index the index of the item to move down.
   */
  private void moveDownItem(int index) {
    int lastIndex = viewColumnTable.getItemCount() - 1;
    if (index < lastIndex) {
      List columnDataList = (List) this.reviewPhaseMap.get(this.reviewPhaseNameKey);
      ColumnData columnData = (ColumnData) columnDataList.remove(index);
      int newIndex = index + 1;
      columnDataList.add(newIndex, columnData);
      removeAllItems();
      fillTable(columnDataList);
      viewColumnTable.select(newIndex);
    }
  }

  /**
   * Performs OK to save changes when this page's OK button has been pressed.
   *
   * @return whether it is okay to close the preference page
   */
  public boolean performOk() {
    ReviewModel reviewModel = ReviewModel.getInstance();
    ColumnDataModelManager columnDataModelManager = ColumnDataModelManager.getInstance();
    for (Iterator i = this.reviewPhaseMap.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      String reviewPhaseNameKey = (String) entry.getKey();
      List columnDataList = (List) entry.getValue();
      ColumnDataModel columnDataModel = columnDataModelManager.getModel();
      columnDataModel.clear();
      columnDataModel.addAll(columnDataList);
      PrefResource.getInstance().storeColumnDataModel(reviewPhaseNameKey, columnDataModel);
    }
    // Updates the view.
    ColumnDataModel columnDataModel = columnDataModelManager.getModel(reviewPhaseNameKey);
    ReviewTableView view = ReviewTableView.getActiveView();
    // null happens when the view is not opened yet after Eclipse startup.
    if (view == null) {
      view = ReviewTableView.bringViewToTop();
    }
    view.createColumns(columnDataModel);
    view.getViewer().refresh();
    return true;
  }
  
  /**
   * Performs OK to save changes when this page's Apply button has been pressed.
   */
  public void performApply() {
    performOk();
  }

  /**
   * Performs Defaults to restore default values when this page's Defaults button has been pressed.
   */
  protected void performDefaults() {
    String reviewPhaseName = reviewPhaseCombo.getItem(reviewPhaseCombo.getSelectionIndex());
    reviewPhaseNameKey = ReviewI18n.getKey(reviewPhaseName);
    ColumnDataModelManager columnDataModelManager = ColumnDataModelManager.getInstance();
    ColumnDataModel columnDataModel = columnDataModelManager.getModel(reviewPhaseNameKey);
    List columnDataList = (List) reviewPhaseMap.get(reviewPhaseNameKey);
    columnDataList.clear();
    columnDataList.addAll(new ArrayList(Arrays.asList(columnDataModel.getAllColumnDataArray())));
    removeAllItems();
    fillTable(columnDataList);
  }

  /**
   * Fills table with the column name, width, and enable status.
   * @param columnDataList the <code>ColumnData</code> list.
   */
  private void fillTable(List columnDataList) {
    for (Iterator i = columnDataList.iterator(); i.hasNext();) {
      ColumnData columnData = (ColumnData) i.next();
      String columnName = columnData.getColumnName();
      int width = columnData.getColumnPixelData().width;
      boolean isEnabled = columnData.isEnabled();
      TableItem item = new TableItem(viewColumnTable, SWT.NONE);
      item.setText(columnName + " [" + width + "]");
      item.setChecked(isEnabled);
    }
  }
  
  /**
   * Remove all items in the table.
   */
  private void removeAllItems() {
    while (viewColumnTable.getItemCount() > 0) {
      int lastIndex = viewColumnTable.getItemCount() - 1;
      viewColumnTable.getItem(lastIndex).dispose();
    }
  }

  /**
   * Modifies a column width.
   */
  private void modifyWidth() {
    int index = viewColumnTable.getSelectionIndex();
    IInputValidator validator = new IInputValidator() {
      public String isValid(String newText) {
        try {
          Integer.parseInt(newText);
          return null;
        }
        catch (NumberFormatException e) {
          return ReviewI18n.getString("ViewPreference.button.messageDialog.error");
        }
      }
    };
    List columnDataList = (List) this.reviewPhaseMap.get(this.reviewPhaseNameKey);
    ColumnData columnData = (ColumnData) columnDataList.get(index);
    String initialWidthValue = columnData.getColumnPixelData().width + "";
    InputDialog dialog = new InputDialog(getShell(), 
        ReviewI18n.getString("ViewPreference.button.messageDialog.short"),
        ReviewI18n.getString("ViewPreference.button.messageDialog.long"),
        initialWidthValue, validator);  //$NON-NLS-1$ //$NON-NLS-2$
    dialog.open();
    if (dialog.getReturnCode() != InputDialog.OK) {
      return;
    }
    String modifiedWidthValue = dialog.getValue();
    int width = Integer.parseInt(modifiedWidthValue);
    columnData.getColumnPixelData().width = width;
    TableItem item = viewColumnTable.getItem(index);
    item.setText(columnData.getColumnName() + " [" + width + "]");
  }

  /**
   * Handles the table selection.
   * @param tableItem the <code>TableItem</code> instance.
   */
  private void handleSelection(TableItem tableItem) {
    int index = viewColumnTable.indexOf(tableItem);
    List columnDataList = (List) this.reviewPhaseMap.get(this.reviewPhaseNameKey);
    ColumnData columnData = (ColumnData) columnDataList.get(index);
    columnData.setEnabled(tableItem.getChecked());    
    boolean isSelected = (viewColumnTable.getSelectionCount() > 0);
    modifyButton.setEnabled(isSelected);
    upButton.setEnabled(isSelected);
    downButton.setEnabled(isSelected);
  }
}

