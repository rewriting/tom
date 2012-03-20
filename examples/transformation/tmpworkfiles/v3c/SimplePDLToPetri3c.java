import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

import simplepdl.*;
import petrinetsemantics.DDMMPetriNet.*;

import java.util.*;

import tom.library.sl.*;
import tom.library.emf.*;

public class SimplePDLToPetri3c {


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
return ( new tom.library.sl.Mu(var,v) );
}
private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { 
return ( new tom.library.sl.MuVar(name) );
}
private static  tom.library.sl.Strategy  tom_make_Identity() { 
return ( new tom.library.sl.Identity() );
}
private static  tom.library.sl.Strategy  tom_make_All( tom.library.sl.Strategy  v) { 
return ( new tom.library.sl.All(v) );
}
private static boolean tom_is_fun_sym_Sequence( tom.library.sl.Strategy  t) {
return ( t instanceof tom.library.sl.Sequence );
}
private static  tom.library.sl.Strategy  tom_empty_list_Sequence() { 
return  null ;
}
private static  tom.library.sl.Strategy  tom_cons_list_Sequence( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { 
return  tom.library.sl.Sequence.make(head,tail) ;
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

;
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
return  (petrinetsemantics.DDMMPetriNet.ArcKind)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.createFromString( (EDataType)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getArcKind(), "normal") ;
}
private static  petrinetsemantics.DDMMPetriNet.ArcKind  tom_make_read_arc() { 
return  (petrinetsemantics.DDMMPetriNet.ArcKind)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.createFromString( (EDataType)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getArcKind(), "read_arc") ;
}
private static boolean tom_is_fun_sym_Arc( petrinetsemantics.DDMMPetriNet.Arc  t) {
return  t instanceof petrinetsemantics.DDMMPetriNet.Arc ;
}
private static  petrinetsemantics.DDMMPetriNet.ArcKind  tom_get_default_Arc_kind() {
return tom_make_normal()

;
}
private static  int  tom_get_default_Arc_weight() {
return  0 ;
}
private static  petrinetsemantics.DDMMPetriNet.Arc  tom_make_Arc( petrinetsemantics.DDMMPetriNet.Node  target,  petrinetsemantics.DDMMPetriNet.Node  source,  petrinetsemantics.DDMMPetriNet.PetriNet  net,  petrinetsemantics.DDMMPetriNet.ArcKind  kind,  int  weight) { 
return  constructArc((petrinetsemantics.DDMMPetriNet.Arc)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.create((EClass)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getEClassifier("Arc")), new Object[]{ target, source, net, kind, weight }) ;
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
return  new org.eclipse.emf.common.util.BasicEList<petrinetsemantics.DDMMPetriNet.Arc>(n) ;
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  tom_cons_array_ArcEList( petrinetsemantics.DDMMPetriNet.Arc  e,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  l) { 
return  appendArcEList(e,l) ;
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
return  new org.eclipse.emf.common.util.BasicEList<petrinetsemantics.DDMMPetriNet.Node>(n) ;
}
private static  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>  tom_cons_array_NodeEList( petrinetsemantics.DDMMPetriNet.Node  e,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>  l) { 
return  appendNodeEList(e,l) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendNodeEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static  petrinetsemantics.DDMMPetriNet.PetriNet  tom_make_PetriNet( org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Node>  nodes,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  arcs,  String  name) { 
return  constructPetriNet((petrinetsemantics.DDMMPetriNet.PetriNet)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.create((EClass)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getEClassifier("PetriNet")), new Object[]{ nodes, arcs, name }) ;
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
private static  int  tom_get_default_Transition_min_time() {
return  0 ;
}
private static  int  tom_get_default_Transition_max_time() {
return  0 ;
}
private static  petrinetsemantics.DDMMPetriNet.Transition  tom_make_Transition( String  name,  petrinetsemantics.DDMMPetriNet.PetriNet  net,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  outgoings,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  incomings,  int  min_time,  int  max_time) { 
return  constructTransition((petrinetsemantics.DDMMPetriNet.Transition)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.create((EClass)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getEClassifier("Transition")), new Object[]{ name, net, outgoings, incomings, min_time, max_time }) ;
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
private static  int  tom_get_default_Place_initialMarking() {
return  0 ;
}
private static  petrinetsemantics.DDMMPetriNet.Place  tom_make_Place( String  name,  petrinetsemantics.DDMMPetriNet.PetriNet  net,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  outgoings,  org.eclipse.emf.common.util.EList<petrinetsemantics.DDMMPetriNet.Arc>  incomings,  int  initialMarking) { 
return  constructPlace((petrinetsemantics.DDMMPetriNet.Place)petrinetsemantics.DDMMPetriNet.DDMMPetriNetFactory.eINSTANCE.create((EClass)petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.eINSTANCE.getEClassifier("Place")), new Object[]{ name, net, outgoings, incomings, initialMarking }) ;
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

//%include{ SimplePDL3c.tom }

private static boolean tom_equal_term_Process(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_Process(Object t) {
return  t instanceof simplepdl.Process ;
}
private static boolean tom_equal_term_ProcessElement(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ProcessElement(Object t) {
return  t instanceof simplepdl.ProcessElement ;
}
private static boolean tom_equal_term_ProcessElementEList(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_ProcessElementEList(Object t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>)t).size()>0 && ((org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>)t).get(0) instanceof simplepdl.ProcessElement)) ;
}
private static boolean tom_is_fun_sym_ProcessElementEList( org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  t) {
return  t instanceof org.eclipse.emf.common.util.EList<?> && (t.size() == 0 || (t.size()>0 && t.get(0) instanceof simplepdl.ProcessElement)) ;
}
private static  org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  tom_empty_array_ProcessElementEList(int n) { 
return  new org.eclipse.emf.common.util.BasicEList<simplepdl.ProcessElement>(n) ;
}
private static  org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  tom_cons_array_ProcessElementEList( simplepdl.ProcessElement  e,  org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  l) { 
return  appendProcessElementEList(e,l) ;
}
private static  simplepdl.ProcessElement  tom_get_element_ProcessElementEList_ProcessElementEList( org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  l, int n) {
return  l.get(n) ;
}
private static int tom_get_size_ProcessElementEList_ProcessElementEList( org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  l) {
return  l.size() ;
}

  private static   org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  tom_get_slice_ProcessElementEList( org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  subject, int begin, int end) {
     org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  result =  new org.eclipse.emf.common.util.BasicEList<simplepdl.ProcessElement>(end-begin) ;
    while(begin!=end) {
      result =  appendProcessElementEList( subject.get(begin) ,result) ;
      begin++;
    }
    return result;
  }

  private static   org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  tom_append_array_ProcessElementEList( org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  l2,  org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  l1) {
    int size1 =  l1.size() ;
    int size2 =  l2.size() ;
    int index;
     org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  result =  new org.eclipse.emf.common.util.BasicEList<simplepdl.ProcessElement>(size1+size2) ;
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
return  t instanceof simplepdl.WorkDefinition ;
}
private static boolean tom_equal_term_WorkSequence(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_WorkSequence(Object t) {
return  t instanceof simplepdl.WorkSequence ;
}
private static boolean tom_equal_term_WorkSequenceType(Object l1, Object l2) {
return  l1.equals(l2) ;
}
private static boolean tom_is_sort_WorkSequenceType(Object t) {
return  t instanceof simplepdl.WorkSequenceType ;
}
private static boolean tom_is_fun_sym_startToStart( simplepdl.WorkSequenceType  t) {
return  t == simplepdl.WorkSequenceType.get("startToStart") ;
}
private static  simplepdl.WorkSequenceType  tom_make_startToStart() { 
return  (simplepdl.WorkSequenceType)simplepdl.SimplepdlFactory.eINSTANCE.createFromString( (EDataType)simplepdl.SimplepdlPackage.eINSTANCE.getWorkSequenceType(), "startToStart") ;
}
private static boolean tom_is_fun_sym_finishToStart( simplepdl.WorkSequenceType  t) {
return  t == simplepdl.WorkSequenceType.get("finishToStart") ;
}
private static boolean tom_is_fun_sym_startToFinish( simplepdl.WorkSequenceType  t) {
return  t == simplepdl.WorkSequenceType.get("startToFinish") ;
}
private static  simplepdl.WorkSequenceType  tom_make_startToFinish() { 
return  (simplepdl.WorkSequenceType)simplepdl.SimplepdlFactory.eINSTANCE.createFromString( (EDataType)simplepdl.SimplepdlPackage.eINSTANCE.getWorkSequenceType(), "startToFinish") ;
}
private static boolean tom_is_fun_sym_finishToFinish( simplepdl.WorkSequenceType  t) {
return  t == simplepdl.WorkSequenceType.get("finishToFinish") ;
}
private static boolean tom_is_fun_sym_WorkSequence( simplepdl.WorkSequence  t) {
return  t instanceof simplepdl.WorkSequence ;
}
private static  simplepdl.WorkSequenceType  tom_get_default_WorkSequence_linkType() {
return tom_make_startToStart()

;
}
private static  simplepdl.WorkSequence  tom_make_WorkSequence( simplepdl.Process  parent,  simplepdl.WorkSequenceType  linkType,  simplepdl.WorkDefinition  predecessor,  simplepdl.WorkDefinition  successor) { 
return  constructWorkSequence((simplepdl.WorkSequence)simplepdl.SimplepdlFactory.eINSTANCE.create((EClass)simplepdl.SimplepdlPackage.eINSTANCE.getEClassifier("WorkSequence")), new Object[]{ parent, linkType, predecessor, successor }) ;
}
private static  simplepdl.Process  tom_get_slot_WorkSequence_parent( simplepdl.WorkSequence  t) {
return  (simplepdl.Process)t.eGet(t.eClass().getEStructuralFeature("parent")) ;
}
private static  simplepdl.WorkSequenceType  tom_get_slot_WorkSequence_linkType( simplepdl.WorkSequence  t) {
return  (simplepdl.WorkSequenceType)t.eGet(t.eClass().getEStructuralFeature("linkType")) ;
}
private static  simplepdl.WorkDefinition  tom_get_slot_WorkSequence_predecessor( simplepdl.WorkSequence  t) {
return  (simplepdl.WorkDefinition)t.eGet(t.eClass().getEStructuralFeature("predecessor")) ;
}
private static  simplepdl.WorkDefinition  tom_get_slot_WorkSequence_successor( simplepdl.WorkSequence  t) {
return  (simplepdl.WorkDefinition)t.eGet(t.eClass().getEStructuralFeature("successor")) ;
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
return  t instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>)t).size() == 0 || (((org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>)t).size()>0 && ((org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>)t).get(0) instanceof simplepdl.WorkSequence)) ;
}
private static  org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>  tom_empty_array_WorkSequenceEList(int n) { 
return  new org.eclipse.emf.common.util.BasicEList<simplepdl.WorkSequence>(n) ;
}
private static  org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>  tom_cons_array_WorkSequenceEList( simplepdl.WorkSequence  e,  org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>  l) { 
return  appendWorkSequenceEList(e,l) ;
}


private static <O> org.eclipse.emf.common.util.EList<O> appendWorkSequenceEList(O e,org.eclipse.emf.common.util.EList<O> l) {
l.add(e);
return l;
}
private static boolean tom_is_fun_sym_WorkDefinition( simplepdl.WorkDefinition  t) {
return  t instanceof simplepdl.WorkDefinition ;
}
private static  simplepdl.WorkDefinition  tom_make_WorkDefinition( simplepdl.Process  parent,  org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>  linksToPredecessors,  org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>  linksToSuccessors,  simplepdl.Process  process,  String  name) { 
return  constructWorkDefinition((simplepdl.WorkDefinition)simplepdl.SimplepdlFactory.eINSTANCE.create((EClass)simplepdl.SimplepdlPackage.eINSTANCE.getEClassifier("WorkDefinition")), new Object[]{ parent, linksToPredecessors, linksToSuccessors, process, name }) ;
}
private static  simplepdl.Process  tom_get_slot_WorkDefinition_parent( simplepdl.WorkDefinition  t) {
return  (simplepdl.Process)t.eGet(t.eClass().getEStructuralFeature("parent")) ;
}
private static  org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>  tom_get_slot_WorkDefinition_linksToPredecessors( simplepdl.WorkDefinition  t) {
return  (org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>)t.eGet(t.eClass().getEStructuralFeature("linksToPredecessors")) ;
}
private static  org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>  tom_get_slot_WorkDefinition_linksToSuccessors( simplepdl.WorkDefinition  t) {
return  (org.eclipse.emf.common.util.EList<simplepdl.WorkSequence>)t.eGet(t.eClass().getEStructuralFeature("linksToSuccessors")) ;
}
private static  simplepdl.Process  tom_get_slot_WorkDefinition_process( simplepdl.WorkDefinition  t) {
return  (simplepdl.Process)t.eGet(t.eClass().getEStructuralFeature("process")) ;
}
private static  String  tom_get_slot_WorkDefinition_name( simplepdl.WorkDefinition  t) {
return  (java.lang.String)t.eGet(t.eClass().getEStructuralFeature("name")) ;
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
private static boolean tom_is_fun_sym_Process( simplepdl.Process  t) {
return  t instanceof simplepdl.Process ;
}
private static  simplepdl.Process  tom_make_Process( String  name,  org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  processElements,  simplepdl.WorkDefinition  from) { 
return  constructProcess((simplepdl.Process)simplepdl.SimplepdlFactory.eINSTANCE.create((EClass)simplepdl.SimplepdlPackage.eINSTANCE.getEClassifier("Process")), new Object[]{ name, processElements, from }) ;
}
private static  String  tom_get_slot_Process_name( simplepdl.Process  t) {
return  (java.lang.String)t.eGet(t.eClass().getEStructuralFeature("name")) ;
}
private static  org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>  tom_get_slot_Process_processElements( simplepdl.Process  t) {
return  (org.eclipse.emf.common.util.EList<simplepdl.ProcessElement>)t.eGet(t.eClass().getEStructuralFeature("processElements")) ;
}
private static  simplepdl.WorkDefinition  tom_get_slot_Process_from( simplepdl.Process  t) {
return  (simplepdl.WorkDefinition)t.eGet(t.eClass().getEStructuralFeature("from")) ;
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
return  t instanceof simplepdl.Guidance ;
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



private Hashtable<Object,HashMap<String,Object>> table;
private PetriNet pn = null;

public SimplePDLToPetri3c() {
this.table = new Hashtable<Object,HashMap<String,Object>>();
}


private static class ResolveProcessPlace extends  petrinetsemantics.DDMMPetriNet.impl.PlaceImpl  {
  public String name;
  public Process o;

  public ResolveProcessPlace(Process o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionPlace( petrinetsemantics.DDMMPetriNet.Place  t) {
return  false ;
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionPlace( petrinetsemantics.DDMMPetriNet.Place  t) {
return  t instanceof ResolveWorkDefinitionPlace ;
}
private static  simplepdl.WorkDefinition  tom_get_slot_ResolveWorkDefinitionPlace_o( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ((ResolveWorkDefinitionPlace)t).o ;
}
private static  String  tom_get_slot_ResolveWorkDefinitionPlace_name( petrinetsemantics.DDMMPetriNet.Place  t) {
return  ((ResolveWorkDefinitionPlace)t).name ;
}
private static class ResolveWorkDefinitionPlace extends  petrinetsemantics.DDMMPetriNet.impl.PlaceImpl  {
  public String name;
  public WorkDefinition o;

  public ResolveWorkDefinitionPlace(WorkDefinition o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionPlace( petrinetsemantics.DDMMPetriNet.Place  t) {
return  t instanceof ResolveWorkDefinitionPlace ;
}
private static class ResolveWorkSequencePlace extends  petrinetsemantics.DDMMPetriNet.impl.PlaceImpl  {
  public String name;
  public WorkSequence o;

  public ResolveWorkSequencePlace(WorkSequence o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static boolean tom_is_fun_sym_ResolveProcessTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  false ;
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionPlace( petrinetsemantics.DDMMPetriNet.Place  t) {
return  t instanceof ResolveWorkDefinitionPlace ;
}
private static boolean tom_is_fun_sym_ResolveProcessTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  t instanceof ResolveProcessTransition ;
}
private static  simplepdl.Process  tom_get_slot_ResolveProcessTransition_o( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveProcessTransition)t).o ;
}
private static  String  tom_get_slot_ResolveProcessTransition_name( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveProcessTransition)t).name ;
}
private static class ResolveProcessTransition extends  petrinetsemantics.DDMMPetriNet.impl.TransitionImpl  {
  public String name;
  public Process o;

  public ResolveProcessTransition(Process o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  false ;
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionPlace( petrinetsemantics.DDMMPetriNet.Place  t) {
return  t instanceof ResolveWorkDefinitionPlace ;
}
private static boolean tom_is_fun_sym_ResolveProcessTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  t instanceof ResolveProcessTransition ;
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  t instanceof ResolveWorkDefinitionTransition ;
}
private static  simplepdl.WorkDefinition  tom_get_slot_ResolveWorkDefinitionTransition_o( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveWorkDefinitionTransition)t).o ;
}
private static  String  tom_get_slot_ResolveWorkDefinitionTransition_name( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  ((ResolveWorkDefinitionTransition)t).name ;
}
private static class ResolveWorkDefinitionTransition extends  petrinetsemantics.DDMMPetriNet.impl.TransitionImpl  {
  public String name;
  public WorkDefinition o;

  public ResolveWorkDefinitionTransition(WorkDefinition o, String name) {
    this.name = name;
    this.o = o;
  }
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionPlace( petrinetsemantics.DDMMPetriNet.Place  t) {
return  t instanceof ResolveWorkDefinitionPlace ;
}
private static boolean tom_is_fun_sym_ResolveProcessTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  t instanceof ResolveProcessTransition ;
}
private static boolean tom_is_fun_sym_ResolveWorkDefinitionTransition( petrinetsemantics.DDMMPetriNet.Transition  t) {
return  t instanceof ResolveWorkDefinitionTransition ;
}
private static class ResolveWorkSequenceTransition extends  petrinetsemantics.DDMMPetriNet.impl.TransitionImpl  {
  public String name;
  public WorkSequence o;

  public ResolveWorkSequenceTransition(WorkSequence o, String name) {
    this.name = name;
    this.o = o;
  }
}
public static class tom__StratResolve_SimplePDLToPetriNet extends tom.library.sl.AbstractStrategyBasic {
private  SimplePDLToPetri3c  translator;
public tom__StratResolve_SimplePDLToPetriNet( SimplePDLToPetri3c  translator) {
super(tom_make_Identity());
this.translator=translator;
}
public  SimplePDLToPetri3c  gettranslator() {
return translator;
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
Transition res = null;//(Transition) translator.table.get(o).get(name);
//resolveInverseLinks(tom__arg, res, translator);
return res;}

}
{
if (tom_is_sort_Transition(((Object)tom__arg))) {
Transition res = null;//(Transition) translator.table.get(o).get(name);
//resolveInverseLinks(tom__arg, res, translator);
return res;}

}
{
if (tom_is_sort_Transition(((Object)tom__arg))) {
Transition res = null;//(Transition) translator.table.get(o).get(name);
//resolveInverseLinks(tom__arg, res, translator);
return res;}

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
Place res = null;//(Place) translator.table.get(o).get(name);
//resolveInverseLinks(tom__arg, res, translator);
return res;}

}
{
if (tom_is_sort_Place(((Object)tom__arg))) {
Place res = null;//(Place) translator.table.get(o).get(name);
//resolveInverseLinks(tom__arg, res, translator);
return res;}

}
{
if (tom_is_sort_Place(((Object)tom__arg))) {
Place res = null;//(Place) translator.table.get(o).get(name);
//resolveInverseLinks(tom__arg, res, translator);
return res;}

}


}
return _visit_Place(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_tom__StratResolve_SimplePDLToPetriNet( SimplePDLToPetri3c  t0) { 
return new tom__StratResolve_SimplePDLToPetriNet(t0);
}
public static class Process2PetriNet extends tom.library.sl.AbstractStrategyBasic {
private  SimplePDLToPetri3c  translator;
public Process2PetriNet( SimplePDLToPetri3c  translator) {
super(tom_make_Identity());
this.translator=translator;
}
public  SimplePDLToPetri3c  gettranslator() {
return translator;
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
return ((T)visit_Process((( simplepdl.Process )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  simplepdl.Process  _visit_Process( simplepdl.Process  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( simplepdl.Process )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  simplepdl.Process  visit_Process( simplepdl.Process  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_Process(((Object)tom__arg))) {
if (tom_is_sort_Process((( simplepdl.Process )((Object)tom__arg)))) {
if (tom_is_fun_sym_Process((( simplepdl.Process )(( simplepdl.Process )((Object)tom__arg))))) {
 String  tom_name=tom_get_slot_Process_name((( simplepdl.Process )((Object)tom__arg)));
 simplepdl.Process  tom_p=(( simplepdl.Process )((Object)tom__arg));


Node p_ready  = 
tom_make_Place(tom_name+ "_ready",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1);
Node p_running  = 
tom_make_Place(tom_name+ "_running",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
Node p_finished  = 
tom_make_Place(tom_name+ "_finished",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
Node t_start  = 
tom_make_Transition(tom_name+ "_start",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1,1);
Node t_finish  = 
tom_make_Transition(tom_name+ "_finish",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1,1);


tom_make_Arc(t_start,p_ready,translator.pn,tom_make_normal(),1);

tom_make_Arc(p_running,t_start,translator.pn,tom_make_normal(),1);

tom_make_Arc(t_finish,p_running,translator.pn,tom_make_normal(),1);

tom_make_Arc(p_finished,t_finish,translator.pn,tom_make_normal(),1);

HashMap<String,Object> map = new HashMap<String,Object>();
map.put("p_ready",p_ready);
map.put("p_running",p_running);
map.put("p_finished",p_finished);
map.put("t_start",t_start);
map.put("t_finish",t_finish);

translator.table.put(
tom_p,map);

WorkDefinition from = 
tom_p.getFrom();
if (from!=null) {
Transition source = new ResolveWorkDefinitionTransition(from, "t_start");
source.setNet(translator.pn);
Arc tmpZoomIn = 
tom_make_Arc(p_ready,source,translator.pn,tom_make_normal(),1);

Transition target = new ResolveWorkDefinitionTransition(from, "t_finish");
target.setNet(translator.pn);
Arc tmpZoomOut = 
tom_make_Arc(target,p_finished,translator.pn,tom_make_read_arc(),1);
}


}
}
}

}

}
return _visit_Process(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_Process2PetriNet( SimplePDLToPetri3c  t0) { 
return new Process2PetriNet(t0);
}
public static class WorkDefinition2PetriNet extends tom.library.sl.AbstractStrategyBasic {
private  SimplePDLToPetri3c  translator;
public WorkDefinition2PetriNet( SimplePDLToPetri3c  translator) {
super(tom_make_Identity());
this.translator=translator;
}
public  SimplePDLToPetri3c  gettranslator() {
return translator;
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
return ((T)visit_WorkDefinition((( simplepdl.WorkDefinition )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  simplepdl.WorkDefinition  _visit_WorkDefinition( simplepdl.WorkDefinition  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( simplepdl.WorkDefinition )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  simplepdl.WorkDefinition  visit_WorkDefinition( simplepdl.WorkDefinition  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_WorkDefinition(((Object)tom__arg))) {
if (tom_is_sort_WorkDefinition((( simplepdl.WorkDefinition )((Object)tom__arg)))) {
if (tom_is_fun_sym_WorkDefinition((( simplepdl.WorkDefinition )(( simplepdl.WorkDefinition )((Object)tom__arg))))) {
 String  tom_name=tom_get_slot_WorkDefinition_name((( simplepdl.WorkDefinition )((Object)tom__arg)));
 simplepdl.WorkDefinition  tom_wd=(( simplepdl.WorkDefinition )((Object)tom__arg));


Node p_ready  = 
tom_make_Place(tom_name+ "_ready",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1);
Node p_started  = 
tom_make_Place(tom_name+ "_started",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
Node p_running  = 
tom_make_Place(tom_name+ "_running",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
Node p_finished  = 
tom_make_Place(tom_name+ "_finished",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),0);
Node t_start  = 
tom_make_Transition(tom_name+ "_start",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1,1);
Node t_finish  = 
tom_make_Transition(tom_name+ "_finish",translator.pn,tom_empty_array_ArcEList(0),tom_empty_array_ArcEList(0),1,1);



tom_make_Arc(t_start,p_ready,translator.pn,tom_make_normal(),1);

tom_make_Arc(p_started,t_start,translator.pn,tom_make_normal(),1);

tom_make_Arc(p_running,t_start,translator.pn,tom_make_normal(),1);

tom_make_Arc(t_finish,p_running,translator.pn,tom_make_normal(),1);

tom_make_Arc(p_finished,t_finish,translator.pn,tom_make_normal(),1);

HashMap<String,Object> map = new HashMap<String,Object>();
map.put("p_ready",p_ready);
map.put("p_started",p_started);
map.put("p_running",p_running);
map.put("p_finished",p_finished);
map.put("t_start",t_start);
map.put("t_finish",t_finish);

translator.table.put(
tom_wd,map);

simplepdl.Process parent = 
tom_wd.getParent();
Transition source = new ResolveProcessTransition(parent , "t_start");
source.setNet(translator.pn);
Arc tmpDistribute = 
tom_make_Arc(p_ready,source,translator.pn,tom_make_normal(),1);

Transition target = new ResolveProcessTransition(parent, "t_finish");
target.setNet(translator.pn);
Arc tmpRejoin = 
tom_make_Arc(target,p_finished,translator.pn,tom_make_read_arc(),1);


}
}
}

}

}
return _visit_WorkDefinition(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_WorkDefinition2PetriNet( SimplePDLToPetri3c  t0) { 
return new WorkDefinition2PetriNet(t0);
}
public static class WorkSequence2PetriNet extends tom.library.sl.AbstractStrategyBasic {
private  SimplePDLToPetri3c  translator;
public WorkSequence2PetriNet( SimplePDLToPetri3c  translator) {
super(tom_make_Identity());
this.translator=translator;
}
public  SimplePDLToPetri3c  gettranslator() {
return translator;
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
return ((T)visit_WorkSequence((( simplepdl.WorkSequence )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  simplepdl.WorkSequence  _visit_WorkSequence( simplepdl.WorkSequence  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( simplepdl.WorkSequence )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  simplepdl.WorkSequence  visit_WorkSequence( simplepdl.WorkSequence  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if (tom_is_sort_WorkSequence(((Object)tom__arg))) {
if (tom_is_sort_WorkSequence((( simplepdl.WorkSequence )((Object)tom__arg)))) {
if (tom_is_fun_sym_WorkSequence((( simplepdl.WorkSequence )(( simplepdl.WorkSequence )((Object)tom__arg))))) {
 simplepdl.WorkDefinition  tom_p=tom_get_slot_WorkSequence_predecessor((( simplepdl.WorkSequence )((Object)tom__arg)));
 simplepdl.WorkDefinition  tom_s=tom_get_slot_WorkSequence_successor((( simplepdl.WorkSequence )((Object)tom__arg)));
 simplepdl.WorkSequenceType  tom_linkType=tom_get_slot_WorkSequence_linkType((( simplepdl.WorkSequence )((Object)tom__arg)));


Place source= null;
Transition target= null;

{
{
if (tom_is_sort_WorkSequenceType(((Object)tom_linkType))) {
boolean tomMatch6_4= false ;
 simplepdl.WorkSequenceType  tomMatch6_3= null ;
 simplepdl.WorkSequenceType  tomMatch6_2= null ;
if (tom_is_sort_WorkSequenceType((( simplepdl.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_finishToFinish((( simplepdl.WorkSequenceType )(( simplepdl.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch6_4= true ;
tomMatch6_2=(( simplepdl.WorkSequenceType )((Object)tom_linkType));

}
} else {
if (tom_is_sort_WorkSequenceType((( simplepdl.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_finishToStart((( simplepdl.WorkSequenceType )(( simplepdl.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch6_4= true ;
tomMatch6_3=(( simplepdl.WorkSequenceType )((Object)tom_linkType));

}
}
}
}
}
if (tomMatch6_4) {
source = new ResolveWorkDefinitionPlace(
tom_p,"p_finished"); 

}

}

}
{
if (tom_is_sort_WorkSequenceType(((Object)tom_linkType))) {
boolean tomMatch6_9= false ;
 simplepdl.WorkSequenceType  tomMatch6_8= null ;
 simplepdl.WorkSequenceType  tomMatch6_7= null ;
if (tom_is_sort_WorkSequenceType((( simplepdl.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_startToStart((( simplepdl.WorkSequenceType )(( simplepdl.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch6_9= true ;
tomMatch6_7=(( simplepdl.WorkSequenceType )((Object)tom_linkType));

}
} else {
if (tom_is_sort_WorkSequenceType((( simplepdl.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_startToFinish((( simplepdl.WorkSequenceType )(( simplepdl.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch6_9= true ;
tomMatch6_8=(( simplepdl.WorkSequenceType )((Object)tom_linkType));

}
}
}
}
}
if (tomMatch6_9) {
source = new ResolveWorkDefinitionPlace(
tom_p,"p_started"); 

}

}

}
{
if (tom_is_sort_WorkSequenceType(((Object)tom_linkType))) {
boolean tomMatch6_14= false ;
 simplepdl.WorkSequenceType  tomMatch6_13= null ;
 simplepdl.WorkSequenceType  tomMatch6_12= null ;
if (tom_is_sort_WorkSequenceType((( simplepdl.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_finishToStart((( simplepdl.WorkSequenceType )(( simplepdl.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch6_14= true ;
tomMatch6_12=(( simplepdl.WorkSequenceType )((Object)tom_linkType));

}
} else {
if (tom_is_sort_WorkSequenceType((( simplepdl.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_startToStart((( simplepdl.WorkSequenceType )(( simplepdl.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch6_14= true ;
tomMatch6_13=(( simplepdl.WorkSequenceType )((Object)tom_linkType));

}
}
}
}
}
if (tomMatch6_14) {
target = new ResolveWorkDefinitionTransition(
tom_s,"t_start"); 

}

}

}
{
if (tom_is_sort_WorkSequenceType(((Object)tom_linkType))) {
boolean tomMatch6_19= false ;
 simplepdl.WorkSequenceType  tomMatch6_18= null ;
 simplepdl.WorkSequenceType  tomMatch6_17= null ;
if (tom_is_sort_WorkSequenceType((( simplepdl.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_startToFinish((( simplepdl.WorkSequenceType )(( simplepdl.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch6_19= true ;
tomMatch6_17=(( simplepdl.WorkSequenceType )((Object)tom_linkType));

}
} else {
if (tom_is_sort_WorkSequenceType((( simplepdl.WorkSequenceType )((Object)tom_linkType)))) {
if (tom_is_fun_sym_finishToFinish((( simplepdl.WorkSequenceType )(( simplepdl.WorkSequenceType )((Object)tom_linkType))))) {
{
tomMatch6_19= true ;
tomMatch6_18=(( simplepdl.WorkSequenceType )((Object)tom_linkType));

}
}
}
}
}
if (tomMatch6_19) {
target = new ResolveWorkDefinitionTransition(
tom_s,"t_finish"); 

}

}

}


}

source.setNet(translator.pn);
target.setNet(translator.pn);
Arc tmp = 
tom_make_Arc(target,source,translator.pn,tom_make_read_arc(),1);  



}
}
}

}

}
return _visit_WorkSequence(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_WorkSequence2PetriNet( SimplePDLToPetri3c  t0) { 
return new WorkSequence2PetriNet(t0);
}
private static  tom.library.sl.Strategy  tom_make_SimplePDLToPetriNet( SimplePDLToPetri3c  t0) { 
return tom_cons_list_Sequence(tom_cons_list_Sequence(tom_make_TopDown(tom_make_Process2PetriNet(t0)),tom_cons_list_Sequence(tom_make_TopDown(tom_make_WorkDefinition2PetriNet(t0)),tom_cons_list_Sequence(tom_make_TopDown(tom_make_WorkSequence2PetriNet(t0)),tom_empty_list_Sequence()))),tom_cons_list_Sequence(tom_make_TopDown(tom_make_tom__StratResolve_SimplePDLToPetriNet(t0)),tom_empty_list_Sequence()));
}


/**
*
* @param resolveNode temporary ResolveNode that should be replaced
* @param newNode node (stored in the HashMap) that will replace the ResolveNode
* @param translator the TimplePDLToPetri3
*/
public static void resolveInverseLinks(EObject resolveNode, Node newNode, SimplePDLToPetri3c translator) {
/* collect arcs having ResolveWorkDefinitionPlace and ResolveWorkDefinitionTransition */

ECrossReferenceAdapter adapter = new ECrossReferenceAdapter(); //create an adapter
translator.pn.eAdapters().add(adapter); //attach it to PetriNet

/*
* 'references' will contains a set of objects (i.e.
* EStructuralFeature.Setting) from which we can retrieve 
* (thanks to getEObject()) objects that
* contains references (i.e. pointers) to resolveNode
*/
Collection<EStructuralFeature.Setting> references = adapter.getInverseReferences(resolveNode);

// for each type of Resolve
boolean toSet = (false
| resolveNode instanceof ResolveWorkDefinitionPlace 
| resolveNode instanceof ResolveWorkDefinitionTransition
| resolveNode instanceof ResolveProcessTransition
);

for (EStructuralFeature.Setting setting:references) {
// current is an object that references resolveNode
EObject current = setting.getEObject();
if (current instanceof Arc) {
Arc newCurrent = (Arc)current;
// for each field of Arc
if(newCurrent.getSource().equals(resolveNode) && toSet) {
newCurrent.setSource(newNode); 
} else if(newCurrent.getTarget().equals(resolveNode) && toSet) {
newCurrent.setTarget(newNode); 
} else {
throw new RuntimeException("should not be there");
}
}
}

}


/*
* Strategy that replaces all Resolve nodes by a normal node
*/
/*%strategy Resolve(translator:SimplePDLToPetri3c) extends Identity() {
visit Place {
pr@ResolveWorkDefinitionPlace[o=o,name=name] -> {
Place res = (Place) translator.table.get(`o).get(`name);
resolveInverseLinks(`pr, res, translator);
return res;
}
}

visit Transition {
tr@ResolveWorkDefinitionTransition[o=o,name=name] -> {
Transition res = (Transition) translator.table.get(`o).get(`name);
resolveInverseLinks(`tr, res, translator);
return res;
}
ptr@ResolveProcessTransition[o=o,name=name] -> {
Transition res = (Transition) translator.table.get(`o).get(`name);
resolveInverseLinks(`ptr, res, translator);
return res;
}

}

}*/

public static void main(String[] args) {
System.out.println("bonne compilation (v3c)\n .......");

/* parent is null since processes have not been yet created */
WorkDefinition wd1 = 
tom_make_WorkDefinition(null,tom_empty_array_WorkSequenceEList(0),tom_empty_array_WorkSequenceEList(0),null,"A");
WorkDefinition wd2 = 
tom_make_WorkDefinition(null,tom_empty_array_WorkSequenceEList(0),tom_empty_array_WorkSequenceEList(0),null,"B");
WorkDefinition wd3 = 
tom_make_WorkDefinition(null,tom_empty_array_WorkSequenceEList(0),tom_empty_array_WorkSequenceEList(0),null,"C");
WorkDefinition wd4 = 
tom_make_WorkDefinition(null,tom_empty_array_WorkSequenceEList(0),tom_empty_array_WorkSequenceEList(0),null,"D");
WorkSequence ws1 = 
tom_make_WorkSequence(null,tom_make_startToStart(),wd1,wd2);
WorkSequence ws2 = 
tom_make_WorkSequence(null,tom_make_startToFinish(),wd3,wd4);

simplepdl.Process p_root = 
tom_make_Process("root",tom_cons_array_ProcessElementEList(ws1,tom_cons_array_ProcessElementEList(wd2,tom_cons_array_ProcessElementEList(wd1,tom_empty_array_ProcessElementEList(3)))),null);
simplepdl.Process p_child = 
tom_make_Process("child",tom_cons_array_ProcessElementEList(ws2,tom_cons_array_ProcessElementEList(wd4,tom_cons_array_ProcessElementEList(wd3,tom_empty_array_ProcessElementEList(3)))),wd2);

wd1.setParent(p_root);
wd2.setParent(p_root);
wd2.setProcess(p_child);

wd3.setParent(p_child);
wd4.setParent(p_child);

ws1.setParent(p_root);
ws2.setParent(p_child);

SimplePDLToPetri3c translator = new SimplePDLToPetri3c();

try {
translator.pn = 
tom_make_PetriNet(tom_empty_array_NodeEList(0),tom_empty_array_ArcEList(0),"main");
/*Strategy transformer = `Sequence(
TopDown(Process2PetriNet(translator)),
TopDown(WorkDefinition2PetriNet(translator)),
TopDown(WorkSequence2PetriNet(translator))
);*/
Strategy transformer = 
tom_make_SimplePDLToPetriNet(translator);
transformer.visit(p_root, new EcoreContainmentIntrospector());

/*System.out.println("\nBefore Resolve");
`Sequence(TopDown(PrintTransition()),TopDown(PrintPlace())).visit(translator.pn, new EcoreContainmentIntrospector());*/

/*System.out.println("\nTest strategy resolve");
`TopDown(Resolve(translator)).visit(translator.pn, new EcoreContainmentIntrospector());*/

/*`TopDown(PrintArc()).visit(pn, new EcoreContainmentIntrospector());*/
/*`TopDown(PrintTransition()).visit(pn, new EcoreContainmentIntrospector());*/

System.out.println("\nAfter Resolve");

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
(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)); /* to avoid printing a Transition twice*/


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
(( petrinetsemantics.DDMMPetriNet.Transition )((Object)tom__arg)); /* to avoid printing a Transition twice*/


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
if (tom_is_sort_Place((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom_node)))) {
if (tom_is_fun_sym_Place((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Place )((Object)tom_node))))) {

s += 
tom_get_slot_Place_name((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom_node)))+ ((
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
if (tom_is_sort_Place((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom_node)))) {
if (tom_is_fun_sym_Place((( petrinetsemantics.DDMMPetriNet.Place )(( petrinetsemantics.DDMMPetriNet.Place )((Object)tom_node))))) {

t += 
tom_get_slot_Place_name((( petrinetsemantics.DDMMPetriNet.Place )((Object)tom_node)))+ ((
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
return new PrintTransition();
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
(( petrinetsemantics.DDMMPetriNet.Place )((Object)tom__arg)); /* to avoid printing a Place twice */


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
return new PrintPlace();
}


}
