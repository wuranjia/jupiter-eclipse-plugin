package edu.hawaii.ics.csdl.jupiter.util;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.update.ui.UpdateManagerUI;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.PropertyResource;
import edu.hawaii.ics.csdl.jupiter.ui.wizard.ReviewIdNewWizard;
import edu.hawaii.ics.csdl.jupiter.ui.wizard.ReviewIdSelectionWizard;

/**
 * Provides the utility of the code review dialogs.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewDialog {
  private static IWorkbench workbench = PlatformUI.getWorkbench();
  /**
   * Processes non review ID notification dialog.
   * @param projectName the project name to be shown.
   * @return the return code of the message dialog.
   */
  public static int processNonReviewIdNotificationDialog(String projectName) {
    String jupiterIconKey = "ReviewDialog.jupiterIcon";
    final String jupiterIcon = ReviewI18n.getString(jupiterIconKey);
    String messagePrefix = "ReviewDialog.nonReviewIdNotification.messageDialog.message.prefix";
    String messageSuffix = "ReviewDialog.nonReviewIdNotification.messageDialog.message.suffix";
    String create = "ReviewDialog.nonReviewIdNotification.messageDialog.button.create";
    String cancel = "ReviewDialog.nonReviewIdNotification.messageDialog.button.cancel";
    
    Shell shell = workbench.getWorkbenchWindows()[0].getShell();
    String title = ReviewI18n.getString("ReviewDialog.nonReviewIdNotification.messageDialog.title");
    Image image = ReviewPlugin.createImageDescriptor(jupiterIcon).createImage(); 
    
    // Image might not be created if debugging Jupiter in Eclipse.
    if (image == null) {
    	
    }
    MessageDialog dialog = new MessageDialog(shell,
        title,
        image,
        ReviewI18n.getString(messagePrefix) + projectName + ReviewI18n.getString(messageSuffix),
        MessageDialog.INFORMATION,
        new String[] {ReviewI18n.getString(create), ReviewI18n.getString(cancel)},
        0);
    return dialog.open();
  }

  /**
   * Processes configuration wizard dialog.
   * @param project the project.
   * @return the return code of the dialog.
   */
  public static int processConfigWizardDialog(IProject project) {
    INewWizard wizard = new ReviewIdNewWizard(project);
    wizard.init(workbench, null);
    // Instantiates the wizard container with the wizard and opens it
    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
    WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
    dialog.create();
    return dialog.open();
  }
  
  /**
   * Processes version check dialog.
   * @param localVersionId the local version ID.
   * @param serverVersionId the server version ID.
   * @return the result code of the dialog.
   */
  public static int processVersionCheckDialog(String localVersionId, String serverVersionId) {
    String jupiterIconKey = "ReviewDialog.jupiterIcon";
    final String jupiterIcon = ReviewI18n.getString(jupiterIconKey);
    MessageDialog dialog = new MessageDialog(workbench.getWorkbenchWindows()[0].getShell(),
        ReviewI18n.getString("ReviewDialog.versionCheck.messageDialog.title"),
        ReviewPlugin.createImageDescriptor(jupiterIcon).createImage(),
        ReviewI18n.getString("ReviewDialog.versionCheck.messageDialog.message.first")
        + localVersionId
        + ReviewI18n.getString("ReviewDialog.versionCheck.messageDialog.message.between")
        + serverVersionId
        + ReviewI18n.getString("ReviewDialog.versionCheck.messageDialog.message.last"),
        MessageDialog.QUESTION,
        new String[] {
          ReviewI18n.getString("ReviewDialog.versionCheck.messageDialog.button.update"),
          ReviewI18n.getString("ReviewDialog.versionCheck.messageDialog.button.cancel")
          },
        0);
    return dialog.open();
  }
  
  /**
   * Processes non project selection notification dialog.
   * @return the result code of the dialog.
   */
  public static int processNonProjectSelectionDialog() {
    String messageKey = "ReviewDialog.nonProjectSelection.messageDialog.info.message";
    boolean result = MessageDialog.openConfirm(workbench.getWorkbenchWindows()[0].getShell(),
        ReviewI18n.getString("ReviewDialog.nonProjectSelection.messageDialog.info.title"),
        ReviewI18n.getString(messageKey));
    return (result) ? WizardDialog.OK : WizardDialog.CANCEL;
  }
  
  /**
   * Processes unspecified field notification dialog.
   * @param message the message to be shown in the dialog.
   * @return the result code of the dialog.
   */
  public static int processUnspecifiedFieldNotificationDialog(String message) {
    String titleKey = "ReviewDialog.unspecifiedFieldNotification.messageDialog.info.title";
    String messageKey = "ReviewDialog.unspecifiedFieldNotification.messageDialog.info.message";
    boolean result = MessageDialog.openConfirm(workbench.getWorkbenchWindows()[0].getShell(),
        ReviewI18n.getString(titleKey), 
        ReviewI18n.getString(messageKey)
        + "\n" + message);
    return (result) ? WizardDialog.OK : WizardDialog.CANCEL;
  }

  /**
   * Processes review id selection wizard. The default project would be based upon the selected 
   * project, which can be given <code>FileResource.getSelectedProject()</code>.
   * After this wizard is finished, 1) review ID instance (determined by project name and review id)
   * will be set to content provider model, 2) reviewer ID string was set to preference store.
   * <p>
   * Clients should not pass <code>IProject</code> to be null. If so,
   * <code>IllegalArgumentException</code> will be thrown.
   * 
   * @param reviewPhaseNameKey the review phase name key.
   * @param project the project to be shown as a default in the dialog.
   * @param isMultipleChoiceEnabled set <code>true</code> if the project choice is mulitpile.
   * <code>false</code> if the project choice is only the passing project.
   * @return the result code of the dialog. e.g WizardDialog.CANEL. 
   * See <code>WizardDialog</code> constants.
   */
  public static int processReviewIdSelectionWizardDialog(String reviewPhaseNameKey, 
                                                         IProject project, 
                                                         boolean isMultipleChoiceEnabled) {
    if (project == null) {
      throw new IllegalArgumentException("project should not be null.");
    }
    // process review id creation wizard if there is no review ID in the project.
    List reviewIdList = PropertyResource.getInstance(project, false).getReviewIdList();
    if (reviewIdList.size() <= 0) {
      int result = processNonReviewIdNotificationDialog(project.getName());
      if (result == WizardDialog.CANCEL) {
        return WizardDialog.CANCEL;
      }
      result = processConfigWizardDialog(project);
      if (result == WizardDialog.CANCEL) {
        return WizardDialog.CANCEL;
      }
    }
    
    INewWizard wizard = new ReviewIdSelectionWizard(reviewPhaseNameKey, project, 
                                                    isMultipleChoiceEnabled);
    IWorkbench workbench = PlatformUI.getWorkbench();
    wizard.init(workbench, null);
    // Instantiates the wizard container with the wizard and opens it
    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
    WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
    dialog.create();
    return dialog.open();
  }
  
  /**
   * Opens simple confirm message dialog.
   * 
   * @param title the title for the dialog.
   * @param message the message for the dialog.
   */
  public static void openSimpleComfirmMessageDialog(String title, String message) {
    MessageDialog.openConfirm(workbench.getWorkbenchWindows()[0].getShell(), title, message);
  }

  /**
   * Opens new update wizard and process update.
   */
  public static void proccessOpenNewUpdatesWizard() { 
	 
	  
    IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
    IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
    if (activeWindow != null) {
      Shell shell = workbench.getActiveWorkbenchWindow().getShell();
      
      UpdateManagerUI.openInstaller(shell);
      
      /*IWizard wizard = new InstallWizard(null);
      WizardDialog dialog = new ResizableInstallWizardDialog(shell, wizard, "");
      dialog.create();
      //dialog.getShell().setText(UpdateUI.getString("InstallWizardAction.title")); //$NON-NLS-1$
      dialog.getShell().setText(UpdateUI.getPluginId());
      dialog.getShell().setSize(600, 500);
      dialog.open();*/
    }
  }
}
