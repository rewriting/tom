String java="java";

/* DEBUG1 */

%include{ DDMMPetriNetPackage.tom }

/* DEBUG2 */

%include{ DDMMSimplePDLPackage.tom }

/* DEBUG3 */

%include{ sl.tom }

/* DEBUG4a */

%typeterm Transfo2 { implement { Transfo2 }}

/* DEBUG4b */

%transformation SimplePDLToPetriNet(translator:Transfo2) with (Process, WorkDefinition, WorkSequence) to (Place, Transition) {
   (Process2PetriNet##TopDown) reference P2PN {
     p@Process[name=n] -> { traitement1 }
     p@Process[name=n] -> { traitement2 }
   }
   
   (WorkDefinition2PetriNet##BottomUp) reference WD2PN {
     wd@WorkDefinition[name=n] -> {
       //started<-`Place[name=n+"_started"];
       String toto = "";
       Place source = `ResolveWorkDefinitionPlace(null,"p_finished");
      }
   }

  (WorkSequence2PetriNet##) ws@WorkSequence(_,_,_,_) -> {
     //Node source = %resolve(started,WD2PN,wd); //problématique : ce n'est pas wd mais wd#i (si plusieurs WD, il faut récupérer le bon)
     Arc(target, source, 1);
   }
}
     /*p@Process[name=n] && n.equal("root") -> { traitement particulier pour le processus root }
     p@Process[name=n] && !n.equal("root") -> { traitement des processus
     normaux }*/

/* DEBUG5 call */

Transfo2 transfo = new Transfo2();
Strategy mytransfo = `SimplePDLToPetriNet(transfo);

/* DEBUG56: test */

  Strategy transformer = `Sequence(
      TopDown(AAAP2PNToToPetriNet()),
      TopDown(AAAWD2PNToToPetriNet()),
      TopDown(AAAWorkSequenceToToPetriNet())
      );
  transformer.visit(p_root, new EcoreContainmentIntrospector());

