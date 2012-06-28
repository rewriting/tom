package fr.irisa.cairn.xtext.utils;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.WorkflowInterruptedException;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;

public class GenmodelGenerator extends RuntimeWorkflowComponent {
	private URI ecore;

	private GenmodelBasicGenerator basicGenerator;

	public GenmodelGenerator(IProject project, String modelPath,
			String pluginID, String base) {
		super(project);
		this.basicGenerator = new GenmodelBasicGenerator(modelPath, pluginID, base);
	}

	/**
	 * Add a referenced genmodel URI to the generated genmodel
	 * 
	 * @param p
	 */
	public void addGenPackage(String uri) {
		this.basicGenerator.addGenPackage(uri);
	}

	public URI getEcore() {
		return this.basicGenerator.getEcore();
	}

	public void setEcore(URI ecore) {
		this.basicGenerator.setEcore(ecore);
	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		monitor.beginTask("Generating Genmodel from ecore",
				ProgressMonitor.UNKNOWN);
		// Get the resource

		Resource resource = getResourceSet().getResource(ecore, true);
		EPackage p = (EPackage) resource.getContents().get(0);
		try {
			basicGenerator.createGenPackage(p);
		} catch (IOException e) {
			throw new WorkflowInterruptedException(e);
		}

	}

}
