/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 Inria
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

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariableResolver;

/**
 * Looks up existing tom variables and proposes them. The proposals are sorted by 
 * their prefix-likeness with the variable type.
 */
public class TomVariableResolver extends TemplateVariableResolver {
	
	
	/*
	 * @see org.eclipse.jface.text.templates.TemplateVariableResolver#resolveAll(org.eclipse.jface.text.templates.TemplateContext)
	 */
	protected String[] resolveAll(TemplateContext context) {
		String[] proposals= new String[] { "${op}", "${sort}" }; //$NON-NLS-1$ //$NON-NLS-2$
		
		Arrays.sort(proposals, new Comparator<Object>() {

			public int compare(Object o1, Object o2) {
				return getCommonPrefixLength(getType(), (String) o2) - getCommonPrefixLength(getType(), (String) o1);
			}

			private int getCommonPrefixLength(String type, String var) {
				int i= 0;
				CharSequence vSeq= var.subSequence(2, var.length() - 1); // strip away ${}
				while (i < type.length() && i < vSeq.length())
					if (Character.toLowerCase(type.charAt(i)) == Character.toLowerCase(vSeq.charAt(i)))
						i++;
					else
						break;
				return i;
			}
		});
		
		return proposals;
	}
	
} //class TomVariableResolver