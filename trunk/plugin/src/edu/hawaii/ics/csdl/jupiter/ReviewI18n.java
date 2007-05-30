package edu.hawaii.ics.csdl.jupiter;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provides resources from resource bundle.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewI18n {
  /** the resource bundle file name */
  private static final String RESOURCE_BUNDLE = "jupiter";
  /** the resource bundle instance. */
  private static ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);
  /** the value-key map.*/
  private static Map valueKeyMap;
  
  static {
    createValueKeyMap();
  }

  /**
   * Prevents clients from instantiating this class.
   */
  private ReviewI18n() {
  }
  
  /**
   * Creates value-key map. Used to get key from value.
   */
  private static void createValueKeyMap() {
    valueKeyMap = new HashMap();
    for (Enumeration keys = bundle.getKeys(); keys.hasMoreElements();) {
      String key = (String) keys.nextElement();
      String value = getString(key);
      valueKeyMap.put(value, key);
    }
  }

  /**
   * Returns the formatted message for the given key in the resource bundle.
   *
   * @param key the resource name
   * @param args the message arguments
   *
   * @return the string
   */
  public static String format(String key, Object[] args) {
    return MessageFormat.format(getString(key), args);
  }

  /**
   * Returns the string from the plugin's resource bundle,
   * or 'key' if not found.
   * @param key the key to search value in the resource bundle.
   * @return the string from the plugin's resource bundle,
   * or 'key' if not found.
   */
  public static String getString(String key) {
    try {
      return bundle.getString(key);
    } 
    catch (MissingResourceException e) {
      return key;
    }
  }
  
  /**
   * Gets the key from the value, or 'value' if not found.
   * @param value the value to search value.
   * @return the key from the value, or 'value' if not found.
   */
  public static String getKey(String value) {
    String key = (String) valueKeyMap.get(value);
    return (key != null) ? key : value;
  }
  
  /**
   * Sets locale for the resource bundle.
   * @param locale the locale.
   */
  public void setLocale(Locale locale) {
    bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
    createValueKeyMap();
  }
}
