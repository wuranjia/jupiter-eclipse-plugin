package edu.hawaii.ics.csdl.jupiter.ui.property;

import java.text.SimpleDateFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;

/**
 * Provides the review id removal dialog.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdRemovalDialog extends Dialog {
 
  private IFile[] reviewIFiles;
  private Label infoLabel;
  private Table reviewFileTable;
  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Instantiates review id removal dialog.
   * @param parentShell the <code>Shell</code> instance.
   * @param reviewIFiles the array of the review <code>IFile</code> instances.
   */
  protected ReviewIdRemovalDialog(Shell parentShell, IFile[] reviewIFiles) {
    super(parentShell);
    this.reviewIFiles = reviewIFiles;
  }

  /**
   * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
   */
  protected Control createDialogArea(Composite parent) {
    Composite composite = (Composite) super.createDialogArea(parent);
    Composite dialog = createsGeneralComposite(composite);
    createMessageLabelContent(dialog);
    if (this.reviewIFiles.length > 0) {
      createReviewFileTableContent(dialog);
    }
    return composite;
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
   * Creates the message label content.
   * @param dialog the composite.
   */
  private void createMessageLabelContent(Composite dialog) {
    this.infoLabel = new Label(dialog, SWT.NULL);
    String infoMessage = "";
    if (this.reviewIFiles.length > 0) {
      infoMessage = ReviewI18n.getString("ReviewIdRemovalDialog.label.info.file");
    }
    else {
      infoMessage = ReviewI18n.getString("ReviewIdRemovalDialog.label.info.noFile");
    }
    infoMessage += ReviewI18n.getString("ReviewIdRemovalDialog.label.info.confirmation");
    infoLabel.setText(infoMessage);
  }
  
  
  /**
   * Creates the review file table content.
   * @param dialog the composite.
   */
  private void createReviewFileTableContent(Composite dialog) {
    this.reviewFileTable = new Table(dialog, SWT.BORDER);
    FormData reviewFileTableData = new FormData();
    reviewFileTableData.left = new FormAttachment(infoLabel, 0, SWT.LEFT);
    reviewFileTableData.right = new FormAttachment(100, 0);
    reviewFileTableData.top = new FormAttachment(infoLabel, 5);
    reviewFileTableData.bottom = new FormAttachment(100, 0);
    reviewFileTable.setLayoutData(reviewFileTableData);
    reviewFileTable.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
      }
    });
    fillReviewFileTable();
  }

  /**
   * Fills review file table with review ID files.
   */
  private void fillReviewFileTable() {
    for (int i = 0; i < this.reviewIFiles.length; i++) {
      TableItem item = new TableItem(this.reviewFileTable, SWT.NONE);
      item.setText(reviewIFiles[i].getProjectRelativePath().toString());
    }
  }
}
