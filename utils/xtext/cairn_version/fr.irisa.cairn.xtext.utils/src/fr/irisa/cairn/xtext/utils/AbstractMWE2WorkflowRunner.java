package fr.irisa.cairn.xtext.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.mwe2.runtime.workflow.Workflow;
import org.eclipse.emf.mwe2.runtime.workflow.WorkflowContextImpl;
import org.eclipse.xpand2.Generator;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.mwe.Reader;
import org.eclipse.xtext.mwe.SlotEntry;
import org.eclipse.xtext.mwe.UriBasedReader;

/**
 * A simple MWE2 workflow launchable in an Eclipse runtime environment. A
 * Default XPand generator and an URI reader are included.
 * 
 * @author antoine
 */
public abstract class AbstractMWE2WorkflowRunner {

	protected String SRC_GEN = "src-gen";
	protected String fileEncoding = "UTF-8";
	protected IFile target;
	protected IProgressMonitor monitor;
	private String gen = SRC_GEN;
	protected IFolder folder;

	protected abstract Workflow buildWorkflow();

	protected String getGeneratedPath() {

		return target.getProject().getLocation().toString() + "/"
				+ getGenRoot() + "/";
	}

	protected String getGenRoot() {

		return gen;

	}

	/**
	 * Get default model path reader component the reader will read all files in
	 * the modelPath folder
	 * 
	 * @param slotName
	 * @param slotType
	 * @param modelPath
	 *            source folder for the reader
	 * @return the reader
	 */
	public Reader getModelPathReader(final String slotName,
			final String slotType, final String modelPath) {

		final Reader reader = new Reader();
		// reader.addPath(this.target.getFullPath().toOSString());
		final SlotEntry entry = new SlotEntry();
		entry.setSlot(slotName);
		entry.setType(slotType);
		// entry.setNsURI(nsURI);
		reader.addPath(modelPath);
		reader.addLoad(entry);
		reader.addRegister(getXtextRegister());
		return reader;
	}

	/**
	 * Get default URIbased reader component
	 * 
	 * @param slotName
	 * @param slotType
	 * @param nsURI
	 * @return
	 */
	public UriBasedReader getReader(String slotName, String slotType,
			String nsURI) {

		final UriBasedReader reader = new UriBasedReader();
		reader.addUri(target.getFullPath().toString());
		final SlotEntry entry = new SlotEntry();
		entry.setSlot(slotName);
		entry.setType(slotType);
		entry.setNsURI(nsURI);
		entry.setFirstOnly(true);
		reader.addLoad(entry);
		reader.addRegister(getXtextRegister());
		return reader;
	}

	protected URI getTargetURI() {

		return URI.createURI(target.getFullPath().toString());
	}

	/**
	 * Get default XPand generator
	 * 
	 * @param rule
	 * @return
	 */
	public Generator getXpandGenerator(String rule) {

		final Generator generator = new Generator();
		generator.setExpand(rule);
		final Outlet outlet = new Outlet();
		outlet.setOverwrite(true);
		outlet.setPath(getGeneratedPath());
		generator.addOutlet(outlet);
		generator.setFileEncoding(fileEncoding);
		return generator;
	}

	/**
	 * Get Setup for target dsl.
	 * 
	 * @return
	 */
	protected abstract ISetup getXtextRegister();

	public void run(IFile file, IProgressMonitor monitor) {

		target = file;
		this.monitor = monitor;
		final Workflow workflow = buildWorkflow();
		workflow.run(new WorkflowContextImpl());
		try {
			this.target.getProject().refreshLocal(IResource.DEPTH_INFINITE,
					monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * This method launch the workflow the reader read a folder and not a file.
	 * the generated file is placed in the source folder
	 * 
	 * @param folder
	 *            source folder
	 * @param monitor
	 */
	public void runFolder(final IFolder folder, final IProgressMonitor monitor) {

		// logger.info("Generate the graph adapter...");
		// this.target = file;
		this.folder = folder;
		this.monitor = monitor;
		final Workflow workflow = buildWorkflow();
		workflow.run(new WorkflowContextImpl());
		try {
			this.target.getProject().refreshLocal(IResource.DEPTH_INFINITE,
					monitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// logger.info("Done");
	}

	protected void setGenRoot(String p) {

		gen = p;
	}

	protected IProject getProject(final String projectPath) {
		final IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace()
				.getRoot();
		return workspace.getProject(projectPath);
	}

}
