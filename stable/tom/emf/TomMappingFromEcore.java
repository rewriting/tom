/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2009-2014, Universite de Lorraine, Inria
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Nicolas HENRY (july 2009)
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 * Jean-Christophe Bach   e-mail: jeanchristophe.bach@inria.fr
 *
 **/

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

/**
 * Give a Tom mapping from an EcorePackage
 */
public class TomMappingFromEcore {

  /**
   * List of classes of which the list mappings are already been generated
   */
  private final static ArrayList<Class<?>> lists = new ArrayList<Class<?>>();

  /**
   * true if the append method used by all list mappings is already generated
   */
  private static boolean append = false;

  /**
   * true if '%subtype' keywords hav to be generated ('-nt' option is enabled)
   */
  private static boolean useNewTyper = false;

  /**
   * true if the EPackage to generate is EcorePackage itself
   */
  private static boolean genEcoreMapping = false;

  /**
   * A dictionnary linking a class with his generated type name
   */
  private final static HashMap<Class<?>, String> types = new HashMap<Class<?>, String>();

  /**
   * Prefix to add to mappings (%typeterm and %op). There is no prefix by
   * default. Useful to avoid names clash when several elements of differemnt
   * packages have the same names.
   */
  private static String prefix = "";

  /** 
   * aa
   * 
   * @param args the argmuents list
   * @return the list of EPackage names to process
   */
  public static List<String> processArgs(String[] args) {
    List<String> ePackageNameList = new ArrayList<String>();
    // -nt: typer
    // -prefix
    int i = 0;
    String argument = "";
    try {
      for(; i < args.length; i++) {
        argument = args[i];

        if(!argument.startsWith("-") || (argument.equals("-"))) {
          // input file name, should never start with '-' (except for System.in)
          ePackageNameList.add(argument);
        } else {
          // argument does start with '-', thus is -or at least should be- an option
          argument = argument.substring(1); // crops the '-'
          if(argument.startsWith("-")) {
            // if there's another one
            argument = argument.substring(1); // crops the second '-'
          }
          /*if(argument.equals("help") || argument.equals("h")) {
            displayHelp();
            return null;
          }*/
          /*if(argument.equals("version") || argument.equals("V")) {
            TomOptionManager.displayVersion();
            return null;
          }*/
          if(argument.equals("X") || argument.equals("cp")) {
            // just skip it, along with its argument (java options)
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
      //TomEMFMessage.error(logger, null, 0, TomEMFMessage.noEPackageToInspect);
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
    /*
     * this way to use options shouldn't be like this : we could add a better
     * option management system (as in Tom/Gom)
     * We may also add an --output <file> option
     */
      if(args.length < 1) {
        displayHelp();
        /*System.out.println("No argument has been given!");
        System.out.println("usage: emf-generate-mappings [options] <EPackageClassName>");
        System.out.println("options:");
        System.out.println("  -nt (allows to generate constructs with subtyping)");
        System.out.println("  any Java options");*/
      } else {
        /*int initindex = 0;
        List<String> ePackageNameList = new ArrayList<String>();

        if (args[0].contentEquals("-nt")) {
          initindex = 1;
          useNewTyper = true;
        }
        for(int i=initindex;i<args.length;i++) {
          ePackageNameList.add(args[i]);
        }*/
        List<String> ePackageNameList = processArgs(args);
        if(ePackageNameList == null) {
          throw new RuntimeException("No EPackage has been given as argument!");
        }

        try {
          for(int i=0;i<ePackageNameList.size();i++) {
            File output = new File(".",ePackageNameList.get(i)+".tom");
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
            //libraries are not necessarily under GPL
            //uncomment the following line when re-generating ecore.tom mapping
            //genLicence(writer);
            //specific case of EcorePackage mapping generation
            if(ePackageNameList.get(i).equals("org.eclipse.emf.ecore.EcorePackage")) {
              genEcoreMapping = true;
            }
            extractFromEPackage(writer, (EPackage) Class.forName(ePackageNameList.get(i)).getField("eINSTANCE").get(null));
            writer.flush();
            writer.close();
            //reset
            genEcoreMapping = false;
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
  }

  private static void genLicence(java.io.Writer writer) throws java.io.IOException {
    writer.write("/*\n *\n * TOM - To One Matching Compiler\n *\n * Copyright (c) 2009-2014, Universite de Lorraine, Inria\n * Nancy, France.\n *\n * This program is free software; you can redistribute it and/or modify\n * it under the terms of the GNU General Public License as published by\n * the Free Software Foundation; either version 2 of the License, or\n * (at your option) any later version.\n *\n * This program is distributed in the hope that it will be useful,\n * but WITHOUT ANY WARRANTY; without even the implied warranty of\n * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n * GNU General Public License for more details.\n *\n * You should have received a copy of the GNU General Public License\n * along with this program; if not, write to the Free Software\n * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA\n *\n *\n **/"





















);
  }

  /**
   * Extract the type term from a classifier
   * @param eclf the classifier to extract
   */
  private static void extractType(java.io.Writer writer, EClassifier eclf) throws java.io.IOException {
    Class<?> c = eclf.getInstanceClass();
    if(!types.containsKey(c)) {
      String n = (c.isArray()?(c.getComponentType().getSimpleName()+"array"):c.getSimpleName());
      String is = "";
      for(int i = 1; types.containsValue(n + is); i++) {
        is = String.valueOf(i);
      }
      n = n + is;
      types.put(c, n);
      if(tomTypes.containsKey(c)) {
        writer.write("\n\n%include { " + tomTypes.get(c) + ".tom }");
      } else if(tomEMFTypes.contains(c) && !genEcoreMapping) {
        //add ecore mappings if needed, when not generating the EcorePackage mapping itself
        writer.write("\n\n%include { emf/ecore.tom }");
      } else {
        String[] decl = getClassDeclarations(eclf); // [canonical name, anonymous generic, generic type]
        writer.write("\n\n%typeterm "+prefix+n+" "+(useNewTyper?genSubtype(eclf):"")+" {\n  implement { "+(eclf instanceof EClass && !EObject.class.isAssignableFrom(c) ? "org.eclipse.emf.ecore.EObject" : decl[0] + decl[2])+" }\n  is_sort(t) { "+(c.isPrimitive() ? "true" : "$t instanceof " + decl[0] + decl[1])+" }\n  equals(l1,l2) { "+(c.isPrimitive() ? "$l1 == $l2" : "$l1.equals($l2)")+" }\n}"





);
      }
    }
  }

  private static String genSubtype(EClassifier eclf) {
    String result = "";
    if((eclf instanceof EClass)) {
      if(!((EClass)eclf).getESuperTypes().isEmpty()) {
        //should be a collection with one and only one element
        for (EClass supertype:((EClass)eclf).getESuperTypes()) {
          result = result + " extends "+ prefix + supertype.getName();
        }
      } else {
        if(!(eclf.getInstanceClassName().equals("EObject"))) {
          result = result + " extends " + "EObject";
        }
      }
    }
    return result;
  }

  /**
   * A dictionnary linking standard java classes with the corresponding tom mapping filename 
   */
  private final static HashMap<Class<?>, String> tomTypes = new HashMap<Class<?>, String>();
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

/**
   * A set of EMF classes 
   */
  private final static HashSet<Class<?>> tomEMFTypes = new HashSet<Class<?>>();
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
    tomEMFTypes.add(org.eclipse.emf.ecore.resource.Resource .class);
    tomEMFTypes.add(byte.class);
    tomEMFTypes.add(java.util.Date.class);
    tomEMFTypes.add(Class.class);
  }


  /** A list of java reserved keywords for variable naming
   */
  private final static List<String> keywords = java.util.Arrays.asList(new
      String[] { "abstract", "continue", "for", "new", "switch", "assert",
      "default", "goto", "package", "synchronized", "boolean", "do", "if",
      "private", "this", "break", "double", "implements", "protected", "throw",
      "byte", "else", "import", "public", "throws", "case", "enum",
      "instanceof", "return", "transient", "catch", "extends", "int", "short",
      "try", "char", "final", "interface", "static", "void", "class", "finally",
      "long", "strictfp", "volatile", "const", "float", "native", "super",
      "while", "true", "false", "null" });

  /**
   * Return an array containing 3 strings from EClassifer :
   * [0] -> canonical name of the instance class
   * [1] -> anonymous generic types of the instance class
   * [2] -> generic types of the instance class
   * @param ec EClassfier to treat
   * @return an array containing 3 strings
   */
  private static String[] getClassDeclarations(EClassifier ec) {
    Class<?> c = ec.getInstanceClass();
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

  /**
   * Return type of a EStructuralFeature
   * @param sf EStructuralFeature to return type
   * @return type of sf
   */
  private static String getType(EStructuralFeature sf) throws java.io.IOException {
    Class<?> c = sf.getEType().getInstanceClass();
    return types.get(c);
  }

  /**
   * Return type of a EStructuralFeature and generate his list mapping if he is many
   * @param sf EStructuralFeature to return type
   * @return type of sf
   */
  private static String getType(java.io.Writer writer, EStructuralFeature sf) throws java.io.IOException {

    Class<?> c = sf.getEType().getInstanceClass();
    //String simplename = types.get(c);
    String argname = isBuiltin(sf)?types.get(c):(prefix+types.get(c));
    String name = argname;
    if(sf.isMany()) {
      name += "EList";
      if(!lists.contains(c)) {

        String[] decl = getClassDeclarations(sf.getEType()); // [canonical name, anonymous generic, generic type]

        /*String inst = (EObject.class.isAssignableFrom(sf.getEType()
            .getInstanceClass()) ? decl[0] + decl[2] :
            "org.eclipse.emf.ecore.EObject");*/
        String inst = "";
        if(EObject.class.isAssignableFrom(c)) {
          inst = decl[0] + decl[2];
        } else if(!c.isInterface()) {
          inst = "java.lang.Object";
        } else {
          inst = "org.eclipse.emf.ecore.EObject";
        }
        writer.write("\n\n%typeterm "+prefix+name+" {\n  implement { org.eclipse.emf.common.util.EList<"+inst+"> }\n  is_sort(t) { $t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<"+inst+">)$t).size() == 0 || (((org.eclipse.emf.common.util.EList<"+inst+">)$t).size()>0 && ((org.eclipse.emf.common.util.EList<"+inst+">)$t).get(0) instanceof "+(decl[0]+decl[1])+")) }\n  equals(l1,l2) { $l1.equals($l2) }\n}\n\n%oparray "+prefix+name+" "+prefix+name+" ( "+argname+"* ) {\n  is_fsym(t) { $t instanceof org.eclipse.emf.common.util.EList<?> && ($t.size() == 0 || ($t.size()>0 && $t.get(0) instanceof "+(decl[0]+decl[1])+")) }\n  make_empty(n) { new org.eclipse.emf.common.util.BasicEList<"+inst+">($n) }\n  make_append(e,l) { append"+name+"($e,$l) }\n  get_element(l,n) { $l.get($n) }\n  get_size(l)      { $l.size() }\n}\n\nprivate static <O> org.eclipse.emf.common.util.EList<O> append"+name+"(O e,org.eclipse.emf.common.util.EList<O> l) {\n  l.add(e);\n  return l;\n}"


















);
        lists.add(c);
      }
    }
    return (sf.isMany()?prefix+name:name);
  }

  /**
   * Generate classifiers and subpackages mappings of a package
   * @param p EPackage to extract
   */
  private static void extractFromEPackage(java.io.Writer writer, EPackage p) throws java.io.IOException {
    for(EClassifier eclf : p.getEClassifiers()) {
      extractFromEClassifier(writer,eclf);
    }
    for(EPackage ep : p.getESubpackages()) {
      extractFromEPackage(writer,ep);
    }
  }

  /**
   * A dictonnary containing the matching java boxed class of a java primitive class
   */
  private final static HashMap<Class<?>, Class<?>> primitiveToBoxed = new HashMap<Class<?>, Class<?>>();
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

  /**
   * Return the boxed type of a primitive class (or the same class if not primitive)
   * @param primitiveType the primitive class to box
   * @return the boxed class (or the same class if not primitive)
   */
  private static Class<?> boxType(Class<?> primitiveType) {
    Class<?> boxedType = (Class<?>) primitiveToBoxed.get(primitiveType);
    if(boxedType != null) {
      return boxedType;
    } else {
      return primitiveType;
    }
  }

  //
  // private final static HashMap<Class<?>, Class<?>> boxedToPrimitive = new
  // HashMap<Class<?>, Class<?>>();
  // static {
  // boxedToPrimitive.put(Boolean.class, Boolean.TYPE);
  // boxedToPrimitive.put(Byte.class, Byte.TYPE);
  // boxedToPrimitive.put(Short.class, Short.TYPE);
  // boxedToPrimitive.put(Character.class, Character.TYPE);
  // boxedToPrimitive.put(Integer.class, Integer.TYPE);
  // boxedToPrimitive.put(Long.class, Long.TYPE);
  // boxedToPrimitive.put(Float.class, Float.TYPE);
  // boxedToPrimitive.put(Double.class, Double.TYPE);
  // }
  //
  // private static Class<?> unboxType(Class<?> boxedType) {
  // Class<?> primitiveType = (Class<?>) boxedToPrimitive.get(boxedType);
  // if(primitiveType != null) {
  // return primitiveType;
  // } else {
  // return boxedType;
  // }
  // }
  //

  /**
   * List of already generated classifiers
   */
  private final static ArrayList<EClassifier> classifiers = new ArrayList<EClassifier>();

  /**
   * Make the name with upper first character
   * @param name name to treat
   * @return name treated
   */
  private static String toUpperName(String name) {
    if(name.length() < 1) {
      throw new IllegalArgumentException(
          "Name must contains at least one character.");
    }
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  /**
   * Generate get_default constructs
   * @param esf the EStructuralFeature which has a default value
   * @return get_default construct
   */
  private static String genGetDefault(EStructuralFeature esf) throws java.io.IOException {
    String result = "";
    String dvalue = ""+esf.getDefaultValue();
    String esftype = getType(esf);
   
    // cases : null, String, `toto(), builtin
    if(esftype.equals("String")) {
      dvalue = "\"" + dvalue + "\"";
    } else if(esftype.equals("boolean") || esftype.equals("int") ||
        esftype.equals("float") || esftype.equals("double") || 
        esftype.equals("long")) {
      //nothing
      //dvalue = sf.getDefaultValue();
    } else {
      dvalue = "`" + prefix + esftype + dvalue + "()";
    }
    //add a suffix '_' if the name is a reserved keyword
    String sfname = (keywords.contains(esf.getName()) ? "_" : "")+esf.getName();
    return result+"\n  get_default("+sfname+") { "+dvalue+" }";
  }

  /**
   * Generate classifier mapping
   * @param eclf classifier to extract
   */
  private static void extractFromEClassifier(java.io.Writer writer, EClassifier eclf) throws java.io.IOException {
    if(!classifiers.contains(eclf)) {
      extractType(writer,eclf);
      classifiers.add(eclf);
      if(eclf instanceof EClass) {
        EClass ecl = (EClass) eclf;
        EList<EStructuralFeature> sfs = ecl.getEAllStructuralFeatures();
        StringBuffer s_types = new StringBuffer();
        //StringBuffer s_types2 = new StringBuffer();
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
            String[] decl = getClassDeclarations(type); // [canonical name, anonymous generic, generic type]
            writer.write("");
            /*s_types2.append(", "
                + (sf.isMany() ? "org.eclipse.emf.common.util.EList<" + decl[0] + decl[2] + ">" : decl[0] + decl[2])
                + " " + sfname);*/
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
          String[] decl = getClassDeclarations(eclf); // [canonical name, anonymous generic, generic type]
          String o1 = ecl.getEPackage().getEFactoryInstance().getClass()
            .getInterfaces()[ecl.getEPackage().getClass()
            .getInterfaces().length - 1].getCanonicalName();
          String o2 = ecl.getEPackage().getClass().getInterfaces()[ecl.getEPackage().getClass().getInterfaces().length - 1]
            .getCanonicalName();
          //avoid to generate mappings already been defined in ecore.tom
          //except if the goal is to generate the EcorePackage mapping itself
          if(genEcoreMapping || !tomEMFTypes.contains(eclf.getInstanceClass())) {
            writer.write("\n\n%op "+prefix+ecl.getInstanceClass().getSimpleName()+" "+prefix+cr+"("+s_types+") {\n  is_fsym(t) { $t instanceof "+(decl[0]+decl[1])+" }"+s_gets+" "+s_defaults+"\n  make("+(s.length() <= 2 ? "" : s.substring(2))+") { construct"+cr+"(("+(EObject.class.isAssignableFrom(ecl.getInstanceClass()) ? ecl.getInstanceClass().getCanonicalName() : "org.eclipse.emf.ecore.EObject")+")"+o1+".eINSTANCE.create((EClass)"+o2+".eINSTANCE.getEClassifier(\""+ecl.getName()+"\")), new Object[]{ "+(s2.length() <= 2 ? "" : s2.substring(2))+" }) }\n  implement() { "+genOpImplementContent(ecl.getInstanceClassName(), cr)+" }\n}\n\npublic static <O extends org.eclipse.emf.ecore.EObject> O construct"+prefix+cr+"(O o, Object[] objs) {\n  int i=0;\n  EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();\n  for(EStructuralFeature esf : sfes) {\n    if(esf.isChangeable()) {\n      o.eSet(esf, objs[i]);\n      i++;\n    }\n  }\n  return o;\n}"

















);
          }
        }
      } else if(eclf instanceof EEnum) {
        EEnum en = (EEnum) eclf;
        String cr = eclf.getName();
        String[] decl = getClassDeclarations(eclf); // [canonical name, anonymous generic, generic type]
        for(EEnumLiteral lit : en.getELiterals()) {
          String o1 = eclf.getEPackage().getEFactoryInstance().getClass()
            .getInterfaces()[eclf.getEPackage().getClass()
            .getInterfaces().length - 1].getCanonicalName();
          String o2 = eclf.getEPackage().getClass().getInterfaces()[eclf
            .getEPackage().getClass().getInterfaces().length - 1]
            .getCanonicalName();
          //Problem: lit.getLiteral() can be a non-alphanumerical symbol. Use
          //name instead (a non alphanumerical name should not be valid
          //String literal = lit.getLiteral();
          String literalname = lit.getName();
          String operatorName = cr+literalname.replaceAll(" ","");
          writer.write("\n\n%op "+prefix+cr+" "+prefix+operatorName+"() {\n  is_fsym(t) { t == "+(decl[0]+decl[1])+".get(\""+literalname+"\") }\n  make() { ("+(decl[0]+decl[2])+")"+o1+".eINSTANCE.createFromString((EDataType)"+o2+".eINSTANCE.get"+toUpperName(cr)+"(), \""+literalname+"\") }\n}"




);
        }
      }
    }
  }
  
  /*
   * Default code generation of EMF works like this:
   * interface -> full.qualified.name.A
   * class     -> full.qualified.name.impl.AImpl
   */
  private static String genOpImplementContent(String eclname, String cr) {
    return eclname.substring(0, eclname.lastIndexOf(cr))+"impl."+cr+"Impl";
  }

  /* TODO: to complete */
  private static final Set<String> builtinSet = new HashSet<String>();
  static {
    builtinSet.add("String");
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

  /**
   * This method tests the type of the parameter represented by the
   * EStructuralFeature
   * @param sf EStructuralFeature corresponding to a parameter we want to
   * obtain the type
   * @return true if the EStructuralFeature represents a parameter whose type
   * is a primitive or implemented by a primitive type.
   */
  private static boolean isBuiltin(EStructuralFeature sf) {
    String typename = sf.getEType().getName();
    return builtinSet.contains(typename);
  }

  private static boolean isBuiltin(String typename) {
    return builtinSet.contains(typename);
  }

}
