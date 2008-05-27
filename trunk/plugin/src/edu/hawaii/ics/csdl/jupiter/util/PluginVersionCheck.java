package edu.hawaii.ics.csdl.jupiter.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;
import edu.hawaii.ics.csdl.jupiter.ui.preference.GeneralPreferencePage;

/**
 * Provides the version check function. Clients may call <code>processUpdateDialog()</code>
 * to check new version on the server, whose URL is specified in the preference page, and bring
 * update info dialog if the new version is available.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class PluginVersionCheck {
  /** Jupiter logger */
  private static JupiterLogger log = JupiterLogger.getLogger();

  /**
   * Processes update dialog to the user. The current version and new version appear in the
   * dialog.
   */
  public static void processUpdateDialog() {
    IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
    String updateUrl = store.getString(GeneralPreferencePage.UPDATE_URL_KEY);
    if (updateUrl != null) {
      try {
        // Gets current version info.
        Version localVerIdentifier = getLocalPluginVersionIdentifier();
        final String localVersionId = localVerIdentifier.toString();

        // Gets new version info.
        URL url = new URL(updateUrl);
        Document serverDocument = parseXml(url.openStream());
        Version serverVerIdentifier = getNewestVersionIdentifier(serverDocument);

        if (serverVerIdentifier != null) {
          final String serverVersionId = serverVerIdentifier.toString();
          // Check if current version is not new version, then show pop up update dialog.
          if ((serverVerIdentifier != null)
              && serverVerIdentifier.compareTo(localVerIdentifier) > 0) {
            IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
            if (workbench.getWorkbenchWindows().length > 0) {
              Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                  int result = ReviewDialog.processVersionCheckDialog(localVersionId,
                      serverVersionId);
                  if (result == MessageDialog.OK) {
                    ReviewDialog.proccessOpenNewUpdatesWizard();
                  }
                }
              });
            }
          }
        }
      }
      catch (NullPointerException e) {
        log.debug(e.getLocalizedMessage());
        e.printStackTrace();
        // couldn't parse XML file, or even though XML is parsed, version attribute is not found.
      }
      catch (IOException e) {
        log.debug(e.getLocalizedMessage());
        // URL connection was not established.
      }
    }
  }

  /**
   * Gets the local plug-in version identifier.
   * 
   * @return the local plug-in version identifier.
   */
  public static Version getLocalPluginVersionIdentifier() {
    return ReviewPlugin.getInstance().getVersionIdentifier();
  }

  /**
   * Gets the version information from the given Document object. The qualifier version is no
   * longer taken into account.
   * 
   * @param document The Document with the plugin version information.
   * @return Returns the newest plugin version found or null if there is no version
   *         information.
   */
  private static Version getNewestVersionIdentifier(Document document) {
    if (document != null) {
      Element root = document.getDocumentElement();
      NodeList list = root.getChildNodes();
      final String feature = "feature";
      final String version = "version";
      TreeSet<Version> allVersions = new TreeSet<Version>(new Comparator<Version>() {
        @Override
        public int compare(Version o1, Version o2) {
          return o1.compareTo(o2);
        }
      });
      for (int i = 0; i < list.getLength(); i++) {
        if ((list.item(i).getNodeType() == Node.ELEMENT_NODE)
            && list.item(i).getNodeName().equalsIgnoreCase(feature)) {
          Element element = (Element) list.item(i);
          if (element.hasAttribute(version)) {
            String versionValue = element.getAttribute(version);
            Version identifier = new Version(versionValue);
            allVersions.add(identifier);
          }
        }
      }
      // newest version should sort to the end
      return allVersions.last();
    }
    return null;
  }

  /**
   * Parses XML file to generate Document object from a given input stream.
   * 
   * @param input The given input stream from which XML file is read.
   * 
   * @return The Documentation object which contains the content parsed from the XML file.
   *         Returns null if parse error occurs.
   */
  private static Document parseXml(InputStream input) {
    try {
      // Create a builder factory
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      // Create the builder and parse the file
      Document document = factory.newDocumentBuilder().parse(input);
      input.close();
      return document;
    }
    catch (SAXException e) {
      // A parsing error occurred; the XML input is not valid
    }
    catch (ParserConfigurationException e) {
      // ignored.
    }
    catch (IOException e) {
      // ignored.
    }
    return null;
  }
}
