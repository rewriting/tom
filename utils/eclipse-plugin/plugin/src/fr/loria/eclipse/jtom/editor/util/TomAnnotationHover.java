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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.MarkerAnnotation;

import fr.loria.eclipse.jtom.editor.TomEditorMessages;


/**
 * Determines all markers for the given line and collects, concatenates, and formates
 * their messages.
 */
public class TomAnnotationHover implements IAnnotationHover {

		
	
		
	public TomAnnotationHover() {
	}
	
	/**
	 * Returns the distance to the ruler line. 
	 */
	protected int compareRulerLine(Position position, IDocument document, int line) {
		
		if (position.getOffset() > -1 && position.getLength() > -1) {
			try {
				int annotationLine= document.getLineOfOffset(position.getOffset());
				if (line == annotationLine)
					return 1;
				if (annotationLine <= line && line <= document.getLineOfOffset(position.getOffset() + position.getLength()))
					return 2;
			} catch (BadLocationException x) {
			}
		}
		
		return 0;
	}
	
	/**
	 * Selects a set of markers from the two lists. By default, it just returns
	 * the set of exact matches.
	 */
	protected List<Annotation> select(List<Annotation> exactMatch, List<Annotation> including) {
		return exactMatch;
	}
	
	/**
	 * Returns one marker which includes the ruler's line of activity.
	 */
	protected List<Annotation> getAllAnnotationsForLine(ISourceViewer viewer, int line) {
		
		IDocument document= viewer.getDocument();
		IAnnotationModel model= viewer.getAnnotationModel();
		
		if (model == null)
			return null;
			
		List<Annotation> exact= new ArrayList<Annotation>();
		List<Annotation> including= new ArrayList<Annotation>();
		
		Iterator<?> e= model.getAnnotationIterator();
		HashMap<Position, Serializable> messagesAtPosition= new HashMap<Position, Serializable>();
		while (e.hasNext()) {
			Annotation annotation= (Annotation) e.next();
			String message = null;
			
			if (annotation instanceof MarkerAnnotation) {
			    MarkerAnnotation anno= (MarkerAnnotation)annotation;
		      message = anno.getMarker().getAttribute(IMarker.MESSAGE, "No error message");
			} else if (annotation.getText() != null) {
				message=annotation.getText();
			}
			
			if(message == null)
			    continue;
			
			Position position= model.getPosition(annotation);
			if (position == null)
				continue;
			
			AnnotationPreference preference= getAnnotationPreference(annotation);
			if (preference == null)
			  continue;
			
			
			/*if (OVERVIEW_RULER_HOVER.equals(fType)) {				
				String key= preference.getOverviewRulerPreferenceKey();
				if (key == null || !fStore.getBoolean(key)){
				    System.out.println("getOverviewRulerPreferenceKey "+key+" "+fStore.getBoolean(key));
				    continue;
				}
			} else if (VERTICAL_RULER_HOVER.equals(fType)) {
				String key= preference.getVerticalRulerPreferenceKey();
				// backward compatibility
				if (key != null && !fStore.getBoolean(key)) {
				    System.out.println("getVerticalRulerPreferenceKey "+key+" "+fStore.getBoolean(key));
					continue;
				}
			}*/
			
			if (isDuplicateAnnotation(messagesAtPosition, position, message))
				continue;
			
			switch (compareRulerLine(position, document, line)) {
			case 1:
				exact.add(annotation);
				break;
			case 2:
				including.add(annotation);
				break;
			}
		}
		
		return select(exact, including);
	}

	private boolean isDuplicateAnnotation(Map<Position, Serializable> messagesAtPosition, Position position, String message) {
		if (messagesAtPosition.containsKey(position)) {
			Object value= messagesAtPosition.get(position);
			if (message.equals(value))
				return true;

			if (value instanceof List) {
				List<String> messages= (List<String>)value;
				if  (messages.contains(message))
					return true;
				else
					messages.add(message);
			} else {
				ArrayList<Object> messages= new ArrayList<Object>();
				messages.add(value);
				messages.add(message);
				messagesAtPosition.put(position, messages);
			}
		} else
			messagesAtPosition.put(position, message);
		return false;
	}
		
	/*
	 * @see IVerticalRulerHover#getHoverInfo(ISourceViewer, int)
	 */
	public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
		List<?> annotations= getAllAnnotationsForLine(sourceViewer, lineNumber);
		if (annotations != null) {
		    
			if (annotations.size() == 1) {
				// optimization
				Annotation annotation= (Annotation) annotations.get(0);
				String message= annotation.getText();
				if (message != null && message.trim().length() > 0)
					return formatSingleMessage(message);
					
			} else {
				List<String> messages= new ArrayList<String>();
				
				Iterator<?> e= annotations.iterator();
				while (e.hasNext()) {
					Annotation annotation= (Annotation) e.next();
					String message= annotation.getText();
					if (message != null && message.trim().length() > 0)
						messages.add(message.trim());
				}
				
				if (messages.size() == 1)
					return formatSingleMessage((String) messages.get(0));
					
				if (messages.size() > 1)
					return formatMultipleMessages(messages);
			}
		}
		
		return null;
	}
	
	/*
	 * Formats a message as HTML text.
	 */
	private String formatSingleMessage(String message) {
		StringBuffer buffer= new StringBuffer();
		HTMLPrinter.addPageProlog(buffer);
		HTMLPrinter.addParagraph(buffer, HTMLPrinter.convertToHTMLContent(message));
		HTMLPrinter.addPageEpilog(buffer);
		return buffer.toString();
	}
	
	/*
	 * Formats several message as HTML text.
	 */
	private String formatMultipleMessages(List<String> messages) {
		StringBuffer buffer= new StringBuffer();
		HTMLPrinter.addPageProlog(buffer);
		HTMLPrinter.addParagraph(buffer, HTMLPrinter.convertToHTMLContent(TomEditorMessages.getResourceString("TomAnnotationHover.multipleMarkersAtThisLine"))); //$NON-NLS-1$
		
		HTMLPrinter.startBulletList(buffer);
		Iterator<String> e= messages.iterator();
		while (e.hasNext())
			HTMLPrinter.addBullet(buffer, HTMLPrinter.convertToHTMLContent((String) e.next()));
		HTMLPrinter.endBulletList(buffer);	
		
		HTMLPrinter.addPageEpilog(buffer);
		return buffer.toString();
	}

	/**
	 * Returns the annotation preference for the given annotation.
	 * 
	 * @param annotation the annotation
	 * @return the annotation preference or <code>null</code> if none
	 */	
	private AnnotationPreference getAnnotationPreference(Annotation annotation) {
		return EditorsUI.getAnnotationPreferenceLookup().getAnnotationPreference(annotation);
	}
	
} //class TomAnnotationHover

