package edu.hawaii.ics.csdl.jupiter.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javanet.staxutils.StaxUtilsXMLOutputFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.core.runtime.FileLocator;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
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
        FileUtil.copy(pluginXmlFile, stateLocationXmlFile);
      }

      XMLInputFactory xmlif = XMLInputFactory.newInstance();
      xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
      xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
      xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
      
      String absolutePath = stateLocationXmlFile.getAbsolutePath();
      XMLStreamReader reader = xmlif.createXMLStreamReader(absolutePath, new FileInputStream(
          absolutePath));

      return StaxPreferenceXmlUtil.parsePreferenceFile(reader);
    }
    catch (XMLStreamException e) {
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
    
    XMLStreamWriter writer = null;
    try {
      StaxUtilsXMLOutputFactory xmlof = new StaxUtilsXMLOutputFactory(XMLOutputFactory
          .newInstance());
      xmlof.setProperty(StaxUtilsXMLOutputFactory.INDENTING, true);

      writer = xmlof.createXMLStreamWriter(new FileOutputStream(stateLocationXmlFile), "UTF-8");
      writer.writeStartDocument("UTF-8", "1.0");
      
      StaxPreferenceXmlUtil.writePreference(writer, preference);
      
      writer.writeEndDocument();
    }
    catch (XMLStreamException e) {
      throw new ReviewException("XMLStreamException: " + e.getMessage(), e);
    }
    catch (FileNotFoundException e) {
      throw new ReviewException("FileNotFoundException: " + e.getMessage(), e);
    }
    finally {
      if (writer != null) {
        try {
          writer.close();
        }
        catch (XMLStreamException e) {
          log.error(e);
        }
      }
    }
  }
}
