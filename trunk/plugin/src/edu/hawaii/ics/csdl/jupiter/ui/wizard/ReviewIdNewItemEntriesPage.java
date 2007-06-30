package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.FieldItem;
import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;

/**
 * Provides item entries page.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdNewItemEntriesPage extends WizardPage {

  private IProject project;
  private Combo itemCombo;
  private Table itemListTable;
  private Button newButtonInItemEntries;
  private Button editButtonInItemEntries;
  private Button removeButtonInItemEntries;
  private Button upButtonInItemEntries;
  private Button downButtonInItemEntries;
  private Button restoreButtonInItemEntries;
  private Map fieldItemIdFieldItemMap;
  private List fieldItemIdList;
  /**
   * @param project the project name.
   * @param pageName the page name.
   * @param imageFilePath the image file path.
   */
  protected ReviewIdNewItemEntriesPage(IProject project, String pageName, String imageFilePath) {
    super(pageName);
    setImageDescriptor(ReviewPlugin.createImageDescriptor(imageFilePath));
    setTitle(ReviewI18n.getString("ReviewIdNewItemEntriesPage.label.title"));
    setDescription(ReviewI18n.getString("ReviewIdNewItemEntriesPage.label.title.description"));
    this.project = project;
  }

  /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite ancestor) {
    Composite parent = createsGeneralComposite(ancestor);
    createItemEntriesContent(parent);
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
   * Creates item entries content.
   * @param composite the composite.
   * @return the control.
   */
  private Control createItemEntriesContent(Composite composite) {
    PropertyResource propertyResource = PropertyResource.getInstance(this.project, true);
    String reviewIdString = PropertyConstraints.DEFAULT_REVIEW_ID;
    ReviewResource reviewResource = propertyResource.getReviewResource(reviewIdString, true);
    if (reviewResource != null) {
      this.fieldItemIdFieldItemMap = reviewResource.getFieldItemMap();
      this.fieldItemIdList = reviewResource.getFieldItemIdList();
    }
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
    FieldItem fieldItem = (FieldItem) this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List itemList = fieldItem.getEntryNameList();
      itemList.add(itemName);
      fillItemTable(fieldItemId);
      updateDefaultItems(fieldItemId);
      IWizardPage page = getWizard().getPage(ReviewIdNewWizard.PAGE_FILTERS);
      ReviewIdNewFilterPage filterPage = (ReviewIdNewFilterPage) page;
      filterPage.updateFilterItems(fieldItemId);
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
    FieldItem fieldItem = (FieldItem) this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List itemList = fieldItem.getEntryNameList();
      int index = itemList.indexOf(oldName);
      itemList.remove(index);
      itemList.add(index, newName);
      fillItemTable(fieldItemId);
      updateDefaultItems(fieldItemId);
      IWizardPage page = getWizard().getPage(ReviewIdNewWizard.PAGE_FILTERS);
      ReviewIdNewFilterPage filterPage = (ReviewIdNewFilterPage) page;
      filterPage.updateFilterItems(fieldItemId);
    }
  }
  
  /**
   * Removes item entries form the table.
   */
  protected void removeItemEntries() {
    String fieldItemId = this.itemCombo.getText();
    FieldItem fieldItem = (FieldItem) this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List itemList = fieldItem.getEntryNameList();
      TableItem[] items = this.itemListTable.getSelection();
      for (int i = 0; i < items.length; i++) {
        String itemName = items[i].getText();
        itemList.remove(itemName);
      }
      fillItemTable(fieldItemId);
      updateDefaultItems(fieldItemId);
      IWizardPage page = getWizard().getPage(ReviewIdNewWizard.PAGE_FILTERS);
      ReviewIdNewFilterPage filterPage = (ReviewIdNewFilterPage) page;
      filterPage.updateFilterItems(fieldItemId);
    }
  }
  
  /**
   * Moves the selected item entry by one upward if <code>isUpward</code> is <code>true</code>.
   * Otherwise, moves the selected item entry by one downward.
   * @param isUpward <code>true</code> if moving the selected item entry by one upward.
   * <code>false</code> if moving the selected item entry by one downward.
   */
  protected void moveItemEntry(boolean isUpward) { 
    String fieldItemId = this.itemCombo.getText();
    FieldItem fieldItem = (FieldItem) this.fieldItemIdFieldItemMap.get(fieldItemId);
    TableItem[] selectedItems = this.itemListTable.getSelection();
    if (selectedItems.length > 0 && fieldItem != null) {
      TableItem selectedItem = selectedItems[0];
      String itemName = selectedItem.getText();
      List itemList = fieldItem.getEntryNameList();
      int index = itemList.indexOf(itemName);
      if ((isUpward) ? index > 0 : index < itemList.size() - 1) {
        int nextIndex = (isUpward) ? index - 1 : index + 1;
        itemList.remove(index);
        itemList.add(nextIndex, itemName);
        fillItemTable(fieldItemId);
        this.itemListTable.select(nextIndex);
        handleItemListTableSelection();
        updateDefaultItems(fieldItemId);
        IWizardPage page = getWizard().getPage(ReviewIdNewWizard.PAGE_FILTERS);
        ReviewIdNewFilterPage filterPage = (ReviewIdNewFilterPage) page;
        filterPage.updateFilterItems(fieldItemId);
      }
    }
  }
  
  /**
   * Restores the item entries from the default review id.
   */
  protected void restoreItemEntries() {
    // read from .jupiter.
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    String defaultReviewID = PropertyConstraints.DEFAULT_REVIEW_ID;
    ReviewResource reviewResource = propertyResource.getReviewResource(defaultReviewID, true);
    if (reviewResource != null) {
      String fieldItemId = this.itemCombo.getText();
      FieldItem fieldItem = reviewResource.getFieldItem(fieldItemId);
      this.fieldItemIdFieldItemMap.put(fieldItemId, fieldItem);
      fillItemTable(fieldItemId);
      updateDefaultItems(fieldItemId);
      IWizardPage page = getWizard().getPage(ReviewIdNewWizard.PAGE_FILTERS);
      ReviewIdNewFilterPage filterPage = (ReviewIdNewFilterPage) page;
      filterPage.updateFilterItems(fieldItemId);
    }
  }
  
  /**
   * Opens the dialog window.
   * @param existingItemName the existing item name.
   * @param shortMessageKey the short message key.
   * @param longMessageKey the long message key.
   * @return the input dialog the <code>InputDialog</code>.
   */
  private InputDialog openDialog(String existingItemName, String shortMessageKey, 
                      String longMessageKey) {
    FieldItem fieldItem = (FieldItem) this.fieldItemIdFieldItemMap.get(this.itemCombo.getText());
    if (fieldItem != null) {
      final List itemList = fieldItem.getEntryNameList();
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
                                           ReviewI18n.getString(longMessageKey), existingItemName,
                                           validator);  //$NON-NLS-1$ //$NON-NLS-2$
      dialog.open();
      return dialog;
    }
    return null;
  }
  
  /**
   * Handles the item id combo selection.
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
   * @param fieldItemId the field item id.
   */
  private void fillItemTable(String fieldItemId) {
    removeItems();
    FieldItem fieldItem = (FieldItem) this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      for (Iterator i = fieldItem.getEntryNameList().iterator(); i.hasNext();) {
        String itemEntry = ReviewI18n.getString((String) i.next());
        TableItem item = new TableItem(this.itemListTable, SWT.NONE);
        item.setText(itemEntry);
      }
    }
  }
  
  /**
   * Removes item data from the item table.
   */
  private void removeItems() {
    TableItem[] items = this.itemListTable.getItems();
    for (int i = 0; i < items.length; i++) {
      TableItem item  = items[i];
      item.dispose();
    }
  }
  
  /**
   * Updates the default items folder.
   * @param fieldItemId the field item id.
   */
  private void updateDefaultItems(String fieldItemId) {
    String pageName = ReviewIdNewWizard.PAGE_DEFAULT_ITEMS;
    ReviewIdNewDefaultItemsPage page = (ReviewIdNewDefaultItemsPage) getWizard().getPage(pageName);
    FieldItem fieldItem = (FieldItem) this.fieldItemIdFieldItemMap.get(fieldItemId);
    if (fieldItem != null) {
      List itemNameList = fieldItem.getEntryNameList();
      if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_TYPE)) {
        page.setItemType((String[]) itemNameList.toArray(new String[] {}));
      }
      else if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY)) {
        page.setItemSeverity((String[]) itemNameList.toArray(new String[] {}));
      }
      else if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION)) {
        page.setItemResolution((String[]) itemNameList.toArray(new String[] {}));
      }
      else if (fieldItemId.equals(PropertyConstraints.ATTRIBUTE_VALUE_STATUS)) {
        page.setItemStatus((String[]) itemNameList.toArray(new String[] {}));
      }
    }
  }
  
  /**
   * Returns the next page. Saves the values from this page in the model associated with the wizard.
   * Initializes the widgets on the next page.
   * @return the next page.
   */
  public IWizardPage getNextPage() {
    String pageName = ReviewIdNewWizard.PAGE_DEFAULT_ITEMS;
    return ((ReviewIdNewWizard) getWizard()).getPage(pageName);
  }
  
  /**
   * Gets the map of the String field item id - the list of the String item name.
   * @return the map of the String field item id - the list of the String item name.
   */
  public Map getFieldItemIdFieldItemMap() {
    return this.fieldItemIdFieldItemMap;
  }

}
