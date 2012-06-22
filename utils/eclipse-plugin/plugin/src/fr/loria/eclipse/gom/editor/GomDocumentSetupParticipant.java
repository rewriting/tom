package fr.loria.eclipse.gom.editor;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * Inits the document, the document is attached to a GomPartitionScanner to provide the syntax coloration
 * 
 * @author Martin GRANDCOLAS
 */

public class GomDocumentSetupParticipant implements IDocumentSetupParticipant {

	/**
	 * Method called at the beginning
	 * 
	 * @see org.eclipse.core.filebuffers.IDocumentSetupParticipant#setup(org.eclipse.jface.text.IDocument)
	 */
	public void setup(IDocument document) {
		if (document instanceof IDocumentExtension3) { // Extension3 because of
														// multiple
														// partitionning
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			IDocumentPartitioner partitioner = new FastPartitioner(JtomPlugin
					.getDefault().getGomPartitionScanner(),
					GomPartitionScanner.GOM_PARTITION_TYPES);
			// Attach the partitioner to the document
			extension3.setDocumentPartitioner(JtomPlugin.GOM_PARTITIONING,
					partitioner);
			partitioner.connect(document);
		}
	}

}