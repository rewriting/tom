package fr.loria.eclipse.jtom;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.preference.IPreferenceStore;

import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class JtomPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static JtomPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
	 */
	public JtomPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		if(plugin == null) {
			plugin = this;
		}
		try {
			resourceBundle= ResourceBundle.getBundle("com.loria.eclipse.jtom.JtomPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static JtomPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle= JtomPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	/** 
	 * Sets default preference values. These values will be used
	 * until some preferences are actually set using Preference dialog.
	 */
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		// These settings will show up when Preference dialog
		// opens up for the first time.
		store.setDefault("FileExtension", "t");
		store.setDefault("JarFile", "Default");
		store.setDefault("defaultCommandLine", "");
	}
}
