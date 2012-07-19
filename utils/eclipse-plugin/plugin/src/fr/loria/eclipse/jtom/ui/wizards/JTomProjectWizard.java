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

package fr.loria.eclipse.jtom.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.preferences.NewJavaProjectPreferencePage;
import org.eclipse.jdt.internal.ui.wizards.IStatusChangeListener;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathsBlock;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.help.WorkbenchHelp;

// Referenced classes of package org.eclipse.jdt.ui.wizards:
//						NewElementWizardPage

public class JTomProjectWizard extends NewElementWizardPage
{

    public JTomProjectWizard(IWorkspaceRoot root, WizardNewProjectCreationPage mainpage)
    {
        super("NewJavaProjectWizardPage");
        setTitle(NewWizardMessages.NewJavaProjectWizardPage_title);
        setDescription(NewWizardMessages.NewJavaProjectWizardPage_description);
        fMainPage = mainpage;
        IStatusChangeListener listener = new IStatusChangeListener()
        {

            public void statusChanged(IStatus status)
            {
                updateStatus(status);
            }

        };
        IRunnableContext runCtx = null;
        fBuildPathsBlock = new BuildPathsBlock(runCtx, listener, 0, true, null);
        fProjectModified = true;
        fOutputLocation = null;
        fClasspathEntries = null;
    }

    public void setDefaultOutputFolder(IPath path)
    {
        fOutputLocation = path;
        setProjectModified();
    }

    public void setDefaultClassPath(IClasspathEntry entries[], boolean appendDefaultJRE)
    {
        if (entries != null && appendDefaultJRE) {
            IClasspathEntry jreEntry[] = NewJavaProjectPreferencePage
                    .getDefaultJRELibrary();
            IClasspathEntry newEntries[] = new IClasspathEntry[entries.length
                    + jreEntry.length];
            System.arraycopy(entries, 0, newEntries, 0, entries.length);
            System.arraycopy(jreEntry, 0, newEntries, entries.length, jreEntry.length);
            entries = newEntries;
        }
        fClasspathEntries = entries;
        setProjectModified();
    }

    public void setProjectModified()
    {
        fProjectModified = true;
    }

    protected IProject getProjectHandle()
    {
        Assert.isNotNull(fMainPage);
        return fMainPage.getProjectHandle();
    }

    protected IPath getLocationPath()
    {
        Assert.isNotNull(fMainPage);
        return fMainPage.getLocationPath();
    }

    public IJavaProject getNewJavaProject()
    {
        return JavaCore.create(getProjectHandle());
    }

    public void createControl(Composite parent)
    {
        org.eclipse.swt.widgets.Control control = fBuildPathsBlock.createControl(parent);
        setControl(control);
        Dialog.applyDialogFont(control);
        WorkbenchHelp.setHelp(control,
                "org.eclipse.jdt.ui.new_javaproject_wizard_page_context");
    }

    protected void initBuildPaths()
    {
        fBuildPathsBlock.init(getNewJavaProject(), fOutputLocation, fClasspathEntries);
    }

    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        if (visible && (fProjectModified || isNewProjectHandle())) {
            initBuildPaths();
            fProjectModified = false;
        }
    }

    private boolean isNewProjectHandle()
    {
        IProject oldProject = fBuildPathsBlock.getJavaProject().getProject();
        return !oldProject.equals(getProjectHandle());
    }

    public IPath getOutputLocation()
    {
        return fBuildPathsBlock.getOutputLocation();
    }

    public IClasspathEntry[] getRawClassPath()
    {
        return fBuildPathsBlock.getRawClassPath();
    }

    public IRunnableWithProgress getRunnable()
    {
        return new IRunnableWithProgress()
        {

            public void run(IProgressMonitor monitor) throws InvocationTargetException,
                    InterruptedException
            {
                if (monitor == null)
                    monitor = new NullProgressMonitor();
                monitor.beginTask(NewWizardMessages.NewJavaProjectWizardPage_op_desc, 10);
                if (fProjectModified || isNewProjectHandle())
                    initBuildPaths();
                try {
                    BuildPathsBlock.createProject(getProjectHandle(), new URI(getLocationPath().toOSString()),
                            new SubProgressMonitor(monitor, 2));
                    BuildPathsBlock.addJavaNature(getProjectHandle(),
                            new SubProgressMonitor(monitor, 2));
                    fBuildPathsBlock.configureJavaProject(new SubProgressMonitor(monitor,
                            6));
                } catch (Exception e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }

        };
    }

    private WizardNewProjectCreationPage fMainPage;
    private IPath fOutputLocation;
    private IClasspathEntry fClasspathEntries[];
    private BuildPathsBlock fBuildPathsBlock;
    private boolean fProjectModified;

}
