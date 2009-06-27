package edu.hawaii.ics.csdl.jupiter.ui.preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.hawaii.ics.csdl.jupiter.ReviewI18n;
import edu.hawaii.ics.csdl.jupiter.ReviewPlugin;

/**
 * Provides a root preference page for the Jupiter review system. Enable user to configure some
 * features such as default reviewer name, update manager URL, etc.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class GeneralPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
  /** The preference store to hold the existing preference values. */
  private IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
  /** The field editor list to hold the field editor. */
  private List<FieldEditor> fieldEditors = new ArrayList<FieldEditor>();
  /** The update URL store key. */
  public static final String UPDATE_URL_KEY = "UpdateUrl";
  /** The enable update store key. */
  public static final String ENABLE_UPDATE_KEY = "EnableUpdate";

  /**
   * Initializes this preference page for the given workbench. Loads the properties file setting to
   * the preference store if the file exists.
   * 
   * <p>
   * This method is called automatically as the preference page is being created and initialized.
   * Clients must not call this method.
   * </p>
   *
   * @param workbench the workbench
   */
  public void init(IWorkbench workbench) {
  }

  /**
   * Creates the root preference page of the jupiter configuration.
   *
   * @param parent the parent composite
   *
   * @return the new control
   */
  protected Control createContents(Composite parent) {
    Composite top = new Composite(parent, SWT.LEFT);
    top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    top.setLayout(new GridLayout());
    // Creates review preference group.
    createGeneralPreferenceGroup(top);
    // Loads the preference store.
    loadFieldEditors(false);
    return top;
  }

  /**
   * Creates preview preference group, which contains default reviewer's name.
   * generalPreferenceGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
   *
   * @param parent the parent composite.
   */
  private void createGeneralPreferenceGroup(Composite parent) {
    final Group generalPreferenceGroup = new Group(parent, SWT.NONE);
    String generalLabelPreferenceKey = "GeneralPreferencePage.general.label.preference";
    generalPreferenceGroup.setText(ReviewI18n.getString(generalLabelPreferenceKey));
    generalPreferenceGroup.setLayout(new GridLayout());
    generalPreferenceGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    // Creates text field sub group.
    final Composite textFieldSubGroup = new Composite(generalPreferenceGroup, SWT.NONE);
    textFieldSubGroup.setLayout(new GridLayout());
    GridData fieldData = new GridData(GridData.FILL_HORIZONTAL);
    fieldData.horizontalIndent = 2;
    textFieldSubGroup.setLayoutData(fieldData);
    String updateUrlKey = GeneralPreferencePage.UPDATE_URL_KEY;
    String generalLabelUpdateUrlKey = "GeneralPreferencePage.general.label.updateUrl";
    final StringFieldEditor updateUrlField = new StringFieldEditor(updateUrlKey,
        ReviewI18n.getString(generalLabelUpdateUrlKey), textFieldSubGroup);

    // Creates sub enable update group.
    Composite enableUpdateGroup = new Composite(generalPreferenceGroup, SWT.NONE);
    enableUpdateGroup.setLayout(new GridLayout());
    GridData enableData = new GridData(GridData.FILL_HORIZONTAL);
    enableData.horizontalIndent = 2;
    enableUpdateGroup.setLayoutData(enableData);
    String enableUpdateKey = GeneralPreferencePage.ENABLE_UPDATE_KEY;
    String generalLabelEnableAutoUpdateKey = "GeneralPreferencePage.general.label.enableAutoUpdate";
    final BooleanFieldEditor enableUpdateField = new BooleanFieldEditor(enableUpdateKey,
        ReviewI18n.getString(generalLabelEnableAutoUpdateKey), enableUpdateGroup);
    enableUpdateField.setPropertyChangeListener(new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
          updateUrlField.setEnabled(enableUpdateField.getBooleanValue(), textFieldSubGroup);
        }
      });

    // Initializes the field status.
    String enableUpdateStoreKey = GeneralPreferencePage.ENABLE_UPDATE_KEY;
    updateUrlField.setEnabled(this.store.getBoolean(enableUpdateStoreKey), textFieldSubGroup);
    // Collects all the FieldEditor instance to the List in order to load the setting in the
    // preference store.
    this.fieldEditors.addAll(Arrays.asList(
        new FieldEditor[] {updateUrlField, enableUpdateField}));
  }

  /**
   * Stores all <code>FieldEditor</code> instances.
   */
  private void storeFieldEditors() {
    for (Iterator<FieldEditor> i = this.fieldEditors.iterator(); i.hasNext();) {
      FieldEditor fieldEditor = (FieldEditor) i.next();
      if (fieldEditor != null) {
        fieldEditor.setPreferenceStore(this.store);
        fieldEditor.store();
      }
    }
  }

  /**
   * Restores all field editor's values.
   *
   * @param isDefautLoaded <code>true</code> if default field editors are loaded.
   *        <code>false</code> otherwise.
   */
  private void loadFieldEditors(boolean isDefautLoaded) {
    for (Iterator<FieldEditor> i = this.fieldEditors.iterator(); i.hasNext();) {
      FieldEditor fieldEditor = (FieldEditor) i.next();
      if (fieldEditor != null) {
        fieldEditor.setPreferenceStore(this.store);
        if (isDefautLoaded) {
          fieldEditor.loadDefault();
        }
        else {
          fieldEditor.load();
        }
      }
    }
  }

  /**
   * Performs to save the current data in the editors.
   *
   * @return <code>false</code> to abort the container's OK processing and <code>true</code> to
   *         allow the OK to happen.
   */
  public boolean performOk() {
    storeFieldEditors();
    return super.performOk();
  }

  /**
   * Performs special processing when this page's Apply button has been pressed. The default
   * implementation of this framework method simply calls performOk to simulate the pressing of
   * the page's OK button.
   */
  public void performApply() {
    performOk();
  }

  /**
   * Performs reset of all field values to the default specified on the
   * <code>csdl.jupiter.ReviewPlugin.initializeDefaultPreferences(IPreferenceStore)</code>
   * method.
   */
  public void performDefaults() {
    loadFieldEditors(true);
  }
}
