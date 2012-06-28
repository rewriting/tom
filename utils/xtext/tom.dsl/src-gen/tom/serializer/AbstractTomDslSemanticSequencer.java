package tom.serializer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.serializer.acceptor.ISemanticSequenceAcceptor;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.diagnostic.ISemanticSequencerDiagnosticProvider;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.sequencer.AbstractSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.GenericSequencer;
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider.INodesForEObjectProvider;
import org.eclipse.xtext.serializer.sequencer.ISemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;
import tom.services.TomDslGrammarAccess;
import tom.tomDsl.ARG;
import tom.tomDsl.Include;
import tom.tomDsl.JavaBody;
import tom.tomDsl.Operation;
import tom.tomDsl.OperationArray;
import tom.tomDsl.TomDslPackage;
import tom.tomDsl.TomFile;
import tom.tomDsl.TypeTerm;

@SuppressWarnings("restriction")
public class AbstractTomDslSemanticSequencer extends AbstractSemanticSequencer {

	@Inject
	protected TomDslGrammarAccess grammarAccess;
	
	@Inject
	protected ISemanticSequencerDiagnosticProvider diagnosticProvider;
	
	@Inject
	protected ITransientValueService transientValues;
	
	@Inject
	@GenericSequencer
	protected Provider<ISemanticSequencer> genericSequencerProvider;
	
	protected ISemanticSequencer genericSequencer;
	
	
	@Override
	public void init(ISemanticSequencer sequencer, ISemanticSequenceAcceptor sequenceAcceptor, Acceptor errorAcceptor) {
		super.init(sequencer, sequenceAcceptor, errorAcceptor);
		this.genericSequencer = genericSequencerProvider.get();
		this.genericSequencer.init(sequencer, sequenceAcceptor, errorAcceptor);
	}
	
	public void createSequence(EObject context, EObject semanticObject) {
		if(semanticObject.eClass().getEPackage() == TomDslPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case TomDslPackage.ARG:
				if(context == grammarAccess.getARGRule()) {
					sequence_ARG(context, (ARG) semanticObject); 
					return; 
				}
				else break;
			case TomDslPackage.INCLUDE:
				if(context == grammarAccess.getIncludeRule()) {
					sequence_Include(context, (Include) semanticObject); 
					return; 
				}
				else break;
			case TomDslPackage.JAVA_BODY:
				if(context == grammarAccess.getJavaBodyRule()) {
					sequence_JavaBody(context, (JavaBody) semanticObject); 
					return; 
				}
				else break;
			case TomDslPackage.OPERATION:
				if(context == grammarAccess.getArrayOperationRule() ||
				   context == grammarAccess.getOperationRule()) {
					sequence_Operation(context, (Operation) semanticObject); 
					return; 
				}
				else break;
			case TomDslPackage.OPERATION_ARRAY:
				if(context == grammarAccess.getArrayOperationRule() ||
				   context == grammarAccess.getOperationArrayRule()) {
					sequence_OperationArray(context, (OperationArray) semanticObject); 
					return; 
				}
				else break;
			case TomDslPackage.TOM_FILE:
				if(context == grammarAccess.getTomFileRule()) {
					sequence_TomFile(context, (TomFile) semanticObject); 
					return; 
				}
				else break;
			case TomDslPackage.TYPE_TERM:
				if(context == grammarAccess.getTypeTermRule()) {
					sequence_TypeTerm(context, (TypeTerm) semanticObject); 
					return; 
				}
				else break;
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Constraint:
	 *     (name=ID type=ID?)
	 */
	protected void sequence_ARG(EObject context, ARG semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (path+=ID | path+='/' | path+='.')+
	 */
	protected void sequence_Include(EObject context, Include semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     body=BRCKTSTMT
	 */
	protected void sequence_JavaBody(EObject context, JavaBody semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, TomDslPackage.Literals.JAVA_BODY__BODY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TomDslPackage.Literals.JAVA_BODY__BODY));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getJavaBodyAccess().getBodyBRCKTSTMTTerminalRuleCall_0(), semanticObject.getBody());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (term=ID name=ID (fsym=JavaBody | size=JavaBody | element=JavaBody | empty=JavaBody | append=JavaBody)+)
	 */
	protected void sequence_OperationArray(EObject context, OperationArray semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         term=ID 
	 *         name=ID 
	 *         (arg+=ARG arg+=ARG*)? 
	 *         fsym=JavaBody 
	 *         slot+=JavaBody* 
	 *         make=JavaBody
	 *     )
	 */
	protected void sequence_Operation(EObject context, Operation semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (ops+=ArrayOperation | terms+=TypeTerm | inc+=Include | locals=Local)*
	 */
	protected void sequence_TomFile(EObject context, TomFile semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID implement=JavaBody sort=JavaBody equals=JavaBody)
	 */
	protected void sequence_TypeTerm(EObject context, TypeTerm semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, TomDslPackage.Literals.TYPE_TERM__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TomDslPackage.Literals.TYPE_TERM__NAME));
			if(transientValues.isValueTransient(semanticObject, TomDslPackage.Literals.TYPE_TERM__IMPLEMENT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TomDslPackage.Literals.TYPE_TERM__IMPLEMENT));
			if(transientValues.isValueTransient(semanticObject, TomDslPackage.Literals.TYPE_TERM__SORT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TomDslPackage.Literals.TYPE_TERM__SORT));
			if(transientValues.isValueTransient(semanticObject, TomDslPackage.Literals.TYPE_TERM__EQUALS) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TomDslPackage.Literals.TYPE_TERM__EQUALS));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getTypeTermAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getTypeTermAccess().getImplementJavaBodyParserRuleCall_4_0(), semanticObject.getImplement());
		feeder.accept(grammarAccess.getTypeTermAccess().getSortJavaBodyParserRuleCall_7_0(), semanticObject.getSort());
		feeder.accept(grammarAccess.getTypeTermAccess().getEqualsJavaBodyParserRuleCall_14_0(), semanticObject.getEquals());
		feeder.finish();
	}
}
