package tom.ui.syntaxhighlithning;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

public class MatlabHighlightingCalculator implements ISemanticHighlightingCalculator{
	// TODO : to complete from Matlab.xtext
	Set<String> keywords = new HashSet<String>(Arrays.asList(new String[]{"while","function","end","for","if","loop","else","switch","case"}));
	Set<String> primitives = new HashSet<String>(Arrays.asList(new String[]{"plot","axis","filter","conv2d","length","sum","min","max","ones","zeros","conv","conv2","hist","prod","isempty","isvector","error","any","disp","sin","cos","size","global","figure","imread","exp","log","acos","asin","atan","abs","sqrt","floor","ceil","colormap","imshow","clf","mean","svd","qr","inv"}));
	
	public MatlabHighlightingCalculator() {
		
		// TODO Auto-generated constructor stub
	}
	public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor) {
		INode root = resource.getParseResult().getRootNode();
		
		for(ILeafNode node : root.getLeafNodes()) {
			EObject semanticElement = node.getSemanticElement();
			String text = node.getText();
//			if(semanticElement instanceof CallInstruction) {
//				acceptor.addPosition(node.getOffset(), node.getLength(), MatlabHighlightingConfiguration.CALL_ID);
//			}
//			if(semanticElement instanceof StringLitteralInstruction) {
//				acceptor.addPosition(node.getOffset(), node.getLength(), MatlabHighlightingConfiguration.STRING_ID);
//			}
//			if( semanticElement instanceof PragmaAnnotation || text.contains("pragma"))
//			{
//				acceptor.addPosition(node.getOffset(), node.getLength(), MatlabHighlightingConfiguration.PRAGMA_ID);
//			}
//			if(keywords.contains(text)) {
//				acceptor.addPosition(node.getOffset(), node.getLength(), MatlabHighlightingConfiguration.KEYWORD_ID);
//			}
//			if(primitives.contains(text)) {
//				acceptor.addPosition(node.getOffset(), node.getLength(), MatlabHighlightingConfiguration.PRIMITIVE_ID);
//			}
//			else if( node instanceof HiddenLeafNode  && node.getGrammarElement() instanceof TerminalRuleImpl )
//				{
//					TerminalRuleImpl ge = (TerminalRuleImpl) node.getGrammarElement();
//					if( ge.getName().equalsIgnoreCase( "SL_COMMENT" ) ) {
//						acceptor.addPosition(node.getOffset(), node.getLength(), MatlabHighlightingConfiguration.COMMENT_ID);
//
//					}
//				}
		}
	}
	
	
}
