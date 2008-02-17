package edu.hawaii.ics.csdl.jupiter.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Resolution;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ResolutionKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.ReviewIssueModel;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Severity;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.SeverityKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Status;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.StatusKeyManager;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.Type;
import edu.hawaii.ics.csdl.jupiter.model.reviewissue.TypeKeyManager;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;
import edu.hawaii.ics.csdl.jupiter.util.ResourceBundleKey;

/**
 * Provides an utility methods to read and write XML file to/from CodeReviewContentProvider model
 * instance.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class Ver1ReviewHelper {
  /** Jupiter logger */
  private static JupiterLogger log = JupiterLogger.getLogger();
  /** The CODE_REVIEWS element name. */
  private static final String CODE_REVIEWS = "CodeReviews";
  /** The CODE_REVIEW element name. */
  private static final String CODE_REVIEW = "CodeReview";
  /** The CODE_REVIEW_ATTRIBUTE_ID attribute name. */
  private static final String CODE_REVIEW_ATTRIBUTE_ID = "id";
  /** The CODE_REIVEW_META element name. */
  private static final String CODE_REIVEW_META = "CodeReviewMeta";
  /** The CREATION_DATE element name. */
  private static final String CREATION_DATE = "CreationDate";
  /** The LAST_MODIFICATION_DATE element name. */
  private static final String LAST_MODIFICATION_DATE = "LastModificationDate";
  /** The FORMAT_ATTRIBUTE attribute name. */
  private static final String FORMAT_ATTRIBUTE = "format";
  /** The REVIEWER element name. */
  private static final String REVIEWER = "Reviewer";
  /** The ASSIGNED_TO element name. */
  private static final String ASSIGNED_TO = "AssignedTo";
  /** The FILE element name. */
  private static final String FILE = "File";
  /** The type attribute of the FILE. */
  private static final String FILE_ATTRIBUTE_TYPE = "type";
  /** The class attribute of the FILE. */
  private static final String FILE_ATTRIBUTE_CLASS = "class";
  /** The method attribute of the FILE. */
  private static final String FILE_ATTRIBUTE_METHOD = "method";
  /** The line attribute of the FILE. */
  private static final String FILE_ATTRIBUTE_LINE = "line";
  /** The line attribute of the EDITOR_ID. */
  private static final String FILE_ATTRIBUTE_EDITOR_ID = "editorId";
  /** The author attribute of the FILE. */
  private static final String FILE_ATTRIBUTE_AUTHOR = "author";
  /** The TYPE element name. */
  private static final String TYPE = "Type";
  /** The SEVERITY element name. */
  private static final String SEVERITY = "Severity";
  /** The SUMMARY element name. */
  private static final String SUMMARY = "Summary";
  /** The DESCRIPTION element name. */
  private static final String DESCRIPTION = "Description";
  /** The ANNOTATION element name. */
  private static final String ANNOTATION = "Annotation";
  /** The REVISION element name. */
  private static final String REVISION = "Revision";
  /** The RESOLUTION element name. */
  private static final String RESOLUTION = "Resolution";
  /** The STATUS element name. */
  private static final String STATUS = "Status";
  /** The attribute id. */
  private static final String ATTRIBUTE_ID = "id";
  /** The date format pattern string. */
  private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd :: HH:mm:ss:SSS z";

  /**
   * Gets the root element of the tree element from the ReviewIssue instance.
   *
   * @param reviewIssue The ReviewIssue instance.
   *
   * @return The root element of the tree element.
   */
  private static Element createCodeReviewElement(ReviewIssue reviewIssue) {
    if (reviewIssue == null) {
      throw new NullPointerException("ReviewIssue instance is null.");
    }
    Element codeReviewRoot = new Element(CODE_REVIEW);
    codeReviewRoot.setAttribute(CODE_REVIEW_ATTRIBUTE_ID, reviewIssue.getIssueId());
    Element codeReviewMetaElement = new Element(CODE_REIVEW_META);
    codeReviewRoot.addContent(codeReviewMetaElement);
    Element creationDateElement = new Element(CREATION_DATE);
    creationDateElement.setAttribute(FORMAT_ATTRIBUTE, DATE_FORMAT_PATTERN);
    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    creationDateElement.setText(format.format(reviewIssue.getCreationDate()));
    codeReviewMetaElement.addContent(creationDateElement);
    Element lastModificationDateElement = new Element(LAST_MODIFICATION_DATE);
    lastModificationDateElement.setAttribute(FORMAT_ATTRIBUTE, DATE_FORMAT_PATTERN);
    lastModificationDateElement.setText(format.format(reviewIssue.getModificationDate()));
    codeReviewMetaElement.addContent(lastModificationDateElement);
    Element reviewerElement = new Element(REVIEWER);
    reviewerElement.setText(reviewIssue.getReviewer());
    codeReviewRoot.addContent(reviewerElement);
    Element assignedToElement = new Element(ASSIGNED_TO);
    assignedToElement.setText(reviewIssue.getAssignedTo());
    codeReviewRoot.addContent(assignedToElement);
    Element fileElement = new Element(FILE);
    fileElement.setText(reviewIssue.getTargetFile());
    fileElement.setAttribute(FILE_ATTRIBUTE_LINE, reviewIssue.getLine());
    codeReviewRoot.addContent(fileElement);
    Element typeElement = new Element(TYPE);
    typeElement.setText(reviewIssue.getType().getKey());
    codeReviewRoot.addContent(typeElement);
    Element severityElement = new Element(SEVERITY);
    severityElement.setText(reviewIssue.getSeverity().getKey());
    codeReviewRoot.addContent(severityElement);
    Element summaryElement = new Element(SUMMARY);
    summaryElement.setText(reviewIssue.getSummary());
    codeReviewRoot.addContent(summaryElement);
    Element descriptionElement = new Element(DESCRIPTION);
    descriptionElement.setText(reviewIssue.getDescription());
    codeReviewRoot.addContent(descriptionElement);
    Element annotationElement = new Element(ANNOTATION);
    annotationElement.setText(reviewIssue.getAnnotation());
    codeReviewRoot.addContent(annotationElement);
    Element revisionElement = new Element(REVISION);
    revisionElement.setText(reviewIssue.getRevision());
    codeReviewRoot.addContent(revisionElement);
    Element resolutionElement = new Element(RESOLUTION);
    resolutionElement.setText(reviewIssue.getResolution().getKey());
    codeReviewRoot.addContent(resolutionElement);
    Element statusElement = new Element(STATUS);
    statusElement.setText(reviewIssue.getStatus().getKey());
    codeReviewRoot.addContent(statusElement);
    return codeReviewRoot;
  }

  /**
   * Gets the filled <code>ReviewIssue</code> instance from the <code>Element</code> instance whose
   * sub elements contain information of code review.
   *
   * @param codeReviewElement The <code>Element</code> instance to hold the root element whose sub
   *        elements contain information for <code>ReviewIssue</code> instance.
   * @param reviewIFile the review <code>iFile</code> instance to be stored in the
   *        <code>ReviewIssue</code> instance.
   *
   * @return The filled <code>ReviewIssue</code> instance.
   *
   * @throws ReviewException if the passing <code>Element</code> is null or the problem occurs
   *         when <code>ReviewIssue</code> is being created.
   */
  private static ReviewIssue createCodeReview(Element codeReviewElement, IFile reviewIFile)
    throws ReviewException {
    if (codeReviewElement == null) {
      new ReviewException("element instance is null");
    }
    Element codeReviewMetaElement = codeReviewElement.getChild(CODE_REIVEW_META);
    Element creationDateElement = codeReviewMetaElement.getChild(CREATION_DATE);
    Element lastModificationDateElement = codeReviewMetaElement.getChild(LAST_MODIFICATION_DATE);
    Element reviewerElement = codeReviewElement.getChild(REVIEWER);
    Element assignedToElement = codeReviewElement.getChild(ASSIGNED_TO);
    Element fileElement = codeReviewElement.getChild(FILE);
    Element typeElement = codeReviewElement.getChild(TYPE);
    Element severityElement = codeReviewElement.getChild(SEVERITY);
    Element summaryElement = codeReviewElement.getChild(SUMMARY);
    Element descriptionElement = codeReviewElement.getChild(DESCRIPTION);
    Element annotationElement = codeReviewElement.getChild(ANNOTATION);
    Element revisionElement = codeReviewElement.getChild(REVISION);
    Element resolutionElement = codeReviewElement.getChild(RESOLUTION);
    Element statusElement = codeReviewElement.getChild(STATUS);
    String creationDateFormat = getAttributeText(creationDateElement, FORMAT_ATTRIBUTE);
    String creationDateString = getElementText(creationDateElement);
    Date creationDate = createDate(creationDateString, creationDateFormat);
    String lastModDateFormat = getAttributeText(lastModificationDateElement, FORMAT_ATTRIBUTE);
    String lastModificationDateString = getElementText(lastModificationDateElement);
    Date lastModificationDate = createDate(lastModificationDateString, lastModDateFormat);
    String reviewer = getElementText(reviewerElement);
    String assignedTo = getElementText(assignedToElement);
    String targetFilePath = getElementText(fileElement);
    String summary = getElementText(summaryElement);
    String description = getElementText(descriptionElement);
    String annotation = getElementText(annotationElement);
    String revision = getElementText(revisionElement);
    String id = getAttributeText(codeReviewElement, CODE_REVIEW_ATTRIBUTE_ID);
    String fullyQualifiedClass = getAttributeText(fileElement, FILE_ATTRIBUTE_CLASS);
    String lineNumber = getAttributeText(fileElement, FILE_ATTRIBUTE_LINE);
    String author = getAttributeText(fileElement, FILE_ATTRIBUTE_AUTHOR);
    String editorId = getAttributeText(fileElement, FILE_ATTRIBUTE_EDITOR_ID);
    // compatible for the old version.
    if (editorId.equals("")) {
      ReviewModel reviewModel = ReviewModel.getInstance();
      IProject project = reviewModel.getProjectManager().getProject();
      if (project != null && !lineNumber.equals("")) {
        try {
          editorId = IDE.getEditorDescriptor(project.getFile(targetFilePath)).getId();
        }
        catch (PartInitException e) {
          throw new ReviewException(e.getMessage());
        }
      }
    }
    Type type = getType(typeElement);
    Severity severity = getSeverity(severityElement);
    Resolution resolution = getResolution(resolutionElement);
    Status status = getStatus(statusElement);
    return new ReviewIssue(creationDate, lastModificationDate, reviewer, assignedTo, targetFilePath,
      lineNumber, type, severity, summary, description, annotation, revision, resolution,
      status, reviewIFile);
  }

  /**
   * Gets the attribute text value with the <code>attributeKey</code> in the <code>Element</code>
   * instance. Note that this returns empty string if either the <code>Element</code> is null or
   * value searched by the attribute key is not found.
   *
   * @param element the <code>Element</code> instance.
   * @param attributeKey the attribute key to retrieve the text value.
   *
   * @return the attribute text value.
   */
  private static String getAttributeText(Element element, String attributeKey) {
    String value = (element != null) ? element.getAttributeValue(attributeKey) : "";
    return (value != null) ? value : "";
  }

  /**
   * Gets the <code>Type</code> instance from the type <code>Element</code> instance.
   *
   * @param typeElement the type <code>Element</code> instance. Note that this supports the 1.4.212
   *        version or below to convert old text to a key.
   *
   * @return the <code>Type</code> instance.
   */
  private static Type getType(Element typeElement) {
    String typeText = getElementText(typeElement);
    String typeKey = "";
    if (typeText.equals("Defect")) {
      typeKey = ResourceBundleKey.ITEM_KEY_TYPE_DEFECT;
    }
    else if (typeText.equals("External_Issue")) {
      typeKey = ResourceBundleKey.ITEM_KEY_TYPE_EXTERNAL_ISSUE;
    }
    else if (typeText.equals("Question")) {
      typeKey = ResourceBundleKey.ITEM_KEY_TYPE_QUESTION;
    }
    else if (typeText.equals("Praise")) {
      typeKey = ResourceBundleKey.ITEM_KEY_TYPE_PRAISE;
    }
    else {
      typeKey = typeText;
    }
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    return new Type(typeKey, TypeKeyManager.getInstance(project, reviewId).getOrdinal(typeKey));
  }

  /**
   * Gets the <code>Severity</code> instance from the severity <code>Element</code> instance.
   *
   * @param severityElement the severity <code>Element</code> instance. Note that this supports the
   *        1.4.212 version or below to convert old text to a key.
   *
   * @return the <code>Severity</code> instance.
   */
  private static Severity getSeverity(Element severityElement) {
    String severityText = getElementText(severityElement);
    String severityKey = "";
    if (severityText.equals("Critical")) {
      severityKey = ResourceBundleKey.ITEM_KEY_SEVERITY_CRITICAL;
    }
    else if (severityText.equals("Major")) {
      severityKey = ResourceBundleKey.ITEM_KEY_SEVERITY_MAJOR;
    }
    else if (severityText.equals("Normal")) {
      severityKey = ResourceBundleKey.ITEM_KEY_SEVERITY_NORMAL;
    }
    else if (severityText.equals("Minor")) {
      severityKey = ResourceBundleKey.ITEM_KEY_SEVERITY_MINOR;
    }
    else if (severityText.equals("Trivial")) {
      severityKey = ResourceBundleKey.ITEM_KEY_SEVERITY_TRIVIAL;
    }
    else {
      severityKey = severityText;
    }
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    SeverityKeyManager mananger = SeverityKeyManager.getInstance(project, reviewId);
    return new Severity(severityKey, mananger.getOrdinal(severityKey));
  }

  /**
   * Gets the <code>Resolution</code> instance from the resolution <code>Element</code> instance.
   *
   * @param resolutionElement the resolution <code>Element</code> instance.  Note that this
   *        supports the 1.4.212 version or below to convert old text to a key.
   *
   * @return the <code>Resolution</code> instance.
   */
  private static Resolution getResolution(Element resolutionElement) {
    String resolutionText = getElementText(resolutionElement);
    String resolutionKey = "";
    if (resolutionText.equals("Valid-Needsfixing")) {
      resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_NEEDSFIXING;
    }
    else if (resolutionText.equals("Valid-Wontfix")) {
      resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_WONTFIX;
    }
    else if (resolutionText.equals("Valid-Duplicate")) {
      resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_DUPLICATE;
    }
    else if (resolutionText.equals("Valid-Fixlater")) {
      resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_VALID_FIXLATER;
    }
    else if (resolutionText.equals("Invalid-Wontfix")) {
      resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_INVALID_WONTFIX;
    }
    else if (resolutionText.equals("Unsure-Validity")) {
      resolutionKey = ResourceBundleKey.ITEM_KEY_RESOLUTION_UNSURE_VALIDITY;
    }
    else if (resolutionText.equals("Unset")) {
      resolutionKey = ResourceBundleKey.ITEM_KEY_UNSET;
    }
    else {
      resolutionKey = resolutionText;
    }
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    ResolutionKeyManager manager = ResolutionKeyManager.getInstance(project, reviewId);
    int resolutionOrdinal = manager.getOrdinal(resolutionKey);
    return new Resolution(resolutionKey, resolutionOrdinal);
  }

  /**
   * Gets the <code>Status</code> instance from the status <code>Element</code> instance.
   *
   * @param statusElement the status<code>Element</code> instance.  Note that this supports the
   *        1.4.212 version or below to convert old text to a key.
   *
   * @return the <code>Status</code> instance.
   */
  private static Status getStatus(Element statusElement) {
    String statusText = getElementText(statusElement);
    String statusKey = "";
    if (statusText.equals("Unresolved")) {
      statusKey = ResourceBundleKey.ITEM_KEY_STATUS_UNRESOLVED;
    }
    else if (statusText.equals("Resolved")) {
      statusKey = ResourceBundleKey.ITEM_KEY_STATUS_RESOLVED;
    }
    else {
      statusKey = statusText;
    }
    ReviewModel reviewModel = ReviewModel.getInstance();
    IProject project = reviewModel.getProjectManager().getProject();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
    StatusKeyManager manager = StatusKeyManager.getInstance(project, reviewId);
    return new Status(statusKey, manager.getOrdinal(statusKey));
  }

  /**
   * Creates the <code>Date</code> instance associated with the <code>dateString</code>. Note the
   * this returns current time <code>Date</code> instance if <code>dateString</code> could not be
   * parsed with <code>dateFormat</code>.
   *
   * @param dateString the date string to be parsed.
   * @param dateFormat the date format to let parser know the date string to be parsed.
   *
   * @return the <code>Date</code> instance associated with the <code>dateString</code>.
   */
  private static Date createDate(String dateString, String dateFormat) {
    try {
      return new SimpleDateFormat(dateFormat).parse(dateString);
    }
    catch (ParseException e) {
      log.warning(e.getMessage());
      return new Date();
    }
  }

  /**
   * Gets the text string from the <code>Element</code> instance. Returns empty string if the
   * <code>Element</code> instance is null.
   *
   * @param element the <code>Element</code> instance.
   *
   * @return the text string from the <code>Element</code> instance
   */
  private static String getElementText(Element element) {
    return (element != null) ? element.getText() : "";
  }

  /**
   * Writes the elements which starts for the element into XML output file.
   *
   * @param outputXml The XML output file.
   * @param element The root element to be written to the output.
   *
   * @throws IOException if problems occur.
   */
  private static void write(File outputXml, Element element)
    throws IOException {
    OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputXml), "UTF-8");

    // output JDOM tree to file
    Document elementDocument = new Document(element);
    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
    outputter.output(elementDocument, writer);
    writer.close();
  }

  /**
   * Writes the content of all the code review data stored in the model into the XML file. Note
   * that clients does not need to check if the specified file path (either directory or file)
   * exists.
   * <p>
   * Clients should check if the passing <code>File</code> or/and <code>ReviewIssueModel</code>
   * instance is not null.
   * Otherwise the <code>IllegalArgumentException</code> is thrown.
   *
   * @param reviewId the review ID.
   * @param model The model which contains the <code>ReviewIssue</code> instances.
   * @param xmlFile The output XML file with absolute path.
   *
   * @throws IOException if problems occur.
   */
  public static void write(ReviewId reviewId, ReviewIssueModel model, File xmlFile)
    throws IOException {
    if (xmlFile == null) {
      log.debug("XML file instance is null.");
      throw new IllegalArgumentException("File instance is null.");
    }
    if (model == null) {
      log.debug("Model is null.");
      throw new IllegalArgumentException("ReviewIssueModel instance is null.");
    }
    // if outputFile does not exit, create them.
    if (!xmlFile.getParentFile().exists()) {
      try {
        xmlFile.getParentFile().mkdirs();
      }
      catch (SecurityException e) {
        // show can-not-create directory message.
        log.debug(e.getMessage());
        throw new SecurityException(e.getMessage());
      }
    }
    if (!xmlFile.exists()) {
      try {
        xmlFile.createNewFile();
      }
      catch (SecurityException e) {
        log.debug(e.getMessage());
        throw new SecurityException(e.getMessage());
      }
    }
    if (!xmlFile.canWrite()) {
      // show can-not-write message in a file.
      log.debug(xmlFile + " can not be written");
      return;
    }
    
    Element codeReviewsRootElement = new Element(CODE_REVIEWS);
    codeReviewsRootElement.setAttribute(ATTRIBUTE_ID, reviewId.getReviewId());
    if (model != null) {
      for (Iterator i = model.iterator(); i.hasNext();) {
        ReviewIssue codeReview = (ReviewIssue) i.next();
        if (xmlFile.equals(codeReview.getReviewIFile().getLocation().toFile())) {
          Element codeReviewElement = createCodeReviewElement(codeReview);
          codeReviewsRootElement.addContent(codeReviewElement);
        }
      }
    }
    write(xmlFile, codeReviewsRootElement);
  }
  
  /**
   * Writes empty code review XML file.
   * @param xmlFile the <code>File</code> XML file.
   * @throws ReviewException if problems occur during writing process.
   */
  public static void writeEmptyCodeReview(File xmlFile) throws ReviewException {
    if (xmlFile == null) {
      log.debug("XML file instance is null.");
      throw new IllegalArgumentException("File instance is null.");
    }
    Element codeReviewsRootElement = new Element(CODE_REVIEWS);   
    ReviewModel reviewModel = ReviewModel.getInstance();
    ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();   
    codeReviewsRootElement.setAttribute(ATTRIBUTE_ID, reviewId.getReviewId());
    try {
      write(xmlFile, codeReviewsRootElement);
    }
    catch (IOException e) {
      throw new ReviewException("IOException: " + e.getMessage());
    }
  }

  /**
   * Reads the xmlFile of the <code>IFile</code> instance to create <code>ReviewIssue</code>
   * instances in the <code>ReviewIssueModel</code> instance. Note that the review file will
   * be skipped if the problems occur on the reading process (e.g. XML file is not valid due to
   * the file confliction).
   *
   * @param reviewId the review id.
   * @param model The <code>ReviewIssueModel</code> instance to hold <code>ReviewIssue</code>
   *        instances.
   * @param iFiles The array of IFile implementing class instance to hold file path.
   */
  public static void read(ReviewId reviewId, ReviewIssueModel model, IFile[] iFiles) {
    if (model == null || iFiles == null) {
      log.debug("ReviewIssueModel or IFile[] is null.");
      return;
    }
    for (int i = 0; i < iFiles.length; i++) {
      log.debug("reading " + iFiles[i] + " ...");
      File xmlFile = new File(iFiles[i].getLocation().toString());
      SAXBuilder builder = new SAXBuilder();
      Document document = null;
      try {
        document = builder.build(xmlFile);
      }
      catch (JDOMException e) {
        log.error(e);
        continue;
      }
      catch (IOException e) {
        log.error(e);
        continue;
      }
      Element codeReviewsElement = document.getRootElement();
      String reviewIdName = codeReviewsElement.getAttributeValue(ATTRIBUTE_ID);
      if (reviewIdName != null && reviewId != null && reviewIdName.equals(reviewId.getReviewId())) {
        List codeReviewList = codeReviewsElement.getChildren();
        List<ReviewIssue> tempCodeReviewList = new ArrayList<ReviewIssue>();
        boolean isSuccessIteration = true;
        for (Iterator iterator = codeReviewList.iterator(); iterator.hasNext();) {
          Element codeReviewElement = (Element) iterator.next();
          try {
            ReviewIssue codeReview = createCodeReview(codeReviewElement, iFiles[i]);
            tempCodeReviewList.add(codeReview);
          }
          catch (ReviewException e) {
            log.error(e);
            isSuccessIteration = false;
            break;
          }
        }
  
        // adds the list to the model only when the iteration of the file succeed.
        if (isSuccessIteration) {
          model.addAll(tempCodeReviewList);
        }
      }
    }
  }
  
  /**
   * Checks if the passing <code>File</code> instance is associated with <code>String</code>
   * review ID.
   * @param reviewId the review ID.
   * @param reviewFile the review file to be checked.
   * @return <code>true</code> if the code>File</code> instance is associated with
   * <code>String</code>.
   * @throws ReviewException if problems occur during file reading process.
   */
  static boolean isReviewIdAssociatedFile(String reviewId, File reviewFile) throws ReviewException {
    SAXBuilder builder = new SAXBuilder();
    Document document = null;
    try {
      document = builder.build(reviewFile);
    }
    catch (JDOMException e) {
      throw new ReviewException("JDOMException: " + e.getMessage());
    }
    catch (IOException e) {
      throw new ReviewException("IOException: " + e.getMessage());
    }
    Element codeReviewsElement = document.getRootElement();
    String reviewIdName = codeReviewsElement.getAttributeValue(ATTRIBUTE_ID);
    return (reviewIdName != null && reviewIdName.equals(reviewId));
  }
  
  /**
   * Removes all <code>IFile</code> instances.
   * @param iFiles the array of the <code>IFile</code> instances.
   * @throws ReviewException if problems occur during the file deletion.
   */
  static void remove(IFile[] iFiles) throws ReviewException {
    for (int i = 0; i < iFiles.length; i++) {
      try {
        iFiles[i].delete(true, false, null);
      }
      catch (CoreException e) {
        throw new ReviewException(e.getMessage());
      }
    }
  }
}
