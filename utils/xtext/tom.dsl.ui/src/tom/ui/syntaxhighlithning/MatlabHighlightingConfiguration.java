package tom.ui.syntaxhighlithning;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

public class MatlabHighlightingConfiguration implements
		IHighlightingConfiguration {

	// Type as appering in Eclipse preferences (must inherit from
	// Default highligthning configuration 

	public static final String KEYWORD_ID = "keyword";
	public static final String PRAGMA_ID = "pragma";
	public static final String COMMENT_ID = "comment";
	public static final String PRIMITIVE_ID = "primitive";
	public static final String STRING_ID = "string";
	public static final String CALL_ID = "call";

	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		acceptor.acceptDefaultHighlighting(KEYWORD_ID, "Keyword",keywordTextStyle());
		acceptor.acceptDefaultHighlighting(PRIMITIVE_ID, "Primitive",primitiveTextStyle());
		acceptor.acceptDefaultHighlighting(PRAGMA_ID, "Pragma",pragmaTextStyle());
		acceptor.acceptDefaultHighlighting(COMMENT_ID, "Comment",commentTextStyle());
		acceptor.acceptDefaultHighlighting(STRING_ID, "String",stringTextStyle());
		acceptor.acceptDefaultHighlighting(CALL_ID, "Call",callTextStyle());
	}

	public TextStyle keywordTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(100, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle primitiveTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 0, 128));
		textStyle.setStyle(SWT.NORMAL);
		return textStyle;
	}

	public TextStyle stringTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 128, 0));
		textStyle.setStyle(SWT.NORMAL);
		return textStyle;
	}

	public TextStyle callTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setStyle(SWT.UNDERLINE_SINGLE);
		return textStyle;
	}
	public TextStyle pragmaTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(120, 120, 120));
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}

	public TextStyle commentTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 120, 100));
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}
}