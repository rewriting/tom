#!/bin/sh

if [ "$1" = "clean" ]
then
	rm mappings/*
else
	cd bin

	echo "Generating Ecore metamodel mapping…"
	java TomMappingFromEcore org.eclipse.emf.ecore.EcorePackage > ../mappings/ecore.tom

	echo "Generating mapping for LigneProdutisTelephones example…"
	java -cp "/home/jcb/workspace/eclipseProjects/LigneProduitsTelephones/bin/:$CLASSPATH" TomMappingFromEcore ligneproduitstelephones.LigneproduitstelephonesPackage > ../mappings/ligneproduitstelephonesmapping.tom

	echo "Generating xmi for LigneProdutisTelephones example…"
	java -cp "/home/jcb/workspace/eclipseProjects/LigneProduitsTelephones/bin/:$CLASSPATH" EcoreMappingToXMI ligneproduitstelephones.LigneproduitstelephonesPackage > ../mappings/ligneproduitstelephones.xmi
fi
