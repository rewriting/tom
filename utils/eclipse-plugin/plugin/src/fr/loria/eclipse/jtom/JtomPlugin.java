/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 * Julien Guyon						e-mail: Julien.guyon@loria.fr
 * 
 **/

package fr.loria.eclipse.jtom;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import fr.loria.eclipse.gom.editor.GomCodeScanner;
import fr.loria.eclipse.gom.editor.GomPartitionScanner;
import fr.loria.eclipse.gom.editor.util.GomColorManager;
import fr.loria.eclipse.jtom.editor.TomEditorPartitionScanner;
import fr.loria.eclipse.jtom.editor.template.TomContextType;

/**
 * The main plugin class to be used in the desktop.
 */
public class JtomPlugin extends AbstractUIPlugin {
	  // The shared singleton instance.
	private static JtomPlugin plugin;
		// Resource bundle.
	private ResourceBundle resourceBundle;
		// Tom Document partition scanner
	private TomEditorPartitionScanner tomScanner;
		
	/** The template store. */
	private TemplateStore fStore;
	/** The context type registry. */
	private ContextTypeRegistry fRegistry;
	private GomPartitionScanner gomPartitionScanner;
	private GomColorManager gomColorManager;

	
	/** Key to store custom templates. */
	private static final String CUSTOM_TEMPLATES_KEY= "fr.loria.eclipse.jtom.editor.templates";

	private final static String RESOURCE_BUNDLE = "fr.loria.eclipse.jtom.JtomPluginResources";
	
	public final static String TOM_PARTITIONING= "__tom_partitioning";
	public final static String GOM_PARTITIONING= "__gom_partitioning";
 	
	public static final String HIGHLIGHT_PREFERENCE = "highlight";
	public static final String TOM_EXTENSION_PREFERENCE = "file_extension";
	public static final String TOM_INCLUDE_PREFERENCE = "included_file_location";
	public static final String TOM_DEFAULT_COMMAND_PREFERENCE = "default_commandline";
	public static final String DISPLAY_JAVA_ERRORS_PREFERENCE ="error_management";
	
	public static final String USE_CUSTOM_COMMAND_PROPERTY = "USE_CUSTOM_COMMAND";
	public static final String USE_GENERATED_COMMAND_PROPERTY = "USE_GENERATED_COMMAND";
	public static final String CUSTOM_COMMAND_PROPERTY = "CUSTOM_COMMAND";
	public static final String GENERATED_COMMAND_PROPERTY = "GENERATED_COMMAND";
	
	public static final String MARKER_ID = "fr.loria.eclipse.tom.tomFailureMarker";
	
	/**
	 * The constructors.
	 */
	
	public JtomPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle   = ResourceBundle.getBundle(RESOURCE_BUNDLE);
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

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}
		
	public static Shell getActiveWorkbenchShell() {
	 IWorkbenchWindow window= getActiveWorkbenchWindow();
	 if (window != null) {
	 	return window.getShell();
	 }
	 return null;
	}
		
	public static IWorkbenchPage getActivePage() {
		return getDefault().internalGetActivePage();
	}
	
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}
	
	private IWorkbenchPage internalGetActivePage() {
		IWorkbenchWindow window= getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
	
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
		
	public static void logErrorMessage(String message) {
		log(new Status(IStatus.ERROR, getPluginId(), 1001/*IJtomStatusConstants.INTERNAL_ERROR*/, message, null));
	}

	public static void logErrorStatus(String message, IStatus status) {
		if (status == null) {
			logErrorMessage(message);
			return;
		}
		Status multi= new Status(IStatus.ERROR, getPluginId(), 1001/*IJtomStatusConstants.INTERNAL_ERROR*/, message, null);
		log(multi);
	}
		
	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, getPluginId(), 1001/*IJtomStatusConstants.INTERNAL_ERROR*/, JtomPlugin.getResourceString("JtomPlugin.internal_error"), e)); //$NON-NLS-1$
	}
		
	public static void log(String message) {
			log(new Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, message, null));
		}
	
	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = JtomPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return "!"+key+"!";
		}
	}

	/**
	 * Gets a string from the resource bundle and formats it with the argument
	 * 
	 * @param key	the string used to get the bundle value, must not be null
	 */
	public static String getFormattedString(String key, Object arg) {
		return MessageFormat.format(getResourceString(key), new Object[] { arg });
	}


	/**
	 * Gets a string from the resource bundle and formats it with arguments
	 */	
	public static String getFormattedString(String key, Object[] args) {
		return MessageFormat.format(getResourceString(key), args);
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
	protected void initializeDefaultPluginPreferences() {
		Preferences preferences = getPluginPreferences();
		preferences.setDefault(HIGHLIGHT_PREFERENCE, StringConverter.asString(new RGB(0, 200, 125)));
		preferences.setDefault(TOM_EXTENSION_PREFERENCE, "t");
		preferences.setDefault(TOM_INCLUDE_PREFERENCE, "");
		preferences.setDefault(TOM_DEFAULT_COMMAND_PREFERENCE, "");
		preferences.setDefault(DISPLAY_JAVA_ERRORS_PREFERENCE, true);
	}
	
	/* 
	 * Method declared in Plugin
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		  // to avoid people complain nothing happens
		  // set auto build mode on ... (is it really a good idea??)
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceDescription description = workspace.getDescription();
		if(!description.isAutoBuilding()) {
			description.setAutoBuilding(true);
			workspace.setDescription(description);
		}
		//setDebugging(true);
	}
	

	
	public ImageDescriptor getImageDescriptor(String name) {
		URL url= getDefault().getBundle().getEntry(name);
		return ImageDescriptor.createFromURL(url);
	}
	
	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Return a scanner for creating tom partitions.
	 */
	public synchronized TomEditorPartitionScanner getTomPartitionScanner() {
		if (tomScanner == null) {
			tomScanner= new TomEditorPartitionScanner();
		}
		return tomScanner;
	}
	
	
	
	public synchronized GomPartitionScanner getGomPartitionScanner() {
		if (gomPartitionScanner == null) {
			gomPartitionScanner= new GomPartitionScanner();
		}
		return gomPartitionScanner;
	}
	
	/**
	 * Returns this plug-in's template store.
	 * 
	 * @return the template store of this plug-in instance
	 */
	public TemplateStore getTemplateStore() {
		if (fStore == null) {
			fStore= new ContributionTemplateStore(getContextTypeRegistry(), JtomPlugin.getDefault().getPreferenceStore(), CUSTOM_TEMPLATES_KEY);
			try {
				fStore.load();
			} catch (IOException e) {
				getDefault().getLog().log(new Status(IStatus.ERROR, "fr.loria.eclipse.jtom.editor", IStatus.OK, "", e));
			}
		}
		return fStore;
	}

	/**
	 * Returns this plug-in's context type registry.
	 * 
	 * @return the context type registry for this plug-in instance
	 */
	public ContextTypeRegistry getContextTypeRegistry() {
		if (fRegistry == null) {
			// create an configure the contexts available in the template editor
			fRegistry= new ContributionContextTypeRegistry();
			fRegistry.addContextType(new TemplateContextType(TomContextType.TOM_CONTEXT_TYPE));
		}
		return fRegistry;
	}

	public GomColorManager getGomColorManager() {
		return gomColorManager == null ? new GomColorManager() : gomColorManager; 		
	}

	
} // class JtomPlugin
