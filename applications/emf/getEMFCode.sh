#!/bin/sh

export ECLIPSE_WORKSPACE="/home/jcb/workspace/eclipseProjects"

cp -R ${ECLIPSE_WORKSPACE}/LigneProduitsTelephones/src/ligneproduitstelephones examples/1lignedeproduits

cp mappings/ligneproduitstelephonesmapping.tom examples/1lignedeproduits
cp bin/EcoreContainmentIntrospector.class examples/1lignedeproduits

