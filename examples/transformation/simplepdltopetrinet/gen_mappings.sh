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

mv SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage.tom mappings/DDMMSimplePDLPackage.tom
mv SimplePDLSemantics.EDMMSimplePDL.EDMMSimplePDLPackage.tom mappings/EDMMSimplePDLPackage.tom
mv SimplePDLSemantics.SDMMSimplePDL.SDMMSimplePDLPackage.tom mappings/SDMMSimplePDLPackage.tom
mv SimplePDLSemantics.TM3SimplePDL.TM3SimplePDLPackage.tom   mappings/TM3SimplePDLPackage.tom

#echo "Generating PetriNet mappings in '$OUTPUT/generated'…"
emf-generate-mappings -cp .:./lib/petrinetsemantics_updated_1.2.jar $OPTS petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage petrinetsemantics.EDMMPetriNet.EDMMPetriNetPackage petrinetsemantics.SDMMPetriNet.SDMMPetriNetPackage petrinetsemantics.TM3PetriNet.TM3PetriNetPackage

mv petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.tom mappings/DDMMPetriNetPackage.tom
mv petrinetsemantics.EDMMPetriNet.EDMMPetriNetPackage.tom mappings/EDMMPetriNetPackage.tom
mv petrinetsemantics.SDMMPetriNet.SDMMPetriNetPackage.tom mappings/SDMMPetriNetPackage.tom
mv petrinetsemantics.TM3PetriNet.TM3PetriNetPackage.tom   mappings/TM3PetriNetPackage.tom
