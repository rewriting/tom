package fr.irisa.cairn.xtext.utils.plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.xtext.ui.util.PluginProjectFactory;
import org.eclipse.xtext.ui.util.ProjectFactory;
import org.eclipse.xtext.ui.wizard.AbstractProjectCreator;

import com.google.common.collect.Lists;

public abstract class AbstractPluginGenerator extends AbstractProjectCreator {

	private  PluginProjectFactoryProvider	projectFactoryProvider;

	@Override
	protected ProjectFactory configureProjectFactory(final ProjectFactory factory) {
		final PluginProjectFactory result = (PluginProjectFactory) super
				.configureProjectFactory(factory);
//		this.projectFactoryProvider = new PluginProjectFactoryProvider(result);
		result.addRequiredBundles(this.getRequiredBundles());
		result.addExportedPackages(this.getExportedPackages());
		result.addImportedPackages(this.getImportedPackages());
		result.setActivatorClassName(this.getActivatorClassName());

		return result;
	}

	@Override
	protected PluginProjectFactory createProjectFactory() {
		this.projectFactoryProvider = new PluginProjectFactoryProvider(new CairnPluginProjectFactory());; 
		return this.projectFactoryProvider.get();
	}

	/**
	 * @return the class-name of the bundle-activator or <code>null</code>
	 */
	protected String getActivatorClassName() {
		return null;
	}

	/**
	 * @return the names of the exported packages. May not be <code>null</code>
	 */
	protected List<String> getExportedPackages() {
		return Collections.emptyList();
	}

	/**
	 * @return the names of the imported packages that a new project requires.
	 *         May not be <code>null</code>
	 */
	protected List<String> getImportedPackages() {
		return Lists.newArrayList();
	}

	/**
	 * @return the names of the bundles that a new project requires. May not be
	 *         <code>null</code>
	 */
	protected List<String> getRequiredBundles() {
		return Lists.newArrayList();
	}
	
	@Override
	protected void execute(final IProgressMonitor monitor)
			throws CoreException, InvocationTargetException, InterruptedException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 
				getCreateModelProjectMessage(), 
				2);
		try {
			final IProject project = createProject(subMonitor.newChild(1));
			if (project == null)
				return;
			enhanceProject(project, subMonitor.newChild(1));
//			IFile modelFile = getModelFile(project);
//			setResult(modelFile);
		} finally {
			subMonitor.done();
		}
	}

}
