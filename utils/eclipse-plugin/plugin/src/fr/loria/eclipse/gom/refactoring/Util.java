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
package fr.loria.eclipse.gom.refactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jface.text.Document;

import fr.loria.eclipse.jtom.JtomPlugin;
import fr.loria.eclipse.jtom.ui.wizards.ClassPathDetector;

/**
 * Some Utils to extract the destination folder of a tom/gom file
 * 
 * @author Martin GRANDCOLAS
 * 
 */
public class Util {

	public static final String TOM_EXTENSION = "t";
	public static final String MODULE_KEYWORD = "module";
	public static final String CLASS_KEYWORD = "class";
	public static final String GOM_KEYWORD = "%gom";
	public static final String GOM_EXTENSION = "gom";

	/**
	 * Extract the module name in a gom file ( if severals modules are defined
	 * in the file they are stored in the returned ArrayList
	 * 
	 * @param file
	 *            the gom file
	 * @return the module list ( lower case)
	 */
	public static ArrayList findModuleName(IFile file) {
		Document doc = new Document();
		ArrayList modules = new ArrayList();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(file
					.getContents()));
			StringBuilder text = new StringBuilder();
			String buffer;
			while ((buffer = reader.readLine()) != null) {
				text.append(buffer);
				text.append("\n");
			}
			doc.set(text.toString());
		} catch (Exception e) {
			System.out.println("error while trying to read the file "
					+ file.getName());
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		try {
			int pos = 0;
			int position = 0;
			while ((position = doc.search(position, MODULE_KEYWORD, true, true,
					true)) != -1) {
				pos = doc.getLineLength(doc.getLineOfOffset(position));
				String line = doc.get(position, pos);
				position += pos;
				line = line.trim();
				String split[] = line.split(" ");
				String tmp = null;
				for (int i = 0; i < split.length; i++) {
					if (split[i].equals(MODULE_KEYWORD) && i < split.length) {
						i++;
						tmp = split[i];
						break;
					}
				}
				if (tmp != null) {
					tmp = tmp.toLowerCase();
					if (tmp.contains(":")) {
						tmp = tmp.substring(0, tmp.indexOf(":"));
					}
					if (!modules.contains(tmp)) {
						modules.add(tmp);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error while trying to read the file "
					+ file.getName());
			e.printStackTrace();
		}
		return modules;
	}

	/**
	 * Get the destination path of a gom/tom file; the path is given from the
	 * project root folder For example if the destination folder is
	 * /home/myhomedir/workspace/projectroot/mydestination returns mydestination .
	 * The package destination option is also supported
	 * 
	 * @param file
	 *            the gom/tomfile
	 * @return the Ipath
	 */
	public static IPath getPathDestination(IFile file) {
		String command;
		QualifiedName qName = new QualifiedName(
				JtomPlugin.USE_CUSTOM_COMMAND_PROPERTY, file.getLocation()
						.toString());
		String commandType;
		String destDir = "";
		try {
			commandType = file.getPersistentProperty(qName);
			if (commandType == null || commandType.equals("true")) {
				qName = new QualifiedName(JtomPlugin.CUSTOM_COMMAND_PROPERTY,
						file.getLocation().toString());
				command = file.getPersistentProperty(qName);
			} else {
				qName = new QualifiedName(
						JtomPlugin.GENERATED_COMMAND_PROPERTY, file
								.getLocation().toString());
				command = file.getPersistentProperty(qName);
			}
			String commandArgs[] = {};
			if (command != null) {
				commandArgs = command.split(" ");
				// if the command line specifie a destination dir
				if (command.contains("--destdir")) {
					for (int i = 0; i < commandArgs.length; i++) {
						if (commandArgs[i].equals("--destdir")
								|| commandArgs[i].equals("-d")) {
							i++;
							destDir = commandArgs[i];
							break;
						}
					}
				}
			}
			if (TOM_EXTENSION.equals(file.getFileExtension())) {
				if (destDir == "") {
					return file.getParent().getProjectRelativePath();
				}
				// the destdir path returned by the wizard is an absolute path
				destDir = destDir.substring(destDir.indexOf(file.getProject()
						.getName()));
				// in order to delete the project root directory
				return new Path(destDir).removeFirstSegments(1);

			} else if (GOM_EXTENSION.equals(file.getFileExtension())) {

				// if the command line specifie a package dir
				if (command != null && command.contains("--package")) {
					for (int i = 0; i < commandArgs.length; i++) {
						if (commandArgs[i].equals("--package")
								|| commandArgs[i].equals("-p")) {
							i++;
							destDir += commandArgs[i].replace('.',
									File.separatorChar);
							break;
						}
					}
				}

				if ("".equals(destDir)) {
					return file.getParent().getProjectRelativePath();
				}
				ClassPathDetector classpath = new ClassPathDetector(file
						.getProject());
				ArrayList resEntries = new ArrayList();
				classpath.detectSourceFolders(resEntries);

				if (resEntries.size() == 0) {
					return file.getProject().getFolder(destDir)
							.getProjectRelativePath();
				}
				// there is a source folder
				else {
					IPath p = ((IClasspathEntry) resEntries.get(0)).getPath();
					for (int i = 1; i < resEntries.size(); i++) {

						IPath localpath = ((IClasspathEntry) resEntries.get(i))
								.getPath();
						if (localpath
								.isPrefixOf(file.getParent().getFullPath())) {
							if (localpath.segmentCount() > p.segmentCount()) {
								p = localpath;
							}
						}
					}
					System.out.println(p.removeFirstSegments(1).append(
							new Path(destDir)));
					return p.removeFirstSegments(1).append(new Path(destDir));

				}

			}
		} catch (CoreException e) {
			System.out.println("failed to remove the folder");
			e.printStackTrace();
		}
		return null;

	}

	public static void deleteTomGeneratedFiles(IFile oldfile) {

		IResource local;
		IPath path = Util.getPathDestination(oldfile);
		IProgressMonitor monitor = null;
		IContainer container = null;
		if (path != null && !"".equals(path.toString())) {
			container = oldfile.getProject().getFolder(path);
		} else {
			container = oldfile.getParent();
		}

		String oldname = null;
		int dotindex = oldfile.getName().indexOf('.');
		if (dotindex >= 0) {
			oldname = oldfile.getName().substring(0, dotindex);
		} else {
			System.out.println(oldname + " does not contain a '.'");
			return;
		}

		try {
			local = container.findMember(oldname + ".java");
			if (local != null && local.exists()) {
				local.delete(true, monitor);
			}
		} catch (CoreException e) {
			System.out.println("Failed to delete the " + oldname
					+ ".java file ");
			// e.printStackTrace();
		}

		try {
			local = container.findMember(oldname + ".class");
			if (local != null &&  local.exists()) {
				local.delete(true, monitor);
			}
		} catch (CoreException e) {
			System.out.println("Failed to delete the " + oldname
					+ ".class file ");
			// e.printStackTrace();
		}

		try {
			local = container.findMember("." + oldname + ".tfix.parsed");
			if (local != null &&  local.exists()) {
				local.delete(true, monitor);
			}
		} catch (CoreException e) {
			System.out.println("Failed to delete the " + oldname
					+ ".tfix.parsed file ");

		}
	}
}
