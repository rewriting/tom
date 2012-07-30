/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2004 INRIA
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


import java.io.IOException;
import java.io.Reader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


/**
 * Provides a set of convenience methods for creating HTML pages.
 */
public class HTMLPrinter {
			
	private HTMLPrinter() {
	}
	
	private static String replace(String text, char c, String s) {
				
		int previous= 0;
		int current= text.indexOf(c, previous);
		
		if (current == -1)
			return text;
		
		StringBuffer buffer= new StringBuffer();	
		while (current > -1) {
			buffer.append(text.substring(previous, current));
			buffer.append(s);
			previous= current + 1;
			current= text.indexOf(c, previous);
		}
		buffer.append(text.substring(previous));
		
		return buffer.toString();
	}
	
	public static String convertToHTMLContent(String content) {
		content= replace(content, '<', "&lt;"); //$NON-NLS-1$
		return replace(content, '>', "&gt;"); //$NON-NLS-1$
	}
	
	public static String read(Reader rd) {
		
		StringBuffer buffer= new StringBuffer();
		char[] readBuffer= new char[2048];
		
		try {
			int n= rd.read(readBuffer);
			while (n > 0) {
				buffer.append(readBuffer, 0, n);
				n= rd.read(readBuffer);
			}
			return buffer.toString();
		} catch (IOException x) {
		}
		
		return null;
	}

	public static void insertPageProlog(StringBuffer buffer, int position, RGB bgRGB) {
		if (bgRGB == null)
			insertPageProlog(buffer, position);
		else {
			StringBuffer pageProlog= new StringBuffer(60);
			pageProlog.append("<html><body text=\"#000000\" bgcolor=\""); //$NON-NLS-1$
			appendColor(pageProlog, bgRGB);
			pageProlog.append("\"><font size=-1>"); //$NON-NLS-1$
			buffer.insert(position,  pageProlog.toString());
		}
	}
	
	private static void appendColor(StringBuffer buffer, RGB rgb) {
		buffer.append('#');
		buffer.append(Integer.toHexString(rgb.red));
		buffer.append(Integer.toHexString(rgb.green));
		buffer.append(Integer.toHexString(rgb.blue));
	}

	public static void insertPageProlog(StringBuffer buffer, int position) {
		RGB bgColor= null;
		IWorkbenchWindow window= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			Display display= window.getShell().getDisplay();
			if (display != null && !display.isDisposed())
				bgColor= display.getSystemColor(SWT.COLOR_INFO_BACKGROUND).getRGB();
		}
		if (bgColor == null)
			bgColor= new RGB(255,255, 225); // RGB value of info bg color on WindowsXP
			
		insertPageProlog(buffer, position, bgColor); //$NON-NLS-1$
	}
	
	public static void addPageProlog(StringBuffer buffer) {
		insertPageProlog(buffer, buffer.length());
	}
	
	public static void addPageEpilog(StringBuffer buffer) {
		buffer.append("</font></body></html>"); //$NON-NLS-1$
	}
	
	public static void startBulletList(StringBuffer buffer) {
		buffer.append("<ul>"); //$NON-NLS-1$
	}
	
	public static void endBulletList(StringBuffer buffer) {
		buffer.append("</ul>"); //$NON-NLS-1$
	}
	
	public static void addBullet(StringBuffer buffer, String bullet) {
		if (bullet != null) {
			buffer.append("<li>"); //$NON-NLS-1$
			buffer.append(bullet);
			buffer.append("</li>"); //$NON-NLS-1$
		}
	}
	
	public static void addSmallHeader(StringBuffer buffer, String header) {
		if (header != null) {
			buffer.append("<h5>"); //$NON-NLS-1$
			buffer.append(header);
			buffer.append("</h5>"); //$NON-NLS-1$
		}
	}
	
	public static void addParagraph(StringBuffer buffer, String paragraph) {
		if (paragraph != null) {
			buffer.append("<p>"); //$NON-NLS-1$
			buffer.append(paragraph);
		}
	}
	
	public static void addParagraph(StringBuffer buffer, Reader paragraphReader) {
		if (paragraphReader != null)
			addParagraph(buffer, read(paragraphReader));
	}

} //class HTMLPrinter 