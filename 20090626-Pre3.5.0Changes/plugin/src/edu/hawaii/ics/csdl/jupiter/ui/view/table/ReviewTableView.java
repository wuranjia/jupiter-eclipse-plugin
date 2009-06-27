package edu.hawaii.ics.csdl.jupiter.ui.view.table;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.event.IReviewIssueModelListener;
import edu.hawaii.ics.csdl.jupiter.event.ReviewIssueModelEvent;
import edu.hawaii.ics.csdl.jupiter.file.PrefResource;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnData;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnDataModel;
import edu.hawaii.ics.csdl.jupiter.model.columndata.ColumnDataModelManager;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.ui.marker.MarkerSelectionListener;
import edu.hawaii.ics.csdl.jupiter.ui.marker.ReviewMarker;
import edu.hawaii.ics.csdl.jupiter.ui.preference.FilterPreferencePage;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;
import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;
import edu.hawaii.ics.csdl.jupiter.util.ReviewComparator;

/**
 * Provides the Code ReviewIssue View table.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewTableView extends TableViewPart {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();

  /** The <code>ReviewTableView</code> instance. */
  private static ReviewTableView theInstance;
  /** The model to contains the <code>ReviewIssue</code> instances. */
  private ReviewIssueModel model;
  /** The listener to listen to the model change. */
  private ReviewIssueModelChangeListener modelListener;
  /** The listener to listen to the part change. */
  private ReviewTableViewPartListenerAdapter partListener;
  /** The code review filter. */
  private ViewerFilter reviewIssueFilter;
  /** The code review title */
  private String title = "";
  /** The view id */
  private static final String VIEW_ID = ReviewTableView.getViewId();
  /** The marker selection listener. */
  private MarkerSelectionListener markerSelectionListener;

  /**
   * Constructor for the ReviewTableView object.
   */
  public ReviewTableView() {
    super();
    theInstance = this;
    this.modelListener = new ReviewIssueModelChangeListener();
    this.partListener = new ReviewTableViewPartListenerAdapter();
    // this.markerSelectionListener = new MarkerSelectionListener();
    
    model = ReviewIssueModelManager.getInstance().getCurrentModel();
    ReviewModel reviewModel = ReviewModel.getInstance();
    String reviewPhaseNameKey = reviewModel.getPhaseManager().getPhaseNameKey();
    ColumnDataModelManager columnDataModelManager = ColumnDataModelManager.getInstance();
    ColumnDataModel columnDataModel = columnDataModelManager.getModel(reviewPhaseNameKey);
    this.setColumnHeaders(columnDataModel.getEnabledColumnNameArray());
    this.setColumnLayouts(columnDataModel.getEnabledColumnPixelDataArray());
  }
  
  

  /**
   * Provides the instance of the <code>ReviewTableView</code>. Clients should check if
   * the instance is <code>null</code> (this probably would be null if the second opened
   * eclipse were closed).
   *
   * @return The <code>ReviewTableView</code> instance.
   */
  public static ReviewTableView getInstance() {
    return theInstance;
  }
  
  /**
   * Creates table columns based upon <code>ColumnDataManager</code>.
   * @param columnDataManager the <code>ColumnDataManager</code> instance.
   */
  public void createColumns(ColumnDataModel columnDataManager) {
    removeAllColumns();
    this.setColumnHeaders(columnDataManager.getEnabledColumnNameArray());
    this.setColumnLayouts(columnDataManager.getEnabledColumnPixelDataArray());
    createColumns();
  }
  
  /**
   * Removes all table columns in the current view.
   */
  private void removeAllColumns() {
    if (!getViewer().getControl().isDisposed()) {
      TableViewer tableViewer = getViewer();
      tableViewer.setSorter(null);
      Table table = tableViewer.getTable();
      while (table.getColumnCount() > 0) {
        int lastIndex = table.getColumnCount() - 1;
        TableColumn column = table.getColumn(lastIndex);
        column.dispose();
      }
    }
  }
  
  /**
   * Updates column name, width, and resizable information of the set of <code>ColumnData</code>
   * which are stored in <code>ColumnDataManager</code>.
   * @param columnDataManager <code>ColumnDataManager</code> instance.
   * @param codeReviewView the <code>ReviewTableView</code> instance.
   */
  public void updateColumnDataManager(ColumnDataModel columnDataManager,
                                      ReviewTableView codeReviewView) {
    ColumnData[] columnDataArray = columnDataManager.getEnabledColumnDataArray();
    TableViewer tableViewer = codeReviewView.getViewer();
    Table table = tableViewer.getTable();
    int length = table.getColumnCount();
    for (int i = 0; i < length; i++) {
      TableColumn column = table.getColumn(0);
      ColumnPixelData columnPixelData = columnDataArray[i].getColumnPixelData();
      columnPixelData.width = column.getWidth();
      columnPixelData.resizable = column.getResizable();
    }
  }

  /**
   * Creates the Code ReviewIssue View table.
   *
   * @param parent The <code>Composite</code> instance to be hooked.
   */
  public void createPartControl(Composite parent) {
    super.createPartControl(parent);
    TableViewer viewer = getViewer();
    viewer.setContentProvider(model);
    viewer.setLabelProvider(new ViewLabelProvider());
    viewer.setInput(ResourcesPlugin.getWorkspace());
    model.addListener(modelListener);
    model.notifyListeners(ReviewIssueModelEvent.MERGE);
    IWorkbenchPage page = getSite().getPage();
    page.addPartListener(this.partListener);
    //    page.addSelectionListener(this.markerSelectionListener);
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    workspace.addResourceChangeListener(new ProjectResourceChangeListener());
    
    this.title = getTitle();
    updateTitle();
    IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
  }

  /**
   * Fill local pull down menu.
   *
   * @param manager The <code>IMenuManager</code> instance.
   */
  protected void fillLocalPullDown(IMenuManager manager) {
    super.fillLocalPullDown(manager);
    manager.add(ReviewTableViewAction.PREFERENCE_SETTING);
    manager.add(ReviewTableViewAction.PROPERTY_SETTING);
  }

  /**
   * Fills context menu.
   *
   * @param manager The <code>IMenuManager</code> instance.
   */
  protected void fillContextMenu(IMenuManager manager) {
    super.fillContextMenu(manager);
    manager.add(ReviewTableViewAction.GOTO);
    manager.add(ReviewTableViewAction.EDIT);
    manager.add(ReviewTableViewAction.DELETE);
    manager.add(new Separator("Additions"));
  }

  /**
   * Fills local tool bar.
   *
   * @param manager The <code>IToolBarManager</code> instance.
   */
  protected void fillLocalToolBar(IToolBarManager manager) {
    super.fillLocalToolBar(manager);
    manager.add(ReviewTableViewAction.GOTO);
    manager.add(ReviewTableViewAction.EDIT);
    manager.add(ReviewTableViewAction.ADD);
    manager.add(ReviewTableViewAction.DELETE);
    manager.add(ReviewTableViewAction.FILTER);
    manager.add(ReviewTableViewAction.PHASE_SELECTION);
  }
  
  /**
   * Updates the tile of table. Adds the number of displayed item information. Also adds the total
   * number of issues in project if filter button in the Jupiter issues view is selected.
   */
  public void updateTitle() {
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    String projectName = (project != null) ? project.getName() : "";
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    String phaseNameKey = reviewModel.getPhaseManager().getPhaseNameKey();
    ColumnDataModelManager columnDataModelManager = ColumnDataModelManager.getInstance();
    ColumnDataModel columnDataModel = columnDataModelManager.getModel(phaseNameKey);
    String reviewIdName = (reviewId != null) ? " - " + reviewId.getReviewId() : "";
    String reviewPhaseName = ReviewI18n.getString(phaseNameKey);
    updateTitle(projectName, reviewIdName, reviewPhaseName);
  }
  
  /**
   * Clears the title.
   */
  public void clearTitle() {
    updateTitle("", "", "");
  }
  
  /**
   * Updates the title of table.
   * @param projectName the project name.
   * @param reviewIdName the review id name.
   * @param reviewPhaseName the review phase name.
   */
  private void updateTitle(String projectName, String reviewIdName, String reviewPhaseName) {
    int displayedItems = getTable().getItemCount();
    ReviewIssueModelManager reviewIssueModelManager = ReviewIssueModelManager.getInstance();
    ReviewIssueModel reviewIssueModel = reviewIssueModelManager.getCurrentModel();
    int totalItems = reviewIssueModel.size();
    if (ReviewTableViewAction.FILTER.isChecked()) {
      IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
      String prefFilterKey = FilterPreferencePage.ENABLE_FILTER_STORE_KEY;
      boolean isPrefFilterEnabled = store.getBoolean(prefFilterKey);
      String titleFirst = (isPrefFilterEnabled)
                           ? "ReviewTableView.view.title.firstPreference"
                           : "ReviewTableView.view.title.firstProperty";
      setContentDescription(projectName + reviewIdName + " - " + reviewPhaseName + " ("
          + ReviewI18n.getString(titleFirst) + displayedItems
          + ReviewI18n.getString("ReviewTableView.view.title.between") + totalItems
          + ((totalItems <= 1)
          ? ReviewI18n.getString("ReviewTableView.view.title.last.singular")
          : ReviewI18n.getString("ReviewTableView.view.title.last.plural")) + ")");
    }
    else {
      setContentDescription(projectName + reviewIdName + " - " + reviewPhaseName
          + " (" + displayedItems 
          + ((displayedItems <= 1)
          ? ReviewI18n.getString("ReviewTableView.view.title.last.singular")
          : ReviewI18n.getString("ReviewTableView.view.title.last.plural")) + ")");
    }
  }
  
  /**
   * Sets the filter status.
   * @param isFilterEnabled <code>true</code> if filter is enabled.
   */
  public void setFilterStatus(boolean isFilterEnabled) {    
    ReviewTableViewAction.FILTER.setChecked(isFilterEnabled);
    ReviewTableViewAction.FILTER.run();
  }
  
  /**
   * Checks if the filter action button is clicked or not. Returns <code>true</code> if 
   * the action button is clicked. Otherwise returns <code>false</code>.
   * @return <code>true</code> if 
   * the action button is clicked. Otherwise returns <code>false</code>.
   */
  public boolean isFilterActionChecked() {
    return ReviewTableViewAction.FILTER.isChecked();
  }

  /**
   * Creates actions which are to registered in menus.
   */
  protected void createActions() {
    setDoubleClickAction(ReviewTableViewAction.GOTO);
    setSingleClickAction(ReviewTableViewAction.NOTIFY_EDITOR);
  }

  /**
   * Called when column header is selected. Checks the header name and triggers
   * <code>csdl.jupiter.model.ReviewIssueModel.sortBy(Comparator)</code> method.
   *
   * @param event the event triggered when column header is selected.
   */
  protected void columnHeaderSelected(SelectionEvent event) {
    String columnName = ((TableColumn) event.getSource()).getText();
    // if column name is link icon, (i.e. column name is empty), use link icon constant.
    String columnHeaderLinkIcon = ReviewI18n.getString(ResourceBundleKey.COLUMN_HEADER_LINK_ICON);
    String columnNameKey = (!columnName.equals("")) ? columnName
                                                    : columnHeaderLinkIcon;
    this.model.sortBy(ReviewComparator.getComparator(columnNameKey));
    this.getViewer().refresh();
    this.getTable().redraw();
  }
  
  /**
   * Called when column header is resized.
   *
   * @param event the event triggered when column header is resized.
   */
  protected void columnHeaderResized(ControlEvent event) {
    TableColumn column = (TableColumn) event.getSource();
    String columnName = column.getText();
    int width = column.getWidth();
    boolean resizable = column.getResizable();
    
    ColumnDataModelManager columnDataModelManager = ColumnDataModelManager.getInstance();
    ColumnDataModel columnDataModel = columnDataModelManager.getModel();
    
    // if column name is link icon, (i.e. column name is empty), use link icon constant.
    String columnNameKey = (!columnName.equals("")) ? ReviewI18n.getKey(columnName)
                                                    : ResourceBundleKey.COLUMN_HEADER_LINK_ICON;
    ColumnData columnData = columnDataModel.get(columnNameKey);
    // set the resized column information into column data.
    if (columnData != null) {
      columnData.setColumnPixelData(new ColumnPixelData(width, resizable));
    }
    ReviewModel reviewModel = ReviewModel.getInstance();    
    String phaseNameKey = reviewModel.getPhaseManager().getPhaseNameKey();
    // store the modified column data model into config XML file. 
    PrefResource.getInstance().storeColumnDataModel(phaseNameKey, columnDataModel);
  }

  /**
   * Disposes listeners.
   */
  public void dispose() {
    log.debug("review table was disposed...");
    super.dispose();
    model.removeListener(modelListener);
    getViewSite().getPage().removePartListener(this.partListener);
    //    getViewSite().getPage().removeSelectionListener(this.markerSelectionListener);
    theInstance = null;
  }

  /**
   * Brings the code review view to the top no matter when review view is closed or not. Means that
   * it is opened if it has not been opened yet, or it is brought to top if it already has been
   * opened, but not brought to top.
   * 
   * @return the <code>ReviewTableView</code> instance to be opened and/or bright to top.
   */
  public static ReviewTableView bringViewToTop() {
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
    IViewPart viewPart = page.findView(VIEW_ID);
    // if  the review view is not showed yet,
    if (viewPart == null) {
      try {
        viewPart = page.showView(VIEW_ID);
      }
      catch (PartInitException e) {
        throw new IllegalStateException(e.getMessage());
      }
    }
    // if there exists the view, but if not on the top,
    // then brings it to top when the view is already showed.
    else if (!page.isPartVisible(viewPart)) {
      page.bringToTop(viewPart);
    }
    if (!(viewPart instanceof ReviewTableView)) {
      throw new IllegalStateException("viewPart is not instance of ReviewTableView");
    }
    else {
      ((ReviewTableView) viewPart).updateTitle();
      return (ReviewTableView) viewPart;
    }
  }
  
  /**
   * Provides the active view of the Review table view. This active review table view
   * associates with the active window. Returns <code>null</code> if there is no active
   * review table view in the active window.
   * @return the active review editor view. Returns <code>null</code> if there is no active
   * review table view in the active window.
   */
  public static ReviewTableView getActiveView() {
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
    if (page == null) {
      return null;
    }
    IViewPart viewPart = page.findView(VIEW_ID);
    return (ReviewTableView) viewPart;
  }
  
  /**
   * Provides the array of the Review table view. A review table view in the array
   * associates with the opened window. The array might contain <code>null</code> 
   * if there is no active review table view in the active window.
   * @return the array of the review table views. 
   */
  public static ReviewTableView[] getViews() {
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
    ReviewTableView[] views = new ReviewTableView[windows.length];
    for (int i = 0; i < windows.length; i++) {
      IWorkbenchPage page = windows[i].getActivePage();
      if (page == null) {
        continue;
      }
      IViewPart viewPart = page.findView(VIEW_ID);
      views[i] = (ReviewTableView) viewPart;
    }
    return views;
  }

  /**
   * Closes the opened code review view if it's opened.
   */
  public static void closeView() {
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
    if (activeWindow != null) {
      IWorkbenchPage page = activeWindow.getActivePage();
      if (page != null) {
        IViewPart viewPart = page.findView(VIEW_ID);
        if (viewPart != null) {
          page.hideView(viewPart);
        }
      }
    }
  }

  /**
   * Gets the view id of this plug-in. Returns <code>null</code> if a view id was not found.
   *
   * @return the view id of this plug-in.
   */
  private static String getViewId() {
    String pluginId = ReviewPlugin.PLUGIN_ID;
    IExtension[] extensions = Platform.getExtensionRegistry().getExtensions(pluginId);
    for (int i = 0; i < extensions.length; i++) {
      if (extensions[i].getExtensionPointUniqueIdentifier().equals("org.eclipse.ui.views")) {
        IConfigurationElement[] elements = extensions[i].getConfigurationElements();
        for (int j = 0; j < elements.length; j++) {
          if (elements[j].getName().equals("view")) {
            return elements[j].getAttribute("id");
          }
        }
      }
    }
    return null;
  }

  /**
   * Provides the listener to listen to the model elements in the
   * <code>CodeReviewModelProvider</code> instance.
   *
   * @author Takuya Yamashita
   * @version $Id$
   */
  private class ReviewIssueModelChangeListener implements IReviewIssueModelListener {
    /**
     * Notified when code review model was changed so that refresh the table view.
     *
     * @see csdl.jupiter.event.IReviewIssueModelListener
     *      #reviewIssueModelChanged(csdl.jupiter.model.ReviewIssue)
     */
    public void reviewIssueModelChanged(final ReviewIssueModelEvent event) {
      final int merge = ReviewIssueModelEvent.MERGE;
      int add = ReviewIssueModelEvent.ADD;
      int delete = ReviewIssueModelEvent.DELETE;
      int edit = ReviewIssueModelEvent.EDIT;
      final int clear = ReviewIssueModelEvent.CLEAR;
      if ((event.getEventType() & (merge | add | delete | edit | clear)) != 0) {
        getSite().getShell().getDisplay().asyncExec(new Runnable() {
            public void run() {
              if (!getViewer().getControl().isDisposed()) {
                getTable().setRedraw(false);
                getViewer().refresh();
                getTable().setRedraw(true);
                if ((event.getEventType() & clear) != 0) {
                  clearTitle();
                }
                else {
                  updateTitle();
                }
                               
                ReviewIssue changedReviewIssue = event.getReviewIssue();
                ReviewModel reviewModel = ReviewModel.getInstance();
                IProject project = reviewModel.getProjectManager().getProject();
//                if ((event.getEventType() & merge) != 0) {
//                  log.debug("merge mode...........");
//                  ReviewIssueModelManager manager = ReviewIssueModelManager.getInstance();
//                  ReviewIssueModel reviewIssueModel = manager.getCurrentModel();
//                  HashSet targetFileSet = new HashSet();
//                  for (Iterator i = reviewIssueModel.iterator(); i.hasNext();) {
//                    ReviewIssue reviewIssue = (ReviewIssue) i.next();
//                    targetFileSet.add(reviewIssue.getTargetFile());
//                  }
//                  for (Iterator i = targetFileSet.iterator(); i.hasNext();) {
//                    String targetFile = (String) i.next();
//                    log.debug("target file path: " + targetFile);
//                    ReviewMarker.updateMarkers(project.getFile(targetFile));
//                  }
//                }
//                else {
                  if (project != null && changedReviewIssue != null) {
                    String targetFile = changedReviewIssue.getTargetFile();
                    if (!targetFile.equals("")) {
                      ReviewMarker.updateMarkers(project.getFile(targetFile));
                    }
                    ReviewTableViewAction.PROPERTY_SETTING.setEnabled(true);
                  }
//                }
              }
            }
          });
      }
    }
  }
}
