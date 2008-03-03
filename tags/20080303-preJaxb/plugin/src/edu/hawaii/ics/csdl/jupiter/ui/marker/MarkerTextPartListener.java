package edu.hawaii.ics.csdl.jupiter.ui.marker;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;


/**
 * Provides the eclipse part editor listener to grab the text part open event.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class MarkerTextPartListener implements IPartListener {
  /** Jupiter logger */
  private JupiterLogger log = JupiterLogger.getLogger();


  /**
   * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
   */
  public void partActivated(IWorkbenchPart part) {
  }

  /**
   * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
   */
  public void partBroughtToTop(IWorkbenchPart part) {
  }

  /**
   * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
   */
  public void partClosed(IWorkbenchPart part) {
  }

  /**
   * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
   */
  public void partDeactivated(IWorkbenchPart part) {
  }

  /**
   * Updates markers for the new opened text editor. It is necessary to update markers for the
   * text editor after an editor is opened at first time. Otherwise the marker would not show up
   * in the opened editor if an review phase were chosen.
   * 
   * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
   */
  public void partOpened(IWorkbenchPart part) {
    log.debug("part is opened.");
    if (part instanceof ITextEditor) {
      ITextEditor textEditor = (ITextEditor) part;
      log.debug("text editor is opened." + textEditor.getTitle());
      IEditorInput editorInput = textEditor.getEditorInput();
      if (editorInput instanceof IFileEditorInput) {
        IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
        ReviewMarker.updateMarkers(fileEditorInput.getFile());
      }
    }
  }
}
