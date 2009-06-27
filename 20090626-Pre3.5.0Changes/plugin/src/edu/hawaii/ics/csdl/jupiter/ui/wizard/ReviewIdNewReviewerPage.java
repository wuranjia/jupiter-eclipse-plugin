package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;

/**
 * Provides reviewer page.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdNewReviewerPage extends WizardPage {
  private Table reviewerListTable;
  private Button addButton;
  private Button removeButton;
  private Combo previousReviewIdCombo;
  private Map<String, ReviewerId> reviewers = new TreeMap<String, ReviewerId>();
  private IProject project;

  /**
   * Instantiates the config reviewer page.
   * 
   * @param project the project.
   * @param pageName the page name
   * @param imageFilePath the image file path.
   */
  protected ReviewIdNewReviewerPage(IProject project, String pageName, String imageFilePath) {
    super(pageName);
    setImageDescriptor(ReviewPlugin.createImageDescriptor(imageFilePath));
    setTitle(ReviewI18n.getString("ReviewIdNewReviewerPage.label.title"));
    setDescription(ReviewI18n.getString("ReviewIdNewReviewerPage.label.title.description"));
    this.project = project;
  }

  /**
   * Creates control.
   * 
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite ancestor) {
    Composite parent = createsGeneralComposite(ancestor);
    createrReviewerListContent(parent);
    fillReviewerTable(false);
    setControl(parent);
    setPageComplete(isTableNonEmpty());
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
   * Creates view preference content.
   * 
   * @param parent the parent composite.
   */
  private void createrReviewerListContent(Composite parent) {
    this.reviewerListTable = new Table(parent, SWT.BORDER | SWT.MULTI);
    FormData reviewerListTableData = new FormData();
    reviewerListTableData.left = new FormAttachment(0, 0);
    reviewerListTableData.right = new FormAttachment(80, 0);
    reviewerListTableData.top = new FormAttachment(0, 0);
    reviewerListTableData.bottom = new FormAttachment(100, 0);
    reviewerListTable.setLayoutData(reviewerListTableData);
    reviewerListTable.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        handleReviewListSelection();
      }
    });

    this.addButton = new Button(parent, SWT.PUSH);
    addButton.setText(ReviewI18n.getString("ReviewIdNewReviewerPage.label.button.add"));
    addButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        addReviewer();
      }
    });
    FormData addButtonData = new FormData();
    addButtonData.top = new FormAttachment(reviewerListTable, 0, SWT.TOP);
    addButtonData.left = new FormAttachment(reviewerListTable, 10);
    addButtonData.right = new FormAttachment(100, 0);
    addButton.setLayoutData(addButtonData);

    this.removeButton = new Button(parent, SWT.PUSH);
    removeButton.setText(ReviewI18n.getString("ReviewIdNewReviewerPage.label.button.remove"));
    removeButton.setEnabled(false);
    removeButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        removeReviewer();
      }
    });
    FormData removeButtonData = new FormData();
    removeButtonData.top = new FormAttachment(addButton, 5);
    removeButtonData.left = new FormAttachment(addButton, 0, SWT.LEFT);
    removeButtonData.right = new FormAttachment(100, 0);
    removeButton.setLayoutData(removeButtonData);
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
      PropertyResource reviewIdResource = PropertyResource.getInstance(this.project, true);
      Map<String, ReviewerId> reviewersMap = reviewIdResource
          .getReviewers(PropertyResource.DEFAULT_ID);
      this.reviewers = new TreeMap<String, ReviewerId>(reviewersMap);
    }
    for (Iterator<String> i = this.reviewers.keySet().iterator(); i.hasNext();) {
      String reviewerId = (String) i.next();
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
   * Creates review id list.
   * 
   * @param parent the composite.
   */
  private void createReviewIdList(Composite parent) {
    this.previousReviewIdCombo = new Combo(parent, SWT.READ_ONLY);
    previousReviewIdCombo.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        // modifyWidth();
      }
    });
    FormData previousReviewIdComboData = new FormData();
    previousReviewIdComboData.top = new FormAttachment(reviewerListTable, 5);
    previousReviewIdComboData.left = new FormAttachment(reviewerListTable, 0, SWT.LEFT);
    previousReviewIdComboData.right = new FormAttachment(80, 0);
    previousReviewIdCombo.setLayoutData(previousReviewIdComboData);
  }

  /**
   * Creates candidate reviewer list.
   * 
   * @param parent the composite.
   */
  private void createCandidateReviewerList(Composite parent) {
    Table previousReviewerListTable = new Table(parent, SWT.CHECK | SWT.BORDER);
    FormData previousReviewerListTableData = new FormData();
    previousReviewerListTableData.left = new FormAttachment(reviewerListTable, 0, SWT.LEFT);
    previousReviewerListTableData.right = new FormAttachment(80, 0);
    previousReviewerListTableData.top = new FormAttachment(previousReviewIdCombo, 5);
    previousReviewerListTableData.bottom = new FormAttachment(100, 0);
    previousReviewerListTable.setLayoutData(previousReviewerListTableData);
    previousReviewerListTable.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        // handleSelection((TableItem) e.item);
      }
    });

    Button addPreviousReviewerButton = new Button(parent, SWT.PUSH);
    addPreviousReviewerButton.setText("Add above");
    addPreviousReviewerButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        // modifyWidth();
      }
    });
    FormData addPreviousReviewerButtonData = new FormData();
    addPreviousReviewerButtonData.top = new FormAttachment(previousReviewerListTable, 0,
        SWT.TOP);
    addPreviousReviewerButtonData.left = new FormAttachment(previousReviewerListTable, 10);
    addPreviousReviewerButtonData.right = new FormAttachment(100, 0);
    addPreviousReviewerButton.setLayoutData(addPreviousReviewerButtonData);
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
    getWizard().getContainer().updateButtons();
    setPageComplete(isTableNonEmpty());
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
    this.removeButton.setEnabled(this.reviewerListTable.getItemCount() > 0);
    getWizard().getContainer().updateButtons();
    setPageComplete(isTableNonEmpty());
  }

  /**
   * Handles the table selection.
   */
  private void handleReviewListSelection() {
    boolean isSelected = (this.reviewerListTable.getSelectionIndex() >= 0);
    this.addButton.setEnabled(isSelected);
    this.removeButton.setEnabled(isSelected);
  }

  /**
   * Updates the author candidate list.
   * 
   * @param reviewers the reviewers which would be the candidates for the author.
   */
  private void updateAuthorCandidates(Map<String, ReviewerId> reviewers) {
    IWizardPage page = getWizard().getPage(ReviewIdNewWizard.PAGE_AUTHOR);
    ReviewIdNewAuthorPage configAuthorPage = (ReviewIdNewAuthorPage) page;
    String selectedAuthor = configAuthorPage.getAuthor();
    configAuthorPage.setAuthorItems(this.reviewers.keySet().toArray(new String[] {}));
    configAuthorPage.setDefaultAuthor(selectedAuthor);
  }

  /**
   * Returns the next page. Saves the values from this page in the model associated with the
   * wizard. Initializes the widgets on the next page.
   * 
   * @return the next page.
   */
  public IWizardPage getNextPage() {
    return getWizard().getPage(ReviewIdNewWizard.PAGE_AUTHOR);
  }

  /**
   * Checks if the wizard can flip to the next page.
   * 
   * @return <code>true</code> if it can flip to the next page.
   */
  public boolean canFlipToNextPage() {
    return isPageComplete();
  }

  /**
   * Checks if the table has at least one reviewer.
   * 
   * @return <code>true</code> if the table has at least one reviewer.
   */
  private boolean isTableNonEmpty() {
    return (this.reviewers.size() > 0);
  }

  /**
   * Gets the map of the String reviewer id - ReviewerId instance.
   * 
   * @return the map of the String reviewer id - ReviewerId instance.
   */
  public Map<String, ReviewerId> getReviewers() {
    return this.reviewers;
  }
}
