package edu.hawaii.ics.csdl.jupiter.file;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import edu.hawaii.ics.csdl.jupiter.file.preference.ColumnEntry;
import edu.hawaii.ics.csdl.jupiter.file.preference.General;
import edu.hawaii.ics.csdl.jupiter.file.preference.Phase;
import edu.hawaii.ics.csdl.jupiter.file.preference.Preference;
import edu.hawaii.ics.csdl.jupiter.file.preference.View;

/**
 * Utility for reading/writing preference XML files using StAX.
 * 
 * @author jsakuda
 */
public class StaxPreferenceXmlUtil {

  private static final String ELEMENT_PREFERENCE = "Preference";

  private static final String ELEMENT_GENERAL = "General";

  private static final String ELEMENT_UPDATE_URL = "UpdateUrl";

  private static final String ELEMENT_ENABLE_UPDATE = "EnableUpdate";

  private static final String ELEMENT_ENABLE_FILTER = "EnableFilter";

  private static final String ELEMENT_VIEW = "View";

  private static final String ELEMENT_PHASE = "Phase";

  private static final String ELEMENT_COLUMN_ENTRY = "ColumnEntry";

  private static final String ATTRIBUTE_DEFAULT = "default";

  private static final String ATTRIBUTE_NAME = "name";

  private static final String ATTRIBUTE_WIDTH = "width";

  private static final String ATTRIBUTE_RESIZABLE = "resizable";

  private static final String ATTRIBUTE_ENABLE = "enable";

  /** Prevent instantiation. */
  private StaxPreferenceXmlUtil() {
    // do nothing
  }

  /**
   * Parses a preference XML file and creates a <code>Preference</code> object.
   * 
   * @param reader The reader used to read in the file.
   * @return Returns a <code>Preference</code> representing the data in the file.
   * @throws XMLStreamException Thrown if there is an error reading the file.
   */
  public static Preference parsePreferenceFile(XMLStreamReader reader)
      throws XMLStreamException {
    Preference preference = null;

    int eventType = reader.getEventType();
    while (reader.hasNext()) {
      eventType = reader.next();

      if (eventType == XMLStreamConstants.START_ELEMENT) {
        QName elementQName = reader.getName();
        String elementName = elementQName.toString();

        if (ELEMENT_PREFERENCE.equals(elementName)) {
          preference = new Preference();
        }
        else if (ELEMENT_GENERAL.equals(elementName)) {
          StaxPreferenceXmlUtil.parseGeneral(reader, preference);
        }
        else if (ELEMENT_VIEW.equals(elementName)) {
          StaxPreferenceXmlUtil.parseView(reader, preference);
        }
      }
    }

    return preference;
  }

  /**
   * Parses the general sections of the preference XML. This method assumes that the cursor has
   * read in the start element event for the general element and has not gone any further.
   * 
   * @param reader The reader to read from.
   * @param preference The preference object to populate with the general information.
   * @throws XMLStreamException Thrown if there is an error reading from the reader.
   */
  private static void parseGeneral(XMLStreamReader reader, Preference preference)
      throws XMLStreamException {
    General general = new General();

    boolean endFound = false;
    while (!endFound) {
      if (reader.hasNext()) {
        int eventType = reader.next();

        if (eventType == XMLStreamConstants.START_ELEMENT) {
          QName elementQName = reader.getName();
          String elementName = elementQName.toString();
          if (ELEMENT_UPDATE_URL.equals(elementName)) {
            eventType = reader.next(); // we need the CHARACTERS event
            if (eventType == XMLStreamConstants.CHARACTERS) {
              String updateUrl = reader.getText();
              general.setUpdateUrl(updateUrl);
            }
          }
          else if (ELEMENT_ENABLE_UPDATE.equals(elementName)) {
            eventType = reader.next(); // we need the CHARACTERS event
            if (eventType == XMLStreamConstants.CHARACTERS) {
              String enableUpdate = reader.getText();
              general.setEnableUpdate(Boolean.valueOf(enableUpdate));
            }
          }
          else if (ELEMENT_ENABLE_FILTER.equals(elementName)) {
            eventType = reader.next(); // we need the CHARACTERS event
            if (eventType == XMLStreamConstants.CHARACTERS) {
              String enableFilter = reader.getText();
              general.setEnableFilter(Boolean.valueOf(enableFilter));
            }
          }
        }
        else if (eventType == XMLStreamConstants.END_ELEMENT) {
          if (ELEMENT_GENERAL.equals(reader.getName().toString())) {
            // this is the end of the General element
            endFound = true;
          }
        }
      }
    }

    preference.setGeneral(general);
  }

  /**
   * Parses the View section of the preference XML file. This method assumes that the reader
   * has read in the view start element event and has not proceeded past that point.
   * 
   * @param reader The reader to read data from.
   * @param preference The preference object that the data is to be added to.
   * @throws XMLStreamException Thrown if there is an error reading from the reader.
   */
  private static void parseView(XMLStreamReader reader, Preference preference)
      throws XMLStreamException {
    View view = new View();
    view.setDefault(reader.getAttributeValue(null, ATTRIBUTE_DEFAULT));

    boolean endFound = false;
    while (!endFound) {
      if (reader.hasNext()) {
        int eventType = reader.next();

        if (eventType == XMLStreamConstants.START_ELEMENT) {
          QName elementQName = reader.getName();
          String elementName = elementQName.toString();
          if (ELEMENT_PHASE.equals(elementName)) {
            StaxPreferenceXmlUtil.parsePhase(reader, view);
          }
        }
        else if (eventType == XMLStreamConstants.END_ELEMENT) {
          if (ELEMENT_VIEW.equals(reader.getName().toString())) {
            // this is the end of the View element
            endFound = true;
          }
        }
      }
    }

    preference.setView(view);
  }

  /**
   * Parses a Phase section of the preference XML file. This method assumes that the reader has
   * read in the Phase start element event and has not proceeded past that point.
   * 
   * @param reader The reader to read data from.
   * @param preference The preference object that the data is to be added to.
   * @throws XMLStreamException Thrown if there is an error reading from the reader.
   */
  private static void parsePhase(XMLStreamReader reader, View view) throws XMLStreamException {
    Phase phase = new Phase();
    phase.setName(reader.getAttributeValue(null, ATTRIBUTE_NAME));

    boolean endFound = false;
    while (!endFound) {
      if (reader.hasNext()) {
        int eventType = reader.next();

        if (eventType == XMLStreamConstants.START_ELEMENT) {
          QName elementQName = reader.getName();
          String elementName = elementQName.toString();
          if (ELEMENT_COLUMN_ENTRY.equals(elementName)) {
            ColumnEntry colEntry = new ColumnEntry();
            colEntry.setName(reader.getAttributeValue(null, ATTRIBUTE_NAME));
            String width = reader.getAttributeValue(null, ATTRIBUTE_WIDTH);
            
            try {
              colEntry.setWidth(Integer.valueOf(width));
            }
            catch (NumberFormatException e) {
              // just don't set the width
            }
            
            colEntry.setResizable(Boolean.valueOf(reader.getAttributeValue(null,
                ATTRIBUTE_RESIZABLE)));
            colEntry.setEnable(Boolean.valueOf(reader
                .getAttributeValue(null, ATTRIBUTE_ENABLE)));
            
            phase.getColumnEntry().add(colEntry);
          }
        }
        else if (eventType == XMLStreamConstants.END_ELEMENT) {
          if (ELEMENT_PHASE.equals(reader.getName().toString())) {
            // this is the end of the Phase element
            endFound = true;
          }
        }
      }
    }

    view.getPhase().add(phase);
  }

  /**
   * Writes the preference to the <code>XMLStreamWriter</code>.
   * 
   * @param writer The writer to write to.
   * @param preference The preference to save.
   * @throws XMLStreamException Thrown if there is an error writing to the writer.
   */
  public static void writePreference(XMLStreamWriter writer, Preference preference)
      throws XMLStreamException {
    writer.writeStartElement(ELEMENT_PREFERENCE);
    
    writer.writeStartElement(ELEMENT_GENERAL);
    General general = preference.getGeneral();
    
    writer.writeStartElement(ELEMENT_UPDATE_URL);
    writer.writeCharacters(general.getUpdateUrl());
    writer.writeEndElement(); // UpdateUrl
    
    writer.writeStartElement(ELEMENT_ENABLE_UPDATE);
    writer.writeCharacters(String.valueOf(general.isEnableUpdate()));
    writer.writeEndElement(); // EnableUpdate

    writer.writeStartElement(ELEMENT_ENABLE_FILTER);
    writer.writeCharacters(String.valueOf(general.isEnableFilter()));
    writer.writeEndElement(); // EnableFilter
    
    writer.writeEndElement(); // General
    
    writer.writeStartElement(ELEMENT_VIEW);
    View view = preference.getView();
    writer.writeAttribute(ATTRIBUTE_DEFAULT, view.getDefault());
    
    List<Phase> phases = view.getPhase();
    for (Phase phase : phases) {
      List<ColumnEntry> columnEntries = phase.getColumnEntry();
      writer.writeStartElement(ELEMENT_PHASE);
      writer.writeAttribute(ATTRIBUTE_NAME, phase.getName());
      
      for (ColumnEntry columnEntry : columnEntries) {
        writer.writeEmptyElement(ELEMENT_COLUMN_ENTRY);
        writer.writeAttribute(ATTRIBUTE_NAME, columnEntry.getName());
        writer.writeAttribute(ATTRIBUTE_WIDTH, String.valueOf(columnEntry.getWidth()));
        writer.writeAttribute(ATTRIBUTE_RESIZABLE, String.valueOf(columnEntry.isResizable()));
        writer.writeAttribute(ATTRIBUTE_ENABLE, String.valueOf(columnEntry.isEnable()));
      }
      
      writer.writeEndElement(); // Phase
    }
    
    writer.writeEndElement(); // View
    
    writer.writeEndElement(); // Preference
  }
}
