package fr.loria.eclipse.jtom.mappinggenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.DialogUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.newresource.ResourceMessages;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import fr.loria.eclipse.jtom.JtomPlugin;

public class MappingGeneratorWizard extends BasicNewResourceWizard {

	private WizardNewFileCreationPage mainPage;
	private boolean isFolder;

	/**
	 * Creates a wizard for creating a new file resource in the workspace.
	 */
	public MappingGeneratorWizard(boolean isFolder) {
		super();
		this.isFolder = isFolder;
		this.setNeedsProgressMonitor(true); 
	}

	/* (non-Javadoc)
	 * Method declared on IWizard.
	 */
	public void addPages() {
		super.addPages();
		mainPage = new WizardNewFileCreationPage("newFilePage1", getSelection());//$NON-NLS-1$
		mainPage.setTitle(JtomPlugin.getResourceString("MappingFileCreationWizard.title"));
		mainPage.setDescription(JtomPlugin.getResourceString("MappingFileCreationWizard.description"));
		mainPage.setFileExtension("tom");		
		addPage(mainPage);
	}

	/* (non-Javadoc)
	 * Method declared on IWorkbenchWizard.
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setWindowTitle(JtomPlugin.getResourceString("MappingFileCreationWizard.window.title"));
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * Method declared on BasicNewResourceWizard.
	 */
	protected void initializeDefaultPageImageDescriptor() {
		ImageDescriptor desc = IDEWorkbenchPlugin
		.getIDEImageDescriptor("wizban/newfile_wiz.png");//$NON-NLS-1$
		setDefaultPageImageDescriptor(desc);
	}

	/* (non-Javadoc)
	 * Method declared on IWizard.
	 */
	public boolean performFinish() {
		IFile file = mainPage.createNewFile();
		if (file == null) {
			return false;
		}
		// for debug purposes
		System.setErr(System.out);
				
		String logFilePath = file.getLocation().toOSString();		
		if (logFilePath.endsWith("tom")) {		
			logFilePath = logFilePath.substring(0, logFilePath.lastIndexOf('.')) + ".log";
		} else {
			logFilePath += ".log";
		}	
				
		String message = "Generation completed ! See the log file for more information.";
		FileOutputStream output = null;
		File fileOutput = null;
		try {
			fileOutput = new File(logFilePath);
			fileOutput.createNewFile();
			output = new FileOutputStream(fileOutput);
			System.setOut(new PrintStream(output));
			MappingGeneratorLauncher.launchGenerator(isFolder,file,selection);
		} catch (Exception e1) {
			message += "\n\n An error occured while creating log file:" + e1.getMessage();
		} finally {
			System.setOut(System.out);
			try {
				output.flush();
				output.close();
			} catch (Exception e) {} //nothing		
		}
		
		selectAndReveal(file);		
		
		// refresh in order ot see the log file
		try {
			file.getParent().refreshLocal(IResource.DEPTH_INFINITE,null);
		} catch (CoreException e1) {} // nothing

		// Open editor on new file.
		IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
		try {
			if (dw != null) {
				// show the log of the generation 
				MessageDialog.openInformation(dw.getShell(), "Generation status", message);
				IWorkbenchPage page = dw.getActivePage();
				if (page != null) {
					IDE.openEditor(page, file, true);
				}
			}
		} catch (PartInitException e) {
			DialogUtil.openError(dw.getShell(),
					ResourceMessages.FileResource_errorMessage, e.getMessage(),
					e);
		}		
		
		return true;
	}
}
