


























package tom.emf;



import java.io.PrintStream;
import java.io.File;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;



import java.lang.reflect.TypeVariable;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.Map.Entry;



import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;










public class TomMappingFromEcore {

  
  private final static ArrayList<java.lang.Class<?>> lists = new ArrayList<java.lang.Class<?>>();

  
  private static boolean append = false;

  
  private static boolean useNewTyper = false;

  
  private static boolean genEcoreMapping = false;
  
  
  private static boolean genUML2Mapping = false;

  
  private final static HashMap<java.lang.Class<?>, String> types = new HashMap<java.lang.Class<?>, String>();

  
  private static final Set<String> includedMappings = new HashSet<String>();

  
  private static String prefix = "";

  
  public static List<String> processArgs(String[] args) {
    List<String> ePackageNameList = new ArrayList<String>();
    
    
    int i = 0;
    String argument = "";
    try {
      for(; i < args.length; i++) {
        argument = args[i];

        if(!argument.startsWith("-") || (argument.equals("-"))) {
          
          ePackageNameList.add(argument);
        } else {
          
          argument = argument.substring(1); 
          if(argument.startsWith("-")) {
            
            argument = argument.substring(1); 
          }
          
          
          if(argument.equals("X") || argument.equals("cp")) {
            
            i++;
            continue;
          }
          if(argument.equals("nt") || argument.equals("subtype")) {
            useNewTyper = true;
          }
          if(argument.equals("prefix")) {
            prefix = args[++i]; 
          }
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      System.err.println("Expecting information after option "+argument);
      return null;
    }
    
    if(ePackageNameList.isEmpty()) {
      
      System.err.println("No EPackage has been given.");
      displayHelp();
      return null;
    }
    return ePackageNameList;
  }

  private static void displayHelp() {
    System.out.println("usage: emf-generate-mappings [options] <EPackageClassName>");
    System.out.println("options:");
        System.out.println("  -nt|-subtype (generates constructs with subtyping)");
        System.out.println("  -prefix (generates mappings with a prefix)");
        System.out.println("  any Java options");
  }

  public static void main(String[] args) {
    
      if(args.length < 1) {
        displayHelp();
        
      } else {
        
        List<String> ePackageNameList = processArgs(args);
        if(ePackageNameList == null) {
          throw new RuntimeException("No EPackage has been given as argument!");
        }

        try {
          
          for(String epn : ePackageNameList) {
            
            
            File output = new File(".",epn+".tom");
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
            
            
            
            
            if(epn.equals("org.eclipse.emf.ecore.EcorePackage")) {
              genEcoreMapping = true;
            } else if(epn.equals("org.eclipse.uml2.uml.UMLPackage")) {
              genUML2Mapping = true;
            }
            extractFromEPackage(writer, (EPackage) java.lang.Class.forName(epn).getField("eINSTANCE").get(null));
            writer.flush();
            writer.close();
            
            
            genEcoreMapping = false;
            genUML2Mapping = false;
          }
          

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
  }

  private static void genLicence(java.io.Writer writer) throws java.io.IOException {
    writer.write("/*\n *\n * TOM - To One Matching Compiler\n *\n * Copyright (c) 2009-2017, Universite de Lorraine, Inria\n * Nancy, France.\n *\n * This program is free software; you can redistribute it and/or modify\n * it under the terms of the GNU General Public License as published by\n * the Free Software Foundation; either version 2 of the License, or\n * (at your option) any later version.\n *\n * This program is distributed in the hope that it will be useful,\n * but WITHOUT ANY WARRANTY; without even the implied warranty of\n * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n * GNU General Public License for more details.\n *\n * You should have received a copy of the GNU General Public License\n * along with this program; if not, write to the Free Software\n * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA\n *\n *\n **/"





















);
  }

  
  private static void extractType(java.io.Writer writer, EClassifier eclf) throws java.io.IOException {
    java.lang.Class<?> c = eclf.getInstanceClass();
    if(!types.containsKey(c)) {
      String n = (c.isArray()?(c.getComponentType().getSimpleName()+"array"):c.getSimpleName());
      String is = "";
      for(int i = 1; types.containsValue(n + is); i++) {
        is = String.valueOf(i);
      }
      n = n + is;
      types.put(c, n);
      String type = tomTypes.get(c);
      if(tomTypes.containsKey(c)) {
        
        if(!includedMappings.contains(type)){
          includedMappings.add(type);
          writer.write("\n\n%include { " + type + ".tom }");
        }
      } else if(tomEMFTypes.contains(c) && !genEcoreMapping) {
        
        if(!includedMappings.contains("emf/ecore.tom")) {
          includedMappings.add("emf/ecore.tom");
          writer.write("\n\n%include { emf/ecore.tom }");
        }
        
        
      } else if(tomUML2Types.contains(c) && !genUML2Mapping) {
        
        if(!includedMappings.contains("emf/uml2.tom")) {
          includedMappings.add("emf/uml2.tom");
          writer.write("\n\n%include { emf/uml2.tom }");
        } 
        
        
      } else {
        String[] decl = getClassDeclarations(eclf); 
        String implementBody = (eclf instanceof EClass && !EObject.class.isAssignableFrom(c))? "org.eclipse.emf.ecore.EObject" : decl[0] + decl[2];

        writer.write("\n\n%typeterm "+prefix+n+" "+(useNewTyper?genSubtype(eclf):"")+" {\n  implement { "+implementBody+" }\n  is_sort(t) { "+(c.isPrimitive() ? "true" : "$t instanceof " + decl[0] + decl[1])+" }\n  equals(l1,l2) { "+(c.isPrimitive() ? "$l1 == $l2" : "$l1.equals($l2)")+" }\n}"





);
      }
    }
  }

  private static String genSubtype(EClassifier eclf) {
    String result = "";
    if((eclf instanceof EClass)) {
      if(!((EClass)eclf).getESuperTypes().isEmpty()) {
        
        
        
        
        
        for (EClass supertype:((EClass)eclf).getESuperTypes()) {
          String superName = supertype.getName();
          result = result + " extends "+((isEcoreType(superName)||isUML2Type(superName))?superName:(prefix+superName));
          
        }
      } else {
        if(!(eclf.getInstanceClassName().equals("EObject"))) {
          result = result + " extends " + "EObject";
        }
      }
    }
    return result;
  }

  
  private final static HashMap<java.lang.Class<?>, String> tomTypes = new HashMap<java.lang.Class<?>, String>();
  static {
    tomTypes.put(boolean.class, "boolean");
    tomTypes.put(char.class, "char");
    tomTypes.put(double.class, "double");
    tomTypes.put(float.class, "float");
    tomTypes.put(int.class, "int");
    tomTypes.put(long.class, "long");
    tomTypes.put(int[].class, "intarray");
    tomTypes.put(String.class, "string");
    tomTypes.put(ArrayList.class, "util/ArrayList");
    tomTypes.put(HashMap.class, "util/HashMap");
    tomTypes.put(HashSet.class, "util/HashSet");
    tomTypes.put(LinkedList.class, "util/LinkedList");
    tomTypes.put(Object.class, "util/Object");
    tomTypes.put(TreeMap.class, "util/TreeMap");
    tomTypes.put(Map.Entry.class, "util/MapEntry");
    tomTypes.put(TreeSet.class, "util/TreeSet");
    tomTypes.put(AbstractCollection.class, "util/types/AbstractCollection");
    tomTypes.put(AbstractList.class, "util/types/AbstractList");
    tomTypes.put(AbstractSequentialList.class, "util/types/AbstractSequentialList");
    tomTypes.put(AbstractSet.class, "util/types/AbstractSet");
    tomTypes.put(Collection.class, "util/types/Collection");
    tomTypes.put(LinkedHashSet.class, "util/types/LinkedHashSet");
    tomTypes.put(List.class, "util/types/List");
    tomTypes.put(Map.class, "util/types/Map");
    tomTypes.put(Set.class, "util/types/Set");
    tomTypes.put(Stack.class, "util/types/Stack");
    tomTypes.put(Vector.class, "util/types/Vector");
    tomTypes.put(WeakHashMap.class, "util/types/WeakHashMap");
  }

  
  private final static HashSet<java.lang.Class<?>> tomEMFTypes = new HashSet<java.lang.Class<?>>();
  static {
    tomEMFTypes.add(org.eclipse.emf.ecore.EAttribute.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EAnnotation.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EStructuralFeature.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EObject.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EDataType.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EModelElement.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EEnumLiteral.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EClassifier.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.ETypeParameter.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EGenericType.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EReference.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EPackage.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EClass.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EParameter.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EOperation.class);
    tomEMFTypes.add(org.eclipse.emf.common.util.EList.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.ETypedElement.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EFactory.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.EEnum.class);
    tomEMFTypes.add(org.eclipse.emf.common.util.Enumerator.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.ENamedElement.class);
    tomEMFTypes.add(java.math.BigInteger.class);
    tomEMFTypes.add(java.math.BigDecimal.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.resource.ResourceSet.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.util.FeatureMap.Entry.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.util.FeatureMap.class);
    tomEMFTypes.add(org.eclipse.emf.common.util.TreeIterator.class);
    tomEMFTypes.add(org.eclipse.emf.common.util.DiagnosticChain.class);
    tomEMFTypes.add(org.eclipse.emf.ecore.resource.Resource.class);
    tomEMFTypes.add(byte.class);
    tomEMFTypes.add(java.util.Date.class);
    tomEMFTypes.add(java.lang.Class.class);
    tomEMFTypes.add(java.util.Map.Entry.class);
  }

  
 private final static HashSet<java.lang.Class<?>> tomUML2Types = new HashSet<java.lang.Class<?>>();
  static {
    tomUML2Types.add(org.eclipse.uml2.uml.Comment.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Element.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Package.class);
    tomUML2Types.add(org.eclipse.uml2.uml.VisibilityKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Dependency.class);
    tomUML2Types.add(org.eclipse.uml2.uml.StringExpression.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TemplateParameter.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TemplateSignature.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TemplateableElement.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TemplateBinding.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TemplateParameterSubstitution.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ParameterableElement.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Type.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ValueSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.NamedElement.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ElementImport.class);
    tomUML2Types.add(org.eclipse.uml2.uml.PackageableElement.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Namespace.class);
    tomUML2Types.add(org.eclipse.uml2.uml.PackageImport.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Constraint.class);
    tomUML2Types.add(org.eclipse.uml2.uml.PackageMerge.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ProfileApplication.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Profile.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Stereotype.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Generalization.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Classifier.class);
    tomUML2Types.add(org.eclipse.uml2.uml.GeneralizationSet.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Substitution.class);
    tomUML2Types.add(org.eclipse.uml2.uml.OpaqueExpression.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Behavior.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CollaborationUse.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Collaboration.class);
    tomUML2Types.add(org.eclipse.uml2.uml.UseCase.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InterfaceRealization.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Interface.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Property.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ConnectorEnd.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ConnectableElement.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Deployment.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DeployedArtifact.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DeploymentSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Artifact.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Manifestation.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Operation.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Parameter.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ParameterSet.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ParameterDirectionKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ParameterEffectKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CallConcurrencyKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Class.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Connector.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Association.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ConnectorKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Port.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DataType.class);
    tomUML2Types.add(org.eclipse.uml2.uml.AggregationKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ProtocolStateMachine.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Trigger.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Event.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Reception.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Signal.class);
    tomUML2Types.add(org.eclipse.uml2.uml.BehavioralFeature.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Region.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Vertex.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Transition.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TransitionKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.State.class);
    tomUML2Types.add(org.eclipse.uml2.uml.StateMachine.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Pseudostate.class);
    tomUML2Types.add(org.eclipse.uml2.uml.PseudostateKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ConnectionPointReference.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ProtocolConformance.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DeploymentTarget.class);
    tomUML2Types.add(org.eclipse.uml2.uml.BehavioredClassifier.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Include.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Extend.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExtensionPoint.class); 
    tomUML2Types.add(org.eclipse.uml2.uml.Image.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DirectedRelationship.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Relationship.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TypedElement.class);
    tomUML2Types.add(org.eclipse.uml2.uml.RedefinableElement.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Feature.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Realization.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Abstraction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.MultiplicityElement.class);
    tomUML2Types.add(org.eclipse.uml2.uml.EncapsulatedClassifier.class);
    tomUML2Types.add(org.eclipse.uml2.uml.StructuredClassifier.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Extension.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExtensionEnd.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Model.class);
    tomUML2Types.add(org.eclipse.uml2.uml.OperationTemplateParameter.class);
    tomUML2Types.add(org.eclipse.uml2.uml.StructuralFeature.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ConnectableElementTemplateParameter.class);
    tomUML2Types.add(org.eclipse.uml2.uml.RedefinableTemplateSignature.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ClassifierTemplateParameter.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Expression.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Usage.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Enumeration.class);
    tomUML2Types.add(org.eclipse.uml2.uml.EnumerationLiteral.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Slot.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InstanceSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.PrimitiveType.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LiteralSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LiteralInteger.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LiteralString.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LiteralBoolean.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LiteralNull.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InstanceValue.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LiteralUnlimitedNatural.class);
    tomUML2Types.add(org.eclipse.uml2.uml.OpaqueBehavior.class);
    tomUML2Types.add(org.eclipse.uml2.uml.FunctionBehavior.class);
    tomUML2Types.add(org.eclipse.uml2.uml.OpaqueAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.StructuredActivityNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Activity.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Variable.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ActivityNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ActivityEdge.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ActivityPartition.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InterruptibleActivityRegion.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ActivityGroup.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExceptionHandler.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExecutableNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ObjectNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ObjectNodeOrderingKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InputPin.class);
    tomUML2Types.add(org.eclipse.uml2.uml.OutputPin.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Action.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Pin.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CallAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InvocationAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.SendSignalAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CallOperationAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CallBehaviorAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.SequenceNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ControlNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ControlFlow.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InitialNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ActivityParameterNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ValuePin.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Message.class);
    tomUML2Types.add(org.eclipse.uml2.uml.MessageSort.class);
    tomUML2Types.add(org.eclipse.uml2.uml.MessageEnd.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Interaction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Lifeline.class);
    tomUML2Types.add(org.eclipse.uml2.uml.PartDecomposition.class);
    tomUML2Types.add(org.eclipse.uml2.uml.GeneralOrdering.class);
    tomUML2Types.add(org.eclipse.uml2.uml.OccurrenceSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InteractionOperand.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InteractionConstraint.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InteractionFragment.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Gate.class);
    tomUML2Types.add(org.eclipse.uml2.uml.MessageKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InteractionUse.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExecutionSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.StateInvariant.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ActionExecutionSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.BehaviorExecutionSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExecutionEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CreationEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DestructionEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.SendOperationEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.MessageEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.SendSignalEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.MessageOccurrenceSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExecutionOccurrenceSpecification.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReceiveOperationEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReceiveSignalEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Actor.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CallEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ChangeEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.SignalEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.AnyReceiveEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ForkNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.FlowFinalNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.FinalNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CentralBufferNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.MergeNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DecisionNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ObjectFlow.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ActivityFinalNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ComponentRealization.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Component.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Node.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CommunicationPath.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Device.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExecutionEnvironment.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CombinedFragment.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InteractionOperatorKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Continuation.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ConsiderIgnoreFragment.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CreateObjectAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DestroyObjectAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TestIdentityAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReadSelfAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.StructuralFeatureAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReadStructuralFeatureAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.WriteStructuralFeatureAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ClearStructuralFeatureAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.RemoveStructuralFeatureValueAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.AddStructuralFeatureValueAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LinkAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LinkEndData.class);
    tomUML2Types.add(org.eclipse.uml2.uml.QualifierValue.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReadLinkAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LinkEndCreationData.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CreateLinkAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.WriteLinkAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DestroyLinkAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LinkEndDestructionData.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ClearAssociationAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.BroadcastSignalAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.SendObjectAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ValueSpecificationAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TimeExpression.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Observation.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Duration.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DurationInterval.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Interval.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TimeConstraint.class);
    tomUML2Types.add(org.eclipse.uml2.uml.IntervalConstraint.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TimeInterval.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DurationConstraint.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TimeObservation.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DurationObservation.class);
    tomUML2Types.add(org.eclipse.uml2.uml.FinalState.class);
    tomUML2Types.add(org.eclipse.uml2.uml.TimeEvent.class);
    tomUML2Types.add(org.eclipse.uml2.uml.VariableAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReadVariableAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.WriteVariableAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ClearVariableAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.AddVariableValueAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.RemoveVariableValueAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.RaiseExceptionAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ActionInputPin.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InformationItem.class);
    tomUML2Types.add(org.eclipse.uml2.uml.InformationFlow.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReadExtentAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReclassifyObjectAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReadIsClassifiedObjectAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.StartClassifierBehaviorAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReadLinkObjectEndAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReadLinkObjectEndQualifierAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.CreateLinkObjectAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.AcceptEventAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.AcceptCallAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReplyAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.UnmarshallAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ReduceAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.StartObjectBehaviorAction.class);
    tomUML2Types.add(org.eclipse.uml2.uml.JoinNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.DataStoreNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ConditionalNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.Clause.class);
    tomUML2Types.add(org.eclipse.uml2.uml.LoopNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExpansionNode.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExpansionRegion.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ExpansionKind.class);
    tomUML2Types.add(org.eclipse.uml2.uml.ProtocolTransition.class);
    tomUML2Types.add(org.eclipse.uml2.uml.AssociationClass.class);
  }

  
  private final static List<String> keywords = java.util.Arrays.asList(new
      String[] { "abstract", "continue", "for", "new", "switch", "assert",
      "default", "goto", "package", "synchronized", "boolean", "do", "if",
      "private", "this", "break", "double", "implements", "protected", "throw",
      "byte", "else", "import", "public", "throws", "case", "enum",
      "instanceof", "return", "transient", "catch", "extends", "int", "short",
      "try", "char", "final", "interface", "static", "void", "class", "finally",
      "long", "strictfp", "volatile", "const", "float", "native", "super",
      "while", "true", "false", "null" });

  
  private static String[] getClassDeclarations(EClassifier ec) {
    java.lang.Class<?> c = ec.getInstanceClass();
    String cn = c.getCanonicalName();
    StringBuffer ca = new StringBuffer();
    StringBuffer cb = new StringBuffer();
    TypeVariable<?>[] tps = c.getTypeParameters();
    if(tps.length > 0) {
      ca.append("<");
      for(int i = 0; i < tps.length; i++) {
        if(i != 0) {
          ca.append(", ");
        }
        ca.append("?");
      }
      ca.append(">");
      if(c == Entry.class && ec instanceof EClass) {
        cb.append("<");
        EList<EAttribute> ats = ((EClass) ec).getEAttributes();
        ListIterator<EAttribute> it = ats.listIterator();
        for(int i = 0; i < tps.length; i++) {
          if(i != 0) {
            cb.append(", ");
          }
          cb.append(it.next().getEAttributeType().getInstanceClass()
              .getCanonicalName());
        }
        cb.append(">");
      } else {
        cb = ca;
      }
    }
    return new String[] { cn, ca.toString(), cb.toString() };
  }

  
  private static String getType(EStructuralFeature sf) throws java.io.IOException {
    java.lang.Class<?> c = sf.getEType().getInstanceClass();
    return types.get(c);
  }

  
  private static String getType(java.io.Writer writer, EStructuralFeature sf) throws java.io.IOException {
    java.lang.Class<?> c = sf.getEType().getInstanceClass();
    
    boolean boolCondition = (isEcoreType(sf) || isUML2Type(sf));
    String argname = (isBuiltin(sf)||boolCondition)?types.get(c):(prefix+types.get(c));
    String name = argname;
    
    
    if(sf.isMany()) {
      name += "EList";
      if(!lists.contains(c)) {
        String[] decl = getClassDeclarations(sf.getEType()); 
        
        String inst = "";
        if(EObject.class.isAssignableFrom(c)) {
          inst = decl[0] + decl[2];
        } else if(!c.isInterface()) {
          inst = "java.lang.Object";
        } else {
          inst = "org.eclipse.emf.ecore.EObject";
        }
        if(!boolCondition && !name.equals("StringEList")) {
        writer.write("\n\n%typeterm "+name+" {\n  implement { org.eclipse.emf.common.util.EList<"+inst+"> }\n  is_sort(t) { $t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<"+inst+">)$t).size() == 0 || (((org.eclipse.emf.common.util.EList<"+inst+">)$t).size()>0 && ((org.eclipse.emf.common.util.EList<"+inst+">)$t).get(0) instanceof "+(decl[0]+decl[1])+")) }\n  equals(l1,l2) { $l1.equals($l2) }\n}\n\n%oparray "+name+" "+name+" ( "+argname+"* ) {\n  is_fsym(t) { $t instanceof org.eclipse.emf.common.util.EList<?> && ($t.size() == 0 || ($t.size()>0 && $t.get(0) instanceof "+(decl[0]+decl[1])+")) }\n  make_empty(n) { new org.eclipse.emf.common.util.BasicEList<"+inst+">($n) }\n  make_append(e,l) { append"+name+"($e,$l) }\n  get_element(l,n) { $l.get($n) }\n  get_size(l)      { $l.size() }\n}\n\nprivate static <O> org.eclipse.emf.common.util.EList<O> append"+name+"(O e,org.eclipse.emf.common.util.EList<O> l) {\n  l.add(e);\n  return l;\n}"


















);
        lists.add(c);
        }
      } else if(name.equals("StringEList")) {
        writer.write("\n\n%include{ emf/StringEList.tom }\n"


);
        includedMappings.add("emf/StringEList.tom");
      }
    }
    
    return name;
  }

  
  private static void extractFromEPackage(java.io.Writer writer, EPackage p) throws java.io.IOException {
    for(EClassifier eclf : p.getEClassifiers()) {
      extractFromEClassifier(writer,eclf);
    }
    for(EPackage ep : p.getESubpackages()) {
      extractFromEPackage(writer,ep);
    }
  }

  
  private final static HashMap<java.lang.Class<?>, java.lang.Class<?>> primitiveToBoxed = new HashMap<java.lang.Class<?>, java.lang.Class<?>>();
  static {
    primitiveToBoxed.put(Boolean.TYPE, Boolean.class);
    primitiveToBoxed.put(Byte.TYPE, Byte.class);
    primitiveToBoxed.put(Short.TYPE, Short.class);
    primitiveToBoxed.put(Character.TYPE, Character.class);
    primitiveToBoxed.put(Integer.TYPE, Integer.class);
    primitiveToBoxed.put(Long.TYPE, Long.class);
    primitiveToBoxed.put(Float.TYPE, Float.class);
    primitiveToBoxed.put(Double.TYPE, Double.class);
  }

  
  private static java.lang.Class<?> boxType(java.lang.Class<?> primitiveType) {
    java.lang.Class<?> boxedType = (java.lang.Class<?>) primitiveToBoxed.get(primitiveType);
    if(boxedType != null) {
      return boxedType;
    } else {
      return primitiveType;
    }
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

  
  private final static ArrayList<EClassifier> classifiers = new ArrayList<EClassifier>();

  
  private static String toUpperName(String name) {
    if(name.length() < 1) {
      throw new IllegalArgumentException(
          "Name must contains at least one character.");
    }
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  
  private static String genGetDefault(EStructuralFeature esf) throws java.io.IOException {
    String result = "";
    String dvalue = ""+esf.getDefaultValue();
    String esftype = getType(esf);
   
    
    if(esftype.equals("String")) {
      dvalue = "\"" + dvalue + "\"";
    } else if(esftype.equals("boolean") || esftype.equals("int") ||
        esftype.equals("float") || esftype.equals("double") || 
        esftype.equals("long")) {
      
      
    } else {
      dvalue = "`" + prefix + esftype + dvalue + "()";
    }
    
    String sfname = (keywords.contains(esf.getName()) ? "_" : "")+esf.getName();
    return result+"\n  get_default("+sfname+") { "+dvalue+" }";
  }

  
  private static void extractFromEClassifier(java.io.Writer writer, EClassifier eclf) throws java.io.IOException {
    if(!classifiers.contains(eclf)) {
      extractType(writer,eclf);
      classifiers.add(eclf);
      
      java.lang.Class<?> c = eclf.getInstanceClass();
      boolean isUML2orEMF = (tomEMFTypes.contains(c)||tomUML2Types.contains(c));
      if(eclf instanceof EClass) {
        EClass ecl = (EClass) eclf;
        EList<EStructuralFeature> sfs = ecl.getEAllStructuralFeatures();
        StringBuffer s_types = new StringBuffer();
        
        StringBuffer s = new StringBuffer();
        StringBuffer s2 = new StringBuffer();
        StringBuffer s_gets = new StringBuffer();
        StringBuffer s_defaults = new StringBuffer();
        for(EStructuralFeature sf : sfs) {
          if(sf.isChangeable()) {
            extractFromEClassifier(writer, sf.getEType());
            EClassifier type = sf.getEType();
            String sfname = (keywords.contains(sf.getName()) ? "_" : "")
                + sf.getName();
            s_types.append(sfname + " : " + getType(writer,sf) + ", ");
            String[] decl = getClassDeclarations(type); 
            writer.write("");
            
            s.append(", " + sfname);
            s2.append(", $" + sfname);
            String na = boxType(sf.getEType().getInstanceClass())
                .getCanonicalName();
            if(type instanceof EClass
                && !EObject.class.isAssignableFrom(type.getInstanceClass())) {
              na = "org.eclipse.emf.ecore.EObject";
            }
            if(sf.isMany()) {
              na = "org.eclipse.emf.common.util.EList<" + na + ">";
            }
            s_gets.append("\n  get_slot(" + sfname + ", t) { (" + na
                + ")$t.eGet($t.eClass().getEStructuralFeature(\"" + sf.getName()
                + "\")) }");
            if(sf.getDefaultValue()!=null) {
              s_defaults.append(genGetDefault(sf));
            }
          } 
        }
        if(s_types.length() >= 2) {
          s_types.delete(s_types.length() - 2, s_types.length());
        }
        if(!ecl.isAbstract()) {
          String cr = eclf.getName();
          String[] decl = getClassDeclarations(eclf); 
          String o1 = ecl.getEPackage().getEFactoryInstance().getClass()
            .getInterfaces()[ecl.getEPackage().getClass()
            .getInterfaces().length - 1].getCanonicalName();
          String o2 = ecl.getEPackage().getClass().getInterfaces()[ecl.getEPackage().getClass().getInterfaces().length - 1]
            .getCanonicalName();
          
          
          
          
          
          if(genEcoreMapping || genUML2Mapping || !isUML2orEMF) {
            
            
            
            
            
            
            
            if (genEcoreMapping && tomUML2Types.contains(c)) {
              if (!includedMappings.contains("emf/uml2.tom")) {
                writer.write(" \n                  %include { emf/uml2.tom } \n                  "

);
                includedMappings.add("emf/uml2.tom");
              }
            
            } else if (genUML2Mapping && tomEMFTypes.contains(c)) {
              if (!includedMappings.contains("emf/ecore.tom")) {
                writer.write(" \n                  %include { emf/ecore.tom } \n                  "

);
                includedMappings.add("emf/ecore.tom");
              }
            
            } else {
              writer.write("\n\n%op "+(prefix+ecl.getInstanceClass().getSimpleName())+" "+(prefix+cr)+"("+s_types+") {\n  is_fsym(t) { $t instanceof "+(decl[0]+decl[1])+" }"+s_gets+" "+s_defaults+"\n  make("+(s.length() <= 2 ? "" : s.substring(2))+") { construct"+(prefix+cr)+"(("+(EObject.class.isAssignableFrom(ecl.getInstanceClass()) ? ecl.getInstanceClass().getCanonicalName() : "org.eclipse.emf.ecore.EObject")+")"+o1+".eINSTANCE.create((EClass)"+o2+".eINSTANCE.getEClassifier(\""+ecl.getName()+"\")), new Object[]{ "+(s2.length() <= 2 ? "" : s2.substring(2))+" }) }\n  implement() { "+genOpImplementContent(ecl.getInstanceClassName(), cr)+" }\n}\n\npublic static <O extends org.eclipse.emf.ecore.EObject> O construct"+(prefix+cr)+"(O o, Object[] objs) {\n  int i=0;\n  EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();\n  for(EStructuralFeature esf : sfes) {\n    if(esf.isChangeable()) {\n      o.eSet(esf, objs[i]);\n      i++;\n    }\n  }\n  return o;\n}"

















);
          }
        }
        }
      } else if(eclf instanceof EEnum) {
        EEnum en = (EEnum) eclf;
        String cr = eclf.getName();
        String[] decl = getClassDeclarations(eclf); 
          
        if(genEcoreMapping || genUML2Mapping || !isUML2orEMF) {
          for(EEnumLiteral lit : en.getELiterals()) {
            String o1 = eclf.getEPackage().getEFactoryInstance().getClass()
              .getInterfaces()[eclf.getEPackage().getClass()
              .getInterfaces().length - 1].getCanonicalName();
            String o2 = eclf.getEPackage().getClass().getInterfaces()[eclf
              .getEPackage().getClass().getInterfaces().length - 1]
              .getCanonicalName();
            
            
            
            String literalname = lit.getName();
            String operatorName = cr+literalname.replaceAll(" ","");
            writer.write("\n\n%op "+(prefix+cr)+" "+(prefix+operatorName)+"() {\n  is_fsym(t) { t == "+(decl[0]+decl[1])+".get(\""+literalname+"\") }\n  make() { ("+(decl[0]+decl[2])+")"+o1+".eINSTANCE.createFromString((EDataType)"+o2+".eINSTANCE.get"+toUpperName(cr)+"(), \""+literalname+"\") }\n}"




);
          }
        }
      }
    }
  }
  
  
  private static String genOpImplementContent(String eclname, String cr) {
    
    
    
    
    
    
    String fqnPrefix;
    if (cr.equals("EStringToStringMapEntry")) {
      
      fqnPrefix = "org.eclipse.emf.ecore.";
    } else {
      
      fqnPrefix = eclname.substring(0, eclname.lastIndexOf(cr));
    }
    
    
    return fqnPrefix+"impl."+cr+"Impl";
  }

  
  private static final Set<String> builtinSet = new HashSet<String>();
  static {
    builtinSet.add("String");
    builtinSet.add("EString");
    builtinSet.add("Boolean");
    builtinSet.add("Byte");
    builtinSet.add("Short");
    builtinSet.add("Character");
    builtinSet.add("Integer");
    builtinSet.add("Long");
    builtinSet.add("Float");
    builtinSet.add("Double");
    builtinSet.add("boolean");
    builtinSet.add("int");
    builtinSet.add("byte");
    builtinSet.add("short");
    builtinSet.add("char");
    builtinSet.add("long");
    builtinSet.add("float");
    builtinSet.add("double");
  }

  
  private static boolean isBuiltin(EStructuralFeature sf) {
    String typename = sf.getEType().getName();
    return builtinSet.contains(typename);
  }

  
  private static boolean isBuiltin(String typename) {
    return builtinSet.contains(typename);
  }

  
  private static final Set<String> ecoreSet = new HashSet<String>();
  static {
    ecoreSet.add("EAttribute");
    ecoreSet.add("EAnnotation");
    ecoreSet.add("EModelElement"); 
    ecoreSet.add("EStructuralFeature");
    ecoreSet.add("EObject");
    ecoreSet.add("EDataType");
    ecoreSet.add("EEnumLiteral");
    ecoreSet.add("EClassifier");
    ecoreSet.add("ETypeParameter");
    ecoreSet.add("EGenericType");
    ecoreSet.add("EReference");
    ecoreSet.add("EPackage");
    ecoreSet.add("EClass");
    ecoreSet.add("EParameter");
    ecoreSet.add("EOperation");
    ecoreSet.add("EList");
    ecoreSet.add("ETypedElement");
    ecoreSet.add("EFactory");
    ecoreSet.add("EEnum");
    ecoreSet.add("Enumerator");
    ecoreSet.add("ENamedElement");
    ecoreSet.add("BigInteger");
    ecoreSet.add("BigDecimal");
    ecoreSet.add("ResourceSet");
    ecoreSet.add("Entry");
    ecoreSet.add("EStringToStringMapEntry");
    
    
    
    
    ecoreSet.add("FeatureMap");
    ecoreSet.add("TreeIterator");
    ecoreSet.add("DiagnosticChain");
    ecoreSet.add("Resource");
    ecoreSet.add("Date");
  }

  
  private static boolean isEcoreType(EStructuralFeature sf) {
    String typename = sf.getEType().getName();
    return ecoreSet.contains(typename);
  }

  
  private static boolean isEcoreType(String typename) {
    return ecoreSet.contains(typename);
  }

  
  private static final Set<String> uml2Set = new HashSet<String>();
  static {
    uml2Set.add("Comment");
    uml2Set.add("Element");
    uml2Set.add("Package");
    uml2Set.add("VisibilityKind");
    uml2Set.add("Dependency");
    uml2Set.add("StringExpression");
    uml2Set.add("TemplateParameter");
    uml2Set.add("TemplateSignature");
    uml2Set.add("TemplateableElement");
    uml2Set.add("TemplateBinding");
    uml2Set.add("TemplateParameterSubstitution");
    uml2Set.add("ParameterableElement");
    uml2Set.add("Type");
    uml2Set.add("ValueSpecification");
    uml2Set.add("NamedElement");
    uml2Set.add("ElementImport");
    uml2Set.add("PackageableElement");
    uml2Set.add("Namespace");
    uml2Set.add("PackageImport");
    uml2Set.add("Constraint");
    uml2Set.add("PackageMerge");
    uml2Set.add("ProfileApplication");
    uml2Set.add("Profile");
    uml2Set.add("Stereotype");
    uml2Set.add("Generalization");
    uml2Set.add("Classifier");
    uml2Set.add("GeneralizationSet");
    uml2Set.add("Substitution");
    uml2Set.add("OpaqueExpression");
    uml2Set.add("Behavior");
    uml2Set.add("CollaborationUse");
    uml2Set.add("Collaboration");
    uml2Set.add("UseCase");
    uml2Set.add("InterfaceRealization");
    uml2Set.add("Interface");
    uml2Set.add("Property");
    uml2Set.add("ConnectorEnd");
    uml2Set.add("ConnectableElement");
    uml2Set.add("Deployment");
    uml2Set.add("DeployedArtifact");
    uml2Set.add("DeploymentSpecification");
    uml2Set.add("Artifact");
    uml2Set.add("Manifestation");
    uml2Set.add("Operation");
    uml2Set.add("Parameter");
    uml2Set.add("ParameterSet");
    uml2Set.add("ParameterDirectionKind");
    uml2Set.add("ParameterEffectKind");
    uml2Set.add("CallConcurrencyKind");
    uml2Set.add("Class");
    uml2Set.add("Connector");
    uml2Set.add("Association");
    uml2Set.add("ConnectorKind");
    uml2Set.add("Port");
    uml2Set.add("DataType");
    uml2Set.add("AggregationKind");
    uml2Set.add("ProtocolStateMachine");
    uml2Set.add("Trigger");
    uml2Set.add("Event");
    uml2Set.add("Reception");
    uml2Set.add("Signal");
    uml2Set.add("BehavioralFeature");
    uml2Set.add("Region");
    uml2Set.add("Vertex");
    uml2Set.add("Transition");
    uml2Set.add("TransitionKind");
    uml2Set.add("State");
    uml2Set.add("StateMachine");
    uml2Set.add("Pseudostate");
    uml2Set.add("PseudostateKind");
    uml2Set.add("ConnectionPointReference");
    uml2Set.add("ProtocolConformance");
    uml2Set.add("DeploymentTarget");
    uml2Set.add("BehavioredClassifier");
    uml2Set.add("Include");
    uml2Set.add("Extend");
    uml2Set.add("ExtensionPoint"); 
    uml2Set.add("Image");
    uml2Set.add("DirectedRelationship");
    uml2Set.add("Relationship");
    uml2Set.add("TypedElement");
    uml2Set.add("RedefinableElement");
    uml2Set.add("Feature");
    uml2Set.add("Realization");
    uml2Set.add("Abstraction");
    uml2Set.add("MultiplicityElement");
    uml2Set.add("EncapsulatedClassifier");
    uml2Set.add("StructuredClassifier");
    uml2Set.add("Extension");
    uml2Set.add("ExtensionEnd");
    uml2Set.add("Model");
    uml2Set.add("OperationTemplateParameter");
    uml2Set.add("StructuralFeature");
    uml2Set.add("ConnectableElementTemplateParameter");
    uml2Set.add("RedefinableTemplateSignature");
    uml2Set.add("ClassifierTemplateParameter");
    uml2Set.add("Expression");
    uml2Set.add("Usage");
    uml2Set.add("Enumeration");
    uml2Set.add("EnumerationLiteral");
    uml2Set.add("Slot");
    uml2Set.add("InstanceSpecification");
    uml2Set.add("PrimitiveType");
    uml2Set.add("LiteralSpecification");
    uml2Set.add("LiteralInteger");
    uml2Set.add("LiteralString");
    uml2Set.add("LiteralBoolean");
    uml2Set.add("LiteralNull");
    uml2Set.add("InstanceValue");
    uml2Set.add("LiteralUnlimitedNatural");
    uml2Set.add("OpaqueBehavior");
    uml2Set.add("FunctionBehavior");
    uml2Set.add("OpaqueAction");
    uml2Set.add("StructuredActivityNode");
    uml2Set.add("Activity");
    uml2Set.add("Variable");
    uml2Set.add("ActivityNode");
    uml2Set.add("ActivityEdge");
    uml2Set.add("ActivityPartition");
    uml2Set.add("InterruptibleActivityRegion");
    uml2Set.add("ActivityGroup");
    uml2Set.add("ExceptionHandler");
    uml2Set.add("ExecutableNode");
    uml2Set.add("ObjectNode");
    uml2Set.add("ObjectNodeOrderingKind");
    uml2Set.add("InputPin");
    uml2Set.add("OutputPin");
    uml2Set.add("Action");
    uml2Set.add("Pin");
    uml2Set.add("CallAction");
    uml2Set.add("InvocationAction");
    uml2Set.add("SendSignalAction");
    uml2Set.add("CallOperationAction");
    uml2Set.add("CallBehaviorAction");
    uml2Set.add("SequenceNode");
    uml2Set.add("ControlNode");
    uml2Set.add("ControlFlow");
    uml2Set.add("InitialNode");
    uml2Set.add("ActivityParameterNode");
    uml2Set.add("ValuePin");
    uml2Set.add("Message");
    uml2Set.add("MessageSort");
    uml2Set.add("MessageEnd");
    uml2Set.add("Interaction");
    uml2Set.add("Lifeline");
    uml2Set.add("PartDecomposition");
    uml2Set.add("GeneralOrdering");
    uml2Set.add("OccurrenceSpecification");
    uml2Set.add("InteractionOperand");
    uml2Set.add("InteractionConstraint");
    uml2Set.add("InteractionFragment");
    uml2Set.add("Gate");
    uml2Set.add("MessageKind");
    uml2Set.add("InteractionUse");
    uml2Set.add("ExecutionSpecification");
    uml2Set.add("StateInvariant");
    uml2Set.add("ActionExecutionSpecification");
    uml2Set.add("BehaviorExecutionSpecification");
    uml2Set.add("ExecutionEvent");
    uml2Set.add("CreationEvent");
    uml2Set.add("DestructionEvent");
    uml2Set.add("SendOperationEvent");
    uml2Set.add("MessageEvent");
    uml2Set.add("SendSignalEvent");
    uml2Set.add("MessageOccurrenceSpecification");
    uml2Set.add("ExecutionOccurrenceSpecification");
    uml2Set.add("ReceiveOperationEvent");
    uml2Set.add("ReceiveSignalEvent");
    uml2Set.add("Actor");
    uml2Set.add("CallEvent");
    uml2Set.add("ChangeEvent");
    uml2Set.add("SignalEvent");
    uml2Set.add("AnyReceiveEvent");
    uml2Set.add("ForkNode");
    uml2Set.add("FlowFinalNode");
    uml2Set.add("FinalNode");
    uml2Set.add("CentralBufferNode");
    uml2Set.add("MergeNode");
    uml2Set.add("DecisionNode");
    uml2Set.add("ObjectFlow");
    uml2Set.add("ActivityFinalNode");
    uml2Set.add("ComponentRealization");
    uml2Set.add("Component");
    uml2Set.add("Node");
    uml2Set.add("CommunicationPath");
    uml2Set.add("Device");
    uml2Set.add("ExecutionEnvironment");
    uml2Set.add("CombinedFragment");
    uml2Set.add("InteractionOperatorKind");
    uml2Set.add("Continuation");
    uml2Set.add("ConsiderIgnoreFragment");
    uml2Set.add("CreateObjectAction");
    uml2Set.add("DestroyObjectAction");
    uml2Set.add("TestIdentityAction");
    uml2Set.add("ReadSelfAction");
    uml2Set.add("StructuralFeatureAction");
    uml2Set.add("ReadStructuralFeatureAction");
    uml2Set.add("WriteStructuralFeatureAction");
    uml2Set.add("ClearStructuralFeatureAction");
    uml2Set.add("RemoveStructuralFeatureValueAction");
    uml2Set.add("AddStructuralFeatureValueAction");
    uml2Set.add("LinkAction");
    uml2Set.add("LinkEndData");
    uml2Set.add("QualifierValue");
    uml2Set.add("ReadLinkAction");
    uml2Set.add("LinkEndCreationData");
    uml2Set.add("CreateLinkAction");
    uml2Set.add("WriteLinkAction");
    uml2Set.add("DestroyLinkAction");
    uml2Set.add("LinkEndDestructionData");
    uml2Set.add("ClearAssociationAction");
    uml2Set.add("BroadcastSignalAction");
    uml2Set.add("SendObjectAction");
    uml2Set.add("ValueSpecificationAction");
    uml2Set.add("TimeExpression");
    uml2Set.add("Observation");
    uml2Set.add("Duration");
    uml2Set.add("DurationInterval");
    uml2Set.add("Interval");
    uml2Set.add("TimeConstraint");
    uml2Set.add("IntervalConstraint");
    uml2Set.add("TimeInterval");
    uml2Set.add("DurationConstraint");
    uml2Set.add("TimeObservation");
    uml2Set.add("DurationObservation");
    uml2Set.add("FinalState");
    uml2Set.add("TimeEvent");
    uml2Set.add("VariableAction");
    uml2Set.add("ReadVariableAction");
    uml2Set.add("WriteVariableAction");
    uml2Set.add("ClearVariableAction");
    uml2Set.add("AddVariableValueAction");
    uml2Set.add("RemoveVariableValueAction");
    uml2Set.add("RaiseExceptionAction");
    uml2Set.add("ActionInputPin");
    uml2Set.add("InformationItem");
    uml2Set.add("InformationFlow");
    uml2Set.add("ReadExtentAction");
    uml2Set.add("ReclassifyObjectAction");
    uml2Set.add("ReadIsClassifiedObjectAction");
    uml2Set.add("StartClassifierBehaviorAction");
    uml2Set.add("ReadLinkObjectEndAction");
    uml2Set.add("ReadLinkObjectEndQualifierAction");
    uml2Set.add("CreateLinkObjectAction");
    uml2Set.add("AcceptEventAction");
    uml2Set.add("AcceptCallAction");
    uml2Set.add("ReplyAction");
    uml2Set.add("UnmarshallAction");
    uml2Set.add("ReduceAction");
    uml2Set.add("StartObjectBehaviorAction");
    uml2Set.add("JoinNode");
    uml2Set.add("DataStoreNode");
    uml2Set.add("ConditionalNode");
    uml2Set.add("Clause");
    uml2Set.add("LoopNode");
    uml2Set.add("ExpansionNode");
    uml2Set.add("ExpansionRegion");
    uml2Set.add("ExpansionKind");
    uml2Set.add("ProtocolTransition");
    uml2Set.add("AssociationClass");
    uml2Set.add("StringEList");
  }

  
  private static boolean isUML2Type(EStructuralFeature sf) {
    String typename = sf.getEType().getName();
    return uml2Set.contains(typename);
  }

  
  private static boolean isUML2Type(String typename) {
    return uml2Set.contains(typename);
  }



}
