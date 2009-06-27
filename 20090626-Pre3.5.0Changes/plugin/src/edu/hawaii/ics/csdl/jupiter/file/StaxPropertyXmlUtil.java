package edu.hawaii.ics.csdl.jupiter.file;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import edu.hawaii.ics.csdl.jupiter.file.property.CreationDate;
import edu.hawaii.ics.csdl.jupiter.file.property.FieldItem;
import edu.hawaii.ics.csdl.jupiter.file.property.FieldItems;
import edu.hawaii.ics.csdl.jupiter.file.property.Files;
import edu.hawaii.ics.csdl.jupiter.file.property.Filter;
import edu.hawaii.ics.csdl.jupiter.file.property.Filters;
import edu.hawaii.ics.csdl.jupiter.file.property.Phase;
import edu.hawaii.ics.csdl.jupiter.file.property.Property;
import edu.hawaii.ics.csdl.jupiter.file.property.Review;
import edu.hawaii.ics.csdl.jupiter.file.property.Reviewers;

/**
 * Utility for reading/writing property XML files using StAX.
 * 
 * @author jsakuda
 */
public class StaxPropertyXmlUtil {
  
  /** Prevent instantiation. */
  private StaxPropertyXmlUtil() {
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
    writer.writeStartElement(PropertyConstraints.ELEMENT_REVIEW);
    writer.writeAttribute(PropertyConstraints.ATTRIBUTE_ID, review.getId());

    writer.writeStartElement(PropertyConstraints.ELEMENT_DESCRIPTION);
    writer.writeCharacters(review.getDescription());
    writer.writeEndElement(); // Description

    writer.writeStartElement(PropertyConstraints.ELEMENT_AUTHOR);
    writer.writeCharacters(review.getAuthor());
    writer.writeEndElement(); // Author

    writer.writeStartElement(PropertyConstraints.ELEMENT_CREATION_DATE);
    CreationDate creationDate = review.getCreationDate();
    writer.writeAttribute(PropertyConstraints.ATTRIBUTE_FORMAT, creationDate.getFormat());
    writer.writeCharacters(creationDate.getValue());
    writer.writeEndElement(); // CreationDate

    writer.writeStartElement(PropertyConstraints.ELEMENT_DIRECTORY);
    writer.writeCharacters(review.getDirectory());
    writer.writeEndElement(); // Directory

    StaxPropertyXmlUtil.writeReviewers(writer, review);
    StaxPropertyXmlUtil.writeFiles(writer, review);
    StaxPropertyXmlUtil.writeFieldItems(writer, review);
    StaxPropertyXmlUtil.writeFilters(writer, review);
    
    writer.writeEndElement(); // Review
  }

  /**
   * Writes the reviewers of the review to the XML stream.
   * 
   * @param writer The stream writer to write to.
   * @param review The review to write data from.
   * @throws XMLStreamException Thrown if there is an error writing to the stream.
   */
  public static void writeReviewers(XMLStreamWriter writer, Review review)
      throws XMLStreamException {
    writer.writeStartElement(PropertyConstraints.ELEMENT_REVIEWERS);
    Reviewers reviewers = review.getReviewers();
    List<Reviewers.Entry> reviewerEntries = reviewers.getEntry();
    for (Reviewers.Entry entry : reviewerEntries) {
      writer.writeEmptyElement(PropertyConstraints.ELEMENT_ENTRY);
      writer.writeAttribute(PropertyConstraints.ATTRIBUTE_ID, entry.getId());
      writer.writeAttribute(PropertyConstraints.ATTRIBUTE_NAME, entry.getName());
    }
    writer.writeEndElement(); // Reviewers
  }

  /**
   * Writes the files of the review to the XML stream.
   * 
   * @param writer The stream writer to write to.
   * @param review The review to write data from.
   * @throws XMLStreamException Thrown if there is an error writing to the stream.
   */
  public static void writeFiles(XMLStreamWriter writer, Review review)
      throws XMLStreamException {
    Files files = review.getFiles();
    List<Files.Entry> fileEntries = files.getEntry();
    if (fileEntries.isEmpty()) {
      writer.writeEmptyElement(PropertyConstraints.ELEMENT_FILES);
    }
    else {
      writer.writeStartElement(PropertyConstraints.ELEMENT_FILES);
      for (Files.Entry entry : fileEntries) {
        writer.writeEmptyElement(PropertyConstraints.ELEMENT_ENTRY);
        writer.writeAttribute(PropertyConstraints.ATTRIBUTE_NAME, entry.getName());
      }
      writer.writeEndElement(); // Files
    }
  }

  /**
   * Writes the field items of the review to the XML stream.
   * 
   * @param writer The stream writer to write to.
   * @param review The review to write data from.
   * @throws XMLStreamException Thrown if there is an error writing to the stream.
   */
  public static void writeFieldItems(XMLStreamWriter writer, Review review)
      throws XMLStreamException {
    writer.writeStartElement(PropertyConstraints.ELEMENT_FIELD_ITEMS);
    FieldItems fieldItems = review.getFieldItems();
    List<FieldItem> fieldItemList = fieldItems.getFieldItem();
    for (FieldItem fieldItem : fieldItemList) {
      writer.writeStartElement(PropertyConstraints.ELEMENT_FIELD_ITEM);
      writer.writeAttribute(PropertyConstraints.ATTRIBUTE_ID, fieldItem.getId());
      writer.writeAttribute(PropertyConstraints.ATTRIBUTE_DEFAULT, fieldItem.getDefault());

      List<FieldItem.Entry> entries = fieldItem.getEntry();
      for (FieldItem.Entry entry : entries) {
        writer.writeEmptyElement(PropertyConstraints.ELEMENT_ENTRY);
        writer.writeAttribute(PropertyConstraints.ATTRIBUTE_NAME, entry.getName());
      }
      writer.writeEndElement(); // Field Item
    }
    writer.writeEndElement(); // Field Items
  }

  /**
   * Writes the filters of the review to the XML stream.
   * 
   * @param writer The stream writer to write to.
   * @param review The review to write data from.
   * @throws XMLStreamException Thrown if there is an error writing to the stream.
   */
  public static void writeFilters(XMLStreamWriter writer, Review review)
      throws XMLStreamException {
    writer.writeStartElement(PropertyConstraints.ELEMENT_FILTERS);
    Filters filters = review.getFilters();
    List<Phase> phases = filters.getPhase();
    for (Phase phase : phases) {
      writer.writeStartElement(PropertyConstraints.ELEMENT_PHASE);
      writer.writeAttribute(PropertyConstraints.ATTRIBUTE_NAME, phase.getName());
      writer.writeAttribute(PropertyConstraints.ATTRIBUTE_ENABLED, phase.isEnabled()
          .toString());

      List<Filter> filterList = phase.getFilter();
      for (Filter filter : filterList) {
        writer.writeEmptyElement(PropertyConstraints.ELEMENT_FILTER);
        writer.writeAttribute(PropertyConstraints.ATTRIBUTE_NAME, filter.getName());
        writer.writeAttribute(PropertyConstraints.ATTRIBUTE_VALUE, filter.getValue());
        writer.writeAttribute(PropertyConstraints.ATTRIBUTE_ENABLED, filter.isEnabled()
            .toString());
      }
      writer.writeEndElement(); // Phase
    }
    writer.writeEndElement(); // Filters
  }

  /**
   * Parses a property file (property.xml or .jupiter) and creates a <code>Property</code>
   * object.
   * 
   * @param reader The reader used to read in the file.
   * @return Returns a <code>Property</code> representing the data in the file.
   * @throws XMLStreamException Thrown if there is an error reading the file.
   */
  public static Property parsePropertyFile(XMLStreamReader reader) throws XMLStreamException {
    Property property = null;
    Review review = null;

    int eventType = reader.getEventType();
    while (reader.hasNext()) {
      eventType = reader.next();

      if (eventType == XMLStreamConstants.START_ELEMENT) {
        QName elementQName = reader.getName();
        String elementName = elementQName.toString();

        if (PropertyConstraints.ELEMENT_PROPERTY.equals(elementName)) {
          // create a new property object, this is the root element
          property = new Property();
        }
        else if (PropertyConstraints.ELEMENT_REVIEW.equals(elementName)) {
          // create a new review and add it to the property object
          review = new Review();
          property.getReview().add(review);

          // get the attributes of review
          String id = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_ID);
          review.setId(id);
        }
        else if (PropertyConstraints.ELEMENT_DESCRIPTION.equals(elementName)) {
          eventType = reader.next(); // we need the CHARACTERS event
          if (eventType == XMLStreamConstants.CHARACTERS) {
            String description = reader.getText();
            review.setDescription(description);
          }
        }
        else if (PropertyConstraints.ELEMENT_AUTHOR.equals(elementName)) {
          eventType = reader.next(); // we need the CHARACTERS event
          if (eventType == XMLStreamConstants.CHARACTERS) {
            String author = reader.getText();
            review.setAuthor(author == null ? "" : author);
          }
        }
        else if (PropertyConstraints.ELEMENT_CREATION_DATE.equals(elementName)) {
          CreationDate creationDate = new CreationDate();

          String format = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_FORMAT);
          creationDate.setFormat(format);

          eventType = reader.next(); // we need the CHARACTERS event
          if (eventType == XMLStreamConstants.CHARACTERS) {
            String dateValue = reader.getText();
            creationDate.setValue(dateValue);
          }

          review.setCreationDate(creationDate);
        }
        else if (PropertyConstraints.ELEMENT_DIRECTORY.equals(elementName)) {
          eventType = reader.next(); // we need the CHARACTERS event
          if (eventType == XMLStreamConstants.CHARACTERS) {
            String directory = reader.getText();
            review.setDirectory(directory);
          }
        }
        else if (PropertyConstraints.ELEMENT_REVIEWERS.equals(elementName)) {
          StaxPropertyXmlUtil.parseReviewers(reader, review);
        }
        else if (PropertyConstraints.ELEMENT_FILES.equals(elementName)) {
          StaxPropertyXmlUtil.parseFiles(reader, review);
        }
        else if (PropertyConstraints.ELEMENT_FIELD_ITEMS.equals(elementName)) {
          review.setFieldItems(new FieldItems());
        }
        else if (PropertyConstraints.ELEMENT_FIELD_ITEM.equals(elementName)) {
          StaxPropertyXmlUtil.parseFieldItem(reader, review);
        }
        else if (PropertyConstraints.ELEMENT_FILTERS.equals(elementName)) {
          review.setFilters(new Filters());
        }
        else if (PropertyConstraints.ELEMENT_PHASE.equals(elementName)) {
          StaxPropertyXmlUtil.parsePhase(reader, review);
        }
      }
      else if (eventType == XMLStreamConstants.END_ELEMENT
          && PropertyConstraints.ELEMENT_REVIEW.equals(reader.getName().toString())) {
          review = null;
      }
    }

    return property;
  }

  /**
   * Reads the reviewers from the <code>XMLStreamReader</code>. This method assumes that the
   * cursor has read in the Reviewers start element event and not gone any further.
   * 
   * @param reader The XML stream reader to read from.
   * @param review The review to add the reviewers to.
   */
  private static void parseReviewers(XMLStreamReader reader, Review review)
      throws XMLStreamException {
    Reviewers reviewers = new Reviewers();

    boolean endFound = false;

    while (!endFound) {
      if (reader.hasNext()) {
        int eventType = reader.next();

        if (eventType == XMLStreamConstants.START_ELEMENT) {
          QName elementQName = reader.getName();
          String elementName = elementQName.toString();

          if (PropertyConstraints.ELEMENT_ENTRY.equals(elementName)) {
            // this is an entry for reviewers
            String id = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_ID);
            String name = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_NAME);

            Reviewers.Entry entry = new Reviewers.Entry();
            entry.setId(id);
            entry.setName(name);

            reviewers.getEntry().add(entry);
          }
        }
        else if (eventType == XMLStreamConstants.END_ELEMENT) {
          QName elementQName = reader.getName();

          if (PropertyConstraints.ELEMENT_REVIEWERS.equals(elementQName.toString())) {
            // this is the end of the reviewers
            endFound = true;
          }
        }
      }
    }
    review.setReviewers(reviewers);
  }

  /**
   * Reads the files from the <code>XMLStreamReader</code>. This method assumes that the cursor
   * has read in the Files start element event and not gone any further.
   * 
   * @param reader The XML stream reader to read from.
   * @param review The review to add the files to.
   */
  private static void parseFiles(XMLStreamReader reader, Review review)
      throws XMLStreamException {
    Files files = new Files();

    boolean endFound = false;

    while (!endFound) {
      if (reader.hasNext()) {
        int eventType = reader.next();

        if (eventType == XMLStreamConstants.START_ELEMENT) {
          QName elementQName = reader.getName();
          String elementName = elementQName.toString();

          if (PropertyConstraints.ELEMENT_ENTRY.equals(elementName)) {
            // this is an entry for files
            String name = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_NAME);

            Files.Entry entry = new Files.Entry();
            entry.setName(name);

            files.getEntry().add(entry);
          }
        }
        else if (eventType == XMLStreamConstants.END_ELEMENT) {
          QName elementQName = reader.getName();

          if (PropertyConstraints.ELEMENT_FILES.equals(elementQName.toString())) {
            // this is the end of the files
            endFound = true;
          }
        }
      }
    }
    review.setFiles(files);
  }

  /**
   * Reads a field item from the <code>XMLStreamReader</code>. This method assumes that the
   * cursor has read in the field item start element event and not gone any further.
   * 
   * @param reader The XML stream reader to read from.
   * @param review The review to add the files to.
   */
  private static void parseFieldItem(XMLStreamReader reader, Review review)
      throws XMLStreamException {
    FieldItems fieldItems = review.getFieldItems();
    if (fieldItems == null) {
      fieldItems = new FieldItems();
      review.setFieldItems(fieldItems);
    }
    
    String id = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_ID);
    String defaultValue = reader
        .getAttributeValue(null, PropertyConstraints.ATTRIBUTE_DEFAULT);

    FieldItem fieldItem = new FieldItem();
    fieldItem.setId(id);
    fieldItem.setDefault(defaultValue);

    boolean endFound = false;

    while (!endFound) {
      if (reader.hasNext()) {
        int eventType = reader.next();

        if (eventType == XMLStreamConstants.START_ELEMENT) {
          QName elementQName = reader.getName();
          String elementName = elementQName.toString();

          if (PropertyConstraints.ELEMENT_ENTRY.equals(elementName)) {
            // this is an entry for field item
            String name = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_NAME);

            FieldItem.Entry entry = new FieldItem.Entry();
            entry.setName(name);

            fieldItem.getEntry().add(entry);
          }
        }
        else if (eventType == XMLStreamConstants.END_ELEMENT) {
          QName elementQName = reader.getName();

          if (PropertyConstraints.ELEMENT_FIELD_ITEM.equals(elementQName.toString())) {
            // this is the end of the field item
            endFound = true;
          }
        }
      }
    }
    fieldItems.getFieldItem().add(fieldItem);
  }

  /**
   * Reads a phase from the <code>XMLStreamReader</code>. This method assumes that the cursor
   * has read in the phase start element event and not gone any further.
   * 
   * @param reader The XML stream reader to read from.
   * @param review The review to add the phase to.
   */
  private static void parsePhase(XMLStreamReader reader, Review review)
      throws XMLStreamException {
    Filters filters = review.getFilters();
    if (filters == null) {
      filters = new Filters();
      review.setFilters(filters);
    }
    
    String name = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_NAME);
    String enabled = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_ENABLED);

    Phase phase = new Phase();
    phase.setName(name);
    phase.setEnabled(Boolean.valueOf(enabled));

    boolean endFound = false;

    // parse out all the Filter elements under the Phase
    while (!endFound) {
      if (reader.hasNext()) {
        int eventType = reader.next();

        if (eventType == XMLStreamConstants.START_ELEMENT) {
          QName elementQName = reader.getName();
          String elementName = elementQName.toString();

          if (PropertyConstraints.ELEMENT_FILTER.equals(elementName)) {
            name = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_NAME);
            String value = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_VALUE);
            enabled = reader.getAttributeValue(null, PropertyConstraints.ATTRIBUTE_ENABLED);

            Filter filter = new Filter();
            filter.setName(name);
            filter.setValue(value);
            filter.setEnabled(Boolean.valueOf(enabled));

            phase.getFilter().add(filter);
          }
        }
        else if (eventType == XMLStreamConstants.END_ELEMENT) {
          QName elementQName = reader.getName();

          if (PropertyConstraints.ELEMENT_PHASE.equals(elementQName.toString())) {
            // this is the end of the phase
            endFound = true;
          }
        }
      }
    }
    filters.getPhase().add(phase);
  }
}
