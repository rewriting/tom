package fr.irisa.cairn.xtext.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.xtend.expression.AbstractExpressionsUsingWorkflowComponent;

/**
 * Abstract class for a MWE2 workflow component that will be executed in an
 * Eclipse runtime environement.
 * 
 * @author antoine
 * 
 */
public abstract class RuntimeWorkflowComponent extends
		AbstractExpressionsUsingWorkflowComponent {
	private ResourceSet resourceSet = new ResourceSetImpl();;
	protected IProject project;

	public RuntimeWorkflowComponent(IProject project) {
		super();
		this.project = project;
	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {

	}

	protected URI getURI(IFile file) {
		String path = file.getFullPath().toString();
		return URI.createFileURI(path);
	}

	/**
	 * Get URI from a relative path file
	 * 
	 * @param file
	 * @return
	 */
	protected URI getURI(String file) {

		String path = project.getLocation().toString() + "/" + file;
		return URI.createFileURI(path);
	}

	public ResourceSet getResourceSet() {
		return resourceSet;
	}

}
