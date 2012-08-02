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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.util.IClassFileReader;
import org.eclipse.jdt.core.util.ISourceAttribute;

import org.eclipse.jdt.ui.PreferenceConstants;

public class ClassPathDetector implements IResourceProxyVisitor {
		
	private HashMap<IPath, List<?>> fSourceFolders;
	private List<IResource> fClassFiles;
	private HashSet<IPath> fJARFiles;
		
	private IProject fProject;
		
	private IPath fResultOutputFolder;
	private IClasspathEntry[] fResultClasspath;
		
	public ClassPathDetector(IProject project) throws CoreException {
		fSourceFolders= new HashMap<IPath, List<?>>();
		fJARFiles= new HashSet<IPath>(10);
		fClassFiles= new ArrayList<IResource>(100);
		fProject= project;
			
		project.accept(this, IResource.NONE);
			
		fResultClasspath= null;
		fResultOutputFolder= null;
			
		detectClasspath();
	}
	
	
	private boolean isNested(IPath path, Iterator<IPath> iter) {
		while (iter.hasNext()) {
			IPath other= (IPath) iter.next();
			if (other.isPrefixOf(path)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method detectClasspath.
	 */
	private void detectClasspath() {
		ArrayList<IClasspathEntry> cpEntries= new ArrayList<IClasspathEntry>();
		
		detectSourceFolders(cpEntries);
		IPath outputLocation= detectOutputFolder(cpEntries);
		
		detectLibraries(cpEntries, outputLocation);
			
		if (cpEntries.isEmpty() && fClassFiles.isEmpty()) {
			return;
		}
		IClasspathEntry[] jreEntries= PreferenceConstants.getDefaultJRELibrary();
		for (int i= 0; i < jreEntries.length; i++) {
			cpEntries.add(jreEntries[i]);
		}
		
		IClasspathEntry[] entries= (IClasspathEntry[]) cpEntries.toArray(new IClasspathEntry[cpEntries.size()]);
		if (!JavaConventions.validateClasspath(JavaCore.create(fProject), entries, outputLocation).isOK()) {
			return;
		}
			
		fResultClasspath= entries;
		fResultOutputFolder= outputLocation;
	}
	
	public IPath findInSourceFolders(IPath path) {
		Iterator<IPath> iter= fSourceFolders.keySet().iterator();
		while (iter.hasNext()) {
			Object key= iter.next();
			List<?> cus= (List<?>) fSourceFolders.get(key);
			if (cus.contains(path)) {
				return (IPath) key;
			}
		}
		return null;
	}
	
	public IPath detectOutputFolder(List<IClasspathEntry> entries) {
		HashSet<IPath> classFolders= new HashSet<IPath>();
		
		for (Iterator<IResource> iter= fClassFiles.iterator(); iter.hasNext();) {
			IFile file= (IFile) iter.next();
			IPath location= file.getLocation();
			if (location == null) {
				continue;
			}

			IClassFileReader reader= ToolFactory.createDefaultClassFileReader(location.toOSString(), IClassFileReader.CLASSFILE_ATTRIBUTES);
			if (reader == null) {
				continue; // problematic class file
			}
			char[] className= reader.getClassName();
			ISourceAttribute sourceAttribute= reader.getSourceFileAttribute();
			if (className != null && sourceAttribute != null && sourceAttribute.getSourceFileName() != null) {
				IPath packPath= file.getParent().getFullPath();
				int idx= CharOperation.lastIndexOf('/', className) + 1;
				IPath relPath= new Path(new String(className, 0, idx));
				IPath cuPath= relPath.append(new String(sourceAttribute.getSourceFileName()));
				
				IPath resPath= null;
				if (idx == 0) {
					resPath= packPath;
				} else {
					IPath folderPath= getFolderPath(packPath, relPath);
					if (folderPath != null) {
						resPath= folderPath;
					}
				}
				if (resPath != null) {
					IPath path= findInSourceFolders(cuPath);
					if (path != null) {
						return resPath;
					} else {
						classFolders.add(resPath);	
					}
				}
			}			
		}		
		IPath projPath= fProject.getFullPath();
		if (fSourceFolders.size() == 1 && classFolders.isEmpty() && fSourceFolders.get(projPath) != null) {
			return projPath;
		} else {
			IPath path= projPath.append(PreferenceConstants.getPreferenceStore().getString(PreferenceConstants.SRCBIN_BINNAME));
			while (classFolders.contains(path)) {
				path= new Path(path.toString() + '1');
			}
			return path;
		} 			
	}


	private void detectLibraries(ArrayList<IClasspathEntry> cpEntries, IPath outputLocation) {
		Set<IPath> sourceFolderSet= fSourceFolders.keySet();
		for (Iterator<IPath> iter= fJARFiles.iterator(); iter.hasNext();) {
			IPath path= (IPath) iter.next();
			if (isNested(path, sourceFolderSet.iterator())) {
				continue;
			}
			if (outputLocation != null && outputLocation.isPrefixOf(path)) {
				continue;
			}
			IClasspathEntry entry= JavaCore.newLibraryEntry(path, null, null);
			cpEntries.add(entry);	
		}
	}


	public void detectSourceFolders(ArrayList<IClasspathEntry> resEntries) {
		Set<IPath> sourceFolderSet= fSourceFolders.keySet();
		for (Iterator<IPath> iter= sourceFolderSet.iterator(); iter.hasNext();) {
			IPath path= (IPath) iter.next();
			ArrayList<IPath> excluded= new ArrayList<IPath>();
			for (Iterator<IPath> inner= sourceFolderSet.iterator(); inner.hasNext();) {
				IPath other= (IPath) inner.next();
				if (!path.equals(other) && path.isPrefixOf(other)) {
					IPath pathToExclude= other.removeFirstSegments(path.segmentCount()).addTrailingSeparator();
					excluded.add(pathToExclude);
				}
			}
			IPath[] excludedPaths= (IPath[]) excluded.toArray(new IPath[excluded.size()]);
			IClasspathEntry entry= JavaCore.newSourceEntry(path, excludedPaths);
			resEntries.add(entry);
		}
	}

	private void visitCompilationUnit(IFile file) {
		ICompilationUnit cu= JavaCore.createCompilationUnitFrom(file);
		if (cu != null) {
			ICompilationUnit workingCopy= null;
			try {
				workingCopy= cu.getWorkingCopy(null);
				IPath relPath= getPackagePath(workingCopy.getSource());
				IPath packPath= file.getParent().getFullPath();
				String cuName= file.getName();
				if (relPath == null) {
					addToMap(fSourceFolders, packPath, new Path(cuName));
				} else {
					IPath folderPath= getFolderPath(packPath, relPath);
					if (folderPath != null) {
						addToMap(fSourceFolders, folderPath, relPath.append(cuName));
					}					
				}				
			} catch (JavaModelException e) {
				// ignore
			} catch (InvalidInputException e) {
				// ignore
			} finally {
				if (workingCopy != null) {
					try {
						workingCopy.discardWorkingCopy();
					} catch (JavaModelException ignore) {
					}
				}
			}
		}
	}
	
	public IPath getPackagePath(String source) throws InvalidInputException {
		IScanner scanner= ToolFactory.createScanner(false, false, false, false);
		scanner.setSource(source.toCharArray());
		scanner.resetTo(0, source.length() - 1);
		int tok= scanner.getNextToken();
		if (tok != ITerminalSymbols.TokenNamepackage) {
			return null;
		}
		IPath res= Path.EMPTY;
		do {
			tok= scanner.getNextToken();
			if (tok == ITerminalSymbols.TokenNameIdentifier) {
				res= res.append(new String(scanner.getCurrentTokenSource()));
			} else {
				return res;
			}
			tok= scanner.getNextToken();
		} while (tok == ITerminalSymbols.TokenNameDOT);
		
		return res;
	}
	
	
	private void addToMap(HashMap<IPath, List<?>> map, IPath folderPath, IPath relPath) {
		List<IPath> list= (List<IPath>) map.get(folderPath);
		if (list == null) {
			list= new ArrayList<IPath>(50);
			map.put(folderPath, list);
		}		
		list.add(relPath);
	}

	public IPath getFolderPath(IPath packPath, IPath relpath) {
		int remainingSegments= packPath.segmentCount() - relpath.segmentCount();
		if (remainingSegments >= 0) {
			IPath common= packPath.removeFirstSegments(remainingSegments);
			if (common.equals(relpath)) {
				return packPath.uptoSegment(remainingSegments);
			}
		}
		return null;
	}

	private boolean hasExtension(String name, String ext) {
		return name.endsWith(ext) && (ext.length() != name.length()); 
	}
	
	@SuppressWarnings("deprecation")
	private boolean isValidCUName(String name) {
		return !JavaConventions.validateCompilationUnitName(name).matches(IStatus.ERROR);
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceProxyVisitor#visit(org.eclipse.core.resources.IResourceProxy)
	 */
	public boolean visit(IResourceProxy proxy) {
		if (proxy.getType() == IResource.FILE) {
			String name= proxy.getName();
			if (hasExtension(name, ".java") && isValidCUName(name)) { //$NON-NLS-1$
				visitCompilationUnit((IFile) proxy.requestResource());
			} else if (hasExtension(name, ".class")) { //$NON-NLS-1$
				fClassFiles.add(proxy.requestResource());
			} else if (hasExtension(name, ".jar")) { //$NON-NLS-1$
				fJARFiles.add(proxy.requestFullPath());
			}
			return false;
		}
		return true;
	}


	public IPath getOutputLocation() {
		return fResultOutputFolder;
	}
		
	public IClasspathEntry[] getClasspath() {
		return fResultClasspath;
	}
	
} //class ClassPathDetector