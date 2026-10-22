/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2015 Inria Nancy, France.
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

package fr.loria.eclipse.aircube.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import tom.engine.Tom;

/**
 * @author julien
 */
public class AircubePreferencePage extends PreferencePage
		implements IWorkbenchPreferencePage {
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(final Composite parent) {
		GridLayout layout = new GridLayout(3, true);
		parent.setLayout(layout);
		
		Label label5_1 = new Label(parent, SWT.NONE);
		label5_1.setText("GOM version: ");
		Label label55_1 = new Label(parent, SWT.NONE);
		label55_1.setText(Tom.VERSION);
		
		Label sep4_1 = new Label(parent, SWT.SEPARATOR|SWT.HORIZONTAL);
		GridData dat4_1 = new GridData();
		dat4_1.horizontalSpan = 3;
		dat4_1.horizontalAlignment = GridData.FILL;
		sep4_1.setLayoutData(dat4_1);
				
		Label label6 = new Label(parent, SWT.NONE);
		label6.setText("Tom version: ");
		Label label66 = new Label(parent, SWT.NONE);
		label66.setText(Tom.VERSION);
		
		noDefaultAndApplyButton();
		return null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
} //class AircubePreferencePage