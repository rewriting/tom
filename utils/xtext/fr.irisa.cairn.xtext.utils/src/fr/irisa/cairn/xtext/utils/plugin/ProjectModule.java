package fr.irisa.cairn.xtext.utils.plugin;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.xtext.ui.wizard.IProjectCreator;

import com.google.inject.AbstractModule;

@SuppressWarnings("restriction")
public class ProjectModule extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub

	}
	
	protected void bindIProjectGenerator() {		
		bind(IProjectCreator.class).to(AbstractPluginGenerator.class);
	}
	
	protected void bindIWorkspace() {		
		bind(IWorkspace.class).to(Workspace.class);
	}
	
	protected void bindIWorkbench() {		
		bind(IWorkbench.class).to(Workbench.class);
	}
	
//	protected void providePojectFactory()  {
//		bind(AbstractPluginGenerator.class).toProvider(PluginProjectFactoryProvider.class);
//	}
	

}
