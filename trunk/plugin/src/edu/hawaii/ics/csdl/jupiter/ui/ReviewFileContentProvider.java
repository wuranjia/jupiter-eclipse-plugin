package edu.hawaii.ics.csdl.jupiter.ui;

import java.io.File;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The content provider used to show <code>File</code>s in a tree.
 * 
 * @author jsakuda
 * 
 */
public class ReviewFileContentProvider implements ITreeContentProvider {

  /** {@inheritDoc} */
  @Override
  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof File) {
      File file = (File) parentElement;
      return file.listFiles();
    }
    
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public Object getParent(Object element) {
    if (element instanceof File) {
      File file = (File) element;
      return file.getParentFile();
    }
    
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasChildren(Object element) {
    if (element instanceof File) {
      File file = (File) element;
      return file.isDirectory() && file.list().length > 0;
    }

    return false;
  }

  /** {@inheritDoc} */
  @Override
  public Object[] getElements(Object inputElement) {
    if (inputElement instanceof File) {
      return getChildren(inputElement);
    }

    return null;
  }

  /** {@inheritDoc} */
  @Override
  public void dispose() {
    // do nothing
  }

  /** {@inheritDoc} */
  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    // do nothing
  }

}
