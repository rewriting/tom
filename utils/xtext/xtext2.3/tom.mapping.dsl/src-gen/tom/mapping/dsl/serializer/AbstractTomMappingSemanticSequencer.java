package tom.mapping.dsl.serializer;

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
import tom.mapping.dsl.services.TomMappingGrammarAccess;
import tom.mapping.model.Accessor;
import tom.mapping.model.AliasOperator;
import tom.mapping.model.ClassOperator;
import tom.mapping.model.EnumLiteralValue;
import tom.mapping.model.FeatureException;
import tom.mapping.model.FeatureNode;
import tom.mapping.model.FeatureParameter;
import tom.mapping.model.Import;
import tom.mapping.model.JavaCodeValue;
import tom.mapping.model.Mapping;
import tom.mapping.model.ModelPackage;
import tom.mapping.model.Module;
import tom.mapping.model.OperatorNode;
import tom.mapping.model.Parameter;
import tom.mapping.model.SettedFeatureParameter;
import tom.mapping.model.Terminal;
import tom.mapping.model.UserOperator;

@SuppressWarnings("restriction")
public class AbstractTomMappingSemanticSequencer extends AbstractSemanticSequencer {

	@Inject
	protected TomMappingGrammarAccess grammarAccess;
	
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
		if(semanticObject.eClass().getEPackage() == ModelPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case ModelPackage.ACCESSOR:
				if(context == grammarAccess.getAccessorRule()) {
					sequence_Accessor(context, (Accessor) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.ALIAS_OPERATOR:
				if(context == grammarAccess.getAliasOperatorRule() ||
				   context == grammarAccess.getOperatorRule()) {
					sequence_AliasOperator(context, (AliasOperator) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.CLASS_OPERATOR:
				if(context == grammarAccess.getClassOperatorWithExceptionsRule()) {
					sequence_ClassOperatorWithExceptions(context, (ClassOperator) semanticObject); 
					return; 
				}
				else if(context == grammarAccess.getClassOperatorRule()) {
					sequence_ClassOperator(context, (ClassOperator) semanticObject); 
					return; 
				}
				else if(context == grammarAccess.getOperatorRule()) {
					sequence_Operator(context, (ClassOperator) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.ENUM_LITERAL_VALUE:
				if(context == grammarAccess.getLiteralValueRule() ||
				   context == grammarAccess.getSettedValueRule()) {
					sequence_LiteralValue(context, (EnumLiteralValue) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.FEATURE_EXCEPTION:
				if(context == grammarAccess.getFeatureExceptionRule() ||
				   context == grammarAccess.getFeatureParameterRule()) {
					sequence_FeatureException(context, (FeatureException) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.FEATURE_NODE:
				if(context == grammarAccess.getAliasNodeRule() ||
				   context == grammarAccess.getFeatureNodeRule()) {
					sequence_FeatureNode(context, (FeatureNode) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.FEATURE_PARAMETER:
				if(context == grammarAccess.getFeatureParameterRule()) {
					sequence_FeatureParameter(context, (FeatureParameter) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.IMPORT:
				if(context == grammarAccess.getImportRule()) {
					sequence_Import(context, (Import) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.JAVA_CODE_VALUE:
				if(context == grammarAccess.getJavaCodeValueRule() ||
				   context == grammarAccess.getSettedValueRule()) {
					sequence_JavaCodeValue(context, (JavaCodeValue) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.MAPPING:
				if(context == grammarAccess.getMappingRule()) {
					sequence_Mapping(context, (Mapping) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.MODULE:
				if(context == grammarAccess.getModuleRule()) {
					sequence_Module(context, (Module) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.OPERATOR_NODE:
				if(context == grammarAccess.getAliasNodeRule() ||
				   context == grammarAccess.getOperatorNodeRule()) {
					sequence_OperatorNode(context, (OperatorNode) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.PARAMETER:
				if(context == grammarAccess.getParameterRule()) {
					sequence_Parameter(context, (Parameter) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.SETTED_FEATURE_PARAMETER:
				if(context == grammarAccess.getFeatureParameterRule() ||
				   context == grammarAccess.getSettedFeatureParameterRule()) {
					sequence_SettedFeatureParameter(context, (SettedFeatureParameter) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.TERMINAL:
				if(context == grammarAccess.getTerminalRule()) {
					sequence_Terminal(context, (Terminal) semanticObject); 
					return; 
				}
				else break;
			case ModelPackage.USER_OPERATOR:
				if(context == grammarAccess.getOperatorRule() ||
				   context == grammarAccess.getUserOperatorRule()) {
					sequence_UserOperator(context, (UserOperator) semanticObject); 
					return; 
				}
				else break;
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Constraint:
	 *     (slot=[Parameter|ID] java=STRING)
	 */
	protected void sequence_Accessor(EObject context, Accessor semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.ACCESSOR__SLOT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.ACCESSOR__SLOT));
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.ACCESSOR__JAVA) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.ACCESSOR__JAVA));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getAccessorAccess().getSlotParameterIDTerminalRuleCall_1_0_1(), semanticObject.getSlot());
		feeder.accept(grammarAccess.getAccessorAccess().getJavaSTRINGTerminalRuleCall_3_0(), semanticObject.getJava());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID op=[Operator|ID] nodes+=AliasNode nodes+=AliasNode*)
	 */
	protected void sequence_AliasOperator(EObject context, AliasOperator semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID class=[EClass|FQN] parameters+=FeatureParameter parameters+=FeatureParameter*)
	 */
	protected void sequence_ClassOperatorWithExceptions(EObject context, ClassOperator semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID class=[EClass|FQN])
	 */
	protected void sequence_ClassOperator(EObject context, ClassOperator semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     feature=[EStructuralFeature|ID]
	 */
	protected void sequence_FeatureException(EObject context, FeatureException semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.FEATURE_PARAMETER__FEATURE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.FEATURE_PARAMETER__FEATURE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getFeatureExceptionAccess().getFeatureEStructuralFeatureIDTerminalRuleCall_1_0_1(), semanticObject.getFeature());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     feature=ID
	 */
	protected void sequence_FeatureNode(EObject context, FeatureNode semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.FEATURE_NODE__FEATURE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.FEATURE_NODE__FEATURE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getFeatureNodeAccess().getFeatureIDTerminalRuleCall_0(), semanticObject.getFeature());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     feature=[EStructuralFeature|ID]
	 */
	protected void sequence_FeatureParameter(EObject context, FeatureParameter semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.FEATURE_PARAMETER__FEATURE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.FEATURE_PARAMETER__FEATURE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getFeatureParameterAccess().getFeatureEStructuralFeatureIDTerminalRuleCall_0_0_1(), semanticObject.getFeature());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     importURI=STRING
	 */
	protected void sequence_Import(EObject context, Import semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.IMPORT__IMPORT_URI) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.IMPORT__IMPORT_URI));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getImportAccess().getImportURISTRINGTerminalRuleCall_0(), semanticObject.getImportURI());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     java=STRING
	 */
	protected void sequence_JavaCodeValue(EObject context, JavaCodeValue semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.JAVA_CODE_VALUE__JAVA) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.JAVA_CODE_VALUE__JAVA));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getJavaCodeValueAccess().getJavaSTRINGTerminalRuleCall_0(), semanticObject.getJava());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     literal=[EEnumLiteral|FQN]
	 */
	protected void sequence_LiteralValue(EObject context, EnumLiteralValue semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.ENUM_LITERAL_VALUE__LITERAL) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.ENUM_LITERAL_VALUE__LITERAL));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getLiteralValueAccess().getLiteralEEnumLiteralFQNParserRuleCall_0_1(), semanticObject.getLiteral());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         name=ID 
	 *         prefix=STRING? 
	 *         imports+=Import* 
	 *         (
	 *             ((terminals+=Terminal terminals+=Terminal*)? (externalTerminals+=[Terminal|ID] externalTerminals+=[Terminal|ID]*)?) | 
	 *             (terminals+=Terminal terminals+=Terminal*)
	 *         )? 
	 *         ((operators+=Operator operators+=Operator*) | modules+=Module)*
	 *     )
	 */
	protected void sequence_Mapping(EObject context, Mapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID (operators+=Operator operators+=Operator*)?)
	 */
	protected void sequence_Module(EObject context, Module semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (op=[Operator|ID] nodes+=AliasNode nodes+=AliasNode*)
	 */
	protected void sequence_OperatorNode(EObject context, OperatorNode semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     ((name=ID class=[EClass|FQN]) | (name=ID class=[EClass|FQN] parameters+=FeatureParameter parameters+=FeatureParameter*))
	 */
	protected void sequence_Operator(EObject context, ClassOperator semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (type=[Terminal|ID] name=EString)
	 */
	protected void sequence_Parameter(EObject context, Parameter semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.PARAMETER__TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.PARAMETER__TYPE));
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.PARAMETER__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.PARAMETER__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getParameterAccess().getTypeTerminalIDTerminalRuleCall_0_0_1(), semanticObject.getType());
		feeder.accept(grammarAccess.getParameterAccess().getNameEStringParserRuleCall_1_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (feature=[EStructuralFeature|ID] value=SettedValue)
	 */
	protected void sequence_SettedFeatureParameter(EObject context, SettedFeatureParameter semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.FEATURE_PARAMETER__FEATURE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.FEATURE_PARAMETER__FEATURE));
			if(transientValues.isValueTransient(semanticObject, ModelPackage.Literals.SETTED_FEATURE_PARAMETER__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ModelPackage.Literals.SETTED_FEATURE_PARAMETER__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getSettedFeatureParameterAccess().getFeatureEStructuralFeatureIDTerminalRuleCall_0_0_1(), semanticObject.getFeature());
		feeder.accept(grammarAccess.getSettedFeatureParameterAccess().getValueSettedValueParserRuleCall_2_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID class=[EClass|FQN] many?='[]'?)
	 */
	protected void sequence_Terminal(EObject context, Terminal semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         name=ID 
	 *         parameters+=Parameter 
	 *         parameters+=Parameter* 
	 *         type=[Terminal|ID] 
	 *         accessors+=Accessor 
	 *         accessors+=Accessor* 
	 *         make=STRING 
	 *         test=STRING
	 *     )
	 */
	protected void sequence_UserOperator(EObject context, UserOperator semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
}
