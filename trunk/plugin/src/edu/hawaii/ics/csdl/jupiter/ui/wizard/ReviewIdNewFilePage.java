package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.internal.filesystem.local.LocalFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.dialogs.FileFolderSelectionDialog;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;

/**
 * Provides review file configuration page.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdNewFilePage extends WizardPage {

  private IProject project;
  private Set<String> files;
  private Table fileListTable;
  private Button addButtonInFile;
  private Button removeButtonInFile;

  /**
   * Instantiates the review file configuration page.
   * @param project the project.
   * @param pageName the page name
   * @param imageFilePath the image file path.
   */
  protected ReviewIdNewFilePage(IProject project, String pageName, String imageFilePath) {
    super(pageName);
    setImageDescriptor(ReviewPlugin.createImageDescriptor(imageFilePath));
    setTitle(ReviewI18n.getString("ReviewIdNewFilePage.label.title"));
    setDescription(ReviewI18n.getString("ReviewIdNewFilePage.label.title.description"));
    this.project = project;
  }

  /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
  public void createControl(Composite ancestor) {
    Composite parent = createsGeneralComposite(ancestor);
    createFileTable(parent);
    //  set the composite as the control for this page
    setControl(parent);
    setPageComplete(isTableNonEmpty());
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
   * Creates the file table.
   * @param composite the composite.
   * @return the control.
   */
  private Control createFileTable(Composite composite) {
    this.fileListTable = new Table(composite, SWT.BORDER | SWT.MULTI);
    FormData fileListTableData = new FormData();
    fileListTableData.left = new FormAttachment(0, 0);
    fileListTableData.right = new FormAttachment(80, 0);
    fileListTableData.top = new FormAttachment(0, 0);
    fileListTableData.bottom = new FormAttachment(100, 0);
    fileListTableData.height = 150;
    fileListTable.setLayoutData(fileListTableData);
    fileListTable.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        handleFileListSelection();
      }
    });
    fillFileTable(this.project.getName());
    
    this.addButtonInFile = new Button(composite, SWT.PUSH);
    String addKey = "ReviewIdEditDialog.label.tab.file.button.add";
    addButtonInFile.setText(ReviewI18n.getString(addKey));
    addButtonInFile.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
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
      public void handleEvent(Event e) {
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
   * Fills the file table with files.
   * @param projectName the project name.
   */
  public void fillFileTable(String projectName) {
    IProject project = FileResource.getProject(projectName);
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    String defaultReviewId = PropertyConstraints.DEFAULT_REVIEW_ID;
    ReviewResource reviewResource = propertyResource.getReviewResource(defaultReviewId, true);
    if (reviewResource != null) {
      this.files = reviewResource.getFileSet();
      for (Iterator<String> i = this.files.iterator(); i.hasNext();) {
        String file = (String) i.next();
        TableItem item = new TableItem(this.fileListTable, SWT.NONE);
        item.setText(file);
      }
    }
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
   * Adds a file to the file list.
   */
  protected void addFile() {
    IWorkbench workbench = PlatformUI.getWorkbench();
    Shell shell = workbench.getActiveWorkbenchWindow().getShell();
    FileFolderSelectionDialog dialog = new FileFolderSelectionDialog(shell, true, IResource.FILE);
    dialog.setTitle(ReviewI18n.getString("ReviewIdEditDialog.label.tab.file.add.title"));
    dialog.setMessage(ReviewI18n.getString("ReviewIdEditDialog.label.tab.file.add.message"));
    //dialog.setInput(project.getLocation().toFile());
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
        String targetFile = project.getFile(projectToFilePath).getProjectRelativePath().toString();
        if (this.files.add(targetFile)) {
          TableItem item = new TableItem(this.fileListTable, SWT.NONE);
          item.setText(targetFile); 
        }
      }
    }
    setPageComplete(isTableNonEmpty());
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
    setPageComplete(isTableNonEmpty());
  }
  
  /**
   * Returns the next page. Saves the values from this page in the model associated with the wizard.
   * Initializes the widgets on the next page.
   * @return the next page.
   */
  public IWizardPage getNextPage() {
    return ((ReviewIdNewWizard) getWizard()).getPage(ReviewIdNewWizard.PAGE_REVIEWER);
  }
  
  /**
   * Checks if the wizard can flip to the next page.
   * @return <code>true</code> if it can flip to the next page.
   */
  public boolean canFlipToNextPage() {
    return isPageComplete();
  }
  
  /**
   * Checks if the table has at least one review file.
   * @return <code>true</code> if the table has at least one review file.
   */
  private boolean isTableNonEmpty() {
//    return (this.files.size() > 0);
    return true;
  }
  
  /**
   * Gets the set of the review files.
   * @return the set of the review files.
   */
  public Set getFiles() {
    return this.files;
  }
}
