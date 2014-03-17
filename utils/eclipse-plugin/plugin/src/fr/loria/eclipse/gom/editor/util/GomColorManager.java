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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import fr.loria.eclipse.jtom.editor.util.TomColorManager;

/**
 * Manager for colors used in the Gom editor
 * @author Martin GRANDCOLAS
 */
public class GomColorManager extends TomColorManager  {

	public final static String GOM_CODE_ATTRIBUTE = "__gom_code_attribute";
	public final static String GOM_HEADER_ATTRIBUTE = "__gom_header_attribute";
	public final static String DEFAULT_ATTRIBUTE = "__default";
	public static final String GOM_TYPE_ATTRIBUTE = "gom_type_attrubute";

	private Map<String, TextAttribute> fAttributes = new HashMap<String, TextAttribute>();

	public GomColorManager() {

		//		the comments colorations are redifined here

		fAttributes.put(DEFAULT_ATTRIBUTE, new TextAttribute(new Color(Display
				.getCurrent(), BLACK_COLOR)));

		fAttributes.put(GOM_HEADER_ATTRIBUTE, new TextAttribute(new Color(
				Display.getCurrent(), TOM_COLOR)));

		fAttributes.put(GOM_CODE_ATTRIBUTE, new TextAttribute(new Color(Display
				.getCurrent(), RED_COLOR)));
		fAttributes.put(GOM_TYPE_ATTRIBUTE, new TextAttribute(new Color(Display
				.getCurrent(), TYPE_COLOR)));

		fAttributes.put(IJavaPartitions.JAVA_STRING, new TextAttribute(
				new Color(Display.getCurrent(), STRING_COLOR)));

		fAttributes.put(IJavaPartitions.JAVA_MULTI_LINE_COMMENT,
				new TextAttribute(new Color(Display.getCurrent(),
						MULTI_LINE_COMMENT_COLOR)));

		fAttributes.put(IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
				new TextAttribute(new Color(Display.getCurrent(),
						SINGLE_LINE_COMMENT_COLOR)));

		fAttributes.put(IJavaPartitions.JAVA_CHARACTER, new TextAttribute(
				new Color(Display.getCurrent(), STRING_COLOR)));

		fAttributes.put(IJavaPartitions.JAVA_DOC, new TextAttribute(new Color(
				Display.getCurrent(), JAVADOC_DEFAULT_COLOR)));

	}

	public TextAttribute getAttribute(String type) {
		TextAttribute attr = (TextAttribute) fAttributes.get(type);
		if (attr == null) {
			attr = (TextAttribute) fAttributes.get(DEFAULT_ATTRIBUTE);
		}
		return attr;
	}
}
