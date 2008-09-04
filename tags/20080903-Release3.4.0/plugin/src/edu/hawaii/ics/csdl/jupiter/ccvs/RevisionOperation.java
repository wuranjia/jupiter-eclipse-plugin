package edu.hawaii.ics.csdl.jupiter.ccvs;

import java.io.InputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.internal.ccvs.core.CVSException;
import org.eclipse.team.internal.ccvs.core.CVSProviderPlugin;
import org.eclipse.team.internal.ccvs.core.CVSStatus;
import org.eclipse.team.internal.ccvs.core.ICVSFolder;
import org.eclipse.team.internal.ccvs.core.ICVSRemoteFile;
import org.eclipse.team.internal.ccvs.core.ICVSRepositoryLocation;
import org.eclipse.team.internal.ccvs.core.ICVSResource;
import org.eclipse.team.internal.ccvs.core.client.Annotate;
import org.eclipse.team.internal.ccvs.core.client.Command;
import org.eclipse.team.internal.ccvs.core.client.Session;
import org.eclipse.team.internal.ccvs.core.client.listeners.AnnotateListener;
import org.eclipse.team.internal.ccvs.core.connection.CVSServerException;
import org.eclipse.team.internal.ccvs.core.resources.CVSWorkspaceRoot;
import org.eclipse.team.internal.ccvs.core.syncinfo.FolderSyncInfo;
import org.eclipse.team.internal.ccvs.core.util.KnownRepositories;
import org.eclipse.team.internal.ccvs.ui.CVSUIPlugin;
import org.eclipse.team.internal.ccvs.ui.Policy;
import org.eclipse.team.internal.ccvs.ui.operations.CVSOperation;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * An operation to fetch the revision for a file from the repository and display them in an editor.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class RevisionOperation extends CVSOperation {
  private JupiterLogger log = JupiterLogger.getLogger();
  private ICVSResource cvsResource;
  private String revision;
  
  /**
   * Instantiates this with cvsResource and revision.
   * @param cvsResource the cvs resource.
   * @param revision the revision.
   */
  public RevisionOperation(ICVSResource cvsResource, String revision) {
    super(null);
    this.cvsResource = cvsResource;
    this.revision = revision;
  }

  /**
   * @see org.eclipse.team.internal.ccvs.ui.operations.CVSOperation
   *      #execute(org.eclipse.core.runtime.IProgressMonitor)
   */
  protected void execute(IProgressMonitor monitor) throws CVSException, InterruptedException {

    monitor.beginTask(null, 100);

    log.debug("cvsResource: " + cvsResource);
    log.debug("revision: " + revision);

    // Get the annotations from the repository.
    final AnnotateListener listener = new AnnotateListener();
    fetchRevision(listener, cvsResource, revision, Policy.subMonitorFor(monitor, 80));
    //        for (Iterator i = listener.getCvsAnnotateBlocks().iterator(); i.hasNext();) {
    //          log.debug("a block: " + ((CVSAnnotateBlock) i.next()).toString());
    //        }
    //        try {
    //          if (hasCharset(cvsResource, listener.getContents())) {
    //            listener.setContents(getRemoteContents(cvsResource,
//    Policy.subMonitorFor(monitor, 20)));
    //          }
    //        }
    //        catch (CoreException e) {
    //          // Log and continue, using the original fetched contents
    //          CVSUIPlugin.log(e);
    //        }

    // Open the view and display it from the UI thread.
    //        final Display display = getPart().getSite().getShell().getDisplay();
    //        display.asyncExec(new Runnable() {
    //          public void run() {
    //            showView(listener);
    //          }
    //        });
    monitor.done();

    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchWindow activeWindow = workbench.getWorkbenchWindows()[0];

    final InputStream contents = listener.getContents();

    final ICVSRemoteFile file = (ICVSRemoteFile) CVSWorkspaceRoot.getRemoteResourceFor(cvsResource);
    IEditorRegistry registry = CVSUIPlugin.getPlugin().getWorkbench().getEditorRegistry();
    log.debug("file name: " + file.getName());
    IEditorDescriptor descriptor = registry.getDefaultEditor(file.getName());
    final String id;
    if (descriptor != null) {
      log.debug("descriptor is not null");
      id = descriptor.getId();
    }
    else {
      id = IDEWorkbenchPlugin.DEFAULT_TEXT_EDITOR_ID;
    }
    log.debug("before checking page null");
    if (activeWindow != null) {
      final IWorkbenchPage page = activeWindow.getActivePage();
      if (page != null) {
        log.debug("page is not null");
        final Display display = workbench.getDisplay();
        display.asyncExec(new Runnable() {
          public void run() {
//            try {
//              
////              FileEditorInput input = new FileEditorInput((IFile) file.getIResource());
////              page.openEditor(input, id);
////              RemoteAnnotationEditorInput rInput = 
////              new RemoteAnnotationEditorInput(file, contents);
//              page.openEditor(new RemoteAnnotationEditorInput(file, contents), id);
//            }
//            catch (PartInitException e) {
//              // TODO Auto-generated catch block
//              e.printStackTrace();
//            }
          }
        });
      }
    }
  }
  
  /**
   * Fetches the revision.
   * @param listener the listener.
   * @param cvsResource the cvs resource.
   * @param revision the revision.
   * @param monitor the monitor.
   * @throws CVSException if problems occur.
   */
  private void fetchRevision(AnnotateListener listener, ICVSResource cvsResource, String revision,
      IProgressMonitor monitor) throws CVSException {

    monitor = Policy.monitorFor(monitor);
    monitor.beginTask(null, 100);

    final ICVSFolder folder = cvsResource.getParent();
    final FolderSyncInfo info = folder.getFolderSyncInfo();
    final ICVSRepositoryLocation location = KnownRepositories.getInstance().getRepository(
        info.getRoot());

    final Session session = new Session(location, folder, true /* output to console */);
    session.open(Policy.subMonitorFor(monitor, 10), false /* read-only */);
    try {
      final Command.QuietOption quietness = CVSProviderPlugin.getPlugin().getQuietness();
      try {
        CVSProviderPlugin.getPlugin().setQuietness(Command.VERBOSE);
        final Command.LocalOption[] localOption;
        if (revision == null) {
          localOption = Command.NO_LOCAL_OPTIONS;
        }
        else {
          localOption = new Command.LocalOption[1];
          localOption[0] = Annotate.makeRevisionOption(revision);
        }
        final IStatus status = Command.ANNOTATE.execute(session, Command.NO_GLOBAL_OPTIONS,
            localOption, new ICVSResource[]{cvsResource
            }, listener, Policy.subMonitorFor(monitor, 90));
        if (status.getCode() == CVSStatus.SERVER_ERROR) {
          throw new CVSServerException(status);
        }
      }
      finally {
        CVSProviderPlugin.getPlugin().setQuietness(quietness);
        monitor.done();
      }
    }
    finally {
      session.close();
    }
  }

  /**
   * @see org.eclipse.team.internal.ccvs.ui.operations.CVSOperation#getTaskName()
   */
  protected String getTaskName() {
    return "Revision Task";
  } 
}