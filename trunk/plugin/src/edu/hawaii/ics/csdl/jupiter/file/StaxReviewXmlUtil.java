package edu.hawaii.ics.csdl.jupiter.file;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import edu.hawaii.ics.csdl.jupiter.file.review.CreationDate;
import edu.hawaii.ics.csdl.jupiter.file.review.File;
import edu.hawaii.ics.csdl.jupiter.file.review.LastModificationDate;
import edu.hawaii.ics.csdl.jupiter.file.review.Review;
import edu.hawaii.ics.csdl.jupiter.file.review.ReviewIssue;
import edu.hawaii.ics.csdl.jupiter.file.review.ReviewIssueMeta;

/**
 * Utility for reading/writing review XML files using StAX.
 * 
 * @author jsakuda
 */
public class StaxReviewXmlUtil {

  private static final String ELEMENT_REVIEW = "Review";
  
  private static final String ELEMENT_REVIEW_ISSUE = "ReviewIssue";

  private static final String ELEMENT_REVIEW_ISSUE_META = "ReviewIssueMeta";
  
  private static final String ELEMENT_CREATION_DATE = "CreationDate";
  
  private static final String ELEMENT_LAST_MODIFICATION_DATE = "LastModificationDate";
  
  private static final String ELEMENT_REVIEWER_ID = "ReviewerId";
  
  private static final String ELEMENT_ASSIGNED_TO = "AssignedTo";
  
  private static final String ELEMENT_FILE = "File";
  
  private static final String ELEMENT_TYPE = "Type";
  
  private static final String ELEMENT_SEVERITY = "Severity";

  private static final String ELEMENT_SUMMARY = "Summary";
  
  private static final String ELEMENT_DESCRIPTION = "Description";
  
  private static final String ELEMENT_ANNOTATION = "Annotation";
  
  private static final String ELEMENT_REVISION = "Revision";
  
  private static final String ELEMENT_RESOLUTION = "Resolution";
  
  private static final String ELEMENT_STATUS = "Status";
  
  private static final String ATTRIBUTE_ID = "id";
  
  private static final String ATTRIBUTE_FORMAT = "format";
  
  private static final String ATTRIBUTE_LINE = "line";
  
  /** Prevent instantiation. */
  private StaxReviewXmlUtil() {
    // do nothing
  }

  /**
   * Writes a single complete review to the XML stream writer.
   * 
   * @param writer The XML writer to write to.
   * @param review The review containing the data to write.
   * @throws XMLStreamException Thrown of if there is an error writing to the stream writer.
   */
  public static void writeReview(XMLStreamWriter writer, Review review)
      throws XMLStreamException {
    writer.writeStartElement(ELEMENT_REVIEW);
    writer.writeAttribute(ATTRIBUTE_ID, review.getId());
    
    List<ReviewIssue> reviewIssues = review.getReviewIssue();
    for (ReviewIssue reviewIssue : reviewIssues) {
      writer.writeStartElement(ELEMENT_REVIEW_ISSUE);
      writer.writeAttribute(ATTRIBUTE_ID, reviewIssue.getId());
      
      StaxReviewXmlUtil.writeReviewIssueMeta(writer, reviewIssue);
      
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_REVIEWER_ID, reviewIssue.getReviewerId());
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_ASSIGNED_TO, reviewIssue.getAssignedTo());
      
      writer.writeStartElement(ELEMENT_FILE);
      File file = reviewIssue.getFile();
      if (file.getLine() != null) {
        writer.writeAttribute(ATTRIBUTE_LINE, String.valueOf(file.getLine()));
      }
      if (file.getValue() != null) {
        writer.writeCharacters(file.getValue());
      }
      writer.writeEndElement(); // File
      
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_TYPE, reviewIssue.getType());
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_SEVERITY, reviewIssue.getSeverity());
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_SUMMARY, reviewIssue.getSummary());
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_DESCRIPTION, reviewIssue.getDescription());
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_ANNOTATION, reviewIssue.getAnnotation());
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_REVISION, reviewIssue.getRevision());
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_RESOLUTION, reviewIssue.getResolution());
      StaxReviewXmlUtil.writeElement(writer, ELEMENT_STATUS, reviewIssue.getStatus());
      
      writer.writeEndElement(); // ReviewIssue
    }
    
    writer.writeEndElement(); // Review
  }

  /**
   * Writes a text only element to the stream. The value of the element will be the value
   * given. No text will be written if the value is null.
   * 
   * @param writer The stream writer to write the element to.
   * @param elementName The element to write.
   * @param value The value of the text element.
   * @throws XMLStreamException Thrown if there is an error writing to the stream.
   */
  private static void writeElement(XMLStreamWriter writer, String elementName, String value)
      throws XMLStreamException {
    writer.writeStartElement(elementName);
    if (value != null) {
      writer.writeCharacters(value);
    }
    writer.writeEndElement();
  }
  
  /**
   * Writes the review issue metadata to the stream writer.
   * 
   * @param writer The stream writer to write to.
   * @param reviewIssue The review issue with the metadata to write.
   * @throws XMLStreamException Thrown if there is an error writing to the stream writer.
   */
  public static void writeReviewIssueMeta(XMLStreamWriter writer, ReviewIssue reviewIssue)
      throws XMLStreamException {
    writer.writeStartElement(ELEMENT_REVIEW_ISSUE_META);
    ReviewIssueMeta reviewIssueMeta = reviewIssue.getReviewIssueMeta();
    
    writer.writeStartElement(ELEMENT_CREATION_DATE);
    CreationDate creationDate = reviewIssueMeta.getCreationDate();
    writer.writeAttribute(ATTRIBUTE_FORMAT, creationDate.getFormat());
    writer.writeCharacters(creationDate.getValue());
    writer.writeEndElement(); // CreationDate
    
    writer.writeStartElement(ELEMENT_LAST_MODIFICATION_DATE);
    LastModificationDate lastModificationDate = reviewIssueMeta.getLastModificationDate();
    writer.writeAttribute(ATTRIBUTE_FORMAT, lastModificationDate.getFormat());
    writer.writeCharacters(lastModificationDate.getValue());
    writer.writeEndElement(); // LastModificationDate
    
    writer.writeEndElement(); // ReviewIssueMeta
  }
  
  /**
   * Parses a .review file and creates a <code>Review</code> object.
   * 
   * @param reader The reader used to read in the file.
   * @return Returns a <code>Review</code> representing the data in the file.
   * @throws XMLStreamException Thrown if there is an error reading the file.
   */
  public static Review parseReviewFile(XMLStreamReader reader) throws XMLStreamException {
    Review review = new Review();
    
    int eventType = reader.getEventType();
    while (reader.hasNext()) {
      eventType = reader.next();

      if (eventType == XMLStreamConstants.START_ELEMENT) {
        QName elementQName = reader.getName();
        String elementName = elementQName.toString();

        if (ELEMENT_REVIEW.equals(elementName)) {
          String id = reader.getAttributeValue(null, ATTRIBUTE_ID);
          review.setId(id);
        }
        else if (ELEMENT_REVIEW_ISSUE.equals(elementName)) {
          StaxReviewXmlUtil.parseReviewIssue(reader, review);
        }
      } 
    }
    
    return review;
  }

  /**
   * Reads a review issue from the reader. This method assumes that the cursor has read in the
   * ReviewIssue start element and has not gone any farther.
   * 
   * @param reader The XML stream reader to read the review issue from.
   * @param review The review that the review issue should be added to.
   * @throws XMLStreamException Thrown if there is an error while reading from the stream.
   */
  private static void parseReviewIssue(XMLStreamReader reader, Review review) throws XMLStreamException {
    ReviewIssue reviewIssue = new ReviewIssue();
    
    String id = reader.getAttributeValue(null, ATTRIBUTE_ID);
    reviewIssue.setId(id);
    
    boolean endFound = false;

    while (!endFound) {
      if (reader.hasNext()) {
        int eventType = reader.next();
        
        if (eventType == XMLStreamConstants.START_ELEMENT) {
          QName elementQName = reader.getName();
          String elementName = elementQName.toString();
          
          if (ELEMENT_REVIEW_ISSUE_META.equals(elementName)) {
            StaxReviewXmlUtil.parseReviewIssueMeta(reader, reviewIssue);
          }
          else if (ELEMENT_REVIEWER_ID.equals(elementName)) {
            // get the CHARACTERS event to get the reviewer id
            eventType = reader.next();
            if (eventType == XMLStreamConstants.CHARACTERS) {
              reviewIssue.setReviewerId(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
          }
          else if (ELEMENT_ASSIGNED_TO.equals(elementName)) {
            // get the CHARACTERS event to get the assigned to
            eventType = reader.next();
            if (eventType == XMLStreamConstants.CHARACTERS) {
              reviewIssue.setAssignedTo(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
          }
          else if (ELEMENT_FILE.equals(elementName)) {
            File file = new File();
            
            String line = reader.getAttributeValue(null, ATTRIBUTE_LINE);
            if (line != null && !"".equals(line)) {
              file.setLine(Integer.parseInt(line));
            }
            
            // get the CHARACTERS event to get the file
            eventType = reader.next();
            if (eventType == XMLStreamConstants.CHARACTERS) {
              file.setValue(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
            reviewIssue.setFile(file);
          }
          else if (ELEMENT_TYPE.equals(elementName)) {
            // get the CHARACTERS event to get the type
            eventType = reader.next();
            if (eventType == XMLStreamConstants.CHARACTERS) {
              reviewIssue.setType(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
          }
          else if (ELEMENT_SEVERITY.equals(elementName)) {
            // get the CHARACTERS event to get the severity
            eventType = reader.next();
            if (eventType == XMLStreamConstants.CHARACTERS) {
              reviewIssue.setSeverity(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
          }
          else if (ELEMENT_SUMMARY.equals(elementName)) {
            // get the CHARACTERS event to get the summary
            StringBuilder sb = new StringBuilder();
            // this won't work if end element ever needs to be handled
            while (reader.next() == XMLStreamConstants.CHARACTERS) {
              sb.append(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
            reviewIssue.setSummary(sb.toString());
//            eventType = reader.next();
//            if (eventType == XMLStreamConstants.CHARACTERS) {
//              reviewIssue.setSummary(StaxReviewXmlUtil.getFixedText(reader.getText()));
//            }
          }
          else if (ELEMENT_DESCRIPTION.equals(elementName)) {
            // get the CHARACTERS event to get the description
            StringBuilder sb = new StringBuilder();
            // this won't work if end element ever needs to be handled
            while (reader.next() == XMLStreamConstants.CHARACTERS) {
              sb.append(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
            reviewIssue.setDescription(sb.toString());
//            eventType = reader.next();
//            if (eventType == XMLStreamConstants.CHARACTERS) {
//              reviewIssue.setDescription(StaxReviewXmlUtil.getFixedText(reader.getText()));
//            }
          }
          else if (ELEMENT_ANNOTATION.equals(elementName)) {
            // get the CHARACTERS event to get the annotation
            StringBuilder sb = new StringBuilder();
            // this won't work if end element ever needs to be handled
            while (reader.next() == XMLStreamConstants.CHARACTERS) {
              sb.append(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
            reviewIssue.setAnnotation(sb.toString());
//            eventType = reader.next();
//            if (eventType == XMLStreamConstants.CHARACTERS) {
//              reviewIssue.setAnnotation(StaxReviewXmlUtil.getFixedText(reader.getText()));
//            }
          }
          else if (ELEMENT_REVISION.equals(elementName)) {
            // get the CHARACTERS event to get the revision
            StringBuilder sb = new StringBuilder();
            // this won't work if end element ever needs to be handled
            while (reader.next() == XMLStreamConstants.CHARACTERS) {
              sb.append(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
            reviewIssue.setRevision(sb.toString());
//            eventType = reader.next();
//            if (eventType == XMLStreamConstants.CHARACTERS) {
//              reviewIssue.setRevision(StaxReviewXmlUtil.getFixedText(reader.getText()));
//            }
          }
          else if (ELEMENT_RESOLUTION.equals(elementName)) {
            // get the CHARACTERS event to get the resolution
            eventType = reader.next();
            if (eventType == XMLStreamConstants.CHARACTERS) {
              reviewIssue.setResolution(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
          }
          else if (ELEMENT_STATUS.equals(elementName)) {
            // get the CHARACTERS event to get the status
            eventType = reader.next();
            if (eventType == XMLStreamConstants.CHARACTERS) {
              reviewIssue.setStatus(StaxReviewXmlUtil.getFixedText(reader.getText()));
            }
          }
        }
        else if (eventType == XMLStreamConstants.END_ELEMENT) {
          QName elementQName = reader.getName();

          if (ELEMENT_REVIEW_ISSUE.equals(elementQName.toString())) {
            // this is the end of the review issue
            endFound = true;
          }
        }
      }
    }
    
    review.getReviewIssue().add(reviewIssue);
  }

  /**
   * If the given text is not null the text will be returned otherwise if it is null it will
   * return the empty string.
   * 
   * @param text The text to check.
   * @return Returns the "fixed" text value.
   */
  private static String getFixedText(String text) {
    String value = "";
    if (text != null) {
      value = text;
    }
    return value;
  }
  
  /**
   * Reads the review issue metadata from the <code>XMLStreamReader</code>. This method assumes
   * that the cursor has read in the ReviewIssueMeta start element event and not gone any
   * further.
   * 
   * @param reader The XML stream reader to read from.
   * @param review The review issue to add the metadata to.
   * @throws XMLStreamException Thrown if there is an error while reading from the stream.
   */
  private static void parseReviewIssueMeta(XMLStreamReader reader, ReviewIssue reviewIssue)
      throws XMLStreamException {
    ReviewIssueMeta meta = new ReviewIssueMeta();
    
    boolean endFound = false;
    
    while (!endFound) {
      if (reader.hasNext()) {
        int eventType = reader.next();

        if (eventType == XMLStreamConstants.START_ELEMENT) {
          QName elementQName = reader.getName();
          String elementName = elementQName.toString();

          
          if (ELEMENT_CREATION_DATE.equals(elementName)) {
            CreationDate creationDate = new CreationDate();
            creationDate.setFormat(reader.getAttributeValue(null, ATTRIBUTE_FORMAT));
            
            // get the CHARACTERS event to get the date string
            eventType = reader.next();
            if (eventType == XMLStreamConstants.CHARACTERS) {
              creationDate.setValue(reader.getText());
            }
            meta.setCreationDate(creationDate);
          }
          else if (ELEMENT_LAST_MODIFICATION_DATE.equals(elementName)) {
            LastModificationDate lastModDate = new LastModificationDate();
            lastModDate.setFormat(reader.getAttributeValue(null, ATTRIBUTE_FORMAT));
            
            // get the CHARACTERS event to get the date string
            eventType = reader.next();
            if (eventType == XMLStreamConstants.CHARACTERS) {
              lastModDate.setValue(reader.getText());
            }
            meta.setLastModificationDate(lastModDate);
          }
        }
        else if (eventType == XMLStreamConstants.END_ELEMENT) {
          QName elementQName = reader.getName();

          if (ELEMENT_REVIEW_ISSUE_META.equals(elementQName.toString())) {
            // this is the end of the reviewers
            endFound = true;
          }
        }
      }
    }
    
    reviewIssue.setReviewIssueMeta(meta);
  }
}
