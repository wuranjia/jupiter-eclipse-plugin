package edu.hawaii.ics.csdl.jupiter.ui.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyXmlSerializer;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.file.Ver1PropertyHelper;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;
import edu.hawaii.ics.csdl.jupiter.util.ReviewDialog;

/**
 * Provides configuration property page.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  private static final String TABLE_COLUMN = "TableColumn";
  private static final String COLUMN_KEY = "ColumnKey";
  private IProject project;
  private TableViewer tableViewer;
  private Button newButton;
  private Table table;
  private Button removeButton;
  private Button editButton;
  private Composite composite;
  /** The column review ID key. */
  public static final String COLUMN_REVIEW_ID_KEY = "ReviewPropertyPage.label.column.reviewId";
  /** The  column description key. */
  public static final String COLUMN_DESCRIPTION_KEY = "ReviewPropertyPage.label.column.description";
  /** The column date key. */
  public static final String COLUMN_DATE_KEY = "ReviewPropertyPage.label.column.date";
  /**
   * Creates content.
   * @param ancestor the composite.
   * @return the control.
   */
  protected Control createContents(Composite ancestor) {
    this.composite = ancestor;
    this.project = (IProject) getElement();
    noDefaultAndApplyButton();
    Composite parent = createsGeneralComposite(ancestor);
    createReviewIdTableContent(parent);
    createButtonsContent(parent);
    return parent;
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
    layout.marginHeight = 7;
    child.setLayout(layout);
    return child;
  }
  
  /**
   * Creates review id table.
   * @param parent the composite.
   */
  private void createReviewIdTableContent(Composite parent) {
    this.table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
    FormData tableData = new FormData();
    tableData.left = new FormAttachment(0, 0);
    tableData.right = new FormAttachment(80, 0);
    tableData.top = new FormAttachment(0, 0);
    tableData.bottom = new FormAttachment(100, 0);
    table.setLayoutData(tableData);
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    table.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        handleReviewIdSelection();
      }
    });
    TableColumn columnReviewId = new TableColumn(table, SWT.NONE);
    columnReviewId.setText(ReviewI18n.getString(COLUMN_REVIEW_ID_KEY));
    columnReviewId.setData(COLUMN_KEY, COLUMN_REVIEW_ID_KEY);
    TableColumn columnDescription = new TableColumn(table, SWT.NONE);
    String description = ReviewI18n.getString(COLUMN_DESCRIPTION_KEY);
    columnDescription.setText(description);
    columnDescription.setData(COLUMN_KEY, COLUMN_DESCRIPTION_KEY);
    TableColumn columnDate = new TableColumn(table, SWT.NONE);
    columnDate.setText(ReviewI18n.getString(COLUMN_DATE_KEY));
    columnDate.setData(COLUMN_KEY, COLUMN_DATE_KEY);
    
    List columnList = new ArrayList();
    columnList.add(columnReviewId);
    columnList.add(columnDescription);
    columnList.add(columnDate);
    hookSelectionListener(columnList);
    
    TableLayout tableLayout = new TableLayout();
    tableLayout.addColumnData(new ColumnWeightData(22));
    tableLayout.addColumnData(new ColumnWeightData(48));
    tableLayout.addColumnData(new ColumnWeightData(20));
    table.setLayout(tableLayout);
    
    this.tableViewer = new TableViewer(table);
    tableViewer.setLabelProvider(new ReviewPropertyLabelProvider());
    tableViewer.setContentProvider(new ReviewPropertyContentProvider());
    tableViewer.setSorter(ReviewPropertyViewerSorter.getViewerSorter(COLUMN_DATE_KEY));
    tableViewer.setInput(PropertyResource.getInstance(this.project, true).getReviewIdList());
    
    // routine for the version 1 compatibility.
    if (tableViewer.getTable().getItemCount() <= 0) {
      // version 1.
      List reviewIdList = Ver1PropertyHelper.getReviewIdList(this.project.getName(), false);
      IFile jupiterConfigFile = project.getFile(PropertyXmlSerializer.PROPERTY_XML_FILE);
      FileResource.remove(new IFile[] {jupiterConfigFile});
      // create version 2 .jupiter file.
      String defaultName = PropertyConstraints.DEFAULT_REVIEW_ID;
      PropertyResource propertyResource = null;
      for (Iterator i = reviewIdList.iterator(); i.hasNext();) {
        ReviewId reviewId = (ReviewId) i.next();
        propertyResource = PropertyResource.getInstance(this.project, true);
        ReviewResource reviewResource = propertyResource.getReviewResource(defaultName, true);
        ReviewId defaultReviewId = reviewResource.getReviewId();
        String directory = defaultReviewId.getDirectory();
        reviewId.setDirectory(directory);
        reviewResource.setReviewId(reviewId);
        try {
          propertyResource.addReviewResource(reviewResource);
        }
        catch (ReviewException e) {
          log.debug(e.getMessage());
        }
      }
      tableViewer.setInput(propertyResource.getReviewIdList());
    }
    tableViewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(DoubleClickEvent event) {
        editReviewId();
      }
    });
  }

  /**
   * Creates buttons content.
   * @param parent the parent.
   */
  private void createButtonsContent(Composite parent) {
    this.newButton = new Button(parent, SWT.PUSH);
    newButton.setText(ReviewI18n.getString("ReviewPropertyPage.label.button.new"));
    newButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
          addReviewId();
      }
    });
    FormData newButtonData = new FormData();
    newButtonData.top = new FormAttachment(table, 0, SWT.TOP);
    newButtonData.left = new FormAttachment(table, 10);
    newButtonData.right = new FormAttachment(100, 0);
    newButton.setLayoutData(newButtonData);
    
    this.editButton = new Button(parent, SWT.PUSH);
    editButton.setText(ReviewI18n.getString("ReviewPropertyPage.label.button.edit"));
    editButton.setEnabled(false);
    editButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
          editReviewId();
      }
    });
    FormData editButtonData = new FormData();
    editButtonData.top = new FormAttachment(newButton, 5);
    editButtonData.left = new FormAttachment(newButton, 0, SWT.LEFT);
    editButtonData.right = new FormAttachment(100, 0);
    editButton.setLayoutData(editButtonData);
    
    
    this.removeButton = new Button(parent, SWT.PUSH);
    removeButton.setText(ReviewI18n.getString("ReviewPropertyPage.label.button.remove"));
    removeButton.setEnabled(false);
    removeButton.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        removeReviewId();
      }
    });
    FormData removeButtonData = new FormData();
    removeButtonData.top = new FormAttachment(editButton, 5);
    removeButtonData.left = new FormAttachment(newButton, 0, SWT.LEFT);
    removeButtonData.right = new FormAttachment(100, 0);
    removeButton.setLayoutData(removeButtonData);
  }

  /**
   * Hooks selection listener for each <code>TableColumn</code> element of the list.
   * @param columnList the list of the <code>TableColumn</code> elements.
   */
  private void hookSelectionListener(List columnList) {
    for (Iterator i = columnList.iterator(); i.hasNext();) {
      TableColumn column = (TableColumn) i.next();
      column.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event event) {
          String columnKey = (String) event.widget.getData(COLUMN_KEY);
          sortBy(columnKey);
        }
      });
    }
  }
  
  /**
   * Sorts by the <code>String</code> columnKey.
   * @param columnKey the <code>String</code> columnKey.
   */
  protected void sortBy(String columnKey) {
    ViewerSorter viewerSorter = ReviewPropertyViewerSorter.getViewerSorter(columnKey);
    if (viewerSorter != null) {
      ViewerSorter previousSorter = tableViewer.getSorter();
      if (previousSorter == viewerSorter) {
        ReviewPropertyViewerSorter.setReverse(!ReviewPropertyViewerSorter.isReverse());
        // Resets sorter.
        tableViewer.setSorter(null);
      }
      else {
        ReviewPropertyViewerSorter.setReverse(false);
      }
      tableViewer.setSorter(viewerSorter);
    }
  }

  /**
   * Adds review ID so as to open review id addition wizard.
   */
  private void addReviewId() {
    ReviewDialog.processConfigWizardDialog(this.project);
    List reviewIdList = PropertyResource.getInstance(this.project, true).getReviewIdList();
    tableViewer.setInput(reviewIdList);
  }
  
  /**
   * Edits the selected review ID.
   */
  private void editReviewId() {
    int selectedIndex = table.getSelectionIndex();
    if (selectedIndex >= 0) {
      ReviewId reviewId = (ReviewId) tableViewer.getElementAt(selectedIndex);
      Dialog dialog = new ReviewIdEditDialog(composite.getShell(), this.project, reviewId);
      dialog.open();
      this.tableViewer.setInput(PropertyResource.getInstance(this.project, true).getReviewIdList());
    }
  }
  
  /**
   * Removes the selected review ID
   */
  private void removeReviewId() {
    int selectedIndex = table.getSelectionIndex();
    if (selectedIndex >= 0) {
      ReviewId reviewId = (ReviewId) tableViewer.getElementAt(selectedIndex);
      IFile[] reviewIFiles = FileResource.getReviewIFiles(this.project, reviewId);
      Dialog dialog = new ReviewIdRemovalDialog(composite.getShell(), reviewIFiles);
      dialog.open();
      if (dialog.getReturnCode() == Dialog.OK) {
        // remove review files associated with the review id.
        FileResource.remove(reviewIFiles);
        try {
          PropertyResource propertyResource = PropertyResource.getInstance(this.project, false);
          propertyResource.removeReviewResource(reviewId);
        }
        catch (ReviewException e) {
          log.error(e);
        }
      }
    }
    tableViewer.setInput(PropertyResource.getInstance(this.project, true).getReviewIdList());
  }

  /**
   * Handles review id selection
   */
  protected void handleReviewIdSelection() {
    int index = this.table.getSelectionIndex();
    boolean isSelected = (index >= 0);
    this.newButton.setEnabled(isSelected);
    this.editButton.setEnabled(isSelected);   
    this.removeButton.setEnabled(isSelected);
    TableItem item = this.table.getItem(index);
    ReviewId reviewId = (ReviewId) item.getData();
    if (reviewId.getReviewId().equals(PropertyConstraints.DEFAULT_REVIEW_ID)) {
      this.removeButton.setEnabled(false);
    }
  }
}
