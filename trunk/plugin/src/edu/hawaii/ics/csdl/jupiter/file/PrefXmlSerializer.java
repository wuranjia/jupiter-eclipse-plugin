package edu.hawaii.ics.csdl.jupiter.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.channels.FileChannel;

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
   * Gets the instance of <code>PrefXmlSerializer</code>. Note that the lazy instantiation is
   * necessary to call<code>ReviewPlugin.getInstance()</code>. Otherwise the method returns
   * null.
   * @return the instance of <code>PrefXmlSerializer</code>.
   */
  public static PrefXmlSerializer getInstance() {
    if (theInstance == null) {
      theInstance = new PrefXmlSerializer();
    }
    return theInstance;
  }
  
  /**
   * Loads root preference element from the PREFERENCE_XML_FILE file in the plug-in directory.
   * @return the <code>Element</code> of the default Review.
   */
  public static Element loadPreferenceElement() {
    ReviewPlugin plugin = ReviewPlugin.getInstance();
    File stateLocationXmlFile = plugin.getStateLocation().append(PREFERENCE_XML_FILE).toFile();
    
    URL pluginUrl = ReviewPlugin.getInstance().getInstallURL();
    try {
      // The preference file will not exist when deploying Jupiter inside Eclipse. 
      // For debugging purpose, we supply a default preference.xml file located in lib directory.
      if (!stateLocationXmlFile.exists()) {
	     String location = plugin.getBundle().getLocation();
	     String pluginPath = location.substring(location.indexOf("@") + 1);
	     File defaultPreferenceFile = new File(pluginPath + File.separator + "lib" + 
	    		 File.separator +PREFERENCE_XML_FILE);
	     if (defaultPreferenceFile.exists()) {
	    	copy(defaultPreferenceFile, stateLocationXmlFile);
	     }
	  }

      if (!stateLocationXmlFile.exists()) {
         URL xmlUrl = FileLocator.toFileURL(new URL(pluginUrl, PREFERENCE_XML_FILE));
         File pluginXmlFile = new File(xmlUrl.getFile());
    	 copy(pluginXmlFile, stateLocationXmlFile);
      }
      
      SAXBuilder builder = new SAXBuilder();
      Document document = builder.build(stateLocationXmlFile);
      Element preferenceElement = document.getRootElement();
      return preferenceElement;
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
   * Serializes <code>Document</code> to the PREFERENCE_XML_FILE file.
   * @param document the document.
   * @throws ReviewException if an error occurs during serialization.
   */
  public static void serializeDocument(Document document) throws ReviewException {
    try {
      ReviewPlugin plugin = ReviewPlugin.getInstance();
      File stateLocationXmlFile = plugin.getStateLocation().append(PREFERENCE_XML_FILE).toFile();
      FileOutputStream fileOutputStream = new FileOutputStream(stateLocationXmlFile);
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
   * Copy the source file to the destination file.
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
