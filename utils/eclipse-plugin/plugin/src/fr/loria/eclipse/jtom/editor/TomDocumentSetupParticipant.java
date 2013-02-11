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

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

import fr.loria.eclipse.jtom.JtomPlugin;


/**
 * @author julien
 */
public class TomDocumentSetupParticipant implements IDocumentSetupParticipant {
 	/*
 	 * @see org.eclipse.core.filebuffers.IDocumentSetupParticipant#setup(org.eclipse.jface.text.IDocument)
 	 */
 	public void setup(IDocument document) {
 			if (document instanceof IDocumentExtension3) {
 				IDocumentExtension3 extension3= (IDocumentExtension3) document;
 				IDocumentPartitioner partitioner= new FastPartitioner(JtomPlugin.getDefault().getTomPartitionScanner(), TomEditorPartitionScanner.PARTITION_TYPES);
 				extension3.setDocumentPartitioner(JtomPlugin.TOM_PARTITIONING, partitioner);
 				partitioner.connect(document);
 			}
 	}
} //class TomDocumentSetupParticipant