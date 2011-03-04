
%include{ DDMMPetriNetPackage.tom }
%include{ DDMMSimplePDLPackage.tom }

/* DEBUG1 */
/*
%typeterm ResolveWorkDefinitionPlace extends Place {
  implement { ResolveWorkDefinitionPlace }
  is_sort(t) { $t instanceof ResolveWorkDefinitionPlace }
}
*/
/* DEBUG1b */
/*
%op Place ResolveWorkDefinitionPlace(o:WorkDefinition, name:String) {
  is_fsym(t) { $t instanceof ResolveWorkDefinitionPlace }
  get_slot(name, t)  { ((ResolveWorkDefinitionPlace)$t).name }
  get_slot(o, t)  { ((ResolveWorkDefinitionPlace)$t).o }
  make(o,name) { new ResolveWorkDefinitionPlace(o,name) }
}
*/
/* DEBUG2 */

/*%include { sl.tom }

%strategy RRR() extends Identity() {
  visit T1 {
    a() -> { System.out.println("rrr"); }
  }
}

%strategy SSS() extends Identity() {
  visit T1 {
    b() -> { System.out.println("sss"); }
  }
}*/

/* DEBUG3 */

//%transformation SimplePDL2Petrinet() {
%transformation Petrinet() with (Process,WorkDefinition,WorkSequence) to (Place,Transition,Arc) {
  p@Process -> { System.out.println("a"); }
  WorkDefinition -> { System.out.println("b"); }
}
