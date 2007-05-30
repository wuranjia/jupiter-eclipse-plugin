package edu.hawaii.ics.csdl.jupiter;

import java.net.URL;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import edu.hawaii.ics.csdl.jupiter.event.IReviewListener;
import edu.hawaii.ics.csdl.jupiter.event.ReviewEvent;
import edu.hawaii.ics.csdl.jupiter.event.ReviewIssueModelListenerAdapter;
import edu.hawaii.ics.csdl.jupiter.event.ReviewSelectionListener;
import edu.hawaii.ics.csdl.jupiter.event.WindowListenerAdapter;
import edu.hawaii.ics.csdl.jupiter.file.PrefResource;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModelManager;
import edu.hawaii.ics.csdl.jupiter.ui.marker.MarkerResourceChangeListener;
import edu.hawaii.ics.csdl.jupiter.ui.marker.MarkerTextPartListener;
import edu.hawaii.ics.csdl.jupiter.ui.menu.UndoReviewIssueManager;
import edu.hawaii.ics.csdl.jupiter.ui.preference.FilterPreferencePage;
import edu.hawaii.ics.csdl.jupiter.ui.preference.GeneralPreferencePage;
import edu.hawaii.ics.csdl.jupiter.ui.view.editor.ReviewEditorView;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;
import edu.hawaii.ics.csdl.jupiter.util.PluginVersionCheck;

/**
 * Provides one time Code ReviewIssue plug-in instantiation (singleton). Contains the
 * <code>ReviewIssueModel</code> model instance.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewPlugin extends AbstractUIPlugin implements IStartup {
  /** The log instance to record log */
  private JupiterLogger log = JupiterLogger.getLogger();
  /** The <code>ReviewPlugin</code> singleton instance. */
  private static ReviewPlugin plugin;
  /** The listener container to contains IListener listeners. */
  private ListenerList listenerList = new ListenerList();
  /** The plug-in id .*/
  public static final String PLUGIN_ID = "csdl.jupiter";
  
  /**
   * Instantiates <code>ReviewPlugin</code> itself and initializes resource bundle. 
   * Called by Eclipse platform. Clients should not instantiate this.
   */
  public ReviewPlugin() {
    super();
    plugin = this;
  }
  
  /**
   * Provides this <code>ReviewPlugin</code> instance.
   *
   * @return The <code>ReviewPlugin</code> instance.
   */
  public static ReviewPlugin getInstance() {
    return plugin;
  }

  /**
   * Initializes a preference store with default preference values for this plug-in. This method
   * could be overridden due to AbstractUIPlugin abstract class. It's because it is empty
   * protected method in the AbstractUIPlugin.
   *
   * @param store the preference store to fill
   *
   * @see AbstractUIPlugin
   */
  protected void initializeDefaultPreferences(IPreferenceStore store) {
    // Stores the preference page default values.
    PrefResource prefResource = PrefResource.getInstance();
    String enableUpdateStoreKey = GeneralPreferencePage.ENABLE_UPDATE_KEY;
    store.setDefault(enableUpdateStoreKey, prefResource.getEnableUpdate());
    String updateUrlStoreKey = GeneralPreferencePage.UPDATE_URL_KEY;
    store.setDefault(updateUrlStoreKey, prefResource.getUpdateUrl());
    store.setDefault(FilterPreferencePage.ENABLE_FILTER_STORE_KEY, prefResource.getEnableFilter());
  }

  /**
   * Check if new update plug-in is available on the net.
   *
   * @see org.eclipse.ui.IStartup#earlyStartup()
   */
  public void earlyStartup() {
    ReviewEditorView.setViewEnable(false);
    String enableUpdateStoreKey = GeneralPreferencePage.ENABLE_UPDATE_KEY;
    if (this.getPreferenceStore().getBoolean(enableUpdateStoreKey)) {
      // Avoids the jupiter startup delay due to the version check connection.
      Thread versionCheck = new Thread() {
        public void run() {
          PluginVersionCheck.processUpdateDialog();
        }
      };
      versionCheck.start();
    }
    // Registers selection listener.
    initializeListeners();
    this.log.info("Jupiter Review Plugin is up.");
  }
  
  /**
   * Initializes listeners.
   *
   */
  private void initializeListeners() {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    workspace.addResourceChangeListener(new MarkerResourceChangeListener(),
                                        IResourceChangeEvent.POST_CHANGE);
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    workbench.addWindowListener(new WindowListenerAdapter());
    IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
    for (int i = 0; i < workbenchWindows.length; i++) {
      IWorkbenchPage page = workbench.getWorkbenchWindows()[i].getActivePage();
      try {
        page.addSelectionListener(new ReviewSelectionListener());
        page.addPartListener(new MarkerTextPartListener());
      }
      catch (NullPointerException e) {
        log.warning("Could not register either review selection listener or part listener.");
      }
    }
    ReviewIssueModel model = ReviewIssueModelManager.getInstance().getCurrentModel();
    model.addListener(new ReviewIssueModelListenerAdapter());
    model.addListener(UndoReviewIssueManager.getInstance());
  }

  /**
   * Creates a image descriptor form the given path. The path should be the relative path from a
   * project root.
   *
   * @param path the relative path from a project root.
   *
   * @return the newly created image descriptor.
   */
  public static ImageDescriptor createImageDescriptor(String path) {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }
  
  /**
   * Gets the plugin's install URL.
   * @return the <code>URL</code> instance.
   */
  public URL getInstallURL() {
    return plugin.getBundle().getEntry("/");
  }
  
  /**
   * Gets the <code>Bundle</code> of this plug-in.
   * @return the <code>Bundle</code> of this plug-in.
   */
  public Bundle getReviewBundle() {
    return plugin.getBundle();
  }
  
  /**
   * Gets the <code>PluginVersionIdentifier</code> instance.
   * @return the <code>PluginVersionIdentifier</code> instance.
   */
  public Version getVersionIdentifier() {
    String bundleVersionKey = org.osgi.framework.Constants.BUNDLE_VERSION;
    String version = (String) plugin.getBundle().getHeaders().get(bundleVersionKey);
    return new Version(version);
  }
  
  /**
   * Gets the <code>IWorkspace</code> instance.
   * @return the <code>IWorkspace</code> instance.
   */
  public IWorkspace getWorkspace() {
    return this.getWorkspace();
  }
  
  /**
   * Adds the IReviewListener's implementing class to be notified when This model is
   * changed.
   *
   * @param listener Description of the Parameter
   */
  public void addListener(IReviewListener listener) {
    listenerList.add(listener);
  }

  /**
   * Remove the IReviewListener's implementing class from this listener list.
   *
   * @param listener Description of the Parameter
   */
  public void removeListener(IReviewListener listener) {
    listenerList.remove(listener);
  }
  
  /**
   * Notifies the listeners who implement the <code>IReviewListener</code>. This
   * method can be invoked anytime, but it is designed for the function to let listeners to know
   * the review event is invoked.
   * 
   * @param type the type of the review event.
   * @param kind the kind of the type.
   */
  public void notifyListeners(int type, int kind) {
    log.debug("review event type: " + type);
    log.debug("review event kind: " + kind);
    ReviewEvent event = new ReviewEvent(type, kind);
    Object[] listeners = listenerList.getListeners();
    for (int i = 0; i < listeners.length; i++) {
      ((IReviewListener) listeners[i]).reviewInvoked(event);
    }
  }
}
