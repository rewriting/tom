package fr.irisa.cairn.xtext.utils.plugin;

import org.eclipse.xtext.ui.util.PluginProjectFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class PluginProjectFactoryProvider implements Provider<PluginProjectFactory>{
	
	private final PluginProjectFactory factory;

	@Inject
	public PluginProjectFactoryProvider(PluginProjectFactory factory) {
		super();
		this.factory = factory;
	}

	public PluginProjectFactory get() {
		return factory;
	}
	
	

}
