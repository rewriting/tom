import java.io.PrintStream;
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
 * 
 * @author Nicolas HENRY
 * 
 */
public class TomMappingFromEcore {

  /**
   * The program output
   */
  private static final PrintStream out = System.out;

  /**
   * List of classes of which the list mappings are already been generated
   */
  private final static ArrayList<Class<?>> lists = new ArrayList<Class<?>>();
  /**
   * true if the append method used by all list mappings is already generated
   */
  private static boolean append = false;

  /**
   * true if the constructor method used by all mappings is already generated
   */
  private static boolean constructor = false;
  /**
   * A dictionnary linking a class with his generated type name
   */
  private final static HashMap<Class<?>, String> types = new HashMap<Class<?>, String>();

  public static void main(String[] args) {
    if(args.length != 1) {
      out.println("usage : java TomMappingFromEcore <EPackageClassName>");
    } else {
      try {
        extract((EPackage) Class.forName(args[0]).getField("eINSTANCE").get(
            null));
      } catch(ClassNotFoundException e) {
        e.printStackTrace();
      } catch(IllegalArgumentException e) {
        e.printStackTrace();
      } catch(SecurityException e) {
        e.printStackTrace();
      } catch(IllegalAccessException e) {
        e.printStackTrace();
      } catch(NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Extract the type term from a classifier
   * @param eclf the classifier to extract
   */
  private static void extractType(EClassifier eclf) {
    Class<?> c = eclf.getInstanceClass();
    if(!types.containsKey(c)) {
      String n = c.getSimpleName();
      if(c.isArray()) {
        n = c.getComponentType().getSimpleName() + "array";
      }
      String is = "";
      for(int i = 1; types.containsValue(n + is); i++) {
        is = String.valueOf(i);
      }
      n = n + is;
      types.put(c, n);
      if(tomTypes.containsKey(c)) {
        out.println("%include { " + tomTypes.get(c) + ".tom }");
      } else {
        String[] d = getClassDeclarations(eclf);
        out.println("%typeterm " + n + " {");
        out
            .println("  implement { "
                + (eclf instanceof EClass && !EObject.class.isAssignableFrom(c) ? "EObject"
                    : d[0] + d[2]) + " }");
        out.println("  is_sort(t) { "
            + (c.isPrimitive() ? "true" : "$t instanceof " + d[0] + d[1])
            + " }");
        out.println("  equals(l1,l2) { "
            + (c.isPrimitive() ? "$l1 == $l2" : "$l1.equals($l2)") + " }");
        out.println("}");
      }
    }
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
    tomTypes.put(AbstractSequentialList.class,
        "util/types/AbstractSequentialList");
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
   * A list of java reserved keywords for variable naming
   */
  private final static ArrayList<String> keywords = new ArrayList<String>();
  static {
    keywords.add("abstract");
    keywords.add("continue");
    keywords.add("for");
    keywords.add("new");
    keywords.add("switch");
    keywords.add("assert");
    keywords.add("default");
    keywords.add("goto");
    keywords.add("package");
    keywords.add("synchronized");
    keywords.add("boolean");
    keywords.add("do");
    keywords.add("if");
    keywords.add("private");
    keywords.add("this");
    keywords.add("break");
    keywords.add("double");
    keywords.add("implements");
    keywords.add("protected");
    keywords.add("throw");
    keywords.add("byte");
    keywords.add("else");
    keywords.add("import");
    keywords.add("public");
    keywords.add("throws");
    keywords.add("case");
    keywords.add("enum");
    keywords.add("instanceof");
    keywords.add("return");
    keywords.add("transient");
    keywords.add("catch");
    keywords.add("extends");
    keywords.add("int");
    keywords.add("short");
    keywords.add("try");
    keywords.add("char");
    keywords.add("final");
    keywords.add("interface");
    keywords.add("static");
    keywords.add("void");
    keywords.add("class");
    keywords.add("finally");
    keywords.add("long");
    keywords.add("strictfp");
    keywords.add("volatile");
    keywords.add("const");
    keywords.add("float");
    keywords.add("native");
    keywords.add("super");
    keywords.add("while");
    keywords.add("true");
    keywords.add("false");
    keywords.add("null");
  }

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
   * Return type of a EStructuralFeature and generate his list mapping if he is many
   * @param sf EStructuralFeature to return type
   * @return type of sf
   */
  private static String getType(EStructuralFeature sf) {

    Class<?> c = sf.getEType().getInstanceClass();
    String simplename = types.get(c);
    String name = simplename;

    if(sf.isMany()) {

      name += "EList";
      if(!lists.contains(c)) {

        String[] d = getClassDeclarations(sf.getEType());

        String cn = d[0];
        String ca = d[1];
        // String cb = d[2];

        String inst = (EObject.class.isAssignableFrom(sf.getEType()
            .getInstanceClass()) ? d[0] + d[2] : "EObject");

        out.println("%typeterm " + name + " {");
        out.println("  implement { EList<" + inst + "> }");
        out.println("  is_sort(t) { " + "$t instanceof EList<?> "
            + "&& (((EList<" + inst + ">)$t).size() == 0 " + "|| (((EList<"
            + inst + ">)$t).size()>0 " + "&& ((EList<" + inst
            + ">)$t).get(0) instanceof " + cn + ca + ")) }");
        out.println("  equals(l1,l2) { $l1.equals($l2) }");
        out.println("}");

        out
            .println("%oparray " + name + " " + name + "(" + simplename
                + "*) {");
        out.println("  is_fsym(t) { $t instanceof EList<?> "
            + "&& ($t.size() == 0 || ($t.size()>0 "
            + "&& $t.get(0) instanceof " + cn + ca + ")) }");
        out.println("  make_empty(n) { new BasicEList<" + inst + ">($n) }");
        out.println("  make_append(e,l) { append($e,$l) }");
        out.println("  get_element(l,n) { $l.get($n) }");
        out.println("  get_size(l)      { $l.size() }");
        out.println("}");

        if(!append) {
          out.println("private static <O> EList<O> append(O e,EList<O> l) {");
          out.println("  l.add(e);");
          out.println("  return l;");
          out.println("}");
          append = true;
        }

        lists.add(c);
      }
    }
    return name;
  }

  /**
   * Generate classifiers and subpackages mappings of a package
   * @param p EPackage to extract
   */
  private static void extract(EPackage p) {
    for(EClassifier eclf : p.getEClassifiers()) {
      extract(eclf);
    }
    for(EPackage ep : p.getESubpackages()) {
      extract(ep);
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
   * Generate classifier mapping
   * @param eclf classifier to extract
   */
  private static void extract(EClassifier eclf) {
    if(!classifiers.contains(eclf)) {
      String[] dec = getClassDeclarations(eclf);
      String cr = eclf.getName();
      extractType(eclf);
      classifiers.add(eclf);
      if(eclf instanceof EClass) {
        EClass ecl = (EClass) eclf;
        EList<EStructuralFeature> sfs = ecl.getEAllStructuralFeatures();
        StringBuffer s_types = new StringBuffer();
        StringBuffer s_types2 = new StringBuffer();
        StringBuffer s = new StringBuffer();
        StringBuffer s2 = new StringBuffer();
        StringBuffer s_gets = new StringBuffer();
        for(EStructuralFeature sf : sfs) {
          if(sf.isChangeable()) {
            extract(sf.getEType());
            EClassifier type = sf.getEType();
            String sfname = (keywords.contains(sf.getName()) ? "_" : "")
                + sf.getName();
            s_types.append(sfname + " : " + getType(sf) + ", ");
            String[] d = getClassDeclarations(type);
            s_types2.append(", "
                + (sf.isMany() ? "EList<" + d[0] + d[2] + ">" : d[0] + d[2])
                + " " + sfname);
            s.append(", " + sfname);
            s2.append(", $" + sfname);
            String na = boxType(sf.getEType().getInstanceClass())
                .getCanonicalName();
            if(type instanceof EClass
                && !EObject.class.isAssignableFrom(type.getInstanceClass())) {
              na = "EObject";
            }
            if(sf.isMany()) {
              na = "EList<" + na + ">";
            }
            s_gets.append("  get_slot(" + sfname + ", t)  { (" + na
                + ")$t.eGet($t.eClass().getEStructuralFeature(\"" + sf.getName()
                + "\")) }\n");
          }
        }
        if(s_types.length() >= 2) {
          s_types.delete(s_types.length() - 2, s_types.length());
        }
        if(!ecl.isAbstract()) {
          out.print("%op " + ecl.getInstanceClass().getSimpleName() + " " + cr
              + "(");

          out.print(s_types);

          out.println(") {");

          out.println("  is_fsym(t) { $t instanceof " + dec[0] + dec[1] + " }");

          out.print(s_gets);

          out
              .println("  make("
                  + (s.length() <= 2 ? "" : s.substring(2))
                  + ") { construct(("
                  + (EObject.class.isAssignableFrom(ecl.getInstanceClass()) ? ecl
                      .getInstanceClass().getCanonicalName()
                      : "EObject")
                  + ")"
                  + ecl.getEPackage().getEFactoryInstance().getClass()
                      .getInterfaces()[ecl.getEPackage().getClass()
                      .getInterfaces().length - 1].getCanonicalName()
                  + ".eINSTANCE.create((EClass)"
                  + ecl.getEPackage().getClass().getInterfaces()[ecl
                      .getEPackage().getClass().getInterfaces().length - 1]
                      .getCanonicalName() + ".eINSTANCE.getEClassifier(\""
                  + ecl.getName() + "\")),new Object[]{"
                  + (s2.length() <= 2 ? "" : s2.substring(2)) + "}) }");

          out.println("}");
          if(!constructor) {
            out
                .println("public static <O extends EObject> O construct(O o, Object[] objs){");

            out.println("int i=0;");
            out
                .println("EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();");
            out.println("for(EStructuralFeature esf : sfes) {");
            out.println("  if(esf.isChangeable()){");
            out.println("    o.eSet(esf, objs[i]);");
            out.println("    i++;");
            out.println("  }");
            out.println("}");

            out.println("  return o;");
            out.println("}");
            constructor = true;
          }
        }
      } else if(eclf instanceof EEnum) {
        EEnum en = (EEnum) eclf;
        for(EEnumLiteral lit : en.getELiterals()) {
          out.println("%op " + cr + " " + lit.getLiteral() + "() {");

          out.println("  is_fsym(t) { $t instanceof " + dec[0] + dec[1] + " }");

          out.println("  make() { ("
              + dec[0]
              + dec[2]
              + ")"
              + eclf.getEPackage().getEFactoryInstance().getClass()
                  .getInterfaces()[eclf.getEPackage().getClass()
                  .getInterfaces().length - 1].getCanonicalName()
              + ".eINSTANCE"
              + ".createFromString((EDataType) "
              + eclf.getEPackage().getClass().getInterfaces()[eclf
                  .getEPackage().getClass().getInterfaces().length - 1]
                  .getCanonicalName() + ".eINSTANCE" + ".get" + toUpperName(cr)
              + "(), \"" + lit.getLiteral() + "\") }");

          out.println("}");

        }
      }
    }

  }

}
