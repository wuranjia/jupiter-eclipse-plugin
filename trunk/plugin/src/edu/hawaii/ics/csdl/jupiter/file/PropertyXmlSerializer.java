package edu.hawaii.ics.csdl.jupiter.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.property.ObjectFactory;
import edu.hawaii.ics.csdl.jupiter.file.property.Property;
import edu.hawaii.ics.csdl.jupiter.file.property.Review;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides an utility for property config XML.
 * 
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
   * Creates the new <code>Property</code> config instance in the <code>IProject</code>.
   * 
   * @param project the project
   * @return the new <code>Property</code> instance.
   * @throws ReviewException if an error occurs during the new document creation.
   */
  public static Property newProperty(IProject project) throws ReviewException {
    IFile jupiterConfigIFile = project.getFile(PROPERTY_XML_FILE);
    File jupiterConfigFile = jupiterConfigIFile.getLocation().toFile();
    Property property = null;

    try {
      JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      if (jupiterConfigFile.exists()) {
        property = (Property) unmarshaller.unmarshal(jupiterConfigFile);
      }
      else {
        if (FileResource.getActiveProject().getName().equals(project.getName())) {
          File configFile = copyDefaultConfigFileTo(jupiterConfigFile);
          property = (Property) unmarshaller.unmarshal(configFile);
          jupiterConfigIFile.refreshLocal(IResource.DEPTH_ONE, null);
        }
      }
    }
    catch (CoreException e) {
      throw new ReviewException("CoreException: " + e.getMessage(), e);
    }
    catch (JAXBException e) {
      throw new ReviewException("JAXBException: " + e.getMessage(), e);
    }
    catch (IOException e) {
      e.printStackTrace();
      throw new ReviewException("IOException: " + e.getMessage(), e);
    }

    return property;
  }

  /**
   * Serializes a <code>Property</code> to the jupiter config file using JAXB.
   * 
   * @param property The properties to save.
   * @param project The project that the property is for.
   * @throws ReviewException Thrown if there is an error during serialization.
   */
  public static void serializeProperty(Property property, IProject project)
      throws ReviewException {
    IFile outputPropertyIFile = project.getFile(PROPERTY_XML_FILE);
    File outputPropertyFile = outputPropertyIFile.getLocation().toFile();

    try {
      FileOutputStream ouput = new FileOutputStream(outputPropertyFile);

      JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(property, ouput);
    }
    catch (JAXBException e) {
      throw new ReviewException("JAXBException: " + e.getMessage());
    }
    catch (IOException e) {
      throw new ReviewException("IOException: " + e.getMessage());
    }
  }

  /**
   * Copies default config file in the <code>Project</code>. Leave the current config file
   * in the project if the file already exists.
   * 
   * @param outputPropertyFile the output property file.
   * @return the config file <code>File</code> instance.
   * @throws IOException if problems occur.
   * @throws CoreException if problems occur.
   */
  private static File copyDefaultConfigFileTo(File outputPropertyFile) throws IOException,
      CoreException {
    // System.out.println("about to copy a file to " + outputPropertyFile);
    if (!outputPropertyFile.exists()) {
      outputPropertyFile.createNewFile();
    }

    URL pluginUrl = ReviewPlugin.getInstance().getInstallURL();
    // System.out.println(pluginUrl.getFile());
    URL xmlUrl = FileLocator.toFileURL(new URL(pluginUrl, DEFAULT_PROPERTY_XML_FILE));
    // System.out.println("From : " + xmlUrl);

    File sourceXmlFile = new File(xmlUrl.getFile());
    // copy XML file in the plug-in directory to the state location.
    // System.out.println("From : " + sourceXmlFile);
    PrefXmlSerializer.copy(sourceXmlFile, outputPropertyFile);
    return outputPropertyFile;
  }

  /**
   * Loads the default review from property.xml. 
   * 
   * @return Returns the <code>Review</code> object or null.
   */
  public static Review cloneDefaultReview() {
    URL pluginUrl = ReviewPlugin.getInstance().getInstallURL();
    try {
      URL xmlUrl = FileLocator.toFileURL(new URL(pluginUrl, DEFAULT_PROPERTY_XML_FILE));
      JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      Property property = (Property) unmarshaller.unmarshal(xmlUrl);
      // there should only be the default review in the list
      return property.getReview().get(0);
    }
    catch (IOException e) {
      log.error(e);
    }
    catch (JAXBException e) {
      log.error(e);
    }
    return null;
  }

  /**
   * Copy the source file to the destination file.
   * 
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
