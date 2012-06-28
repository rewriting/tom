package fr.irisa.cairn.xtext.utils.plugin;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.ui.util.PluginProjectFactory;

public class CairnPluginProjectFactory extends PluginProjectFactory {
	
	public CairnPluginProjectFactory() {
		this.workspace = ResourcesPlugin.getWorkspace();
		this.workbench = PlatformUI.getWorkbench();
	}

}
