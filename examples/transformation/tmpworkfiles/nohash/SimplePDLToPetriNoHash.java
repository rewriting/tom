import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.*;
import org.eclipse.emf.ecore.xmi.impl.*;

import SimplePDLSemantics.DDMMSimplePDL.*;
import petrinetsemantics.DDMMPetriNet.*;

import SimplePDLSemantics.EDMMSimplePDL.*;
import petrinetsemantics.EDMMPetriNet.*;
import SimplePDLSemantics.SDMMSimplePDL.*;
import petrinetsemantics.SDMMPetriNet.*;
import SimplePDLSemantics.TM3SimplePDL.*;
import petrinetsemantics.TM3PetriNet.*;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import tom.library.utils.ReferenceClass;
import tom.library.utils.LinkClass;
import tom.library.sl.*;
import tom.library.emf.*;

public class SimplePDLToPetriNoHash {


private static boolean tom_equal_term_Strategy(Object t1, Object t2) {
return  (t1.equals(t2)) ;
}
private static boolean tom_is_sort_Strategy(Object t) {
return  (t instanceof tom.library.sl.Strategy) ;
}
private static boolean tom_equal_term_Position(Object t1, Object t2) {
return  (t1.equals(t2)) ;
}
private static boolean tom_is_sort_Position(Object t) {
return  (t instanceof tom.library.sl.Position) ;
}
private static boolean tom_equal_term_int(int t1, int t2) {
return  t1==t2 ;
}
private static boolean tom_is_sort_int(int t) {
return  true ;
}
private static boolean tom_equal_term_char(char t1, char t2) {
return  t1==t2 ;
}
private static boolean tom_is_sort_char(char t) {
return  true ;
}
private static boolean tom_equal_term_String(String t1, String t2) {
return  t1.equals(t2) ;
}
private static boolean tom_is_sort_String(String t) {
return  t instanceof String ;
}
private static  tom.library.sl.Strategy  tom_make_mu( tom.library.sl.Strategy  var,  tom.library.sl.Strategy  v) { 
return ( new tom.library.sl.Mu(var,v) );/*toto*/
}
private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { 
return ( new tom.library.sl.MuVar(name) );/*toto*/
}
private static  tom.library.sl.Strategy  tom_make_Identity() { 
return ( new tom.library.sl.Identity() );/*toto*/
}
private static  tom.library.sl.Strategy  tom_make_All( tom.library.sl.Strategy  v) { 
return ( new tom.library.sl.All(v) );/*toto*/
}
private static boolean tom_is_fun_sym_Sequence( tom.library.sl.Strategy  t) {
return ( t instanceof tom.library.sl.Sequence );
}
private static  tom.library.sl.Strategy  tom_empty_list_Sequence() { 
return  null ;/*toto*/
}
private static  tom.library.sl.Strategy  tom_cons_list_Sequence( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { 
return  tom.library.sl.Sequence.make(head,tail) ;/*toto*/
}
private static  tom.library.sl.Strategy  tom_get_head_Sequence_Strategy( tom.library.sl.Strategy  t) {
return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.FIRST) );
}
private static  tom.library.sl.Strategy  tom_get_tail_Sequence_Strategy( tom.library.sl.Strategy  t) {
return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.THEN) );
}
private static boolean tom_is_empty_Sequence_Strategy( tom.library.sl.Strategy  t) {
return ( t == null );
}

  private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 == null )) {
      return l2;
    } else if(( l2 == null )) {
      return l1;
    } else if(( l1 instanceof tom.library.sl.Sequence )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {
        return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;
      } else {
        return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;
      }
    } else {
      return  tom.library.sl.Sequence.make(l1,l2) ;
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_Sequence())) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):tom_empty_list_Sequence()),end,tail)) ;
  }
  private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { 
return ( 
tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence()))))

;/*toto*/
}
private static boolean tom_equal_term_EAttribute(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EAttribute(Object t) {
return  t instanceof org.eclipse.emf.ecore.EAttribute ;
}
private static boolean tom_equal_term_EAnnotation(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EAnnotation(Object t) {
return  t instanceof org.eclipse.emf.ecore.EAnnotation ;
}
private static boolean tom_equal_term_EAnnotationEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EAnnotationEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)t).get(0) instanceof org.eclipse.emf.ecore.EAnnotation)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEAnnotationEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_equal_term_Entry(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Entry(Object t) {
return  t instanceof java.util.Map.Entry<?, ?> ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEStringToStringMapEntry(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_EntryEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EntryEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)t).get(0) instanceof java.util.Map.Entry<?, ?>)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEntryEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_equal_term_EModelElement(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EModelElement(Object t) {
return  t instanceof org.eclipse.emf.ecore.EModelElement ;
}
private static boolean tom_equal_term_EObject(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EObject(Object t) {
return  t instanceof org.eclipse.emf.ecore.EObject ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEObject(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_EObjectEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EObjectEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)t).get(0) instanceof org.eclipse.emf.ecore.EObject)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEObjectEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEAnnotation(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_boolean(boolean t1, boolean t2) {
return  t1==t2 ;
}
private static boolean tom_is_sort_boolean(boolean t) {
return  true ;
}
private static boolean tom_equal_term_EClassifier(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EClassifier(Object t) {
return  t instanceof org.eclipse.emf.ecore.EClassifier ;
}
private static boolean tom_equal_term_ETypeParameter(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ETypeParameter(Object t) {
return  t instanceof org.eclipse.emf.ecore.ETypeParameter ;
}
private static boolean tom_equal_term_EGenericType(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EGenericType(Object t) {
return  t instanceof org.eclipse.emf.ecore.EGenericType ;
}
private static boolean tom_equal_term_EGenericTypeEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EGenericTypeEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EGenericType>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EGenericType>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EGenericType>)t).get(0) instanceof org.eclipse.emf.ecore.EGenericType)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEGenericTypeEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEGenericType(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructETypeParameter(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_ETypeParameterEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ETypeParameterEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.ETypeParameter>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.ETypeParameter>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.ETypeParameter>)t).get(0) instanceof org.eclipse.emf.ecore.ETypeParameter)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendETypeParameterEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEAttribute(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_EClass(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EClass(Object t) {
return  t instanceof org.eclipse.emf.ecore.EClass ;
}
private static boolean tom_equal_term_EClassEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EClassEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>)t).get(0) instanceof org.eclipse.emf.ecore.EClass)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEClassEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_equal_term_EOperation(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EOperation(Object t) {
return  t instanceof org.eclipse.emf.ecore.EOperation ;
}
private static boolean tom_equal_term_EParameter(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EParameter(Object t) {
return  t instanceof org.eclipse.emf.ecore.EParameter ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEParameter(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_EParameterEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EParameterEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EParameter>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EParameter>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EParameter>)t).get(0) instanceof org.eclipse.emf.ecore.EParameter)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEParameterEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_equal_term_EClassifierEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EClassifierEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>)t).get(0) instanceof org.eclipse.emf.ecore.EClassifier)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEClassifierEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEOperation(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_EOperationEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EOperationEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EOperation>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EOperation>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EOperation>)t).get(0) instanceof org.eclipse.emf.ecore.EOperation)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEOperationEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_equal_term_EStructuralFeature(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EStructuralFeature(Object t) {
return  t instanceof org.eclipse.emf.ecore.EStructuralFeature ;
}
private static boolean tom_equal_term_EStructuralFeatureEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EStructuralFeatureEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EStructuralFeature>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EStructuralFeature>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EStructuralFeature>)t).get(0) instanceof org.eclipse.emf.ecore.EStructuralFeature)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEStructuralFeatureEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEClass(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_EDataType(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EDataType(Object t) {
return  t instanceof org.eclipse.emf.ecore.EDataType ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEDataType(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_EEnum(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EEnum(Object t) {
return  t instanceof org.eclipse.emf.ecore.EEnum ;
}
private static boolean tom_equal_term_EEnumLiteral(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EEnumLiteral(Object t) {
return  t instanceof org.eclipse.emf.ecore.EEnumLiteral ;
}
private static boolean tom_equal_term_Enumerator(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Enumerator(Object t) {
return  t instanceof org.eclipse.emf.common.util.Enumerator ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEEnumLiteral(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_EEnumLiteralEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EEnumLiteralEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EEnumLiteral>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EEnumLiteral>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EEnumLiteral>)t).get(0) instanceof org.eclipse.emf.ecore.EEnumLiteral)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEEnumLiteralEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEEnum(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_EFactory(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EFactory(Object t) {
return  t instanceof org.eclipse.emf.ecore.EFactory ;
}
private static boolean tom_equal_term_EPackage(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EPackage(Object t) {
return  t instanceof org.eclipse.emf.ecore.EPackage ;
}
private static boolean tom_equal_term_EPackageEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EPackageEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EPackage>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EPackage>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EPackage>)t).get(0) instanceof org.eclipse.emf.ecore.EPackage)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEPackageEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEPackage(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEFactory(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_ENamedElement(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ENamedElement(Object t) {
return  t instanceof org.eclipse.emf.ecore.ENamedElement ;
}
private static boolean tom_equal_term_EReference(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EReference(Object t) {
return  t instanceof org.eclipse.emf.ecore.EReference ;
}
private static boolean tom_equal_term_EAttributeEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EAttributeEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>)t).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>)t).get(0) instanceof org.eclipse.emf.ecore.EAttribute)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendEAttributeEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructEReference(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_ETypedElement(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ETypedElement(Object t) {
return  t instanceof org.eclipse.emf.ecore.ETypedElement ;
}
private static boolean tom_equal_term_BigDecimal(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_BigDecimal(Object t) {
return  t instanceof java.math.BigDecimal ;
}
private static boolean tom_equal_term_BigInteger(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_BigInteger(Object t) {
return  t instanceof java.math.BigInteger ;
}
private static boolean tom_equal_term_Boolean(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Boolean(Object t) {
return  t instanceof java.lang.Boolean ;
}
private static boolean tom_equal_term_byte(Object l1, Object l2) {
return  l1 == l2 ;
}
private static boolean tom_is_sort_byte(Object t) {
return  true ;
}
private static boolean tom_equal_term_bytearray(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_bytearray(Object t) {
return  t instanceof byte[] ;
}
private static boolean tom_equal_term_Byte(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Byte(Object t) {
return  t instanceof java.lang.Byte ;
}
private static boolean tom_equal_term_Character(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Character(Object t) {
return  t instanceof java.lang.Character ;
}
private static boolean tom_equal_term_Date(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Date(Object t) {
return  t instanceof java.util.Date ;
}
private static boolean tom_equal_term_DiagnosticChain(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_DiagnosticChain(Object t) {
return  t instanceof org.eclipse.emf.common.util.DiagnosticChain ;
}
private static boolean tom_equal_term_double(double t1, double t2) {
return  t1==t2 ;
}
private static boolean tom_is_sort_double(double t) {
return  true ;
}
private static boolean tom_equal_term_Double(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Double(Object t) {
return  t instanceof java.lang.Double ;
}
private static boolean tom_equal_term_EList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_EList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> ;
}
private static boolean tom_equal_term_FeatureMap(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_FeatureMap(Object t) {
return  t instanceof org.eclipse.emf.ecore.util.FeatureMap ;
}
private static boolean tom_equal_term_Entry1(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Entry1(Object t) {
return  t instanceof org.eclipse.emf.ecore.util.FeatureMap.Entry ;
}
private static boolean tom_equal_term_float(float t1, float t2) {
return  t1==t2 ;
}
private static boolean tom_is_sort_float(float t) {
return  true ;
}
private static boolean tom_equal_term_Float(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Float(Object t) {
return  t instanceof java.lang.Float ;
}
private static boolean tom_equal_term_Integer(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Integer(Object t) {
return  t instanceof java.lang.Integer ;
}
private static boolean tom_equal_term_Class(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Class(Object t) {
return  t instanceof java.lang.Class<?> ;
}
private static boolean tom_equal_term_Object(Object o1, Object o2) {
return  o1.equals(o2) ;
}
private static boolean tom_is_sort_Object(Object t) {
return  t instanceof Object ;
}
private static boolean tom_equal_term_long(long t1, long t2) {
return  t1==t2 ;
}
private static boolean tom_is_sort_long(long t) {
return  true ;
}
private static boolean tom_equal_term_Long(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Long(Object t) {
return  t instanceof java.lang.Long ;
}
private static boolean tom_equal_term_Map(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Map(Object t) {
return  t instanceof java.util.Map ;
}
private static boolean tom_equal_term_Resource(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Resource(Object t) {
return  t instanceof org.eclipse.emf.ecore.resource.Resource ;
}
private static boolean tom_equal_term_ResourceSet(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ResourceSet(Object t) {
return  t instanceof org.eclipse.emf.ecore.resource.ResourceSet ;
}
private static boolean tom_equal_term_short(Object l1, Object l2) {
return  l1 == l2 ;
}
private static boolean tom_is_sort_short(Object t) {
return  true ;
}
private static boolean tom_equal_term_Short(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Short(Object t) {
return  t instanceof java.lang.Short ;
}
private static boolean tom_equal_term_TreeIterator(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_TreeIterator(Object t) {
return  t instanceof org.eclipse.emf.common.util.TreeIterator<?> ;
}
private static boolean tom_equal_term_PetriNet(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_PetriNet(Object t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.PetriNet ;
}
private static boolean tom_equal_term_Node(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Node(Object t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.Node ;
}
private static boolean tom_equal_term_Arc(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Arc(Object t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.Arc ;
}
private static boolean tom_equal_term_ArcKind(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ArcKind(Object t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.ArcKind ;
}
private static  petrinetsemantics.DDMMPetriNet.ArcKind  tom_make_normal() { 
return  (petrinetsemantics.DDMMPetriNet.ArcKind)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.createFromString( (EDataType)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getArcKind(), "normal") ;/*toto*/
}
private static  petrinetsemantics.DDMMPetriNet.ArcKind  tom_make_read_arc() { 
return  (petrinetsemantics.DDMMPetriNet.ArcKind)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.createFromString( (EDataType)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getArcKind(), "read_arc") ;/*toto*/
}
private static boolean tom_is_fun_sym_Arc( petrinetsemantics.DDMMPetriNet.Arc  t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.Arc ;
}
private static  petrinetsemantics.DDMMPetriNet.Arc  tom_make_Arc( petrinetsemantics.DDMMPetriNet.Node  target,  petrinetsemantics.DDMMPetriNet.Node  source,  petrinetsemantics.DDMMPetriNet.PetriNet  net,  petrinetsemantics.DDMMPetriNet.ArcKind  kind,  int  weight) { 
return  constructArc((petrinetsemantics.DDMMPetriNet.Arc)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.create((EClass)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getEClassifier("Arc")), new Object[]{ target, source, net, kind, weight }) ;/*toto*/
}
private static  petrinetsemantics.DDMMPetriNet.Node  tom_get_slot_Arc_target( petrinetsemantics.DDMMPetriNet.Arc  t) {
return  (petrinetsemantics.DDMMPetriNet.Node)t.eGet(t.eClass().getEStructuralFeature("target")) ;
}
private static  petrinetsemantics.DDMMPetriNet.Node  tom_get_slot_Arc_source( petrinetsemantics.DDMMPetriNet.Arc  t) {
return  (petrinetsemantics.DDMMPetriNet.Node)t.eGet(t.eClass().getEStructuralFeature("source")) ;
}
private static  petrinetsemantics.DDMMPetriNet.PetriNet  tom_get_slot_Arc_net( petrinetsemantics.DDMMPetriNet.Arc  t) {
return  (petrinetsemantics.DDMMPetriNet.PetriNet)t.eGet(t.eClass().getEStructuralFeature("net")) ;
}
private static  petrinetsemantics.DDMMPetriNet.ArcKind  tom_get_slot_Arc_kind( petrinetsemantics.DDMMPetriNet.Arc  t) {
return  (petrinetsemantics.DDMMPetriNet.ArcKind)t.eGet(t.eClass().getEStructuralFeature("kind")) ;
}
private static  int  tom_get_slot_Arc_weight( petrinetsemantics.DDMMPetriNet.Arc  t) {
return  (java.lang.Integer)t.eGet(t.eClass().getEStructuralFeature("weight")) ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructArc(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_ArcEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ArcEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>)t).size()>0 && ((org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>)t).get(0) instanceof petrinetsemantics.DDMMPetriNet.Arc)) ;
}
private static boolean tom_is_fun_sym_ArcEList( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (t.size() == 0 || (t.size()>0 && t.get(0) instanceof petrinetsemantics.DDMMPetriNet.Arc)) ;
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_empty_array_ArcEList(int n) { 
return  new org.eclipse.emf.common.util.BasicEList<petrinetsemantics.DDMMPetriNet.Arc>(n) ;/*toto*/
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_cons_array_ArcEList( petrinetsemantics.DDMMPetriNet.Arc  e,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  l) { 
return  appendArcEList(e,l) ;/*toto*/
}
private static  petrinetsemantics.DDMMPetriNet.Arc  tom_get_element_ArcEList_ArcEList( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  l, int n) {
return  l.get(n) ;
}
private static int tom_get_size_ArcEList_ArcEList( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  l) {
return  l.size() ;
}

  private static   org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_get_slice_ArcEList( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  subject, int begin, int end) {
     org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  result =  new org.eclipse.emf.common.util.BasicEList<petrinetsemantics.DDMMPetriNet.Arc>(end-begin) ;
    while(begin!=end) {
      result =  appendArcEList( subject.get(begin) ,result) ;
      begin++;
    }
    return result;
  }

  private static   org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_append_array_ArcEList( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  l2,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  l1) {
    int size1 =  l1.size() ;
    int size2 =  l2.size() ;
    int index;
     org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  result =  new org.eclipse.emf.common.util.BasicEList<petrinetsemantics.DDMMPetriNet.Arc>(size1+size2) ;
    index=size1;
    while(index >0) {
      result =  appendArcEList( l1.get(size1-index) ,result) ;
      index--;
    }

    index=size2;
    while(index > 0) {
      result =  appendArcEList( l2.get(size2-index) ,result) ;
      index--;
    }
    return result;
  }

private static <O> org.eclipse.emf.common.util.EList<O> appendArcEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_equal_term_NodeEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_NodeEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>)t).size()>0 && ((org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>)t).get(0) instanceof petrinetsemantics.DDMMPetriNet.Node)) ;
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>  tom_empty_array_NodeEList(int n) { 
return  new org.eclipse.emf.common.util.BasicEList<petrinetsemantics.DDMMPetriNet.Node>(n) ;/*toto*/
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>  tom_cons_array_NodeEList( petrinetsemantics.DDMMPetriNet.Node  e,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>  l) { 
return  appendNodeEList(e,l) ;/*toto*/
}


private static <O> org.eclipse.emf.common.util.EList<O> appendNodeEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static  petrinetsemantics.DDMMPetriNet.PetriNet  tom_make_PetriNet( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>  nodes,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  arcs,  String  name) { 
return  constructPetriNet((petrinetsemantics.DDMMPetriNet.PetriNet)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.create((EClass)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getEClassifier("PetriNet")), new Object[]{ nodes, arcs, name }) ;/*toto*/
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructPetriNet(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_Transition(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Transition(Object t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.Transition ;
}
private static boolean tom_is_fun_sym_Transition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.Transition ;
}
private static  petrinetsemantics.DDMMPetriNet.Transition  tom_make_Transition( String  name,  petrinetsemantics.DDMMPetriNet.PetriNet  net,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  outgoings,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  incomings,  int  min_time,  int  max_time) { 
return 
    constructTransition((petrinetsemantics.DDMMPetriNet.Transition)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.createTransition(), new Object[]{ name, net, outgoings, incomings, min_time, max_time }) ;/*toto*/
}
private static  String  tom_get_slot_Transition_name( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  (java.lang.String)t.eGet(t.eClass().getEStructuralFeature("name")) ;
}
private static  petrinetsemantics.DDMMPetriNet.PetriNet  tom_get_slot_Transition_net( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  (petrinetsemantics.DDMMPetriNet.PetriNet)t.eGet(t.eClass().getEStructuralFeature("net")) ;
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_get_slot_Transition_outgoings( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  (org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>)t.eGet(t.eClass().getEStructuralFeature("outgoings")) ;
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_get_slot_Transition_incomings( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  (org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>)t.eGet(t.eClass().getEStructuralFeature("incomings")) ;
}
private static  int  tom_get_slot_Transition_min_time( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  (java.lang.Integer)t.eGet(t.eClass().getEStructuralFeature("min_time")) ;
}
private static  int  tom_get_slot_Transition_max_time( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  (java.lang.Integer)t.eGet(t.eClass().getEStructuralFeature("max_time")) ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructTransition(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_Place(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Place(Object t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.Place ;
}
private static boolean tom_is_fun_sym_Place( petrinetsemantics.DDMMPetriNet.Place  t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.Place ;
}
private static  petrinetsemantics.DDMMPetriNet.Place  tom_make_Place( String  name,  petrinetsemantics.DDMMPetriNet.PetriNet  net,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  outgoings,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  incomings,  int  initialMarking) { 
return  constructPlace((petrinetsemantics.DDMMPetriNet.Place)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.create((EClass)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getEClassifier("Place")), new Object[]{ name, net, outgoings, incomings, initialMarking }) ;/*toto*/
}
private static  String  tom_get_slot_Place_name( petrinetsemantics.DDMMPetriNet.Place  t) {
return  (java.lang.String)t.eGet(t.eClass().getEStructuralFeature("name")) ;
}
private static  petrinetsemantics.DDMMPetriNet.PetriNet  tom_get_slot_Place_net( petrinetsemantics.DDMMPetriNet.Place  t) {
return  (petrinetsemantics.DDMMPetriNet.PetriNet)t.eGet(t.eClass().getEStructuralFeature("net")) ;
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_get_slot_Place_outgoings( petrinetsemantics.DDMMPetriNet.Place  t) {
return  (org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>)t.eGet(t.eClass().getEStructuralFeature("outgoings")) ;
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_get_slot_Place_incomings( petrinetsemantics.DDMMPetriNet.Place  t) {
return  (org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>)t.eGet(t.eClass().getEStructuralFeature("incomings")) ;
}
private static  int  tom_get_slot_Place_initialMarking( petrinetsemantics.DDMMPetriNet.Place  t) {
return  (java.lang.Integer)t.eGet(t.eClass().getEStructuralFeature("initialMarking")) ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructPlace(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_Process(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Process(Object t) {
return  t instanceof SimplePDLSemantics.DDMMSimplePDL.Process ;
}
private static boolean tom_equal_term_ProcessElement(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ProcessElement(Object t) {
return  t instanceof SimplePDLSemantics.DDMMSimplePDL.ProcessElement ;
}
private static boolean tom_equal_term_ProcessElementEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ProcessElementEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>)t).size()>0 && ((org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>)t).get(0) instanceof SimplePDLSemantics.DDMMSimplePDL.ProcessElement)) ;
}
private static boolean tom_is_fun_sym_ProcessElementEList( org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (t.size() == 0 || (t.size()>0 && t.get(0) instanceof SimplePDLSemantics.DDMMSimplePDL.ProcessElement)) ;
}
private static  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  tom_empty_array_ProcessElementEList(int n) { 
return  new org.eclipse.emf.common.util.BasicEList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>(n) ;/*toto*/
}
private static  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  tom_cons_array_ProcessElementEList( SimplePDLSemantics.DDMMSimplePDL.ProcessElement  e,  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  l) { 
return  appendProcessElementEList(e,l) ;/*toto*/
}
private static  SimplePDLSemantics.DDMMSimplePDL.ProcessElement  tom_get_element_ProcessElementEList_ProcessElementEList( org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  l, int n) {
return  l.get(n) ;
}
private static int tom_get_size_ProcessElementEList_ProcessElementEList( org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  l) {
return  l.size() ;
}

  private static   org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  tom_get_slice_ProcessElementEList( org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  subject, int begin, int end) {
     org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  result =  new org.eclipse.emf.common.util.BasicEList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>(end-begin) ;
    while(begin!=end) {
      result =  appendProcessElementEList( subject.get(begin) ,result) ;
      begin++;
    }
    return result;
  }

  private static   org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  tom_append_array_ProcessElementEList( org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  l2,  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  l1) {
    int size1 =  l1.size() ;
    int size2 =  l2.size() ;
    int index;
     org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  result =  new org.eclipse.emf.common.util.BasicEList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>(size1+size2) ;
    index=size1;
    while(index >0) {
      result =  appendProcessElementEList( l1.get(size1-index) ,result) ;
      index--;
    }

    index=size2;
    while(index > 0) {
      result =  appendProcessElementEList( l2.get(size2-index) ,result) ;
      index--;
    }
    return result;
  }

private static <O> org.eclipse.emf.common.util.EList<O> appendProcessElementEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_equal_term_WorkDefinition(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_WorkDefinition(Object t) {
return  t instanceof SimplePDLSemantics.DDMMSimplePDL.WorkDefinition ;
}
private static boolean tom_equal_term_WorkSequence(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_WorkSequence(Object t) {
return  t instanceof SimplePDLSemantics.DDMMSimplePDL.WorkSequence ;
}
private static boolean tom_equal_term_WorkSequenceType(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_WorkSequenceType(Object t) {
return  t instanceof SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType ;
}
private static boolean tom_is_fun_sym_startToStart( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  t) {
return  t == SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType.get("startToStart") ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tom_make_startToStart() { 
return  (SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLFactory.eINSTANCE.createFromString( (EDataType)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage.eINSTANCE.getWorkSequenceType(), "startToStart") ;/*toto*/
}
private static boolean tom_is_fun_sym_finishToStart( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  t) {
return  t == SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType.get("finishToStart") ;
}
private static boolean tom_is_fun_sym_startToFinish( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  t) {
return  t == SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType.get("startToFinish") ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tom_make_startToFinish() { 
return  (SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLFactory.eINSTANCE.createFromString( (EDataType)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage.eINSTANCE.getWorkSequenceType(), "startToFinish") ;/*toto*/
}
private static boolean tom_is_fun_sym_finishToFinish( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  t) {
return  t == SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType.get("finishToFinish") ;
}
private static boolean tom_is_fun_sym_WorkSequence( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  t) {
return  t instanceof SimplePDLSemantics.DDMMSimplePDL.WorkSequence ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkSequence  tom_make_WorkSequence( SimplePDLSemantics.DDMMSimplePDL.Process  parent,  SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  linkType,  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  predecessor,  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  successor) { 
return  constructWorkSequence((SimplePDLSemantics.DDMMSimplePDL.WorkSequence)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLFactory.eINSTANCE.create((EClass)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage.eINSTANCE.getEClassifier("WorkSequence")), new Object[]{ parent, linkType, predecessor, successor }) ;/*toto*/
}
private static  SimplePDLSemantics.DDMMSimplePDL.Process  tom_get_slot_WorkSequence_parent( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  t) {
return  (SimplePDLSemantics.DDMMSimplePDL.Process)t.eGet(t.eClass().getEStructuralFeature("parent")) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tom_get_slot_WorkSequence_linkType( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  t) {
return  (SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType)t.eGet(t.eClass().getEStructuralFeature("linkType")) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  tom_get_slot_WorkSequence_predecessor( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  t) {
return  (SimplePDLSemantics.DDMMSimplePDL.WorkDefinition)t.eGet(t.eClass().getEStructuralFeature("predecessor")) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  tom_get_slot_WorkSequence_successor( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  t) {
return  (SimplePDLSemantics.DDMMSimplePDL.WorkDefinition)t.eGet(t.eClass().getEStructuralFeature("successor")) ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructWorkSequence(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_WorkSequenceEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_WorkSequenceEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>)t).size()>0 && ((org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>)t).get(0) instanceof SimplePDLSemantics.DDMMSimplePDL.WorkSequence)) ;
}
private static  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>  tom_empty_array_WorkSequenceEList(int n) { 
return  new org.eclipse.emf.common.util.BasicEList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>(n) ;/*toto*/
}
private static  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>  tom_cons_array_WorkSequenceEList( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  e,  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>  l) { 
return  appendWorkSequenceEList(e,l) ;/*toto*/
}


private static <O> org.eclipse.emf.common.util.EList<O> appendWorkSequenceEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_is_fun_sym_WorkDefinition( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  t) {
return  t instanceof SimplePDLSemantics.DDMMSimplePDL.WorkDefinition ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  tom_make_WorkDefinition( SimplePDLSemantics.DDMMSimplePDL.Process  parent,  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>  linksToPredecessors,  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>  linksToSuccessors,  String  name,  SimplePDLSemantics.DDMMSimplePDL.Process  process) { 
return  constructWorkDefinition((SimplePDLSemantics.DDMMSimplePDL.WorkDefinition)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLFactory.eINSTANCE.create((EClass)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage.eINSTANCE.getEClassifier("WorkDefinition")), new Object[]{ parent, linksToPredecessors, linksToSuccessors, name, process }) ;/*toto*/
}
private static  SimplePDLSemantics.DDMMSimplePDL.Process  tom_get_slot_WorkDefinition_parent( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  t) {
return  (SimplePDLSemantics.DDMMSimplePDL.Process)t.eGet(t.eClass().getEStructuralFeature("parent")) ;
}
private static  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>  tom_get_slot_WorkDefinition_linksToPredecessors( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  t) {
return  (org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>)t.eGet(t.eClass().getEStructuralFeature("linksToPredecessors")) ;
}
private static  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>  tom_get_slot_WorkDefinition_linksToSuccessors( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  t) {
return  (org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.WorkSequence>)t.eGet(t.eClass().getEStructuralFeature("linksToSuccessors")) ;
}
private static  String  tom_get_slot_WorkDefinition_name( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  t) {
return  (java.lang.String)t.eGet(t.eClass().getEStructuralFeature("name")) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.Process  tom_get_slot_WorkDefinition_process( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  t) {
return  (SimplePDLSemantics.DDMMSimplePDL.Process)t.eGet(t.eClass().getEStructuralFeature("process")) ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructWorkDefinition(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_is_fun_sym_Process( SimplePDLSemantics.DDMMSimplePDL.Process  t) {
return  t instanceof SimplePDLSemantics.DDMMSimplePDL.Process ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.Process  tom_make_Process( String  name,  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  processElements,  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  from) { 
return  constructProcess((SimplePDLSemantics.DDMMSimplePDL.Process)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLFactory.eINSTANCE.create((EClass)SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage.eINSTANCE.getEClassifier("Process")), new Object[]{ name, processElements, from }) ;/*toto*/
}
private static  String  tom_get_slot_Process_name( SimplePDLSemantics.DDMMSimplePDL.Process  t) {
return  (java.lang.String)t.eGet(t.eClass().getEStructuralFeature("name")) ;
}
private static  org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>  tom_get_slot_Process_processElements( SimplePDLSemantics.DDMMSimplePDL.Process  t) {
return  (org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement>)t.eGet(t.eClass().getEStructuralFeature("processElements")) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  tom_get_slot_Process_from( SimplePDLSemantics.DDMMSimplePDL.Process  t) {
return  (SimplePDLSemantics.DDMMSimplePDL.WorkDefinition)t.eGet(t.eClass().getEStructuralFeature("from")) ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructProcess(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_Guidance(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Guidance(Object t) {
return  t instanceof SimplePDLSemantics.DDMMSimplePDL.Guidance ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructGuidance(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_PetriNetEvent(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_PetriNetEvent(Object t) {
return  t instanceof petrinetsemantics.EDMMPetriNet.PetriNetEvent ;
}
private static boolean tom_equal_term_FireTransitionEvent(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_FireTransitionEvent(Object t) {
return  t instanceof petrinetsemantics.EDMMPetriNet.FireTransitionEvent ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructFireTransitionEvent(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_Event(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Event(Object t) {
return  t instanceof SimplePDLSemantics.EDMMSimplePDL.Event ;
}
private static boolean tom_equal_term_WorkDefinitionEvent(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_WorkDefinitionEvent(Object t) {
return  t instanceof SimplePDLSemantics.EDMMSimplePDL.WorkDefinitionEvent ;
}
private static boolean tom_equal_term_StartWD(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_StartWD(Object t) {
return  t instanceof SimplePDLSemantics.EDMMSimplePDL.StartWD ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructStartWD(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_FinishWD(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_FinishWD(Object t) {
return  t instanceof SimplePDLSemantics.EDMMSimplePDL.FinishWD ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructFinishWD(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_Node_dynamic(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Node_dynamic(Object t) {
return  t instanceof petrinetsemantics.SDMMPetriNet.Node_dynamic ;
}
private static boolean tom_equal_term_Place_dynamic(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Place_dynamic(Object t) {
return  t instanceof petrinetsemantics.SDMMPetriNet.Place_dynamic ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructPlace_dynamic(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_PetriNet_dynamic(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_PetriNet_dynamic(Object t) {
return  t instanceof petrinetsemantics.SDMMPetriNet.PetriNet_dynamic ;
}
private static boolean tom_equal_term_Node_dynamicEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Node_dynamicEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<petrinetsemantics.SDMMPetriNet.Node_dynamic>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<petrinetsemantics.SDMMPetriNet.Node_dynamic>)t).size()>0 && ((org.eclipse.emf.common.util.EList<petrinetsemantics.SDMMPetriNet.Node_dynamic>)t).get(0) instanceof petrinetsemantics.SDMMPetriNet.Node_dynamic)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendNode_dynamicEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructPetriNet_dynamic(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_DynamicWorkDefinition(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_DynamicWorkDefinition(Object t) {
return  t instanceof SimplePDLSemantics.SDMMSimplePDL.DynamicWorkDefinition ;
}
private static boolean tom_equal_term_ExecutionState(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ExecutionState(Object t) {
return  t instanceof SimplePDLSemantics.SDMMSimplePDL.ExecutionState ;
}
private static boolean tom_equal_term_TimeState(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_TimeState(Object t) {
return  t instanceof SimplePDLSemantics.SDMMSimplePDL.TimeState ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructDynamicWorkDefinition(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_PNScenario(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_PNScenario(Object t) {
return  t instanceof petrinetsemantics.TM3PetriNet.PNScenario ;
}
private static boolean tom_equal_term_PNTrace(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_PNTrace(Object t) {
return  t instanceof petrinetsemantics.TM3PetriNet.PNTrace ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructPNTrace(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_PNTraceEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_PNTraceEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<petrinetsemantics.TM3PetriNet.PNTrace>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<petrinetsemantics.TM3PetriNet.PNTrace>)t).size()>0 && ((org.eclipse.emf.common.util.EList<petrinetsemantics.TM3PetriNet.PNTrace>)t).get(0) instanceof petrinetsemantics.TM3PetriNet.PNTrace)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendPNTraceEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_equal_term_PNSimEvent(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_PNSimEvent(Object t) {
return  t instanceof petrinetsemantics.TM3PetriNet.PNSimEvent ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructPNSimEvent(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_PNSimEventEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_PNSimEventEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<petrinetsemantics.TM3PetriNet.PNSimEvent>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<petrinetsemantics.TM3PetriNet.PNSimEvent>)t).size()>0 && ((org.eclipse.emf.common.util.EList<petrinetsemantics.TM3PetriNet.PNSimEvent>)t).get(0) instanceof petrinetsemantics.TM3PetriNet.PNSimEvent)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendPNSimEventEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructPNScenario(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_SPDLScenario(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_SPDLScenario(Object t) {
return  t instanceof SimplePDLSemantics.TM3SimplePDL.SPDLScenario ;
}
private static boolean tom_equal_term_SPDLTrace(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_SPDLTrace(Object t) {
return  t instanceof SimplePDLSemantics.TM3SimplePDL.SPDLTrace ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructSPDLTrace(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_SPDLTraceEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_SPDLTraceEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<SimplePDLSemantics.TM3SimplePDL.SPDLTrace>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<SimplePDLSemantics.TM3SimplePDL.SPDLTrace>)t).size()>0 && ((org.eclipse.emf.common.util.EList<SimplePDLSemantics.TM3SimplePDL.SPDLTrace>)t).get(0) instanceof SimplePDLSemantics.TM3SimplePDL.SPDLTrace)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendSPDLTraceEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_equal_term_SPDLSimEvent(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_SPDLSimEvent(Object t) {
return  t instanceof SimplePDLSemantics.TM3SimplePDL.SPDLSimEvent ;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructSPDLSimEvent(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}
private static boolean tom_equal_term_SPDLSimEventEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_SPDLSimEventEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<SimplePDLSemantics.TM3SimplePDL.SPDLSimEvent>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<SimplePDLSemantics.TM3SimplePDL.SPDLSimEvent>)t).size()>0 && ((org.eclipse.emf.common.util.EList<SimplePDLSemantics.TM3SimplePDL.SPDLSimEvent>)t).get(0) instanceof SimplePDLSemantics.TM3SimplePDL.SPDLSimEvent)) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendSPDLSimEventEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}


public static <O extends org.eclipse.emf.ecore.EObject> O constructSPDLScenario(O o, Object[] objs) {
int i=0;
EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();
for(EStructuralFeature esf : sfes) {
if(esf.isChangeable()) {
o.eSet(esf, objs[i]);
i++;
}
}
return o;
}


private static PetriNet pn = null;
private static LinkClass tom__linkClass;

public SimplePDLToPetriNoHash() {
this.tom__linkClass = new LinkClass();
}




public static class tom__reference_class_P2PN implements tom.library.utils.ReferenceClass {
  
  private Node t_start;
  public Node gett_start() { return t_start; }
  public void sett_start(Node value) { this.t_start = value; }

  private Node t_finish;
  public Node gett_finish() { return t_finish; }
  public void sett_finish(Node value) { this.t_finish = value; }

  public Object get(String name) {
    if(name.equals("t_start")) {
        return gett_start();
    } else if(name.equals("t_finish")) {
        return gett_finish();
    } else  {
      throw new RuntimeException("This field does not exist:" + name);
    }
  }

}

public static class P2PN extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.utils.LinkClass  tom__linkClass;
private  petrinetsemantics.DDMMPetriNet.PetriNet  pn;
public P2PN( tom.library.utils.LinkClass  tom__linkClass,  petrinetsemantics.DDMMPetriNet.PetriNet  pn) {
super(tom_make_Identity());
this.tom__linkClass=tom__linkClass;
this.pn=pn;
}
public  tom.library.utils.LinkClass  gettom__linkClass() {
return tom__linkClass;
}
public  petrinetsemantics.DDMMPetriNet.PetriNet  getpn() {
return pn;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (tom_is_sort_Process(v)) {
return ((T)visit_Process((( SimplePDLSemantics.DDMMSimplePDL.Process )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  SimplePDLSemantics.DDMMSimplePDL.Process  _visit_Process( SimplePDLSemantics.DDMMSimplePDL.Process  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( SimplePDLSemantics.DDMMSimplePDL.Process )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  SimplePDLSemantics.DDMMSimplePDL.Process  visit_Process( SimplePDLSemantics.DDMMSimplePDL.Process  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_Process(((Object)tom__arg))) {
if (tom_is_sort_Process((( SimplePDLSemantics.DDMMSimplePDL.Process )((Object)tom__arg)))) {
if (tom_is_fun_sym_Process((( SimplePDLSemantics.DDMMSimplePDL.Process )(( SimplePDLSemantics.DDMMSimplePDL.Process )((Object)tom__arg))))) {
 String  tom_name=tom_get_slot_Process_name((( SimplePDLSemantics.DDMMSimplePDL.Process )((Object)tom__arg)));
 SimplePDLSemantics.DDMMSimplePDL.Process  tom_p=(( SimplePDLSemantics.DDMMSimplePDL.Process )((Object)tom__arg));

Node p_ready  = 
tom_make_Place(tom_name+ "_ready",pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1);
Node p_running  = 
tom_make_Place(tom_name+ "_running",pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
Node p_finished  = 
tom_make_Place(tom_name+ "_finished",pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
String n1 = 
tom_name+"_start";

Node t_start = tom_make_Transition(n1,pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1,1);
;
n1 = 
tom_name+"_finish";

Node t_finish = tom_make_Transition(n1,pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1,1);
;


tom_make_Arc(t_start,p_ready,pn,tom_make_normal(),1);

tom_make_Arc(p_running,t_start,pn,tom_make_normal(),1);

tom_make_Arc(t_finish,p_running,pn,tom_make_normal(),1);

tom_make_Arc(p_finished,t_finish,pn,tom_make_normal(),1);

WorkDefinition from = 
tom_p.getFrom();
if (from!=null) {
Transition source = 
tom_make_ResolveWorkDefinitionTransition(from,"t_start");
source.setNet(pn);
Arc tmpZoomIn = 
tom_make_Arc(p_ready,source,pn,tom_make_normal(),1);

Transition target = 
tom_make_ResolveWorkDefinitionTransition(from,"t_finish");
target.setNet(pn);
Arc tmpZoomOut = 
tom_make_Arc(target,p_finished,pn,tom_make_read_arc(),1);
}

tom__reference_class_P2PN var_tom__reference_class_p2pn = new tom__reference_class_P2PN();var_tom__reference_class_p2pn.sett_start(t_start);var_tom__reference_class_p2pn.sett_finish(t_finish);tom__linkClass.put(tom_p,var_tom__reference_class_p2pn);
}
}
}

}

}
return _visit_Process(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_P2PN( tom.library.utils.LinkClass  t0,  petrinetsemantics.DDMMPetriNet.PetriNet  t1) { 
return new P2PN(t0,t1);/*toto*/
}

public static class tom__reference_class_WD2PN implements tom.library.utils.ReferenceClass {
  
  private Node p_started;
  public Node getp_started() { return p_started; }
  public void setp_started(Node value) { this.p_started = value; }

  private Node p_finished;
  public Node getp_finished() { return p_finished; }
  public void setp_finished(Node value) { this.p_finished = value; }

  private Node t_start;
  public Node gett_start() { return t_start; }
  public void sett_start(Node value) { this.t_start = value; }

  private Node t_finish;
  public Node gett_finish() { return t_finish; }
  public void sett_finish(Node value) { this.t_finish = value; }

  public Object get(String name) {
    if(name.equals("p_started")) {
        return getp_started();
    } else if(name.equals("p_finished")) {
        return getp_finished();
    } else if(name.equals("t_start")) {
        return gett_start();
    } else if(name.equals("t_finish")) {
        return gett_finish();
    } else  {
      throw new RuntimeException("This field does not exist:" + name);
    }
  }

}

public static class WD2PN extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.utils.LinkClass  tom__linkClass;
private  petrinetsemantics.DDMMPetriNet.PetriNet  pn;
public WD2PN( tom.library.utils.LinkClass  tom__linkClass,  petrinetsemantics.DDMMPetriNet.PetriNet  pn) {
super(tom_make_Identity());
this.tom__linkClass=tom__linkClass;
this.pn=pn;
}
public  tom.library.utils.LinkClass  gettom__linkClass() {
return tom__linkClass;
}
public  petrinetsemantics.DDMMPetriNet.PetriNet  getpn() {
return pn;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (tom_is_sort_WorkDefinition(v)) {
return ((T)visit_WorkDefinition((( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  _visit_WorkDefinition( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  visit_WorkDefinition( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_WorkDefinition(((Object)tom__arg))) {
if (tom_is_sort_WorkDefinition((( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition )((Object)tom__arg)))) {
if (tom_is_fun_sym_WorkDefinition((( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition )(( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition )((Object)tom__arg))))) {
 String  tom_name=tom_get_slot_WorkDefinition_name((( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition )((Object)tom__arg)));
 SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  tom_wd=(( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition )((Object)tom__arg));

Node p_ready  = 
tom_make_Place(tom_name+ "_ready",pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1);
String n1 = 
tom_name+"_started";

Node p_started = tom_make_Place(n1,pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
;
Node p_running  = 
tom_make_Place(tom_name+"_running",pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
n1 = 
tom_name+"_finished";

Node p_finished = tom_make_Place(n1,pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
;
n1 = 
tom_name+"_start";

Node t_start = tom_make_Transition(n1,pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1,1);
;
n1 = 
tom_name+"_finish";

Node t_finish = tom_make_Transition(n1,pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1,1);
;


tom_make_Arc(t_start,p_ready,pn,tom_make_normal(),1);

tom_make_Arc(p_started,t_start,pn,tom_make_normal(),1);

tom_make_Arc(p_running,t_start,pn,tom_make_normal(),1);

tom_make_Arc(t_finish,p_running,pn,tom_make_normal(),1);

tom_make_Arc(p_finished,t_finish,pn,tom_make_normal(),1);

SimplePDLSemantics.DDMMSimplePDL.Process parent = 
tom_wd.getParent();
Transition source = 
tom_make_ResolveProcessTransition(parent,"t_start");
source.setNet(pn);
Arc tmpDistribute = 
tom_make_Arc(p_ready,source,pn,tom_make_normal(),1);

Transition target = 
tom_make_ResolveProcessTransition(parent,"t_finish");
target.setNet(pn);
Arc tmpRejoin = 
tom_make_Arc(target,p_finished,pn,tom_make_read_arc(),1);

tom__reference_class_WD2PN var_tom__reference_class_wd2pn = new tom__reference_class_WD2PN();var_tom__reference_class_wd2pn.setp_started(p_started);var_tom__reference_class_wd2pn.setp_finished(p_finished);var_tom__reference_class_wd2pn.sett_start(t_start);var_tom__reference_class_wd2pn.sett_finish(t_finish);tom__linkClass.put(tom_wd,var_tom__reference_class_wd2pn);
}
}
}

}

}
return _visit_WorkDefinition(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_WD2PN( tom.library.utils.LinkClass  t0,  petrinetsemantics.DDMMPetriNet.PetriNet  t1) { 
return new WD2PN(t0,t1);/*toto*/
}

public static class tom__reference_class_WS2PN implements tom.library.utils.ReferenceClass {
  
  public Object get(String name) {
     {
      throw new RuntimeException("This field does not exist:" + name);
    }
  }

}

public static class WS2PN extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.utils.LinkClass  tom__linkClass;
private  petrinetsemantics.DDMMPetriNet.PetriNet  pn;
public WS2PN( tom.library.utils.LinkClass  tom__linkClass,  petrinetsemantics.DDMMPetriNet.PetriNet  pn) {
super(tom_make_Identity());
this.tom__linkClass=tom__linkClass;
this.pn=pn;
}
public  tom.library.utils.LinkClass  gettom__linkClass() {
return tom__linkClass;
}
public  petrinetsemantics.DDMMPetriNet.PetriNet  getpn() {
return pn;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (tom_is_sort_WorkSequence(v)) {
return ((T)visit_WorkSequence((( SimplePDLSemantics.DDMMSimplePDL.WorkSequence )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  SimplePDLSemantics.DDMMSimplePDL.WorkSequence  _visit_WorkSequence( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( SimplePDLSemantics.DDMMSimplePDL.WorkSequence )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  SimplePDLSemantics.DDMMSimplePDL.WorkSequence  visit_WorkSequence( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_WorkSequence(((Object)tom__arg))) {
if (tom_is_sort_WorkSequence((( SimplePDLSemantics.DDMMSimplePDL.WorkSequence )((Object)tom__arg)))) {
if (tom_is_fun_sym_WorkSequence((( SimplePDLSemantics.DDMMSimplePDL.WorkSequence )(( SimplePDLSemantics.DDMMSimplePDL.WorkSequence )((Object)tom__arg))))) {
 SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tom_linkType=tom_get_slot_WorkSequence_linkType((( SimplePDLSemantics.DDMMSimplePDL.WorkSequence )((Object)tom__arg)));

Place source= null;
Transition target= null;
WorkDefinition pre = 
tom_get_slot_WorkSequence_predecessor((( SimplePDLSemantics.DDMMSimplePDL.WorkSequence )((Object)tom__arg)));
WorkDefinition suc = 
tom_get_slot_WorkSequence_successor((( SimplePDLSemantics.DDMMSimplePDL.WorkSequence )((Object)tom__arg)));

{
{
if (tom_is_sort_WorkSequenceType(((Object)tom_linkType))) {
boolean tomMatch4_4= false ;
 SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tomMatch4_3= null ;
 SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tomMatch4_2= null ;
if (tom_is_sort_WorkSequenceType((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_finishToFinish((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch4_4= true ;
tomMatch4_2=(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType));

}
} else {
if (tom_is_sort_WorkSequenceType((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_finishToStart((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch4_4= true ;
tomMatch4_3=(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType));

}
}
}
}
}
if (tomMatch4_4) {
source = 
tom_make_ResolveWorkDefinitionPlace(pre,"p_finished"); 

}

}

}
{
if (tom_is_sort_WorkSequenceType(((Object)tom_linkType))) {
boolean tomMatch4_9= false ;
 SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tomMatch4_7= null ;
 SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tomMatch4_8= null ;
if (tom_is_sort_WorkSequenceType((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_startToStart((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch4_9= true ;
tomMatch4_7=(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType));

}
} else {
if (tom_is_sort_WorkSequenceType((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_startToFinish((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch4_9= true ;
tomMatch4_8=(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType));

}
}
}
}
}
if (tomMatch4_9) {
source = 
tom_make_ResolveWorkDefinitionPlace(pre,"p_started"); 

}

}

}
{
if (tom_is_sort_WorkSequenceType(((Object)tom_linkType))) {
boolean tomMatch4_14= false ;
 SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tomMatch4_12= null ;
 SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tomMatch4_13= null ;
if (tom_is_sort_WorkSequenceType((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_finishToStart((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch4_14= true ;
tomMatch4_12=(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType));

}
} else {
if (tom_is_sort_WorkSequenceType((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_startToStart((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch4_14= true ;
tomMatch4_13=(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType));

}
}
}
}
}
if (tomMatch4_14) {
target = 
tom_make_ResolveWorkDefinitionTransition(suc,"t_start"); 

}

}

}
{
if (tom_is_sort_WorkSequenceType(((Object)tom_linkType))) {
boolean tomMatch4_19= false ;
 SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tomMatch4_18= null ;
 SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType  tomMatch4_17= null ;
if (tom_is_sort_WorkSequenceType((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_startToFinish((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch4_19= true ;
tomMatch4_17=(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType));

}
} else {
if (tom_is_sort_WorkSequenceType((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_finishToFinish((( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch4_19= true ;
tomMatch4_18=(( SimplePDLSemantics.DDMMSimplePDL.WorkSequenceType )((Object)tom_linkType));

}
}
}
}
}
if (tomMatch4_19) {
target = 
tom_make_ResolveWorkDefinitionTransition(suc,"t_finish"); 

}

}

}


}

source.setNet(pn);
target.setNet(pn);

Arc tmp = 
tom_make_Arc(target,source,pn,tom_make_read_arc(),1);  

tom__reference_class_WS2PN var_tom__reference_class_ws2pn = new tom__reference_class_WS2PN();tom__linkClass.put((( SimplePDLSemantics.DDMMSimplePDL.WorkSequence )((Object)tom__arg)),var_tom__reference_class_ws2pn);
}
}
}

}

}
return _visit_WorkSequence(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_WS2PN( tom.library.utils.LinkClass  t0,  petrinetsemantics.DDMMPetriNet.PetriNet  t1) { 
return new WS2PN(t0,t1);/*toto*/
}
private static boolean tom_is_fun_sym_ResolveProcessPlace( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ( t instanceof ResolveProcessPlace ) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.Process  tom_get_slot_ResolveProcessPlace_o( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ((ResolveProcessPlace)t).o ;
}
private static  String  tom_get_slot_ResolveProcessPlace_name( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ((ResolveProcessPlace)t).name ;
}
private static class ResolveProcessPlace extends  petrinetsemantics.DDMMPetriNet.impl.PlaceImpl  {
  public String name;
  public  SimplePDLSemantics.DDMMSimplePDL.Process  o;

  public ResolveProcessPlace( SimplePDLSemantics.DDMMSimplePDL.Process  o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static  petrinetsemantics.DDMMPetriNet.Place  tom_make_ResolveWorkDefinitionPlace( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  o,  String  name) { 
return new ResolveWorkDefinitionPlace(o, name);/*titi*/
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionPlace( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ( t instanceof ResolveWorkDefinitionPlace ) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  tom_get_slot_ResolveWorkDefinitionPlace_o( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ((ResolveWorkDefinitionPlace)t).o ;
}
private static  String  tom_get_slot_ResolveWorkDefinitionPlace_name( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ((ResolveWorkDefinitionPlace)t).name ;
}
private static class ResolveWorkDefinitionPlace extends  petrinetsemantics.DDMMPetriNet.impl.PlaceImpl  {
  public String name;
  public  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  o;

  public ResolveWorkDefinitionPlace( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static boolean tom_is_fun_sym_ResolveWorkSequencePlace( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ( t instanceof ResolveWorkSequencePlace ) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkSequence  tom_get_slot_ResolveWorkSequencePlace_o( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ((ResolveWorkSequencePlace)t).o ;
}
private static  String  tom_get_slot_ResolveWorkSequencePlace_name( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ((ResolveWorkSequencePlace)t).name ;
}
private static class ResolveWorkSequencePlace extends  petrinetsemantics.DDMMPetriNet.impl.PlaceImpl  {
  public String name;
  public  SimplePDLSemantics.DDMMSimplePDL.WorkSequence  o;

  public ResolveWorkSequencePlace( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static  petrinetsemantics.DDMMPetriNet.Transition  tom_make_ResolveProcessTransition( SimplePDLSemantics.DDMMSimplePDL.Process  o,  String  name) { 
return new ResolveProcessTransition(o, name);/*titi*/
}
private static boolean tom_is_fun_sym_ResolveProcessTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ( t instanceof ResolveProcessTransition ) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.Process  tom_get_slot_ResolveProcessTransition_o( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveProcessTransition)t).o ;
}
private static  String  tom_get_slot_ResolveProcessTransition_name( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveProcessTransition)t).name ;
}
private static class ResolveProcessTransition extends  petrinetsemantics.DDMMPetriNet.impl.TransitionImpl  {
  public String name;
  public  SimplePDLSemantics.DDMMSimplePDL.Process  o;

  public ResolveProcessTransition( SimplePDLSemantics.DDMMSimplePDL.Process  o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static  petrinetsemantics.DDMMPetriNet.Transition  tom_make_ResolveWorkDefinitionTransition( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  o,  String  name) { 
return new ResolveWorkDefinitionTransition(o, name);/*titi*/
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ( t instanceof ResolveWorkDefinitionTransition ) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  tom_get_slot_ResolveWorkDefinitionTransition_o( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveWorkDefinitionTransition)t).o ;
}
private static  String  tom_get_slot_ResolveWorkDefinitionTransition_name( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveWorkDefinitionTransition)t).name ;
}
private static class ResolveWorkDefinitionTransition extends  petrinetsemantics.DDMMPetriNet.impl.TransitionImpl  {
  public String name;
  public  SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  o;

  public ResolveWorkDefinitionTransition( SimplePDLSemantics.DDMMSimplePDL.WorkDefinition  o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static boolean tom_is_fun_sym_ResolveWorkSequenceTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ( t instanceof ResolveWorkSequenceTransition ) ;
}
private static  SimplePDLSemantics.DDMMSimplePDL.WorkSequence  tom_get_slot_ResolveWorkSequenceTransition_o( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveWorkSequenceTransition)t).o ;
}
private static  String  tom_get_slot_ResolveWorkSequenceTransition_name( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveWorkSequenceTransition)t).name ;
}
private static class ResolveWorkSequenceTransition extends  petrinetsemantics.DDMMPetriNet.impl.TransitionImpl  {
  public String name;
  public  SimplePDLSemantics.DDMMSimplePDL.WorkSequence  o;

  public ResolveWorkSequenceTransition( SimplePDLSemantics.DDMMSimplePDL.WorkSequence  o, String name) {
    this.name = name;
    this.o = o;
  }
}
public static class tom__StratResolve_SimplePDLToPetriNet extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.utils.LinkClass  tom__linkClass;
private  petrinetsemantics.DDMMPetriNet.PetriNet  pn;
public tom__StratResolve_SimplePDLToPetriNet( tom.library.utils.LinkClass  tom__linkClass,  petrinetsemantics.DDMMPetriNet.PetriNet  pn) {
super(tom_make_Identity());
this.tom__linkClass=tom__linkClass;
this.pn=pn;
}
public  tom.library.utils.LinkClass  gettom__linkClass() {
return tom__linkClass;
}
public  petrinetsemantics.DDMMPetriNet.PetriNet  getpn() {
return pn;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (tom_is_sort_Transition(v)) {
return ((T)visit_Transition((( petrinetsemantics.DDMMPetriNet.Transition )v),introspector));
}
if (tom_is_sort_Place(v)) {
return ((T)visit_Place((( petrinetsemantics.DDMMPetriNet.Place )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Place  _visit_Place( petrinetsemantics.DDMMPetriNet.Place  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( petrinetsemantics.DDMMPetriNet.Place )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Transition  _visit_Transition( petrinetsemantics.DDMMPetriNet.Transition  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( petrinetsemantics.DDMMPetriNet.Transition )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Transition  visit_Transition( petrinetsemantics.DDMMPetriNet.Transition  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_Transition(((Object)tom__arg))) {
if (tom_is_sort_Transition((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)))) {
if (tom_is_fun_sym_ResolveProcessTransition((( petrinetsemantics.DDMMPetriNet.Transition )(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg))))) {
resolveInverseLinks(tom__arg,res,pn);return res;

}
}
}

}
{
if (tom_is_sort_Transition(((Object)tom__arg))) {
if (tom_is_sort_Transition((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)))) {
if (tom_is_fun_sym_ResolveWorkDefinitionTransition((( petrinetsemantics.DDMMPetriNet.Transition )(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg))))) {
resolveInverseLinks(tom__arg,res,pn);return res;

}
}
}

}
{
if (tom_is_sort_Transition(((Object)tom__arg))) {
if (tom_is_sort_Transition((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)))) {
if (tom_is_fun_sym_ResolveWorkSequenceTransition((( petrinetsemantics.DDMMPetriNet.Transition )(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg))))) {
resolveInverseLinks(tom__arg,res,pn);return res;

}
}
}

}


}
return _visit_Transition(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Place  visit_Place( petrinetsemantics.DDMMPetriNet.Place  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_Place(((Object)tom__arg))) {
if (tom_is_sort_Place((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg)))) {
if (tom_is_fun_sym_ResolveProcessPlace((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg))))) {
resolveInverseLinks(tom__arg,res,pn);return res;

}
}
}

}
{
if (tom_is_sort_Place(((Object)tom__arg))) {
if (tom_is_sort_Place((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg)))) {
if (tom_is_fun_sym_ResolveWorkDefinitionPlace((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg))))) {
resolveInverseLinks(tom__arg,res,pn);return res;

}
}
}

}
{
if (tom_is_sort_Place(((Object)tom__arg))) {
if (tom_is_sort_Place((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg)))) {
if (tom_is_fun_sym_ResolveWorkSequencePlace((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg))))) {
resolveInverseLinks(tom__arg,res,pn);return res;

}
}
}

}


}
return _visit_Place(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_tom__StratResolve_SimplePDLToPetriNet( tom.library.utils.LinkClass  t0,  petrinetsemantics.DDMMPetriNet.PetriNet  t1) { 
return new tom__StratResolve_SimplePDLToPetriNet(t0,t1);/*toto*/
}

  public static void resolveInverseLinks(EObject resolveNode, EObject newNode, EObject acc) {
    ECrossReferenceAdapter adapter = new ECrossReferenceAdapter();
    acc.eAdapters().add(adapter);
    Collection<EStructuralFeature.Setting> references = adapter.getInverseReferences(resolveNode);

        boolean toSet = (false
         | resolveNode instanceof ResolveProcessPlace | resolveNode instanceof ResolveWorkDefinitionPlace | resolveNode instanceof ResolveWorkSequencePlace | resolveNode instanceof ResolveProcessTransition | resolveNode instanceof ResolveWorkDefinitionTransition | resolveNode instanceof ResolveWorkSequenceTransition
        );

    for (EStructuralFeature.Setting setting:references) {
      EObject current = setting.getEObject();
if (current instanceof  petrinetsemantics.DDMMPetriNet.PetriNet ) {
         petrinetsemantics.DDMMPetriNet.PetriNet  newCurrent = ( petrinetsemantics.DDMMPetriNet.PetriNet )current;
      {
          throw new RuntimeException("should not be there");
        }
      } else if (current instanceof  petrinetsemantics.DDMMPetriNet.Transition ) {
         petrinetsemantics.DDMMPetriNet.Transition  newCurrent = ( petrinetsemantics.DDMMPetriNet.Transition )current;
      if(newCurrent.getNet().equals(resolveNode) && toSet) {
          newCurrent.setNet((PetriNet)newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      } else if (current instanceof  petrinetsemantics.DDMMPetriNet.Place ) {
         petrinetsemantics.DDMMPetriNet.Place  newCurrent = ( petrinetsemantics.DDMMPetriNet.Place )current;
      if(newCurrent.getNet().equals(resolveNode) && toSet) {
          newCurrent.setNet((PetriNet)newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      } else if (current instanceof  petrinetsemantics.DDMMPetriNet.Arc ) {
         petrinetsemantics.DDMMPetriNet.Arc  newCurrent = ( petrinetsemantics.DDMMPetriNet.Arc )current;
      if(newCurrent.getTarget().equals(resolveNode) && toSet) {
          newCurrent.setTarget((Node)newNode); 
        } else if(newCurrent.getSource().equals(resolveNode) && toSet) {
          newCurrent.setSource((Node)newNode); 
        } else if(newCurrent.getNet().equals(resolveNode) && toSet) {
          newCurrent.setNet((PetriNet)newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      } else if (current instanceof  petrinetsemantics.SDMMPetriNet.Place_dynamic ) {
         petrinetsemantics.SDMMPetriNet.Place_dynamic  newCurrent = ( petrinetsemantics.SDMMPetriNet.Place_dynamic )current;
      if(newCurrent.getNode_static().equals(resolveNode) && toSet) {
          newCurrent.setNode_static((Node)newNode); 
        } else if(newCurrent.getPlace_static().equals(resolveNode) && toSet) {
          newCurrent.setPlace_static((Place)newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      } else if (current instanceof  petrinetsemantics.SDMMPetriNet.PetriNet_dynamic ) {
         petrinetsemantics.SDMMPetriNet.PetriNet_dynamic  newCurrent = ( petrinetsemantics.SDMMPetriNet.PetriNet_dynamic )current;
      if(newCurrent.getPetriNet_static().equals(resolveNode) && toSet) {
          newCurrent.setPetriNet_static((PetriNet)newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      } else if (current instanceof  petrinetsemantics.EDMMPetriNet.FireTransitionEvent ) {
         petrinetsemantics.EDMMPetriNet.FireTransitionEvent  newCurrent = ( petrinetsemantics.EDMMPetriNet.FireTransitionEvent )current;
      if(newCurrent.getFiredTransition().equals(resolveNode) && toSet) {
          newCurrent.setFiredTransition((Transition)newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      } else if (current instanceof  petrinetsemantics.TM3PetriNet.PNScenario ) {
         petrinetsemantics.TM3PetriNet.PNScenario  newCurrent = ( petrinetsemantics.TM3PetriNet.PNScenario )current;
      {
          throw new RuntimeException("should not be there");
        }
      } else if (current instanceof  petrinetsemantics.TM3PetriNet.PNTrace ) {
         petrinetsemantics.TM3PetriNet.PNTrace  newCurrent = ( petrinetsemantics.TM3PetriNet.PNTrace )current;
      if(newCurrent.getScenario().equals(resolveNode) && toSet) {
          newCurrent.setScenario((PNScenario)newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      } else if (current instanceof  petrinetsemantics.TM3PetriNet.PNSimEvent ) {
         petrinetsemantics.TM3PetriNet.PNSimEvent  newCurrent = ( petrinetsemantics.TM3PetriNet.PNSimEvent )current;
      {
          throw new RuntimeException("should not be there");
        }
      } else  { throw new RuntimeException("should not be there"); }
    }
  }
private static  tom.library.sl.Strategy  tom_make_SimplePDLToPetriNet( tom.library.utils.LinkClass  t0,  petrinetsemantics.DDMMPetriNet.PetriNet  t1) { 
return tom_cons_list_Sequence(tom_cons_list_Sequence(tom_make_TopDown(tom_make_P2PN(tom__linkClass,pn)),tom_cons_list_Sequence(tom_make_TopDown(tom_make_WD2PN(tom__linkClass,pn)),tom_cons_list_Sequence(tom_make_TopDown(tom_make_WS2PN(tom__linkClass,pn)),tom_empty_list_Sequence()))),tom_cons_list_Sequence(tom_make_TopDown(tom_make_tom__StratResolve_SimplePDLToPetriNet(t0,t1)),tom_empty_list_Sequence()));/*toto*/
}


/**
*
* @param resolveNode temporary ResolveNode that should be replaced
* @param newNode node (stored in the HashMap) that will replace the ResolveNode
* @param translator the TimplePDLToPetri3
*/
/*  public static void resolveInverseLinks(EObject resolveNode, Node newNode, PetriNet pn) {
ECrossReferenceAdapter adapter = new ECrossReferenceAdapter(); //create an adapter
translator.pn.eAdapters().add(adapter); //attach it to PetriNet

Collection<EStructuralFeature.Setting> references = adapter.getInverseReferences(resolveNode);

boolean toSet = (false
| resolveNode instanceof ResolveWorkDefinitionPlace 
| resolveNode instanceof ResolveWorkDefinitionTransition
| resolveNode instanceof ResolveProcessTransition
);

for (EStructuralFeature.Setting setting:references) {
EObject current = setting.getEObject();
if (current instanceof Arc) {
Arc newCurrent = (Arc)current;
if(newCurrent.getSource().equals(resolveNode) && toSet) {
newCurrent.setSource(newNode); 
} else if(newCurrent.getTarget().equals(resolveNode) && toSet) {
newCurrent.setTarget(newNode); 
} else {
throw new RuntimeException("should not be there");
}
}
}

}*/


//DEBUG 
/*%strategy Resolve(tom__linkClass:LinkClass,pn:PetriNet) extends Identity() {
visit Place {
pr@ResolveWorkDefinitionPlace[o=o,name=name] -> {
Place res = (Place) tom__linkClass.get(`o).get(`name);
resolveInverseLinks(`pr, res, pn);
return res;
}
}

visit Transition {
tr@ResolveWorkDefinitionTransition[o=o,name=name] -> {
Transition res = (Transition) tom__linkClass.get(`o).get(`name);
resolveInverseLinks(`tr, res, pn);
return res;
}
ptr@ResolveProcessTransition[o=o,name=name] -> {
Transition res = (Transition) tom__linkClass.get(`o).get(`name);
resolveInverseLinks(`ptr, res, pn);
return res;
}
}
}*/
//DEBUG 

public static void main(String[] args) {
System.out.println("\nStarting…\n");

XMIResourceImpl resource = new XMIResourceImpl();
SimplePDLSemantics.DDMMSimplePDL.Process p_root;
Map opts = new HashMap();
opts.put(XMIResource.OPTION_SCHEMA_LOCATION, java.lang.Boolean.TRUE);

if (args.length>0) {
DDMMSimplePDLPackage packageInstance = DDMMSimplePDLPackage.eINSTANCE;
File input = new File(args[0]);
try {
resource.load(new FileInputStream(input),opts);
} catch (Exception e) {
e.printStackTrace();
}
p_root = (SimplePDLSemantics.DDMMSimplePDL.Process) resource.getContents().get(0);
} else {
System.out.println("No model instance given in argument. Using default hardcoded model.");
WorkDefinition wd1 = 
tom_make_WorkDefinition(null,tom_empty_array_WorkSequenceEList(0),tom_empty_array_WorkSequenceEList(0),"A",null);
WorkDefinition wd2 = 
tom_make_WorkDefinition(null,tom_empty_array_WorkSequenceEList(0),tom_empty_array_WorkSequenceEList(0),"B",null);
WorkDefinition wd3 = 
tom_make_WorkDefinition(null,tom_empty_array_WorkSequenceEList(0),tom_empty_array_WorkSequenceEList(0),"C",null);
WorkDefinition wd4 = 
tom_make_WorkDefinition(null,tom_empty_array_WorkSequenceEList(0),tom_empty_array_WorkSequenceEList(0),"D",null);
WorkSequence ws1 = 
tom_make_WorkSequence(null,tom_make_startToStart(),wd1,wd2);
WorkSequence ws2 = 
tom_make_WorkSequence(null,tom_make_startToFinish(),wd3,wd4);

p_root = 
tom_make_Process("root",tom_cons_array_ProcessElementEList(ws1,tom_cons_array_ProcessElementEList(wd2,tom_cons_array_ProcessElementEList(wd1,tom_empty_array_ProcessElementEList(3)))),null);
SimplePDLSemantics.DDMMSimplePDL.Process p_child = 
tom_make_Process("child",tom_cons_array_ProcessElementEList(ws2,tom_cons_array_ProcessElementEList(wd4,tom_cons_array_ProcessElementEList(wd3,tom_empty_array_ProcessElementEList(3)))),wd2);

wd1.setParent(p_root);
wd2.setParent(p_root);
wd2.setProcess(p_child);

wd3.setParent(p_child);
wd4.setParent(p_child);

ws1.setParent(p_root);
ws2.setParent(p_child);
}
SimplePDLToPetriNoHash translator = new SimplePDLToPetriNoHash();

try {
translator.pn = 
tom_make_PetriNet(tom_empty_array_NodeEList(0),tom_empty_array_ArcEList(0),"main");

/*
Strategy transformer = `Sequence(
TopDown(Process2PetriNet(translator)),
TopDown(WorkDefinition2PetriNet(translator)),
TopDown(WorkSequence2PetriNet(translator))
);
*/
Strategy transformer = 
tom_make_SimplePDLToPetriNet(translator.tom__linkClass,translator.pn);
transformer.visit(p_root, new EcoreContainmentIntrospector());

/*
System.out.println("\nBefore Resolve");
`Sequence(TopDown(PrintTransition()),TopDown(PrintPlace())).visit(translator.pn, new EcoreContainmentIntrospector());
*/

/*
System.out.println("\nTest strategy resolve");
`TopDown(Resolve(translator)).visit(translator.pn, new EcoreContainmentIntrospector());
*/

System.out.println("\nResult");

tom_cons_list_Sequence(tom_make_TopDown(tom_make_PrintTransition()),tom_cons_list_Sequence(tom_make_TopDown(tom_make_PrintPlace()),tom_empty_list_Sequence())).visit(translator.pn, new EcoreContainmentIntrospector());

} catch(VisitFailure e) {
System.out.println("strategy fail");
}
}



public static class PrintArc extends tom.library.sl.AbstractStrategyBasic {
public PrintArc() {
super(tom_make_Identity());
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (tom_is_sort_Arc(v)) {
return ((T)visit_Arc((( petrinetsemantics.DDMMPetriNet.Arc )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Arc  _visit_Arc( petrinetsemantics.DDMMPetriNet.Arc  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( petrinetsemantics.DDMMPetriNet.Arc )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Arc  visit_Arc( petrinetsemantics.DDMMPetriNet.Arc  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_Arc(((Object)tom__arg))) {
if (tom_is_sort_Arc((( petrinetsemantics.DDMMPetriNet.Arc )((Object)tom__arg)))) {
if (tom_is_fun_sym_Arc((( petrinetsemantics.DDMMPetriNet.Arc )(( petrinetsemantics.DDMMPetriNet.Arc )((Object)tom__arg))))) {

System.out.println(
tom_get_slot_Arc_source((( petrinetsemantics.DDMMPetriNet.Arc )((Object)tom__arg))).getName() + " -> " + 
tom_get_slot_Arc_target((( petrinetsemantics.DDMMPetriNet.Arc )((Object)tom__arg))).getName());


}
}
}

}

}
return _visit_Arc(tom__arg,introspector);

}
}
public static class PrintTransition extends tom.library.sl.AbstractStrategyBasic {
public PrintTransition() {
super(tom_make_Identity());
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (tom_is_sort_Transition(v)) {
return ((T)visit_Transition((( petrinetsemantics.DDMMPetriNet.Transition )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Transition  _visit_Transition( petrinetsemantics.DDMMPetriNet.Transition  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( petrinetsemantics.DDMMPetriNet.Transition )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Transition  visit_Transition( petrinetsemantics.DDMMPetriNet.Transition  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_Transition(((Object)tom__arg))) {
if (tom_is_sort_Transition((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)))) {
if (tom_is_fun_sym_ResolveWorkDefinitionTransition((( petrinetsemantics.DDMMPetriNet.Transition )(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg))))) {

System.out.println("tr resolve " + 
tom_get_slot_ResolveWorkDefinitionTransition_name((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg))));
return 
(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg));


}
}
}

}
{
if (tom_is_sort_Transition(((Object)tom__arg))) {
if (tom_is_sort_Transition((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)))) {
if (tom_is_fun_sym_ResolveProcessTransition((( petrinetsemantics.DDMMPetriNet.Transition )(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg))))) {

System.out.println("tr process resolve " + 
tom_get_slot_ResolveProcessTransition_name((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg))));
return 
(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg));


}
}
}

}
{
if (tom_is_sort_Transition(((Object)tom__arg))) {
if (tom_is_sort_Transition((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)))) {
if (tom_is_fun_sym_Transition((( petrinetsemantics.DDMMPetriNet.Transition )(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg))))) {
 org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_sources=tom_get_slot_Transition_incomings((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)));
 org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_targets=tom_get_slot_Transition_outgoings((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)));

String s = "";
String t = "";

{
{
if (tom_is_sort_ArcEList(((Object)tom_sources))) {
if (tom_is_fun_sym_ArcEList((( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )(( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )((Object)tom_sources))))) {
int tomMatch9__end__9=0;
do {
{
if (!(tomMatch9__end__9 >= tom_get_size_ArcEList_ArcEList((( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )((Object)tom_sources))))) {
 petrinetsemantics.DDMMPetriNet.Arc  tomMatch9_15=tom_get_element_ArcEList_ArcEList((( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )((Object)tom_sources)),tomMatch9__end__9);
if (tom_is_sort_Arc(tomMatch9_15)) {
if (tom_is_fun_sym_Arc((( petrinetsemantics.DDMMPetriNet.Arc )tomMatch9_15))) {
 petrinetsemantics.DDMMPetriNet.Node  tom_node=tom_get_slot_Arc_source(tomMatch9_15);
if (tom_is_sort_Place(((Object)tom_node))) {
if (tom_is_sort_Place((( petrinetsemantics.DDMMPetriNet.Node )((Object)tom_node)))) {
if (tom_is_fun_sym_Place((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Node )((Object)tom_node))))) {

s += 
tom_get_slot_Place_name((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Node )((Object)tom_node))))+ ((
tom_get_slot_Arc_kind(tomMatch9_15)==
tom_make_read_arc())?"?":"*") + 
tom_get_slot_Arc_weight(tomMatch9_15)+ " "; 


}
}
}
}
}
}
tomMatch9__end__9=tomMatch9__end__9 + 1;

}
} while(!(tomMatch9__end__9 > tom_get_size_ArcEList_ArcEList((( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )((Object)tom_sources)))));
}
}

}
{
if (tom_is_sort_ArcEList(((Object)tom_targets))) {
if (tom_is_fun_sym_ArcEList((( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )(( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )((Object)tom_targets))))) {
int tomMatch9__end__26=0;
do {
{
if (!(tomMatch9__end__26 >= tom_get_size_ArcEList_ArcEList((( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )((Object)tom_targets))))) {
 petrinetsemantics.DDMMPetriNet.Arc  tomMatch9_32=tom_get_element_ArcEList_ArcEList((( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )((Object)tom_targets)),tomMatch9__end__26);
if (tom_is_sort_Arc(tomMatch9_32)) {
if (tom_is_fun_sym_Arc((( petrinetsemantics.DDMMPetriNet.Arc )tomMatch9_32))) {
 petrinetsemantics.DDMMPetriNet.Node  tom_node=tom_get_slot_Arc_target(tomMatch9_32);
if (tom_is_sort_Place(((Object)tom_node))) {
if (tom_is_sort_Place((( petrinetsemantics.DDMMPetriNet.Node )((Object)tom_node)))) {
if (tom_is_fun_sym_Place((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Node )((Object)tom_node))))) {

t += 
tom_get_slot_Place_name((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Node )((Object)tom_node))))+ ((
tom_get_slot_Arc_kind(tomMatch9_32)==
tom_make_read_arc())?"?":"*") + 
tom_get_slot_Arc_weight(tomMatch9_32)+ " "; 


}
}
}
}
}
}
tomMatch9__end__26=tomMatch9__end__26 + 1;

}
} while(!(tomMatch9__end__26 > tom_get_size_ArcEList_ArcEList((( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc> )((Object)tom_targets)))));
}
}

}


}

System.out.println("tr " + 
tom_get_slot_Transition_name((( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)))+ " " + s + " --> " + t);


}
}
}

}


}
return _visit_Transition(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_PrintTransition() { 
return new PrintTransition();/*toto*/
}
public static class PrintPlace extends tom.library.sl.AbstractStrategyBasic {
public PrintPlace() {
super(tom_make_Identity());
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (tom_is_sort_Place(v)) {
return ((T)visit_Place((( petrinetsemantics.DDMMPetriNet.Place )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Place  _visit_Place( petrinetsemantics.DDMMPetriNet.Place  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( petrinetsemantics.DDMMPetriNet.Place )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  petrinetsemantics.DDMMPetriNet.Place  visit_Place( petrinetsemantics.DDMMPetriNet.Place  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_Place(((Object)tom__arg))) {
if (tom_is_sort_Place((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg)))) {
if (tom_is_fun_sym_ResolveWorkDefinitionPlace((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg))))) {

System.out.println("pl resolve " + 
tom_get_slot_ResolveWorkDefinitionPlace_name((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg))));
return 
(( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg));


}
}
}

}
{
if (tom_is_sort_Place(((Object)tom__arg))) {
if (tom_is_sort_Place((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg)))) {
if (tom_is_fun_sym_Place((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg))))) {
 int  tom_w=tom_get_slot_Place_initialMarking((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg)));
if (!(tom_equal_term_int(0, tom_w))) {

System.out.println("pl " + 
tom_get_slot_Place_name((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg)))+ " " + 
tom_w);


}
}
}
}

}


}
return _visit_Place(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_PrintPlace() { 
return new PrintPlace();/*toto*/
}


}
