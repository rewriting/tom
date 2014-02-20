/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2004-2014 Inria Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
	  Julien Guyon			e-mail: Julien.Guyon@loria.fr
	
*/

package fr.loria.eclipse.gom.editor.util;

import org.eclipse.jface.text.rules.RuleBasedScanner;

import fr.loria.eclipse.gom.editor.GomCodeScanner;

/**
 * The TomEditorEnvironment maintains singletons used by the TOM editor
 */
public class GomEditorEnvironment {

	private static GomColorManager fgColorProvider;
	private static GomCodeScanner fgCodeScanner;
	
	private static int fgRefCount= 0;

	/**
	 * A connection has occured - initialize the receiver if it is the first activation.
	 */
	public static void connect(Object client) {
		if (++fgRefCount == 1) {
			fgColorProvider= new GomColorManager();
			fgCodeScanner= new GomCodeScanner();
		}
	}
	
	/**
	 * A disconnection has occured - clear the receiver if it is the last deactivation.
	 */
	 public static void disconnect(Object client) {
		if (--fgRefCount == 0) {
			fgCodeScanner= null;
			fgColorProvider.dispose();
			fgColorProvider= null;
		}
	}
	
	/**
	 * Returns the singleton tom scanner.
	 */
	 public static RuleBasedScanner getGomCodeScanner() {
		return fgCodeScanner;
	}
	
	/**
	 * Returns the singleton color provider.
	 */
	 public static GomColorManager getGomColorManager() {
		return fgColorProvider;
	}
	
} //class TomEditorEnvironment