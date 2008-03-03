package edu.hawaii.ics.csdl.jupiter.ui.view.table;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

/**
 * Provides the implementation for table view.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class TableViewPart extends ViewPart {
  /** The TAG_COLUMN type. */
  protected static final String TAG_COLUMN = "column";
  /** The TAG_NUMBER type. */
  protected static final String TAG_NUMBER = "number";
  /** The TAG_NAME type, which means column header name. */
  protected static final String TAG_NAME = "name";
  /** The TAG_WIDTH type. */
  protected static final String TAG_WIDTH = "width";
  /** The array of the column header string. */
  private String[] columnHeaders;
  /** The array of the column layouts */
  private ColumnLayoutData[] columnLayouts;
  /** The double click action. */
  private IAction doubleClickAction;
  /** The single click action. */
  private IAction singleClickAction;
  /** The <code>Table</code> instance. */
  private Table table;
  /** The <code>TableViewer</code> instance. */
  private TableViewer viewer;

  /**
   * Creates table view part.
   *
   * @param parent The <code>Composite</code> instance to be hooked.
   */
  public void createPartControl(Composite parent) {
    viewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
    table = viewer.getTable();
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    createColumns();
    createActions();
    hookMenus();
    hookEvents();
    contributeToActionBars();
  }

  /**
   * Creates columns of the table view.
   */
  protected void createColumns() {
    TableLayout layout = new TableLayout();
    table.setLayout(layout);
    for (int i = 0; i < columnHeaders.length; i++) {
      final TableColumn tableColumn = new TableColumn(table, SWT.NONE, i);
      tableColumn.setText(columnHeaders[i]);
      tableColumn.setResizable(columnLayouts[i].resizable);
      // :SOLVED: 4/12/04 sets column width.
      if (columnLayouts[i] instanceof ColumnPixelData) {
        ColumnPixelData columnPixeldata = (ColumnPixelData) columnLayouts[i];
        tableColumn.setWidth(columnPixeldata.width);
      }
      tableColumn.addControlListener(new ControlListener() {
        public void controlMoved(ControlEvent event) {       
        }
        public void controlResized(ControlEvent event) {
          TableViewPart.this.columnHeaderResized(event);
        }
      });
      tableColumn.addSelectionListener(new SelectionListener() {
          public void widgetSelected(SelectionEvent event) {
            TableViewPart.this.columnHeaderSelected(event);
          }

          public void widgetDefaultSelected(SelectionEvent e) {
            // nothing so for.
          }
        });
      layout.addColumnData(columnLayouts[i]);
    }
  }

  /**
   * Hooks pop up menus, and add menu listener to it in order to fill context menu. The listener is
   * notified when the menu is about to show.
   */
  protected void hookMenus() {
    MenuManager menuManager = new MenuManager("#PopupMenu");
    menuManager.setRemoveAllWhenShown(true);
    menuManager.addMenuListener(new IMenuListener() {
        public void menuAboutToShow(IMenuManager manager) {
          TableViewPart.this.fillContextMenu(manager);
        }
      });

    Menu menu = menuManager.createContextMenu(viewer.getControl());
    viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuManager, viewer);
  }

  /**
   * Hooks event listeners to listen to events. When selection is changed, the
   * <code>selectionChanged</code> is called. When the each row is clicked, the
   * <code>doubleClickAction</code> runs.
   */
  protected void hookEvents() {
    viewer.addSelectionChangedListener(new ISelectionChangedListener() {
        public void selectionChanged(SelectionChangedEvent event) {
          if (event.getSelection() != null) {
            TableViewPart.this.selectionChanged(event);
            if (singleClickAction != null) {
              singleClickAction.run();
            }
          }
        }
      });
    viewer.addDoubleClickListener(new IDoubleClickListener() {
        public void doubleClick(DoubleClickEvent event) {
          doubleClickAction.run();
        }
      });
  }

  /**
   * Fills the action bars. The pull down menu and tool bar menu are filled.
   */
  protected void contributeToActionBars() {
    IActionBars bars = getViewSite().getActionBars();
    fillLocalPullDown(bars.getMenuManager());
    fillLocalToolBar(bars.getToolBarManager());
  }

  /**
   * Initialized the table view part. Called by Eclipse platform. Clients should not call this.
   *
   * @param site The <code>IViewSite</code> instance.
   * @param memento The <code>IMemento</code> instance.
   *
   * @exception PartInitException if problems occur.
   */
  public void init(IViewSite site, IMemento memento) throws PartInitException {
    super.init(site, memento);
  }

  /**
   * Asks this part to take focus within the workbench.
   */
  public void setFocus() {
    viewer.getControl().setFocus();
  }

  /**
   * Gets the <code>Table</code> instance.
   *
   * @return The <code>Table</code> instance.
   */
  public Table getTable() {
    return table;
  }

  /**
   * Gets the <code>TableViewer</code> instance.
   *
   * @return The <code>TableViewer</code> instance.
   */
  public TableViewer getViewer() {
    return viewer;
  }

  /**
   * Sets the array of the column header strings.
   *
   * @param strings The array of the column header strings.
   */
  public void setColumnHeaders(String[] strings) {
    columnHeaders = strings;
  }

  /**
   * Sets the array of the column layouts.
   *
   * @param data the array of the column layouts.
   */
  public void setColumnLayouts(ColumnLayoutData[] data) {
    columnLayouts = data;
  }

  /**
   * Sets the double click action.
   *
   * @param action The <code>IAction</code> instance.
   */
  public void setDoubleClickAction(IAction action) {
    doubleClickAction = action;
  }

  /**
   * Sets the single click action.
   *
   * @param action The <code>IAction</code> instance.
   */
  public void setSingleClickAction(IAction action) {
    singleClickAction = action;
  }

  /**
   * Fills the context menu. Sub extending class should implements this.
   *
   * @param manager The <code>IMenuManager</code> instance.
   */
  protected void fillContextMenu(IMenuManager manager) {
  }

  /**
   * Fills the pull down menu. Sub extending class should implements this.
   *
   * @param manager The <code>IMenuManager</code> instance.
   */
  protected void fillLocalPullDown(IMenuManager manager) {
  }

  /**
   * Fills the tool bar menu. Sub extending class should implements this.
   *
   * @param manager The <code>IMenuManager</code> instance.
   */
  protected void fillLocalToolBar(IToolBarManager manager) {
  }

  /**
   * Called when selection of the row data is changed. Sub extending class should implements this.
   *
   * @param event The <code>SelectionChangedEvent</code> instance.
   */
  protected void selectionChanged(SelectionChangedEvent event) {
  }

  /**
   * Called when the column header of the table view is selected.. Sub extending class can listen
   * to the event triggered in this case.
   *
   * @param event the <code>SelectionEvent</code> instance.
   */
  protected void columnHeaderSelected(SelectionEvent event) {
  }
  
  /**
   * Called when the column header of the table view is resized. Sub extending class can listen
   * to the event triggered in this case.
   *
   * @param event the <code>ControlEvent</code> instance.
   */
  protected void columnHeaderResized(ControlEvent event) {
  }

  /**
   * Creates actions to be triggered when menus are selected. Sub extending class should implements
   * this.
   */
  protected void createActions() {
  }
}
