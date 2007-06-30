package edu.hawaii.ics.csdl.jupiter.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides an utility for property config XML.
 * @author Takuya Yamashita
 * @version $Id$
 */
public class PropertyXmlSerializer {
  /** Jupiter logger */
  private static JupiterLogger log = JupiterLogger.getLogger();

  private static final String DEFAULT_PROPERTY_XML_FILE = "property.xml";
  /** The property XML file name. */
  public static final String PROPERTY_XML_FILE = ".jupiter";
  /**
   * Prohibits instantiation.
   */
  private PropertyXmlSerializer() {
  }
  
  /**
   * Creates the new property config document in the <code>IProject</code>.
   * @param project the project
   * @return the new <code>Document</code> instance.
   * @throws ReviewException if an error occurs during the new document creation.
   */
  public static Document newDocument(IProject project) throws ReviewException {
    try {
      SAXBuilder builder = new SAXBuilder();
      IFile jupiterConfigIFile = project.getFile(PROPERTY_XML_FILE);
      File jupiterConfigFile = jupiterConfigIFile.getLocation().toFile();
      Document document = null;
      
      if (jupiterConfigIFile.getLocation().toFile().exists()) {
    	  document = builder.build(jupiterConfigFile);
      }
      else {
        if (FileResource.getActiveProject().getName().equals(project.getName())) {
        	document = builder.build(copyDefaultConfigFileTo(jupiterConfigFile));
          jupiterConfigIFile.refreshLocal(IResource.DEPTH_ONE, null);
        }
      }
      return document;
    }
    catch (CoreException e) {
      throw new ReviewException("CoreException: " + e.getMessage(), e);
    }
    catch (JDOMException e) {
      throw new ReviewException("JDOMException: " + e.getMessage(), e);
    }
    catch (IOException e) {
      e.printStackTrace();
    	throw new ReviewException("IOException: " + e.getMessage(), e);      
    }
  }
  
  /**
   * Serializes <code>Document</code> to the jupiter config file.
   * @param document the document.
   * @param project the project.
   * @throws ReviewException if an error occurs during serialization.
   */
  public static void serializeDocument(Document document, IProject project)
                                       throws ReviewException {
    try {
      IFile outputPropertyIFile = project.getFile(PROPERTY_XML_FILE);
      File outputPropertyFile = outputPropertyIFile.getLocation().toFile();
      FileOutputStream fileOutputStream = new FileOutputStream(outputPropertyFile);
      OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
      XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
      outputter.output(document, writer);
      writer.close();
    }
    catch (IOException e) {
      throw new ReviewException("IOException: " + e.getMessage());
    }
  }
  
  /**
   * Copies default config file in the <code>Project</code>. Leave the current config file in the
   * project if the file already exists.
   * @param outputPropertyFile the output property file.
   * @return the config file <code>File</code> instance.
   * @throws IOException if problems occur.
   * @throws CoreException if problems occur.
   */
  private static File copyDefaultConfigFileTo(File outputPropertyFile) 
                      throws IOException, CoreException {
    System.out.println("about to copy a file to " + outputPropertyFile);
	if (!outputPropertyFile.exists()) {
      outputPropertyFile.createNewFile();
    }
      
    URL pluginUrl = ReviewPlugin.getInstance().getInstallURL();
    System.out.println(pluginUrl.getFile());
    URL xmlUrl = FileLocator.toFileURL(new URL(pluginUrl, DEFAULT_PROPERTY_XML_FILE));
    System.out.println("From : " + xmlUrl);

    File sourceXmlFile = new File(xmlUrl.getFile());
    // copy XML file in the plug-in directory to the state location.
    System.out.println("From : " + sourceXmlFile);
    PrefXmlSerializer.copy(sourceXmlFile, outputPropertyFile);
    return outputPropertyFile;
  }
  
  /**
   * Loads default Review element from the default property.xml file in the plug-in directory.
   * @return the <code>Element</code> of the default Review.
   */
  public static Element cloneDefaultReviewElement() {
    URL pluginUrl = ReviewPlugin.getInstance().getInstallURL();
    try {
      URL xmlUrl = FileLocator.toFileURL(new URL(pluginUrl, DEFAULT_PROPERTY_XML_FILE));
      SAXBuilder builder = new SAXBuilder();
      Document document = builder.build(new File(xmlUrl.getFile()));
      Element propertyElement = document.getRootElement();
      return (Element) propertyElement.getChild(PropertyConstraints.ELEMENT_REVIEW).clone();
    }
    catch (IOException e) {
      log.error(e);
    }
    catch (JDOMException e) {
      log.error(e);
    }
    return null;
  }
    
  /**
  * Copy the source file to the destination file.
  * @param srouceFile the source <code>File</code>.
  * @param destinationFile the destination <code>File</code>.
  * @throws IOException if problems occur.
  */
 private static void copy(File srouceFile, File destinationFile) throws IOException {
   FileChannel sourceChannel = new FileInputStream(srouceFile).getChannel();
   FileChannel destinationChannel = new FileOutputStream(destinationFile).getChannel();
   destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
   sourceChannel.close();
   destinationChannel.close();
 }
}
