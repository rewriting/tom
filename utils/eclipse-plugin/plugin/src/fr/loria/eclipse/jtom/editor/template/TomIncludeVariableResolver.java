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

package fr.loria.eclipse.jtom.editor.template;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariableResolver;

import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * Looks up existing ant variables and proposes them. The proposals are sorted by 
 * their prefix-likeness with the variable type.
 */
public class TomIncludeVariableResolver extends TemplateVariableResolver {
	
	
	/*
	 * @see org.eclipse.jface.text.templates.TemplateVariableResolver#resolveAll(org.eclipse.jface.text.templates.TemplateContext)
	 */
	protected String[] resolveAll(TemplateContext context) {

		try {
			String defaultIncludedPath = FileLocator.resolve(
			        JtomPlugin.getDefault().getBundle().getEntry("include")).getPath();
			System.out.println(defaultIncludedPath);
			File dir = new File(defaultIncludedPath);
			String[] includedFile = dir.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".tom");
				}
			});
			String[] res = new String[includedFile.length+1];
			res[0] = "${include}";
			for (int i = 0; i < includedFile.length; i++) {
				res[1+i] = includedFile[i];
			}
			return res;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new String[]{"${include}"};
	}

	
} //class TomIncludeVariableResolver
