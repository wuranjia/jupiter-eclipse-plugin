package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;

/**
 * Provides the order (priority) of an item. The main purpose is to search the index of a certain
 * keys type of the item.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public abstract class KeyManager {
  /** The keys for the localized value in the item. */
  protected List ordinalKeys;
  /** The key-value map. */
  protected Map keyValues;
  /** The key-localizedLabel map. */
  protected Map keyLocalizedLabels;
  /** The localizedLabel-key map. */
  protected Map localizedLabelKeys;

  /**
   * Instantiates this object.
   */
  protected KeyManager() {
    this.ordinalKeys = new ArrayList();
    this.keyValues = new HashMap();
    this.keyLocalizedLabels = new HashMap();
    this.localizedLabelKeys = new HashMap();
  }

  /**
   * Adds the key and its value of an item. Ignore the second keys if the keys already exist in the
   * manager (i.e No duplication for the keys). Sets empty string if key or/and its value is null.
   *
   * @param key the keys to add in the manager.
   * @param value the value associated with the key.
   */
  public void add(String key, String value) {
    if (!ordinalKeys.contains(key)) {
      this.ordinalKeys.add(key);
      this.keyLocalizedLabels.put(key, ReviewI18n.getString(key));
      this.keyValues.put((key != null) ? key : "", (value != null) ? value : "");
      this.localizedLabelKeys.put(ReviewI18n.getString(key), key);
    }
  }

  /**
   * Gets the ordinal (index) for the keys. Returns the integer max number if the keys does not
   * exist.
   *
   * @param key the keys to search a type index.
   * @return the ordinal for the keys. Returns the <code>Integer.MAX_VALUE</code> if the keys does
   *         not exist.
   */
  public int getOrdinal(String key) {
    int index = ordinalKeys.indexOf(key);
    return (index != -1) ? index : Integer.MAX_VALUE;
  }

  /**
   * Gets the value corresponding to the key.
   *
   * @param key the key to get the value.
   * @return the value for the key. Returns the key if the value corresponding to the key is not
   *         found.
   */
  public String getValue(String key) {
    Object value = this.keyValues.get(key);
    if (value != null) {
      return (String) value;
    }
    return key;
  }

  /**
   * Gets the localized label from the key.
   *
   * @param key the key to get the localized label.
   * @return the localized label from the key. Returns the key if the corresponding label is not
   *         found.
   */
  public String getLocalizedLabel(String key) {
    Object object = this.keyLocalizedLabels.get(key);
    if (object != null) {
      return (String) object;
    }
    return key;
  }

  /**
   * Gets the key from the localized label.
   *
   * @param localizedLabel the localized label to get the corresponding key.
   * @return the key from the localized label. Returns the localized label if the corresponding key
   *         is not found.
   */
  public String getKey(String localizedLabel) {
    Object object = this.localizedLabelKeys.get(localizedLabel);
    if (object != null) {
      return (String) object;
    }
    return localizedLabel;
  }

  /**
   * Gets the item object. Sub class should overwrite this method and return type should be the sub
   * class casted type.
   *
   * @param key the key to get an object.
   * @return the sub class casted type.
   */
  public abstract Object getItemObject(String key);

  /**
   * Gets the item object in the index. Subclass should overwrite this method and return type
   * should be the sub class casted type.
   *
   * @param index the index of the item object.
   * @return the item object in the index.
   */
  public abstract Object getItem(int index);

  /**
   * Returns the size of the manager.
   *
   * @return the size of the manager.
   */
  public int size() {
    return this.ordinalKeys.size();
  }

  /**
   * Returns the array of localized label string with the key order.
   *
   * @return the array of localized label string.
   */
  public String[] getElements() {
    String[] localizedLabels = new String[this.ordinalKeys.size()];
    for (int i = 0; i < this.ordinalKeys.size(); i++) {
      String key = (String) this.ordinalKeys.get(i);
      localizedLabels[i] = (String) this.keyLocalizedLabels.get(key);
    }
    return localizedLabels;
  }
  
  /**
   * Clears all internal data structures.
   */
  public void clear() {
    this.ordinalKeys.clear();
    this.keyValues.clear();
    this.keyLocalizedLabels.clear();
    this.localizedLabelKeys.clear();
  }
}
