String java="java";

/* DEBUG1 */

%include{ DDMMPetriNetPackage.tom }

/* DEBUG2 */

%include{ DDMMSimplePDLPackage.tom }

/* DEBUG3 */

%include{ sl.tom }

/* DEBUG4 */

%transformation ToPetriNet() with (Process, WorkDefinition, WorkSequence) to (Place, Transition) {
   (Process2PN##TopDown) reference P2PN {
     p@Process[name=n] -> { traitement1 }
     p@Process[name=n] -> { traitement2 }
   }
   
   (WorkDefinition2PN##BottomUp) reference WD2PN {
     wd@WorkDefinition[name=n] -> {
       //started<-`Place[name=n+"_started"];
       String toto = "";
       Place source = `ResolveWorkDefinitionPlace(null,"p_finished");
      }
   }

  (WS2PN##) ws@WorkSequence(_,_,_,_) -> {
     //Node source = %resolve(started,WD2PN,wd); //problématique : ce n'est pas wd mais wd#i (si plusieurs WD, il faut récupérer le bon)
     Arc(target, source, 1);
   }
}
     /*p@Process[name=n] && n.equal("root") -> { traitement particulier pour le processus root }
     p@Process[name=n] && !n.equal("root") -> { traitement des processus
     normaux }*/

/* DEBUG5 */

  Strategy transformer = `Sequence(
      TopDown(AAAP2PNToToPetriNet()),
      TopDown(AAAWD2PNToToPetriNet()),
      TopDown(AAAWorkSequenceToToPetriNet())
      );
  transformer.visit(p_root, new EcoreContainmentIntrospector());

