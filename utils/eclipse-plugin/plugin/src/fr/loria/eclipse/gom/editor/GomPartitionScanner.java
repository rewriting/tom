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

package fr.loria.eclipse.gom.editor;

import org.eclipse.jdt.internal.ui.text.JavaPartitionScanner;

/**
 * This scanner recognizes the JavaDoc comments, Java multi line comments, Java
 * single line comments, Java strings and Java characters and TOM code.
 */
public class GomPartitionScanner extends JavaPartitionScanner {

	public final static String GOM_PART = "gom_part";
	public final static String[] GOM_PARTITION_TYPES = new String[] { GOM_PART,
			JAVA_CHARACTER, JAVA_DOC, JAVA_MULTI_LINE_COMMENT,
			JAVA_SINGLE_LINE_COMMENT, JAVA_STRING

	};

	/**
	 * Constructs the differents rules to change partitions 
	 */
	public GomPartitionScanner() {
		super();

	}

} // class GomEditorPartitionScanner
