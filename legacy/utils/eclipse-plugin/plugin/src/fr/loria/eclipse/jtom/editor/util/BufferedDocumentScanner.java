/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2004 Inria
	   			       Nancy, France.

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

package fr.loria.eclipse.jtom.editor.util;


import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;



/**
 * A buffered document scanner. The buffer always contains a section 
 * of a fixed size of the document to be scanned.
 */

public final class BufferedDocumentScanner implements ICharacterScanner {

	/** The document being scanned. */
	private IDocument fDocument;
	/** The offset of the document range to scan. */
	private int fRangeOffset;
	/** The length of the document range to scan. */
	private int fRangeLength;
	/** The delimiters of the document. */
	private char[][] fDelimiters;

	/** The buffer. */
	private final char[] fBuffer;
	/** The offset of the buffer within the document. */
	private int fBufferOffset;
	/** The valid length of the buffer for access. */
	private int fBufferLength;
	/** The offset of the scanner within the buffer. */
	private int fOffset;

	
	/**
	 * Creates a new buffered document scanner.
	 * The buffer size is set to the given number of characters.
	 * 
	 * @param size the buffer size
	 */
	public BufferedDocumentScanner(int size) {
		Assert.isTrue(size >= 1);
		fBuffer= new char[size];
	}

	/**
	 * Fills the buffer with the contens of the document starting at the given offset.
	 *
	 * @param offset the document offset at which the buffer starts
	 */
	private final void updateBuffer(int offset) {

		fBufferOffset= offset;
		
		if (fBufferOffset + fBuffer.length > fRangeOffset + fRangeLength)
			fBufferLength= fRangeLength - (fBufferOffset - fRangeOffset);
		else
			fBufferLength= fBuffer.length;

		try {
			final String content= fDocument.get(fBufferOffset, fBufferLength);
			content.getChars(0, fBufferLength, fBuffer, 0);
		} catch (BadLocationException e) {
		}
	}

	/**
	 * Configures the scanner by providing access to the document range over which to scan.
	 *
	 * @param document the document to scan
	 * @param offset the offset of the document range to scan
	 * @param length the length of the document range to scan
	 */
	public final void setRange(IDocument document, int offset, int length) {

		fDocument= document;
		fRangeOffset= offset;
		fRangeLength= length;

		String[] delimiters= document.getLegalLineDelimiters();
		fDelimiters= new char[delimiters.length][];
		for (int i= 0; i < delimiters.length; i++)
			fDelimiters[i]= delimiters[i].toCharArray();

		updateBuffer(offset);
		fOffset= 0;
	}

	/*
	 * @see ICharacterScanner#read()
	 */
	public final int read() {
		if (fOffset == fBufferLength) {
			if (fBufferOffset + fBufferLength == fDocument.getLength()) {
			    //System.out.println("=====> Reading EOF");
			    return EOF;
			}
			else {
				updateBuffer(fBufferOffset + fBufferLength);
				fOffset= 0;
			}
		}
		/*char res = fBuffer[fOffset];
		String re = (res=='\n')?"\\n":new String(new char[]{res});
		re = (res=='\t')?"\\t":new String(new char[]{res});
		System.out.println("=====> Reading "+re);*/
		return fBuffer[fOffset++];
	}

	/*
	 * @see ICharacterScanner#unread
	 */
	public final void unread() {

		if (fOffset == 0) {
			if (fBufferOffset == fRangeOffset) {
				// error: BOF
			} else {
				updateBuffer(fBufferOffset - fBuffer.length);
				fOffset= fBuffer.length - 1;
			}
		} else {			
			--fOffset;
		}
	}

	/*
	 * @see ICharacterScanner#getColumn()
	 */
	public final int getColumn() {

		try {
			final int offset= fBufferOffset + fOffset;
			final int line= fDocument.getLineOfOffset(offset);
			final int start= fDocument.getLineOffset(line);
			return offset - start;
		} catch (BadLocationException e) {
		}

		return -1;
	}

	/*
	 * @see ICharacterScanner#getLegalLineDelimiters()
	 */
	public final char[][] getLegalLineDelimiters() {
		return fDelimiters;
	}
	
} //class BufferedDocumentScanner