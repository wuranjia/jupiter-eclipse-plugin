package edu.hawaii.ics.csdl.jupiter.event;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import edu.hawaii.ics.csdl.jupiter.file.FileResource;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;


/**
 * Provides file event manager. Clients can get the latest event associated file.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class EventFileManager {
  private JupiterLogger log = JupiterLogger.getLogger();
  private static EventFileManager theInstance = new EventFileManager();
  private String filePath = null;
  /**
   * Prohibits clients from instantiating this.
   */
  private EventFileManager() {
    this.filePath = getSelectedFile();
  }
  
  /**
   * Gets the selected file path. Returns empty string if there are not an applicable path.
   * @return The selected file path.
   */
  private String getSelectedFile() {
    String file = "";
    IFile selectedFile = FileResource.getSelectedIFile();
    if (selectedFile != null) {
      IPath selectedPath = selectedFile.getLocation();
      if (selectedPath != null) {
        file = selectedPath.toFile().toString();
      }
    }
    return file;
  }
  
  /**
   * Gets the singleton instance.
   * @return the singleton instance.
   */
  public static EventFileManager getInstance() {
    return theInstance;
  }
  
  /**
   * Sets the event associated file path.
   * @param filePath the fully qualified file path.
   */
  public void setEventFilePath(String filePath) {
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    if (filePath != null && project != null) {
      filePath = (!filePath.equals("")) ? filePath : File.separator;
      this.filePath = new File(project.getLocation().toString(), filePath).toString();
    }
    else {
      this.filePath = getSelectedFile();
    }
  }
  
  /**
   * Gets the event associated file path. Returns empty string if there is no associated file.
   * @return the fully qualified event associated file path. 
   * Returns empty string if there is no associated file.
   */
  public String getEventFilePath() {
    log.debug("Event file path: " + this.filePath);
    return this.filePath;
  }
}
