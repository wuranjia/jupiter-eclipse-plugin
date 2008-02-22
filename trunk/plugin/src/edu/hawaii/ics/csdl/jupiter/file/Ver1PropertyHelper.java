package edu.hawaii.ics.csdl.jupiter.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
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
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewerId;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 * Provides an utility for property config XML.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class Ver1PropertyHelper {

  /**
   * Prohibits instantiation.
   * 
   */
  private Ver1PropertyHelper() {
  }

  /**
   * Creates the new property config document in the <code>IProject</code>.
   * 
   * @param project the project
   * @return the new <code>Document</code> instance.
   * @throws ReviewException if an error occurs during the new document creation.
   */
  public static Document newDocument(IProject project) throws ReviewException {
    try {
      SAXBuilder builder = new SAXBuilder();
      IFile jupiterConfigIFile = project.getFile(".jupiter");
      File jupiterConfigFile = jupiterConfigIFile.getLocation().toFile();
      Document document = null;
      if (jupiterConfigIFile.getLocation().toFile().exists()) {
        document = builder.build(jupiterConfigFile);
      }
      else {
        document = builder.build(copyDefaultConfigFileTo(jupiterConfigFile));
        jupiterConfigIFile.refreshLocal(IResource.DEPTH_ONE, null);
      }
      return document;
    }
    catch (CoreException e) {
      throw new ReviewException("CoreException: " + e.getMessage());
    }
    catch (JDOMException e) {
      throw new ReviewException("JDOMException: " + e.getMessage());
    }
    catch (IOException e) {
      throw new ReviewException("IOException: " + e.getMessage());
    }
  }

  /**
   * Serializes <code>Document</code> to the jupiter config file.
   * 
   * @param document the document.
   * @param project the project.
   * @throws ReviewException if an error occurs during serialization.
   */
  public static void serializeDocument(Document document, IProject project)
      throws ReviewException {
    try {
      IFile outputPropertyIFile = project.getFile(".jupiter");
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
    if (!outputPropertyFile.exists()) {
      outputPropertyFile.createNewFile();
    }
    URL pluginUrl = ReviewPlugin.getInstance().getInstallURL();
    URL xmlUrl = FileLocator.toFileURL(new URL(pluginUrl, "default.jupiter"));
    File sourceXmlFile = new File(xmlUrl.getFile());
    // copy XML file in the plug-in directory to the state location.
    Ver1PropertyHelper.copy(sourceXmlFile, outputPropertyFile);
    return outputPropertyFile;
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

  /** Jupiter logger */
  private static JupiterLogger log = JupiterLogger.getLogger();

  // private static final String ELEMENT_JUPITER_CONFIG = "JupiterConfig";
  private static final String ELEMENT_REVIEW_ID = "ReviewId";
  private static final String ELEMENT_PREFERENCE = "Preference";
  private static final String ELEMENT_REVIEWER = "Reviewer";
  private static final String ELEMENT_CATEGORY = "Category";
  private static final String ELEMENT_ITEM = "Item";
  private static final String ATTRIBUTE_NAME = "name";
  private static final String ATTRIBUTE_DESCRIPTION = "description";
  private static final String ATTRIBUTE_AUTHOR = "author";
  private static final String ATTRIBUTE_ID = "id";
  private static final String ATTRIBUTE_ENABLE = "enable";
  private static final String ATTRIBUTE_DATE_FORMAT = "dateFormat";
  private static final String ATTRIBUTE_CREATION_DATE = "creationDate";
  private static final String ATTRIBUTE_VALUE_REVIEWER = "Reviewer";
  private static final String ATTRIBUTE_VALUE_STORAGE = "Storage";
  private static final String ATTRIBUTE_VALUE_CATEGORY_ITEM = "CategoryItem";
  // private static final String ATTRIBUTE_VALUE_TYPE = "Type";
  // private static final String ATTRIBUTE_VALUE_SEVERITY = "Severity";
  // private static final String ATTRIBUTE_VALUE_RESOLUTION = "Resolution";
  // private static final String ATTRIBUTE_VALUE_STATUS = "Status";
  // private static final String ATTRIBUTE_VALUE_INTERVAL = "Interval";
  /** the attribute value for default. */
  public static final String ATTRIBUTE_VALUE_DEFAULT = "Default";
  private static final String ATTRIBUTE_DIRECTORY = "directory";

  // private File xmlFile;

  /**
   * Checks if the element contains the review id. Returns the contained <code>Element</code>
   * if the review id exists. Returns <code>null</code> if not.
   * 
   * @param jupiterConfigElement the jupiter config <code>Element</code> instance.
   * @param reviewId the <code>reviewId</code> instance.
   * @return the contained <code>Element</code> if the review id exists. Returns
   *         <code>null</code> if not.
   */
  private static Element getReviewId(Element jupiterConfigElement, ReviewId reviewId) {
    List reviewIdElementList = jupiterConfigElement.getChildren(ELEMENT_REVIEW_ID);
    for (Iterator i = reviewIdElementList.iterator(); i.hasNext();) {
      Element reviewIdElement = (Element) i.next();
      if (reviewIdElement.getAttributeValue(ATTRIBUTE_NAME).equals(reviewId.getReviewId())) {
        return reviewIdElement;
      }
    }
    return null;
  }

  /**
   * Reads the <code>IProject</code> instance to create the list of <code>ReviewId</code>
   * instances and returns it.
   * 
   * @param project the <code>IProject</code> instance.
   * @param isDefaultLoaded sets <code>true</code> if the default review id is loaded too.
   * @return the created list of the <code>ReviewId</code> instances.
   * @throws ReviewException if an error occurs during process.
   */
  public static List<ReviewId> loadReviewIds(IProject project, boolean isDefaultLoaded)
      throws ReviewException {
    List<ReviewId> reviewIdList = new ArrayList<ReviewId>();
    if (project != null) {
      Document document = Ver1PropertyHelper.newDocument(project);
      Element jupiterConfigElement = document.getRootElement();
      List reviewIdElementList = jupiterConfigElement.getChildren(ELEMENT_REVIEW_ID);
      for (Iterator i = reviewIdElementList.iterator(); i.hasNext();) {
        Element reviewIdElement = (Element) i.next();
        if (reviewIdElement != null) {
          String reviewIdName = reviewIdElement.getAttributeValue(ATTRIBUTE_NAME);
          String description = reviewIdElement.getAttributeValue(ATTRIBUTE_DESCRIPTION);
          String author = reviewIdElement.getAttributeValue(ATTRIBUTE_AUTHOR);
          author = (author != null) ? author : "";
          // Supports 1.4.303 version. should be removed in the future.
          description = (description != null) ? description : "";
          if (isDefaultLoaded || !reviewIdName.equals(ATTRIBUTE_VALUE_DEFAULT)) {
            String dateFormat = reviewIdElement.getAttributeValue(ATTRIBUTE_DATE_FORMAT);
            String creationDate = reviewIdElement.getAttributeValue(ATTRIBUTE_CREATION_DATE);
            Date date = createDate(creationDate, dateFormat);
            Element storagePreferenceElement = getPreferenceElement(ATTRIBUTE_VALUE_STORAGE,
                reviewIdElement);
            String directory = createDirectory(document, storagePreferenceElement);
            Element reviewerPreferenceElement = getPreferenceElement(ATTRIBUTE_VALUE_REVIEWER,
                reviewIdElement);
            Map<String, ReviewerId> reviewers = createReviewers(document, reviewerPreferenceElement);
            Element categoryItemPrefElement = getPreferenceElement(
                ATTRIBUTE_VALUE_CATEGORY_ITEM, reviewIdElement);
            Map<String, List<String>> categoryMap = createCategoryMap(categoryItemPrefElement);
            reviewIdList.add(new ReviewId(reviewIdName, description, author, directory,
                reviewers, categoryMap, date));
          }
        }
      }
    }
    return reviewIdList;
  }

  /**
   * Gets the preference element with the attribute name. Returns null if the element is not
   * found.
   * 
   * @param attributeValueName the attribute name of the preference element.
   * @param reviewIdElement the review id element.
   * @return the preference <code>Element</code> instance.
   */
  private static Element getPreferenceElement(String attributeValueName,
      Element reviewIdElement) {
    List preferenceList = reviewIdElement.getChildren(ELEMENT_PREFERENCE);
    Element preferenceElement = null;
    for (Iterator i = preferenceList.iterator(); i.hasNext();) {
      Element tempElement = (Element) i.next();
      String name = tempElement.getAttributeValue(ATTRIBUTE_NAME);
      if (name.equals(attributeValueName)) {
        preferenceElement = tempElement;
        break;
      }
    }
    return preferenceElement;
  }

  /**
   * Creates the <code>Map</code> of the reviewer names. Returns empty map if
   * <code>Element</code> is null.
   * 
   * @param document the <code>Document</code> instance.
   * @param reviewerPreferenceElement the reviewer preference element.
   * @return the <code>Map</code> of the reviewer names.
   */
  private static Map<String, ReviewerId> createReviewers(Document document,
      Element reviewerPreferenceElement) {
    Map<String, ReviewerId> reviewers = new TreeMap<String, ReviewerId>();
    if (reviewerPreferenceElement != null) {
      List reviewerElementList = reviewerPreferenceElement.getChildren(ELEMENT_REVIEWER);
      if (reviewerElementList.size() > 0) {
        for (Iterator i = reviewerElementList.iterator(); i.hasNext();) {
          Element reviewerElement = (Element) i.next();
          String reviewerId = reviewerElement.getAttributeValue(ATTRIBUTE_ID);
          String reviewerName = reviewerElement.getAttributeValue(ATTRIBUTE_NAME);
          String enable = reviewerElement.getAttributeValue(ATTRIBUTE_ENABLE);
          boolean isEnabled = new Boolean(enable).booleanValue();
          reviewers.put(reviewerId, new ReviewerId(reviewerId, reviewerName));
        }
      }
      else {
        reviewers.putAll(getDefaultReviewers(reviewerPreferenceElement.getDocument()));
      }
    }
    else {
      // log.debug("reviewerPreferenceElement is null: there is no reviewer preference
      // element."
      // + "read default reviewer values...");
      reviewers.putAll(getDefaultReviewers(document));
    }
    return reviewers;
  }

  /**
   * Creates directory.
   * 
   * @param document the <code>Document</code>.
   * @param storagePreferenceElement the storage preference <code>Element</code>.
   * @return the directory.
   */
  private static String createDirectory(Document document, Element storagePreferenceElement) {
    String directory = "";
    if (storagePreferenceElement != null) {
      directory = storagePreferenceElement.getAttributeValue(ATTRIBUTE_DIRECTORY);
    }
    else {
      directory = getDefaultDirectory(document);
    }
    return directory;
  }

  /**
   * Gets default reviewer map from the document.
   * 
   * @param document the <code>Document</code> instance.
   * @return the map of the <code>String</code> reviewer IDs.
   */
  private static Map<String, ReviewerId> getDefaultReviewers(Document document) {
    Map<String, ReviewerId> defaultReviewers = new TreeMap<String, ReviewerId>();
    Element jupiterConfigElement = document.getRootElement();
    List reviewIdElementList = jupiterConfigElement.getChildren(ELEMENT_REVIEW_ID);
    Element defaultPrefElement = getDefaultPrefElement(ATTRIBUTE_VALUE_REVIEWER, document);
    if (defaultPrefElement != null) {
      List reviewerElementList = defaultPrefElement.getChildren(ELEMENT_REVIEWER);
      for (Iterator j = reviewerElementList.iterator(); j.hasNext();) {
        Element reviewerElement = (Element) j.next();
        String reviewerId = reviewerElement.getAttributeValue(ATTRIBUTE_ID);
        String reviewerName = reviewerElement.getAttributeValue(ATTRIBUTE_NAME);
        String enable = reviewerElement.getAttributeValue(ATTRIBUTE_ENABLE);
        boolean isEnabled = new Boolean(enable).booleanValue();
        ReviewerId reviewer = new ReviewerId(reviewerId, reviewerName);
        defaultReviewers.put(reviewerId, reviewer);
      }
    }
    return defaultReviewers;
  }

  /**
   * Gets default directory. Returns empty string if the default review id element does not
   * exist or the value of the name attribute of the preference element in the default review
   * id does not exist.
   * 
   * @param document the document
   * @return the default directory.
   */
  private static String getDefaultDirectory(Document document) {
    String directory = "";
    Element defaultPrefElement = getDefaultPrefElement(ATTRIBUTE_VALUE_STORAGE, document);
    if (defaultPrefElement != null) {
      defaultPrefElement.getAttributeValue(ATTRIBUTE_DIRECTORY);
    }
    return directory;
  }

  /**
   * Gets default preference element whose name attribute is <code>attributeValueName</code>.
   * 
   * @param attributeValueName the value of the name attribute of the preference element in the
   *          default review id.
   * @param document the document.
   * @return the default preference element whose name attribute is
   *         <code>attributeValueName</code>. Returns <code>null</code> if the default
   *         review id element does not exist or the value of the name attribute of the
   *         preference element in the default review id does not exist.
   */
  private static Element getDefaultPrefElement(String attributeValueName, Document document) {
    Element jupiterConfigElement = document.getRootElement();
    List reviewIdElementList = jupiterConfigElement.getChildren(ELEMENT_REVIEW_ID);
    for (Iterator i = reviewIdElementList.iterator(); i.hasNext();) {
      Element reviewIdElement = (Element) i.next();
      if (reviewIdElement.getAttributeValue(ATTRIBUTE_NAME).equals(ATTRIBUTE_VALUE_DEFAULT)) {
        Element defaultPrefElement = getPreferenceElement(attributeValueName, reviewIdElement);
        return defaultPrefElement;
      }
    }
    return null;
  }

  /**
   * Creates the <code>Map</code> with the category <code>String</code> name and
   * <code>List</code> of items. Returns empty list if <code>Element</code> is null.
   * 
   * @param categoryItemPreferenceElement the category item preference element.
   * @return the <code>Map</code> with the category <code>String</code> name and
   *         <code>List</code> of items.
   */
  private static Map<String, List<String>> createCategoryMap(Element categoryItemPreferenceElement) {
    Map<String, List<String>> categoryMap = new HashMap<String, List<String>>();
    if (categoryItemPreferenceElement != null) {
      List categoryElementList = categoryItemPreferenceElement.getChildren(ELEMENT_CATEGORY);
      for (Iterator i = categoryElementList.iterator(); i.hasNext();) {
        Element categoryElement = (Element) i.next();
        String categoryName = categoryElement.getAttributeValue(ATTRIBUTE_NAME);
        List itemElementList = categoryElement.getChildren(ELEMENT_ITEM);
        List<String> itemList = new ArrayList<String>();
        for (Iterator j = itemElementList.iterator(); j.hasNext();) {
          Element itemElement = (Element) j.next();
          String itemName = itemElement.getAttributeValue(ATTRIBUTE_NAME);
          itemList.add(itemName);
        }
        categoryMap.put(categoryName, itemList);
      }
    }
    return categoryMap;
  }

  /**
   * Creates the <code>Date</code> instance associated with the <code>dateString</code>.
   * Note the this returns current time <code>Date</code> instance if <code>dateString</code>
   * could not be parsed with <code>dateFormat</code>.
   * 
   * @param dateString the date string to be parsed.
   * @param dateFormat the date format to let parser know the date string to be parsed.
   * 
   * @return the <code>Date</code> instance associated with the <code>dateString</code>.
   */
  private static Date createDate(String dateString, String dateFormat) {
    if (dateString == null || dateFormat == null) {
      return new Date();
    }
    try {
      return new SimpleDateFormat(dateFormat).parse(dateString);
    }
    catch (ParseException e) {
      log.warning(e.getMessage());
      return new Date();
    }
  }

  /**
   * Creates document.
   * 
   * @param element the element as a root to add document.
   * @return the created document.
   */
  private static Document createDocument(Element element) {
    Document rootDocument = new Document();
    rootDocument.setRootElement(element);
    return rootDocument;
  }

  /**
   * Creates the reviewer preference element
   * 
   * @param reviewerList the <code>List</code> instance of the reviewer names.
   * @return the <code>Element</code> of the review preference.
   */
  private static Element createReviewerPreferenceElement(List<String> reviewerList) {
    Element reviewerPreferenceElement = new Element(ELEMENT_PREFERENCE);
    reviewerPreferenceElement.setAttribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE_REVIEWER);
    for (Iterator<String> i = reviewerList.iterator(); i.hasNext();) {
      String reviewer = i.next();
      Element reviewerElement = new Element(ELEMENT_REVIEWER);
      reviewerElement.setAttribute(ATTRIBUTE_NAME, reviewer);
      reviewerPreferenceElement.addContent(reviewerElement);
    }
    return reviewerPreferenceElement;
  }

  /**
   * Creates root review id element.
   * 
   * @param reviewId the <code>ReviewId</code> instance.
   * @return the <code>Element</code> of the review id.
   */
  private static Element createReviewIdElement(ReviewId reviewId) {
    Element reviewIdElement = new Element(ELEMENT_REVIEW_ID);
    reviewIdElement.setAttribute(ATTRIBUTE_NAME, reviewId.getReviewId());
    reviewIdElement.setAttribute(ATTRIBUTE_DESCRIPTION, reviewId.getDescription());
    reviewIdElement.setAttribute(ATTRIBUTE_AUTHOR, reviewId.getAuthor());
    String formatString = "yyyy-MM-dd :: HH:mm:ss z";
    reviewIdElement.setAttribute(ATTRIBUTE_DATE_FORMAT, formatString);
    SimpleDateFormat format = new SimpleDateFormat(formatString);
    reviewIdElement.setAttribute(ATTRIBUTE_CREATION_DATE, format.format(reviewId.getDate()));
    return reviewIdElement;
  }

  /**
   * Gets the list of the <code>ReviewId</code> instances.
   * 
   * @param projectName the project name.
   * @param isDefaultLoaded sets <code>true</code> if the default review id is loaded too.
   * @return the list of the <code>ReviewId</code> instances.
   */
  public static List<ReviewId> getReviewIdList(String projectName, boolean isDefaultLoaded) {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IProject selectedProject = root.getProject(projectName);
    List<ReviewId> reviewIdList = new ArrayList<ReviewId>();
    if (selectedProject != null && projectName != null) {
      try {
        reviewIdList = Ver1PropertyHelper.loadReviewIds(selectedProject, isDefaultLoaded);
        Collections.sort(reviewIdList, new Comparator<ReviewId>() {
          public int compare(ReviewId reviewId1, ReviewId reviewId2) {
            return reviewId2.getDate().compareTo(reviewId1.getDate());
          }
        });
      }
      catch (Exception e) {
        e.printStackTrace();
        log.error(e);
      }
    }
    return reviewIdList;
  }
}
