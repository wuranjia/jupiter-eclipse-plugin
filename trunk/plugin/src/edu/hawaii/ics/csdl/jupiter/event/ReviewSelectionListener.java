package edu.hawaii.ics.csdl.jupiter.event;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import edu.hawaii.ics.csdl.jupiter.file.FileResource;

/**
 * Provides the listener to listen to the selection change. 
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewSelectionListener implements ISelectionListener {
  /** The selection. */
  private static IStructuredSelection structuredSelection;
  
  /**
   * Gets structured selection.
   * @return the structured selection.
   */
  public static IStructuredSelection getStructuredSelection() {
    return structuredSelection;
  }  
  
  /**
   * Notified when selection is changed. Brings the opened code review editor to top if a code
   * review file is selected and the editor associated with the file is opened.
   *
   * @param workbenchPart the workbench part containing the selection
   * @param selection the current selection
   */
  public void selectionChanged(IWorkbenchPart workbenchPart, ISelection selection) {
    IEditorPart editorPart = workbenchPart.getSite().getPage().getActiveEditor();
    if (editorPart != null && editorPart.getEditorInput() instanceof IFileEditorInput) {
      IFile activeFile = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
      FileResource.setSelectedResource(activeFile);
    }
    if (selection instanceof IStructuredSelection) {
      structuredSelection = (IStructuredSelection) selection;
      if (structuredSelection.size() == 1) {
        Object object = structuredSelection.getFirstElement();
        if (object instanceof IAdaptable) {
          IAdaptable adaptable = (IAdaptable) object;
          IResource resource = (IResource) adaptable.getAdapter(IResource.class);
          FileResource.setSelectedResource(resource);
//          IJavaElement javaElement = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
//          if (javaElement instanceof IMember) {
//            IMember member = (IMember) javaElement;
//            IFile selectedFile = (IFile) member.getResource();
//            FileResource.setSelectedResource(selectedFile);
//          }
        }
      }
    }
  }
}
