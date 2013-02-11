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

package fr.loria.eclipse.jtom.editor;

import org.eclipse.jdt.ui.text.IJavaPartitions;


public interface ITomPartitions extends IJavaPartitions
{

    /**
     * The name of the Java partitioning (inherited)
     * @since 3.0 from org.eclipse.jdt.internal.ui.text.IJavaPartitions
     public final static String JAVA_PARTITIONING= "___java_partitioning";  //$NON-NLS-1$
     
     public final static String JAVA_SINGLE_LINE_COMMENT= "__java_singleline_comment"; //$NON-NLS-1$
     public final static String JAVA_MULTI_LINE_COMMENT= "__java_multiline_comment"; //$NON-NLS-1$
     public final static String JAVA_DOC= "__java_javadoc"; //$NON-NLS-1$
     public final static String JAVA_STRING= "__java_string"; //$NON-NLS-1$
     public final static String JAVA_CHARACTER= "__java_character";  //$NON-NLS-1$
     */
    /**
     * The name of the Tom partitioning.
     */
	
	
	/*if we want to have the gom completion in the %gom , we should have a new partition state GOM_STATE => not yet implemented*/
	
	
    public final static String TOM_HEADER_PART = "__tom_header_code";
    public final static String TOM_CODE_PART = "__tom_code";
    public final static String TOM_JAVA_PART = "__java_in_tom_code";
    public final static String TOM_CLOSURE_PART = "__tom_closure_code";
    public final static String TOM_BACKQUOTE_JAVA_PART = "__tom_backquotejava_code";
    public final static String TOM_BACKQUOTE_TOM_PART = "__tom_backquotetom_code";
    public final static String GOM_PART = "gom_code"; 
} //interface ITomPartitions
