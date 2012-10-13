#!/bin/sh

#if [ $1="-nost" ]
#then
#	OUTPUT=sans_sous-typage
#	OPTS=""
#fi
#
#if [ -z $1 ]
#then
#	OUTPUT=avec_sous-typage
#	OPTS=-nt
#fi

#OPTS=""
OPTS="-nt"
#echo "Generating SimplePDL mappings in '$OUTPUT/generated'…"
emf-generate-mappings -cp .:./lib/simplepdlsemantics_updated_1.2.jar $OPTS SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage SimplePDLSemantics.EDMMSimplePDL.EDMMSimplePDLPackage SimplePDLSemantics.SDMMSimplePDL.SDMMSimplePDLPackage SimplePDLSemantics.TM3SimplePDL.TM3SimplePDLPackage

mv SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage.tom DDMMSimplePDLPackage.tom
mv SimplePDLSemantics.EDMMSimplePDL.EDMMSimplePDLPackage.tom EDMMSimplePDLPackage.tom
mv SimplePDLSemantics.SDMMSimplePDL.SDMMSimplePDLPackage.tom SDMMSimplePDLPackage.tom
mv SimplePDLSemantics.TM3SimplePDL.TM3SimplePDLPackage.tom   TM3SimplePDLPackage.tom

#echo "Generating PetriNet mappings in '$OUTPUT/generated'…"
emf-generate-mappings -cp .:./lib/petrinetsemantics_updated_1.2.jar $OPTS petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage petrinetsemantics.EDMMPetriNet.EDMMPetriNetPackage petrinetsemantics.SDMMPetriNet.SDMMPetriNetPackage petrinetsemantics.TM3PetriNet.TM3PetriNetPackage

mv petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.tom DDMMPetriNetPackage.tom
mv petrinetsemantics.EDMMPetriNet.EDMMPetriNetPackage.tom EDMMPetriNetPackage.tom
mv petrinetsemantics.SDMMPetriNet.SDMMPetriNetPackage.tom SDMMPetriNetPackage.tom
mv petrinetsemantics.TM3PetriNet.TM3PetriNetPackage.tom   TM3PetriNetPackage.tom
