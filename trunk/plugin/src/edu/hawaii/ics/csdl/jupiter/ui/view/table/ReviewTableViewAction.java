package edu.hawaii.ics.csdl.jupiter.ui.view.table;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.team.internal.ccvs.core.CVSException;
import org.eclipse.team.internal.ccvs.core.ICVSResource;
import org.eclipse.team.internal.ccvs.core.resources.CVSWorkspaceRoot;
import org.eclipse.team.internal.ccvs.core.syncinfo.ResourceSyncInfo;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.PropertyDialog;
import org.eclipse.ui.internal.dialogs.PropertyPageManager;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.ccvs.RevisionOperation;
import edu.hawaii.ics.csdl.jupiter.event.ReviewEvent;
import edu.hawaii.ics.csdl.jupiter.event.ReviewIssueModelEvent;
import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.ui.marker.ReviewMarker;
import edu.hawaii.ics.csdl.jupiter.ui.menu.PhaseSelectionMenu;
import edu.hawaii.ics.csdl.jupiter.ui.menu.ReviewFileSelectionMenu;
import edu.hawaii.ics.csdl.jupiter.ui.preference.FilterPreferencePage;
import edu.hawaii.ics.csdl.jupiter.ui.property.ReviewPropertyPage;
import edu.hawaii.ics.csdl.jupiter.ui.view.editor.ReviewEditorView;
import edu.hawaii.ics.csdl.jupiter.ui.view.editor.ReviewEditorViewAction;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;
import edu.hawaii.ics.csdl.jupiter.util.ReviewDialog;

/**
 * Provides the static view actions.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewTableViewAction {
  /** Jupiter logger */
  private static JupiterLogger log = JupiterLogger.getLogger();

  /** The edit action to edit the selected code review. */
  public static final Action EDIT;
  /** The notify editor action to notify the content of an code review instance. */
  public static final Action NOTIFY_EDITOR;
  /** The go to action to jump to the specified source code */
  public static final Action GOTO;
  /** The go to action to jump to the specified revision source code */
  public static final Action GOTO_REVISION_SOURCE;
  /** The add action to add new Jupiter issue. */
  public static final Action ADD;
  /** The delete action to delete one existing Jupiter issue. */
  public static final Action DELETE;
  /** The review id configuration action to add, edit, and delete review id. */
  public static final Action PROPERTY_SETTING;
  /** The filters action. */
  public static final Action FILTER;
  /** The filter setting action. */
  public static final Action PREFERENCE_SETTING;
  /** The group review action. */
  public static final Action PHASE_SELECTION;
  static {
    PHASE_SELECTION = new Action("", Action.AS_DROP_DOWN_MENU) {
      public void run() {
        ReviewModel reviewModel = ReviewModel.getInstance();
        PhaseSelectionMenu.doUpdateMenuCommand(reviewModel.getPhaseManager().getPhaseNameKey());
        int type = ReviewEvent.TYPE_COMMAND;
        int kind = ReviewEvent.KIND_PHASE_SELECTION;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public IMenuCreator getMenuCreator() {
        return new IMenuCreator() {
          /**
           * Ignore to implement this method.
           */
          public void dispose() {
          }

          /**
           * Returns the menu for this pull down action.
           * 
           * @param parent the parent of <code>Control</code> instance.
           * @return The <code>Menu</code> instance to be filled with the set of the review mode.
           */
          public Menu getMenu(Control parent) {
            return PhaseSelectionMenu.createPhaseSelectionPullDownMenu(new Menu(parent));
          }

          /**
           * Ignore to implement this method.
           * 
           * @param menu The <code>Menu</code> instance.
           * @return The <code>Menu</code> instance.
           */
          public Menu getMenu(Menu menu) {
            return null;
          }
        };
      }
    };
    String groupReviewLabelKey = "ReviewTableViewAction.label.groupReview";
    PHASE_SELECTION.setText(ReviewI18n.getString(groupReviewLabelKey));
    String groupReviewToolTipKey = "ReviewTableViewAction.toolTip.groupReview";
    PHASE_SELECTION.setToolTipText(ReviewI18n.getString(groupReviewToolTipKey));
    ImageDescriptor descriptor = ReviewPlugin.createImageDescriptor("icons/jupiter.gif");
    PHASE_SELECTION.setImageDescriptor(descriptor);
    PREFERENCE_SETTING = new Action() {
      public void run() {
        IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        PreferenceManager manager = workbench.getPreferenceManager();
        IPreferenceNode node = manager.find("edu.hawaii.ics.csdl.jupiter.ui.preference.GeneralPreferencePage");
        //        node = node.findSubNode("edu.hawaii.ics.csdl.jupiter.ui.preference.FilterPreferencePage");
        manager = new PreferenceManager();
        manager.addToRoot(node);
        PreferenceDialog dialog = new PreferenceDialog(window.getShell(), manager);
        dialog.create();
        dialog.open();

        int type = ReviewEvent.TYPE_COMMAND;
        int kind = ReviewEvent.KIND_FILTER_SETTING;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
    };
    String filterSettingLabelKey = "ReviewTableViewAction.label.preferenceSetting";
    PREFERENCE_SETTING.setText(ReviewI18n.getString(filterSettingLabelKey));
    String filterSettingToolTipKey = "ReviewTableViewAction.toolTip.preferenceSetting";
    PREFERENCE_SETTING.setToolTipText(ReviewI18n.getString(filterSettingToolTipKey));
    String iconPath = "icons/preference_config.gif";
    PREFERENCE_SETTING.setImageDescriptor(ReviewPlugin.createImageDescriptor(iconPath));
    String filterLabelKey = "ReviewTableViewAction.label.filter";
    FILTER = new Action(ReviewI18n.getString(filterLabelKey), Action.AS_CHECK_BOX) {
      public void run() {
        ReviewTableView view = ReviewTableView.getActiveView();
        if (view == null) {
          return;
        }
        StructuredViewer viewer = view.getViewer();
        ReviewModel reviewModel = ReviewModel.getInstance();
        IProject project = reviewModel.getProjectManager().getProject();
        ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
        String phaseNameKey = reviewModel.getPhaseManager().getPhaseNameKey();
        if (project == null && reviewId == null) {
          return;
        }

        // if filter is checked, add a filter.
        if (FILTER.isChecked()) {
          IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
          String enableFilterKey = FilterPreferencePage.ENABLE_FILTER_STORE_KEY;
          boolean isPrefFilterEnabled = store.getBoolean(enableFilterKey);
          if (isPrefFilterEnabled) {
            ViewerFilter filter = ReviewFilterManager.createFilterFromPreference();
            ReviewFilterManager filterManager = ReviewFilterManager.getInstance(viewer, filter);
            filterManager.addFilter();
          }
          else {
            ViewerFilter filter = ReviewFilterManager.createFilterFromProperty(project, reviewId,
                phaseNameKey);
            ReviewFilterManager filterManager = ReviewFilterManager.getInstance(viewer, filter);
            filterManager.addFilter();
          }
        }
        else {
          ReviewFilterManager filterManager = ReviewFilterManager.getInstance(viewer);
          filterManager.removeFilter();
        }
        view.updateTitle();
        view.getViewer().refresh();
        view.getTable().redraw();

        ReviewMarker.updateAllMarkersInReviewId();

        int type = ReviewEvent.TYPE_COMMAND;
        int kind = ReviewEvent.KIND_FILTER;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
    };
    String filterToolTipKey = "ReviewTableViewAction.toolTip.filter";
    FILTER.setToolTipText(ReviewI18n.getString(filterToolTipKey));
    FILTER.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/filter.gif"));
    GOTO = new Action("", Action.AS_DROP_DOWN_MENU) {
      public void run() {
        ReviewTableView view = ReviewTableView.getActiveView();
        if (view == null) {
          view = ReviewTableView.bringViewToTop();
        }
        ISelection selection = view.getViewer().getSelection();
        Object object = ((IStructuredSelection) selection).getFirstElement();
        if (object != null) {
          ReviewIssue reviewIssue = (ReviewIssue) object;
          String targetFile = reviewIssue.getTargetFile();
          if (!targetFile.equals("")) {
            IProject project = FileResource.getProject(reviewIssue.getReviewIFile());
            IFile targetIFile = project.getFile(targetFile);
            String lineNumberString = reviewIssue.getLine();
            boolean isNumber = ((lineNumberString != null) && !lineNumberString.equals(""));
            int lineNumber = Integer.parseInt((isNumber) ? lineNumberString : "0");
            FileResource.goToLine(targetIFile, lineNumber);
          }
        }
        else {
          openOneTableItemSelectionDialog();
        }

        int type = ReviewEvent.TYPE_COMMAND;
        int kind = ReviewEvent.KIND_GOTO;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
      public IMenuCreator getMenuCreator() {
        return new IMenuCreator() {
          /** Ignore to implement this method. */
          public void dispose() {
          }

          /**
           * Returns the menu for this pull down action.
           * 
           * @param parent the parent of <code>Control</code> instance.
           * @return The <code>Menu</code> instance to be filled with the set of the review mode.
           */
          public Menu getMenu(Control parent) {
            return ReviewFileSelectionMenu.createPulldownMenu(new Menu(parent));
          }

          /**
           * Ignore to implement this method.
           * 
           * @param menu The <code>Menu</code> instance.
           * @return The <code>Menu</code> instance.
           */
          public Menu getMenu(Menu menu) {
            return null;
          }
        };
      }
    };
    String gotoLabelKey = "ReviewTableViewAction.label.goto";
    GOTO.setText(ReviewI18n.getString(gotoLabelKey));
    String gotoToolTipKey = "ReviewTableViewAction.toolTip.goto";
    GOTO.setToolTipText(ReviewI18n.getString(gotoToolTipKey));
    ISharedImages sharedImage = PlatformUI.getWorkbench().getSharedImages();
    GOTO.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/goto.gif"));

    GOTO_REVISION_SOURCE = new Action() {
      public void run() {
        ReviewTableView view = ReviewTableView.getActiveView();
        ISelection selection = view.getViewer().getSelection();
        Object object = ((IStructuredSelection) selection).getFirstElement();
        if (object == null) {
          return;
        }
        ReviewIssue reviewIssue = (ReviewIssue) object;
        String targetFile = reviewIssue.getTargetFile();
        if (targetFile.equals("")) {
          return;
        }
        IProject project = FileResource.getProject(reviewIssue.getReviewIFile());
        IFile targetIFile = project.getFile(targetFile);
        //          String lineNumberString = reviewIssue.getLine();
        //          boolean isNumber = ((lineNumberString != null) && !lineNumberString.equals(""));
        //          int lineNumber = Integer.parseInt((isNumber) ? lineNumberString : "0");
        //          FileResource.goToLine(targetIFile, lineNumber);

        final ICVSResource cvsResource = CVSWorkspaceRoot.getCVSResourceFor(targetIFile);
        if (cvsResource == null) {
          return;
        }
        String revision = getRevision(cvsResource);
        if (revision == null) {
          return;
        }
        try {
          new RevisionOperation(cvsResource, revision).run();
        }
        catch (InvocationTargetException e) {
          e.printStackTrace();
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      /**
       * Get the revision for the CVS resource. Returns null if the revision
       * could not be determined.
       * 
       * @param cvsResource The CVS resource
       * @return The revision of the resource.
       */
      private String getRevision(ICVSResource cvsResource) {
        final ResourceSyncInfo info;
        try {
          info = cvsResource.getSyncInfo();
          if (info == null) {
            return null;
          }
        }
        catch (CVSException e) {
          return null;
        }
        return info.getRevision();
      }
    };

    EDIT = new Action() {
      public void run() {
        GOTO_REVISION_SOURCE.run();
        ReviewTableView view = ReviewTableView.getActiveView();
        ISelection selection = view.getViewer().getSelection();
        Object object = ((IStructuredSelection) selection).getFirstElement();
        IWorkbenchPage page = view.getViewSite().getPage();
        if (object != null) {
          try {
            ReviewEditorView editorView = ReviewEditorView.bringViewToTop();
            editorView.setReviewIssue((ReviewIssue) object);
            editorView.setEnable(true);
          }
          catch (ReviewException e) {
            return;
          }
          ReviewEditorViewAction.updateNextPreviousIcon();
          int type = ReviewEvent.TYPE_COMMAND;
          int kind = ReviewEvent.KIND_EDIT;
          ReviewPlugin.getInstance().notifyListeners(type, kind);
        }
        else {
          openOneTableItemSelectionDialog();
        }
      }
    };
    String editLabelKey = "ReviewTableViewAction.label.edit";
    EDIT.setText(ReviewI18n.getString(editLabelKey));
    String editToolTipKey = "ReviewTableViewAction.toolTip.edit";
    EDIT.setToolTipText(ReviewI18n.getString(editToolTipKey));
    EDIT.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/edit.gif"));
    NOTIFY_EDITOR = new Action() {
      private IProject previousProject = null;
      private ReviewId previousReviewId = null;
      public void run() {
        ReviewTableView view = ReviewTableView.getActiveView();
        if (view == null) {
          log.warning("Review table view is null.");
          return;
        }
        TableViewer viewer = view.getViewer();
        ISelection selection = viewer.getSelection();
        Object object = ((IStructuredSelection) selection).getFirstElement();        
        ReviewEditorView[] editorViews = ReviewEditorView.getViews();
        ReviewModel reviewModel = ReviewModel.getInstance();
        IProject project = reviewModel.getProjectManager().getProject();
        ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
        for (int i = 0; i < editorViews.length; i++) {
          ReviewEditorView editorView = editorViews[i];
          if (editorView != null && !editorView.isDisposed()) {
            if (object != null && project != null && reviewId != null) {
              ReviewIssue notifyingReviewIssue = (ReviewIssue) object;
              if (previousProject != project || previousReviewId != reviewId) {
                editorView.setItemFields(project, reviewId);
                previousProject = project;
                previousReviewId = reviewId;
              }
              editorView.setReviewIssue(notifyingReviewIssue);
              editorView.setEnable(true);
            }
            else {
              editorView.clearAllFields();
              editorView.setEnable(false);
            }
          }
        }
        ReviewEditorViewAction.updateNextPreviousIcon();
      }
    };
    ADD = new Action() {
      public void run() {
        ReviewModel reviewModel = ReviewModel.getInstance();
        String reviewPhaseNameKey = reviewModel.getPhaseManager().getPhaseNameKey();
        IProject project = reviewModel.getProjectManager().getProject();
        ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
        if (project == null || reviewId == null) {
          boolean isSuccess = PhaseSelectionMenu.doUpdateMenuCommand(reviewPhaseNameKey);
          if (!isSuccess) {
            return;
          }
        }

        reviewModel = ReviewModel.getInstance();
        project = reviewModel.getProjectManager().getProject();
        reviewId = reviewModel.getReviewIdManager().getReviewId();
        ReviewerId reviewerId = reviewModel.getReviewerIdManager().getReviewerId();
        IFile iReviewFile = FileResource.getReviewFile(project, reviewId, reviewerId);
        if (iReviewFile == null) {
          String titleKey = "ReviewDialog.noReviewFileDetermined.simpleConfirm.messageDialog.title";
          String title = ReviewI18n.getString(titleKey);
          String messageKey = "ReviewDialog.noReviewFileDetermined.simpleConfirm"
              + ".messageDialog.message";
          String message = ReviewI18n.getString(messageKey);
          ReviewDialog.openSimpleComfirmMessageDialog(title, message);
          log.debug(message);
          return;
        }
        ReviewEditorView editorView;
        try {
          editorView = ReviewEditorView.bringViewToTop();
          editorView.setEnable(true);
          editorView.setNewEmptyReviewIssue(iReviewFile);
          editorView.setItemFields(project, reviewId);
          editorView.setFocus();
        }
        catch (ReviewException e) {
          log.debug(e.getMessage());
        }

        int type = ReviewEvent.TYPE_COMMAND;
        int kind = ReviewEvent.KIND_ADD;
        ReviewPlugin.getInstance().notifyListeners(type, kind);
      }
    };
    String addLabelKey = "ReviewTableViewAction.label.add";
    ADD.setText(ReviewI18n.getString(addLabelKey));
    String addToolTipKey = "ReviewTableViewAction.toolTip.add";
    ADD.setToolTipText(ReviewI18n.getString(addToolTipKey));
    
    ADD.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/add.gif"));
    DELETE = new Action() {
      public void run() {
        ReviewTableView view = ReviewTableView.getActiveView();
        ISelection selection = view.getViewer().getSelection();
        IStructuredSelection structureSelection = (IStructuredSelection) selection;
        Table table = view.getTable();
        int deletingIndex = table.getSelectionIndex();
        log.debug("structure selection size: " + structureSelection.size());
        if ((structureSelection.size() > 0)
            && MessageDialog.openConfirm(view.getSite().getShell(), ReviewI18n
                .getString("ReviewTableView.messageDialog.confirm.title"), ReviewI18n
                .getString("ReviewTableView.messageDialog.confirm.message"))) {
          ReviewIssueModel model = ReviewIssueModelManager.getInstance().getCurrentModel();
          for (Iterator i = ((IStructuredSelection) selection).iterator(); i.hasNext();) {
            model.remove((ReviewIssue) i.next());
            model.notifyListeners(ReviewIssueModelEvent.DELETE);
          }
          view.getViewer().refresh();
          table = view.getTable();
          int length = table.getItemCount();
          if (length > 0) {
            int index = (deletingIndex < length) ? deletingIndex : deletingIndex - 1;
            table.select(index);
            ReviewTableViewAction.NOTIFY_EDITOR.run();
          }
          else {
            ReviewEditorView.setViewEnable(false);
          }
          int type = ReviewEvent.TYPE_COMMAND;
          int kind = ReviewEvent.KIND_DELETE;
          ReviewPlugin.getInstance().notifyListeners(type, kind);
        }
        else {
          openOneTableItemSelectionDialog();
        }
      }
    };
    String deleteLabelKey = "ReviewTableViewAction.label.delete";
    DELETE.setText(ReviewI18n.getString(deleteLabelKey));
    String deleteToolTipKey = "ReviewTableViewAction.toolTip.delete";
    DELETE.setToolTipText(ReviewI18n.getString(deleteToolTipKey));
    DELETE.setImageDescriptor(ReviewPlugin.createImageDescriptor("icons/remove.gif"));
    PROPERTY_SETTING = new Action() {
      public void run() {
        ReviewModel reviewModel = ReviewModel.getInstance();
        final IProject project = reviewModel.getProjectManager().getProject();
        ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
        String phaseNameKey = reviewModel.getPhaseManager().getPhaseNameKey();
        if (project == null || reviewId == null) {
          return;
        }
        Shell shell = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell();

        //        How to open the all property pages of a project.
        //        new PropertyDialogAction(shell, new ISelectionProvider() {
        //          protected IStructuredSelection projectSelection = 
//        new StructuredSelection(project);
        //          public void addSelectionChangedListener(ISelectionChangedListener listener) {
        //          }
        //          public ISelection getSelection() {
        //            return projectSelection;
        //          }
        //          public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        //          }
        //          public void setSelection(ISelection selection) {
        //          }
        //        }).run();

        PropertyPageManager pageManager = new PropertyPageManager();
        ReviewPropertyPage reviewPropertyPage = new ReviewPropertyPage();
        reviewPropertyPage.setElement(project);
        String propertyTitleKey = "ReviewTableViewAction.label.property.title";
        String propertyTitle = ReviewI18n.getString(propertyTitleKey);
        reviewPropertyPage.setTitle(propertyTitle);
        String propertyId = "edu.hawaii.ics.csdl.jupiter.ui.property.ReviewPropertyPage";
        PreferenceNode node = new PreferenceNode(propertyId, reviewPropertyPage);
        pageManager.addToRoot(node);
        IStructuredSelection selection = new StructuredSelection(project);
        PropertyDialog propertyDialog = new PropertyDialog(shell, pageManager, selection);
        propertyDialog.create();
        String shellTitleBeforeKey = "ReviewTableViewAction.label.property.shellTitle.before";
        String shellTitleBefore = ReviewI18n.getString(shellTitleBeforeKey);
        String shellTitleAfterKey = "ReviewTableViewAction.label.property.shellTitle.after";
        String shellTitleAfter = ReviewI18n.getString(shellTitleAfterKey);
        String projectName = project.getName();
        propertyDialog.getShell().setText(shellTitleBefore + projectName + shellTitleAfter);
        propertyDialog.open();
      }
    };
    String configLabelKey = "ReviewTableViewAction.label.property";
    PROPERTY_SETTING.setText(ReviewI18n.getString(configLabelKey));
    String configToolTipKey = "ReviewTableViewAction.toolTip.property";
    PROPERTY_SETTING.setToolTipText(ReviewI18n.getString(configToolTipKey));
    String reviewIdConfigIconPath = "icons/property_config.gif";
    PROPERTY_SETTING.setImageDescriptor(ReviewPlugin.createImageDescriptor(reviewIdConfigIconPath));
  }

  /**
   * Opens one table item selection dialog.
   */
  private static void openOneTableItemSelectionDialog() {
    String titleKey = "ReviewTableViewAction.oneItemSelection.messageDialog.title";
    String messageKey = "ReviewTableViewAction.oneItemSelection.messageDialog.message";
    String title = ReviewI18n.getString(titleKey);
    String message = ReviewI18n.getString(messageKey);
    ReviewDialog.openSimpleComfirmMessageDialog(title, message);
  }
}