package edu.hawaii.ics.csdl.jupiter.ui.wizard;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import edu.hawaii.ics.csdl.jupiter.file.PropertyConstraints;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.file.ReviewResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides config wizard.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIdNewWizard extends Wizard implements INewWizard {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  /** The review id page. */
  public static final String PAGE_REVIEW_ID = "ReviewId";
  /** The reviewer page. */
  public static final String PAGE_REVIEWER = "ReviewerId";
  /** The author page. */
  public static final String PAGE_AUTHOR = "Author";
  /** The file page. */
  public static final String PAGE_FILE = "File";
  /** The storage page. */
  public static final String PAGE_STORAGE = "Storage";
  /** The default items page. */
  public static final String PAGE_DEFAULT_ITEMS = "DefaultItems";
  /** The item entries page. */
  public static final String PAGE_ITEM_ENTRIES = "ItemEntries";
  /** The filter page. */
  public static final String PAGE_FILTERS = "Filters";
  
  private ReviewIdNewReviewIdPage reviewIdPage;
  private ReviewIdNewFilePage filePage;
  private ReviewIdNewReviewerPage reviewerPage;
  private ReviewIdNewAuthorPage authorPage;
  private ReviewIdNewDefaultItemsPage defaultItemsPage;
  private ReviewIdNewItemEntriesPage itemEntriesPage;
  private ReviewIdNewStoragePage storagePage;
  private ReviewIdNewFilterPage filterPage;
  private static final String IMAGE_PATH = "icons/review_id_config_wiz.gif";
  private IProject project;
  private IWorkbench workbench;
  
  /**
   * Instantiates this with the <code>IProject</code> instance.
   * @param project the <code>IProject</code> instance.
   */
  public ReviewIdNewWizard(IProject project) {
    this.project = project;
  }

  /**
   * Performs finish processing for the wizard.
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  public boolean performFinish() {
    Map<String, List<String>> categoryMap = new TreeMap<String, List<String>>();
    PropertyResource propertyResource = PropertyResource.getInstance(project, true);
    String defaultReviewID =  PropertyConstraints.DEFAULT_REVIEW_ID;
    ReviewResource reviewResource = propertyResource.getReviewResource(defaultReviewID, true);
    if (reviewResource != null) {
      Map<String, ReviewerId> reviewers = reviewerPage.getReviewers();
      String reviewIdString = reviewIdPage.getReviewId();
      String description = reviewIdPage.getDescription();
      String author = authorPage.getAuthor();
      String directory = storagePage.getDirectory();
      try {
        Date date = new Date();
        ReviewId reviewId = new ReviewId(reviewIdString, description, author,
                                         directory, reviewers, categoryMap, date);
        reviewResource.setReviewId(reviewId);
        reviewResource.setTargetFiles(filePage.getFiles());
        String typeKey = PropertyConstraints.ATTRIBUTE_VALUE_TYPE;
        reviewResource.setDefaultField(typeKey, defaultItemsPage.getDefaultTypeKey());
        String severityKey = PropertyConstraints.ATTRIBUTE_VALUE_SEVERITY;
        reviewResource.setDefaultField(severityKey, defaultItemsPage.getDefaultSeverityKey());
        String resolutionKey = PropertyConstraints.ATTRIBUTE_VALUE_RESOLUTION;
        reviewResource.setDefaultField(resolutionKey, defaultItemsPage.getDefaultResolutionKey());
        String statusKey = PropertyConstraints.ATTRIBUTE_VALUE_STATUS;
        reviewResource.setDefaultField(statusKey, defaultItemsPage.getDefaultStatusKey());
        reviewResource.setFieldItemMap(this.itemEntriesPage.getFieldItemIdFieldItemMap());
        reviewResource.setPhaseNameFilterPhaseMap(this.filterPage.getPhaseNameFilterPhaseMap());
        return propertyResource.addReviewResource(reviewResource);
      }
      catch (Exception e) {
        log.error(e);
        e.printStackTrace();
        MessageDialog.openInformation(workbench.getActiveWorkbenchWindow().getShell(), 
            "Error info", "Could not create review id");
      }
    }
    return false;
  }
  
  /**
   * Initializes the wizard.
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
   *  org.eclipse.jface.viewers.IStructuredSelection)
   */
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.workbench = workbench;
  }

  /**
   * Adds pages.
   * @see org.eclipse.jface.wizard.IWizard#addPages()
   */
  public void addPages() {
    reviewIdPage = new ReviewIdNewReviewIdPage(this.workbench, PAGE_REVIEW_ID, this.project,
                                                   IMAGE_PATH);
    filePage = new ReviewIdNewFilePage(this.project, PAGE_FILE, IMAGE_PATH);
    reviewerPage = new ReviewIdNewReviewerPage(this.project, PAGE_REVIEWER, IMAGE_PATH);
    authorPage = new ReviewIdNewAuthorPage(this.project, PAGE_AUTHOR, IMAGE_PATH);
    itemEntriesPage = new ReviewIdNewItemEntriesPage(this.project, PAGE_ITEM_ENTRIES,
                                                     IMAGE_PATH);
    defaultItemsPage = new ReviewIdNewDefaultItemsPage(this.project, PAGE_DEFAULT_ITEMS,
                                                           IMAGE_PATH);
    storagePage = new ReviewIdNewStoragePage(this.project, PAGE_STORAGE, IMAGE_PATH);
    filterPage = new ReviewIdNewFilterPage(this.project, PAGE_FILTERS, IMAGE_PATH);
    addPage(reviewIdPage);
    addPage(filePage);
    addPage(reviewerPage);
    addPage(authorPage);
    addPage(itemEntriesPage);
    addPage(defaultItemsPage);
    addPage(storagePage);
    addPage(filterPage);
  }
  
  /**
   * Checks if the wizard can finish.
   * @see org.eclipse.jface.wizard.IWizard#canFinish()
   */
  public boolean canFinish() {
//    return (this.reviewId != null && ((this.reviewers != null) && this.reviewers.size() > 0)
//            && this.author != null);
    return reviewIdPage.isPageComplete() && reviewerPage.isPageComplete();
  }
}
