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
package fr.loria.eclipse.gom.refactoring;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This class is used to visit a folder , find a resource and delete it
 * @author Martin GRANDCOLAS
 *
 */

public class ResourceVisitor implements IResourceVisitor {
	protected String TokenToFind = null;

	public ResourceVisitor(String token, IProgressMonitor pm) {
		this.TokenToFind = token;
	}

	private boolean DeleteResource(IResource resource) {
		switch (resource.getType()) {
		//we delete all the last genereted  package 
		case IResource.FOLDER:
			IFolder folder = (IFolder) resource;
			if (TokenToFind.toLowerCase().equals(folder.getName())) {
				try {
					folder.refreshLocal(IResource.DEPTH_INFINITE, null);
					folder.delete(true, null);
					return false;
				} catch (CoreException e) {
					System.out.println("Impossioble to delete the folder " + folder);
					e.printStackTrace();
				}
			}
			return true;
		default:
			return true;
		}
	}

	public boolean visit(IResource resource) {
		//		return true to continue visiting children.	
		return DeleteResource(resource);

	}
}
