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

import org.eclipse.core.runtime.FileLocator;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.file.preference.ObjectFactory;
import edu.hawaii.ics.csdl.jupiter.file.preference.Preference;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides the function to read and write information in the config.xml file.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class PrefXmlSerializer {
  /** Jupiter logger */
  private static JupiterLogger log = JupiterLogger.getLogger();

  /** The singleton instance */
  private static PrefXmlSerializer theInstance;
  /** The preference file. */
  private static final String PREFERENCE_XML_FILE = "preference.xml";

  /**
   * Prohibits the instantiation from clients.
   */
  private PrefXmlSerializer() {
  }

  /**
   * Gets the instance of <code>PrefXmlSerializer</code>. Note that the lazy instantiation
   * is necessary to call<code>ReviewPlugin.getInstance()</code>. Otherwise the method
   * returns null.
   * 
   * @return the instance of <code>PrefXmlSerializer</code>.
   */
  public static PrefXmlSerializer getInstance() {
    if (theInstance == null) {
      theInstance = new PrefXmlSerializer();
    }
    return theInstance;
  }

  /**
   * Loads <code>Preference</code> from the PREFERENCE_XML_FILE file in the plug-in directory.
   * 
   * @return Returns the <code>Preference</code> or null if it could not be loaded from file.
   */
  public static Preference loadPreference() {
    ReviewPlugin plugin = ReviewPlugin.getInstance();
    File stateLocationXmlFile = plugin.getStateLocation().append(PREFERENCE_XML_FILE).toFile();

    URL pluginUrl = ReviewPlugin.getInstance().getInstallURL();

    try {
      if (!stateLocationXmlFile.exists()) {
        URL xmlUrl = FileLocator.toFileURL(new URL(pluginUrl, PREFERENCE_XML_FILE));
        File pluginXmlFile = new File(xmlUrl.getFile());
        copy(pluginXmlFile, stateLocationXmlFile);
      }

      JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      
      Preference preference = (Preference) unmarshaller.unmarshal(stateLocationXmlFile);
      
      return preference;
    }
    catch (JAXBException e) {
      log.error(e);
    }
    catch (IOException e) {
      log.error(e);
    }
    return null;
  }
  
  /**
   * Serializes <code>Preference</code> to the PREFERENCE_XML_FILE file.
   * 
   * @param preference The <code>Preference</code> to save to file.
   * @throws ReviewException Thrown if an error occurs while writing to file.
   */
  public static void serializePreference(Preference preference) throws ReviewException {
    ReviewPlugin plugin = ReviewPlugin.getInstance();
    File stateLocationXmlFile = plugin.getStateLocation().append(PREFERENCE_XML_FILE).toFile();
    
    try {
      FileOutputStream ouput = new FileOutputStream(stateLocationXmlFile);
      
      JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(preference, ouput);
    }
    catch (JAXBException e) {
      throw new ReviewException("JAXBException: " + e.getMessage());
    }
    catch (IOException e) {
      throw new ReviewException("IOException: " + e.getMessage());
    }
  }

  /**
   * Copy the source file to the destination file.
   * 
   * @param srouceFile the source <code>File</code>.
   * @param destinationFile the destination <code>File</code>.
   * @throws IOException if problems occur.
   */
  static void copy(File srouceFile, File destinationFile) throws IOException {
    FileChannel sourceChannel = new FileInputStream(srouceFile).getChannel();
    FileChannel destinationChannel = new FileOutputStream(destinationFile).getChannel();
    destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
    sourceChannel.close();
    destinationChannel.close();
  }
}
