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

import java.util.*;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jface.resource.StringConverter;

/**
 * Manager for colors used in the Tom editor
 */
public class TomColorManager implements IColorManager{

	public static final RGB RED_COLOR= new RGB(255, 0, 0);
	public static final RGB GREEN_COLOR= new RGB(0, 255, 0);
	public static final RGB BLUE_COLOR= new RGB(0, 0, 255);
	public static final RGB REDGREEN_COLOR= new RGB(255, 255, 0);
	public static final RGB GREENBLUE_COLOR= new RGB(0, 255, 255);
	public static final RGB REDBLUE_COLOR= new RGB(255, 0, 255);
	public static final RGB WHITE_COLOR= new RGB(255, 255, 255);
	public static final RGB BLACK_COLOR= new RGB(0, 0, 0);
	public static final RGB OTHER_COLOR= new RGB(128, 128, 128);

	public static final RGB MULTI_LINE_COMMENT_COLOR= new RGB(128, 0, 0);
	public static final RGB SINGLE_LINE_COMMENT_COLOR= new RGB(128, 128, 0);
	public static final RGB KEYWORD_COLOR= new RGB(0, 0, 128);
	public static final RGB TYPE_COLOR= new RGB(0, 0, 128);
	public static final RGB STRING_COLOR= new RGB(128, 0, 0);
	public static final RGB DEFAULT_COLOR= new RGB(128, 128, 0);
	public static final RGB TOM_COLOR= new RGB(0, 128, 128);
	public static final RGB JAVADOC_KEYWORD_COLOR= new RGB(0, 128, 0);
	public static final RGB JAVADOC_TAG_COLOR= new RGB(128, 128, 128);
	public static final RGB JAVADOC_LINK_COLOR= new RGB(128, 128, 128);
	public static final RGB JAVADOC_DEFAULT_COLOR= new RGB(0, 128, 128);

	protected Map<RGB, Color> fColorTable= new HashMap<RGB, Color>(20);

	/**
	 * Release all of the color resources held onto by the receiver.
	 */	
	public void dispose() {
		Iterator<Color> e= fColorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
	}
	
	/**
	 * Return the Color that is stored in the Color table as rgb.
	 */
	public Color getColor(RGB rgb) {
		Color color= (Color) fColorTable.get(rgb);
		if (color == null) {
			color= new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}

	public Color getColor(String color) {
    return getColor(StringConverter.asRGB(color));
  }

} //class TomColorProvider 